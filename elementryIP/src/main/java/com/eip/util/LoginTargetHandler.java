/*
 *
 * @date Nov 22, 2012
 *
 * The contents of this file are copyrighted by iLike Technologies Limited, UK. 
 * The contents of this file represents the real and intellectual property of iLike Technologies Limited, UK
 * Any source code, configuration parameters, documentation, 
 * data or database schema may not be copied, modified, 
 * reused or distributed without the written consent of iLike Technologies Limited, UK.
 *
 */
package com.eip.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.eip.dao.UserDao;
import com.eip.entity.User;


public class LoginTargetHandler implements AuthenticationSuccessHandler {
	public static String SPRING_SECURITY_SWITCH_USERNAME_KEY = "j_username";
	public static String SPRING_SECURITY_SWITCH_PASSWORD_KEY = "j_password";

	final private String defaultTargetUrl = "/home.html";
	
	@Autowired
	private UserDao userDao;

	public LoginTargetHandler() {

	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		//System.out.println("###################### onAuthenticationSuccess #############################");
		String userName = request
				.getParameter(SPRING_SECURITY_SWITCH_USERNAME_KEY);
		User user=userDao.getUserByName(userName);
		//System.out.println("after get user from dao");
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		response.sendRedirect(request.getContextPath() + defaultTargetUrl);
	}
}
