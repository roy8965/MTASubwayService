package com.nyc.subway;

import java.util.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;  
import org.w3c.dom.Node;   


public class LineUtils {
	/** Service start time */
	public static final Timestamp INCEPTION_TIMESTAMP = new Timestamp(System.currentTimeMillis());
	public static final String MTA_SERVICE_STATUS_URL = "http://web.mta.info/status/serviceStatus.txt";
	private static final String MTA_FORMAT = "MM/dd/yyyy hh:mmaa";
	
	public static Map<String, LineInfo> statusMap = new HashMap<>();
	
	/** XML DOM Parser */
	public static void xmlReader(String pathUrl) {
		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //step 1
            DocumentBuilder builder = factory.newDocumentBuilder(); //step 2
            Document doc = builder.parse(pathUrl); //step 3
            
            Element root = doc.getDocumentElement(); // root <service>  
            if (root == null) return;  

            NodeList propNodes = root.getChildNodes(); 
            
            for (int i = 0; i < propNodes.getLength(); i++) {
            	Node propNode = propNodes.item(i);  
            	if("subway".equals(propNode.getNodeName())) {
            		NodeList lines = propNode.getChildNodes();
            		for (int j = 0; j < lines.getLength(); j++) {
            			Node line = lines.item(j);
            			if("line".equals(line.getNodeName())) {
            				NodeList lineProps = line.getChildNodes();
            				StringBuilder sb = new StringBuilder();
            				for (int k = 0; k < lineProps.getLength(); k++) {
            					Node lineProp = lineProps.item(k);
            					if("name".equals(lineProp.getNodeName())) {
            						Node name = lineProp.getFirstChild();
            						sb.append(name.getNodeValue() + "_");
            					}
            					if("status".equals(lineProp.getNodeName())) {
            						Node status = lineProp.getFirstChild();
            						sb.append(status.getNodeValue() + "_");
            					}
            					if("Date".equals(lineProp.getNodeName())) {
            						Node date = lineProp.getFirstChild();
            						sb.append(date.getNodeValue() + "_");
            					}
            					if("Time".equals(lineProp.getNodeName())) {
            						Node time = lineProp.getFirstChild();
            						sb.append(time.getNodeValue());
            					}
            				}
            				updateLineStatus(sb.toString());
            			}
            		}
            	}	
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void updateLineStatus(String info) {
		if(info == null || info.isEmpty()) return;
		String[] props = info.split("_");
		
		String name = props[0].trim();
		
		if("SIR".equals(name)) {
			updateLineStatus(name, props);
		} else {
			for(int i = 0; i < name.length(); i++) {
				updateLineStatus(String.valueOf(name.charAt(i)), props);
			}
		}
	}
	
	private static void updateLineStatus(String name, String[] props) {
		String status = parseLineStatus(props[1]).trim();
		String curDate = props[2].trim() + " " + props[3].trim();         
		SimpleDateFormat dateFormat = new SimpleDateFormat(LineUtils.MTA_FORMAT);
		Timestamp delayStartTime = null;
		
		try {
			Date parsedDate = dateFormat.parse(curDate);
			delayStartTime = new Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		if(!statusMap.containsKey(name)) {
			LineInfo lineInfo = new LineInfo(name, status, curTimestamp);
			if("delayed".equals(status)) {
				lineInfo.setDelayStartTime(curTimestamp);
			} 
			statusMap.put(name, lineInfo);
		} else {
			LineInfo lineInfo = statusMap.get(name);
			if(lineInfo == null) lineInfo = new LineInfo(name, status, curTimestamp);
			
			if(!lineInfo.getStatus().equals(status)) {
				if("delayed".equals(status)) { // not delayed -> delay
					lineInfo.setDelayStartTime(curTimestamp);
					lineInfo.setStatus(status);
				} else { // delay -> not delayed
					long totalTimeDelayed = lineInfo.getTotalTimeDelayed();
					delayStartTime = lineInfo.getDelayStartTime() == null ? curTimestamp : lineInfo.getDelayStartTime();
					totalTimeDelayed += curTimestamp.getTime() - delayStartTime.getTime();
					lineInfo.setTotalTimeDelayed(totalTimeDelayed);
					lineInfo.setDelayStartTime(null);
					lineInfo.setStatus(status);
				}
				lineStatusChangeAlert(name, status);
			} else {
				if("delayed".equals(status)) { // delay -> delay
					long totalTimeDelayed = lineInfo.getTotalTimeDelayed();
					if(lineInfo.getDelayStartTime() == null) lineInfo.setDelayStartTime(LineUtils.INCEPTION_TIMESTAMP);
					delayStartTime = lineInfo.getDelayStartTime();
					long timeDelayed = totalTimeDelayed + (curTimestamp.getTime() - delayStartTime.getTime());
					lineInfo.setTimeDelayed(timeDelayed);
				}
			}
			statusMap.put(name, lineInfo);
		}
	}
	
	public static String getUptime(String line) {
		HttpUrlConnectionToMTA.doPostOrGet(LineUtils.MTA_SERVICE_STATUS_URL, "");
		if(!statusMap.containsKey(line)) return "N/A";
		double timeDelayed = statusMap.get(line).getTimeDelayed();
		long serviceStartTime = statusMap.get(line).getServiceStartTime().getTime();
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		double totalTime = curTimestamp.getTime() - serviceStartTime;
		double delayedPercentage = totalTime == 0 ? 100 : (1 - timeDelayed / totalTime) * 100.00;
		String delayedPercentageStr = String.format("%.2f", delayedPercentage) + "%";
		return delayedPercentageStr;
	}
	
	public static String getLineStatus(String line) {
		HttpUrlConnectionToMTA.doPostOrGet(LineUtils.MTA_SERVICE_STATUS_URL, "");
		if(!statusMap.containsKey(line)) return "N/A";
		String status = statusMap.get(line).getStatus();
		return status;
	}
	
	public static void lineStatusChangeAlert(String line, String status) {
		if("not delayed".equals(status)) {
			System.out.println("Line " + line + " is now recovered.");
		} else { // delayed
			System.out.println("Line " + line + " is experiencing delays.");
		}
	}
	
	public static String parseLineStatus(String statusInfo) {
		if(statusInfo == null || statusInfo.isEmpty()) return "N/A";
		String status = "N/A";
		
		switch(statusInfo) {
		case "PLANNED WORK": 
			status = "not delayed";
			break;
		case "WEEKEND SERVICE": 
			status = "not delayed";
			break;
		case "WEEKDAY SERVICE": 
			status = "not delayed";
			break;
		case "NO SCHEDULED SERVICE": 
			status = "not delayed";
			break;
		case "EXPRESS TO LOCAL": 
			status = "delayed";
			break;
		case "SLOW SPEEDS": 
			status = "delayed";
			break;
		case "SOME REROUTES": 
			status = "delayed";
			break;
		case "SOME DELAYS": 
			status = "delayed";
			break;
		case "DELAYS": 
			status = "delayed";
			break;
		case "DELAYED": 
			status = "delayed";
			break;
		default:
			status = statusInfo;
			break;
		}
		
		return status;
	}
}
