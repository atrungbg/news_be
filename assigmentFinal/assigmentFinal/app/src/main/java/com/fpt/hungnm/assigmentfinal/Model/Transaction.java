package com.fpt.hungnm.assigmentfinal.Model;

import java.util.Date;

public class Transaction {
    private int id;
    private String title;
    private String price;
    private String category;

    private String isIncome;
    private String createDate;

    public Transaction(int id, String title, String price, String category, String isIncome, String createDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.category = category;
        this.isIncome = isIncome;
        this.createDate = createDate;
    }

    public Transaction(String title, String price, String category, String isIncome, String createDate) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.isIncome = isIncome;
        this.createDate = createDate;
    }

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsIncome() {
        return isIncome;
    }

    public void setIsIncome(String isIncome) {
        this.isIncome = isIncome;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
