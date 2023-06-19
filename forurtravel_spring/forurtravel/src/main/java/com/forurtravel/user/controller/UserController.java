package com.forurtravel.user.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.forurtravel.user.model.dto.EmailDto;
import com.forurtravel.user.model.dto.JwtResponseDTO;
import com.forurtravel.user.model.dto.UserDto;
import com.forurtravel.user.model.service.EmailService;
import com.forurtravel.user.model.service.JwtService;
import com.forurtravel.user.model.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
@Api("사용자 컨트롤러  API V1")
@CrossOrigin("*")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${file.path}")
	private String uploadPath;

	@Value("${file.imgPath}")
	private String uploadImgPath;

	private UserService us;
	private JwtService js;
	private EmailService es;

	@Autowired
	public UserController(UserService us, JwtService js,EmailService es) {
		super();
		this.us = us;
		this.js = js;
		this.es = es;
	}
	
	@ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호)", required = true) UserDto userDto) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			UserDto loginUser = us.loginUser(userDto);
			if (loginUser != null) {
				JwtResponseDTO jrdto = new JwtResponseDTO();
				jrdto.setAuthority(loginUser.getAuthority());
				jrdto.setUserId(loginUser.getUserId());
				jrdto.setUserNo(loginUser.getUserNo());
				String accessToken = js.createAccessToken("user", jrdto);// key, data
				String refreshToken = js.createRefreshToken("user", jrdto);// key, data
				us.saveRefreshToken(loginUser.getUserNo(),refreshToken);
				logger.debug("로그인 accessToken 정보 : {}", accessToken);
				logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				resultMap.put("code", "200");
				status = HttpStatus.ACCEPTED;
			} else {
				resultMap.put("code", "401");
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "로그아웃", notes = "로그아웃을 하고, 기존의 토큰을 제거한다.", response = Map.class)
	@PutMapping("/logout")
	public ResponseEntity<Map<String, Object>> logout() {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			LinkedHashMap<String,Object> jrdto = js.getUserToken();
			us.deleRefreshToken((int)jrdto.get("userNo"));
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그아웃 실패 : {}", e);
			resultMap.put("code", "401");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원정보 by id", notes = "id를 이용하여 회원 정보를 반환한다.", response = Map.class)
	@GetMapping("/info/{userId}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable @ApiParam(value = "조회할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {
		logger.debug("userId : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			UserDto userDto = us.getUser(userId);
			resultMap.put("userInfo", userDto);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원정보 by no", notes = "no를 이용하여 회원 정보를 반환한다.", response = Map.class)
	@GetMapping("/info/no/{userNo}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable @ApiParam(value = "조회할 회원의 번호.", required = true) int userNo,
			HttpServletRequest request) {
		logger.debug("userId : {} ", userNo);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			UserDto userDto = us.getUserByNo(userNo);
			resultMap.put("userInfo", userDto);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "아이디중복체크", notes = "아이디를 사용가능한지 확인한다.", response = Map.class)
	@GetMapping("/id-check/{userId}")
	public ResponseEntity<Map<String, Object>> checkId(
			@PathVariable("userId") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {
		logger.debug("userId : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			int res = us.idCheck(userId);
			if (res == 1)
				resultMap.put("code", "401");
			else
				resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원가입", notes = "회원가입을 한다.", response = Map.class)
	@PostMapping("/join-form")
	public ResponseEntity<Map<String, Object>> join(
			@ModelAttribute @ApiParam(value = "회원가입할 회원의 정보", required = true) UserDto userDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		System.out.println(userDto);
		MultipartFile file = userDto.getFile();
		System.out.println(file);
		logger.debug("join user : {} ", userDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		if (!file.isEmpty()) {
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
				userDto.setSfolder(today);
				userDto.setOfile(originalFileName);
				userDto.setSfile(saveFileName);
				logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", file.getOriginalFilename(), saveFileName);
				file.transferTo(new File(folder, saveFileName));
			}
		}

		try {
			us.joinUser(userDto);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원가입", notes = "회원가입을 한다.", response = Map.class)
	@PostMapping("/join-json")
	public ResponseEntity<Map<String, Object>> joinJson(@RequestBody @ApiParam(value = "회원가입할 회원의 정보", required = true) UserDto userDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		logger.debug("join user : {} ", userDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			userDto.setOfile("noimg.png");
			userDto.setSfile("noimg.png");
			userDto.setSfolder("noimg");
			us.joinUser(userDto);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원정보 수정", notes = "회원의 정보를 수정한다.", response = Map.class)
	@PutMapping("/update-image")
	public ResponseEntity<Map<String, Object>> update(
			@ModelAttribute @ApiParam(value = "수정할 회원의 정보", required = true) UserDto userDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		MultipartFile file = userDto.getFile();
		logger.debug("update user : {} ", userDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		if (!file.isEmpty()) {
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
				userDto.setSfolder(today);
				userDto.setOfile(originalFileName);
				userDto.setSfile(saveFileName);
				logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", file.getOriginalFilename(), saveFileName);
				file.transferTo(new File(folder, saveFileName));
			}
		}

		try {
			us.updateUser(userDto);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원정보 수정", notes = "회원의 정보를 수정한다.", response = Map.class)
	@PutMapping("/update")
	public ResponseEntity<Map<String, Object>> updateNoImage(@RequestBody @ApiParam(value = "수정할 회원의 정보", required = true) UserDto userDto){
		logger.debug("update user : {} ", userDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			userDto.setPassword(null);
			userDto.setOfile(null);
			userDto.setSfile(null);
			userDto.setSfolder(null);
			us.updateUser(userDto);
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PutMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody UserDto userDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refresh-token");
		logger.debug("token : {}, memberDto : {}", token, userDto);
		if (js.checkToken(token)) {
			if (token.equals(us.getRefreshToken(userDto.getUserNo()))) {
				JwtResponseDTO jrdto = new JwtResponseDTO();
				jrdto.setAuthority(userDto.getAuthority());
				jrdto.setUserId(userDto.getUserId());
				jrdto.setUserNo(userDto.getUserNo());
				String accessToken = js.createAccessToken("user", jrdto);
				logger.debug("token : {}", accessToken);
				logger.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				resultMap.put("code", "200");
				status = HttpStatus.ACCEPTED;
			}
		} else {
			logger.debug("리프레쉬토큰도 사용불가!!!!!!!");
			resultMap.put("code", "401");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "이메일 인증코드 전송", notes = "전송한 인증코드를 반환한다.", response = Map.class)
	@PostMapping("/sendmail")
	public ResponseEntity<Map<String, Object>> sendMail(@RequestBody EmailDto  emailDto){ 
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			String code = "error";
			UserDto userDto = us.getUser(emailDto.getUserId());
			if(userDto.getEmail().equals(emailDto.getEmail())) {
				code = es.sendMail(emailDto);
			}
			
			if(code.equals("error")) {
				resultMap.put("code","401");
				status = HttpStatus.ACCEPTED;
			}else {
				if(emailDto.getType().equals("register")) resultMap.put("emailCode", code);
				else if(emailDto.getType().equals("findPw")) {
					userDto = new UserDto();
					userDto.setPassword(code);
					userDto.setUserId(emailDto.getUserId());
					userDto.setEmail(emailDto.getEmail());
					System.out.println(userDto);
					us.findPw(userDto);
				}
				status = HttpStatus.ACCEPTED;
				resultMap.put("code", "200");
			}
		}catch(Exception e) {
			resultMap.put("code", "500");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원탈퇴", notes = "회원을 탈퇴한다.", response = Map.class)
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			LinkedHashMap<String,Object> jrdto = js.getUserToken();
			us.deleteUser((int)jrdto.get("userNo"));
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("회원가입 실패 : {}", e);
			resultMap.put("code", "401");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원아이디 검색", notes = "일치하는 모든 회원을 조회한다.", response = Map.class)
	@GetMapping("/search/{userId}")
	public ResponseEntity<?> searchUser(@PathVariable String userId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			resultMap.put("userList", us.searchUser(userId));
			resultMap.put("code", "200");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("회원가입 실패 : {}", e);
			resultMap.put("code", "401");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}
