<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="today" />

<t:pageLayout pageTitle="Dashboard">
	
	<div class="wrapper wrapper-content"> 
		<div class="row">
            <div class="col-lg-2">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-success pull-right">Active</span>
                        <h5> ${data.noOfGroups gt 1 ? 'Groups' : 'Group'} </h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins"> <fmt:formatNumber type="number" value="${data.noOfGroups}" /> </h1>
                        <!-- <div class="stat-percent font-bold text-success">98% <i class="fa fa-bolt"></i></div> -->
                        <small>Groups you belong to</small>
                    </div>
                </div>
            </div>
            <div class="col-lg-2">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-info pull-right">All</span>
                        <h5>Projects</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins"> <fmt:formatNumber type="number" value="${data.noOfProjects}" /> </h1>
                        <!-- <div class="stat-percent font-bold text-info">20% <i class="fa fa-level-up"></i></div> -->
                        <small>Projects you can donate to</small>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-primary pull-right">Personal</span>
                        <h5>Just for you</h5>
                    </div>
                    <div class="ibox-content">

                        <div class="row">
                            <div class="col-md-6">
                                <h1 class="no-margins">&#8358; <fmt:formatNumber type="number" maxFractionDigits="2" value="${data.amountDonated}" /></h1>
                                <div class="font-bold text-navy"> <small>Amount you have donated</small></div>
                            </div>
                            <div class="col-md-6">
                                <h1 class="no-margins"> <fmt:formatNumber type="number" value="${data.noOfCampaigns}" /> </h1>
                                <div class="font-bold text-navy"> <small> Campaigns started by you </small></div>
                            </div>
                        </div>


                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5> Projects in your groups </h5>
                        <div class="ibox-tools">
                            <span class="label label-primary pull-right">2 Most Recent</span>
                        </div>
                    </div>
                    <div class="ibox-content no-padding">
                        <div style="height: 85px;">
                           <div class="row text-center">
                           
                       		<c:choose>
                           		<c:when test="${not empty data.pieCharts}">
                           			<c:forEach items="${data.pieCharts}" var="pieChart" varStatus="i">
		                                <div class="col-lg-6">
		                                    <canvas id="doughnutChart_${i.index}" width="60" height="60" style="margin: 4px auto 0"></canvas>
		                                    <h5> ${pieChart.labels} </h5>
		                                </div>
	                                </c:forEach>
                                </c:when>
                                <c:otherwise>
                                	<p class="p-t-20"> No active projects in your groups </p>
                                </c:otherwise>
                            </c:choose>
                           </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-lg-8">
                <div class="ibox float-e-margins">
                    <div class="ibox-content">
                        <div>
                            <span class="pull-right text-right">
                            <small>Statistics of your activities on <strong>Group Wallet</strong></small>
                                <br/>
                                Total activities: <fmt:formatNumber type="number" value="${data.totalActivities}" />
                            </span>
                            <h3 class="font-bold no-margins">
                                Your activity summary
                            </h3>
                            <small>Within the past ${noOfMonths} months</small>
                        </div>

                        <div class="m-t-sm">

                            <div class="row">
                                <div class="col-md-8">
                                    <div>
                                        <canvas id="lineChart" height="114"></canvas>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <ul class="stat-list m-t-lg">
                                        <li>
                                            <h2 class="no-margins"> <fmt:formatNumber type="number" value="${data.activitiesInChart}" /> </h2>
                                            <small>Total activities in chart</small>
                                        </li>
                                        <li>
                                            <h2 class="no-margins "><fmt:formatNumber type="number" value="${data.lastMonthActivities}" /></h2>
                                            <small>Total activities in the last month </small>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                        </div>

                        <div class="m-t-md">
                            <small class="pull-right">
                                <i class="fa fa-clock-o"> </i>
                                As at ${today}
                            </small>
                            <small>
                                <strong>Analysis of activities: </strong> The values change over time.
                            </small>
                        </div>

                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-warning pull-right">5 Most Recent</span>
                        <h5>Your activity log</h5>
                    </div>
                    
					<c:choose>
                		<c:when test="${not empty data.activityLog}">
                			
                			<c:forEach var="entry" items="${data.activityLog}">   
                				<div class="ibox-content" style="padding: 5px 20px 0px 20px;">
                					<div class="row">
		                				<div class="col-xs-12">             				
				                			<p class="text-uppercase text-success"> <strong>
				                			 	<i class="fa fa-calendar"></i> 
				                				<c:choose>
				                					<c:when test="${entry.key eq today}"> Today </c:when>
				                					<c:otherwise> <fmt:formatDate pattern="MMMM dd" value = "${entry.key}" /> </c:otherwise>
				                				</c:choose>
				                			</strong></p>
			                			</div>
		                            </div>
                				</div>
                				<div class="ibox-content">
	                				<c:forEach items="${entry.value}" var="activity">	                			
		                            <div class="stream-small">
	                                    <span class="label label-default"> Note</span>
	                                    <span class="text-muted"> 
	                                    	<small> <fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${activity.createDate}" /> </small> 
                                    	</span> - ${activity.title}
	                                </div>              				
	                				</c:forEach>
	                			</div>
                			</c:forEach>
                		</c:when>
                		
                		<c:otherwise>                			
							<div class="text-center p-t-10">
								<h5 class="text-bold">You do not have any activity logs yet. You can: </h5>
								<p><a href="${ctx}/group/create" class="btn btn-success"> Create Group </a></p>
								<p> Or </p>
								<p><a href="${ctx}/donate/projects" class="btn btn-info"> Donate </a></p>
							</div>
                		</c:otherwise>
                	</c:choose>
                	
                </div>
            </div>

        </div>
        
	</div>
	
	
	
    <script src="${ctx}/assets/js/plugins/chartJs/Chart.min.js"></script>
    <script>
        $(document).ready(function() {        	
        	loadCharts();
        });
        
        function loadCharts(){
        	/* ------------------ Line Chart --------------------- */
       		var lineData = {
                labels: ${data.chartData.labels},
                datasets: [
                    {
                        label: "Activities",
                        backgroundColor: "rgba(26,179,148,0.5)",
                        borderColor: "rgba(26,179,148,0.7)",
                        pointBackgroundColor: "rgba(26,179,148,1)",
                        pointBorderColor: "#fff",
                        data: ${data.chartData.data}
                    }
                ]
            };

            var lineOptions = {
                responsive: true
            };

            var ctx = document.getElementById("lineChart").getContext("2d");
            new Chart(ctx, {type: 'line', data: lineData, options:lineOptions});
            
            
            /* ------------------ Doughnut CHart --------------------- */
            var pieChart = ${data.pieChartData};
            for (var i = 0; i < pieChart.length; i++) {            	            	
            	
	            var doughnutData = {
	                labels: ["Paid","Pending"],
	                datasets: [{
	                    data: JSON.parse(pieChart[i].data),
	                    backgroundColor: ["#9CC3DA","#a3e1d4"]
	                }]
	            } ;
	
	            var doughnutOptions = {
	                responsive: false,
	                legend: {
	                    display: false
	                }
	            };
	
	            var id = "doughnutChart_"+ i;
	            var ctx4 = document.getElementById(id).getContext("2d");
	            new Chart(ctx4, {type: 'doughnut', data: doughnutData, options:doughnutOptions});
            }
        }
    </script>
</t:pageLayout>
