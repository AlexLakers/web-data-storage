<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 21.10.2024
  Time: 17:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Uploading process page</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/files/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" id="fileId" required>
    <input type="hidden" name="prevPage" value="${requestScope.prevPage}" id="prevPageId">
    <button type="submit">Upload</button>
</form>

</body>
</html>
