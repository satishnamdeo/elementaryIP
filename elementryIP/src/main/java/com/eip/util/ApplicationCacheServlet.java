package com.eip.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eip.dao.UrlMasterDao;



public class ApplicationCacheServlet extends HttpServlet {

	private static final long serialVersionUID = 6216790912125747028L;

	protected final static Logger log = Logger
			.getLogger(ApplicationCacheServlet.class);
	private static ServletContext context = null;
	
	private static Map<String, Object> constantsMap = new HashMap<String, Object>();

	
	private static Map<String, List<String>> roleUrlMasterMap;
	private static UrlMasterDao urlMasterDao;
	@Override
	public void init() throws ServletException {
		super.init();
		if (log.isDebugEnabled()) {
			log.debug("ApplicationCacheServlet.init()");
		}

		context = getServletContext();
		ApplicationContext springAppContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
		
		urlMasterDao = (UrlMasterDao) springAppContext
				.getBean("UrlMasterDao");
		setRolesName();
		setUrlMasterCache();

	}

	public static void setRolesName() {
		context.setAttribute("eipAdmin", ApplicationConstant.ROLE_ELEMNTRYIP_ADMIN);
		context.setAttribute("eipUser", ApplicationConstant.ROLE_ELEMNTRYIP_USER);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
		response.sendRedirect(context.getContextPath()
				+ "/home.html");
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	public static void setUrlMasterCache() {
		roleUrlMasterMap = new HashMap<String, List<String>>();

		List<String> list1 = urlMasterDao.getUrlsForRole(ApplicationConstant.ROLE_ELEMNTRYIP_ADMIN);
		roleUrlMasterMap.put(ApplicationConstant.ROLE_ELEMNTRYIP_ADMIN, list1);
		List<String> list2 = urlMasterDao.getUrlsForRole(ApplicationConstant.ROLE_ELEMNTRYIP_USER);
		roleUrlMasterMap.put(ApplicationConstant.ROLE_ELEMNTRYIP_USER, list2);
		List<String> list3 = urlMasterDao.getUrlsForRole(ApplicationConstant.ROLE_ANONYMOUS);
		roleUrlMasterMap.put(ApplicationConstant.ROLE_ANONYMOUS, list3);
		
	}

	public static List<String> getUrlListForRoles(String roles) {
		List<String> list = new ArrayList<String>();

		if (roles.contains(ApplicationConstant.ROLE_ELEMNTRYIP_ADMIN)) {
			list.addAll(roleUrlMasterMap.get(ApplicationConstant.ROLE_ELEMNTRYIP_ADMIN));
		}

		if (roles.contains(ApplicationConstant.ROLE_ELEMNTRYIP_USER)) {
			list.addAll(roleUrlMasterMap.get(ApplicationConstant.ROLE_ELEMNTRYIP_USER));
		}
		
		if (roles.contains(ApplicationConstant.ROLE_ANONYMOUS)) {
			list.addAll(roleUrlMasterMap.get(ApplicationConstant.ROLE_ANONYMOUS));
		}

		return list;
	}

	
}
