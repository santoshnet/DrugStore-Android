package com.frontendsource.drugstore.model;

public class OrderItem {
    String id;
    String orderid;
    String attribute;
    String currency;
    String itemImage;
    String token;
    String order_id;
    String itemName;
    String itemQuantity;
    String itemPrice;
    String itemTotal;

    public OrderItem() {
    }

    public OrderItem(String itemName, String itemQuantity, String attribute, String currency, String itemImage, String itemPrice, String itemTotal) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.attribute = attribute;
        this.currency = currency;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.itemTotal = itemTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }



    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getitemImage() {
        return itemImage;
    }


    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }
}
