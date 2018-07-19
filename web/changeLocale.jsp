<%@ include file="/WEB-INF/include/head.jspf" %>
<fmt:setLocale value="${param.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<c:set var="currentLocale" value="${param.locale}" scope="session"/>
<c:redirect url="/controller?command=loadingEdition">
    <c:param name="local" value="${currentLocale}"/>
</c:redirect>
