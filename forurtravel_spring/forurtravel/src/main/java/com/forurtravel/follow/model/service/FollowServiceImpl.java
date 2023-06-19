package com.forurtravel.follow.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.forurtravel.follow.model.dto.FollowDto;
import com.forurtravel.follow.model.mapper.FollowMapper;

@Service
public class FollowServiceImpl implements FollowService {

	private FollowMapper followMapper;
	
	
	public FollowServiceImpl(FollowMapper followMapper) {
		super();
		this.followMapper = followMapper;
	}

	@Override
	public List<Integer> getFollower(int userNo) throws Exception {
		
		return followMapper.getFollower(userNo);
	}

	@Override
	public List<Integer> getFollowing(int userNo) throws Exception {
		// TODO Auto-generated method stub
		return followMapper.getFollowing(userNo);
	}

	@Override
	public void addFollow(FollowDto followDto) throws Exception {
		followMapper.addFollow(followDto);
	}

	@Override
	public void delete(int followerNo, int followingNo) throws Exception {
		followMapper.delete(followerNo,followingNo);
	}

}
