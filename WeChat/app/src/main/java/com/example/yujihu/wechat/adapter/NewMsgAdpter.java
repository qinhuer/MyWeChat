package com.example.yujihu.wechat.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.yujihu.wechat.R;
import com.example.yujihu.wechat.common.ViewHolder;
import com.example.yujihu.wechat.entity.Conversation;


public class NewMsgAdpter extends BaseAdapter {
    protected Context context;
    private List<Conversation> conversationList;

    public NewMsgAdpter(Context ctx, List<Conversation> objects) {
        context = ctx;
        conversationList = objects;
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_item_msg, parent, false);
        }
        ImageView img_avar = ViewHolder.get(convertView,
                R.id.contactitem_avatar_iv);
        TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
        TextView txt_state = ViewHolder.get(convertView, R.id.txt_state);
        TextView txt_content = ViewHolder.get(convertView, R.id.txt_content);
        TextView txt_time = ViewHolder.get(convertView, R.id.txt_time);
       // TextView unreadLabel = ViewHolder.get(convertView, R.id.unread_msg_number);


        // 获取与此用户/群组的会话
        final Conversation conversation = conversationList.get(position);


        if (conversation != null) {
            txt_name.setText(conversation.getUserName());
        } else {
            txt_name.setText("好友");
        }
        if (conversation.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            txt_content.setText(conversation.getContent());
            txt_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if (conversation.isConversationState()) {
                txt_state.setText("已读");
                //txt_state.setBackgroundResource(R.drawable.btn_bg_orgen);
            }
        } else {
            txt_state.setText("未读");
            txt_state.setBackgroundResource(R.drawable.btn_bg_blue);
        }

    return convertView;
}
}
