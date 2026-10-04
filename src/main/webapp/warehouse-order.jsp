<%@ page import="ui.ViewOrder" %>
<%@ page import="ui.ViewOrderLine" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Order</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f5f5f5;
        }

        .order {
            background: white;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            max-width: 600px;
        }

        .line {
            border-bottom: 1px solid #ddd;
            padding: 10px 0;
        }

        button {
            padding: 10px 15px;
            cursor: pointer;
        }
    </style>
</head>
    <a href="<%= request.getContextPath() %>/" class="back-btn">
        ← Back to the start page
    </a>
<body>

<%
    ViewOrder order =
            (ViewOrder) request.getAttribute("order");

    List<ViewOrderLine> lines =
            (List<ViewOrderLine>) request.getAttribute("lines");
%>

<h1>Order #<%= order.getId() %></h1>

<p>
    User ID: <%= order.getUserId() %>
</p>

<p>
    Date: <%= order.getOrderDate() %>
</p>

<p>
    Status: <%= order.getStatus() %>
</p>

<h2>Products</h2>

<%
    for (ViewOrderLine line : lines) {
%>

<div class="line">

    <strong>
        Product ID: <%= line.getProductId() %>
    </strong>

    <br>

    Quantity:
    <%= line.getQuantity() %>

    <br>

    Price:
    <%= line.getPrice() %>

</div>

<%
    }
%>

<% if ("NEW".equals(order.getStatus())) { %>

    <form action="<%= request.getContextPath() %>/warehouse" method="post">

        <input type="hidden" name="action" value="pack">
        <input type="hidden" name="orderId" value="<%= order.getId() %>">

        <button type="submit">
            Pack order
        </button>

    </form>

<% } else if ("PACKED".equals(order.getStatus())) { %>

    <p>
        <strong>Status:</strong> PACKED
    </p>

<% } %>

    <br>

    <a href="<%= request.getContextPath() %>/warehouse">
        Back to orders
    </a>

</div>

</body>
</html>