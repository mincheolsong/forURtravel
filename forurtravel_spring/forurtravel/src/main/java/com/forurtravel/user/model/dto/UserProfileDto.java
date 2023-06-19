package com.forurtravel.user.model.dto;

public class UserProfileDto {
	private int userNo;
	private String userId;
	private String userName;
	private String email;
	private String authority;
	private String createdTime;
	private String modifiedTime;
	private String ofile;
	private String sfolder;
	private String sfile;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getOfile() {
		return ofile;
	}
	public void setOfile(String ofile) {
		this.ofile = ofile;
	}
	public String getSfolder() {
		return sfolder;
	}
	public void setSfolder(String sfolder) {
		this.sfolder = sfolder;
	}
	public String getSfile() {
		return sfile;
	}
	public void setSfile(String sfile) {
		this.sfile = sfile;
	}
	@Override
	public String toString() {
		return "UserProfileDto [userNo=" + userNo + ", userId=" + userId + ", userName=" + userName + ", email=" + email
				+ ", authority=" + authority + ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime
				+ ", ofile=" + ofile + ", sfolder=" + sfolder + ", sfile=" + sfile + "]";
	}
	
}
