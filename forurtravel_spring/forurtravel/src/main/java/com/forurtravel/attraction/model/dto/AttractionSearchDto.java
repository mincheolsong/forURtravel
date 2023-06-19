package com.forurtravel.attraction.model.dto;

public class AttractionSearchDto {
	private String word;
	private int contentTypeId;
	private int sidoCode;
	private int gugunCode;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getContentTypeId() {
		return contentTypeId;
	}
	public void setContentTypeId(int contentTypeId) {
		this.contentTypeId = contentTypeId;
	}
	public int getSidoCode() {
		return sidoCode;
	}
	public void setSidoCode(int sidoCode) {
		this.sidoCode = sidoCode;
	}
	public int getGugunCode() {
		return gugunCode;
	}
	public void setGugunCode(int gugunCode) {
		this.gugunCode = gugunCode;
	}
	@Override
	public String toString() {
		return "AttractionSearchDto [word=" + word + ", contentTypeId=" + contentTypeId + ", sidoCode=" + sidoCode
				+ ", gugunCode=" + gugunCode + "]";
	}
	
	
}
