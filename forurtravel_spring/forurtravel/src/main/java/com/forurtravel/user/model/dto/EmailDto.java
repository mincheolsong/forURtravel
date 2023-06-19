package com.forurtravel.user.model.dto;

public class EmailDto {
	private String userId;
	private String type;
	private String email;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "EmailDto [userId=" + userId + ", type=" + type + ", email=" + email + "]";
	}
	
	
}
