package com.bigdata.yls_apply.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
	private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@GetMapping("/login")
	public String login(String username,HttpSession session) {
		System.out.println(username+":"+session);
		session.setAttribute(Constant.USER_NAME, username);
		return username;
	}
}
