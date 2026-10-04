<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>All Products</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>
<a href="<%= request.getContextPath() %>/" class="back-btn">
    ← Back to the start page
</a>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <p class="error"><%= error %></p>
    <%
        }
        %>
    <form method="post" action="<%= request.getContextPath() %>/login">
        <table>
            <tbody>
            <tr>
                <td>User Name</td>
                <td><input type="text" name="userName" value=""/></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="password" value=""></td>
            </tr>
            <tr>
                <td><input type="submit" value="Login"></td>
            </tr>
            </tbody>
        </table>
    </form>
</body>