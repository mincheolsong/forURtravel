package com.forurtravel.travelReview.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forurtravel.travelReview.model.dto.TravelReviewDto;
import com.forurtravel.travelReview.model.service.TravelReviewService;
import com.forurtravel.user.model.service.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/travel-review")
@Api("여행후기 컨트롤러  API V1")
@CrossOrigin("*")
public class TravelReviewController {

	private final Logger logger = LoggerFactory.getLogger(TravelReviewController.class);
	
	@Value("${file.path}")
	private String uploadPath;

	private TravelReviewService trs;
	private JwtService js;
	
	@Autowired
	public TravelReviewController(TravelReviewService trs, JwtService js) {
		this.trs = trs;
		this.js = js;
	}

	@ApiOperation(value = "후기작성", notes = "여행후기를 작성한다.", response = Map.class)
	@PostMapping(value="/write")
	public ResponseEntity<Map<String, Object>> write(@RequestBody @ApiParam(value = "여행후기 작성시 필요한 정보", required = true) TravelReviewDto travelReviewDto, HttpServletRequest request){
		logger.debug("write travelReviewDto : {}", travelReviewDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("access-token");
		if(!js.checkToken(token)) {
			resultMap.put("code", "401");
			return new ResponseEntity<Map<String, Object>>(resultMap, status); 
		}
		try {
			LinkedHashMap<String,Object> jrdto = js.getUserToken();
			int userNo = (int)jrdto.get("userNo");
			travelReviewDto.setUserNo(userNo);
			trs.write(travelReviewDto);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "후기 읽기", notes = "여행후기를 읽는다.", response = Map.class)
	@GetMapping(value="/detail/{travelReviewNo}")
	public ResponseEntity<Map<String, Object>> readDetail(@PathVariable @ApiParam(value = "여행후기 번호", required = true) int travelReviewNo){
		logger.debug("read travelReviewNo : {}", travelReviewNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			resultMap.put("travelReview",trs.detail(travelReviewNo));
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "관광지여행후기 목록 읽기", notes = "관광지의 여행후기 목록을 읽는다.", response = Map.class)
	@GetMapping(value="/list-attraction/{attractionNo}")
	public ResponseEntity<Map<String, Object>> readListAttraction(@PathVariable @ApiParam(value = "관광지 번호", required = true) int attractionNo){
		logger.debug("read attractionNo : {}", attractionNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			resultMap.put("travelReview",trs.listAttraction(attractionNo));
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "유저여행후기 목록 읽기", notes = "유저의 여행후기 목록을 읽는다.", response = Map.class)
	@GetMapping(value="/list-user/{userNo}")
	public ResponseEntity<Map<String, Object>> readListUser(@PathVariable @ApiParam(value = "유저 번호", required = true) int userNo){
		logger.debug("read userNo : {}", userNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			resultMap.put("travelReview",trs.listUser(userNo));
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "핫플레이스 목록 읽기", notes = "핫플레이스 목록을 읽는다.", response = Map.class)
	@GetMapping(value="/hotplace")
	public ResponseEntity<Map<String, Object>> hotplace(){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			resultMap.put("hotplace",trs.hotplace());
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("핫플레이스 읽기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "핫플레이스 목록 데이터 (워드 클라우드용)", notes = "핫플레이스 목록을 읽는다.", response = Map.class)
	@GetMapping(value="/wordcloud")
	public ResponseEntity<Map<String, Object>> wordcloud(){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			resultMap.put("wordcloud",trs.wordcloud());
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("핫플레이스 읽기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "랜덤 관광지 후기 목록 읽기", notes = "랜덤하게 관광지후기를 읽는다", response = Map.class)
	@GetMapping(value="/randlom")
	public ResponseEntity<Map<String, Object>> randomPlanReview(){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			resultMap.put("planReview",trs.random());
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("핫플레이스 읽기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}
