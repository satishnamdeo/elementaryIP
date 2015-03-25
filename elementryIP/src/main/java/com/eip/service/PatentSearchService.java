package com.eip.service;

import java.util.List;

import com.eip.domain.PatentDo;
import com.eip.domain.SearchHistoryDo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface PatentSearchService {
	
	public JSONArray searchPatent(String sh,boolean fromCookie);
	
	public JSONObject viewPatent(String searchQueryStr);
	
	public List<SearchHistoryDo> getHistory();
	
	public String addSegment(PatentDo patentDo);
	
	public String updatePatentSegment(PatentDo patentDo);
	
	public String deletePatentSegment(PatentDo patentDo);

}
