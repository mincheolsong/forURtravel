package com.forurtravel.travelReview.model.dto;

import com.forurtravel.attraction.model.dto.AttractionDto;
import com.forurtravel.user.model.dto.UserProfileDto;

public class TravelReviewResultDto {
	private int travelReviewNo;
	private String title;
	private String startDay;
	private String endDay;
	private String review;
	private String createdTime;
	private String modifiedTime;
	private AttractionDto attraction;
	private UserProfileDto user;
	private int money;
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
	public String getcreatedTime() {
		return createdTime;
	}
	public void setcreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public AttractionDto getAttraction() {
		return attraction;
	}
	public void setAttraction(AttractionDto attraction) {
		this.attraction = attraction;
	}
	public UserProfileDto getuser() {
		return user;
	}
	public void setuser(UserProfileDto user) {
		this.user = user;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "TravelReviewResponseDto [travelReviewNo=" + travelReviewNo + ", title=" + title + ", startDay="
				+ startDay + ", endDay=" + endDay + ", review=" + review + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + ", attraction=" + attraction + ", user=" + user + ", money="
				+ money + "]";
	}
	
}
