package com.forurtravel.attraction.model.dto;

public class GugunDto {
	private int gugunCode;
	private int sidoCode;
	private String gugunName;
	public int getGugunCode() {
		return gugunCode;
	}
	public void setGugunCode(int gugunCode) {
		this.gugunCode = gugunCode;
	}
	public int getSidoCode() {
		return sidoCode;
	}
	public void setSidoCode(int sidoCode) {
		this.sidoCode = sidoCode;
	}
	public String getGugunName() {
		return gugunName;
	}
	public void setGugunName(String gugunName) {
		this.gugunName = gugunName;
	}
	@Override
	public String toString() {
		return "GugunDto [gugunCode=" + gugunCode + ", sidoCode=" + sidoCode + ", gugunName=" + gugunName + "]";
	}
}
