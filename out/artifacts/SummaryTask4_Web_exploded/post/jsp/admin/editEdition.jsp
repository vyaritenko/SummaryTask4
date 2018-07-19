<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mytag" uri="/WEB-INF/tags/tld/implicit.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit edition</title>
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
        <input type="hidden" name="command" value="">
        <input type="submit" value="<fmt:message key="button.account"/>"/>
    </form>
    <form action="/controller" method="post">
        <input type="hidden" name="command" value="admin">
        <input type="submit" name="add" value="<fmt:message key="button.add_edition"/>"/>
    </form>
    <form action="/controller" method="post">
        <input type="hidden" name="command" value="logout">
        <input type="submit" value="<fmt:message key="button.logOut"/>"/>
    </form>
    </p>

    <fieldset>
        <legend><fmt:message key="editEdition_jsp.legend.form_editing"/></legend>
        <form id="edit" action="/controller" method="post">
            <input type="hidden" name="command" value="saveEditEdition">

            <p><fmt:message key="editEdition_jsp.label.name_edition"/>: <input type="text" name="name" maxlength="100"
                                                                               pattern="[\sA-Za-zА-Яа-яЁё0-9]{2,}"
                                                                               title="<fmt:message key="title.name_edition"/>"><br/>
            </p>
            <p><fmt:message key="editEdition_jsp.label.price"/>: <input type="number" name="price" value="0" size="6"
                                                                        min="0" max="10000" step="1"><br/></p>

            <c:forEach items="${editions}" var="entry">
                <p>
                    <input type="radio" name="id" value="${entry.key}"> &nbsp;
                    <b>"${entry.value.name}"&nbsp;</b>
                    <mytag:category id="${entry.value.categoryId}"/>&nbsp;
                        ${entry.value.price} <fmt:message key="label.grn"/>.
                </p>
            </c:forEach>
            <input type="submit" value="<fmt:message key="editEdition_jsp.button.save"/>">
        </form>
    </fieldset>
</c:if>
</body>
</html>
