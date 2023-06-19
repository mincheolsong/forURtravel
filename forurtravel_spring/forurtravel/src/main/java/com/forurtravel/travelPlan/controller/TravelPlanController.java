package com.forurtravel.travelPlan.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forurtravel.travelPlan.model.service.TravelPlanService;
import com.forurtravel.user.model.service.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/travel-plan")
@Api("여행계획 컨트롤러  API V1")
@CrossOrigin("*")
public class TravelPlanController {
	private final Logger logger = LoggerFactory.getLogger(TravelPlanController.class);
	
	private TravelPlanService tps;
	private JwtService js;
	
	@Autowired
	public TravelPlanController(TravelPlanService tps, JwtService js) {
		super();
		this.tps = tps;
		this.js = js;
	}
	@ApiOperation(value = "여행계획 쓰기", notes = "여행계획을 작성한다", response = Map.class)
	@PostMapping("/write")
	public ResponseEntity<Map<String, Object>> write(@RequestBody @ApiParam(value = "여행계획작성시 필요한 정보", required = true) Map<String,Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			System.out.println(map);
			LinkedHashMap<String,Object> jrdto = js.getUserToken();
			map.put("userNo", (int)jrdto.get("userNo"));
			tps.write(map);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "여행계획 읽기", notes = "여행계획을 반환한다", response = Map.class)
	@GetMapping("/planlist/{planlistNo}")
	public ResponseEntity<Map<String, Object>> readList(@PathVariable @ApiParam(value = "여행계획 번호", required = true) int planlistNo) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			resultMap.put("code", "200");
			resultMap.put("planlist",tps.readList(planlistNo));
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "여행계획 미리보기", notes = "여행계획 미리보기 정보를 반환한다", response = Map.class)
	@GetMapping("/planlist-preview/{userNo}")
	public ResponseEntity<Map<String, Object>> readPreview(@PathVariable @ApiParam(value = "여행계획 상용자 번호", required = true) int userNo) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			resultMap.put("code", "200");
			resultMap.put("planlist",tps.readListPreview(userNo));
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}
