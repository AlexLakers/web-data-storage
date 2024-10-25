<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 22.10.2024
  Time: 07:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List of files</title>
</head>
<body>
<%@include file="header.jsp"%>

<h3>Upload a new file:</h3>
<div style="color: blue">
    <a href="${pageContext.request.contextPath}/files/upload"><button type="button">Upload</button></a>
    <c:if test="${not empty param.file}">
        <span>The file '${param.file}' was ${param.status} successfully</span>
    </c:if>

</div>

<h3>Search all the necessary files by the filter:</h3>
<div>
    <form action="${pageContext.request.contextPath}/files" method="post">
        <label for="nameId">The file name contains:
            <input type="text" name="name" id="nameId">
        </label><br>
        <label for="sizeId">The file size more then:
            <input type="text" name="size" id="sizeId">
        </label><br>
        <label for="uploadDateId">The creation date is later then:
            <input type="date" name="uploadDate" id="uploadDateId">
        </label><br>
        <label for="limitId">The limit of files:
            <input type="text" name="limit" id="limitId" >
        </label><br>
        <button type="submit">Find</button>
    </form>
</div>

<h2>List of files:</h2>
<div>
    <c:if test="${not empty requestScope.readFileInfoDtoList}">
        <span>Please,click on the "Download" to start the downloading process</span>
        <c:forEach var="file" items="${requestScope.readFileInfoDtoList}">
            <ul>
                <li>
                    <details>
                        <summary>
                            <span>${file.name}</span>
                            <a href="${pageContext.request.contextPath}files/download?file=${file.name}"><button style="color: green" type="button" >Download</button></a>
                            <a href="${pageContext.request.contextPath}files/delete?id=${file.id}&file=${file.name}"><button style="color: red" type="button">Delete</button></a>
                        </summary>
                        <p>${file.desc}</p>
                    </details>
                </li>
            </ul>
        </c:forEach>
    </c:if>
</div>

<div style="color: red">
    <c:if test="${not empty requestScope.validErrors}">
        <c:forEach var="validError" items="${requestScope.validErrors}">
            <span>${validError}</span>
        </c:forEach>
    </c:if>
    <c:if test="${not empty requestScope.serviceError}">
        <span>${requestScope.serviceError}</span>
    </c:if>
</div>

</body>
</html>
