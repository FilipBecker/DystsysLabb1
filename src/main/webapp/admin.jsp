<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Admin</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f5f5f5;
        }

        h1 {
            color: #333;
        }

        h2 {
            color: #333;
            margin-top: 0;
        }

        .back-btn {
            display: inline-block;
            margin-bottom: 20px;
        }

        .error {
            color: red;
            font-weight: bold;
        }

        .section {
            background: white;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 25px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.08);
            max-width: 500px;
        }

        input,
        textarea,
        select {
            width: 100%;
            box-sizing: border-box;
            padding: 8px;
            margin-top: 5px;
            margin-bottom: 12px;
        }

        textarea {
            min-height: 80px;
            resize: vertical;
        }

        button {
            padding: 8px 14px;
            cursor: pointer;
        }
    </style>
</head>

<body>

<a href="<%= request.getContextPath() %>/"
   class="back-btn">
    ← Back to the start page
</a>

<h1>Admin</h1>

<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>

    <p class="error"><%= error %></p>

<%
    }
%>


<div class="section">

    <h2>Create product</h2>

    <form method="post"
          action="<%= request.getContextPath() %>/admin">

        <input type="hidden"
               name="action"
               value="createProduct">

        <label>Name:</label>

        <input type="text"
               name="name"
               required>


        <label>Description:</label>

        <textarea name="description"></textarea>


        <label>Price:</label>

        <input type="number"
               name="price"
               step="0.01"
               min="0"
               required>


        <label>Stock:</label>

        <input type="number"
               name="stock"
               min="0"
               required>


        <label>Category ID:</label>

        <input type="number"
               name="categoryId"
               min="1"
               required>


        <button type="submit">
            Create product
        </button>

    </form>

</div>

<div class="section">

    <h2>Edit product</h2>

    <form method="post"
          action="<%= request.getContextPath() %>/admin">

        <input type="hidden"
               name="action"
               value="updateProduct">


        <label>Product ID:</label>

        <input type="number"
               name="id"
               min="1"
               required>


        <label>Name:</label>

        <input type="text"
               name="name"
               required>


        <label>Description:</label>

        <textarea name="description"></textarea>


        <label>Price:</label>

        <input type="number"
               name="price"
               step="0.01"
               min="0"
               required>


        <label>Stock:</label>

        <input type="number"
               name="stock"
               min="0"
               required>


        <label>Category ID:</label>

        <input type="number"
               name="categoryId"
               min="1"
               required>


        <button type="submit">
            Save product
        </button>

    </form>

</div>


<div class="section">

    <h2>Create category</h2>

    <form method="post"
          action="<%= request.getContextPath() %>/admin">

        <input type="hidden"
               name="action"
               value="createCategory">


        <label>Name:</label>

        <input type="text"
               name="name"
               required>


        <button type="submit">
            Create category
        </button>

    </form>

</div>

<div class="section">

    <h2>Edit category</h2>

    <form method="post"
          action="<%= request.getContextPath() %>/admin">

        <input type="hidden"
               name="action"
               value="updateCategory">


        <label>Category ID:</label>

        <input type="number"
               name="id"
               min="1"
               required>


        <label>Name:</label>

        <input type="text"
               name="name"
               required>


        <button type="submit">
            Save category
        </button>

    </form>

</div>

</body>
</html>
