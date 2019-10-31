package com.example.lwxg.changweistory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.UserData;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.adapter.MyMessagesAdapter;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.model.Messages;
import com.example.lwxg.changweistory.util.NetTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class MyMessgaesActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private MyMessagesAdapter myMessagesAdapter;
    private ListView myblog_list;
    private List<Messages> blogs;
    private Button myblog_back;
    private TimeData data = new TimeData(MyMessgaesActivity.this);
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        initView();
    }

    private void initView() {
        context = this;
        blogs = new ArrayList<Messages>();
        initList();
        myMessagesAdapter = new MyMessagesAdapter(context, blogs);
        myblog_list = (ListView) findViewById(R.id.myblog_list);
        myblog_list.setOnItemClickListener(this);
        myblog_back = (Button) findViewById(R.id.myblog_back);

        myblog_back.setOnClickListener(this);
        myblog_list.setAdapter(myMessagesAdapter);
    }

    private void initList() {
        blogs.clear();
        String id = data.selectId();
        Log.i("json", id);
        String url = context.getString(R.string.url)+"Cw/BlogServlet?action=selectAuhorBlog_list&id=" + id;
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    Log.i("json", json + "111");
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray array = (JSONArray) jsonObject.get("list");
                    Log.i("json", array + "");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject list = array.getJSONObject(i);
                        String id = list.getString("id");
                        String title = list.getString("title");
                        String content = list.getString("content");
                        String create_time = list.getString("create_time");
                        String type = list.getString("type");
                        JSONObject author = (JSONObject) list.get("author");
                        String username = author.getString("username");
                        blogs.add(new Messages(id, username, title, content, create_time, type));
                    }
                    myMessagesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            Log.i("json", "title:" + v.getTag());
            deleteMessages(v.getTag().toString());
        }
        switch (v.getId()) {
            case R.id.myblog_back:
                startActivity(new Intent(context, MessageBoardActivity.class));
                finish();
                break;
        }
    }

    private void deleteMessages(String title) {
        String url = context.getString(R.string.url)+"Cw/BlogServlet?action=deleteBlog_user&id=" + title;
        Log.i("json", "title:" + title);
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    Log.i("json", json + "");
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (!status.equals("error")) {
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                    initList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("title", blogs.get(position).getTilte());
        intent.putExtra("content", blogs.get(position).getContent());
        intent.putExtra("time", blogs.get(position).getTime());
        intent.putExtra("type", blogs.get(position).getType());
        intent.putExtra("username", blogs.get(position).getUser());
        startActivity(intent);
    }
}