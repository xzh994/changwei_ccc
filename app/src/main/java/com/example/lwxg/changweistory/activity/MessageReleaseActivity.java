package com.example.lwxg.changweistory.activity;

import android.annotation.SuppressLint;
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


import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.util.NetTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;

public class MessageReleaseActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private TimeData data = new TimeData(MessageReleaseActivity.this);
    private EditText release_et_title;
    private EditText release_type;
    private EditText release_et_content;
    private Button release_save;
    private Button release_back;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        initView();
    }

    private void initView() {
        release_et_title = findViewById(R.id.release_et_title);
        release_type = findViewById(R.id.release_type);
        release_et_content = findViewById(R.id.release_et_content);
        release_save = findViewById(R.id.release_save);
        release_back = findViewById(R.id.release_back);

        release_save.setOnClickListener(this);
        release_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.release_save:
                submit();
                break;
            case R.id.release_back:
                finish();

                break;
        }
    }


    private void submit() {
        // validate
        String title = release_et_title.getText().toString().trim();
        Log.i("fatie", title + "");
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "title不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String content = release_et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "content不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        saveBlog(title, content);
    }

    private void saveBlog(String title, String content) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        String id = data.selectId();
        String type = release_type.getText().toString();
        if (TextUtils.isEmpty(type)) {
            Toast.makeText(this, "类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = context.getString(R.string.url) + "Cw/BlogServlet?action=insertBlog_anzhuo&id=" + id + "&title=" + title + "&content=" + content + "&time=" + time + "&type=" + type;
        //id title content  create_time typeW
        FormBody body = new FormBody.Builder().build();
//        FormBody body = new FormBody.Builder().add("id", id).add("title", title).add("content", content).add("time", time).add("type", type).build();
        NetTool.netPost(handler, url, body, (json) -> {
            try {
                Log.i("json", json + "");
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (!status.equals("error")) {
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MessageBoardActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }
}
