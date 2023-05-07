package com.frontendsource.drugstore.model;

/**
 * Drug App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class SubCategory {
    String id;
    String category_id;
    String sub_category_title;
    String sub_category_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_title() {
        return sub_category_title;
    }

    public void setSub_category_title(String sub_category_title) {
        this.sub_category_title = sub_category_title;
    }

    public String getSub_category_img() {
        return sub_category_img;
    }

    public void setSub_category_img(String sub_category_img) {
        this.sub_category_img = sub_category_img;
    }
}
