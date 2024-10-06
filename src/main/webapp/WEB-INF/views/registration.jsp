<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 05.10.2024
  Time: 23:57
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Registration</title>
</head>

<body>
<%@include file="header.jsp" %>
<h1>Registration form:</h1>
<div style="color: red">
    <c:if test="${not empty requestScope.validErrors}">
        <c:forEach var="validError" items="${requestScope.validErrors}">
            <span>${validError}</span>
        </c:forEach>
    </c:if>
</div>
<form action="${pageContext.request.contextPath}/registration" method="post">
    <label for="firstNameId">First name:
        <input type="text" name="firstName" id="firstNameId" required>
    </label><br>
    <label for="lastNameId">Last name:
        <input type="text" name="lastName" id="lastNameId" required>
    </label><br>
    <label for="birthDateId">Birthday date:
        <input type="date" name="birthDate" id="birthDateId" required>
    </label><br>

    <select name="role" id="roleId">Role:
        <c:forEach var="role" items="${requestScope.roles}">
            <option value="${role}">${role}</option>
        </c:forEach>
    </select><br>

    <label for="loginId">Login:
        <input type="text" name="login" id="loginId" required>
    </label><br>

    <label for="passwordId">Password:
        <input type="password" name="password" id="passwordId" required>
    </label><br>

    <label for="folderId">Folder:
        <input type="text" name="folder" id="folderId" required>
    </label><br>

    <button type="submit">Register</button>
</form>
</body>
</html>
