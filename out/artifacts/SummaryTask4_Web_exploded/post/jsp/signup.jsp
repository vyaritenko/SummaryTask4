<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Singup</title>
</head>
<body>
<script>
    function isEmail() {
        var str = document.getElementById("email").value;
        var status = document.getElementById("status");
        var re = /^[^\s()<>@,;:\/]+@\w[\w\.-]+\.[a-z]{2,}$/i;
        if (re.test(str)) status.innerHTML = " <fmt:message key="signup_jsp.massage.address_correct"/>";
        else status.innerHTML = "<fmt:message key="signup_jsp.massage.address_incorrect"/>";
        if (isEmpty(str)) status.innerHTML = "<fmt:message key="signup_jsp.massage.empty_field"/>";
    }
    function isEmpty(str) {
        return (str == null) || (str.length == 0);
    }
</script>
<a href="/index.jsp"><fmt:message key="signup_jsp.link.main"/></a>
<p></p>
<fieldset id="registration_form">
    <legend><fmt:message key="signup_jsp.legend.registration"/></legend>
    <form action="/controller" method="post">
        <input type="hidden" name="command" value="registration"/>
        <p><fmt:message key="signup_jsp.label.login"/> <input name="login" maxlength="20" pattern="[A-Za-z0-9]{4,}"
                                                              title="<fmt:message key="title.login"/>"
                                                              required><br/></p>
        <p><fmt:message key="signup_jsp.label.password"/> <input type="password" name="password" maxlength="20"
                                                                 pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                                                                 title="<fmt:message key="title.password"/>"
                                                                 required><br/></p>
        <p><fmt:message key="signup_jsp.label.name"/> <input name="first_name" maxlength="20"
                                                             pattern="[A-Za-zА-Яа-яЁё]{2,}" title="<fmt:message key="title.first_name"/>" required><br/></p>
        <p><fmt:message key="signup_jsp.label.surname"/> <input name="last_name" maxlength="20"
                                                                pattern="[A-Za-zА-Яа-яЁё]{2,}" title="<fmt:message key="title.last_name"/>" required><br/></p>
        <p>Email <input id="email" type="email" name="email" maxlength="100" onclick="isEmail()" required><span
                id="status"></span><br/></p>
        <p><input type="submit" value="<fmt:message key="signup_jsp.form.submit_signup"/>">
    </form>
</fieldset>
</body>
</html>
