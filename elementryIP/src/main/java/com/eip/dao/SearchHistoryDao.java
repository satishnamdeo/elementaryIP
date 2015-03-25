package com.eip.dao;

import java.util.List;

import com.eip.entity.SearchHistory;

public interface SearchHistoryDao {
	
	public void addSearchHistory(SearchHistory sh);
	
	public List<SearchHistory> getHistory(Integer userId);

}
