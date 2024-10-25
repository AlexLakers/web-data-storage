<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 06.10.2024
  Time: 22:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<%@include file="header.jsp" %>
<h2>Login form</h2>
<form action="${pageContext.request.contextPath}/login" method="post">

    <label style="color: green" for="loginId">Enter your login:
        <input type="text" name="login" id="loginId" value="${param.login}" required>
    </label><br>

    <label style="color: green" for="passwordId">Enter your password:
        <input type="password" name="password" id="passwordId">
    </label><br>

    <button type="submit">Login</button>

    <div style="color: red">
        <c:if test="${not empty requestScope.authWarn}">
            <span>${requestScope.authWarn}</span>
        </c:if>
        <%--        <c:if test="${not empty requestScope.authError}">
                    <span>${requestScope.authError}</span>
                </c:if>--%>
    </div>
</form>
</body>
</html>
