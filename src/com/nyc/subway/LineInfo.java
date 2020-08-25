package com.nyc.subway;

import java.sql.Timestamp;

public class LineInfo {
	
	private String name;
	private String status;
	private Timestamp delayStartTime;
	private Timestamp delayEndTime;
	private Timestamp serviceStartTime;
	private long totalTimeDelayed;
	private long timeDelayed;
	
	public LineInfo(String name, String status, Timestamp serviceStartTime, Timestamp delayStartTime) {
		this.name = name;
		this.status = status;
		this.delayStartTime = delayStartTime;
		this.serviceStartTime = serviceStartTime;
		this.totalTimeDelayed = 0L;
		this.timeDelayed = 0L;
	}
	
	public LineInfo(String name, String status, Timestamp serviceStartTime) {
		this.name = name;
		this.status = status;
		this.serviceStartTime = serviceStartTime;
		this.totalTimeDelayed = 0L;
		this.timeDelayed = 0L;
	}
	
	public LineInfo(String name, String status) {
		this.name = name;
		this.status = status;
		this.delayStartTime = null;
		this.delayEndTime = null;
		this.totalTimeDelayed = 0L;
		this.timeDelayed = 0L;
	}
	
	public LineInfo(String name) {
		this.name = name;
		this.status = "not delayed";
		this.delayStartTime = null;
		this.delayEndTime = null;
		this.totalTimeDelayed = 0L;
		this.timeDelayed = 0L;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Timestamp getDelayStartTime() {
		return delayStartTime;
	}

	public void setDelayStartTime(Timestamp delayStartTime) {
		this.delayStartTime = delayStartTime;
	}

	public Timestamp getDelayEndTime() {
		return delayEndTime;
	}

	public void setDelayEndTime(Timestamp delayEndTime) {
		this.delayEndTime = delayEndTime;
	}

	public Timestamp getServiceStartTime() {
		return serviceStartTime;
	}

	public void setServiceStartTime(Timestamp serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}

	public long getTotalTimeDelayed() {
		return totalTimeDelayed;
	}

	public void setTotalTimeDelayed(long totalTimeDelayed) {
		this.totalTimeDelayed = totalTimeDelayed;
	}

	public long getTimeDelayed() {
		return timeDelayed;
	}

	public void setTimeDelayed(long timeDelayed) {
		this.timeDelayed = timeDelayed;
	}
}
