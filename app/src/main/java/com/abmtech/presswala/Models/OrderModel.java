package com.abmtech.presswala.Models;

import java.util.List;

public class OrderModel {


    String name;

    String orderStatus;
    List<OrderData> list;

    public OrderModel() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderData> getList() {
        return list;
    }

    public void setList(List<OrderData> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "name='" + name + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", list=" + list +
                '}';
    }
}
