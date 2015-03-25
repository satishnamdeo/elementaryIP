package com.eip.controller.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eip.controller.ManageProjectController;
import com.eip.entity.ProjectManage;
import com.eip.entity.User;
import com.eip.service.impl.ManageProjectServiceImpl;
import com.eip.service.impl.UserServiceImpl;

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
			@RequestParam("users") List<Integer> users,
			@RequestParam("ProjectName") String ProjectName,
			RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		/* Integer id=1; */
		Integer id = manageProjectService.createProject(user, ProjectName);
		if (users.size() > 0 && id != 0) {
			manageProjectService.addCollaborator(users, id);
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
		System.out.println("++++++++++++ IN external search +++++++++++ "+searchQueryStr );
		Document doc;
		List<String> patentList=new ArrayList<String>();
		BufferedWriter writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("d:/Search_"+searchQueryStr+".txt"), "utf-8"));
		    writer.write("PAGE NUMBER -------------------  1");
		  
		} catch (IOException ex) {
		  ex.printStackTrace();
		}
		try {
				 
			// need http protocol
			doc = Jsoup.connect("http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&TERM1=murder&FIELD1=&co1=AND&TERM2=&FIELD2=&d=PTXT").timeout(60*1000).get();
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
			System.out.println("+++++++++++++ number Of Records ++++++++++++  "+numberOfRecords);
			/*System.out.println("+++++++++++++ Total Page number ++++++++++++  "+pageNumber);*/
			Elements links = doc.select("table tr");
			for (Element link : links) {
				JSONObject patent = new JSONObject();
				// get the value from href attribute
				/*System.out.println("\nlink : " + link.attr("href"));
				System.out.println("text : "+links.size()+" -" + link.text());*/
				patentList.add(link.text());
				 writer.write(link.text()+" \n");
				 writer.newLine();
				 if(!link.text().equals("") && !link.text().equals("PAT. NO. Title")){
				 patent.put("id",link.text());
				 patent.put("Title","");
				 SearchResult.add(patent);
				 }
						 
			}
			if(pageNumber>1){
				for(int i=page;page<=pageNumber+1;page++){
					
					 writer.write("PAGE NUMBER -------------------  "+page);
					 writer.newLine();
					doc = Jsoup.connect(url0+page+url1).timeout(60*1000).get();
					Elements link = doc.select("table tr");
					for (Element link1 : link) {
						JSONObject patent = new JSONObject();
						// get the value from href attribute
						/*System.out.println("\nlink : " + link1.attr("href"));
						System.out.println("text : " + link1.text());*/
						patentList.add(link1.text());
						 writer.write(link1.text()+"\n");
						 writer.newLine();
						 if(!link1.text().equals("") && !link1.text().equals("PAT. NO. Title")){
							 patent.put("id",link1.text());
							 patent.put("Title","");
							 SearchResult.add(patent);
							 }
								 
					}
				}
			}
			 writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SearchResult;
		
	}
}
