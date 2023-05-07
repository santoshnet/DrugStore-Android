package com.frontendsource.drugstore.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Drug App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class Category {
    String id;
    String category;
    String cateimg;
    String token;
    String category_id;
   List<SubCategory> subCategory = new ArrayList<>();

    public Category(String token, String category_id) {
        this.token = token;
        this.category_id = category_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCateimg() {
        return cateimg;
    }

    public void setCateimg(String cateimg) {
        this.cateimg = cateimg;
    }

    public List<SubCategory> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategory> subCategory) {
        this.subCategory = subCategory;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
