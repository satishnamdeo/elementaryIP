/*window.name;*/

$(function() {
	$("#searchQuery").popover({
	    placement: 'bottom',
	    title: 'Search Query Suggestion',
	    content: $("#querysuggetion").html(),
	    trigger:'click' ,
	 });
	
	
	/*$('body').click(function(event){
		$('.popover').removeClass('in');
	});*/
	/*if($("#search-result").hasClass("add-scroll")){
		alert("add-scroll");
		$(window).resize(function() {
			var winHeight = $(window).height();
			$("#search-result").css("height",(winHeight-214)+"px");
		});
	}*/
	
	$(window).resize(function() {
		var winHeight = $(window).height();
		$("#search-result").css("height",(winHeight-214)+"px");
	});
		
		$("#searchQuery").click(function(event){
			 event.stopPropagation();
			$('.popover-content').mCustomScrollbar({
				mouseWheelPixels: 500 ,
				theme:"dark"
			});
			$('.popover').css("left","0px");
			$('.popover').draggable({ containment: "body", scroll: false });
			$('.popover').draggable({ containment: "body" });
		});
			
	
	$("#querysuggetion").mCustomScrollbar({
		mouseWheelPixels: 500 ,
		scrollButtons:{
			enable:true,
		},
		advanced: {
            updateOnContentResize: true,
            updateOnBrowserResize: true,
        },
		theme:"dark",
	});
	$('#search-text').keypress(function(event){
		
		var keycode = (event.keyCode ? event.keyCode : event.which);
	    if(keycode == '13'){
	    	$("#search").click();
	    	
	    }else {
	    	
	    };  
	});
	$('#search-text').blur(function(event){
		$('#search-text').mCustomScrollbar('destroy');
	});
	$("#search").click(function(){
		getContent($('#search-text').val(),false);
	});
	
	$("#external_search").click(function(){
		getExternalContent($('#search-text').val(),false);
		/*window.location.replace(location.protocol + "//" + location.host +aapContext+"/externalsearch.html");*/
	});
	

/*	var flag = true;
	$("#suggetionoption").click(function(){
		if(flag){
			$("#querysuggetion").show();
			
			
			var cookies = $.cookie();
			  for(var cookie in cookies) {
			     $.removeCookie(cookie);
			  }
			  $("#querysuggetion").show();
			  $('#search-result').empty();
			
			flag= false;
		}else{
			$("#querysuggetion").hide();
			flag = true;
		}
		 
	});*/
	/******************************* Ajax Call For Patent End***********************************/
	/*alert(window.name);*/
});
/*function selectAll(){
	$(':checkbox').each(function() {
    this.checked = true;
});}*/
var allSelected=false;
function selectAll(){
	
	if(allSelected){
		
	$(':checkbox').each(function() {
    this.checked = false;
	});
	 allSelected=false;
	 document.getElementById("select_all").value="Select All"; 
	}
	else{
		$(':checkbox').each(function() {
	    this.checked = true;
	});
	allSelected=true;
	document.getElementById("select_all").value="Unselect All"; 
	}
	
}
function importPatent(projectid){
	alert(projectid);
	 var patentList = [];
	
	 var patentdiv = $('#patentdiv');
	 var i=0;
	 $('<input type="hidden" name="projectId" id="projectId" value="'+projectid+'"/>').appendTo(patentdiv);
	 

		 $('input:checkbox:checked').each(
			function() {
				var patentSlice = [];

				patentSlice = this.value.split(" ");
				var seqNumber = patentSlice[0].substring(0, patentSlice[0]
						.indexOf("."));
				/* alert(seqNumber);alert(splitString[2]); */
				var patent = {
					"patentId" : patentSlice[2],
					"seqNumber" : seqNumber
				};
				patentList.push(patent);
				
				
			});
	 if(patentList.length>0){
		 var json={
					"patentList" : patentList,
					"projectId" : projectid
				};
		var patentListDo=JSON.stringify(json);
		$.ajax({
			 url: aapContext+"/importpatent.html",
				type : "post",
				data : patentListDo,
				  contentType: "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					alert("sucess");				
				},
		error : function(xhr, ajaxOptions, thrownError) {
			 console.log(xhr.status);
	    	 console.log(thrownError);
		}
	});
		 /*alert("length"+checked.length);*/
		/*document.getElementById('import-patent-form').submit(); */
	
	 }
	
	
}
/*function viewDetail(searchQueryStr) {
	alert("dskfjksldf");
	window.location.replace(location.protocol + "//" + location.host +aapContext+"/"+searchQueryStr+"/viewPatent.html");
}*/

function getContent(searchStr,fromCookie){
	var htmlString="";
	var searchPatentVal  = searchStr;
	$.ajax({
		url : aapContext + "/searchProjectPatent.html",
		type : "post",
		data : {
			projectId:projectid,
			searchQueryStr : searchStr/*.replace(/\\/g, "\\\\")*/,
			fromCookie:fromCookie
		},
		dataType : "json",
		cache: false,
		beforeSend: function() {
			var loaderHtml = "<img id='dataLoader' src='./images/loader.gif' style='margin: 0 auto; display: block;'>";
			$("#search-result").html(loaderHtml);
			$('#search-text').blur();
			$('#search-text').mCustomScrollbar('destroy');
			$('.total-size').hide();
		    if ($('#dataLoader').is(':visible')) {
		    	$("#search-result").removeClass('add-scroll');
		    } else {

		    }
		},
		success : function(data) {
			$("#search-result").empty();
		
			if(data.length==0){
				htmlString = htmlString+"<span>No record found for search keyword</span>";
				$("#search-result").addClass('no-record');
				$("#search-result").removeClass("add-scroll");
				$("#search-result").html(htmlString);
				$('.total-size').show();
				$('.total-size').addClass('patents-size');
				$('.total-size').text(" Total Patents Found : "+data.length);
				/*if ($('#search-result').is(':empty')){
					$("#querysuggetion").show();
				}else{
					$("#querysuggetion").hide();
				}*/
			}else{
				$('.total-size').show();
				$("#search-result").removeClass('no-record');
				if(!fromCookie){
					$.cookie("keyword", searchPatentVal);
					$.cookie("userName", userName);
				}
				$.each(data,function(index,obj){
					
					htmlString = htmlString+
					"<div class='row'>" +
						"<div class='col-md-12'>"+
							"<div class='container-fluid'>"+
								"<div class='row'>" +
									"<div class='col-md-12'>"+
										"<a  class='view-link' href='"+aapContext+"/"+projectid+"/"+obj.US_Patent_Number+"/viewPatent.html'>" +
											"<span>"+
												"<strong>"+obj.id+" "+obj.Title+"</strong>"+
											"</span>"+
										"</a>" +
										/*"<p class='font-9'>GRANT DATE:"+obj.PublicationDate+"&nbsp   &nbsp  &nbsp  FILE DATE: "+obj.FilingDate+"</p>"+*/
									"</div>"+
									/*"<div class='col-md-1'>" +
										"<div class='pull-right' style='margin-top:0;'>" +
											"<a  class='view-link' href='#"+obj.US_Patent_Number+"' onclick='viewDetail("+obj.id+");'></a>" +
										"</div>"+
									"</div>"+*/
								"</div>"+
								/*"<div class='row'>" +
								"<div class='col-md-12'>"+
									"<p class='font-9'>" +
										"<span>"+obj.Abstract+"</span>" +
									"</p>" +
								"</div>"+
							"</div>"+*/
						"</div>"+
					"</div>"+
				"</div>";
					
				});
				/*$("#querysuggetion").hide();*/
				$('.total-size').addClass('patents-size');
				$('.total-size').text(" Total Patents Found : "+data.length);
				//$("#search-result").addClass('inner-block');
				$("#search-result").html(htmlString);
				var winHeight = $(window).height();
				//alert(winHeight);
				$("#search-result").css("height",(winHeight-215)+"px");
				
				var height = $("#search-result").height();
				var count = 2;
				if(height>=410 || data.length>3){
					
					$("#search-result").mCustomScrollbar("destroy");
					//$("#search-result").removeClass("mCustomScrollbar _mCS_"+count+"");
					$("#search-result").addClass("add-scroll");
					$("#search-result").mCustomScrollbar({
						mouseWheelPixels: 500 ,
						scrollButtons:{
							enable:true
						},
						advanced: {
							 autoScrollOnFocus: false,
				            updateOnContentResize: true,
				            updateOnBrowserResize: true
				        },
						theme:"dark"
					});
					count++;
				}else{
					$("#search-result").mCustomScrollbar("destroy");
					$("#search-result").removeClass("add-scroll");
				}
				
				$('#search-text').val(searchPatentVal);
				/*window.name = htmlString;*/
				
			}
		},
		complete: function() {
			 $("#dataLoader").addClass('loading');
	    },
		
		error:function(){
			alert("error");
		}
	});

}
function getExternalContent(searchStr,fromCookie){
	
	var htmlString="";
	var searchPatentVal  = searchStr;
	$.ajax({
		url : aapContext + "/externalsearch.html",
		type : "post",
		data : {
			searchQueryStr : searchStr/*.replace(/\\/g, "\\\\")*/,
			
		},
		dataType : "json",
		cache: false,
		beforeSend: function() {
			var loaderHtml = "<img id='dataLoader' src='./images/loader.gif' style='margin: 0 auto; display: block;'>";
			$("#search-result").html(loaderHtml);
			$('#search-text').blur();
			$('#search-text').mCustomScrollbar('destroy');
			$('.total-size').hide();
		    if ($('#dataLoader').is(':visible')) {
		    	$("#search-result").removeClass('add-scroll');
		    } 
		},
		success : function(data) {
			$("#search-result").empty();
		
			if(data.length==0){
				htmlString = htmlString+"<span>No record found for search keyword</span>";
				$("#search-result").addClass('no-record');
				$("#search-result").removeClass("add-scroll");
				$("#search-result").html(htmlString);
				$('.total-size').show();
				$('.total-size').addClass('patents-size');
				$('.total-size').text(" Total Patents Found : "+data.length);
				/*if ($('#search-result').is(':empty')){
					$("#querysuggetion").show();
				}else{
					$("#querysuggetion").hide();
				}*/
			}else{
				$('.total-size').show();
				$("#search-result").removeClass('no-record');
				if(!fromCookie){
					$.cookie("keyword", searchPatentVal);
					$.cookie("userName", userName);
				}
				/*htmlString=	"<span id=\"select_all\" class=\"input-group-addon\">"+
				"<button class=\"btn search-button\" id=\"select_all\"></button>"+
			"</span>";*/
				htmlString="<input type=\"button\" name=\"select\" value=\"Select All\"id=\"select_all\" onclick=\"selectAll()\"  />"+
				           "<input type=\"button\" name=\"select\" value=\"Import\"id=\"select_all\" onclick=\"importPatent(projectid)\" />";
				var i=0
				$.each(data,function(index,obj){
					
					htmlString = htmlString+
					"<div class='row'>" +
						"<div class='col-md-12'>"+
							"<div class='container-fluid'>"+
								"<div class='row'>" +
									"<div class='col-md-12'>"+
									"<input type=\"checkbox\" name=\"patent\" value=\""+obj.id +"\"/>"+
										"<span  class='view-link'  style=\"margin-left:20px\">" +
											"<span>"+
												"<strong>"+obj.id+" "+obj.Title+"</strong>"+
											"</span>"+
										"</span>" +
										/*"<p class='font-9'>GRANT DATE:"+obj.PublicationDate+"&nbsp   &nbsp  &nbsp  FILE DATE: "+obj.FilingDate+"</p>"+*/
									"</div>"+
									/*"<div class='col-md-1'>" +
										"<div class='pull-right' style='margin-top:0;'>" +
											"<a  class='view-link' href='#"+obj.US_Patent_Number+"' onclick='viewDetail("+obj.id+");'></a>" +
										"</div>"+
									"</div>"+*/
								"</div>"+
								/*"<div class='row'>" +
								"<div class='col-md-12'>"+
									"<p class='font-9'>" +
										"<span>"+obj.Abstract+"</span>" +
									"</p>" +
								"</div>"+
							"</div>"+*/
						"</div>"+
					"</div>"+
				"</div>";
				i++;	
				});
				/*$("#querysuggetion").hide();*/
				$('.total-size').addClass('patents-size');
				$('.total-size').text(" Total Patents Found : "+data.length);
				//$("#search-result").addClass('inner-block');
				
				$("#search-result").html(htmlString);
				var winHeight = $(window).height();
				//alert(winHeight);
				$("#search-result").css("height",(winHeight-215)+"px");
				
				var height = $("#search-result").height();
				var count = 2;
				if(height>=410 || data.length>3){
					
					$("#search-result").mCustomScrollbar("destroy");
					//$("#search-result").removeClass("mCustomScrollbar _mCS_"+count+"");
					$("#search-result").addClass("add-scroll");
					$("#search-result").mCustomScrollbar({
						mouseWheelPixels: 500 ,
						scrollButtons:{
							enable:true
						},
						advanced: {
							 autoScrollOnFocus: false,
				            updateOnContentResize: true,
				            updateOnBrowserResize: true
				        },
						theme:"dark"
					});
					count++;
				}else{
					$("#search-result").mCustomScrollbar("destroy");
					$("#search-result").removeClass("add-scroll");
				}
				
				$('#search-text').val(searchPatentVal);
				/*window.name = htmlString;*/
				
			}
		},
		complete: function() {
			 $("#dataLoader").addClass('loading');
	    },
		
		error:function(){
			alert("error");
		}
	});
	
}