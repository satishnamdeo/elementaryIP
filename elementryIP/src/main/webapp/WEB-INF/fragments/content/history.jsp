
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	</script>
	
	<div class="container">
		<div class="page-header">
			<span class="search-history-title">Search History</span>
		</div>
		
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12">
				<ul id="content">
					<c:forEach items="${historyList}" var="element" begin="0" varStatus="counter" > 
						<li>
							<div class="container-fluid margin-bottom">
								<div class="row">
								<div class="col-xs-12 col-sm-12 col-md-12">
									<div class="search-date">
											#${counter.index+1} 
											<fmt:formatDate value="${element.searchTime}" var="searchtime" type="both"/>
											${searchtime}
											
									</div>
									<div class="key-result">
										<div class="search-key">${element.searchKeyword}</div>
										<div class="search-result">
											<a href="#" style="text-decoration: underline; color: #444;">${element.searchCount} results</a>
										</div>
									</div>
									
								</div>
								</div>
							</div>
						</li>
						
					</c:forEach>
				</ul>
				<div class="holder"></div>
			</div>
		</div>
		
		
	</div>
