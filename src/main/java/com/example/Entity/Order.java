package com.example.Entity;

import java.sql.Timestamp;

public class Order {
    private int orderId;              // Unique order ID
    private int buyerId;              // ID of the buyer
    private String status;             // Status of the order (e.g., "Pending", "Completed")
    private String shippingAddress;    // Shipping address for the order
    private String billingAddress;     // Billing address for the order
    private double totalAmount;        // Total amount of the order
    private String createdAt;   
    private int contactNumber;// Timestamp of when the order was created
    private int productId;
    private  int quantity;

    

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	// Constructor
    public Order(int orderId, int buyerId, String status, String shippingAddress, String billingAddress, double totalAmount, String createdAt) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public Order(int orderId, int buyerId, String status, Timestamp createdAt) {
    	this.orderId=orderId;
    	this.buyerId=buyerId;
    	this.status=status;
 
		// TODO Auto-generated constructor stub
	}

	public Order(int buyerId, String shippingAddress, String billingAddress, String string) {
		this.buyerId=buyerId;
		this.shippingAddress=shippingAddress;
		this.billingAddress=billingAddress;
	
		// TODO Auto-generated constructor stub
	}

	public Order() {
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String timestamp) {
        this.createdAt = timestamp;
    }
    public int getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}
}
