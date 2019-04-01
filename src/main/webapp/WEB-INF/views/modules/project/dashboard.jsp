<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
	
<t:pageLayout pageTitle="${project.name}">
	<link href="${ctx}/assets/css/dataTables/datatables.min.css" rel="stylesheet">

    <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2> ${project.name} </h2>
        </div>
        <div class="col-lg-2 p-t-20">
			<a href="${ctx}/project/index" class="btn btn-default btn-rounded"><i class="fa fa-arrow-left"></i> Return </a>
        </div>
    </div>
    
     <div class="wrapper wrapper-content">
        <div class="row">
        
        	<div class="col-lg-4">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-success pull-right">Total</span>
                        <h5>Project Target Amount</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins"> &#8358; <fmt:formatNumber type="number" maxFractionDigits="2" value="${dashboard.targetAmt}" /> </h1>
                        <div class="stat-percent font-bold text-success">100% <i class="fa fa-bolt"></i></div>
                        <small>Target</small>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-info pull-right">Available</span>
                        <h5>Total Amount Donated</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins"> &#8358; <fmt:formatNumber type="number" maxFractionDigits="2" value="${dashboard.donatedAmt}" /> </h1>
                        <div class="stat-percent font-bold text-info">${dashboard.donatedPercentage}% <i class="fa fa-level-up"></i></div>
                        <small> Received Amount </small>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-primary pull-right">Anticipating</span>
                        <h5>Remaining Amount</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins"> &#8358; <fmt:formatNumber type="number" maxFractionDigits="2" value="${dashboard.remainingAmt}" /> </h1>
                        <div class="stat-percent font-bold text-navy">${dashboard.remainingPercentage}% <i class="fa fa-level-down"></i></div>
                        <small> Expected Amount </small>
                    </div>
                </div>
            </div>                    
	    </div>
	    <div class="clearfix"></div>
	    
	    <div class="row">
	    	<div class="col-lg-12">
	    		<div class="ibox float-e-margins">
		            <div class="ibox-title">
		                <h5> Project Donations </h5>
		            </div>
		            <!--Data Table-->
		            <!--===================================================-->
		            <div class="ibox-content">
		                <div class="table-responsive">
		                    <table id="trans-table" class="table table-striped table-bordered table-hover">
		                        <thead>
		                            <tr>
		                                <th>Donor</th>
		                                <th>Amount (&#8358;)</th>
		                                <th>Date <i class="fa fa-clock-o"></i> </th>
		                            </tr>
		                        </thead>
		                        <tbody>
		                        	<c:forEach items="${dashboard.donations}" var="donation">
			                            <tr>
			                                <td>${donation[0]}</td>
			                                <td> <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${donation[1]}" /> </td>
			                                <td><span class="text-muted">
			                                	<fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${donation[2]}" /> </span>
		                                	</td>
			                            </tr>
		                            </c:forEach>
		                        </tbody>
		                    </table>
		                </div>
		            </div>
		            <!--===================================================-->
		            <!--End Data Table-->
		        </div>
	    	</div>
	    </div>
    </div>
    
    <script type="text/javascript">
    $(document).ready(function() {
			$('#trans-table').DataTable({
		        "responsive": true,
		        dom: '<"html5buttons"B>lTfgitp',
		        buttons: [
                    {extend: 'copy'},
                    {extend: 'csv'},
                    {extend: 'excel', title: 'Group Wallet Transaction History'},
                    {extend: 'pdf', title: 'Group Wallet Transaction History'},

                    {extend: 'print',
                    	customize: function (win){
                            $(win.document.body).addClass('white-bg');
                            $(win.document.body).css('font-size', '10px');
                            $(win.document.body).find('table')
                                    .addClass('compact')
                                    .css('font-size', 'inherit');
                    	}
                    }
                ]
		    });
		});    
    </script>
    
   	<script src="${ctx}/assets/js/plugins/dataTables/datatables.min.js"></script>
</t:pageLayout>