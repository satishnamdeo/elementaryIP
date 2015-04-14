
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="<%=request.getContextPath()%>//javascript/jquery/jquery.cookie.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css">
<script type="text/javascript">
	var aapContext='<%= request.getContextPath() %>';
	
	$(function(){
		var cookies = $.cookie();
		for(var cookie in cookies) {
		   $.removeCookie(cookie);
		}
		$("#myform").submit(function(e) {
			var forgotPasswordAPI=aapContext+"/forgotpassword.html";
        	$.ajax({
				url: forgotPasswordAPI,
				data: {email : $("#userId").val()},
				type: "POST",
			     beforeSend: function(){
			    	 e.preventDefault();
			    	 $("#notice_box").addClass("notice");
			 		 $("#notice_box").removeClass("alert");
			    	 $("#notice_bar").css("display","block");
			 		 $("#notice_box").text(" Sending...");
			 	},
				success: function(json){
					
					 var state=json.status;
					
				    	if(state=="sucess"){
				    		$('.close').click();
				    		
				    		 $("#notice_bar").css("display","block");
				    			 $("#notice_box").addClass("notice");
				    			 $("#notice_box").removeClass("alert");
						 		 $("#notice_box").text("A mail has been sent with new password to your email.");
				    			setTimeout(function(){
				    				$('#notice_bar').fadeOut();
				    			}, 4000); 
				    	 }
				    	 else{
				    		
				    		 $("#notice_bar").css("display","block");
					 		 $("#notice_box").text("There is a problem in sending your mail");
					 		 $("#notice_box").addClass("alert");
					 		 $("#notice_box").removeClass("notice");
					 		 setTimeout(function(){
					 			 $('#notice_bar').fadeOut();
				    			}, 5000); 
				    	 }
					
				},
				error: function(err){
					 $("#notice_bar").css("display","block");
			 		 $("#notice_box").text("There is a problem in sending your mail");
			 		 $("#notice_box").addClass("alert");
			 		 $("#notice_box").removeClass("notice");
			 		 setTimeout(function(){
			 			 $('#notice_bar').fadeOut();
		    			}, 5000); 
					
				}
			 });
		});
			
	});
		   
	
		
</script>
	<div class="container">
		<img class="img-responsive logo-img" src="./images/logo1.png">
		<form id="login-form" class="form-signin" method="post" name="f" style="display: block;" action="<c:url value='/j_spring_security_check' />" method="post">
	    	<div class="login-title"> Login to your account! </div>
	       	<div class="login-error">
					<c:if test="${not empty login_error}">
     			 		<div style="height: 15px; color: #ff0000;">${login_error}</div>
     			 		<% session.removeAttribute("login_error"); %>
   					</c:if> 
   			</div>
	        
			<input type="email" class="form-control" placeholder="User ID" required autofocus id="j_username" name="j_username" />
	        
	        <input type="password" id="j_password" name="j_password"  class="form-control"   placeholder="Password" required>
	        <div class="checkbox">
	           <input type="checkbox"  name="j_spring_security_remember_me" id="j_spring_security_remember_me" value="remember-me">Remember me !
	           <a data-toggle="modal" href="#forgotpassword">Forgot Password?</a>
	        </div>
	       	<input type="submit" name="submit" value="Sign In" class="btn btn-lg btn-primary btn-block" id="login-submit-button" />
      	</form>

    </div>
 
 
 <div class="modal fade" id="forgotpassword" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form id="myform">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title" id="myModalLabel">Forgot Password</h4>
				</div>
				<div class="modal-body">
					<input type="email" class="form-control modal-field" placeholder="User ID" autofocus required id="userId" name="userId" />	
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button class="btn blue-btn">Reset Password</button>
				</div>
			</form>
			
		</div>
	</div>
</div>