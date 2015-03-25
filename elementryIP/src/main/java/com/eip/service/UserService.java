package com.eip.service;

import java.util.List;

import com.eip.domain.PasswordDo;
import com.eip.domain.UserDo;
import com.eip.domain.UserResponse;
import com.eip.entity.RoleMaster;
import com.eip.entity.User;

public interface UserService {
	
	List <RoleMaster> getRoleList();

	UserResponse createUser(UserDo userDo);

	String changePassword(User user);

	User findUserByEmail(String email);

	User revertToOldPassword(User user, String oldPassword);

	void updatePassword(PasswordDo passwordDo);
	
	List<User> getUserList(Integer userId);

}
