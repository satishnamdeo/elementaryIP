<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
	<link href="<%=request.getContextPath()%>/css/history.css" rel="stylesheet">
	
	<%-- <link href="<%=request.getContextPath()%>/css/style.css" media="screen" rel="stylesheet" type="text/css" /> --%>
	<script type="text/javascript" src="<%=request.getContextPath()%>//javascript/jquery/jquery.pages.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		$("div.holder").jPages({
		    containerID : "content",
		    perPage:10
	  	});
		$('.jp-previous').html('&laquo');
		$('.jp-next').html('&raquo');
	});
	function deleteProject(projectId){
		/* alert(projectId); */
		$("#project_id").val(projectId);
		document.getElementById('delete_project_form').submit();
	}
	function getProject(projectId){
	/* 	alert(projectId); */
		$("#project_id_detail").val(projectId);
		document.getElementById('project_detail_form').submit();
	}
	</script>
	<form hidden="" id="delete_project_form" method="post" action="<%=request.getContextPath() %>/deleteproject.html">
	<input type="hidden" id="project_id" name="projectId" >
	</form>
	<form hidden="" id="project_detail_form" method="post" action="<%=request.getContextPath() %>/projectdetail.html">
	<input type="hidden" id="project_id_detail" name="projectId" >
	</form>
	<div class="container">
		<div class="page-header">
			<span class="search-history-title">Manage Project</span>
		</div>
		
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12">
			<form action="<%=request.getContextPath() %>/createproject.html" commandName="projectDo">
				<label>Project Name :</label>
				<input type="text" name="projectName" id="projectName" />
				<label>Add Collaborator :</label>
				<select name="users" multiple="multiple" size="3">
				<c:forEach var="userList" items="${userList}" varStatus="count">
			<option value="${userList.userId}">${userList.email} </option>
			</c:forEach>
			
				</select>
				<label>Project Notes :</label>
				<input type="text" name="projectNotes" id="projectNotes" />
				<input type="submit" value="Create" />
				</form>
			</div>
		</div>
		<div class="row" style="margin-top: 1%">
			<div class="col-xs-12 col-sm-12 col-md-12">
			<c:if test="${projectList.size() > 0}"> 
			<c:forEach var="projectList" items="${projectList}" varStatus="count">
			<div class="row">
			<a onclick="getProject(${projectList.projectId})" class="col-xs-7 col-sm-7 col-md-7">${count.count}.${projectList.projectName}
			</a>
			<a onclick="deleteProject(${projectList.projectId})" class="col-xs-offset-4 col-sm-offset-4 col-md-offset-4 col-xs-1 col-sm-1 col-md-1">DELETE
			</a>
			</div>
			</c:forEach></c:if>
			<c:if test="${projectList.size() == 0}"><label style="color: blue"> Project not found</label></c:if>
			</div>
		</div> 
		<sec:authorize ifAnyGranted="ELEMNTRYIP_USER">
			<div class="row" style="margin-top: 1%">
			<div class="col-xs-12 col-sm-12 col-md-12">
			<label>Others Project :</label>
			<c:forEach var="projectList" items="${projectColabList}" varStatus="count">
			<div class="row">
			<a onclick="getProject(${projectList.projectId})" class="col-xs-7 col-sm-7 col-md-7">${count.count}.${projectList.projectName}
			</a>
			<%-- <a onclick="deleteProject(${projectList.projectId})" class="col-xs-offset-4 col-sm-offset-4 col-md-offset-4 col-xs-1 col-sm-1 col-md-1">DELETE
			</a> --%>
			</div>
			</c:forEach>
			</div>
		</div>
				</sec:authorize>
		
	</div>
