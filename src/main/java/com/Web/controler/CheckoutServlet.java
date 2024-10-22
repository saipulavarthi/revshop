package com.Web.controler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.example.DAOO.CartItemDAOImpl;
import com.example.DAOO.OrderDAO;
import com.example.DAOO.OrderItemDAO;
import com.example.Entity.CartItem;
import com.example.Entity.Order;
import com.example.Entity.OrderItem;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int buyerId = (int) session.getAttribute("buyerId"); // Retrieve buyerId from session
        String shippingAddress = request.getParameter("shippingAddress"); // Get shipping address from request
        String billingAddress = request.getParameter("billingAddress"); // Get billing address from request
        Double totalAmount = Double.parseDouble(request.getParameter("totalAmount")); // Retrieve total amount
        System.out.print( "see"+billingAddress);
        System.out.print("id"+buyerId);
        System.out.print("Amount" +totalAmount);
        // Initialize DAOs
        OrderDAO orderDAO = new OrderDAO();
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        CartItemDAOImpl cartItemDAO = new CartItemDAOImpl(); // Assuming you have a CartDAO for managing the cart

        try {
            // Step 1: Create a new order
            Order order = new Order(buyerId, shippingAddress, billingAddress, "Pending"); // Create an order with status 'Pending'
            int orderId = orderDAO.createOrder(order); // Assuming createOrder returns the order ID

            // Step 2: Retrieve cart items
            List<CartItem> cartItems = cartItemDAO.getCartItemsByBuyerId(buyerId); // Get cart items for the buyer
            
            // Step 3: Add order items
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem(orderId, cartItem.getProductId(), cartItem.getQuantity(), orderId, cartItem.getPrice());
                orderItemDAO.addOrderItem(orderItem); // Add each cart item as an order item
            }

            // Optional: Clear the cart after successful checkout
            cartItemDAO.clearCart(buyerId);

            // Step 4: Prepare Razorpay payment details
            int amountInPaise = (int) (totalAmount * 100); // Convert amount to paise
            String razorpayKey = "rzp_test_37MbLXJMCUd3yL"; // Replace with your Razorpay key

            // Set attributes to be accessed in Razorpay JSP
            request.setAttribute("razorpayKey", razorpayKey);
            request.setAttribute("amount", amountInPaise);
            request.setAttribute("orderId", orderId); // Pass the orderId for reference
            request.setAttribute("shippingAddress", shippingAddress); // Pass shipping address for potential use

            // Forward to Razorpay payment JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("razorpay.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred during checkout. Please try again.");
            response.sendRedirect("checkout.jsp"); // Redirect back to checkout page on error
        }
    }
}
