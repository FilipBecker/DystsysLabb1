<%@ page import="bo.Facade" %>
<%@ page import="ui.ViewItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World!</h2>
<% ViewItem item = Facade.getItem(); %>
<%= item.toString()%>
</body>
</html>
