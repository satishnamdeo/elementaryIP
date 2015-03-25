package com.eip.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eip.dao.UrlMasterDao;
import com.eip.entity.RoleMaster;
import com.eip.entity.UrlMaster;


@Repository("UrlMasterDao")
public class UrlMasterDaoImpl implements UrlMasterDao {
	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<String> getUrlsForRole(String role) {
		List<String> urlList=new ArrayList<String>();
		try{
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(RoleMaster.class);
		criteria.add(Restrictions.eq("roleCode", role));
		RoleMaster r=(RoleMaster) criteria.uniqueResult();
		List<UrlMaster> li=r.getUrlMasterList();
		System.out.println("List Size : "+li.size());
		for(UrlMaster u : li){
			urlList.add(u.getUrl());
		}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	        return urlList;
	}

}
