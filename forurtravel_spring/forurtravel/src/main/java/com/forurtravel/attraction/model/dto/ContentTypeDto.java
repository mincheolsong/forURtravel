package com.forurtravel.attraction.model.dto;

public class ContentTypeDto {
	private int contentTypeId;
	private String contentTypeName;
	public int getContentTypeId() {
		return contentTypeId;
	}
	public void setContentTypeId(int contentTypeId) {
		this.contentTypeId = contentTypeId;
	}
	public String getContentTypeName() {
		return contentTypeName;
	}
	public void setContentTypeName(String contentTypeName) {
		this.contentTypeName = contentTypeName;
	}
	@Override
	public String toString() {
		return "ContentTypeDto [contentTypeId=" + contentTypeId + ", contentTypeName=" + contentTypeName + "]";
	}
	
}
