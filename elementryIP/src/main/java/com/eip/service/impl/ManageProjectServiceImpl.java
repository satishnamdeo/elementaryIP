package com.eip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eip.dao.ManageProjectDao;
import com.eip.entity.ProjectManage;
import com.eip.entity.ProjectUser;
import com.eip.entity.SearchHistory;
import com.eip.entity.User;
import com.eip.service.ManageProjectService;
import com.eip.util.SOLRUtil;

@Service("ManageProjectService")
public class ManageProjectServiceImpl implements ManageProjectService {
	@Autowired
	ManageProjectDao manageProjectDao;
	@Resource
	Properties applicationProperties;

	@Override
	public Integer createProject(User user, String ProjectName) {

		ProjectManage projectManage = new ProjectManage();
		projectManage.setProjectName(ProjectName);
		projectManage.setCreationDate(new Date());
		projectManage.setProjectOwner(user);
		Integer id = manageProjectDao.createProject(projectManage);
		if (id > 0) {
			SOLRUtil.createCore(id);
		}

		return id;
	}

	@Override
	public List<ProjectManage> getProjectListByUserId(Integer userId) {
		List<ProjectManage> projectManageList = manageProjectDao
				.getprojectListByUserId(userId);
		return projectManageList;
	}

	@Override
	public void addCollaborator(List<Integer> users, Integer id) {
		for (Integer userId : users) {
			ProjectUser collaborator = new ProjectUser();
			collaborator.setColabId(new User(userId));
			collaborator.setProjectId(new ProjectManage(id));
			collaborator.setCreationDate(new Date());
			collaborator.setLastModified(new Date());

			collaborator.setWritePermission(true);
			manageProjectDao.addCollaborator(collaborator);
		}

	}

	@Override
	public void deleteProject(Integer projectId) {
		ProjectManage projectManage = manageProjectDao
				.getProjectByProjectId(projectId);
		boolean responce = SOLRUtil.deleteCore(projectId);
		/* System.out.println("+++++++++++ RESPONCE +++++++++++ "+responce); */
		if (responce == true) {
			manageProjectDao.deleteProject(projectManage);
		}

	}

	public JSONArray searchProjectPatent(Integer projectId,
			String searchQueryStr, boolean fromCookie) {
		JSONArray SearchResult = new JSONArray();
		try {
			// indexing start
			// SOLRUtil.indexProjectFile(projectId,"c:/Solr/patents/"+projectId+".xml");
			// indexing end
			// System.out.println("------ "+searchQueryStr);

			SolrDocumentList sdList = SOLRUtil.searchInProjectFile(projectId,
					searchQueryStr);
			/*
			 * if(!fromCookie){ User user=getUserFromSession(); SearchHistory
			 * searchHistory=new SearchHistory();
			 * searchHistory.setUserId(user.getUserId());
			 * searchHistory.setSearchCount(sdList.size());
			 * searchHistory.setSearchTime(new Date());
			 * searchHistory.setSearchKeyword(searchQueryStr);
			 * searchHistoryDao.addSearchHistory(searchHistory); }
			 */
			for (SolrDocument sd : sdList) {
				// SearchResult.add(sd);
				JSONObject patent = new JSONObject();
				patent.put("id", (String) sd
						.getFieldValue(applicationProperties
								.getProperty("patent.id")));
				patent.put("US_Patent_Number", (String) sd
						.getFieldValue(applicationProperties
								.getProperty("patent.patent_Number")));
				patent.put("PublicationDate", (String) sd
						.getFieldValue(applicationProperties
								.getProperty("patent.publicationDate")));
				patent.put("FilingDate", (String) sd
						.getFieldValue(applicationProperties
								.getProperty("patent.filingDate")));
				patent.put("Title", (String) sd
						.getFieldValue(applicationProperties
								.getProperty("patent.title")));
				patent.put("Abstract", (String) sd
						.getFieldValue(applicationProperties
								.getProperty("patent.abstract")));
				SearchResult.add(patent);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SearchResult;

	}

	@Override
	public List<ProjectManage> getProjectListByColabId(Integer colabId) {
		List<ProjectUser> projectUsers = manageProjectDao
				.getprojectListByColabId(colabId);
		List<ProjectManage> projectManages = new ArrayList<ProjectManage>();
		for (ProjectUser projectUser : projectUsers) {
			ProjectManage projectManage = projectUser.getProjectId();
			projectManages.add(projectManage);
		}

		return projectManages;
	}

	@Override
	public List<ProjectManage> getProjectList() {
		List<ProjectManage> projectManages = manageProjectDao.getprojectList();
		return projectManages;
	}

}
