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

    <h1>Welcome to the webshop version 0.1</h1>
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

    <p>Click on the button to see all the products in storage.</p>

    <a href="<%= request.getContextPath() %>/test-product">
        Show products
    </a>
    <a href="<%= request.getContextPath() %>/cart">
            Show cart
    </a>


    <a href="<%= request.getContextPath() %>/login.jsp">
        Login
    </a>

    <a href="<%= request.getContextPath() %>/userAdmin">
        User administration
    </a>
</body>
</html>