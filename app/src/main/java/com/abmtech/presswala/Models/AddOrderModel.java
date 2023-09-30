package com.abmtech.presswala.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class AddOrderModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private OrderData data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

    public static class OrderData {



        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("orderId")
        @Expose
        private String orderId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("userMobile")
        @Expose
        private String userMobile;
        @SerializedName("orderDate")
        @Expose
        private String orderDate;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("orderStatus")
        @Expose
        private String orderStatus;
        @SerializedName("productName")
        @Expose
        private String productName;
        @SerializedName("productPrice")
        @Expose
        private String productPrice;
        @SerializedName("totalPrice")
        @Expose
        private String totalPrice;
        @SerializedName("productOrderId")
        @Expose
        private String productOrderId;
        @SerializedName("customer_otp")
        @Expose
        private String customerOtp;
        @SerializedName("delivery_boy_otp")
        @Expose
        private String deliveryBoyOtp;
        @SerializedName("items")
        @Expose
        private List<Item> items;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getProductOrderId() {
            return productOrderId;
        }

        public void setProductOrderId(String productOrderId) {
            this.productOrderId = productOrderId;
        }

        public String getCustomerOtp() {
            return customerOtp;
        }

        public void setCustomerOtp(String customerOtp) {
            this.customerOtp = customerOtp;
        }

        public String getDeliveryBoyOtp() {
            return deliveryBoyOtp;
        }

        public void setDeliveryBoyOtp(String deliveryBoyOtp) {
            this.deliveryBoyOtp = deliveryBoyOtp;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public static  class Item {

            public Item(String proName, String proPrice, String proQuantity, String orderId) {
                this.proName = proName;
                this.proPrice = proPrice;
                this.proQuantity = proQuantity;
                this.orderId = orderId;
            }

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("pro_name")
            @Expose
            private String proName;
            @SerializedName("pro_price")
            @Expose
            private String proPrice;
            @SerializedName("pro_quantity")
            @Expose
            private String proQuantity;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("orderId")
            @Expose
            private String orderId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProName() {
                return proName;
            }

            public void setProName(String proName) {
                this.proName = proName;
            }

            public String getProPrice() {
                return proPrice;
            }

            public void setProPrice(String proPrice) {
                this.proPrice = proPrice;
            }

            public String getProQuantity() {
                return proQuantity;
            }

            public void setProQuantity(String proQuantity) {
                this.proQuantity = proQuantity;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }


            @Override
            public String toString() {
                return "Item{" +
                        "id='" + id + '\'' +
                        ", proName='" + proName + '\'' +
                        ", proPrice='" + proPrice + '\'' +
                        ", proQuantity='" + proQuantity + '\'' +
                        ", date='" + date + '\'' +
                        ", orderId='" + orderId + '\'' +
                        '}';
            }
        }
    }
}
