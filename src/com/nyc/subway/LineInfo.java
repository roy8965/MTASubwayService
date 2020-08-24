package com.nyc.subway;

import java.sql.Timestamp;

public class LineInfo {
	
	private String name;
	private String status;
	private Timestamp delayStartTime;
	private double totalTimeDelayed;
	
	public LineInfo(String name, String status, Timestamp delayStartTime) {
		this.name = name;
		this.status = status;
		this.delayStartTime = delayStartTime;
		this.totalTimeDelayed = 0.0;
	}
	
	public LineInfo(String name, String status) {
		this.name = name;
		this.status = status;
		this.delayStartTime = null;
		this.totalTimeDelayed = 0.0;
	}
	
	public LineInfo(String name) {
		this.name = name;
		this.status = "not delayed";
		this.delayStartTime = null;
		this.totalTimeDelayed = 0.0;
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

	public double getTotalTimeDelayed() {
		return totalTimeDelayed;
	}

	public void setTotalTimeDelayed(double totalTimeDelayed) {
		this.totalTimeDelayed = totalTimeDelayed;
	}
}
