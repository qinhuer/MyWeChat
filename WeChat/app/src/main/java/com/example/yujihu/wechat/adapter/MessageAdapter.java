package com.example.yujihu.wechat.adapter;

import java.util.List;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yujihu.wechat.R;
import com.example.yujihu.wechat.entity.ChatMessage;


public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> coll;
    private LayoutInflater mInflater;

    public static interface MsgType {
        int IMVT_COM_MEG = 0;
        int IMVT_TO_MSG = 1;
    }

    public MessageAdapter(Context context, List<ChatMessage> msgs) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        coll = msgs;
    }

    // public void setUser(String user) {
    // this.user = user;
    // }

    /**
     * 获取item数
     */
    public int getCount() {
        return coll.size();
    }


    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取item类型
     */
    public int getItemViewType(int position) {
        ChatMessage msg = coll.get(position);
        if (msg.isComMeg()) {
            return MsgType.IMVT_COM_MEG;
        } else {
            return MsgType.IMVT_TO_MSG;
        }
    }

    public int getViewTypeCount() {
        return 2;
    }


    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChatMessage msg = coll.get(position);
        boolean isComMsg = msg.isComMeg();

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (isComMsg) {
                convertView = mInflater.inflate(R.layout.row_received_message, null);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                holder.tv_username = (TextView) convertView.findViewById(R.id.tv_userid);

            } else {
                convertView = mInflater.inflate(R.layout.row_sent_message, null);
                convertView.findViewById(R.id.pb_sending).setVisibility(View.GONE);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            }
            //holder.isComMsg = isComMsg;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(isComMsg){
            holder.tv_username.setText(msg.getName());
            holder.tv_content.setText(msg.getText());
        }else {
            holder.tv_content.setText(msg.getText());
        }


        return convertView;
    }

    public static class ViewHolder {
        //public ProgressBar pb;
        public TextView tv_username;
        public TextView tv_content;
        //public boolean isComMsg = true;
    }


}