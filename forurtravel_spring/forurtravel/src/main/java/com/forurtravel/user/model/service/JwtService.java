package com.forurtravel.user.model.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.forurtravel.user.model.dto.JwtResponseDTO;

public interface JwtService {

	<T> String createAccessToken(String key, T data);
	<T> String createRefreshToken(String key, T data);
	<T> String create(String key, T data, String subject, long expir);
	Map<String, Object> get(String key);
	boolean checkToken(String jwt);
	LinkedHashMap<String, Object> getUserToken();
	
}
