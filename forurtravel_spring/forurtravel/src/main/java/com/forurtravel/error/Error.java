package com.forurtravel.error;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class Error implements ErrorController {
	private final Logger logger = LoggerFactory.getLogger(Error.class);

	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // error로 들어온 에러의 status를 불러온다 (ex:404,500,403...)
        
        if(status != null){
            int statusCode = Integer.valueOf(status.toString());
            
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return new ResponseEntity<String>("404 ERROR! : 페이지를 찾을 수 없습니다.",HttpStatus.NOT_FOUND);
            } else {
            	return new ResponseEntity<String>("에러입니다!",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<String>("에러입니다!",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}