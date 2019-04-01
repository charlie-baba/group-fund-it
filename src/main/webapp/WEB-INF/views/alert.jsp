<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty errorMessage}">
	<div class="alert alert-danger alert-dismissable">
		<button aria-hidden="true" data-dismiss="alert" class="close" type="button">&times;</button>
	    <strong>Error! &nbsp; </strong> ${errorMessage}
	</div>
</c:if>

<c:if test="${not empty successMessage}">
	<div class="alert alert-success alert-dismissable">
		<button aria-hidden="true" data-dismiss="alert" class="close" type="button">&times;</button>
	    <strong>Successful! &nbsp; </strong> ${successMessage}
	</div>
</c:if>

<c:if test="${not empty infoMessage}">
	<div class="alert alert-info alert-dismissable">
		<button aria-hidden="true" data-dismiss="alert" class="close" type="button">&times;</button>
	    <strong>Heads Up! &nbsp; </strong> ${infoMessage}
	</div>
</c:if>

