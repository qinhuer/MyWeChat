package com.example.yujihu.wechat.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.yujihu.wechat.R;
import com.example.yujihu.wechat.adapter.MessageAdapter;
import com.example.yujihu.wechat.entity.ChatMessage;
import com.example.yujihu.wechat.entity.MsgResponse;
import com.example.yujihu.wechat.entity.Response;
import com.example.yujihu.wechat.http.HttpClient;
import com.example.yujihu.wechat.http.HttpResponseHandler;

import okhttp3.Request;

//聊天页面
public class ChatActivity extends Activity {

    private Button buttonSend;
    private ListView listView;
    private Button btnMore;
    private EditText mEditTextContent;
    private MsgResponse response;
    private Response response2;
    private int success;
    private int success2;
    private String message;
    private String far_user_name;
    private String send_content;
    private List<ChatMessage> msgs = new ArrayList<>();
    private String tempMessage = "";

    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;

    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;

    private View buttonPressToSpeak;
    // private ViewPager expressionViewpager;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;
    // private ImageView locationImgview;
    private View more;
    private int position;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;
    private int chatType;
    public static ChatActivity activityInstance = null;
    // 给谁发送消息
    private String Name;
    private String toChatUsername;
    private MessageAdapter adapter;
    private File cameraFile;
    static int resendPos;

    private TextView txt_title;
    private ImageView iv_emoticons_normal, img_right;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    private ProgressBar loadmorePB;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;

    public String playMsgId;
    private AnimationDrawable animationDrawable;
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
        }
    };

    // private EMGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        setUpView();
        setListener();

        new Thread(myRun).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * initView
     */
    protected void initView() {
        recordingContainer = findViewById(R.id.view_talk);
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_right = (ImageView) findViewById(R.id.img_right);
        micImage = (ImageView) findViewById(R.id.mic_image);
        animationDrawable = (AnimationDrawable) micImage.getBackground();
        animationDrawable.setOneShot(false);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);//消息输入
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = (Button) findViewById(R.id.btn_send);//发送按钮
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        // locationImgview = (ImageView) findViewById(R.id.btn_location);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        more = findViewById(R.id.more);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

        edittext_layout.requestFocus();
        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout
                            .setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout
                            .setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edittext_layout
                        .setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setUpView() {
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ChatMessage msg = new ChatMessage();
        msg.setComMeg(true);
        msg.setName("沁浒");
        msg.setText("你好啊！");
        msgs.add(msg);
        adapter = new MessageAdapter(this, msgs);
        // 显示消息
        listView.setAdapter(adapter);
        //listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count);
        }

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

    }

    //发送消息
    private void sendMsg() {
        String contString = mEditTextContent.getText().toString().trim();
        if (contString.length() > 0) {
            send_content = contString;
            ChatMessage entity = new ChatMessage();
            entity.setName(MainActivity.user_name);
            entity.setText(send_content);
            entity.setComMeg(false);

            msgs.add(entity);
            adapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            listView.setSelection(listView.getCount() - 1);

            new SendMsg().execute(contString);
        }
    }

    //接收消息
    private void getMsg() {
        new GetMsg().execute();
    }

    protected void setListener() {
        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                sendMsg();
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 后台接收消息
     */
    private class GetMsg extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String str = "" + success;
            Toast.makeText(ChatActivity.this, "username:" + far_user_name + "    success：" + str + "  message:" + message, Toast.LENGTH_LONG).show();
            if (success == 1 && tempMessage.equals(message) == false) {
                tempMessage = message;
                String contString = message;
                if (contString.length() > 1) {
                    ChatMessage entity = new ChatMessage();
                    entity.setName(far_user_name);
                    entity.setText(message);
                    entity.setComMeg(true);

                    msgs.add(entity);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(listView.getCount() - 1);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient.getMsg(MainActivity.user_name, new HttpResponseHandler() {

                    @Override
                    public void onSuccess(String content) {
                        response = JSONArray.parseObject(content, MsgResponse.class);
                        success = response.getSuccess();
                        if (success == 1) {
                            far_user_name = response.getUsername();
                        } else {
                            far_user_name = "";
                        }
                        message = response.getMessage();


                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d("register", "注册失败");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 后台发送消息
     */
    private class SendMsg extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String str = "" + success2;
            Toast.makeText(ChatActivity.this, "success：" + str + "  message:" + message, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient.getSMsg(MainActivity.user_name,params[0], new HttpResponseHandler() {

                    @Override
                    public void onSuccess(String content) {
                        response2 = JSONArray.parseObject(content, Response.class);
                        message = response2.getMessage();
                        success2 = response2.getSuccess();

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d("sendmsg", "发送消息失败");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    Runnable myRun = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new GetMsg().execute();

                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
