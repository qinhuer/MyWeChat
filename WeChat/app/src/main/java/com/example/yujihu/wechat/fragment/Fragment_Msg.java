package com.example.yujihu.wechat.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yujihu.wechat.R;
import com.example.yujihu.wechat.activity.ChatActivity;
import com.example.yujihu.wechat.activity.MainActivity;
import com.example.yujihu.wechat.adapter.NewMsgAdpter;
import com.example.yujihu.wechat.entity.Conversation;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Msg extends Fragment implements OnClickListener,
        OnItemClickListener {
    private Activity ctx;
    private View layout, layout_head;
    public RelativeLayout errorItem;
    public TextView errorText;
    private ListView lvContact;
    private MainActivity parentActivity;
    private NewMsgAdpter adpter;
    private List<Conversation> conversationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {
            ctx = this.getActivity();
            parentActivity = (MainActivity) getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.fragment_msg,
                    null);
            lvContact = (ListView) layout.findViewById(R.id.listview);
            errorItem = (RelativeLayout) layout
                    .findViewById(R.id.rl_error_item);
            errorText = (TextView) errorItem
                    .findViewById(R.id.tv_connect_errormsg);
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        initViews();
    }

    private void initViews() {
        layout.findViewById(R.id.txt_nochat).setVisibility(View.GONE);
        for (int i = 0; i < 10; i++) {
            Conversation conversation = new Conversation();
            conversation.setContent("内容主体");
            conversation.setUserName("用户名");
            conversation.setConversationState(true);
            conversation.setMsgCount(3);
            conversationList.add(conversation);
        }
        adpter = new NewMsgAdpter(this.getActivity(), conversationList);
        lvContact.setAdapter(adpter);
    }

    private void setOnListener() {
        lvContact.setOnItemClickListener(this);
        errorItem.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        Toast.makeText(this.getActivity(), position + "", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this.getActivity(), ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

}
