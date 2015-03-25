package com.eip.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ManageProjectController {
	
	String manageProject(HttpServletRequest request,ModelMap map);
	
	String createProject(HttpServletRequest request,List<Integer> user,String ProjectName,RedirectAttributes redirect);
	
	String deleteProject(Integer projectId,RedirectAttributes redirect);
	
	String getProjectDetail(Integer projectId,ModelMap map);
	
	JSONArray searchProjectPatent(Integer projectId,String searchStr,boolean fromCookie); 
	
    JSONArray externalSearch(String query);

}
