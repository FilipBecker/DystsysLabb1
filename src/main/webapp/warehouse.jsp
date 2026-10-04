<%@ page import="java.util.List" %>
<%@ page import="ui.ViewOrder" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Warehouse</title>

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
            margin-bottom: 15px;
            max-width: 600px;
        }

        button {
            padding: 8px 14px;
            cursor: pointer;
        }
    </style>
</head>

<body>
    <a href="<%= request.getContextPath() %>/" class="back-btn">
        ← Back to the start page
    </a>

<%
    List<ViewOrder> orders =
            (List<ViewOrder>) request.getAttribute("orders");

    List<ViewOrder> packedOrders =
            (List<ViewOrder>) request.getAttribute("packedOrders");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Warehouse</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f5f5f5;
        }

        .order {
            background: white;
            padding: 20px;
            margin-bottom: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        h1 {
            margin-bottom: 30px;
        }

        h2 {
            margin-top: 30px;
        }

        a {
            text-decoration: none;
        }

        button {
            padding: 8px 15px;
            cursor: pointer;
        }
    </style>
</head>

<body>

<h1>Warehouse</h1>


<h2>Orders to pack</h2>

<%
    if (orders == null || orders.isEmpty()) {
%>

    <p>No orders to pack.</p>

<%
    } else {

        for (ViewOrder order : orders) {
%>

    <div class="order">

        <p>
            <strong>Order ID:</strong>
            <%= order.getId() %>
        </p>

        <p>
            <strong>User ID:</strong>
            <%= order.getUserId() %>
        </p>

        <p>
            <strong>Date:</strong>
            <%= order.getOrderDate() %>
        </p>

        <p>
            <strong>Status:</strong>
            <%= order.getStatus() %>
        </p>

        <a href="<%= request.getContextPath() %>/warehouse?orderId=<%= order.getId() %>">
            View order
        </a>

    </div>

<%
        }
    }
%>


<h2>Packed orders</h2>

<%
    if (packedOrders == null || packedOrders.isEmpty()) {
%>

    <p>No packed orders.</p>

<%
    } else {

        for (ViewOrder order : packedOrders) {
%>

    <div class="order">

        <p>
            <strong>Order ID:</strong>
            <%= order.getId() %>
        </p>

        <p>
            <strong>User ID:</strong>
            <%= order.getUserId() %>
        </p>

        <p>
            <strong>Date:</strong>
            <%= order.getOrderDate() %>
        </p>

        <p>
            <strong>Status:</strong>
            <%= order.getStatus() %>
        </p>

        <a href="<%= request.getContextPath() %>/warehouse?orderId=<%= order.getId() %>">
            View order
        </a>

    </div>

<%
        }
    }
%>

</body>
</html>