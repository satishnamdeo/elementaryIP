var pageContent;
var oldTagName;
var listOfAnnotations=[];
var totalAnnotation;
var claimIndependentNo;
if (!Date.prototype.toISOString) {
      Date.prototype.toISOString = function() {
        function pad(n) { return n < 10 ? '0' + n : n; }
        return this.getUTCFullYear() + '-'
            + pad(this.getUTCMonth() + 1) + '-'
                + pad(this.getUTCDate()) + 'T'
                    + pad(this.getUTCHours()) + ':'
                        + pad(this.getUTCMinutes()) + ':'
                            + pad(this.getUTCSeconds()) + 'Z';
      };
    }

    // Please note that you would *never* expose the consumer secret in this way
    // for an ordinary application. For the purposes of convenience for this demo, we do.
    var demoConsumerKey = 'd4c108122b51434aab1d27ad4ebd2b02';
    var demoConsumerSecret = '36977e7b-be7f-4b57-a9eb-9617e4740b6a';
   
    
   
    function getAuthToken(consumerKey, consumerSecret,annotatorUsername) {
      "use strict";

      var date = new Date(new Date() - 1 * 60 * 60 * 1000), // 1 hour ago
          alg = { typ:'JWT', alg:'HS256' },
          payload = {
            consumerKey: consumerKey,
            userId: annotatorUsername,
            issuedAt: date.toISOString(),
            ttl: 86400
          },
          token;

      alg = JSON.stringify(alg);
      payload = JSON.stringify(payload);
      token = new jwt.WebToken(payload, alg);

      return token.serialize(consumerSecret);
    }

    function clearAnnotation(){
		$.ajax({
			 url:"http://annotateit.org/api/search?uri="+encodeURIComponent(window.location)+"&limit="+totalAnnotation,
			 Accept:"application/json, text/javascript, */*; q=0.01",
			 async:false,
			 headers:
				{"x-annotator-auth-token":getAuthToken(demoConsumerKey, demoConsumerSecret,"ElementaryAdmin")},
			 success:function(arrayOfAnnotations){
				 //listOfAnnotations= arrayOfAnnotations;
				 //alert("arrayOfAnnotations.total 1 :"+arrayOfAnnotations.total);
				 $.each(arrayOfAnnotations.rows,function(index,object){
					 console.log(object.id);
				  listOfAnnotations.push(object);
				 });
			 },
		});
		
		//one by one delete annotation
		$.each(listOfAnnotations,function(index,object){
			 //console.log(annotationId);
			 $.ajax({
				 	url :"http://annotateit.org/api/annotations/"+object.id,
					Accept:"application/json, text/javascript, */*; q=0.01",
					headers:{
						//"Referer":encodeURIComponent(window.location),
						"x-annotator-auth-token":getAuthToken(demoConsumerKey, demoConsumerSecret,object.user)},
					beforeSend: function() {
							$(".delete-loader").show();	},
					type : "DELETE",
					dataType : "json",
					contentType:"application/json",
					async:false,
					crossDomain:true,
					cache: false,
					success : function(annotation) {
						if(index+1==totalAnnotation){
							 $("#notice_bar").css("display","block");
			    			 $("#notice_box").addClass("notice");
			    			 $("#notice_box").removeClass("alert");
					 		 $("#notice_box").text("All Annotation deleted from cloud successfully for current patent.");
				    		 setTimeout(function(){
				    			$('#notice_bar').fadeOut();
				    		 }, 4000);
				    		 $('body').find('*').each(function(){
				    			// console.log("i "+i);
				    			 if($(this).hasClass('annotator-hl custom-claim')){
				    				 console.log("custom ");
				    				 $(this).removeClass('annotator-hl custom-claim');
				    			 }
					    		 if($(this).hasClass('annotator-hl claim-preamble')){
					    			 console.log("preamble ");
					    			 $(this).removeClass('annotator-hl claim-preamble');
					    		 }
					    		 if($(this).hasClass('annotator-hl feed-back')){
					    			 console.log("feed-back ");
					    			 $(this).removeClass('annotator-hl feed-back');
					    		 }
					    		 if($(this).hasClass('annotator-hl claim-elements')){
					    			 console.log("claim-elements ");
					    			 $(this).removeClass('annotator-hl claim-elements');
					    		 }
					    		 if($(this).hasClass('annotator-hl element-attributes')){
					    			 console.log("element-attributes ");
					    			 $(this).removeClass('annotator-hl element-attributes');
					    		 }
					    		 if($(this).hasClass('annotator-hl claim-dictionary')){
					    			 console.log("dictionary");
					    			 $(this).removeClass('annotator-hl claim-dictionary');
					    		 }
					    		 if($(this).hasClass('annotator-hl claim-embodiment')){
					    			 console.log("embodiment");
					    			 $(this).removeClass('annotator-hl claim-embodiment');
					    		 }
				    		});
				    		 $(".delete-loader").hide(); 
				    		 location.reload();
							// location.reload();
						}
					},
					error:function(){
						//alert("error");
					}
				});
		 });
	}
    
    
    
    
    
    
    
    

 $(function() {
	 
	 
	/* $('.highlight-adder button').mousedown(function(e){
		 e.preventDefault();
		 $('.annotator-adder button').click();
	 });*/
	 
	 	 	
	 $('.element-suggestion').affix({
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
	 
		 var editorHtml = ""; 
			editorHtml = editorHtml+
				"<li class='annotator-item'>"+
					"<textarea id='annotator-field' placeholder='Dictonary'></textarea>"+
				"</li>"	;
		 
		var	content = $('#search-details').annotator().annotator('setupPlugins', null, {
	    	Auth:{
	    		token: getAuthToken(demoConsumerKey, demoConsumerSecret,userName)
	    	},
	    	Filter:false,
	    	Annotations:false
		 }).annotator("addPlugin", "CustomHighlighter");
		 
		
		/*$('highlight-adder button').mousedown(function(event){
			event.stopPropagation();
			alert('dsvgkhj');
		});*/
  
	 /********************************* More Link******************************************/
	 	
	
	
		$("#searchQuery").popover({
		    placement: 'bottom',
		    title: 'Search Query Suggestion',
		    content: $("#querysuggetion").html(),
		    trigger:'click'
		 });
		/*$('body').click(function(event){
			$('.popover').removeClass('in');
		});*/
		
		
			
			$("#searchQuery").click(function(event){
				 event.stopPropagation();
				$('.popover-content').mCustomScrollbar({
					theme:"dark"
				});
				$('.popover').css("left","0px");
				$('.popover').css("left","0px");
				$('.popover').draggable({ containment: "body", scroll: false });
				$('.popover').draggable({ containment: "body" });
				
			});
				
		
		$("#querysuggetion").mCustomScrollbar({
			scrollButtons:{
				enable:true
			},
			advanced: {
	            updateOnContentResize: true,
	            updateOnBrowserResize: true
	        },
			theme:"dark"
		});
		$('#search-text').keypress(function(event){
			
			var keycode = (event.keyCode ? event.keyCode : event.which);
		    if(keycode == '13'){
		    	$("#search").click();
		    	
		    }else {
		    	
		    }  
		});
		$('#search-text').blur(function(event){
			$('#search-text').mCustomScrollbar('destroy');
		});
	
	 $("#search").click(function(){
		/* alert("in search details.js");*/
		search();
	 });
	 $.ajax({
		 url:"http://annotateit.org/api/search?uri="+encodeURIComponent(window.location),
		 Accept:"application/json, text/javascript, */*; q=0.01",
		 headers:
			{"x-annotator-auth-token":getAuthToken(demoConsumerKey, demoConsumerSecret,"ElementaryAdmin")},
		 success:function(arrayOfAnnotations){
			 	totalAnnotation=arrayOfAnnotations.total;
				if(arrayOfAnnotations.total==0){
					//$(".delete-loader").show();
					$.each(segmentList,function(index,segment){ 
						var startOffset = segment.startOffset;//areatext.indexOf(segment.quote);
						var endOffset   = segment.endOffset;//startOffset+segment.quote.length;
						var start;
						if(segment.area==title){
							start="/div[1]/div[1]/div[1]/div[1]";
						}else if(segment.area==publicationDate){
							start="/div[1]/div[1]/div[2]/div[1]/div[1]";//"/div[1]/div[1]/div[2]/div[1]";
						}else if(segment.area==filingDate){
							start="/div[1]/div[1]/div[2]/div[2]/div[1]";//"/div[1]/div[1]/div[2]/div[2]";
						}else if(segment.area==abstrac){
							start="/div[1]/div[1]/div[4]/div[1]";
						}else if(segment.area==feedback){
							if(segment.quote.trim()=="Relevance"){
								start="/div[1]/div[1]/div[8]/span[2]";
							}
							if(segment.quote.trim()=="Segmentation"){
								start="/div[1]/div[1]/div[8]/span[3]";
							}
							if(segment.quote.trim()=="ClaimStructure"){
								start="/div[1]/div[1]/div[8]/span[4]";
							}
							if(segment.quote.trim()=="ElementLength"){
								start="/div[1]/div[1]/div[8]/span[5]";
							}
							if(segment.quote.trim()=="ClaimType"){
								start="/div[1]/div[1]/div[8]/span[6]";
							}
							if(segment.quote.trim()=="PunctuationStyle"){
								start="/div[1]/div[1]/div[8]/span[7]";
							}
							if(segment.quote.trim()=="Other feedback"){
								start="/div[1]/div[1]/div[8]/span[8]";
							}
							
						}else if(segment.area.indexOf("c")==0){
							//alert("segment area startwith c : "+segment.area.startsWith("c"));
							var numberDiv = segment.area.split('c')[1].split('_')[0];
							start="/div[1]/div[1]/div[9]/div["+numberDiv+"]";
						}else if(segment.area.indexOf("d")==0){
							var numberDiv = segment.area.split('d')[1].split('_')[0];
							start="/div[1]/div[1]/div[10]/div["+numberDiv+"]";
						}
						// For Automatic Annotation 
						var currentLocation = location.href;
						var autoData ={
								"permissions": {
									"read": ["group:__world__"],
									"update": [],
									"delete": [],
									"admin": []
								},
								"user": segment.username,//'Elementary Admin',
								consumer: demoConsumerKey,
								"ranges": [{
									"start":start,
									"startOffset": startOffset,
									"end":start,
									"endOffset": endOffset
								}],
								"quote": segment.quote,
								"text": segment.comment,
								"tags": [segment.tagname],
								"uri": currentLocation
							};
						if(arrayOfAnnotations.total==0){
							
							$.ajax({
								url : "http://annotateit.org/api/annotations",
								type : "POST",
								data :JSON.stringify(autoData),
								dataType : "json",
								contentType:"application/json; charset=UTF-8",
								Accept:"application/json, text/javascript, */*; q=0.01",
								async:false,
								cache: false,
								headers:{"x-annotator-auth-token":getAuthToken(demoConsumerKey, demoConsumerSecret,segment.username)},//"Elementary Admin")},
								success : function(annotation) {
									if(index==segmentList.length-1){
										 $("#notice_bar").css("display","block");
						    			 $("#notice_box").addClass("notice");
						    			 $("#notice_box").removeClass("alert");
								 		 $("#notice_box").text("Automatic annotation created successfully.");
							    		 setTimeout(function(){
							    			$('#notice_bar').fadeOut();
							    		 }, 40000);
										 location.reload();
									}
								},
								error:function(){
									alert("error");
								}
							});
						}
					});
				 }
		 },
		 error:function(){
			alert("error");
		 }
	 });
	 
	 
	 
	
});
 
 
 
 
 
 
 
 Annotator.Plugin.CustomHighlighter = function(element) {
	 	var myPlugin = {};
		myPlugin.pluginInit = function() {
			myPlugin.annotator.subscribe("annotationsLoaded", function(arrayOfAnnotations) {
				$.each(arrayOfAnnotations, function(i) {
					var annotation = arrayOfAnnotations[i];
					//$.each(annotation.highlights, function(j) {
					console.info("The annotation:  ", annotation);	
						if(annotation.tags[0]){
							switch (annotation.tags[0] )
							{
								case claims[0]:
									$.each(annotation.highlights, function(j) {
										$(this).removeClass();
										$(this).addClass('annotator-hl custom-claim');
									});
							        break;
								case claims[1]:
									$.each(annotation.highlights, function(j) {
										$(this).removeClass();
										$(this).addClass('annotator-hl claim-preamble');
									});
						            break;
								case claims[2]:
									$.each(annotation.highlights, function(j) {
										$(this).removeClass();
										$(this).addClass('annotator-hl preamble-attributes');
									});
						            break;
								case claims[3]: 
									$.each(annotation.highlights, function(j) {
										$(this).removeClass();
										$(this).addClass('annotator-hl claim-elements');
									});
							        break;
								case claims[4]:
									$.each(annotation.highlights, function(j) {
										$(this).removeClass();
										$(this).addClass('annotator-hl element-attributes');
									});
							         break;
							    case claims[5]:
							    	$.each(annotation.highlights, function(j) {
							    		$(this).removeClass();
							    		$(this).addClass('annotator-hl feed-back');
							    	});
							        break;
							    case claims[6]:
							    	$.each(annotation.highlights, function(j) {
							    		$(this).removeClass();
							    		$(this).addClass('annotator-hl claim-dictionary');
							    	});
							        break;
							    case claims[7]:
							    	$.each(annotation.highlights, function(j) {
							    		$(this).removeClass();
							    		$(this).addClass('annotator-hl claim-embodiment');
							    	});
							        break;
								default: 
									$(this).removeClass();
									$(this).addClass('annotator-hl');
									
							}
						}
						
					//});
					
				});
				
				
			});
			myPlugin.annotator.subscribe("annotationCreated", function(annotation) {
				console.info("The annotation: %o has just been created!", annotation);		
				/*$("#search-details").removeHighlight();*/
				customhighlight(annotation);
				
				
			
			});
			myPlugin.annotator.subscribe("annotationUpdated", function(annotation) {
				$.each(annotation.highlights, function(i) {
					//var count;
					claimIndependentNo = $(this).closest('div').attr('id');
					switch (annotation.tags[i]/*.toLowerCase()*/)
					{
						case claims[0]:
							$(this).removeClass();
							$(this).addClass('annotator-hl custom-claim');
							keyname =annotation.tags[i];
					        break;
						case claims[1]:
							$(this).removeClass();
							$(this).addClass('annotator-hl claim-preamble');
							keyname =annotation.tags[i];
							//count=$(".claim-preamble").length-1;
				            break;
						case claims[2]:
							$(this).removeClass();
							$(this).addClass('annotator-hl preamble-attributes');
							keyname =annotation.tags[i];
							//count=$(".preamble-attributes").length-1;
				            break;
						case claims[3]: 
							$(this).removeClass();
							$(this).addClass('annotator-hl claim-elements');
							keyname =annotation.tags[i];
							//count=$(".claim-elements").length-1;
					        break;
						case claims[4]:
							$(this).removeClass();
							$(this).addClass('annotator-hl element-attributes');
							keyname =annotation.tags[i];
							//count=$(".element-attributes").index();//$(".element-attributes").length-1;
					        break;
						case claims[5]:
						    $(this).removeClass();
							$(this).addClass('annotator-hl feed-back');
							keyname =annotation.tags[i];
						    break;
						case claims[6]:
						    $(this).removeClass();
							$(this).addClass('annotator-hl claim-dictionary');
							keyname =annotation.tags[i];
						    break;
						case claims[7]:
					    	$(this).removeClass();
							$(this).addClass('annotator-hl claim-embodiment');
							keyname =annotation.tags[i];
					        break;
						default: 
							$(this).removeClass();
							$(this).addClass('annotator-hl');
							
					}
					
					var patentSegment={
							patentId:patentID,
							claimIndependentNo:claimIndependentNo,
							keyname:keyname,
							keyvalue:annotation.quote,
							comment:annotation.text,
							//tagCount:count,
							startOffset:annotation.ranges[0].startOffset,
							endOffset:annotation.ranges[0].endOffset,
							currentUser:annotation.user/*.replace(/\s/g, '')*/,
							oldTagName:oldTagName
							//oldUser:oldUser
							
					};
					//alert(JSON.stringify(patentSegment));
					$.ajax({
						url : aapContext + "/editpatent_segment.html",
						type : "POST",
						data :JSON.stringify(patentSegment),
						dataType : "json",
						contentType: "application/json; charset=utf-8",
						cache: false,
						success : function(data) {
							 $("#notice_bar").css("display","block");
			    			 $("#notice_box").addClass("notice");
			    			 $("#notice_box").removeClass("alert");
					 		 $("#notice_box").text(""+keyname+" annotation updated successfully.");
				    		 setTimeout(function(){
				    			$('#notice_bar').fadeOut();
				    		 }, 4000);
				    			
						},
						error:function(){
							alert("error");
						}
					});
				});
				//console.info("The annotation: %o has just been updated!", annotation);
				
				
			});
			
			
			myPlugin.annotator.subscribe("annotationDeleted", function(annotation) {
				//console.info("The annotation: %o has just been deleted by me!", annotation);
				if(annotation.tags){
					var patentSegment={
							patentId:patentID,
							keyname:annotation.tags[0],
							keyvalue:annotation.quote,
							comment:annotation.text,
							currentUser:annotation.user,
							startOffset:annotation.ranges[0].startOffset,
							endOffset:annotation.ranges[0].endOffset
					};
					//alert(JSON.stringify(annotation));
					$.ajax({
						url : aapContext + "/deletepatent_segment.html",
						type : "POST",
						data :JSON.stringify(patentSegment),
						dataType : "json",
						contentType: "application/json; charset=utf-8",
						cache: false,
						success : function(data) {
							 $("#notice_bar").css("display","block");
			    			 $("#notice_box").addClass("notice");
			    			 $("#notice_box").removeClass("alert");
					 		 $("#notice_box").text(""+annotation.tags[0]+" annotation deleted successfully.");
				    		 setTimeout(function(){
				    			$('#notice_bar').fadeOut();
				    		 }, 4000);
				    			
						},
						error:function(){
							alert("error");
						}
					});
				}
				
			});
			
			/*myPlugin.annotator.subscribe("beforeAnnotationCreated",function(annotation){
				//alert("Editor Click");
				console.info("The annotation: %o has just been deleted!", annotation);
				$(".highlight-adder").css("width","0");
			});*/
			
			myPlugin.annotator.subscribe("annotationViewerShown", function(viewer, annotation) {
				//console.info("The annotation: %o has just been deleted!", annotation);	
				
				$.each(annotation, function(i,v){
					//console.info("The annotation: %o has just been deleted!"+ v.user);	
					var annotatorCreator = "<div class='annotator-user' style='display:none;'>"+v.user+"</div>";
					
					$('.annotator-viewer .annotator-item').append(annotatorCreator);
					
					
					
				});
				
				$('.annotator-viewer .annotator-widget li').each(function(i,v){
					/*$(".annotator-viewer .annotator-widget li #"+i+"").show(); */
					$($(v).children( ".annotator-user" )[i]).show();
					
				});
			});
			myPlugin.annotator.subscribe("annotationEditorShown", function(editor, annotation) {
				$(".annotator-checkbox").hide();
				//$("#search-details").removeHighlight();
				if(annotation.tags){
					oldTagName=annotation.tags[0];
					//oldUser=annotation.user;
				}
				$.each(annotation, function(i,v){
					if($('.annotator-editor .annotator-item input').type!="checkbox" ){
						$(".annotator-editor .annotator-item:nth-child(2) input").addClass("tag-suggetion");
					}
					$(".ui-autocomplete").addClass('autocomplete-menu');
					
					
					$(".tag-suggetion").focus(function(){
						$(".tag-suggetion").keydown();
					});
					
					
					
					$(".tag-suggetion").attr('type','text');
					$(".tag-suggetion").attr('onkeypress','return isSpace(event);');
						
					
					
				
					
					
					//var claims =['Custom','Element', 'ElementAttribute', 'Preamble', 'PreambleAttribute'];
					
	                   $(".tag-suggetion").autocomplete({
	                     source: claims,
	                     minLength: 0
	                   }) .focus(function(){            
				            $(this).trigger('keydown.autocomplete');
				        });
					
					
	                   
	                   
	                   
				});
			});
		};
		return myPlugin;
	};


	
	function isSpace(evt)
	{
	    var e = evt ; // for trans-browser compatibility
	    var charCode = e.which || e.keyCode;
	   
	    if(charCode==8 || charCode==46 || charCode==13){
	    	 return true;
	    }
	    return false;
	}

	
	function customhighlight(annotation) {
		var tagName = "";
		var keyname;
		var patentDo;
		var claimIndependentNo="";
		if(annotation.tags!=""){
			
		$.each(annotation.tags, function(i) {
			
			$.each(annotation.highlights.reverse(), function(j) {
				var highlight = annotation.highlights[j];
				claimIndependentNo = $(this).closest('div').attr('id');
				if(annotation.tags[i]){
				
					switch (annotation.tags[i]/*.toLowerCase()*/)
					{
						case claims[0]:
													
								$(highlight).removeClass();
								$(highlight).addClass('annotator-hl custom-claim');
								tagName =annotation.tags[i];							
								keyname =annotation.tags[i];
					        break;
						case claims[1]:
															
								$(highlight).removeClass();
								$(highlight).addClass('annotator-hl claim-preamble');
								tagName =annotation.tags[i];	
								keyname =annotation.tags[i];
				            break;
						case claims[2]:
							
								$(highlight).removeClass();
								$(highlight).addClass('annotator-hl preamble-attributes');
								tagName =annotation.tags[i];
								keyname =annotation.tags[i];
				            break;
						case claims[3]: 
														
								$(highlight).removeClass();
								$(highlight).addClass('annotator-hl claim-elements');
								tagName =annotation.tags[i];
								keyname =annotation.tags[i];
					         break;
						case claims[4]:
														
								$(highlight).removeClass();
								$(highlight).addClass('annotator-hl element-attributes');
								tagName =annotation.tags[i];
								keyname =annotation.tags[i];
					         break;
						case claims[5]:
							    $(highlight).removeClass();
								$(highlight).addClass('annotator-hl feed-back');
								tagName =annotation.tags[i];
								keyname =annotation.tags[i];
							    break;
						case claims[6]:
						    $(highlight).removeClass();
							$(highlight).addClass('annotator-hl claim-dictionary');
							tagName =annotation.tags[i];
							keyname =annotation.tags[i];
						    break;
						case claims[7]:
						    $(highlight).removeClass();
							$(highlight).addClass('annotator-hl claim-embodiment');
							tagName =annotation.tags[i];
							keyname =annotation.tags[i];
						    break; 
						default: 
							$(highlight).removeClass();
							$(highlight).addClass('annotator-hl');
							
					}
				}else{
				}
			});
			
			
			
			
		});
		
		//alert("startOffset : "+annotation.ranges[0].startOffset);
		patentDo={
				patentId:patentID,
				claimIndependentNo:claimIndependentNo,
				keyname:keyname,
				keyvalue:annotation.quote,
				comment:annotation.text,
				startOffset:annotation.ranges[0].startOffset,
				endOffset:annotation.ranges[0].endOffset,
				currentUser:annotation.user/*.replace(/\s/g, '')*/
		};
		$.ajax({
			url : aapContext + "/updatepatent.html",
			beforeSend: function() {
				alert("hello");
				$('body').addClass("unselect");
				$('body').fadeIn();
				/*$('button').attr('disabled','disabled');*/
				/* window.document.body.disabled=true;*/},
			type : "POST",
			data :JSON.stringify(patentDo),
			dataType : "json",
			contentType: "application/json; charset=utf-8",
			cache: false,
			success : function(data) {
				$('body').fadeIn();
				/*window.opener.document.body.disabled=true;*/
				$('body').removeClass("unselect");
				 $("#notice_bar").css("display","block");
    			 $("#notice_box").addClass("notice");
    			 $("#notice_box").removeClass("alert");
		 		 $("#notice_box").text(""+tagName+" Annotation created successfully.");
		 		
	    			setTimeout(function(){
	    				$('#notice_bar').fadeOut();
	    			}, 4000);
	    			
	    			
			},
			error:function(){
				alert("error");
			}
		});
		
		
		
		}else{
			 $("#notice_bar").css("display","block");
			 $("#notice_box").addClass("notice");
			 $("#notice_box").removeClass("alert");
	 		 $("#notice_box").text("Default Annotation created successfully.");
	 		setTimeout(function(){
				$('#notice_bar').fadeOut();
			}, 4000);	
		}
	}
	
	function search(){
		var queryString=$("#search-text").val();
		window.location.replace(location.protocol + "//" + location.host +aapContext+"/searchnewpatent.html?searchQueryStr="+queryString);
		
	}
	
/********************************************************* Highlight Text*******************************************************/
	
	var selection="";
	var result;
	
	$(document).ready(function(){
		
		$("#highlighter-area").val("");
		$(".highlight-adder button").click(function(){
			
			 selection = window.getSelection().toString();
			 $(".annotator-adder").hide();
			 $(".highlight-adder").hide();
			 
			 if($("#highlighter-area").val()==""){
					$("#highlighter-area").val(selection);
				}else{
					var oldval="";
					oldval = $("#highlighter-area").val();
					$("#highlighter-area").val("");
					//alert(oldval);
					$("#highlighter-area").val(oldval+","+selection);
			}
			
			
		});
		
		$("#nav").click(function(){
			hideButtons();
		});
		$(".form-group span").click(function(){
			hideButtons();
		});
		$("#lookup-highlighter").click(function(){
			
			hideButtons();
			var selectedText = $("#highlighter-area").val();
			var arr = selectedText.split(',');
			
			
			$.each( arr, function(i, value ) {
				$("#search-details").userSelectedHighlight(arr[i]);
			});
			
		});
		$("#clear-highlighter").click(function(){
			 hideButtons();
			$("#search-details").removeHighlight();
			$("#highlighter-area").val("");
			
		});
		
		$(".annotator-save").click(function(){
			
			var tagName = $("#annotator-field-1").val();
			
			if(!tagName){
								
				
				 $("#notice_bar").css("display","block");
				 $("#notice_box").addClass("notice");
				 $("#notice_box").removeClass("alert");
		 		 $("#notice_box").text("Please select any tag.");
		 		setTimeout(function(){
					$('#notice_bar').fadeOut();
				}, 4000);	
				
				
				$("#annotator-field-1").focus();
				return false;
			}
		});
		
	});

	
	
/************************ highlight ************************************/
	
	
	jQuery.fn.userSelectedHighlight = function(pat) {
		
		 function innerHighlight(node, pat) {
		  var skip = 0;
		  if (node.nodeType == 3) {
			 
		   var pos = node.data.toUpperCase().indexOf(pat);
		   if (pos >= 0) {
		    var spannode = document.createElement('span');
		    spannode.className = 'search-highlight';
		    var middlebit = node.splitText(pos);
		    var endbit = middlebit.splitText(pat.length);
		    var middleclone = middlebit.cloneNode(true);
		    spannode.appendChild(middleclone);
		    middlebit.parentNode.replaceChild(spannode, middlebit);
		    skip = 1;
		   }
		  }
		  else if (node.nodeType == 1 && node.childNodes && !/(script|style)/i.test(node.tagName)) {
		   for (var i = 0; i < node.childNodes.length; ++i) {
		    i += innerHighlight(node.childNodes[i], pat);
		   }
		  }
		  return skip;
		 }
		 return this.length && pat && pat.length ? this.each(function() {
		  innerHighlight(this, pat.toUpperCase());
		 }) : this;
		};

		jQuery.fn.removeHighlight = function() {
		 return this.find("span.search-highlight").each(function() {
		  this.parentNode.firstChild.nodeName;
		  with (this.parentNode) {
		   replaceChild(this.firstChild, this);
		   normalize();
		  }
		 }).end();
		};

		
function hideButtons() {
	window.getSelection().removeAllRanges();
	$(".annotator-adder").hide();
	$(".highlight-adder").hide();
}
