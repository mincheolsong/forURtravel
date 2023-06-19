package com.forurtravel.user.model.service;

import com.forurtravel.user.model.dto.EmailDto;

public interface EmailService {
	String makeCode(int size);
	String makeHtml(String type, String code);
	String sendMail(EmailDto emailDto);
}
