package com.example.fourpeople.campushousekeeper.auction.entity;

import com.example.fourpeople.campushousekeeper.api.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bid implements Serializable {
    User bider;
    Auction auction;
    String price;

    public User getBider() {
        return bider;
    }

    public void setBider(User bider) {
        this.bider = bider;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
