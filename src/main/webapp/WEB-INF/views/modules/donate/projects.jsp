<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:pageLayout pageTitle="Donate to Projects">

	<link href="${ctx}/assets/css/dataTables/datatables.min.css" rel="stylesheet">

    <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2> Donate to a Project </h2>
        </div>
        <div class="col-lg-2 p-t-20">
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
					<div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
		    			<!-- Tabs -->
				        <!--===================================================-->
				        <div class="tab-base">
				
				            <!--Nav Tabs-->
				            <ul class="nav nav-tabs">
				                <li class="active">
				                    <a data-toggle="tab" href="#tab-1">My Projects</a>
				                </li>
				                <li>
				                    <a data-toggle="tab" href="#tab-2">Public Projects</a>
				                </li>
				            </ul>
				
				            <!--Tabs Content-->
				            <div class="tab-content m-t-20">
				                <div id="tab-1" class="tab-pane fade active in">
				                    <jsp:include page="/WEB-INF/views/modules/donate/fragments/privateProj.jsp" />
				                </div>
				                <div id="tab-2" class="tab-pane fade">
				                    <jsp:include page="/WEB-INF/views/modules/donate/fragments/publicProj.jsp" />
				                </div>
				            </div>
				        </div>
				        <!--===================================================-->
				        <!--End Tabs-->
			        </div>
	        	</div>  
	        	
    		</div>
    	</div>
   	</div>   	
    
   	<script src="${ctx}/assets/js/plugins/dataTables/datatables.min.js"></script>
</t:pageLayout>