package com.forurtravel.boardcomment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;
import com.forurtravel.boardcomment.model.dto.BoardcommentDto;
import com.forurtravel.boardcomment.model.service.BoardcommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/boardcomment")
@Api("게시판 댓글 컨트롤러  API V1")
@CrossOrigin("*")
public class BoardcommentController {

	private final Logger logger = LoggerFactory.getLogger(BoardcommentController.class);

	private BoardcommentService bs;
	
	@Autowired
	public BoardcommentController(BoardcommentService bs) {
		this.bs = bs;
	}

	@ApiOperation(value = "글쓰기", notes = "댓글을 작성한다.", response = Map.class)
	@PostMapping(value="/write")
	public ResponseEntity<Map<String, Object>> write(@RequestBody @ApiParam(value = "댓글작성시 필요한 정보 (게시글번호,본문,유저번호)", required = true) BoardcommentDto boardcommentDto){
		logger.debug("write BoardcommentDto : {}", boardcommentDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			bs.write(boardcommentDto);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping(value="/list/{boardNo}")
	public ResponseEntity<Map<String, Object>> list(@PathVariable int boardNo){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {

			System.out.println(boardNo);
			List<BoardDto> list = bs.getList(boardNo);
			if(list != null && !list.isEmpty()) {
				resultMap.put("list",list);
				resultMap.put("code", "200");
			} else {
				resultMap.put("code", "401");
			}
		}catch (Exception e) {
			logger.error("게시판목록 조회 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping(value="/total-count/{boardNo}")
	public ResponseEntity<Map<String, Object>> getTotalCount(@PathVariable int boardNo){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			int totalCount = bs.getTotalCount(boardNo);
			resultMap.put("totalCount",totalCount);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판목록 조회 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PutMapping(value="/update")
	public ResponseEntity<Map<String, Object>> update(@RequestBody BoardcommentDto boardcommentDto) throws Exception {
		logger.debug("update boardcommentDto : {}", boardcommentDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			bs.update(boardcommentDto);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판 갱신 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}


	@DeleteMapping(value="/delete/{commentNo}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("commentNo") int commentNo) throws Exception {
		logger.debug("delete commentNo : {}", commentNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			bs.delete(commentNo);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판 삭제 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	
}
