<%@ page import="ui.ViewUser" %>
<%@ page import="java.util.List" %>
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

<a href="<%= request.getContextPath() %>/" class="back-btn">
    ← Back to the start page
</a>

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
    <input type="radio" id="All" name="SearchType" value="ALL">
    <label for="All">All</label><br>
    <input type="radio" id="Id" name="SearchType" value="ID">
    <label for="Id">Id</label><br>
    <input type="radio" id="Username" name="SearchType" value="USERNAME">
    <label for="Username">Username</label><br>
    <input type="radio" id="Role" name="SearchType" value="ROLE">
    <label for="Role">Role</label><br>
    <input type="radio" id="Email" name="SearchType" value="EMAIL">
    <label for="Email">Email</label><br>
    <input type="text" name="SearchValue" value="">
    <input type="submit" value="Search">
</form>

<% List<ViewUser> users = (List<ViewUser>) request.getAttribute("users");
    if (users != null && !users.isEmpty()) {
        %>
    <table>
        <tbody>
            <tr>
                <th>User id</th>
                <th>Username</th>
                <th>Role</th>
                <th>Email</th>
            </tr>
        <% for (ViewUser u: users) {%>
            <tr>
                <td><%= u.getId()%></td>
                <td><%= u.getUsername()%></td>
                <td><%= u.getRole()%></td>
                <td><%= u.getEmail()%></td>
            </tr>
        <%}%>
        </tbody>
    </table>
    <%}%>

<p>Add user</p>
<form method="post" action="<%= request.getContextPath() %>/Users">
    <label for="Username">Username</label>
    <input type="text" id="Username" name="userName" value=""><br>
    <label for="Password">Password</label>
    <input type="text" id="Password" name="password" value=""><br>

    <input type="radio" id="Warehouse" name="role" value="WAREHOUSE">
    <label for="Warehouse">Warehouse</label><br>
    <input type="radio" id="Customer" name="role" value="CUSTOMER">
    <label for="Customer">Customer</label><br>

    <label for="Email">Email</label>
    <input type="text" id="Email" name="email" value=""><br>
    <input type="submit" value="Create user">
</form>
</body>
</html>