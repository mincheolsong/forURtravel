package com.forurtravel.travelPlan.model.dto;

import java.util.List;

public class TravelPlanlistDto {
	private int planlistNo;
	private int userNo;
	private String title;
    private String image;
	private String content;
	private String createdTime;
	private String modifiedTime;
	private List<TravelPlanDto> plan;
	public int getPlanlistNo() {
		return planlistNo;
	}
	public void setPlanlistNo(int planlistNo) {
		this.planlistNo = planlistNo;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public List<TravelPlanDto> getPlan() {
		return plan;
	}
	public void setPlan(List<TravelPlanDto> plan) {
		this.plan = plan;
	}
	@Override
	public String toString() {
		return "TravelPlanlistDto [planlistNo=" + planlistNo + ", userNo=" + userNo + ", title=" + title + ", image="
				+ image + ", content=" + content + ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime
				+ ", plan=" + plan + "]";
	}
	
	
}
