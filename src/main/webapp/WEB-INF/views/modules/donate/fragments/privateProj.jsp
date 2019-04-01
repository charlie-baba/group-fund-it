<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<c:choose>
	<c:when test="${not empty myprojects}">
		<div class="table-responsive">
			<!-- Private Projects Table -->
			<!--===================================================-->
			<table id="myprojects-table" class="table table-striped table-bordered" cellspacing="0" width="100%">
	            <thead>
	                <tr>
	                    <th>Project Name</th>
	                    <th style="width: 40%">Description</th>
	                    <th>Group Name</th>
	                    <th>Action </th>
	                </tr>
	            </thead>
	            <tbody>
	            	<c:forEach items="${myprojects}" var="proj">
	            		<tr>
		                    <td>${proj.name}</td>
		                    <td>${proj.description}</td>
		                    <td>${proj.group.name}</td>
		                    <td>
		                    	<%-- <a class="btn btn-xs btn-info" href="${ctx}/group/dashboard/${proj.group.id}"> <i class="fa fa-info-circle"></i> View Group </a> --%>
								<a class="btn btn-xs btn-primary" href="${ctx}/donate/projectDetails/${proj.id}"> <i class="fa fa-money"></i> View Details </a>
		                    </td>
		                </tr>
	            	</c:forEach>
	            </tbody>
	        </table>
			<!--===================================================-->
			<!-- End Private Projects Table -->
		</div>
	</c:when>
	
	<c:otherwise>
		<hr class="new-section-sm bord-no">
		<div class="row">
		    <div class="col-lg-8 col-lg-offset-2">
		        <div class="panel panel-trans text-center">
			        <div class="panel-heading">
			            <h3>You are not a member of any project group.</h3>
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
		$('#myprojects-table').DataTable({
	        "responsive": true
	    });
	});
</script>
