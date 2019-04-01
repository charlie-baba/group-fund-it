<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
	

<t:pageLayout pageTitle="Search">

	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2>Search</h2>
            <ol class="breadcrumb">
                <li>
                    <a href="${ctx}/user/dashboard">Dashboard</a>
                </li>
                <li class="active">
                    <strong>Search</strong>
                </li>
            </ol>
        </div>
    </div>
    
    <div class="wrapper wrapper-content animated fadeInRight">
	    <div class="row">
	        <div class="col-lg-12">
	            <div class="ibox float-e-margins">
	                <div class="ibox-content">
	                
		                <h2>
	                        <fmt:formatNumber type="number" value="${sizeOfResult}" /> results found for: <span class="text-navy">"${searchQ}"</span>
	                    </h2>
	                    <small>Request time  (<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="3" value="${resultTime}" /> seconds)</small>
	
	
						<div class="hr-line-dashed"></div>
	                    <div class="search-result">
	                        <h3><a href="#">GroupWallet | Facebook</a></h3>
	                        <a href="#" class="search-link">http://groupwallet.com</a>
	                        <p>
	                            This section is still in progress...
	                        </p>
	                    </div>
	                    
	                    <div class="hr-line-dashed"></div>
                        <div class="search-result">
                            <h3><a href="#">Samgpe Group</a></h3>
                            <a href="#" class="search-link">http://groupwallet.com/samplegroup</a>
                            <p>
                                It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, 
                                and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
                            </p>
                        </div>
                        
                        <div class="hr-line-dashed"></div>
                        <div class="text-center">
                            <div class="btn-group">
                                <button class="btn btn-white" type="button"><i class="fa fa-chevron-left"></i></button>
                                <button class="btn btn-white">1</button>
                                <button class="btn btn-white  active">2</button>
                                <button class="btn btn-white">3</button>
                                <button class="btn btn-white">4</button>
                                <button class="btn btn-white">5</button>
                                <button class="btn btn-white">6</button>
                                <button class="btn btn-white">7</button>
                                <button class="btn btn-white" type="button"><i class="fa fa-chevron-right"></i> </button>
                            </div>
                        </div>
                            
	                </div>
                </div>
            </div>
        </div>
    </div>


</t:pageLayout>