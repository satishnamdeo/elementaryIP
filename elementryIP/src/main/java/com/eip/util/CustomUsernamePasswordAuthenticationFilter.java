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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.TextEscapeUtils;



public class CustomUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	@SuppressWarnings("unused")
	private static Logger logger = Logger
			.getLogger(CustomUsernamePasswordAuthenticationFilter.class
					.getName());

	public static String SPRING_SECURITY_SWITCH_USERNAME_KEY = "j_username";
	public static String SPRING_SECURITY_SWITCH_PASSWORD_KEY = "j_password";

	@Autowired
	private MailSender mailSender;

	@SuppressWarnings("deprecation")
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {

		super.successfulAuthentication(request, response, authResult);
		String username = request
				.getParameter(SPRING_SECURITY_SWITCH_USERNAME_KEY);
	}

	@SuppressWarnings("unused")
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);
		HttpSession session=request.getSession();
		session.setAttribute("login_error", ApplicationConstant.INVALID_CREDENTIALS);
		

	}

	@SuppressWarnings("deprecation")
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {

		if (true && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: "
							+ request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);

		// Place the last username attempted into HttpSession for views
		HttpSession session = request.getSession(false);

		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute(
					SPRING_SECURITY_LAST_USERNAME_KEY,
					TextEscapeUtils.escapeEntities(username));
		}

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

}