<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Text Back Machine</title>
</head>
<body>
<p>Enter a file that you wish to reverse.</p>
<%= new java.util.Date().toString() %>
<form method="post" action="ReverseText" enctype="multipart/form-data">
	<br><input type="file" name="theFile">
	<br><input type="submit" value="Upload">
</form>
</body>
</html>