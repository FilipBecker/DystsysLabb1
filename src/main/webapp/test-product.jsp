<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Product" %>
<%@ page import="ui.ViewProduct" %>
<html>
<head>
    <title>All Products</title>
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

<h1>All the products in the webshop</h1>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p class="error"><%= error %></p>
<%
    } else {
        List<ViewProduct> products = (List<ViewProduct>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
%>
    <div class="product-grid">
<%
            for (ViewProduct p : products) {
%>
        <div class="card">
            <h2><%= p.getName() %></h2>
            <p><%= p.getDescription() %></p>
            <p class="ID">Id: <%= String.format("%d", p.getId()) %> </p>
            <p class="price"><%= String.format("%.2f", p.getPrice()) %> $</p>
            <p class="stock">In storage: <%= p.getStock() %> </p>

        </div>
<%
            }
%>
    </div>
<%
        } else {
%>
    <p>No products found.</p>
<%
        }
    }
%>

</body>
</html>