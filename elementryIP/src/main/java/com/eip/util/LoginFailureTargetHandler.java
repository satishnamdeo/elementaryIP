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
import org.springframework.mail.MailSender;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Controller;

@Controller
public class LoginFailureTargetHandler extends	SimpleUrlAuthenticationFailureHandler {
	public static String SPRING_SECURITY_SWITCH_USERNAME_KEY = "j_username";
	public static String SPRING_SECURITY_SWITCH_PASSWORD_KEY = "j_password";
	public static String LOGIN_ATTEMP = "LOGIN_ATTEMP";

	
	
    
	
	final private String defaultTargetUrl = "/login.html";

	public LoginFailureTargetHandler() {
		super("/login.html");

	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		super.onAuthenticationFailure(request, response, exception);
	}
}
