<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page import="com.eip.util.ApplicationConstant"%>
<script src="<%=request.getContextPath()%>/javascript/header.js"></script>
<%
response.setHeader("pragma", "no-cache");              
response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");             
response.setHeader("Expires", "0"); 
%>
<script type="text/javascript">
$(function() {
	$("#user-name span").text("Welcome "+firstName+" !");
});
</script>	
<%-- <div class="container-fluid">
	<div class="row">
		<div class="col-md-6">
			<a class="navbar-brand" href="<%=request.getContextPath() %>/home.html"><img src="<%=request.getContextPath()%>/images/innerlogo.png"></a>
		</div>
		<div class="col-md-6">
			<div class="pull-right user">
				<ul class="user-profile">
					<li>
	              		<a href="#" id="user-name"></a>
	              	</li>
	              	<li class="logout">
	              		
						<a href="<%=request.getContextPath() %>/j_spring_security_logout"><span></span>Logout</a>
					</li>
				</ul>
			 
			</div>
			
		</div>
	</div>
</div> --%>
<nav id="nav" class="navbar navbar-default " role="navigation">
	<div class="container-fluid">
		
		<div class="navbar-header">
			<button class="navbar-toggle collapsed" data-target="#navbar-main" data-toggle="collapse" type="button">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<%=request.getContextPath() %>/home.html">
				<img src="<%=request.getContextPath()%>/images/logo.png">
			</a>
		</div>
		<div  class="navbar-collapse collapse" id="navbar-main">
			
			<ul class="nav navbar-nav navbar-right">
				<li>
					<a href="<%=request.getContextPath() %>/home.html">Home</a>
				</li>
				<li>
					<a  href="<%=request.getContextPath() %>/view_history.html">Search History</a>
				</li>
				<%-- <sec:authorize ifAnyGranted="ELEMNTRYIP_ADMIN"> --%>
				<li>
					<a data-toggle="modal" href="<%=request.getContextPath() %>/manageproject.html">Manage Project</a>
				</li>
				<%-- </sec:authorize> --%>
				<li>
	            	<a href="#" id="user-name">
	            		<span></span>
	            	</a>
	            </li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						<img src="<%=request.getContextPath()%>/images/setting.png">
						<!-- <span class="caret"></span> -->
					</a>
					<ul class="dropdown-menu" role="menu">
						<sec:authorize ifAnyGranted="ELEMNTRYIP_ADMIN">
							<li>
								<a data-toggle="modal" href="#addNewUser">Create User</a>
							</li>
						</sec:authorize> 
						<li>
							<a data-toggle="modal" href="#changePassword">Change Password</a>
						</li>
						<li class="logout">
							<a href="<%=request.getContextPath() %>/j_spring_security_logout"><span></span>Logout</a>
						</li>
					</ul>
				</li>
			</ul>
         
		</div>
	</div>
</nav>

<div class="modal fade" id="addNewUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
				<form id="userform">				
					<input type="hidden" name="roleCode" id="roleCode"	value="<%=ApplicationConstant.ROLE_ELEMNTRYIP_USER%>" />								
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
						<h4 class="modal-title" id="myModalLabel">Add New User</h4>
					</div>
					<div class="modal-body">
						<input type="text" class="form-control modal-field" placeholder="First Name" required autofocus id="firstname" name="firstname" />
						<input type="text" class="form-control modal-field" placeholder="Last Name" required autofocus id="lastname" name="lastname" />
						<input type="email" class="form-control modal-field" placeholder="User ID" required autofocus id="username" name="username" />
						<input type="hidden" name="isActive" id="isActive" value="true">
					</div>
					<div class="modal-footer">
						<button id="adduser" class="blue-btn">Save changes</button>
					</div>
				</form>
		</div>
	</div>
</div>

<div class="modal fade" id="changePassword" tabindex="-2" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
				<form id="reset-password-form">				
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
						<h4 class="modal-title" id="myModalLabel">Change Password</h4>
					</div>
					<div class="modal-body">
						<input type="password" class="form-control modal-field" placeholder="Old Password" required autofocus id="oldpassword" name="oldpassword" />
						<input type="password" class="form-control modal-field" placeholder="New Password" required autofocus id="newpassword" name="newpassword" />
						<input type="password" class="form-control modal-field" placeholder="Confirm Password" required autofocus id="cpassword" name="cpassword" />
					</div>
					<div class="modal-footer">
						<button id="adduser" class="blue-btn">Save changes</button>
					</div>
				</form>
			<%-- </form:form> --%>
		</div>
	</div>
</div>	