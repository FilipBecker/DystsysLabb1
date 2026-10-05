<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ui.ViewItems.ViewProduct" %>
<%@ page import="ui.ViewItems.ViewCartItem" %>
<html>
<head>
    <title>Cart</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
        h1 { color: #333; }
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
        }
        .card {
            background: white;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.08);
        }
        .card h2 { margin-top: 0; font-size: 1.2rem; color: #222; }
        .price { font-weight: bold; color: #2a7a2a; font-size: 1.1rem; }
        .stock { color: #555; }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>

    <a href="<%= request.getContextPath() %>/" class="back-btn">
        ← Back to the start page
    </a>

<h1>All your items in the cart</h1>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p class="error"><%= error %></p>
<%
    } else {
        List<ViewCartItem> cart = (List<ViewCartItem>) request.getAttribute("cart");
        Double total = (Double) request.getAttribute("total");
        if (cart != null && !cart.isEmpty()) {

%>
    <p style="font-size: 1.3rem; font-weight: bold; margin-top: 20px;">
        Total: <%= String.format("%.2f", total) %> $
    </p>
    <ul class="cart-list">
<%
            for (ViewCartItem p : cart) {
%>
        <li class="cart-item">
            <span class="product-name"><%= p.getName() %></span>
            <span>Quantity: <%= p.getQuantity() %></span>
            <span class="price">
                <%= String.format("%.2f", p.getSubtotal()) %> $
            </span>
        </li>
<%
            }
%>

    </div>
    <a href="<%= request.getContextPath() %>/test-product">
                Show products
     </a>
    <form action="cart" method="post">
        <input type="hidden" name="action" value="clear">
        <button type="submit">Clear Cart</button>
    </form>
    <form action="<%= request.getContextPath() %>/cart" method="post">
        <input type="hidden" name="action" value="placeOrder">
        <button type="submit">Place Order</button>
    </form>
<%
        } else {
%>
    <p>Cart empty.</p>
    <a href="<%= request.getContextPath() %>/test-product">
                Show products
    </a>
<%
        }
    }
%>

</body>
</html>