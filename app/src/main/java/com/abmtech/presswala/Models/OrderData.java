package com.abmtech.presswala.Models;

public class OrderData {


    public OrderData() {
    }

    String userId,orderId,productOrderId,totalPrice ,productPrice,productName, orderStatus  ,userName, userMobile , orderDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(String productOrderId) {
        this.productOrderId = productOrderId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productOrderId='" + productOrderId + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productName='" + productName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", userName='" + userName + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }
}
