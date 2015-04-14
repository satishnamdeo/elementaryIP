
	<link href="<%=request.getContextPath()%>/css/jquery.mCustomScrollbar.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/css/home.css" rel="stylesheet">
	<script src="<%=request.getContextPath()%>/javascript/jquery/jquery-ui.js"></script>
	<script src="<%=request.getContextPath()%>/javascript/jquery/jquery.mCustomScrollbar.concat.min.js"></script>
	<script src="<%=request.getContextPath()%>/javascript/jquery/jquery.cookie.js"></script>
	<script src="<%=request.getContextPath()%>/javascript/project.js"></script>
	
	<style>
	#notice_box {
    display: inline-block;
    font-size: 14px;
    padding: 7px 15px;
    position: relative;
}
#notice_bar {
    height: 1px;
    left: 0;
    overflow: visible;
    position: fixed;
    text-align: center;
    top: 0;
    width: 100%;
    z-index: 100000;
}
#notice_box.notice {
    background-color: #ffcc00;
    color: #333333;
    font-weight: bold;
}
#notice_box.alert {
    background-color: #ff3300;
    color: #ffffff;
    font-weight: bold;
}</style>
	<script type="text/javascript">
	var aapContext;
	aapContext='<%=request.getContextPath()%>';
	/* var searchNewPatent='${searchNewPatent}'; */
	var projectid='${projectId}';
	var msg='${message}';
if(msg=='NO'){
	
	 $("#notice_bar").css("display","block");
	 $("#notice_box").addClass("notice");
	 $("#notice_box").removeClass("alert");
	 $("#notice_box").text("Automatic annotation created successfully.");
	 setTimeout(function(){
		$('#notice_bar').fadeOut();
	 }, 4000);
	 /* location.reload(); */
}
	/* if(searchNewPatent=='true'){
		var searchStr='${keyword}';
		getContent(searchStr,false);
	} else if($.cookie("userName")==userName){
		//alert("user cookie"+$.cookie("userName"));
		if($.cookie("keyword")!=null){
			//alert("keyword cookie"+$.cookie("keyword"));
			getContent($.cookie("keyword"),true);
		}
	} */
	</script>
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header" >
				
					<div class="block">
						<div class="form-group" style="display:inline;">
							<div class="input-group col-centered">
								
								<input type="text" name="search-text" id="search-text" class="form-control form-control-search"  placeholder="Enter search terms (for format, right-click icon to right)">
								
								
								
								<span id="search" class="input-group-addon">
									<button class="btn search-button" id="search"></button>
								</span>
								<span id="searchQuery" class="input-group-addon"  data-html="true">
									<button class="suggetion" >?</button>
								</span>
								
								<span id="external_search" class="input-group-addon">External
									<button class="btn search-button" id="search"></button>
								</span>
								<%-- <!-- remove this after testing done begin -->
								<span class="input-group-addon">
									<a href="<%=request.getContextPath() %>/python.html">python index page</a>
								</span>
								<!-- remove this after testing done End  --> --%>
							</div>
						</div>
						<div class="container-fluid  total-size"></div>
					</div>
				</div>
				<div id="search-result" class="container-fluid">
										
				</div>
			</div>
		</div>
	</div>
	<form  class="hidden-form" id="import-patent-form" method="post" action="<%=request.getContextPath()%>/importpatent.html" commandName="patentListDo">
	<div id="patentdiv">
</div>
	</form>
	
