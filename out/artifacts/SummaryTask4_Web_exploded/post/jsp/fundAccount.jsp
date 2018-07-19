<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Fund Account</title>
</head>
<body>
<c:if test="${not empty user}">
    <p>
            <c:out value="${user.firstName} ${user.lastName}"/> &nbsp;

    <c:if test="${not empty userRole}">
        <c:if test="${userRole.name == 'admin'}">
            (<fmt:message key="label.role.admin"/>)&nbsp;
        </c:if>
        <c:if test="${userRole.name == 'client'}">
            ( <fmt:message key="label.role.client"/>)&nbsp;
        </c:if>
    </c:if>
    <fmt:message key="label.bill"/>:
            <c:out value="${user.bill}"/> <fmt:message key="label.grn"/>.&nbsp;

    <c:set var="role" value="${userRole.name}"/>

    <c:choose>
        <c:when test="${role == 'client'}">
            <form action="/controller" method="post">
                <input type="hidden" name="command" value="user">
                <input type="submit" value="<fmt:message key="button.main"/>"/>
            </form>
        </c:when>
        <c:otherwise>
            <form action="/controller" method="post">
                <input type="hidden" name="command" value="admin">
                <input type="submit" value="<fmt:message key="button.main"/>"/>
            </form>
        </c:otherwise>

    </c:choose>

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="account">
        <input type="submit" value="<fmt:message key="button.account"/>"/>
    </form>

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="logout">
        <input type="submit" value="<fmt:message key="button.logOut"/>"/>
    </form>
    </p>

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="replenish">
        <fmt:message key="label.amount_replenish"/>: &nbsp;
        <input type="number" name="summa" value="0" size="6" min="1" max="10000" step="1" required>
        <input type="submit" value="<fmt:message key="button.replenish"/>">
    </form>

</c:if>
</body>
</html>
