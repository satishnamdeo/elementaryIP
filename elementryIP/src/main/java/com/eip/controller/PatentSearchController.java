package com.eip.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import com.eip.domain.PatentDo;


public interface PatentSearchController {
	
	JSONArray searchPatent(String searchStr,boolean fromCookie);
	
	public String searchNewPatent(String searchQueryStr,ModelMap map);
	
	String viewPatent(String searchQueryStr,ModelMap map);
	
	String viewHistory(ModelMap map);
	
	JSONObject updatePatentIndex(PatentDo patentDo);

	JSONObject editPatentSegment(PatentDo patentDo);
	
	JSONObject deletePatentSegment(PatentDo patentDo);
}
