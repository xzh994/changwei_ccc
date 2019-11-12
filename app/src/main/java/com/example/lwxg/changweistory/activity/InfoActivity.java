package com.example.lwxg.changweistory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.adapter.ReplyAdapter;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.model.Messages;
import com.example.lwxg.changweistory.model.Reply;
import com.example.lwxg.changweistory.util.NetTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;


public class InfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TimeData data = new TimeData(InfoActivity.this);
    private Context context = this;
    private Button info_back;
    private ListView info_list;
    private ReplyAdapter replyAdapter;
    private List<Reply> replies;
    private String blog_id;
    private Intent intent;
    private EditText reply_list_foot_content;
    private Button reply_list_foot_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        intent = getIntent();
        initView();
        initData();

    }

    private void initView() {
        blog_id = intent.getStringExtra("id");
        Log.i("infoActivity", "--blog_id:" + blog_id);

        info_back = (Button) findViewById(R.id.info_back);
        info_list = findViewById(R.id.info_list);
        reply_list_foot_content = findViewById(R.id.reply_list_foot_content);
        reply_list_foot_ok = findViewById(R.id.reply_list_foot_ok);
        replies = new ArrayList<>();
        replyAdapter = new ReplyAdapter(context, replies);

        info_list.addHeaderView(initListHeader());
        info_list.setAdapter(replyAdapter);

        reply_list_foot_ok.setOnClickListener(this);
        info_back.setOnClickListener(this);
    }

    private View initListHeader() {
        View listHeader = View.inflate(context, R.layout.info_list_header, null);
        TextView info_title = listHeader.findViewById(R.id.info_title);
        TextView info_name = listHeader.findViewById(R.id.info_name);
        TextView info_time = listHeader.findViewById(R.id.info_time);
        TextView info_content = listHeader.findViewById(R.id.info_content);
        info_title.setText(intent.getStringExtra("title"));
        info_name.setText(intent.getStringExtra("username"));
        info_time.setText(intent.getStringExtra("time"));
        info_content.setText("正文：" + intent.getStringExtra("content"));
        //在textView控件添加链接
//        info_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        return listHeader;
    }

    private void initData() {
        replies.clear();
        String url = context.getString(R.string.url) + "Cw/BlogServlet?action=selectReply&id=" + blog_id;
        Log.i("URL", "url:" + url);
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    Log.i("json", json + "1");
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray array = (JSONArray) jsonObject.get("list");
                    Log.i("json", array + "3");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject list = array.getJSONObject(i);
                        String id = list.getString("id");
                        String time = list.getString("time");
                        String content = list.getString("content");
                        String name = list.getString("reply_user");
                        String day = list.getString("user_day");
                        replies.add(new Reply(Integer.parseInt(id), content, time, name, day));
                    }
                    replyAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_back:
                finish();
                break;
            case R.id.reply_list_foot_ok:
                insertReply();
                break;
        }
    }

    private void insertReply() {

        String user_id = data.selectId();
        String content = reply_list_foot_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = context.getString(R.string.url) + "Cw/BlogServlet?action=insertReply&user_id=" + user_id + "&blog_id=" + blog_id + "&content=" + content;
        //id title content  create_time type
        FormBody body = new FormBody.Builder().build();
//        FormBody body = new FormBody.Builder().add("id", id).add("title", title).add("content", content).add("time", time).add("type", type).build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    Log.i("json", json + "");
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (!status.equals("error")) {
                        Toast.makeText(context, "回复成功", Toast.LENGTH_SHORT).show();
                        reply_list_foot_content.setText("");
                        initData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                replyAdapter.notifyDataSetChanged();
            }
        }
    };
}
