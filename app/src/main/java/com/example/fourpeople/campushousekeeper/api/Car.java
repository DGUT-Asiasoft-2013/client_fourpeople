package com.example.fourpeople.campushousekeeper.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/3.
 */

public class Car implements Serializable {
    Goods goods;
    Date createDate;
    Date editDate;
    Integer customerId;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
