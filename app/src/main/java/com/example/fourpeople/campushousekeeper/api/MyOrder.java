package com.example.fourpeople.campushousekeeper.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/6.
 */

public class MyOrder implements Serializable {
    Integer id;
    Date createDate;
    Date editDate;
    User user;
    Goods goods;
    String money;
    int orderState;
    Boolean commentState;//是否评论
    Boolean over;//是否取消
    int BuyNumber;

    public int getBuyNumber() {
        return BuyNumber;
    }

    public void setBuyNumber(int buyNumber) {
        BuyNumber = buyNumber;
    }

    public Boolean getOver() {
        return over;
    }

    public void setOver(Boolean over) {
        this.over = over;
    }

    public Boolean getCommentState() {
        return commentState;
    }

    public void setCommentState(Boolean commentState) {
        this.commentState = commentState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }
}
