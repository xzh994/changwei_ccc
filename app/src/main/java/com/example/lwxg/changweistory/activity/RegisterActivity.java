package com.example.lwxg.changweistory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lwxg.changweistory.MainActivity;
import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.util.NetTool;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private EditText register_name;
    private EditText register_email;
    private Button register_email_yz;
    private EditText register_email_ma;
    private TimeData timeData;
    private Button register_zc;
    private String mes = "000000";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        timeData = new TimeData(context);
        initView();


    }

    private void initView() {
        register_name = findViewById(R.id.register_name);
        register_email = findViewById(R.id.register_email);
        register_email_yz = findViewById(R.id.register_email_yz);
        register_email_ma = findViewById(R.id.register_email_ma);
        register_zc = findViewById(R.id.register_zc);

        register_email_yz.setOnClickListener(this);
        register_zc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_email_yz:
                submit_yz();
                break;
            case R.id.register_zc:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String name = register_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = register_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String ma = register_email_ma.getText().toString().trim();
        if (TextUtils.isEmpty(ma)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() > 7 || name.length() < 2) {
            Toast.makeText(this, "用户名过长或过短", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ma.equals(mes)) {
            Toast.makeText(this, "验证码错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = context.getString(R.string.url) + "Cw/UserServlet?action=selName&name=" + name;
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (!status.equals("success")) {
                        Toast.makeText(context, "用户名已注册", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=register&name=" + name + "&email=" + email;
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                        String id = jsonObject.getString("msg");
                        timeData.addId(id);
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void submit_yz() {
        String email = register_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = context.getString(R.string.url) + "Cw/UserServlet?action=selEmail&email=" + email;
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("error")) {
                        Toast.makeText(context, "邮箱已注册", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=sendEmail&email=" + email;
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "验证码发送成功，请查看邮箱，切勿重复点击,三十秒未收到请检查邮箱再发送", Toast.LENGTH_LONG).show();
                        mes = jsonObject.getString("msg");
                    } else {
                        Toast.makeText(context, "验证码发送失败，邮箱已注册或邮箱不正确", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
