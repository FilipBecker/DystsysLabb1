<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Webshop - Start page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 50px;
            text-align: center;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 24px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 16px;
        }
        a:hover {
            background-color: #45a049;
        }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>

<h1>User administration page</h1>
<% String userName = (String) session.getAttribute("userName");
    if (userName != null) {
%> <p>Loged in as: <%= userName %></p>
<%} else {
%> <p>Not loged in</p>
<%}%>
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p class="error"><%= error %></p>
<%
    }
%>

<p>Search users by</p>
<form method="get" action="<%= request.getContextPath() %>/Users">
    <input type="radio" id="All" name="SearchType" value="all">
    <label for="All">All</label><br>
    <input type="radio" id="Id" name="SearchType" value="id">
    <label for="Id">Id</label><br>
    <input type="radio" id="Username" name="SearchType" value="username">
    <label for="Username">Username</label><br>
    <input type="radio" id="Role" name="SearchType" value="role">
    <label for="Role">Role</label><br>
    <input type="radio" id="Email" name="SearchType" value="email">
    <label for="Email">Email</label><br>
    <input type="text" name="SearchValue" value="">
    <input type="submit" value="Search">
</form>

</body>
</html>