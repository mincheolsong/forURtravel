package com.forurtravel.follow.model.dto;

public class FollowDto {
	
	private int followNo;
	private int followerNo;
	private int followingNo;
	
	public int getFollowNo() {
		return followNo;
	}
	public void setFollowNo(int followNo) {
		this.followNo = followNo;
	}
	public int getFollowerNo() {
		return followerNo;
	}
	public void setFollowerNo(int followerNo) {
		this.followerNo = followerNo;
	}
	public int getFollowingNo() {
		return followingNo;
	}
	public void setFollowingNo(int followingNo) {
		this.followingNo = followingNo;
	}
	@Override
	public String toString() {
		return "FollowDto [followNo=" + followNo + ", followerNo=" + followerNo + ", followingNo=" + followingNo + "]";
	}
	
	
	
}
