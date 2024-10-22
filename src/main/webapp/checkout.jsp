<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.CartItem" %>
<%@ page import="com.example.Entity.Buyer" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #ff7e5f, #feb47b);
            display: flex;
            flex-direction: column;
            align-items: center;
            min-height: 100vh;
            animation: fadeIn 1s ease-in-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        h1 {
            color: #333;
            margin: 20px 0;
        }

        .error {
            color: red;
            margin: 20px 0;
        }

        .form-container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
            width: 80%;
            max-width: 500px;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin-bottom: 8px;
            color: #555;
            font-weight: bold;
        }

        input[type="text"] {
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            font-size: 16px;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        a:hover {
            background-color: #45a049;
        }
    </style>
</head>

<body>
    <h1>Checkout</h1>
    
    <% 
        // Retrieve Buyer object from session
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        
        // Check if buyer is null (not logged in)
        if (buyer == null) { 
    %>
        <div class="error">You must be logged in to checkout.</div>
        <a href="BuyerLogin.html">Login</a>
    <% 
        return; // Stop processing if the user is not logged in 
    } %>
    
    <% 
        // Retrieve the cart from the session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        Double totalAmount = (Double) session.getAttribute("totalAmount");
        if (totalAmount == null) {
            totalAmount = 0.0; // Default value if not set
        }
        
        // Display error message if it exists
        String errorMessage = (String) session.getAttribute("error");
        if (errorMessage != null) { 
    %>
        <div class="error"><%= errorMessage %></div>
    <% 
        session.removeAttribute("error"); 
    } %>

    <div class="form-container">
        <form action="CheckoutServlet" method="post">
            <input type="hidden" name="buyerId" value="<%= buyer.getBuyerId() %>">
            <input type="hidden" name="totalAmount" value="<%= totalAmount %>">
            
            <label for="shippingAddress">Shipping Address:</label>
            <input type="text" id="shippingAddress" name="shippingAddress" value="<%= request.getParameter("shippingAddress") %>" required><br><br>
            
            <label for="billingAddress">Billing Address:</label>
            <input type="text" id="billingAddress" name="billingAddress" value="<%= request.getParameter("billingAddress") %>" required><br><br>
            
            <label for="contactNumber">Contact Number:</label>
            <input type="text" id="contactNumber" name="contactNumber" value="<%= request.getParameter("contactNumber") %>" required><br><br>
            
            <input type="hidden" name="paymentStatus" value="Pending">
            <input type="hidden" name="paymentMethod" value="Credit Card">
            
            <% 
                // Add hidden fields for each product in the cart
                if (cart != null) {
                    for (CartItem cartItem : cart) { 
            %>
                    <input type="hidden" name="productId" value="<%= cartItem.getProduct().getProductId() %>">
                    <input type="hidden" name="quantity" value="<%= cartItem.getQuantity() %>">
            <% 
                    } 
                } else { 
            %>
                <div class="error">Your cart is empty.</div>
            <% 
                } 
            %>
            
            <input type="submit" value="Place Order">
        </form>
    </div>
    
    <a href="cart.jsp">Go back to Cart</a>
        <script>
        function placeOrder() {
            console.log("Place Order button clicked");
            const totalAmount = <%= (int)(totalAmount * 100) %>; // amount in paise
            const orderAddress = document.getElementById("orderAddress").value;

            if (!orderAddress) {
                alert("Please enter your delivery address.");
                return;
            }

            // Razorpay payment options
            const options = {
                "key": "rzp_test_37MbLXJMCUd3yL",
                "amount": totalAmount,
                "currency": "INR",
                "name": "Revshop",
                "description": "Order Payment",
                "handler": function (response) {
                    console.log("Payment successful, response:", response);

                    // Call the servlet to process the order
                    fetch('<%= request.getContextPath() %>/checkout', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({
                            userId: user.id,
                            paymentId: response.razorpay_payment_id,
                            orderAddress: orderAddress,
                            grandTotal: totalAmount / 100  // Send total amount back in rupees
                        })
                    })
                    .then(res => res.json())
                    .then(data => {
                        if (data.success) {
                            alert("Order placed successfully!");
                            window.location.href = "<%= request.getContextPath() %>/orderConfirmation.jsp";
                        } else {
                            alert("Order confirmation failed.");
                        }
                    })
                    .catch(err => console.error('Error confirming order:', err));
                },
                "prefill": {
                    "name": "vinay",
                    "email": "mudapaka.vinay@gmail.com",
                    "contact": "8497908605"
                },
                "theme": {
                    "color": "#3399cc"
                }
            };

            const rzp1 = new Razorpay(options);
            rzp1.open();
        }
    </script>
</body>
</html>
</html>