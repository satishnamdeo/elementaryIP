package com.eip.service.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eip.dao.SearchHistoryDao;
import com.eip.domain.PatentDo;
import com.eip.domain.SearchHistoryDo;
import com.eip.entity.SearchHistory;
import com.eip.entity.User;
import com.eip.service.PatentSearchService;
import com.eip.util.SOLRUtil;
@Service("PatentSearchService")
public class PatentSearchServiceImpl implements PatentSearchService {
	@Autowired
	SearchHistoryDao searchHistoryDao;
	@Resource
	Properties applicationProperties;
	
	@Override
	public JSONArray searchPatent(String searchQueryStr,boolean fromCookie) {
		JSONArray SearchResult=new JSONArray();
		try{
			//indexing start
			//SOLRUtil.indexFile("c:/Solr/patents/SegmentedClaims.xml");
			// indexing end
			//System.out.println("------ "+searchQueryStr);
			
			SolrDocumentList sdList=SOLRUtil.searchInFile(searchQueryStr);
			if(!fromCookie){
				User user=getUserFromSession();
				SearchHistory searchHistory=new SearchHistory();
				searchHistory.setUserId(user.getUserId());
				searchHistory.setSearchCount(sdList.size());
				searchHistory.setSearchTime(new Date());
				searchHistory.setSearchKeyword(searchQueryStr);
				searchHistoryDao.addSearchHistory(searchHistory);
			}
			for(SolrDocument sd:sdList){
				//SearchResult.add(sd);
				JSONObject patent=new JSONObject();
				patent.put("id",(String)sd.getFieldValue(applicationProperties.getProperty("patent.id")));
				patent.put("US_Patent_Number",(String)sd.getFieldValue(applicationProperties.getProperty("patent.patent_Number")));
				patent.put("PublicationDate",(String)sd.getFieldValue(applicationProperties.getProperty("patent.publicationDate")));
				patent.put("FilingDate",(String)sd.getFieldValue(applicationProperties.getProperty("patent.filingDate")));
				patent.put("Title",(String)sd.getFieldValue(applicationProperties.getProperty("patent.title")));
				patent.put("Abstract",(String)sd.getFieldValue(applicationProperties.getProperty("patent.abstract")));
				SearchResult.add(patent);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return SearchResult;
	}
	public JSONObject viewPatent(String searchQueryStr) {
		JSONObject patent=new JSONObject();
		JSONObject claimIndependent=new JSONObject();
		JSONArray claimIndependentList=new JSONArray();
		JSONArray segmentList=new JSONArray();
		SolrInputDocument sd=SOLRUtil.getPatentById(searchQueryStr);
		//System.out.println("total document found : "+sdList.size()+"\n");
		//for(SolrInputDocument sd:sdList){
			Collection<String> keyList=sd.getFieldNames();
			for(String key:keyList){
				//System.out.println(" key : "+key);
				if(key.equals(applicationProperties.getProperty("patent.claims_independent"))){
					Collection<Object> claimsvalueList=sd.getFieldValues(key);
					for(Object obj:claimsvalueList){
						claimIndependent.put("claimIndependent", obj.toString());
						claimIndependentList.add(claimIndependent);
					}
					patent.put("claimIndependentList", claimIndependentList);
				}else if(key.equals(applicationProperties.getProperty("patent.claims_dependent"))){
					patent.put(key, sd.getFieldValues(key));
				}else if(key.equals(applicationProperties.getProperty("patent.field6"))){
					String rawDescriptin=sd.getFieldValue(key).toString();
					String description=rawDescriptin.replace("TAGNEWLINETAG", "<br />");
					patent.put(key,description);
				}else{
					String field=key.substring(key.lastIndexOf('_')+1, key.length());
					JSONObject segment=new JSONObject();
					if(field.equals(applicationProperties.getProperty("patent.custom"))||field.equals(applicationProperties.getProperty("patent.preamble"))||field.equals(applicationProperties.getProperty("patent.preambleAttribute"))||
							field.equals(applicationProperties.getProperty("patent.element"))||field.equals(applicationProperties.getProperty("patent.elementAttribute"))||field.equals(applicationProperties.getProperty("patent.feedback"))){
						String username=key.substring(0,key.indexOf('_'));
						//System.out.println("username : "+username);
						String fieldName1=key.substring(key.indexOf('_')+1);
						String fieldName=fieldName1.substring(fieldName1.indexOf('_')+1);
						String area=fieldName.substring(0,fieldName.indexOf('_')+1);
						Integer startOffset=(Integer) sd.getFieldValue(key+"_startOffset");
						Integer endOffset=(Integer) sd.getFieldValue(key+"_endOffset");
						//ArrayList<String> list = (ArrayList)sd.getFieldValue(key);
						segment.put("username",username);
						segment.put("tagname", field);
						segment.put("area", area);
						segment.put("startOffset", startOffset);
						segment.put("endOffset", endOffset);
						segment.put("quote", sd.getFieldValue(key));
						if(sd.containsKey(key+"_"+applicationProperties.getProperty("patent.comment")))
						segment.put("comment", sd.getFieldValue(key+"_"+applicationProperties.getProperty("patent.comment")));
						else
						segment.put("comment", "Automatically created annotation");	
						segmentList.add(segment);
					}
					if(!key.equals(applicationProperties.getProperty("patent.field6")) &&!key.equals(applicationProperties.getProperty("patent.claims_dependent")) && !field.equals(applicationProperties.getProperty("patent.preamble")) 
							&& !field.equals(applicationProperties.getProperty("patent.preambleAttribute")) && !field.equals(applicationProperties.getProperty("patent.element"))
							&& !field.equals(applicationProperties.getProperty("patent.elementAttribute"))){
						patent.put(key, sd.getFieldValue(key).toString());
					}
				}
			}
		//}
		//System.out.println("segment list : "+segmentList.toString());
		patent.put("segmentList", segmentList);
		return patent;
	}
	
	
	private User getUserFromSession(){
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session= attr.getRequest().getSession(true);
		User user = (User) session.getAttribute("user");
		return user;
	}
	@Override
	public List<SearchHistoryDo> getHistory() {
		List<SearchHistoryDo> history=new ArrayList<SearchHistoryDo>();
		try {
			User user=getUserFromSession();
			List<SearchHistory>list=searchHistoryDao.getHistory(user.getUserId());
			Collections.reverse(list);
			for(SearchHistory histry:list){
				SearchHistoryDo historyDo=new SearchHistoryDo();
				historyDo.setSearchKeyword(histry.getSearchKeyword());
				historyDo.setSearchTime(histry.getSearchTime());
				historyDo.setSearchCount(histry.getSearchCount());
				history.add(historyDo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return history;
	}
	
	
	@Override
	public String addSegment(PatentDo patentDo) { 
		String res="";
		try{
			if(patentDo.getClaimIndependentNo()!=null){
				String keyname=SOLRUtil.updateXML(patentDo);
				SolrInputDocument solrInputDoc=SOLRUtil.getPatentById(patentDo.getPatentId());
				if(solrInputDoc.containsKey(keyname)){
					solrInputDoc.setField(keyname, patentDo.getKeyvalue());
					solrInputDoc.setField(keyname+"_startOffset", patentDo.getStartOffset());
					solrInputDoc.setField(keyname+"_endOffset", patentDo.getEndOffset());
					solrInputDoc.setField(keyname+"_"+applicationProperties.getProperty("patent.comment"), patentDo.getComment());
				}else{
					solrInputDoc.addField(keyname, patentDo.getKeyvalue());
					solrInputDoc.addField(keyname+"_startOffset", patentDo.getStartOffset());
					solrInputDoc.addField(keyname+"_endOffset", patentDo.getEndOffset());
					solrInputDoc.addField(keyname+"_"+applicationProperties.getProperty("patent.comment"), patentDo.getComment());
				}
				
				SOLRUtil.updatePatentIndex(solrInputDoc);
				res="success";
			}
		}catch(Exception ex){
			res="failed";
			ex.printStackTrace();
		}
		return res;
	}
	@Override
	public String updatePatentSegment(PatentDo patentDo) {
		String response="";
		//System.out.println("claimNo : "+patentDo.getClaimIndependentNo()+"   key : "+patentDo.getKeyname()+"  value : "+patentDo.getKeyvalue());
		try{
			if(patentDo.getKeyvalue()!=null){
				SolrInputDocument solrInputDoc=SOLRUtil.getPatentById(patentDo.getPatentId());
					Collection<String> keyList=solrInputDoc.getFieldNames();
					for (String key : keyList) {
						String field=key.substring(key.lastIndexOf('_')+1, key.length());
						//System.out.println("field :"+field+"         ,key : "+key);
						if(field.equals(applicationProperties.getProperty("patent.preamble"))||field.equals(applicationProperties.getProperty("patent.preambleAttribute"))||
								field.equals(applicationProperties.getProperty("patent.element"))||field.equals(applicationProperties.getProperty("patent.elementAttribute"))||
								field.equals(applicationProperties.getProperty("patent.feedback"))){
							if(solrInputDoc.getFieldValue(key).equals(patentDo.getKeyvalue())){
								//System.out.println("solrInputDoc.getFieldValue(key).equals(patentDo.getKeyvalue()) : "+solrInputDoc.getFieldValue(key).equals(patentDo.getKeyvalue()));
								if(patentDo.getKeyname().equals(patentDo.getOldTagName())){
									if(solrInputDoc.containsKey(key+"_"+applicationProperties.getProperty("patent.comment"))){
										solrInputDoc.setField(key+"_"+applicationProperties.getProperty("patent.comment"), patentDo.getComment());
									}else{
										solrInputDoc.addField(key+"_"+applicationProperties.getProperty("patent.comment"), patentDo.getComment());
									}
									patentDo.setKeyname(key+"_"+applicationProperties.getProperty("patent.comment"));
									SOLRUtil.updateCommentXML(patentDo);
									break;
								}else {
									solrInputDoc.removeField(key);
									solrInputDoc.removeField(key+"_startOffset");
									solrInputDoc.removeField(key+"_endOffset");
									if(solrInputDoc.containsKey(key+"_"+applicationProperties.getProperty("patent.comment")))
									solrInputDoc.removeField(key+"_"+applicationProperties.getProperty("patent.comment"));
									patentDo.setOldTagName(key);
									String keyname=SOLRUtil.updateXML(patentDo);
									if(keyname!=null){
										solrInputDoc.addField(keyname, patentDo.getKeyvalue());
										solrInputDoc.addField(keyname+"_startOffset", patentDo.getStartOffset());
										solrInputDoc.addField(keyname+"_endOffset", patentDo.getEndOffset());
										solrInputDoc.addField(keyname+"_"+applicationProperties.getProperty("patent.comment"), patentDo.getComment());
									}
									break;
								}
							}
						}
					}
					SOLRUtil.updatePatentIndex(solrInputDoc);
				//}
			}
		}catch(Exception ex){
			response="failed";
			ex.printStackTrace();
		}
		return response;
	}
	
	
	@Override
	public String deletePatentSegment(PatentDo patentDo) {
		String response="";
		try {
//			System.out.println(" tagname : "+patentDo.getKeyname());
			if(patentDo.getKeyvalue()!=null){
				SolrInputDocument solrInputDoc=SOLRUtil.getPatentById(patentDo.getPatentId());
				Collection<String> keyList=solrInputDoc.getFieldNames();
				for (String key : keyList) {
					//System.out.println("key : "+key);
					String field=key.substring(key.lastIndexOf('_')+1, key.length());
					//System.out.println(" field.equals(patentDo.getKeyname()) && solrInputDoc.getFieldValue(key).equals(patentDo.getKeyvalue()) : "+(field.equals(patentDo.getKeyname()) && solrInputDoc.getFieldValue(key).equals(patentDo.getKeyvalue())));
					if(field.equals(patentDo.getKeyname()) && solrInputDoc.getFieldValue(key).equals(patentDo.getKeyvalue())){
						String username=key.substring(0,key.indexOf('_'));
						//System.out.println("userName of current key : "+key.substring(0,key.indexOf('_')));
						if(solrInputDoc.containsKey(key+"_"+applicationProperties.getProperty("patent.comment"))){
							//System.out.println(patentDo.getCurrentUser()+"    comment exist for the key : "+key);
							//System.out.println("username : "+username+"   "+ username.equals(patentDo.getCurrentUser()));
							if(username.equals(patentDo.getCurrentUser()) &&  solrInputDoc.getFieldValue(key+"_"+applicationProperties.getProperty("patent.comment")).equals(patentDo.getComment())){
								patentDo.setKeyname(key);
								String keyname=SOLRUtil.removeSegment(patentDo);
								///System.out.println("removed keyname : "+keyname);
								if(keyname!=null){
									solrInputDoc.removeField(keyname);
									solrInputDoc.removeField(keyname+"_startOffset");
									solrInputDoc.removeField(keyname+"_endOffset");
									if(solrInputDoc.containsKey(keyname+"_"+applicationProperties.getProperty("patent.comment")))
									solrInputDoc.removeField(keyname+"_"+applicationProperties.getProperty("patent.comment"));
									break;
								}
							}
						}else{
							//System.out.println("comment is not exist for the key : "+key);
							if(username.equals(patentDo.getCurrentUser())){
								patentDo.setKeyname(key);
								String keyname=SOLRUtil.removeSegment(patentDo);
								//System.out.println("removed keyname : "+keyname);
								if(keyname!=null){
									solrInputDoc.removeField(keyname);
									solrInputDoc.removeField(keyname+"_startOffset");
									solrInputDoc.removeField(keyname+"_endOffset");
									if(solrInputDoc.containsKey(keyname+"_"+applicationProperties.getProperty("patent.comment")))
									solrInputDoc.removeField(keyname+"_"+applicationProperties.getProperty("patent.comment"));
									break;
								}
							}
						}
					}
				}
				SOLRUtil.updatePatentIndex(solrInputDoc);
				response="success";
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

}
