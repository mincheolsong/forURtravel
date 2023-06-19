package com.forurtravel.boardcomment.model.dto;

import com.forurtravel.user.model.dto.UserDto;

public class BoardcommentDto {
	private int commentNo;
	private int boardNo;
	private int userNo;
	private String content;
	private String createdTime;
	private String modifiedTime;
	private UserDto user;
	
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public int getCommentNo() {
		return commentNo;
	}
	public void setCommentNo(int commentNo) {
		this.commentNo = commentNo;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	@Override
	public String toString() {
		return "BoardcommentDto [commentNo=" + commentNo + ", boardNo=" + boardNo + ", userNo=" + userNo + ", content="
				+ content + ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime + "]";
	}
	
	
	
}
