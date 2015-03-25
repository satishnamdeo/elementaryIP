<%@page import="net.sf.json.JSONObject"%>
<%@ page import = "java.util.ResourceBundle" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>	
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
	<link href="<%=request.getContextPath()%>/css/annotator.min.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/css/jquery.mCustomScrollbar.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/css/details.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/pace-theme-fill-left.css" />
	<link href="<%=request.getContextPath()%>/css/jquery-ui.css" rel="stylesheet">
	<script src="<%=request.getContextPath()%>/javascript/jquery/jquery-ui.js"></script>
	<script src="<%=request.getContextPath()%>/javascript/jquery/annotator-full.min.js"></script>
	<script src="<%=request.getContextPath()%>/javascript/jquery/jquery.mCustomScrollbar.concat.min.js"></script>
	<script src="<%=request.getContextPath()%>/javascript/jquery/jwt.js"></script>
	<script src="<%=request.getContextPath()%>/javascript/jquery/pace.js"></script>
	<script type="text/javascript">
		var aapContext='<%=request.getContextPath()%>';
		<%ResourceBundle resource = ResourceBundle.getBundle("application");
		JSONObject patent=(JSONObject)request.getAttribute("patent");%>
		var patentID = '${patent.id}';
		var segmentList=${patent.segmentList};
		var claims=${claims_properties};
		var title='<%=resource.getString("patent.title")%>'+"_";
		var publicationDate='<%=resource.getString("patent.publicationDate")%>'+"_";
		var filingDate='<%=resource.getString("patent.filingDate")%>'+"_";
		var abstrac='<%=resource.getString("patent.abstract")%>'+"_";
		var feedback='<%=resource.getString("patent.feedback")%>'+"_";
	</script>
	<script>
    paceOptions = {
      elements: true
    };
  </script>
  <script>
    function load(time){
      var x = new XMLHttpRequest()
      x.open('GET', "http://localhost:5646/walter/" + time, true);
      x.send();
    };

  <!--   load(20); -->
   <!--  load(100);    load(500);    load(2000);    load(3000); -->

    setTimeout(function(){
      Pace.ignore(function(){
        load(3100);
      });
    }, 4000);

    Pace.on('hide', function(){
      console.log('done');
    });
  </script>
	<script>
    range.addEventListener('input', function(){
      document.querySelector('.pace').classList.remove('pace-inactive');
      document.querySelector('.pace').classList.add('pace-active');

      document.querySelector('.pace-progress').setAttribute('data-progress-text', range.value + '%');
      document.querySelector('.pace-progress').setAttribute('style', '-webkit-transform: translate3d(' + range.value + '%, 0px, 0px)');
    });
  </script>
	<script src="<%=request.getContextPath()%>/javascript/details.js"></script>
	<div class="container-fluid delete-loader">
		<div class="row" style="height:100%">
			<div class="col-md-12" style=" height: 100%; position: relative;  top: 200px;">
				<img id="dataLoader" src="../images/ajaxloader.gif" style="margin: 0 auto; display: block;">
			</div>
		</div>
	</div>
	<div class="container-fluid" style="margin-bottom:1%;">
		<div class="row" style="margin-left: 15px; margin-right: 15px;">
		
			<div class="col-md-8">
				
				<div class="form-group">
					<div class="input-group">
						<input type="text" name="search-text" id="search-text" class="form-control form-control-search"  value="US${patent.id}" placeholder="Enter search terms (for format, right-click icon to right)">
						<span id="search" class="input-group-addon">
							<button class="btn search-button" id="search" ></button>
						</span>
						<span id="searchQuery" class="input-group-addon"  data-html="true">
							<button class="suggetion" >?</button>
						</span>
					</div>
				</div>
						
				
					
				
			</div>
			<div class="col-md-4" style="">
				<!-- <div class=""><textarea id="highlighter-area" style="width: 100%;">  </textarea></div>
				<div class="inline">
					<input class="btn btn-primary"  id="lookup-highlighter" type="button" name="fname" value="Lookup">
					<input class="btn btn-primary" id="clear-highlighter" type="button" name="fname" value="Clear">
				</div> -->
				
				<div class="form-group">
					<div class="input-group input-group-highlighter">
						<textarea id="highlighter-area"  class="form-control form-control-search" style="height: 48px; padding: 0; resize: none; padding: 10px 0 0 10px;">  </textarea>
						<span id="lookup-highlighter" class="input-group-addon">
							Lookup
						</span>
						<span id="clear-highlighter" class="input-group-addon"  data-html="true">
							Clear
						</span>
						<sec:authorize ifAnyGranted="ELEMNTRYIP_ADMIN">
						<span id="clear-annotation" class="input-group-addon"  data-html="true" onclick="clearAnnotation()">
							Clear Annotation
						</span>
						</sec:authorize> 
					</div>
				</div>
				
				
			</div>
		</div>
		<div class="row" style="margin-left: 15px; margin-right: 15px;">
			<div class="col-md-12">
			
				<div class="element-suggestion">
					<div class="element-view">
						<div class="custom-claim suggetion-block"></div>
						<div class="inline-block" style="color: rgba(50, 50, 255);">Essential</div>
					</div>
					<div class="element-view">
						<div class="claim-preamble suggetion-block"></div>
						<div class="inline-block" style="color: rgba(240, 200, 0);">Claim Preamble</div>
					</div>
					<div class="element-view">
						<div class="claim-elements suggetion-block"></div>
						<div class="inline-block" style="color: rgba(255, 10, 10);">Claim Elements</div>
					</div>
					<div class="element-view">
						<div class="element-attributes suggetion-block"></div>
						<div class="inline-block" style=" color: rgba(150, 255, 100);">Element Attributes</div>
					</div>
					<div class="element-view">
						<div class="feed-back suggetion-block"></div>
						<div class="inline-block" style="color: rgba(0, 255, 255);">Feedback</div>
					</div>					
					<!--<div class="element-view">
						<div class="preamble-attributes suggetion-block"></div>
						<div class="inline-block" style="color: rgba(255, 255, 255);">Preamble Attributes</div>
					</div>-->
					<div class="element-view">
						<div class="claim-dictionary suggetion-block"></div>
						<div class="inline-block">Dictionary</div>
					</div>
					<div class="element-view">
						<div class="claim-embodiment suggetion-block"></div>
						<div class="inline-block">Embodiment</div>
					</div>
					
				</div>
				
			</div>
		</div>
	</div>
		
	<div class="container-fluid">
	
		<div  id="search-details" class="row" style="margin-left: 15px; margin-right: 15px;">
		
			<div class="col-md-12">
			
				<div class="detail-title-top inner-content">
					<span>
						<a id="US<%=patent.getString(resource.getString("patent.id")) %>" class="patent-number" href="http://www.google.com/patents/US<%=patent.getString(resource.getString("patent.id")) %>" target="_blank"><strong class="patent-title">United States Patent <%=patent.getString(resource.getString("patent.id")) %></strong></a>
					</span>	
					<div>
						<div id="<%=resource.getString("patent.title") %>_"><b><%=patent.getString(resource.getString("patent.title")) %></b></div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<span>GRANT DATE :</span>
							<div id="<%=resource.getString("patent.publicationDate") %>_" Style=" display: inline-block;"><%=patent.getString(resource.getString("patent.publicationDate")) %></div>
						</div>
						<div class="col-md-4">
							<span>FILE DATE :</span>
							<div id="<%=resource.getString("patent.filingDate") %>_" Style=" display: inline-block;"><%=patent.getString(resource.getString("patent.filingDate")) %></div>
						</div>
					</div>
					<div>
							<span>INVENTORS :</span>
							<div id="<%=resource.getString("patent.field1") %>_" Style=" display: inline-block;"><%=patent.getString(resource.getString("patent.field1")) %></div>
					</div>		
					<div>
						<span>US CLASSES :</span>
						<div id="<%=resource.getString("patent.field2") %>_" Style=" display: inline-block;"><%=patent.getString(resource.getString("patent.field2")) %></div>
					</div>
					<div>
						<span>REFERENCE PATENTS :</span>
						<div id="<%=resource.getString("patent.field3") %>_" Style=" display: inline-block;word-break: break-all;"><%=patent.getString(resource.getString("patent.field3")) %></div>
					</div>
					<p style="padding-top: 5px;">
						<span><b>ABSTRACT</b></span>
					</p>
					<div style="padding-top: 5px;">
						<div id="<%=resource.getString("patent.abstract") %>_"><%=patent.getString(resource.getString("patent.abstract")) %></div>
					</div>	

					<%-- <p style="padding-top: 5px;">
						<span>SUMMARY OF INVENTION</span>
					</p>
					<div style="padding-top: 5px;">
						<div id="<%=resource.getString("patent.field4") %>_"><%=patent.getString(resource.getString("patent.field4")) %></div>
					</div>	 --%>
					<p style="padding-top: 5px;">
						<span><b>COMPUTED STATISTICS</b></span>
					</p>
					<div style="padding-top: 5px;">
						<div id="<%=resource.getString("patent.field5") %>_"><%=patent.getString(resource.getString("patent.field5")) %></div>
					</div>	
					
					<p style="padding-top: 5px;">
						<span><b>FEEDBACK</b></span>
					</p>
					<div id="<%=resource.getString("patent.feedback") %>_">
								<span> Annotate the parameters below with comments per instructions</span><br>
								<span>(A) Relevance (enter 1 to 5 to indicate high to low)</span><br>
								<span>(B) Segmentation (enter 1 to 3 to indicate good to bad)</span><br>
								<span>(C) ClaimStructure (enter 0 if incorrect, and explain) = [Preamble][Element1][Element1 Attr]...[ElementN][ElementN Attr]</span><br>
								<span>(D) ElementLength (enter 0 if too short or long, and explain)</span><br>
								<span>(E) ClaimType (enter type = apparatus, method or system)</span><br>
								<span>(F) PunctuationStyle (enter style = ColonSemiComma, ColonComma, SemiComma, ColonSemi, Comma, Semi, Colon)</span><br>
								<span>(G) Other feedback (e.g. accuracy of patent data)</span><br>
					</div>
					
					<p style="padding-top: 5px;">
						<span><b>CLAIMS INDEPENDENT</b></span>
					</p>
					<div id="claimsIndependent">
						<c:forEach items="${patent.claimIndependentList}" var="claim" varStatus="counter" > 
									<%-- <p id="c${counter.index+1}_">${claim.claimIndependent}</p><br> --%>
									<div id="c${counter.index+1}_">${claim.claimIndependent}</div><br>
						</c:forEach>
					</div>
					<p style="padding-top: 5px;">
						<span><b>CLAIMS DEPENDENT</b></span>
					</p>
					<div id="claimsDependent">
						<c:forEach items="${patent.Claims_Dependent}" var="claim_dependent" varStatus="count"> 
							<div id="d${count.index+1}_">${claim_dependent}</div><br/>
						</c:forEach>
					</div>
					<p style="padding-top: 5px;">
						<span><b>PATENT DESCRIPTION</b></span>
					</p>
					 <div id="<%=resource.getString("patent.field6") %>_"><%=patent.getString(resource.getString("patent.field6")) %></div>
					<%--
					<div id="<%=resource.getString("patent.field9") %>_"><%=patent.getString(resource.getString("patent.field9")) %></div>
					<div id="<%=resource.getString("patent.field10") %>_"><%=patent.getString(resource.getString("patent.field10")) %></div> --%>
					
					
							
				</div>
				
			</div>	
				
		</div>
		
		
	</div>	
	
	
	