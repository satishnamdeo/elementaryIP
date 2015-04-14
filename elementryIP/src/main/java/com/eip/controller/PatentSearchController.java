package com.eip.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;

import com.eip.domain.PatentDo;


public interface PatentSearchController {
	
	JSONArray searchPatent(String searchStr,boolean fromCookie);
	
	public String searchNewPatent(String searchQueryStr,ModelMap map);
	
	String viewPatent(String patentId,String projectId,ModelMap map);
	
	String viewHistory(ModelMap map);
	
	JSONObject updatePatentIndex(PatentDo patentDo);

	JSONObject editPatentSegment(PatentDo patentDo);
	
	JSONObject deletePatentSegment(PatentDo patentDo);
}
