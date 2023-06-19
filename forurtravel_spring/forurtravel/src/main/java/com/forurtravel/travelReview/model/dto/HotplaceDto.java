package com.forurtravel.travelReview.model.dto;

public class HotplaceDto {
	private int reviewCount;
	private int attractionNo;
	private String title;
	private String addr;
	private String image;
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	public int getAttractionNo() {
		return attractionNo;
	}
	public void setAttractionNo(int attractionNo) {
		this.attractionNo = attractionNo;
	}
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public HotplaceDto(int reviewCount, int attractionNo, String title, String addr, String image) {
		super();
		this.reviewCount = reviewCount;
		this.attractionNo = attractionNo;
		this.title = title;
		this.addr = addr;
		this.image = image;
	}
	
}
