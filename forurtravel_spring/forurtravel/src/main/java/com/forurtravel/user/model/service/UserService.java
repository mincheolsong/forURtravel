package com.forurtravel.user.model.service;

import java.util.List;

import com.forurtravel.user.model.dto.UserDto;

public interface UserService {

	int idCheck(String userId) throws Exception;
	void joinUser(UserDto UserDto) throws Exception;
	UserDto loginUser(UserDto loginUser) throws Exception;
	void saveRefreshToken(int userNo, String refreshToken) throws Exception;
	void deleRefreshToken(int userNo) throws Exception;
	String getRefreshToken(int userNo) throws Exception;
	UserDto getUser(String userId) throws Exception;
	UserDto getUserByNo(int userNo) throws Exception;
	void findPw(UserDto userDto) throws Exception;
	void updateUser(UserDto UserDto) throws Exception;
	void deleteUser(int userNo) throws Exception;
	List<UserDto> searchUser(String userId) throws Exception;
	
}
