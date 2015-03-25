<%@ page language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional //EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
    <head>
    	<title><tiles:getAsString name="title" ignore="true"/></title>
    	<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="description" content="">
		<meta name="author" content="">
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    	
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/favicon.ico">
		<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
		<link href="<%=request.getContextPath()%>/css/elementaryip.css" rel="stylesheet">
		<!-- Custom styles for this template -->
		
		<script src="<%=request.getContextPath()%>/javascript/jquery/jquery-2.0.0.js"></script>
		<script src="<%=request.getContextPath()%>/javascript/jquery/bootstrap.js"></script>
	   	<script src="<%=request.getContextPath()%>/javascript/elemntryip.js"></script>
 	</head>
 	<body >
 	 
 		
		<!-- start of Content -->
		
		<tiles:insertAttribute name="content" />
		
		<!-- End of Content -->
	

	</body>
  </html> 
