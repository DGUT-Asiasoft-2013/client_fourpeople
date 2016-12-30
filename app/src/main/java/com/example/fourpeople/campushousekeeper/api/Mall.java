package com.example.fourpeople.campushousekeeper.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/23.
 */

public class Mall implements Serializable {
    Integer id;
    String shopName;
    String shopType;
    String shopAvatar;
    User user;
    Date createDate;
    Date editDate;
    int shopLiked;
    String shopAbout;

    public int getShopLiked() {
        return shopLiked;
    }

    public void setShopLiked(int shopLiked) {
        this.shopLiked = shopLiked;
    }

    public String getShopAbout() {
        return shopAbout;
    }

    public void setShopAbout(String shopAbout) {
        this.shopAbout = shopAbout;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
