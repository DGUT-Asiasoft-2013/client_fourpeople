package com.example.fourpeople.campushousekeeper.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Interview implements Serializable {
    String time;
    String area;
    String phone;
    String remark;
    String interviewer;
    String employer;
    String authorAvater;
    Date createDate;
    Date editDete;
    int id;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getAuthorAvater() {
        return authorAvater;
    }

    public void setAuthorAvater(String authorAvater) {
        this.authorAvater = authorAvater;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDete() {
        return editDete;
    }

    public void setEditDete(Date editDete) {
        this.editDete = editDete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
