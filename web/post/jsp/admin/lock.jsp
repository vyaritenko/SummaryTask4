<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/tagFile" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
    <title>Lock user</title>
</head>
<body>
<c:if test="${not empty user and userRole.name == 'admin'}">
    <p>
            <c:out value="${user.firstName} ${user.lastName}"/> &nbsp;

        <c:if test="${not empty userRole}">
        (<fmt:message key="label.role.admin"/>) &nbsp;
        </c:if>
            <fmt:message key="label.bill"/>:
            <c:out value="${user.bill}"/> <fmt:message key="label.grn"/>.&nbsp;

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="admin">
        <input type="submit" value="<fmt:message key="button.main"/>"/>
    </form>
    <form action="/controller" method="post">
        <input type="hidden" name="command" value="account">
        <input type="submit" value="<fmt:message key="button.account"/>"/>
    </form>

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

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="searchUser"/>
        <p>
            <input type="search" name="search" placeholder="<fmt:message key="lock_jsp.placeholder.search_reader"/>"/>&nbsp;
            <input type="submit" value="<fmt:message key="lock_jsp.button.search"/>"/>&nbsp;
        </p>
    </form>

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="admin">
        <input type="submit" name="lock" value="<fmt:message key="button.reset_search"/>"/>
    </form>


    <form action="/controller" method="post">
        <input type="hidden" name="command" value="lockUnlock"/>
        <c:choose>
            <c:when test="${not empty allUser}">
                <table rules="rows" cellpadding="10" cellspacing="17" border="1">
                    <c:set var="role" value="${role}"/>
                    <c:set var="userLock" value="${userLock}"/>
                    <tr>
                        <th align="center"><fmt:message key="lock_jsp.label.login_upper_case"/></th>
                        <th align="center"><fmt:message key="lock_jsp.label.first_last_name_upper_case"/></th>
                        <th align="center"><fmt:message key="lock_jsp.label.role_upper_case"/></th>
                        <th align="center"><fmt:message key="lock_jsp.label.status_upper_case"/></th>
                        <th align="center"></th>
                    </tr>
                    <c:forEach items="${allUser}" var="user">
                        <tr>
                            <td align="center"><b>${user.login}</b></td>
                            <td align="center">${user.lastName} ${user.firstName}</td>
                            <td align="center"><c:out value="${role[user.roleId - 1]}"/></td>
                            <td align="center"><c:out value="${userLock[user.userLockId - 1]}"/></td>
                            <tags:choose lock="${userLock[user.userLockId - 1]}" userId="${user.id}"/>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <h3><fmt:message key="lock_jsp.label.have_not_got_reader"/>!</h3>
            </c:otherwise>
        </c:choose>
    </form>
</c:if>
</body>
</html>
