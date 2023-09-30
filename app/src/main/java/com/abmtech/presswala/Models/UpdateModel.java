package com.abmtech.presswala.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user_otp")
    @Expose
    private String user_otp;
    @SerializedName("delivery_boy_otp")
    @Expose
    private String delivery_boy_otp;

    public String getUser_otp() {
        return user_otp;
    }

    public void setUser_otp(String user_otp) {
        this.user_otp = user_otp;
    }

    public String getDelivery_boy_otp() {
        return delivery_boy_otp;
    }

    public void setDelivery_boy_otp(String delivery_boy_otp) {
        this.delivery_boy_otp = delivery_boy_otp;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
