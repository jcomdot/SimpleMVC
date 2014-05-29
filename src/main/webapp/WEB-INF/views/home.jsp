<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!
</h1>

<P>  The time on the server is ${serverTime}. </P>
<c:if test="${not empty user}">
<p>ID	: <c:out value="${user.id}" /></p>
<p>Name	: <c:out value="${user.name}" /></p>
<p>Email: <c:out value="${user.email}" /></p>
</c:if>
</body>
</html>
