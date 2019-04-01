<%@tag description="Structured Non-Authenticated Page template" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="pageTitle" required="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:baseLayout pageTitle="${pageTitle}">

<body class="top-navigation">

	<div id="wrapper">    
		<div id="page-wrapper" class="gray-bg">
		
			<div class="row border-bottom white-bg">
        		<nav class="navbar navbar-static-top" role="navigation">
	
					<div class="navbar-header">
		                <button aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" class="navbar-toggle collapsed" type="button">
		                    <i class="fa fa-reorder"></i>
		                </button>
		                <a href="#" class="navbar-brand">Group Wallet</a>
		            </div>
				
				
				    <div class="navbar-collapse collapse" id="navbar">
				        <ul class="nav navbar-top-links navbar-right">
		                    <li> <a href="${ctx}/login"> <i class="fa fa-sign-in"></i> Log in </a> </li>
		                    <li> <a href="${ctx}/signup"> Sign up </a> </li>
		                </ul>
				    </div>
			    </nav>
		    </div>
		    
		    <div class="wrapper wrapper-content">
            	<div class="container">
		    		<!-- <div class="col-md-10 col-md-offset-1"> -->
		      			<jsp:doBody/>
		    		<!-- </div> -->
		      	</div>
		    </div>
		    
		    <div class="footer">
	            <jsp:include page="/WEB-INF/views/fragments/footer.jsp"></jsp:include>
	        </div>
		    
		    <!-- <button class="scroll-top btn">
		        <i class="pci-chevron chevron-up"></i>
		    </button> -->
		</div>	
	</div>

</body>
</t:baseLayout>