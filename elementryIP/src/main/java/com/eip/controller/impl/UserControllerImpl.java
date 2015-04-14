package com.eip.controller.impl;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eip.controller.UserController;
import com.eip.domain.PasswordDo;
import com.eip.domain.UserDo;
import com.eip.domain.UserResponse;
import com.eip.entity.RoleMaster;
import com.eip.entity.User;
import com.eip.service.MailService;
import com.eip.service.UserService;
import com.eip.util.ApplicationConstant;
@Controller
public class UserControllerImpl implements UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService mailService;
	
	/*@Override
	@RequestMapping(value="/createuser" , method=RequestMethod.GET)
	public String createUserForm(ModelMap map,RedirectAttributes redirectAttributes) {
		try{
			List<RoleMaster> roleList = userService.getRoleList();
			if (roleList != null) {
				map.addAttribute("roleList", roleList);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			redirectAttributes.addFlashAttribute(ApplicationConstant.ERROR_MESSAGE_KEY,ApplicationConstant.ERROR_NO_ROLE_AVAILABLE);
		}
		return "createuser";
	}
*/
	@Override
	@RequestMapping(value="/adduser",method = RequestMethod.POST)
	public @ResponseBody JSONObject addUser(@RequestBody UserDo userDo,RedirectAttributes redirectAttributes) {
		System.out.println("user Form to add user"+userDo.getFirstName());
		JSONObject jsonObject=new JSONObject();
		UserResponse userResponse;
		userResponse= userService.createUser(userDo);
		if(userResponse.getUserId()!= null) {
			try{
				String emailMsgTxt = "<html><body>Hello&nbsp;"
				        + userResponse.getFirstName()
				        + " "
				        + userResponse.getLastName()
				        + ",<br />&nbsp;&nbsp;&nbsp;&nbsp;Congratulation your account successfully created on ElemntryIP and your password is</b>&nbsp;"
				        + userResponse.getPassword()
				        + "<br /><br/>Regards<br/>ElemntryIP Administrator</body></html>";
				
				String fromEmail ="elemntryip@gmail.com";
				String toEmail=userResponse.getEmail();
				mailService.sendMail(toEmail , "Confirmation!", emailMsgTxt);
				jsonObject.put("userId",userResponse.getUserId());
				jsonObject.put("status","success");
			}catch(Exception e){
				e.printStackTrace();
				jsonObject.put("status","failed");
			}
		} else {
			jsonObject.put("status","failed");
			redirectAttributes.addFlashAttribute(ApplicationConstant.ERROR_MESSAGE_KEY,ApplicationConstant.ERROR_CREATE_USER);
			//return "home.html";
		}
		return jsonObject;
	}
	
	@Override
	@RequestMapping(value="/forgotpassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject forgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes){
			User user = userService.findUserByEmail(email);
			JSONObject response=new JSONObject();
			if(user != null){
				try{
					String emailMsgTxt = "<html><body>Hello&nbsp;"
					        + user.getFirstName()
					        + " "
					        + user.getLastName()
					        + ",<br />&nbsp;&nbsp;&nbsp;&nbsp;Your password has been changed successfully your password is</b>&nbsp;"
					        + userService.changePassword(user)
					        + "<br /><br/>Regards<br/>ElemntryIP Administrator</body></html>";
					String fromEmail ="elemntryip@gmail.com";
					String toEmail=user.getEmail();
					mailService.sendMail(toEmail,"Forgot password!",emailMsgTxt);
					response.put("status", "sucess");
				}catch(MessagingException exception){
					userService.revertToOldPassword(user, user.getPassword());
					exception.printStackTrace();
					response.put("status", "failed");
				}catch (MailAuthenticationException e) {
					userService.revertToOldPassword(user, user.getPassword());
					e.printStackTrace();
					response.put("status", "failed");
				}
			}else{
				response.put("status", "failed");
				
			}
			
			return response;
	}
	
	@Override
	@RequestMapping(value="/updatepassword" , method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody JSONObject updatePassword(@RequestBody PasswordDo passwordDo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user =(User)session.getAttribute("user");
		JSONObject response=new JSONObject();
		if(user!=null){			
			passwordDo.setUserId(user.getUserId());
			userService.updatePassword(passwordDo);	
			response.put("message", passwordDo.getMessage());
			response.put("status", "success");
			return response;
			
		}else{
			response.put("message", passwordDo.getMessage());
			response.put("status", "failed");
			return response;
		}
	}

}
