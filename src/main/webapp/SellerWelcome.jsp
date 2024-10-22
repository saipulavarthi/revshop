<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.Product" %>
<%@ page import="com.example.DAOO.ProductDAOImpl" %>
<%@ page import="DBConnection.dbconnection" %>
<%
    ProductDAOImpl productDAO = new ProductDAOImpl();
    int sellerId = (Integer) session.getAttribute("sellerId"); // Get sellerId from session
    List<Product> products = productDAO.getProductsBySellerId(sellerId); // Fetch products
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Seller Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
        th { background-color: #f4f4f4; }
        .navbar { margin-bottom: 20px; }
        .navbar a { margin-right: 15px; }
    </style>
</head>
<body>
    <h2>Welcome, Seller!</h2>
    <div class="navbar">
        <a href="ProductAdd.html">Add Product</a>
        <a href="index.html">Logout</a>
    </div>

    <h3>Your Products</h3>
    
    <table>
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Description</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Category</th>
                <th>Image</th>
            </tr>
        </thead>
        <tbody>
            <% for (Product product : products) { %>
                <tr>
                    <td><%= product.getProductName() %></td>
                    <td><%= product.getDescription() %></td>
                    <td><%= product.getPrice() %></td>
                    <td><%= product.getQuantity() %></td>
                    <td><%= product.getCategory() %></td>
                    <td><img src="<%= product.getImageUrl() %>" alt="<%= product.getProductName() %>" style="width:50px; height:50px;"></td>
                </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
