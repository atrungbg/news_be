package com.fpt.hungnm.assigmentfinal.Model;

import java.util.Date;

public class Category {
    private int id;
    private String title;
    private String isIncome;
    private String createDate;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    private Long total;

    public Category(int id, String title, String isIncome, String createDate, Long total) {
        this.id = id;
        this.title = title;
        this.isIncome = isIncome;
        this.createDate = createDate;
        this.total = total;
    }

    public Category(int id, String title, String isIncome, String createDate) {
        this.id = id;
        this.title = title;
        this.isIncome = isIncome;
        this.createDate = createDate;
    }

    public Category(String title, String isIncome, String createDate) {
        this.title = title;
        this.isIncome = isIncome;
        this.createDate = createDate;
    }

    public Category() {
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
