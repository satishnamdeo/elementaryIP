package com.eip.domain;

import java.util.List;

public class PatentListDo {
	List<PatentDo> patentList;
	
	Integer projectId;
	
	String patentId;

	public String getPatentId() {
		return patentId;
	}

	public void setPatentId(String patentId) {
		this.patentId = patentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public List<PatentDo> getPatentList() {
		return patentList;
	}

	public void setPatentList(List<PatentDo> patentList) {
		this.patentList = patentList;
	}

	

	
}
