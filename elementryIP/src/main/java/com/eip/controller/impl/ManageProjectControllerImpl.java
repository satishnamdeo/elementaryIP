package com.eip.controller.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eip.controller.ManageProjectController;
import com.eip.domain.PatentDo;
import com.eip.domain.PatentFieldsDo;
import com.eip.domain.PatentListDo;
import com.eip.domain.ProjectDo;
import com.eip.entity.ProjectManage;
import com.eip.entity.User;
import com.eip.service.impl.ManageProjectServiceImpl;
import com.eip.service.impl.UserServiceImpl;
import com.eip.util.SOLRUtil;

@Controller
public class ManageProjectControllerImpl implements ManageProjectController {
	@Autowired
	ManageProjectServiceImpl manageProjectService;
	@Autowired
	UserServiceImpl userService;
	@Resource
	Properties applicationProperties;

	@RequestMapping(value = "/manageproject", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String manageProject(HttpServletRequest request, ModelMap map) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		List<ProjectManage> projectManageList = null;
		if (user.getRoleId().getRoleId() != 1) {
			List<ProjectManage> projectManageColabList = manageProjectService
					.getProjectListByColabId(user.getUserId());
			projectManageList = manageProjectService
					.getProjectListByUserId(user.getUserId());
			map.addAttribute("projectColabList", projectManageColabList);
			map.addAttribute("projectList", projectManageList);
		}

		if (user.getRoleId().getRoleId() == 1) {
			projectManageList = manageProjectService.getProjectList();
			map.addAttribute("projectList", projectManageList);
		}

		List<User> userList = userService.getUserList(user.getUserId());
		map.addAttribute("userList", userList);
		return "manageproject";
	}

	@RequestMapping(value = "/createproject", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String createProject(HttpServletRequest request,
			@ModelAttribute("projectDo") ProjectDo projectDo,
			RedirectAttributes redirect) {
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			/* Integer id=1; */
			Integer id = manageProjectService.createProject(user,projectDo);
			if (projectDo.getUsers() != null && id != 0) {
				manageProjectService.addCollaborator(projectDo.getUsers(), id);
			}

			return "redirect:/manageproject.html";
		} catch (Exception e) {

			e.printStackTrace();
		}
		return "redirect:/manageproject.html";
	}

	@Override
	@RequestMapping(value = "/deleteproject", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String deleteProject(Integer projectId, RedirectAttributes redirect) {

		manageProjectService.deleteProject(projectId);
		return "redirect:/manageproject.html";
	}

	@Override
	@RequestMapping(value = "/projectdetail", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String getProjectDetail(
			@RequestParam("projectId") Integer projectId, ModelMap map) {
		map.addAttribute("projectId", projectId);
		map.addAttribute("message", "NO");
		return "projectdetail";
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/searchProjectPatent", method = {
			RequestMethod.POST, RequestMethod.GET })
	public JSONArray searchProjectPatent(Integer projectId,
			@RequestParam("searchQueryStr") String searchQueryStr,
			boolean fromCookie) {

		JSONArray SearchResult = new JSONArray();
		if (searchQueryStr != null && !searchQueryStr.equals("")) {
			SearchResult = manageProjectService.searchProjectPatent(projectId,
					searchQueryStr, fromCookie);
		}
		return SearchResult;

	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/externalsearch", method = { RequestMethod.POST,
			RequestMethod.GET })
	public JSONArray externalSearch(@RequestParam("searchQueryStr")String searchQueryStr) {
		JSONArray SearchResult = new JSONArray();
		/*System.out.println("++++++++++++ IN external search +++++++++++ "+searchQueryStr );*/
		Document doc;
		List<String> patentList=new ArrayList<String>();
		/*BufferedWriter writer = null;*/
		   /* writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("d:/Search_"+searchQueryStr+".txt"), "utf-8"));
		    writer.write("PAGE NUMBER -------------------  1");*/
		try {
				 
			// need http protocol
			doc = Jsoup.connect("http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&TERM1=murder&FIELD1=&co1=AND&TERM2=&FIELD2=&d=PTXT").timeout(600*1000).get();
			String url0="http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r=0&f=S&l=50&d=PTXT&OS=murder&RS=murder&Query=murder&TD=430&Srch1=murder&NextList";
			String url1="=Next+50+Hits";
			// get page title
			String title = doc.title();
			System.out.println("title : " + title);
	        Integer page=2;
			// get all links
			Elements links0 = doc.select("body i");
			String records=links0.get(1).text();
			int index=records.indexOf("of");
			String subString=records.substring(index+3);
			Integer numberOfRecords=Integer.parseInt(subString.trim());
			Integer pageNumber=numberOfRecords/50;
			Elements links = doc.select("table tr");
			for (Element link : links) {
				JSONObject patent = new JSONObject();
				patentList.add(link.text());
				if(!link.text().equals("")&& !link.text().equals("PAT. NO. Title")){
				int indexOfSpace=link.text().indexOf(" ");
				String patentString=new StringBuffer(link.text()).insert(indexOfSpace, ". ").toString();
				 patent.put("Title","");
				 patent.put("id",patentString);
				 SearchResult.add(patent);
				 }
						 
			}
			if(pageNumber>1){
				for(int i=page;page<=pageNumber+1;page++){
					doc = Jsoup.connect(url0+page+url1).timeout(600*1000).get();
					Elements link = doc.select("table tr");
					for (Element link1 : link) {
						JSONObject patent = new JSONObject();
						patentList.add(link1.text());
						 if(!link1.text().equals("") && !link1.text().equals("PAT. NO. Title")){
							 int indexOfSpace=link1.text().indexOf(" ");
							 String patentString=new StringBuffer(link1.text()).insert(indexOfSpace, ". ").toString();
							 patent.put("id",patentString);
							 patent.put("Title","");
							 SearchResult.add(patent);
							 }
								 
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SearchResult;
		/*return "externalsearch";*/
		
	}

	@Override
	@RequestMapping(value = "/importpatent", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public String importPatent(@RequestBody PatentListDo patentListDo,ModelMap map) {
		/*System.out.println("projcet id "+patentListDo.getProjectId());
		System.out.println("patent list size "+patentListDo.getPatentList().size());*/
		List<String> patentIdList=null;
		boolean projectFileExist=false;
		boolean isNewPatent=true;
		/*BufferedWriter writer = null;*/
		
		/*System.out.println("+++++++++patentListDo.getProjectId() ++++++++++++"+patentListDo.getProjectId());*/
		/*if(!patentListDo.getPatentId().equals("") && patentListDo.getPatentId() != null ){
			List<String> patentList=new ArrayList<>();
			patentList.add(patentListDo.getPatentId());
			patentListDo.setPatentList(patentList);
		}*/
			
		try{
			File projectFile=new File(applicationProperties.getProperty("patent.recordFile.path")+patentListDo.getProjectId()+".xml");
			if(projectFile.exists()){
				System.out.println("in if project file already exist"+patentListDo.getProjectId());
				projectFileExist=true;
		 patentIdList=	manageProjectService.getPatentListFromXML(patentListDo.getProjectId());
				
			}
			
			/* writer = new BufferedWriter(new OutputStreamWriter(
	          new FileOutputStream("d:/Patent_"+patentListDo.getProjectId()+".txt"), "utf-8"));*/
	          
			for(PatentDo patentData:patentListDo.getPatentList()){
			isNewPatent=true;
			Document doc;
			
			
			String number=patentData.getSeqNumber();
			
			String patentNumberWithComma=patentData.getPatentId();
			String patentNumber=patentNumberWithComma.replaceAll("," , "");
			/*System.out.println("patent id list "+patentIdList.size());*/
			if(patentIdList!=null){
				/*System.out.println("in if id list not null");*/
			for(String id:patentIdList){
				
				if(id.equals(patentNumber.trim())){
					
					
					isNewPatent=false;
					break;
				}
			}}
			if(isNewPatent ){
				/*System.out.println(" in if new patent ");*/
			Integer patentnumber=Integer.parseInt(number);
			int page=patentnumber/50;
			PatentFieldsDo patentFieldsDo=new PatentFieldsDo();
			
		       if(page==0){
			
				String url="http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r="
							+patentnumber+"&f=G&l=50&co1=AND&d=PTXT&s1=murder&OS=murder&RS=murder";
				doc = Jsoup.connect(url).timeout(600*1000).get();
							
				Elements content = doc.select("p");
				Elements tablecontenttd = doc.select("table tr td");
				Elements tablecontent = doc.select("table tr ");
				Elements tittle = doc.select("font");
				Elements heading = doc.select("coma ");
				
				boolean claims=false;
				boolean description=false;
				boolean claimsOneAdded=false;
				boolean isClameDependent=false;
				String descriptionText="";
				int claimNumber=0;
				List<String> claimIndependentList=new ArrayList<>();
				List<String> claimDependentList=new ArrayList<>();
				List<Integer> claimNumberAdded=new ArrayList<>();
				
				for (Element link : heading) {
					
					List<TextNode> textNode=link.textNodes();
					
					for(TextNode t1:textNode){
						
						isClameDependent=false;
						if(t1.text().trim().endsWith(":")&& claimNumberAdded.size()==0){
							
							claims=true;}
						if(claims && t1.text().trim().startsWith("1.")){
							
							claimsOneAdded=true;
							claimIndependentList.add(t1.text())	;
							claimNumber++;
							claimNumberAdded.add(claimNumber);
						}
						if(claims && claimsOneAdded && !t1.text().equals(" ") && !t1.text().trim().startsWith("1.")){
							String subString="";
							if(t1.text().length()>30){
							 subString=t1.text();}
							else{
								 subString=t1.text();
							}
							for(Integer numberClaim:claimNumberAdded){
								if(subString.contains("claim "+numberClaim)){
									
									claimDependentList.add(t1.text());
									isClameDependent=true;
									claimNumber++;
									claimNumberAdded.add(claimNumber);
									break;
								}
							}
							if(!isClameDependent){
								
							claimIndependentList.add(t1.text());
							claimNumber++;
							claimNumberAdded.add(claimNumber);
							}
						}
						if(t1.text().equals(" ") && claimNumberAdded.size()>0 && claims){
							
							claims=false;		
						}
						if(!claims && !t1.text().equals(" ")&&claimNumberAdded.size()>0 && descriptionText.equals("")){
							claims=false;	
							description=true;
							
						}
						if(description && !t1.text().equals(" ")){
							descriptionText+=t1.text();
						}
						
						
					}
									
							 
				}
				if(tittle.get(4).text().trim().startsWith("**")){
					if(tittle.get(7).text().trim().startsWith("**")){
						
						patentFieldsDo.setTitle(tittle.get(8).text());
					}
					else{
						patentFieldsDo.setTitle(tittle.get(7).text());
					}
					
				}
				else {
					patentFieldsDo.setTitle(tittle.get(4).text());
				
					
				}
				
				patentFieldsDo.setAbstract(content.get(0).text());
				
				patentFieldsDo.setId(tablecontenttd.get(7).text().replaceAll(",", ""));
				patentFieldsDo.setUS_Patent_Number(tablecontenttd.get(7).text().replaceAll(",", ""));
				
				patentFieldsDo.setPublicationDate(tablecontenttd.get(9).text());
				patentFieldsDo.setDescription(descriptionText);	
				patentFieldsDo.setClaimIndependentList(claimIndependentList);
				patentFieldsDo.setClaimDependentList(claimDependentList);
				for (Element link : tablecontent) {
					if(link.text().startsWith("Inventors")){
					int indexOf=link.text().indexOf(":");
						
						patentFieldsDo.setInventors(link.text().substring(indexOf+2));
					}
					if(link.text().startsWith("Filed")){
						int indexOf=link.text().indexOf(":");
							
							patentFieldsDo.setFilingDate(link.text().substring(indexOf+2));
						}
						if(link.text().startsWith("Current U.S. Class")){
							int indexOf=link.text().indexOf(":");
								
								patentFieldsDo.setClassesUS(link.text().substring(indexOf+2));
								break;
							}
					
							 
				}
				
				if(projectFileExist){
					manageProjectService.appandProjectXML(patentFieldsDo,patentListDo.getProjectId());
				}
				else
				{				
					projectFileExist=manageProjectService.createProjectXML(patentFieldsDo,patentListDo.getProjectId());
					}
			}
			if(page>0){
				int numberPage=page+1;
				
				String url="http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r="
							+patentnumber+"&f=G&l=50&d=PTXT&s1=murder&p="+numberPage+"&OS=murder&RS=murder";
				doc = Jsoup.connect(url).timeout(600*1000).get();
				Elements content = doc.select("p");
				Elements tablecontenttd = doc.select("table tr td");
				Elements tablecontent = doc.select("table tr ");
				Elements tittle = doc.select("font");
                Elements heading = doc.select("coma ");
				
				boolean claims=false;
				boolean description=false;
				boolean claimsOneAdded=false;
				boolean isClameDependent=false;
				String descriptionText="";
				int claimNumber=0;
				List<String> claimIndependentList=new ArrayList<>();
				List<String> claimDependentList=new ArrayList<>();
				List<Integer> claimNumberAdded=new ArrayList<>();
				
				for (Element link : heading) {
					
					List<TextNode> textNode=link.textNodes();
					
					for(TextNode t1:textNode){
						
						isClameDependent=false;
						if(t1.text().trim().endsWith(":")&& claimNumberAdded.size()==0){
							
							claims=true;}
						if(claims && t1.text().trim().startsWith("1.")){
							
							claimsOneAdded=true;
							claimIndependentList.add(t1.text())	;
							claimNumber++;
							claimNumberAdded.add(claimNumber);
						}
						if(claims && claimsOneAdded && !t1.text().equals(" ") && !t1.text().trim().startsWith("1.")){
							String subString="";
							if(t1.text().length()>30){
							 subString=t1.text();}
							else{
								 subString=t1.text();
							}
							for(Integer numberClaim:claimNumberAdded){
								if(subString.contains("claim "+numberClaim)){
									
									claimDependentList.add(t1.text());
									isClameDependent=true;
									claimNumber++;
									claimNumberAdded.add(claimNumber);
									break;
								}
							}
							if(!isClameDependent){
								
							claimIndependentList.add(t1.text());
							claimNumber++;
							claimNumberAdded.add(claimNumber);
							}
						}
						if(t1.text().equals(" ") && claimNumberAdded.size()>0 && claims){
							
							claims=false;		
						}
						if(!claims && !t1.text().equals(" ")&&claimNumberAdded.size()>0 && descriptionText.equals("")){
							claims=false;	
							description=true;
							
						}
						if(description && !t1.text().equals(" ")){
							descriptionText+=t1.text();
						}
						
						
					}
					
					
							 
				}
				if(tittle.get(4).text().trim().startsWith("**")){
					if(tittle.get(7).text().trim().startsWith("**")){
					
						patentFieldsDo.setTitle(tittle.get(8).text());
					}
					else{
						patentFieldsDo.setTitle(tittle.get(7).text());
					}
					
				}
				else {
					patentFieldsDo.setTitle(tittle.get(4).text());
									
				}
				/*if(tittle.get(4).text().trim().startsWith("**")){patentFieldsDo.setTitle(tittle.get(7).text());}
				else {
					patentFieldsDo.setTitle(tittle.get(4).text());
				}*/
				
				patentFieldsDo.setAbstract(content.get(0).text());
				
				patentFieldsDo.setId(tablecontenttd.get(7).text().replaceAll(",", ""));
				patentFieldsDo.setUS_Patent_Number(tablecontenttd.get(7).text().replaceAll(",", ""));
				
				patentFieldsDo.setPublicationDate(tablecontenttd.get(9).text());
				patentFieldsDo.setDescription(descriptionText);	
				patentFieldsDo.setClaimIndependentList(claimIndependentList);
				patentFieldsDo.setClaimDependentList(claimDependentList);
				
				
				for (Element link : tablecontent) {
					if(link.text().startsWith("Inventors")){
					int indexOf=link.text().indexOf(":");
						
						patentFieldsDo.setInventors(link.text().substring(indexOf+2));
					}
					if(link.text().startsWith("Filed")){
						int indexOf=link.text().indexOf(":");
						
							patentFieldsDo.setFilingDate(link.text().substring(indexOf+2));
						}
						if(link.text().startsWith("Current U.S. Class")){
							int indexOf=link.text().indexOf(":");
							
								patentFieldsDo.setClassesUS(link.text().substring(indexOf+2));
								break;
							}
					
							 
				}
				
				if(projectFileExist){
					manageProjectService.appandProjectXML(patentFieldsDo,patentListDo.getProjectId());
				}
				else
				{				
					projectFileExist=manageProjectService.createProjectXML(patentFieldsDo,patentListDo.getProjectId());
					}
			}
		}else{
			System.out.println("++++++++ patent already exist +++++++++++++"+patentNumber);
		}
		/*System.out.println("+++++++++ complete ++++++++++++");*/
		
		}
			SOLRUtil.indexProjectFile(patentListDo.getProjectId(), applicationProperties.getProperty("patent.recordFile.path")+patentListDo.getProjectId()+".xml");
		}
		catch(Exception e){e.printStackTrace();}
		map.addAttribute("projectId", patentListDo.getProjectId());
		map.addAttribute("message", "YES");
		return "projectdetail";
		
	}

	@Override
	@RequestMapping(value = "/testimport", method = { RequestMethod.POST,RequestMethod.GET })
	public void testImport() {
		System.out.println("++++++++ in test import +++++++++++++");
		BufferedWriter writer = null;
		Document doc;
		try{
		 writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("d:/Patent.txt"), "utf-8"));
		/*WebDriver webDriver = new FirefoxDriver();
		webDriver.get("http://localhost:8181/elementryIP/externalsearch.html");*/
		/*String htmlPage = webDriver.getPageSource();*/
		/*Tidy tidy = new Tidy();*/
		/*InputStream inputStream = new ByteArrayInputStream(htmlPage.getBytes());*/
		/*Document doc = tidy.parseDOM(inputStream, null);*/
		/*Element element = doc.getElementById("sharetools-iframe");*/
	/*	WebElement webElement=webDriver.switchTo().frame(webDriver.findElement(By.id("iframe"))).findElement(By.tagName("body"));
		writer.write("-----------------"+webElement.getText());*/
		 String url="http://localhost:8181/elementryIP/externalsearch.html";
		doc = Jsoup.connect(url).timeout(600*1000).get();
		writer.write("-----------------"+doc.html());
		System.out.println(" element "+doc.html());}catch(Exception e){e.printStackTrace();}
	}
}
