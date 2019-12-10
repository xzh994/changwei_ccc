package com.example.lwxg.changweistory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

import okhttp3.FormBody;

public class EnterActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private EditText enter_name;
    private EditText enter_pwd;
    private Button enter_dl;
    private Button enter_zc;
    private TimeData data;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        data = new TimeData(context);
        initView();


    }

    private void initView() {
        enter_name = findViewById(R.id.enter_name);
        enter_pwd = findViewById(R.id.enter_pwd);
        enter_dl = findViewById(R.id.enter_dl);

        enter_dl.setOnClickListener(this);
        enter_zc = findViewById(R.id.enter_zc);
        enter_zc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_dl:
                submit();
                break;
            case R.id.enter_zc:
                startActivity(new Intent(EnterActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }

    private void submit() {
        // validate
        String name = enter_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = enter_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=userLogin&name=" + name + "&email=" + pwd;
        Log.i("enterActivity", url2 + "");
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                        String id = jsonObject.getString("msg");
                        data.addId(id);
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
