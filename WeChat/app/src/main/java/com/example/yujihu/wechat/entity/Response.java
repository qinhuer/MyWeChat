package com.example.yujihu.wechat.entity;

/**
 * Created by yujihu on 2016/6/1.
 */
public class Response {
    private int success;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
