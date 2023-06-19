package com.forurtravel.follow.model.service;

import java.util.List;

import com.forurtravel.follow.model.dto.FollowDto;


public interface FollowService {

	List<Integer> getFollower(int userNo) throws Exception;

	List<Integer> getFollowing(int userNo) throws Exception;

	void addFollow(FollowDto followDto) throws Exception;

	void delete(int followerNo, int followingNo) throws Exception;

	
}
