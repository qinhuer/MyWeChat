package com.example.yujihu.wechat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yujihu.wechat.R;
import com.example.yujihu.wechat.fragment.Fragment_Dicover;
import com.example.yujihu.wechat.fragment.Fragment_Friends;
import com.example.yujihu.wechat.fragment.Fragment_Msg;
import com.example.yujihu.wechat.fragment.Fragment_Profile;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private TextView txt_title;
    private ImageView img_right;
    public static String user_name;

    protected static final String TAG = "MainActivity";
    //private TitlePopup titlePopup;
    private TextView unreaMsgdLabel;// 未读消息textview
    private TextView unreadAddressLable;// 未读通讯录textview
    private TextView unreadFindLable;// 发现
    private Fragment[] fragments;
    public Fragment_Msg homefragment;
    private Fragment_Friends contactlistfragment;
    private Fragment_Dicover findfragment;
    private Fragment_Profile profilefragment;
    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private String connectMsg = "";
    private int index;
    private int currentTabIndex;// 当前fragment的index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_name = getIntent().getStringExtra("username");
        findViewById();
        initViews();
        initTabView();
    }

    private void initTabView() {
        unreaMsgdLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        unreadFindLable = (TextView) findViewById(R.id.unread_find_number);
        homefragment = new Fragment_Msg();
        contactlistfragment = new Fragment_Friends();
        findfragment = new Fragment_Dicover();
        profilefragment = new Fragment_Profile();
        fragments = new Fragment[] { homefragment, contactlistfragment,
                findfragment, profilefragment };
        imagebuttons = new ImageView[4];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);
        imagebuttons[3] = (ImageView) findViewById(R.id.ib_profile);

        imagebuttons[0].setSelected(true);
        textviews = new TextView[4];
        textviews[0] = (TextView) findViewById(R.id.tv_weixin);
        textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
        textviews[2] = (TextView) findViewById(R.id.tv_find);
        textviews[3] = (TextView) findViewById(R.id.tv_profile);
        textviews[0].setTextColor(0xFF45C01A);
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homefragment)
                .add(R.id.fragment_container, contactlistfragment)
                .add(R.id.fragment_container, profilefragment)
                .add(R.id.fragment_container, findfragment)
                .hide(contactlistfragment).hide(profilefragment)
                .hide(findfragment).show(homefragment).commit();
        updateUnreadLabel();
    }

    private void findViewById() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_right = (ImageView) findViewById(R.id.img_right);
    }

    public void onTabClicked(View view) {
        img_right.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.re_weixin:
                img_right.setVisibility(View.VISIBLE);
                index = 0;
                if (homefragment != null) {
                    homefragment.refresh();
                }
                txt_title.setText(R.string.app_name);
                img_right.setImageResource(R.drawable.icon_add);
                break;
            case R.id.re_contact_list:
                index = 1;
                txt_title.setText(R.string.contacts);
                img_right.setVisibility(View.VISIBLE);
                img_right.setImageResource(R.drawable.icon_titleaddfriend);
                break;
            case R.id.re_find:
                index = 2;
                txt_title.setText(R.string.discover);
                break;
            case R.id.re_profile:
                index = 3;
                txt_title.setText(R.string.me);
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        imagebuttons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF999999);
        textviews[index].setTextColor(0xFF45C01A);
        currentTabIndex = index;
    }

    private void initViews() {
        // 设置消息页面为初始页面
        img_right.setVisibility(View.VISIBLE);
        img_right.setImageResource(R.drawable.icon_add);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取未读消息数
     */
    public void updateUnreadLabel() {

    }
}
