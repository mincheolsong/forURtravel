package com.forurtravel.user.model.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.stereotype.Service;

import com.forurtravel.user.model.dto.UserDto;
import com.forurtravel.user.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	
	private static final int SALT_SIZE = 16;
	private UserMapper UserMapper;

	public UserServiceImpl(UserMapper UserMapper) {
		super();
		this.UserMapper = UserMapper;
	}

	@Override
	public int idCheck(String userId) throws Exception {
		return UserMapper.idCheck(userId);
	}

	@Override
	public void joinUser(UserDto UserDto) throws Exception {
		String salt = makeSalt();
		UserDto.setPassword(hashing(UserDto.getPassword(),salt));
		UserDto.setSalt(salt);
		UserMapper.joinUser(UserDto);
	}

	@Override
	public UserDto loginUser(UserDto loginUser) throws Exception {
		String userId = loginUser.getUserId();
		String salt = UserMapper.getSalt(userId);
		loginUser.setPassword(hashing(loginUser.getPassword(),salt));
		return UserMapper.loginUser(loginUser);
	}

	@Override
	public UserDto getUser(String userId) throws Exception {
		return UserMapper.getUser(userId);
	}
	
	@Override
	public UserDto getUserByNo(int userNo) throws Exception {
		return UserMapper.getUserByNo(userNo);
	}

	@Override
	public void updateUser(UserDto UserDto) throws Exception {
		String salt = UserMapper.getSalt(UserDto.getUserId());
		UserDto.setPassword(hashing(UserDto.getPassword(),salt));
		UserDto.setSalt(salt);
		UserMapper.updateUser(UserDto);
	}
	
	@Override
	public void deleteUser(int userNo) throws Exception {
		UserMapper.deleteUser(userNo);		
	}
	
	// 비밀번호 해싱
	private String hashing(String password, String salt) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");// SHA-256 해시함수를 이용
		for (int i = 0; i < 10000; i++) {// salt를 합쳐 새로운 해시비밀번호를 생성해 디코딩를 방지
			String temp = password + salt;// 패스워드와 Salt를 합쳐 새로운 문자열 생성
			md.update(temp.getBytes());// temp의 문자열을 해싱하여 md에 저장
			password = byteToString(md.digest());// md객체의 다이제스트를 얻어 password를 갱신
		}
		return password;
	}

	private String byteToString(byte[] temp) {// 바이트 값을 16진수로 변경
		StringBuilder sb = new StringBuilder();
		for (byte a : temp) {
			sb.append(String.format("%02x", a));
		}
		return sb.toString();
	}

	private String makeSalt() throws Exception {// salt값 생성
		SecureRandom random = new SecureRandom();
		byte[] temp = new byte[SALT_SIZE];
		random.nextBytes(temp);

		return byteToString(temp);
	}

	@Override
	public void saveRefreshToken(int userNo, String refreshToken) throws Exception {
		UserMapper.saveRefreshToken(userNo,refreshToken);
	}

	@Override
	public void deleRefreshToken(int userNo) throws Exception {
		UserMapper.deleteRefreshToken(userNo);
	}

	@Override
	public String getRefreshToken(int userNo) throws Exception {
		return UserMapper.getRefreshToken(userNo);
	}

	@Override
	public void findPw(UserDto userDto) throws Exception {
		String userId = userDto.getUserId();
		String salt = UserMapper.getSalt(userId);
		userDto.setPassword(hashing(userDto.getPassword(),salt));
		UserMapper.findPw(userDto);
	}

	@Override
	public List<UserDto> searchUser(String userId) throws Exception {
		return UserMapper.searchUser(userId);
	}


}
