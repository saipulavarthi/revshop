package com.example.DAOO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Entity.Order;
import com.example.Entity.OrderItem;
import DBConnection.dbconnection;

public class OrderDAO {
    private Connection connection;

    public OrderDAO() {
        this.connection = dbconnection.getConnection();
    }

    // Method to create a new order
    public int createOrder(Order order) throws SQLException {
        // Update the query to include shipping and billing address
        String orderQuery = "INSERT INTO orders (buyer_id, shipping_address, billing_address, status,contact_number,total_amount) VALUES (?, ?, ?, 'pending',?,?)";
        
        try (PreparedStatement orderStmt = connection.prepareStatement(orderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            orderStmt.setInt(1, order.getBuyerId());
            orderStmt.setString(2, order.getShippingAddress()); // Add shipping address
            orderStmt.setString(3, order.getBillingAddress());
            orderStmt.setInt(4,order.getContactNumber());// Add billing address
            orderStmt.setFloat(5, (float) order.getTotalAmount());

            // Execute the insert
            orderStmt.executeUpdate();

            // Get the generated order ID
            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }// Indicate failure to create the order
    }

    // Method to add order items to an order
    public void addOrderItems(List<OrderItem> orderItems) throws SQLException {
        String itemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement itemStmt = connection.prepareStatement(itemQuery)) {
            for (OrderItem item : orderItems) {
                itemStmt.setInt(1, item.getOrderId());
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getPrice());
                itemStmt.addBatch(); // Add to batch for performance
            }
            itemStmt.executeBatch(); // Execute batch insert
        }
    }

    // Method to get orders for a specific buyer
    public List<Order> getOrdersByBuyerId(int buyerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE buyer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, buyerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String status = rs.getString("status");
                java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
                orders.add(new Order(orderId, buyerId, status, createdAt));
            }
        }
        return orders;
    }

    // Method to get order details by order ID
    public List<OrderItem> getOrderItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int orderItemId = rs.getInt("order_item_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                orderItems.add(new OrderItem(orderItemId, orderId, productId, quantity, price));
            }
        }
        return orderItems;
    }

    // Optional: Method to delete an order by order ID
    public boolean deleteOrder(int orderId) throws SQLException {
        String query = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Returns true if a row was deleted
        }
    }
}
