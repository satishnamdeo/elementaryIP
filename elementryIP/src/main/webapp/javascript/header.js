
$(function(){
	/* affix the navbar after scroll below header */
	$('#nav').affix({
	      offset: {
	        top: 45
	      }
	});	

	/* highlight the top nav as scrolling occurs */
	$('body').scrollspy({ target: '#nav' });

	/* smooth scrolling for scroll to top */
	$('.scroll-top').click(function(){
	  $('body,html').animate({scrollTop:0},1000);
	});

	/* smooth scrolling for nav sections */
	/*$('#nav .navbar-nav li>a').click(function(){
	  var link = $(this).attr('href');
	  var posi = $(link).offset().top+20;
	  $('body,html').animate({scrollTop:posi},700);
	});*/
	
	$(".logout").click(function(){
		var cookies = $.cookie();
		  for(var cookie in cookies) {
		     $.removeCookie(cookie);
		  }
	
	});
	
	
	
	$("#userform").submit(function(e) {
		
		var userDo={
				firstName : $("#firstname").val(),
				lastName : $("#lastname").val(),
				email : $("#username").val(),
				roleCode : $("#roleCode").val(),
				isActive : $("#isActive").val()
			};
		
		$.ajax({
			url : aapContext + "/adduser.html",
			type : "post",
			beforeSend: function(){
		    	 e.preventDefault();
		    	 $("#notice_box").addClass("notice");
		 		 $("#notice_box").removeClass("alert");
		    	 $("#notice_bar").css("display","block");
		 		 $("#notice_box").text(" Creating...");
		 	},
			data :JSON.stringify(userDo),
			dataType : "json",
			contentType: "application/json; charset=utf-8",
			success : function(data) {
				
				var state=data.status;
				
		    	if(state=="success"){
		    		$('.close').click();
		    		
		    		 $("#notice_bar").css("display","block");
		    			 $("#notice_box").addClass("notice");
		    			 $("#notice_box").removeClass("alert");
				 		 $("#notice_box").text("User account created successfully.");
		    			setTimeout(function(){
		    				$('#notice_bar').fadeOut();
		    			}, 4000); 
		    	 }
		    	 else{
		    		
		    		 $("#notice_bar").css("display","block");
			 		 $("#notice_box").text("There is a problem for creating user account");
			 		 $("#notice_box").addClass("alert");
			 		 $("#notice_box").removeClass("notice");
			 		 setTimeout(function(){
			 			 $('#notice_bar').fadeOut();
		    			}, 5000); 
		    	 }
				
				
			},error:function(){
				 $("#notice_bar").css("display","block");
				 $("#notice_box").text("There is a problem for creating user account");
		 		 $("#notice_box").addClass("alert");
		 		 $("#notice_box").removeClass("notice");
		 		 setTimeout(function(){
		 			 $('#notice_bar').fadeOut();
	    			}, 5000); 
			}
		});
		
	});
	
	
$("#reset-password-form").submit(function(e) {
		
		var passwordDo={
				currentPassword : $("#oldpassword").val(),
				newPassword : $("#newpassword").val(),
			};
			if($("#newpassword").val()==$("#cpassword").val()){
				$.ajax({
					url : aapContext + "/updatepassword.html",
					type : "post",
					beforeSend: function(){
				    	 e.preventDefault();
				    	 $("#notice_box").addClass("notice");
				 		 $("#notice_box").removeClass("alert");
				    	 $("#notice_bar").css("display","block");
				 		 $("#notice_box").text(" Processing...");
				 	},
					data :JSON.stringify(passwordDo),
					dataType : "json",
					contentType: "application/json; charset=utf-8",
					success : function(data) {
						
						var state=data.status;
						
				    	if(state=="success"){
				    		$('.close').click();
				    		 $("#notice_bar").css("display","block");
				    			 $("#notice_box").addClass("notice");
				    			 $("#notice_box").removeClass("alert");
						 		 $("#notice_box").text(data.message);
				    			setTimeout(function(){
				    				$('#notice_bar').fadeOut();
				    			}, 4000); 
				    	 }
				    	 else{
				    		
				    		 $("#notice_bar").css("display","block");
					 		 $("#notice_box").text(data.message);
					 		 $("#notice_box").addClass("alert");
					 		 $("#notice_box").removeClass("notice");
					 		 setTimeout(function(){
					 			 $('#notice_bar').fadeOut();
				    			}, 5000); 
				    	 }
						
						
					},error:function(){
						 $("#notice_bar").css("display","block");
						 $("#notice_box").text("Authentication failed");
				 		 $("#notice_box").addClass("alert");
				 		 $("#notice_box").removeClass("notice");
				 		 setTimeout(function(){
				 			 $('#notice_bar').fadeOut();
			    			}, 5000); 
					}
				});
			}else{
				 e.preventDefault();
				 $("#notice_bar").css("display","block");
				 $("#notice_box").addClass("alert");
		 		 $("#notice_box").removeClass("notice");
		 		 $("#notice_box").text("New Password and confirm password does not matching");
		 		 setTimeout(function(){
		 			 $('#notice_bar').fadeOut();
	    			}, 5000); 
			}
		
	});
		
});

function viewDetail(id) {
	$("#searchQueryStr").val(id);
	$("#patent-details-form").submit();
}
