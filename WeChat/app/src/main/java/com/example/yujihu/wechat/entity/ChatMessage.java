package com.example.yujihu.wechat.entity;

/**
 * Created by yujihu on 2016/6/2.
 */
public class ChatMessage {
    private String name;
    private String text;
    private  boolean isComMeg = true;

    public ChatMessage(boolean isComMeg, String name, String text) {
        this.isComMeg = isComMeg;
        this.name = name;
        this.text = text;
    }

    public ChatMessage() {
    }

    public boolean isComMeg() {
        return isComMeg;
    }

    public void setComMeg(boolean comMeg) {
        isComMeg = comMeg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
