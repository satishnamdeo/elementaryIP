package com.eip.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Controller;


@Controller
public class CustomerRoleAccessDecisionManager implements AccessDecisionManager {
	protected final Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	public void decide(Authentication authentication, Object secureObject,
			Collection attributes) throws AccessDeniedException,
			InsufficientAuthenticationException {

		FilterInvocation invocation = (FilterInvocation) secureObject;
		boolean authorized = true;

		String requestUrl = (invocation.getRequestUrl());
		if (requestUrl.contains("?")) {
			requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
		}
		String[] strArr = requestUrl.split("/");
		//System.out.println(strArr.length +" Size : "+strArr[strArr.length-1]);
		requestUrl=strArr[strArr.length-1];

		//System.out.println("The req url ::::" + requestUrl
				//+ "\t complete url:::" + invocation.getFullRequestUrl());

		// allow user according to their access permission.
		Collection<GrantedAuthority> authorityList = (Collection<GrantedAuthority>) authentication
				.getAuthorities();
		StringBuffer strBuff = new StringBuffer();
		if (authorityList != null)
			for (Iterator<GrantedAuthority> iterator = authorityList.iterator(); iterator
					.hasNext();) {
				SimpleGrantedAuthority roleName = (SimpleGrantedAuthority) iterator
						.next();
				strBuff.append("'" + roleName.getAuthority() + "'" + ",");

			}
		//System.out.println("Roles " + strBuff.toString());
		if (null != authorityList && strBuff.length() > 2) {
			String str = strBuff.substring(0, strBuff.length() - 1);
			//System.out.println("Str Buf:" + str);
			List urlList = ApplicationCacheServlet.getUrlListForRoles(str);
			// urlList.add("/ontoit/some.html");
			// String roles = authorityList.toString();

			if (null != urlList && !urlList.isEmpty()) {
				if (urlList.toString().contains(requestUrl)) {
					authorized = true;
				} else {
					authorized = false;
				}
			} else {
				throw new AccessDeniedException(
						"user does not have permission!!!!");
			}

		} else {
			authorized = false;
		}

		if (!authorized) {
			throw new AccessDeniedException("user not authorizedd....");
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	public boolean supports(ConfigAttribute config) {
		return true;
	}

}
