<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mytag" uri="/WEB-INF/tags/tld/implicit.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
</head>
<body>
<c:if test="${empty user}">
    <c:redirect url="login.jsp"/>
</c:if>
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
        <c:when test="${role == 'admin'}">
            <form action="/controller" method="post">
                <input type="hidden" name="command" value="admin">
                <input type="submit" value="<fmt:message key="button.main"/>"/>
            </form>

            <form action="/controller" method="post">
                <input type="hidden" name="command" value="admin">
                <input type="submit" name="lock" value="<fmt:message key="button.lock_users"/>"/>
            </form>
        </c:when>

    </c:choose>
    <form action="/controller" method="post">
        <input type="hidden" name="command" value="fundAccount">
        <input type="submit" value="<fmt:message key="button.fund_account"/>">
    </form>

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="openSettings">
        <input type="submit" value="<fmt:message key="button.settings"/>">
    </form>

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="logout">
        <input type="submit" value="<fmt:message key="button.logOut"/>"/>
    </form>
    </p>

    <c:if test="${empty listSubscribeEdition}">
        <h3><fmt:message key="account.label.have_not_subscribes"/>!</h3>
    </c:if>

    <c:if test="${not empty listSubscribeEdition}">
        <fieldset>
            <legend> <fmt:message key="account.jsp.legend.my_subscriibes"/></legend>
            <form action="/controller" method="post">
                <input type="hidden" name="command" value="unsubscribe">
                <c:forEach items="${listSubscribeEdition}" var="item">
                    <p>
                        <input type="checkbox" name="${item.id}" value="${item.price}"> &nbsp;
                        <b>"${item.name}"&nbsp;</b>
                        <mytag:category id="${item.categoryId}"/>&nbsp;
                            ${item.price} <fmt:message key="label.grn"/>.
                    </p>
                </c:forEach>
                <p>
                    <input type="submit" value="<fmt:message key="button.unsubscribe_edition"/>"/>&nbsp;
                </p>
            </form>
        </fieldset>
    </c:if>
</c:if>
</body>
</html>
