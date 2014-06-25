<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="./resources/js/jquery-2.1.1.js"></script>
<script type="text/javascript" src="./resources/js/json.min.js"></script>

<title>Message Converter Tests</title>

<script>

$(document).ready(function () {
	$("#loginidcheck").click(function() {
		$.getJSON("checkLoginId/" + $("#loginid").val(), function(result) {
			if (result.duplicated == true) {
				alert("이미 등록된 로그인ID입니다. " + result.availableId + "는 사용할 수 있습니다.");
			}
			else {
				alert("사용할 수 있는 로그인ID입니다.");
			}
		});
	});

	$("#user").submit(function() {
		var user = $(this).serializeObject();
		$.postJSON("register", user, function(result) {
			if (result.duplicated == true) {
				alert("이미 등록된 로그인ID입니다. " + result.availableId + "는 사용할 수 있습니다.");
			}
			else {
				alert("사용할 수 있는 로그인ID입니다.");
			}
		});
		return false;
	});
});

</script>

</head>
<body>

<label>로그인 아이디 : </label>
<input id="loginid" name="loginid" type="text" />
<input id="loginidcheck" type="button" value="아이디 중복검사" />

<form id="user">
	<fieldset>
		<label>로그인 아이디 : </label><input id="loginid" name="loginid" type="text" />
		<label>이름 : </label><input id="name" name="name" type="text" /><br />
		<input type="submit" value="등록" />
	</fieldset>
</form>
</body>
</html>