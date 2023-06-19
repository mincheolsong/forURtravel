package com.forurtravel.travelPlan.model.dto;

import java.util.List;

public class TravelPlanDto {
	private int planlistNo;
	private int planNo;
	private int money;
	private String planDate;
	private List<TravelPlanAttractionDto> planAttraction;
	public int getPlanlistNo() {
		return planlistNo;
	}
	public void setPlanlistNo(int planlistNo) {
		this.planlistNo = planlistNo;
	}
	public int getPlanNo() {
		return planNo;
	}
	public void setPlanNo(int planNo) {
		this.planNo = planNo;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public String getPlanDate() {
		return planDate;
	}
	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}
	public List<TravelPlanAttractionDto> getPlanAttraction() {
		return planAttraction;
	}
	public void setPlanAttraction(List<TravelPlanAttractionDto> planAttraction) {
		this.planAttraction = planAttraction;
	}
	@Override
	public String toString() {
		return "TravelPlanDto [planlistNo=" + planlistNo + ", planNo=" + planNo + ", money=" + money + ", planDate="
				+ planDate + ", planAttraction=" + planAttraction + "]";
	}
}
