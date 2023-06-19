package com.forurtravel.user.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.forurtravel.user.model.dto.UserDto;

@Mapper
public interface UserMapper {

	int idCheck(String userId) throws SQLException;
	void joinUser(UserDto UserDto) throws SQLException;
	UserDto loginUser(UserDto loginUser) throws SQLException;
	String getSalt(String userId) throws SQLException;
	UserDto getUser(String userId) throws SQLException;
	UserDto getUserByNo(int userNo) throws SQLException;
	void updateUser(UserDto UserDto) throws SQLException;
	void deleteUser(int userNo) throws SQLException;
	void saveRefreshToken(int userNo, String refreshToken)throws SQLException;
	void deleteRefreshToken(int userNo)throws SQLException;
	String getRefreshToken(int userNo)throws SQLException;
	void findPw(UserDto userDto)throws SQLException;
	List<UserDto> searchUser(String userId)throws SQLException;
	
}
