package com.eip.dao;

import java.util.List;

import com.eip.domain.PasswordDo;
import com.eip.entity.User;
import com.eip.entity.RoleMaster;
public interface UserDao {
	
	User getUserByName(String userName);
	
	List <RoleMaster>getRoleList();

	String addUser(User user);

	RoleMaster getRoleIdByRoleCode(String roleCode);

	User updateUser(User user);

	User findUserByEmail(String email);

	void updatePassword(PasswordDo passwordDo);
	
	List<User> getUserList(Integer userId);

}
