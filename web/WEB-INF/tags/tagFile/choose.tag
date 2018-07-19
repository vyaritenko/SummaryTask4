<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@attribute name="lock" required="true"%>
<%@attribute name="userId" required="true"%>
<%@tag pageEncoding="UTF-8" %>
<c:if test="${lock == 'unlock'}">
    <td align="center">
        <input type="submit" name="${userId}" value="<fmt:message key="button.lock_lock"/>"/>
    </td>
</c:if>
<c:if test="${lock == 'lock'}">
    <td align="center">
        <input type="submit" name="${userId}" value="<fmt:message key="button.lock_unlock"/>"/>
    </td>
</c:if>
