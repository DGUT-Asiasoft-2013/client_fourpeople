package com.example.fourpeople.campushousekeeper.auction.entity;

import com.example.fourpeople.campushousekeeper.api.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    Integer id;
    User auctionner;
    Bid bid;
    String state;

    public User getAuctionner() {
        return auctionner;
    }

    public void setAuctionner(User auctionner) {
        this.auctionner = auctionner;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
