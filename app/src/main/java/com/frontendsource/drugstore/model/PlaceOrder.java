package com.frontendsource.drugstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceOrder {
    @SerializedName("token")
    String token;
    @SerializedName("name")
    String name;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("email")
    String email;
    @SerializedName("city")
    String city;
    @SerializedName("state")
    String state;
    @SerializedName("zip_code")
    String zip_code;
    @SerializedName("address")
    String address;


    @SerializedName("orderitems")
    List<OrderItem> orderitems;

    public PlaceOrder(String token, String name, String mobile, String email, String city, String state, String zip_code, String address, List<OrderItem> orderitems) {
        this.token = token;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.address = address;
        this.orderitems = orderitems;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderItem> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(List<OrderItem> orderitems) {
        this.orderitems = orderitems;
    }
}
