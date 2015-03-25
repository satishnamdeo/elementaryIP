package com.eip.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eip.dao.UserDao;
import com.eip.domain.PasswordDo;
import com.eip.domain.UserDo;
import com.eip.domain.UserResponse;
import com.eip.entity.RoleMaster;
import com.eip.entity.User;
import com.eip.service.UserService;
import com.eip.util.CustomPasswordEncoder;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private CustomPasswordEncoder passwordEncoder;
	
	@Override
	public List<RoleMaster> getRoleList() {
		List<RoleMaster> roleList = null;
		try{
		roleList = userDao.getRoleList();
		if(roleList.size()>0){
			return roleList ;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null ;
	}


	@Override
	public UserResponse createUser(UserDo userDo) {
		UserResponse response= new UserResponse();
		try{
			User user = new User();
			BeanUtils.copyProperties(user, userDo);
			RoleMaster roleId = userDao.getRoleIdByRoleCode(userDo.getRoleCode());
			user.setRoleId(roleId);
			String  randomPassword = RandomStringUtils.random(10, userDo.getFirstName()+userDo.getLastName());			
			user.setPassword(passwordEncoder.encodePassword(randomPassword, passwordEncoder.getSaltValue()));
			user.setCreationDate(new Date());
			user.setAuthToken(RandomStringUtils.random(10, userDo.getEmail()).toUpperCase());
			userDao.addUser(user);
		    response.setUserId(user.getUserId());
		    response.setFirstName(user.getFirstName());
		    response.setLastName(user.getLastName());
		    response.setEmail(user.getEmail());
		    response.setPassword(randomPassword);
		    response.setRoleCode(user.getRoleId().getRoleCode());
		}catch(Exception exception){
			response.setUserId(0);
			exception.printStackTrace();
			
		}
		return response;
		
	}
	
	@Override
	public String changePassword(User user){
		String  randomPassword = RandomStringUtils.random(10, user.getFirstName()+user.getLastName());			
		user.setPassword(passwordEncoder.encodePassword(randomPassword, passwordEncoder.getSaltValue()));
		userDao.updateUser(user);
		return randomPassword;
	}

	@Override
	public User findUserByEmail(String email){
		return userDao.findUserByEmail(email);
	}
	
	@Override
	public User revertToOldPassword(User user, String oldPassword){
		user.setPassword(oldPassword);
		return userDao.updateUser(user);
	}
	
	@Override
	public void updatePassword(PasswordDo passwordDo) {
		userDao.updatePassword(passwordDo);		
	}


	@Override
	public List<User> getUserList(Integer userId) {
		List<User> userList=userDao.getUserList(userId);
		return userList;
	}
}
