<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.io.PrintWriter" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
<c:if test="${backToUser != null}">
    <a href="${backToUser}">Назад</a>
</c:if>

<table id="main-container">
    <tr >
        <td class="content">
            <%-- CONTENT --%>

            <h2 class="error">
                The following error occurred
            </h2>
            <%-- this way we obtain an information about an exception (if it has been occurred) --%>
            <c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
            <c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>
            <c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>

            <c:if test="${not empty code}">
                <h3>Error code: ${code}</h3>
            </c:if>

            <c:if test="${not empty message}">
                <h3>${message}</h3>
            </c:if>

            <c:if test="${not empty exception}">
                <% exception.printStackTrace(new PrintWriter(out)); %>
            </c:if>

            <%-- if we get this page using forward --%>
            <c:if test="${not empty requestScope.errorMessage}">
                <h3>${requestScope.errorMessage}</h3>
            </c:if>

            <%-- CONTENT --%>
        </td>
    </tr>
</table>
</body>
</html>
