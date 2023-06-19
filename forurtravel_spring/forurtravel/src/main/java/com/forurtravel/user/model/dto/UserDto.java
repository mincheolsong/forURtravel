package com.forurtravel.user.model.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserDto {
	private int userNo;
	private String userId;
	private String userName;
	private String password;
	private String email;
	private String authority;
	private String salt;
	private String createdTime;
	private String modifiedTime;
	private MultipartFile file;
	private String ofile;
	private String sfolder;
	private String sfile;
	private String token;
	private String refreshToken;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
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
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
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
		return "UserDto [userNo=" + userNo + ", userId=" + userId + ", userName=" + userName + ", password=" + password
				+ ", email=" + email + ", authority=" + authority + ", salt=" + salt + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + ", file=" + file + ", ofile=" + ofile + ", sfolder=" + sfolder
				+ ", sfile=" + sfile + ", token=" + token + ", refreshToken=" + refreshToken + "]";
	}
	
	
}
