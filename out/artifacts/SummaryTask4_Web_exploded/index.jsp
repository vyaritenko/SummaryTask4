<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mytag" uri="/WEB-INF/tags/tld/implicit.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
    <title>Periodical</title>
</head>
<body>
<a href="post/jsp/login.jsp"><fmt:message key="index_jsp.link.login"/></a> &nbsp;
<a href="post/jsp/signup.jsp"><fmt:message key="index_jsp.link.signup"/></a>
<p>
    <%@ include file="/WEB-INF/include/selectLanguage.jspf" %>
</p>
<%@ include file="/WEB-INF/include/searchAndSorting.jspf" %>

<fieldset>
    <legend><fmt:message key="index_jsp.legend.edition"/></legend>
    <c:forEach items="${editionList}" var="item">
        <p><b>"${item.name}"&nbsp;</b>
            <mytag:category id="${item.categoryId}"/>&nbsp;
                ${item.price} <fmt:message key="label.grn"/>.
        </p>
    </c:forEach>
</fieldset>
</body>
</html>
