package com.eip.domain;

import java.util.Date;

public class SearchHistoryDo {
	private Integer id;
	private Integer userId;
	private Integer searchCount;
	private Date searchTime;
	private String searchKeyword;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}
	public Date getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(Date searchTime) {
		this.searchTime = searchTime;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	
	

}
