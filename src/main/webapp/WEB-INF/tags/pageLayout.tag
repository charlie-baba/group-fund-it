<%@tag description="Structured Page template" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="pageTitle" required="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:baseLayout pageTitle="${pageTitle}">

<body class="">

	<div id="wrapper">
		
		<nav class="navbar-default navbar-static-side" role="navigation">
      		<jsp:include page="/WEB-INF/views/fragments/sidenav.jsp"></jsp:include>
        </nav>
	      
		
	    <div id="page-wrapper" class="gray-bg">
	    	<div class="row border-bottom">
	      		<jsp:include page="/WEB-INF/views/fragments/header.jsp"></jsp:include>
	    	</div>
	    
	    	<div id="">
	      		<jsp:doBody/>
	      	</div>
	      
	      	<div class="footer">
	      		<jsp:include page="/WEB-INF/views/fragments/footer.jsp"></jsp:include>
	    	</div>
	    </div>
	    
	    
	    <!-- <button class="scroll-top btn">
	        <i class="pci-chevron chevron-up"></i>
	    </button> -->
    </div>  
    
    <script type="text/javascript">
		$(function () {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$(document).ajaxSend(function(e, xhr, options) {
				xhr.setRequestHeader(header, token);
			});
		});
	</script>
	
    <script src="${ctx}/assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="${ctx}/assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="${ctx}/assets/js/inspinia.js"></script>
    <script src="${ctx}/assets/js/plugins/pace/pace.min.js"></script>
</body>

</t:baseLayout>