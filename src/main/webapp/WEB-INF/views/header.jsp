<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 05.10.2024
  Time: 23:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

<hr>
<b style="color: chocolate;font-size: large">The File Storage</b>

<c:if test="${empty sessionScope.account}">
    <a href="${pageContext.request.contextPath}/login">
        <button type="button">Login</button>
    </a>
    <a href="${pageContext.request.contextPath}/registration">
        <button type="button">Registration</button>
    </a>
</c:if>

<c:if test="${not empty sessionScope.account}">
    <a href="${pageContext.request.contextPath}/logout">
        <button type="button">Logout</button>
    </a>
</c:if>
<a href="${pageContext.request.contextPath}/index.jsp">
    <button type="button">Main page</button>
</a>


<hr>
</body>
</html>
