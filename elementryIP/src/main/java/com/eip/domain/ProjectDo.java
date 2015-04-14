package com.eip.domain;

import java.util.List;

public class ProjectDo {
	Integer projectId;
	String projectName;
	String projectNotes;
	
	List<Integer> users;
	String projectObject;
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectNotes() {
		return projectNotes;
	}
	public void setProjectNotes(String projectNotes) {
		this.projectNotes = projectNotes;
	}
	public List<Integer> getUsers() {
		return users;
	}
	public void setUsers(List<Integer> users) {
		this.users = users;
	}
	public String getProjectObject() {
		return projectObject;
	}
	public void setProjectObject(String projectObject) {
		this.projectObject = projectObject;
	}

}
