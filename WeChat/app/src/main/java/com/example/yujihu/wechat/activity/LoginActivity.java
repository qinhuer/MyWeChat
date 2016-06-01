package com.example.yujihu.wechat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.yujihu.wechat.R;
import com.example.yujihu.wechat.entity.Response;
import com.example.yujihu.wechat.http.HttpClient;
import com.example.yujihu.wechat.http.HttpResponseHandler;

import java.io.IOException;

import okhttp3.Request;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private Button bt_login;
    private Button bt_register;
    public static String user_name;
    private ProgressDialog dialog;
    private int success;
    private String message;
    private Response response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initAcion();
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.btn_login);
        bt_register = (Button) findViewById(R.id.btn_register);
    }

    private void initAcion() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_username.getText().toString().trim().equals("") || et_password.getText().toString().trim().equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入账号密码", Toast.LENGTH_LONG).show();
                }else {
                    new Login().execute();
                }
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_username.getText().toString().trim().equals("") || et_password.getText().toString().trim().equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入账号密码", Toast.LENGTH_LONG).show();
                }else {
                    new Register().execute();
                }
            }
        });
    }

    /**
     * 后台异步注册
     */
    private class Register extends AsyncTask<String, String, String> {

        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = et_username.getText().toString().trim();
            password = et_password.getText().toString().trim();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("正在注册...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            String str = "" + success;
            Toast.makeText(LoginActivity.this, "success：" + str + "  message:" + message, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
                try {
                    HttpClient.postRegister(username, password, new HttpResponseHandler() {

                        @Override
                        public void onSuccess(String content) {
                            response = JSONArray.parseObject(content, Response.class);
                            message = response.getMessage();
                            success = response.getSuccess();

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
     * 后台异步登录
     */
    private class Login extends AsyncTask<String, String, String> {

        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = et_username.getText().toString().trim();
            password = et_password.getText().toString().trim();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("正在登录...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            String str = "" + success;
            Toast.makeText(getBaseContext(), "success：" + str + "  message:" + message, Toast.LENGTH_LONG).show();

            if (success == 1) {
                user_name = username;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
                try {
                    HttpClient.postLogin(username, password, new HttpResponseHandler() {

                        @Override
                        public void onSuccess(String content) {
                            response = JSONArray.parseObject(content, Response.class);
                            message = response.getMessage();
                            success = response.getSuccess();

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
}
