package com.forurtravel.follow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.follow.model.dto.FollowDto;
import com.forurtravel.follow.model.service.FollowService;
import com.forurtravel.user.model.dto.EmailDto;
import com.forurtravel.user.model.dto.UserDto;
import com.forurtravel.user.model.service.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/follow")
@Api("팔로우 컨트롤러  API V1")
@CrossOrigin("*")

public class FollowController {

	private final Logger logger = LoggerFactory.getLogger(FollowController.class);
	
	private FollowService fs;
	private JwtService js;
	
	
	@Autowired
	public FollowController(FollowService fs, JwtService js) {
		super();
		this.fs = fs;
		this.js = js;
	}

	@ApiOperation(value = "팔로워", notes = "팔로워(userNo)를 반환한다.", response = Map.class)
	@GetMapping("/follower/{userNo}")
	public ResponseEntity<Map<String, Object>> getFollower(
			@PathVariable @ApiParam(value = "팔로워를 얻고자 하는 회원번호.", required = true) int userNo,
			HttpServletRequest request) {
		logger.debug("userNo : {} ", userNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			List<Integer> list = fs.getFollower(userNo);
			resultMap.put("follower", list);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "팔로잉", notes = "팔로잉을(userNo) 반환한다.", response = Map.class)
	@GetMapping("/following/{userNo}")
	public ResponseEntity<Map<String, Object>> getFollowing(
			@PathVariable @ApiParam(value = "팔로잉를 얻고자 하는 회원번호.", required = true) int userNo,
			HttpServletRequest request) {
		logger.debug("userNo : {} ", userNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			List<Integer> list = fs.getFollowing(userNo);
			resultMap.put("following", list);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "팔로우", notes = "사용자 간 팔로우 동작 수행", response = Map.class)
	@PostMapping("/add")
	public ResponseEntity<Map<String, Object>> sendMail(@RequestBody FollowDto  followDto){ 
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		try {
			fs.addFollow(followDto);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("팔로우 등록 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@DeleteMapping(value="/delete/{followerNo}/{followingNo}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("followerNo") int followerNo,@PathVariable("followingNo") int followingNo) throws Exception {
		logger.debug("delete follow : {}", followerNo, followingNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			fs.delete(followerNo,followingNo);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("팔루워 삭제 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	
}
