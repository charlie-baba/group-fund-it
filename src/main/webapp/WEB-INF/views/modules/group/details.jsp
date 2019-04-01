<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:pageLayout pageTitle="${group.name}">

	<link href="${ctx}/assets/plugins/datatables/media/css/dataTables.bootstrap.css" rel="stylesheet">
	<link href="${ctx}/assets/plugins/datatables/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet">

	<div id="page-head">                    
        <div id="page-title">
            <h1 class="page-header text-overflow">${group.name}</h1>
            <div class="searchbox" style="width: 100px">
            	<a href="${ctx}/user/dashboard" class="btn btn-rounded btn-mint"><i class="fa fa-arrow-left"></i> Return </a>
            </div>
        </div>
    </div>
    
    <div id="page-content">
		<div class="panel">			
			<div class="panel-body task-success">
			
				<div class="fixed-fluid">
					
					<div class="fixed-md-300 pull-sm-left">
						<div class="text-center">
		                    <div class="pad-ver">
		                        <img alt="Group Logo" src="data:image/jpeg;base64,${group.base64Image}" 
							             	onerror="this.src='${ctx}/assets/img/favicon.ico'" class="img-lg img-circle img-border" />
		                    </div>
		                    <h4 class="text-lg text-overflow mar-no">${group.name}</h4>
		                </div>
                	</div>
                	
                	<div class="fluid fixed-left-border">
		                <dl class="mar-all pad-btm text-dark dl-horizontal">
		                	<dt> Description: </dt> <dd> ${group.description}</dd>
		                	<dt> Privacy: </dt> <dd> ${group.privateGroup ? "Private Group" : "Public Group"}</dd>
		                	<dt> Target Amount: </dt> <dd> &#8358; <fmt:formatNumber type="number" maxFractionDigits="2" value="${group.targetAmount}" /> </dd>
		                </dl>
		                <hr>
		                
		                <h5> Group Members</h5>
		                <!-- Members Table -->
						<!--===================================================-->
		    			<table id="members-table" class="table table-striped table-bordered" cellspacing="0" width="100%">
				            <thead>
				                <tr>
				                	<th>#</th>
				                    <th>Email Address</th>
				                    <th>Status</th>
				                </tr>
				            </thead>
				            <tbody>
				            	<c:forEach items="${members}" var="member" varStatus="i">
				            		<tr>
				            			<td>${i.index + 1}</td>
					                    <td>${member.email}</td>
					                    <td><span class="label ${member.status == 'SIGNED_UP' ? 'label-info' : 'label-warning'}">${member.status}</span></td>
					                </tr>
				            	</c:forEach>
				            </tbody>
				        </table>
		   				<!--===================================================-->
						<!-- End Members Table -->
	                </div>
	                
				</div>
				
			</div>
	    </div>		    
    </div>
    
    
    <script type="text/javascript">
		$(document).ready(function() {
			$('#members-table').DataTable({
		        "responsive": true,
		        "language": {
		            "paginate": {
		              "previous": '<i class="fa fa-angle-left"></i>',
		              "next": '<i class="fa fa-angle-right"></i>'
		            }
		        }
		    });
		});
	</script>
    
    <script src="${ctx}/assets/plugins/datatables/media/js/jquery.dataTables.js"></script>
   	<script src="${ctx}/assets/plugins/datatables/media/js/dataTables.bootstrap.js"></script>
	<script src="${ctx}/assets/plugins/datatables/extensions/Responsive/js/dataTables.responsive.min.js"></script>
</t:pageLayout>