package com.eip.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.eip.domain.UserDo;

public interface HomeController {
	
	public String showLogin(HttpServletRequest request, HttpServletResponse response);
	
	/*String showIndexPage(HttpServletRequest request,HttpServletResponse response);*/
	
	public String runPython(HttpServletRequest request,HttpServletResponse response);
	
	String showHome(HttpServletRequest request, HttpServletResponse response,ModelMap map);

	String accessDenied(HttpServletRequest request, ModelMap map);

	String login(UserDo userDo, BindingResult result,
			HttpServletRequest request, ModelMap map,
			HttpServletResponse response);
}



