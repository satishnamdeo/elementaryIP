package com.eip.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.eip.dao.UserDao;
import com.eip.domain.PasswordDo;
import com.eip.entity.RoleMaster;
import com.eip.entity.User;
import com.eip.util.CustomPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("UserDao")
@Transactional
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CustomPasswordEncoder passwordEncoder;
	
	@Transactional
	@Override
	public User getUserByName(String userName) {		
		User user = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			//System.out.println("Session : "+session.isOpen()+" "+session.isConnected());
			Criteria crt = session.createCriteria(User.class).add(
					Restrictions.eq("email", userName));
			user = (User) crt.uniqueResult();
			//System.out.println("user : "+user.getFirstName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleMaster> getRoleList() {
		Session session= sessionFactory.getCurrentSession();
		List <RoleMaster> rolelist = null;
		Criteria crit = session.createCriteria(RoleMaster.class);
		rolelist= crit.list();
	    return rolelist;
	}

	@Override
	public String addUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println();
		session.save(user);
		return null;
	}

	@Override
	public RoleMaster getRoleIdByRoleCode(String roleCode) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.getNamedQuery("RoleMaster.findByRoleCode");
		query.setString("roleCode", roleCode);
		RoleMaster roleId = (RoleMaster) query.list().get(0);
		return roleId;
	}
	
	@Override
	public User updateUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.update(user);
		return user;
	}
	
	@Override
	public User findUserByEmail(String email){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		User user = (User)criteria.add(Restrictions.eq("email", email)).uniqueResult();
		Hibernate.initialize(user);
		return user;
	}

	@Transactional
	@Override
	public void updatePassword(PasswordDo passwordDo) {		
		User user = null;
		try {
			Session session = sessionFactory.getCurrentSession();	
			session.beginTransaction();
			user =(User) session.load(User.class, passwordDo.getUserId());
			if(passwordEncoder.isPasswordValid(user.getPassword(), passwordDo.getCurrentPassword(), passwordEncoder.getSaltValue())){				
				user.setPassword(passwordEncoder.encodePassword(passwordDo.getNewPassword(), passwordEncoder.getSaltValue()));
				session.update(user);
				passwordDo.setModified(true);
				passwordDo.setMessage("Paassword changed successfully");
			}else{
				passwordDo.setModified(false);
				passwordDo.setMessage("Current password is not valid. Please enter correct password.");
			}
		} catch (Exception ex) {
			passwordDo.setModified(false);
			passwordDo.setMessage("Password change failed, try again");
			ex.printStackTrace();
		}
		
	}

	@Override
	public List<User> getUserList(Integer userId) {
		Session session = sessionFactory.getCurrentSession();	
		Criteria criteria = session.createCriteria(User.class);
		List<User> userList = (List<User>)criteria.add(Restrictions.ne("userId", userId)).list();
		
		return userList;
	}
	
}
