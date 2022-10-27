package com.frontendsource.drugstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResult {
    @SerializedName("products")
    List<Product> productList;
    @SerializedName("status")
    int status;
    @SerializedName("message")
    String message;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
