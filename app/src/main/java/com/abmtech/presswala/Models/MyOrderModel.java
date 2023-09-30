package com.abmtech.presswala.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<MyOrderData> data;

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

    public List<MyOrderData> getData() {
        return data;
    }

    public void setData(List<MyOrderData> data) {
        this.data = data;
    }

    public class MyOrderData {


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
        @SerializedName("orderStatus")
        @Expose
        private String orderStatus;
        @SerializedName("totalPrice")
        @Expose
        private String totalPrice;
        @SerializedName("customer_otp")
        @Expose
        private String customerOtp;

        @SerializedName("delivery_boy_otp")
        @Expose
        private String deliveryBoyOtp;


        @SerializedName("delivery_boy_name")
        @Expose
        private String delivery_boy_name;

        @SerializedName("dev_boy_mobile")
        @Expose
        private String dev_boy_mobile;

        @SerializedName("dev_boy_id")
        @Expose
        private String dev_boy_id;

        @SerializedName("items")
        @Expose
        private List<MyOrderItem> items;

        public String getDelivery_boy_name() {
            return delivery_boy_name;
        }

        public void setDelivery_boy_name(String delivery_boy_name) {
            this.delivery_boy_name = delivery_boy_name;
        }

        public String getDev_boy_mobile() {
            return dev_boy_mobile;
        }

        public void setDev_boy_mobile(String dev_boy_mobile) {
            this.dev_boy_mobile = dev_boy_mobile;
        }

        public String getDev_boy_id() {
            return dev_boy_id;
        }

        public void setDev_boy_id(String dev_boy_id) {
            this.dev_boy_id = dev_boy_id;
        }

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

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
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

        public List<MyOrderItem> getItems() {
            return items;
        }

        public void setItems(List<MyOrderItem> items) {
            this.items = items;
        }

        public class MyOrderItem {
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

        }


    }

}
