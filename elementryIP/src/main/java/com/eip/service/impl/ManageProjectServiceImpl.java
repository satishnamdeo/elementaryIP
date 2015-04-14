package com.eip.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.jdom2.input.SAXBuilder;
import org.python.core.exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eip.dao.ManageProjectDao;
import com.eip.domain.PatentFieldsDo;
import com.eip.domain.ProjectDo;
import com.eip.entity.ProjectManage;
import com.eip.entity.ProjectUser;
import com.eip.entity.SearchHistory;
import com.eip.entity.User;
import com.eip.service.ManageProjectService;
import com.eip.util.SOLRUtil;


/*import org.jdom2.Document;
import org.jdom2.Element;*/
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

@Service("ManageProjectService")
public class ManageProjectServiceImpl implements ManageProjectService {
	@Autowired
	ManageProjectDao manageProjectDao;
	@Resource
	Properties applicationProperties;

	@Override
	public Integer createProject(User user, ProjectDo projectDo) {

		ProjectManage projectManage = new ProjectManage();
		projectManage.setProjectName(projectDo.getProjectName());
		projectManage.setProjectNotes(projectDo.getProjectNotes());
		projectManage.setCreationDate(new Date());
		projectManage.setProjectOwner(user);
		Integer id = manageProjectDao.createProject(projectManage);
		
		if (id > 0) {
			try{
		boolean answer=	SOLRUtil.createCore(id);
				if (!answer) {
					ProjectManage projectManageToDelete = manageProjectDao
							.getProjectByProjectId(id);

					manageProjectDao.deleteProject(projectManageToDelete);

					return 0;
				}
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
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

	@Override
	public boolean createProjectXML(PatentFieldsDo patentFieldsDo,Integer projectId) {
		String filepath = applicationProperties.getProperty("patent.recordFile.path")+projectId+".xml";

		  try {
	 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("add");
			doc.appendChild(rootElement);
	 
			
			Element patentDocument = doc.createElement("doc");
			rootElement.appendChild(patentDocument);
	 
			
			Element id = doc.createElement("field");
			id.appendChild(doc.createTextNode(patentFieldsDo.getId()));
			patentDocument.appendChild(id);
			
			Attr attrId = doc.createAttribute("name");
			attrId.setValue("id");
			id.setAttributeNode(attrId);
			
			Element usPatentNumber = doc.createElement("field");
			usPatentNumber.appendChild(doc.createTextNode(patentFieldsDo.getUS_Patent_Number()));
			patentDocument.appendChild(usPatentNumber);
			
			Attr attrUsPatentNumber = doc.createAttribute("name");
			attrUsPatentNumber.setValue("US_Patent_Number");
			usPatentNumber.setAttributeNode(attrUsPatentNumber);
	 
			Element publicationDate = doc.createElement("field");
			publicationDate.appendChild(doc.createTextNode(patentFieldsDo.getPublicationDate()));
			patentDocument.appendChild(publicationDate);
			
			Attr attrPublicationDate = doc.createAttribute("name");
			attrPublicationDate.setValue("PublicationDate");
			publicationDate.setAttributeNode(attrPublicationDate);
	 
			
			Element filingDate = doc.createElement("field");
			filingDate.appendChild(doc.createTextNode(patentFieldsDo.getFilingDate()));
			patentDocument.appendChild(filingDate);
			
			Attr attrFilingDate = doc.createAttribute("name");
			attrFilingDate.setValue("FilingDate");
			filingDate.setAttributeNode(attrFilingDate);
	 
			
			Element inventors = doc.createElement("field");
			inventors.appendChild(doc.createTextNode(patentFieldsDo.getInventors()));
			patentDocument.appendChild(inventors);
			
			Attr attrInventors = doc.createAttribute("name");
			attrInventors.setValue("Inventors");
			inventors.setAttributeNode(attrInventors);
			
			Element classesUS = doc.createElement("field");
			classesUS.appendChild(doc.createTextNode(patentFieldsDo.getClassesUS()));
			patentDocument.appendChild(classesUS);
			
			Attr attrClassesUS = doc.createAttribute("name");
			attrClassesUS.setValue("ClassesUS");
			classesUS.setAttributeNode(attrClassesUS);
			
			Element title = doc.createElement("field");
			title.appendChild(doc.createTextNode(patentFieldsDo.getTitle()));
			patentDocument.appendChild(title);
			
			Attr attrTitle = doc.createAttribute("name");
			attrTitle.setValue("Title");
			title.setAttributeNode(attrTitle);
			
			Element abstractElement = doc.createElement("field");
			abstractElement.appendChild(doc.createTextNode(patentFieldsDo.getAbstract()));
			patentDocument.appendChild(abstractElement);
			
			Attr attrAbstract = doc.createAttribute("name");
			attrAbstract.setValue("Abstract");
			abstractElement.setAttributeNode(attrAbstract);
			
			if(patentFieldsDo.getClaimIndependentList().size()>0){
				for(String claimIndependentSring:patentFieldsDo.getClaimIndependentList()){
					Element claimIndependent = doc.createElement("field");
					claimIndependent.appendChild(doc.createTextNode(claimIndependentSring));
					patentDocument.appendChild(claimIndependent);
					
					Attr attrClaimIndependent = doc.createAttribute("name");
					attrClaimIndependent.setValue("Claims_Independent");
					claimIndependent.setAttributeNode(attrClaimIndependent);	
				}
			}
			
			if(patentFieldsDo.getClaimDependentList().size()>0){
				for(String claimDependentSring:patentFieldsDo.getClaimDependentList()){
					Element claimDependent = doc.createElement("field");
					claimDependent.appendChild(doc.createTextNode(claimDependentSring));
					patentDocument.appendChild(claimDependent);
					
					Attr attrClaimDependent = doc.createAttribute("name");
					attrClaimDependent.setValue("Claims_Dependent");
					claimDependent.setAttributeNode(attrClaimDependent);	
				}
			}
			
			Element description = doc.createElement("field");
			description.appendChild(doc.createTextNode(patentFieldsDo.getDescription()));
			patentDocument.appendChild(description);
			
			Attr attrDescription = doc.createAttribute("name");
			attrDescription.setValue("Description");
			description.setAttributeNode(attrDescription);
	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 2);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
	 
			transformer.transform(source, result);
	 
		
			return true;
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		  return false;
		}

	@Override
	public void appandProjectXML(PatentFieldsDo patentFieldsDo,Integer projectId) {
	

		String filepath = applicationProperties.getProperty("patent.recordFile.path")+projectId+".xml";
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			NodeList rootList = doc.getElementsByTagName("add");
			Node rootElement = rootList.item(0);
			
			Element patentDocument = doc.createElement("doc");
			rootElement.appendChild(patentDocument);
			
			Element id = doc.createElement("field");
			id.appendChild(doc.createTextNode(patentFieldsDo.getId()));
			patentDocument.appendChild(id);
			
			Attr attrId = doc.createAttribute("name");
			attrId.setValue("id");
			id.setAttributeNode(attrId);
			
			Element usPatentNumber = doc.createElement("field");
			usPatentNumber.appendChild(doc.createTextNode(patentFieldsDo.getUS_Patent_Number()));
			patentDocument.appendChild(usPatentNumber);
			
			Attr attrUsPatentNumber = doc.createAttribute("name");
			attrUsPatentNumber.setValue("US_Patent_Number");
			usPatentNumber.setAttributeNode(attrUsPatentNumber);
	 
			// lastname elements
			Element publicationDate = doc.createElement("field");
			publicationDate.appendChild(doc.createTextNode(patentFieldsDo.getPublicationDate()));
			patentDocument.appendChild(publicationDate);
			
			Attr attrPublicationDate = doc.createAttribute("name");
			attrPublicationDate.setValue("PublicationDate");
			publicationDate.setAttributeNode(attrPublicationDate);
	 
			// nickname elements
			Element filingDate = doc.createElement("field");
			filingDate.appendChild(doc.createTextNode(patentFieldsDo.getFilingDate()));
			patentDocument.appendChild(filingDate);
			
			Attr attrFilingDate = doc.createAttribute("name");
			attrFilingDate.setValue("FilingDate");
			filingDate.setAttributeNode(attrFilingDate);
	 
			// salary elements
			Element inventors = doc.createElement("field");
			inventors.appendChild(doc.createTextNode(patentFieldsDo.getInventors()));
			patentDocument.appendChild(inventors);
			
			Attr attrInventors = doc.createAttribute("name");
			attrInventors.setValue("Inventors");
			inventors.setAttributeNode(attrInventors);
			
			Element classesUS = doc.createElement("field");
			classesUS.appendChild(doc.createTextNode(patentFieldsDo.getClassesUS()));
			patentDocument.appendChild(classesUS);
			
			Attr attrClassesUS = doc.createAttribute("name");
			attrClassesUS.setValue("ClassesUS");
			classesUS.setAttributeNode(attrClassesUS);
			
			Element title = doc.createElement("field");
			title.appendChild(doc.createTextNode(patentFieldsDo.getTitle()));
			patentDocument.appendChild(title);
			
			Attr attrTitle = doc.createAttribute("name");
			attrTitle.setValue("Title");
			title.setAttributeNode(attrTitle);
			
			Element abstractElement = doc.createElement("field");
			abstractElement.appendChild(doc.createTextNode(patentFieldsDo.getAbstract()));
			patentDocument.appendChild(abstractElement);
			
			Attr attrAbstract = doc.createAttribute("name");
			attrAbstract.setValue("Abstract");
			abstractElement.setAttributeNode(attrAbstract);
			if(patentFieldsDo.getClaimIndependentList().size()>0){
				for(String claimIndependentSring:patentFieldsDo.getClaimIndependentList()){
					Element claimIndependent = doc.createElement("field");
					claimIndependent.appendChild(doc.createTextNode(claimIndependentSring));
					patentDocument.appendChild(claimIndependent);
					
					Attr attrClaimIndependent = doc.createAttribute("name");
					attrClaimIndependent.setValue("Claims_Independent");
					claimIndependent.setAttributeNode(attrClaimIndependent);	
				}
			}
			if(patentFieldsDo.getClaimDependentList().size()>0){
				for(String claimDependentSring:patentFieldsDo.getClaimDependentList()){
					Element claimDependent = doc.createElement("field");
					claimDependent.appendChild(doc.createTextNode(claimDependentSring));
					patentDocument.appendChild(claimDependent);
					
					Attr attrClaimDependent = doc.createAttribute("name");
					attrClaimDependent.setValue("Claims_Dependent");
					claimDependent.setAttributeNode(attrClaimDependent);	
				}
			}
			
			Element description = doc.createElement("field");
			description.appendChild(doc.createTextNode(patentFieldsDo.getDescription()));
			patentDocument.appendChild(description);
			
			Attr attrDescription = doc.createAttribute("name");
			attrDescription.setValue("Description");
			description.setAttributeNode(attrDescription);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			 transformerFactory.setAttribute("indent-number", 2);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
				 
			
				 
			transformer.transform(source, result);
				 
				 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<String> getPatentListFromXML(Integer projectId) {
		String filePath=applicationProperties.getProperty("patent.recordFile.path")+projectId+".xml";
			List<String> idList=new ArrayList<>();
		try {
			
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File(filePath);
			org.jdom2.Document doc = (org.jdom2.Document) builder
					.build(xmlFile);
			org.jdom2.Element rootNode = doc.getRootElement();
			List<org.jdom2.Element> DocList = rootNode.getChildren("doc");
			for (org.jdom2.Element document : DocList) {
				List<org.jdom2.Element> nodes = document.getChildren("field");
				for (org.jdom2.Element node : nodes) {
					if (node.getAttributeValue("name").equals(
							applicationProperties.getProperty("patent.id"))
							) {
						idList.add(node.getValue());
						
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idList;
	}
	
		
	

}
