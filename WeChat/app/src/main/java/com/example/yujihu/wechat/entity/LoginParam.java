package com.example.yujihu.wechat.entity;

import java.io.Serializable;

/**
 * Created by yujihu on 2016/6/1.
 */
public class LoginParam implements Serializable {
    private String user_name;
    private String user_pwd;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

}
