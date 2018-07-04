package com.example.demo.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Object handleIOException(Exception ex) {
		log.error(ex.getMessage());
		String errMsg = "系统异常，请稍后重试。";
		if (ex instanceof UnknownAccountException) {
			errMsg = "用户不存在";
		}
		if (ex instanceof UnauthorizedException) {
			errMsg = "没有权限";
		}
		if (ex instanceof IncorrectCredentialsException) {
			errMsg = "没有权限";
		}
		if (ex instanceof LockedAccountException) {
			errMsg = "用户被锁定";
		}
		if (ex instanceof ExcessiveAttemptsException) {
			errMsg = "用户不存在";
		}
		return errMsg;
	}

}