<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Settings</title>
</head>
<body>
<c:if test="${not empty user}">

    <p><c:out value="${user.firstName} ${user.lastName}"/> &nbsp;

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


    <script>
        function isEmail() {
            var str = document.getElementById("email").value;
            var status = document.getElementById("status");
            var re = /^[^\s()<>@,;:\/]+@\w[\w\.-]+\.[a-z]{2,}$/i;
            if (re.test(str)) status.innerHTML = "<fmt:message key="signup_jsp.massage.address_correct"/>";
            else status.innerHTML = "<fmt:message key="signup_jsp.massage.address_incorrect"/>";
            if (isEmpty(str)) status.innerHTML = "<fmt:message key="signup_jsp.massage.empty_field"/>";
        }
        function isEmpty(str) {
            return (str == null) || (str.length == 0);
        }
    </script>

    <form action="/controller" method="post">
        <input type="hidden" name="command" value="settings">
        <h2><fmt:message key="settings_jsp.label.editing_form"/></h2>
        <p><fmt:message key="settings_jsp.label.password"/>: <input type="password" name="password" maxlength="20"
                                                                    pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                                                                    title="Пароль не может быть короче восьми символов и должен содержать хотя бы одну цифру, одну маленькую и одну большую латинскую букву."
                                                                    required><br/></p>
        <p><fmt:message key="settings_jsp.label.name"/>: <input type="text" name="firstName" maxlength="20"
                                                                pattern="[A-Za-zА-Яа-яЁё]{2,}" title="Имя должно быть больше 2 букв"
                                                                required><br/></p>
        <p><fmt:message key="settings_jsp.label.lastname"/>: <input type="text" name="lastName" maxlength="20"
                                                                    pattern="[A-Za-zА-Яа-яЁё]{2,}" title="Фамилия должна быть больше 2 букв" required><br/>
        </p>
        <p>Email: <input id="email" type="email" name="email" maxlength="100" onclick="isEmail()" required><span
                id="status"></span><br/></p>
        <input type="submit" value="<fmt:message key="settings_jsp.button.save"/>">
    </form>
</c:if>
</body>
</html>
