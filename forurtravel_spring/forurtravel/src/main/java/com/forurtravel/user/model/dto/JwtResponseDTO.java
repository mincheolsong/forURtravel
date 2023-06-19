package com.forurtravel.user.model.dto;

public class JwtResponseDTO {
	private int userNo;
	private String userId;
	private String authority;
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	@Override
	public String toString() {
		return "JwtResponseDTO [userNo=" + userNo + ", userId=" + userId + ", authority=" + authority + "]";
	}
	
}
