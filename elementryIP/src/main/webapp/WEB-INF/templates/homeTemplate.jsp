<%@ page language="java" %>
<%@ page import="com.eip.entity.User"%>
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
    
		
		<% User user=(User)session.getAttribute("user");%>
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/favicon.ico">
		<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
		<link href="<%=request.getContextPath()%>/css/header.css" rel="stylesheet">
		<link href="<%=request.getContextPath()%>/css/elementaryip.css" rel="stylesheet">
		<link href="<%=request.getContextPath()%>/css/footer.css" rel="stylesheet">
		<!-- Custom styles for this template -->
		
		<script src="<%=request.getContextPath()%>/javascript/jquery/jquery-2.0.0.js"></script>
		<script src="<%=request.getContextPath()%>/javascript/jquery/bootstrap.js"></script>
		<script src="<%=request.getContextPath()%>/javascript/elemntryip.js"></script>
	   <script type="text/javascript">
	   	var userName = "<%=user.getFirstName()+user.getLastName()%>";
	   	var firstName = "<%=user.getFirstName()%>";
	   </script>
	   <style>
	    .unselect {
  -webkit-user-select: none;
     -moz-user-select: -moz-none;
      -ms-user-select: none;
          user-select: none;
} 
	   </style>
 	</head>
 	<body >
 	
		<!-- start of header -->

			<tiles:insertAttribute name="header" />
		
		<!-- End of header -->
	
	
		
		<!-- start of Content -->
		
			<tiles:insertAttribute name="content" />
		
		<!-- End of Content -->
		
		
		
		<!-- Start of Footer -->
		
			<tiles:insertAttribute name="footer" />
	    
		<!-- end of footer -->
    	
    	
    	<div class="modal-body" id="querysuggetion" style="display: none;">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					Search for upto 30 patent numbers at once with query = id:(PN1 PN2 ... PN30)
					
					<h4>Search Fields</h4>
						query = field:(search terms)
						<div><b>Patent Fields</b> : id or US_Patent_Number (numbers only, e.g. 7654321) , ClassesUS , Title , Abstract , Claims_Independent , Claims_Dependent , ReferencePatents , Inventors , PublicationDate , FilingDate </div>
						
						<div><b>User Annotation Fields</b> : Essential = essential to invention , Preamble (of independent claim) , Element (of invention in independent claim) , ElementAttributes , Comment = in annotation , Feedback = by user </div>	
						
						<div><b>Note</b> : Multiple search fields can be combined using operators, e.g. query = id:(7*) AND ClassesUS:(370 OR 100).</div>
						
					<h4>Search Terms and Operators</h4>
						<a href="http://lucene.apache.org/core/4_9_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package_description">see link for details</a>
						
						<div><b>Search Terms</b> : single words or phrases grouped by ""</div>
						
						<div><b>Term Modifiers</b> : * = wildcard (multiple characters) , ? = wildcard (single characters). Cannot be used within phrases or at beginning of query term.</div>
						
						<div><b>Boolean operators</b> : OR (default operator) , AND (or &&) , NOT (used with more than one term), + (specifies required terms) e.g. query = +jakarta lucene , - (specifies excluded terms) e.g. query = "jakarta apache" -"Apache Lucene". Operators OR, AND, NOT must be ALL CAPS.</div>
						
						<div><b>Grouping</b> : () e.g. query = (jakarta OR apache) AND website , e.g. query = title:(+return +"pink panther"). </div>
						
						<div><b>Proximity Search</b> :  ~N = proximity search within N characters, e.g. query = "jakarta apache"~10.</div>

						<div><b>Range Search</b> :  [] = inclusive range search e.g. query = FilingDate:[1960 TO 1980] , {} = exclusive range search, e.g. query = Inventors:{John TO Turner}.</div>
						
						<div><b>Term Boosting</b> :  ^ = boost term e.g. query = jakarta^4 apache , query = "jakarta apache"^4 "Apache Lucene". Boost factor must be positive.</div>
						
						<div><b>RegExp and Fuzzy Search</b> : "/" = regular expression search e.g. query = /[mb]oat/ , ~ = fuzzy search e.g. query = term~.</div>
						
						<div><b>Escaping special characters</b> : \ = escapes special characters = { + - && || ! ( ) { } [ ] ^ " ~ * ? : \ / } , e.g. to search for (1+1):2 use query = \(1\+1\)\:2</div>

						</div>


						
<!--						<h3>Performance</h3>
						<div class="container-fluid">
						
							<div class="row margin-bottom">
								<div class="col-xs-3 col-sm-3 col-md-3 col-lg-3 right-align">PrecisionRecall:()</div>
								<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">=</div> 
								<div class="col-xs-8 col-sm-8 col-md-8 col-lg-8"> (precision, recall) both in range 1-10 indicating low to hig</div>
							</div>
						</div>
						
						<h3>Examples</h3>
						<div class="container-fluid">
						
							<div class="row margin-bottom">
								<div class="col-md-12 col-xs-12 ">
									eyeglass, locator, wireless, eyeglass case, Dates:( * - 01/01/2005)
								</div>
							</div>
							<div class="row margin-bottom">
								<div class="col-md-12 col-xs-12 ">
									Abstract:(eyeglass , locator NOT GPS , wireless)  Claims:(transmitter , receiver , eyeglass container)
								</div>
							</div>
						</div>-->
						
					</div>
				</div>
			</div>		
		</div>
		 
	</body>
  </html> 
