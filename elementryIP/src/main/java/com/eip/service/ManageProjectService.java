package com.eip.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.eip.domain.PatentFieldsDo;
import com.eip.domain.ProjectDo;
import com.eip.entity.ProjectManage;
import com.eip.entity.User;

public interface ManageProjectService {
	
    public Integer createProject(User user,ProjectDo projectDo);
	
	public List<ProjectManage> getProjectListByUserId(Integer userId);
	
	public void addCollaborator(List<Integer> users,Integer id);
	
	public void deleteProject(Integer projectId);

	public JSONArray searchProjectPatent(Integer projectId,
			String searchQueryStr, boolean fromCookie);
	
	public List<ProjectManage> getProjectListByColabId(Integer colabId);
	
	public List<ProjectManage> getProjectList();
	
	public boolean createProjectXML(PatentFieldsDo patentFieldsDo,Integer projectId);
	public void appandProjectXML(PatentFieldsDo patentFieldsDo,Integer projectId);
	
	public List<String> getPatentListFromXML(Integer projectId);

}
