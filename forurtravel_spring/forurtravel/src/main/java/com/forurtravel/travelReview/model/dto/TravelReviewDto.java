package com.forurtravel.travelReview.model.dto;

public class TravelReviewDto {
	private int travelReviewNo;
	private String title;
	private String startDay;
	private String endDay;
	private String review;
	private String createTime;
	private String modifiedTime;
	private int attractionNo;
	private int userNo;
	private int money;
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getTravelReviewNo() {
		return travelReviewNo;
	}
	public void setTravelReviewNo(int travelReviewNo) {
		this.travelReviewNo = travelReviewNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public int getAttractionNo() {
		return attractionNo;
	}
	public void setAttractionNo(int attractionNo) {
		this.attractionNo = attractionNo;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	@Override
	public String toString() {
		return "TravelReviewDto [travelReviewNo=" + travelReviewNo + ", title=" + title + ", startDay=" + startDay
				+ ", endDay=" + endDay + ", review=" + review + ", createTime=" + createTime + ", modifiedTime="
				+ modifiedTime + ", attractionNo=" + attractionNo + ", userNo=" + userNo + ", money=" + money + "]";
	}
	
	
}
