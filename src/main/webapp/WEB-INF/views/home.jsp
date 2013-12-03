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
<c:if test="${not empty actor}">
<p>ID			: <c:out value="${actor.actorId}" /></p>
<p>First Name	: <c:out value="${actor.firstName}" /></p>
<p>Last Name	: <c:out value="${actor.lastName}" /></p>
<p>Last Update	: <c:out value="${actor.lastUpdate}" /></p>
</c:if>
</body>
</html>
