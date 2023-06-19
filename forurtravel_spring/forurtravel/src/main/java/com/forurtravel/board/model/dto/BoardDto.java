package com.forurtravel.board.model.dto;

import org.springframework.web.multipart.MultipartFile;

import com.forurtravel.user.model.dto.UserDto;

public class BoardDto {

	private int boardNo;
	private String subject;
	private String content;
	private int hit;
	private int userNo;
	private String modifiedTime;
	private String createdTime;
	private MultipartFile file;
	private String ofile;
	private String sfolder;
	private String sfile;
	private UserDto user;
	private int boardType;
	
	public int getBoardType() {
		return boardType;
	}
	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
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
		return "BoardDto [boardNo=" + boardNo + ", subject=" + subject + ", content=" + content + ", hit=" + hit
				+ ", userNo=" + userNo + ", modifiedTime=" + modifiedTime + ", createdTime=" + createdTime + ", file="
				+ file + ", ofile=" + ofile + ", sfolder=" + sfolder + ", sfile=" + sfile + ", user=" + user
				+ ", boardType=" + boardType + "]";
	}
	
	
}
