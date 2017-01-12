package com.example.fourpeople.campushousekeeper.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/9.
 */

public class GoodsComment implements Serializable {
    Integer id;
    Date createDate;
    Date editDate;
    User user;
    String text;
    Goods goods;
    Date myOrderDate;

    public Date getMyOrderDate() {
        return myOrderDate;
    }

    public void setMyOrderDate(Date myOrderDate) {
        this.myOrderDate = myOrderDate;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
