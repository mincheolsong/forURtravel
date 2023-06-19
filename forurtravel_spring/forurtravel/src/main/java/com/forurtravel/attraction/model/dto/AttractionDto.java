package com.forurtravel.attraction.model.dto;

public class AttractionDto {
	private int contentTypeId;
	private int contentId;
	private String title;
	private String image;
	private double latitude;
	private double longitude;
	private String addr;
	private String description;
	
	public int getContentTypeId() {
		return contentTypeId;
	}
	public void setContentTypeId(int contentTypeId) {
		this.contentTypeId = contentTypeId;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
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
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "AttractionDto [contentTypeId=" + contentTypeId + ", contentId=" + contentId + ", title=" + title
				+ ", image=" + image + ", latitude=" + latitude + ", longitude=" + longitude + ", addr=" + addr
				+ ", description=" + description + "]";
	}
	
	
}
