package com.eip.controller.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.eip.controller.HomeController;
import com.eip.domain.UserDo;
@Controller
public class HomeControllerImpl implements HomeController {
	
	/*@Override
	@RequestMapping(value="/index" , method={RequestMethod.POST,RequestMethod.GET})
	public String showIndexPage(HttpServletRequest request,HttpServletResponse response) {
		return "index";
	}*/
	
	@Override
	@RequestMapping(value="/python" , method={RequestMethod.POST,RequestMethod.GET})
	public String runPython(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("+++++++++++++++++++++++ in home controller  run Python method+++++++++++++++++++++++++");
		try{
		org.python.util.PythonInterpreter python=new org.python.util.PythonInterpreter();
		python.execfile("D:/Python27/test.py");
		}catch(Exception e){
			e.printStackTrace();			
		}
		return "home";
	}

	@Override
	@RequestMapping(value="/login" , method={RequestMethod.POST,RequestMethod.GET})
	public String showLogin(HttpServletRequest request,
			HttpServletResponse response) {
		//System.out.println("+++++++++++++++++++++++ in home controller  login method+++++++++++++++++++++++++");
		return "login";
	}

	@Override
	@RequestMapping(value="/home" , method={RequestMethod.POST,RequestMethod.GET})
	public String showHome(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
	//System.out.println("+++++++++++++++++++++++ in home controller +++++++++++++++++++++++++");
	map.addAttribute("keyword","");
	map.addAttribute("searchNewPatent", false);
		return "home";
	}

	
	@Override
	@RequestMapping("/authorize")
	public String login(
			@ModelAttribute("userDo") UserDo userDo,
			BindingResult result, HttpServletRequest request, ModelMap map,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		return null;
	}
	
	@Override
	@RequestMapping("/accessdenied")
	public String accessDenied(HttpServletRequest request, ModelMap map) {
		return "accessdenied";
	}
}
