<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:nonAuthLayout pageTitle="Donate to Projects">

	<link href="${ctx}/assets/css/dataTables/datatables.min.css" rel="stylesheet">

	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-12">
            <h2> Donate to a Project </h2>
        </div>
    </div>
    
    <div class="wrapper wrapper-content animated fadeInRight">
    	<div class="row">
    		<div class="col-lg-12">
    		
	    		<div class="ibox">
	                <div class="ibox-title">
						<h5> Projects you can donate to </h5>
	                </div>
	
					<div class="ibox-content">
	    				<jsp:include page="/WEB-INF/views/modules/donate/fragments/publicProj.jsp" />
	   				</div>
	   			</div> 
   			 
   			</div>
		</div>
   	</div>
   	
   	<script src="${ctx}/assets/js/plugins/dataTables/datatables.min.js"></script>
</t:nonAuthLayout>