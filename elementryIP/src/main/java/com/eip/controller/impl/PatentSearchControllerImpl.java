package com.eip.controller.impl;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eip.controller.PatentSearchController;
import com.eip.domain.PatentDo;
import com.eip.domain.SearchHistoryDo;
import com.eip.service.PatentSearchService;

@Controller
public class PatentSearchControllerImpl implements PatentSearchController{
	
	@Autowired
	PatentSearchService patentSearchService;
	@Resource
	Properties applicationProperties;
	
	@RequestMapping(value="/searchnewpatent" ,method = { RequestMethod.POST,RequestMethod.GET })
	public String searchNewPatent(@RequestParam("searchQueryStr") String searchQueryStr,ModelMap map){
		if(searchQueryStr!=null && !searchQueryStr.equals("")){
			map.addAttribute("keyword", searchQueryStr);
			map.addAttribute("searchNewPatent", true);
		}	
		return "home";
	}
	
	
	@RequestMapping(value="/searchPatent" ,method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public JSONArray searchPatent(@RequestParam("searchQueryStr") String searchQueryStr,boolean fromCookie){
		JSONArray SearchResult=new JSONArray();
		if(searchQueryStr!=null && !searchQueryStr.equals("")){
			SearchResult=patentSearchService.searchPatent(searchQueryStr,fromCookie);
		}	
		return SearchResult;
	}
	
	
	@RequestMapping(value="{projectId}/{patentId}/viewPatent" , method = { RequestMethod.POST,RequestMethod.GET })
	public String viewPatent(@PathVariable("projectId") String projectId,@PathVariable("patentId") String patentId,ModelMap map){
		JSONObject patent=new JSONObject();
		JSONArray properties=new JSONArray();
		if(patentId!=null && !patentId.equals("")){
			patent=patentSearchService.viewPatent(patentId,projectId);
			map.addAttribute("patent", patent);
			properties.add(applicationProperties.getProperty("patent.custom"));
			properties.add(applicationProperties.getProperty("patent.preamble"));
			properties.add(applicationProperties.getProperty("patent.preambleAttribute"));
			properties.add(applicationProperties.getProperty("patent.element"));
			properties.add(applicationProperties.getProperty("patent.elementAttribute"));
			properties.add(applicationProperties.getProperty("patent.feedback"));
			properties.add(applicationProperties.getProperty("patent.field7"));
			properties.add(applicationProperties.getProperty("patent.field8"));
			map.addAttribute("claims_properties", properties);
		}	
		return "detail";
	}
	
	
	@RequestMapping(value="/view_history" ,method = { RequestMethod.POST,RequestMethod.GET })
	public String viewHistory(ModelMap map){
			List<SearchHistoryDo> li=patentSearchService.getHistory();
			map.addAttribute("historyList", li);
		return "history";
	}
	
	@Override
	@RequestMapping(value="/updatepatent",method =RequestMethod.POST)
	public @ResponseBody JSONObject updatePatentIndex(@RequestBody PatentDo patentDo) {
		JSONObject response=new JSONObject();
		String result=patentSearchService.addSegment(patentDo);
		response.put("status", result);
		return response;
	}
	
	@Override
	@RequestMapping(value="/editpatent_segment",method =RequestMethod.POST)
	public @ResponseBody JSONObject editPatentSegment(@RequestBody PatentDo patentDo) {
		JSONObject response=new JSONObject();
		String result=patentSearchService.updatePatentSegment(patentDo);
		response.put("status", result);
		return response;
	}


	@Override
	@RequestMapping(value="/deletepatent_segment",method =RequestMethod.POST)
	public @ResponseBody JSONObject deletePatentSegment(@RequestBody PatentDo patentDo) {
		JSONObject response=new JSONObject();
		String result=patentSearchService.deletePatentSegment(patentDo);
		response.put("status", result);
		return response;
	}
	
}
