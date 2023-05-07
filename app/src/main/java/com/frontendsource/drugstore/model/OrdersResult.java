package com.frontendsource.drugstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersResult {
    @SerializedName("code")
    int code;
    @SerializedName("status")
    String status;
    @SerializedName("order_id")
    String order_id;

    @SerializedName("orders")
    List<Order> orderList;

    @SerializedName("orderList")
    List<OrderItem> orderItemList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
