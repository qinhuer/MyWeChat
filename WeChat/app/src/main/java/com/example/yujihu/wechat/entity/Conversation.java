package com.example.yujihu.wechat.entity;

/**
 * Created by yujihu on 2016/6/2.
 */
public class Conversation {
    private String userName;
    private int msgCount;
    private String content;
    private boolean conversationState;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isConversationState() {
        return conversationState;
    }

    public void setConversationState(boolean conversationState) {
        this.conversationState = conversationState;
    }
}
