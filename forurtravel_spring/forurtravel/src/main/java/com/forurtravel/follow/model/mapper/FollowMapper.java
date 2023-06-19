package com.forurtravel.follow.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.forurtravel.follow.model.dto.FollowDto;

@Mapper
public interface FollowMapper {

	List<Integer> getFollower(int userId);

	List<Integer> getFollowing(int userId);

	void addFollow(FollowDto followDto);

	void delete(int followerNo, int followingNo);


}
