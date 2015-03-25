package com.eip.dao;

import java.util.List;

import com.eip.entity.ProjectManage;
import com.eip.entity.ProjectUser;

public interface ManageProjectDao {
	
	public Integer createProject(ProjectManage projectManage);
	
	public Integer addCollaborator(ProjectUser collaborator);
    
	public List<ProjectManage> getprojectList();
	
	public void deleteProject(ProjectManage projectManage);
	
	public ProjectManage getProjectByProjectId(Integer projectId);
	
	public List<ProjectUser> getprojectListByColabId(Integer userId);
	
	public List<ProjectManage> getprojectListByUserId(Integer userId);

}
