<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Login</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
</head>
<body>

<a href="/index.jsp"><fmt:message key="login_jsp.link.main"/></a> &nbsp;
<a href="/post/jsp/signup.jsp"><fmt:message key="login_jsp.link.signup"/></a>
<p></p>
<form id="login_form" action="/controller" method="post">
    <input type="hidden" name="command" value="login"/>
    <fieldset>
        <legend><fmt:message key="login_jsp.legend.login"/></legend>
        <input name="login" maxlength="20" pattern="[A-Za-z0-9]{4,}"
               title="<fmt:message key="title.login"/>" required/><br/>
    </fieldset>
    <br/>
    <fieldset>
        <legend><fmt:message key="login_jsp.legend.password"/></legend>
        <input type="password" maxlength="20" name="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
               title="<fmt:message key="title.password"/>"
               required/>
    </fieldset>
    <br/>
    <input type="submit" value="<fmt:message key="login_jsp.form.submit_come_in"/>">
</form>
</body>
</html>
