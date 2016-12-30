package com.example.fourpeople.campushousekeeper.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/23.
 */

public class Goods implements Serializable {
    Integer id;
    String goodsName;
    String goodsPiece;
    String goodsNumber;
    String goodsAbout;
    String goodsAvatar;
    Mall mall;
    Date createDate;
    Date editDate;
    int goodsLiked;

    public int getGoodsLiked() {
        return goodsLiked;
    }

    public void setGoodsLiked(int goodsLiked) {
        this.goodsLiked = goodsLiked;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPiece() {
        return goodsPiece;
    }

    public void setGoodsPiece(String goodsPiece) {
        this.goodsPiece = goodsPiece;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsAbout() {
        return goodsAbout;
    }

    public void setGoodsAbout(String goodsAbout) {
        this.goodsAbout = goodsAbout;
    }

    public String getGoodsAvatar() {
        return goodsAvatar;
    }

    public void setGoodsAvatar(String goodsAvatar) {
        this.goodsAvatar = goodsAvatar;
    }

    public Mall getMall() {
        return mall;
    }

    public void setMall(Mall mall) {
        this.mall = mall;
    }
}
