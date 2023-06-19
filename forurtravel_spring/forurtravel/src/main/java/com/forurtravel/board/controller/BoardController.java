package com.forurtravel.board.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;
import com.forurtravel.board.model.service.BoardService;
import com.forurtravel.user.model.service.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/board")
@Api("게시판 컨트롤러  API V1")
@CrossOrigin("*")
public class BoardController {

	private final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.imgPath}")
	private String uploadImgPath;

	private BoardService boardService;
	private JwtService js;
	@Autowired
	public BoardController(BoardService boardService,JwtService js) {
		super();
		this.boardService = boardService;
		this.js=js;
	}
	
	@ApiOperation(value = "글쓰기", notes = "게시판의 글을 작성한다.", response = Map.class)
	@PostMapping(value="/write-text")
	public ResponseEntity<Map<String, Object>> writeText(@RequestBody @ApiParam(value = "글작성시 필요한 정보 (제목,본문,유저번호)", required = true) BoardDto boardDto){
		logger.debug("write boardDto : {}", boardDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			boardDto.setOfile(null);
			boardDto.setSfile(null);
			boardDto.setSfolder(null);
			boardService.write(boardDto);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "글쓰기", notes = "게시판의 글을 작성한다.", response = Map.class)
	@PostMapping(value="/write-file")
	public ResponseEntity<Map<String, Object>> writeFile(@ModelAttribute @ApiParam(value = "글작성시 필요한 정보 (제목,본문,유저번호,파일)", required = true) BoardDto boardDto){
		logger.debug("write boardDto : {}", boardDto);
		MultipartFile file = boardDto.getFile();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			if (file!=null && !file.isEmpty()) {
				String today = new SimpleDateFormat("yyMMdd").format(new Date());
				String saveFolder = uploadPath + File.separator + today;
				logger.debug("저장 폴더 : {}", saveFolder);
				File folder = new File(saveFolder);
				if (!folder.exists())
					folder.mkdirs();
				String originalFileName = file.getOriginalFilename();
				if (!originalFileName.isEmpty()) {
					String saveFileName = UUID.randomUUID().toString()
							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
					boardDto.setSfolder(today);
					boardDto.setOfile(originalFileName);
					boardDto.setSfile(saveFileName);
					logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", file.getOriginalFilename(), saveFileName);
					file.transferTo(new File(folder, saveFileName));
				}
			}
			boardService.write(boardDto);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error("게시판 쓰기 실패 : {}", e);
			resultMap.put("code", "500");
		}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping(value="/list/{boardType}/{key}/{word}/{listCount}/{curPage}")
	public ResponseEntity<Map<String, Object>> list(@PathVariable int boardType,@PathVariable int key,@PathVariable String word,@PathVariable int listCount,@PathVariable int curPage){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			SearchDto searchDto = new SearchDto();
			searchDto.setBoardType(boardType);
			searchDto.setKey(key);
			searchDto.setWord(word);
			searchDto.setListCount(listCount);
			searchDto.setCurPage(curPage);
			
			List<BoardDto> list = boardService.getList(searchDto);
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
	
	@GetMapping(value="/total-count/{boardType}/{key}/{word}")
	public ResponseEntity<Map<String, Object>> getTotalCount(@PathVariable int boardType,@PathVariable int key,@PathVariable String word){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			SearchDto searchDto = new SearchDto();
			searchDto.setBoardType(boardType);
			searchDto.setKey(key);
			searchDto.setWord(word);
			int totalCount = boardService.getTotalCount(searchDto);
			resultMap.put("totalCount",totalCount);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판목록 조회 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping(value="/detail/{boardNo}")
	public ResponseEntity<Map<String, Object>> detail(@PathVariable("boardNo") int boardNo) throws Exception {
		logger.debug("detail boardNo : {}", boardNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			BoardDto boardDto = boardService.getBoard(boardNo);
			resultMap.put("board",boardDto);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판 조회 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	

	@PutMapping(value="/update")
	public ResponseEntity<Map<String, Object>> update(@RequestBody BoardDto boardDto) throws Exception {
		logger.debug("update boardDto : {}", boardDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			boardService.update(boardDto);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판 갱신 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PutMapping(value="/hit/{boardNo}")
	public ResponseEntity<Map<String, Object>> updateHit(@PathVariable int boardNo) throws Exception {
		logger.debug("update hit boardDto : {}", boardNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			boardService.updateHit(boardNo);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판 갱신 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}


	@DeleteMapping(value="/delete/{boardNo}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("boardNo") int boardNo) throws Exception {
		logger.debug("delete boardNo : {}", boardNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {			
			boardService.delete(boardNo);
			resultMap.put("code", "200");
		}catch (Exception e) {
			logger.error("게시판 삭제 실패 : {}", e);
			resultMap.put("code", "500");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	
}
