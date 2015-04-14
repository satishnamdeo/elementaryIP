package com.eip.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eip.dao.SearchHistoryDao;
import com.eip.entity.SearchHistory;

@Repository("SearchHistoryDao")
@Transactional
public class SearchHistoryDaoImpl implements SearchHistoryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void addSearchHistory(SearchHistory sh) {
		Session session = sessionFactory.getCurrentSession();
		session.save(sh);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SearchHistory> getHistory(Integer userId) {
		System.out.println(" history dao : ");
		Session session= sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SearchHistory.class);
		List<SearchHistory> historyList = (List<SearchHistory>)criteria.add(Restrictions.eq("userId", userId)).list();
		System.out.println(" history size : "+historyList.size());
		return historyList;
	}

}
