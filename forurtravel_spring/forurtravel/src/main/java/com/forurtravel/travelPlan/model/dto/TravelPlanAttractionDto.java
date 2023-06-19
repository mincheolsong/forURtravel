package com.forurtravel.travelPlan.model.dto;

public class TravelPlanAttractionDto {
	private String title;
	private String addr;	
	private int planAttractionNo;
	private int attractionNo;
	private int planNo;
	private String startTime;
	private String endTime;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getPlanAttractionNo() {
		return planAttractionNo;
	}
	public void setPlanAttractionNo(int planAttractionNo) {
		this.planAttractionNo = planAttractionNo;
	}
	public int getAttractionNo() {
		return attractionNo;
	}
	public void setAttractionNo(int attractionNo) {
		this.attractionNo = attractionNo;
	}
	public int getPlanNo() {
		return planNo;
	}
	public void setPlanNo(int planNo) {
		this.planNo = planNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "TravelPlanAttractionDto [title=" + title + ", addr=" + addr + ", planAttractionNo=" + planAttractionNo
				+ ", attractionNo=" + attractionNo + ", planNo=" + planNo + ", startTime=" + startTime + ", endTime="
				+ endTime + "]";
	}
	
}
