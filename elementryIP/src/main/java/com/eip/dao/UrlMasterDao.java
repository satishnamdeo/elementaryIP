package com.eip.dao;

import java.util.List;

public interface UrlMasterDao {

	List<String> getUrlsForRole(String role);
}
