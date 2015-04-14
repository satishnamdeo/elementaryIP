package com.eip.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eip.domain.PasswordDo;
import com.eip.domain.UserDo;

public interface UserController {
	
	/*String createUserForm(ModelMap map,RedirectAttributes redirectAttributes);*/

	JSONObject addUser(UserDo userDo,RedirectAttributes redirectAttributes);

	JSONObject forgotPassword(String email,RedirectAttributes redirectAttributes);

	JSONObject updatePassword(PasswordDo passwordDo,HttpServletRequest request);


}
