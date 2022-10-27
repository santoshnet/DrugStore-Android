package com.frontendsource.drugstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResult {
    @SerializedName("categories")
    List<Category> categoryList;
    @SerializedName("status")
    int status;
    @SerializedName("message")
    String message;

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
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
