<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>


<c:choose>
	<c:when test="${not empty publicProjects}">
		<div class="table-responsive">
			<!-- Public Projects Table -->
			<!--===================================================-->
			<table id="publicprojects-table" class="table table-striped table-bordered" cellspacing="0" width="100%">
	            <thead>
	                <tr>
	                    <th>Project Name</th>
	                    <th style="width: 40%">Description</th>
	                    <th>Group Name</th>
	                    <th>Action</th>
	                </tr>
	            </thead>
	            <tbody>
	            	<c:forEach items="${publicProjects}" var="proj">
	            		<tr>
		                    <td>${proj.name}</td>
		                    <td>${proj.description}</td>
		                    <td>${proj.group.name}</td>
		                    <td>
		                    	<c:choose>
		                    		<c:when test="${isAuthenticated}">
		                    			<a class="btn btn-xs btn-primary" href="${ctx}/donate/projectDetails/${proj.id}"> <i class="fa fa-money"></i> View Details </a>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<a class="btn btn-xs btn-primary" href="${ctx}/na/lnk/${proj.link}"> <i class="fa fa-money"></i> View Details </a>
		                    		</c:otherwise>
		                    	</c:choose>
		                    	
							</td>
		                </tr>
	            	</c:forEach>
	            </tbody>
	        </table>
			<!--===================================================-->
			<!-- End Public Projects Table -->
		</div>
	</c:when>
	
	<c:otherwise>
		<hr class="new-section-sm bord-no">
		<div class="row">
		    <div class="col-lg-8 col-lg-offset-2">
		        <div class="panel panel-trans text-center">
			        <div class="panel-heading">
			            <h3>There is currently no active public project. Please check back later.</h3>
			        </div>
			        <div class="panel-body">
			            <p>Projects are campaigns created within groups to aid donations to a beneficiary. </p>
			        </div>
		        </div>
		    </div>
		</div>
	</c:otherwise>
</c:choose>


<script type="text/javascript">
	$(document).ready(function() {
		$('#publicprojects-table').DataTable({
	        "responsive": true
	    });
	});
</script>
