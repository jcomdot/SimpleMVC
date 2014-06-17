<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello home world!
</h1>

<P>  The time on the server is ${serverTime}. </P>
<c:if test="${not empty host}">
<p>host	: <c:out value="${ host }" /></p>
</c:if>
<c:if test="${not empty osName}">
<p>osName	: <c:out value="${ osName }" /></p>
</c:if>
</body>
</html>
