package com.forurtravel.attraction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forurtravel.attraction.model.dto.AttractionDto;
import com.forurtravel.attraction.model.dto.AttractionSearchDto;
import com.forurtravel.attraction.model.dto.ContentTypeDto;
import com.forurtravel.attraction.model.dto.SidoDto;
import com.forurtravel.attraction.model.service.AttractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/attraction")
@Api("관광지정보 컨트롤러  API V1")
@CrossOrigin("*")
public class AttractionController {
	private final Logger logger = LoggerFactory.getLogger(AttractionController.class);
	private AttractionService as;

	public AttractionController(AttractionService as) {
		super();
		this.as = as;
	}
	
	@ApiOperation(value = "관광지정보 리스트", notes = "관광지 정보를 조회한다", response = Map.class)
	@GetMapping(value="/list/{contentTypeId}/{sidoCode}/{gugunCode}/{word}")
	public ResponseEntity<Map<String, Object>> getList(@PathVariable @ApiParam(value = "관광지 유형", required = true) int contentTypeId, @PathVariable @ApiParam(value = "시도", required = true) int sidoCode,@PathVariable @ApiParam(value = "구군", required = true) int gugunCode,@PathVariable @ApiParam(value = "검색할 단어", required = true) String word){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			AttractionSearchDto attractionSearchDto = new AttractionSearchDto();
			attractionSearchDto.setWord(word);
			attractionSearchDto.setContentTypeId(contentTypeId);
			attractionSearchDto.setSidoCode(sidoCode);
			attractionSearchDto.setGugunCode(gugunCode);
			List<AttractionDto> list = as.getList(attractionSearchDto);
			resultMap.put("attraction", list);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "관광지 유형 리스트", notes = "관광지 유형 리스트를 조회한다", response = Map.class)
	@GetMapping(value="/content-type")
	public ResponseEntity<Map<String, Object>> getContentTypeList(){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			List<ContentTypeDto> list = as.getContentTypeList();
			resultMap.put("contentType", list);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "시도 리스트", notes = "시도 리스트를 조회한다", response = Map.class)
	@GetMapping(value="/sido")
	public ResponseEntity<Map<String, Object>> getSidoList(){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			List<SidoDto> list = as.getSidoList();
			resultMap.put("sido", list);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "구군 리스트", notes = "구군 리스트를 조회한다", response = Map.class)
	@GetMapping(value="/gugun")
	public ResponseEntity<Map<String, Object>> getGugunList(){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			List<SidoDto> list = as.getGugunList();
			resultMap.put("gugun", list);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}
