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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.adapter.MessageAdapter;
import com.example.lwxg.changweistory.model.Messages;
import com.example.lwxg.changweistory.util.NetTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class MessageBoardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Context context;
    private List<Messages> messages;
    private ListView home_list;
    private TextView home_meun_my;
    private MessageAdapter messageAdapter;
    private List<Messages> someMessages;
    private EditText home_et_search;
    private Button home_bt_search;
    private Button mb_back;
    private MessageAdapter messageAdapter2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
    private Button home_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        initData();
    }

    private void initData() {
        messages.clear();
        String url = "http://116.62.110.51:8080/Cw/BlogServlet?action=selectBlog_list";
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
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
                        messages.add(new Messages(id, username, title, content, create_time, type));
                    }
                    messageAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        context = this;
        messages = new ArrayList<Messages>();
        home_list = (ListView) findViewById(R.id.home_list);
        home_list.setOnItemClickListener(this);
        someMessages = new ArrayList<Messages>();
        home_et_search = (EditText) findViewById(R.id.home_et_search);
        home_bt_search = (Button) findViewById(R.id.home_bt_search);
        home_bt_search.setOnClickListener(this);
        mb_back = (Button) findViewById(R.id.mb_back);
        mb_back.setOnClickListener(this);
        home_meun_my = (TextView) findViewById(R.id.home_meun_my);
        home_meun_my.setOnClickListener(this);
        messageAdapter = new MessageAdapter(context, messages);
        messageAdapter2 = new MessageAdapter(context, someMessages);
        home_list.setAdapter(messageAdapter);
        home_add = (Button) findViewById(R.id.home_add);
        home_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mb_back:
                finish();
                break;
            case R.id.home_bt_search:
                Toast.makeText(context, "查询", Toast.LENGTH_SHORT).show();
                submit();
                break;
            case R.id.home_meun_my:
                startActivity(new Intent(context, MyMessgaesActivity.class));
                finish();
                break;
            case R.id.home_add:
                startActivity(new Intent(context, MessageReleaseActivity.class));
                finish();
                break;
        }
    }

    private void getSomeList(String search) {
        someMessages.clear();
        String url = "http://116.62.110.51:8080/Cw/BlogServlet?action=selectBlog&title=" + search;
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    Log.i("json2", json + "");
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray array = (JSONArray) jsonObject.get("list");
                    Log.i("json2", array + "");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject list = array.getJSONObject(i);
                        String id = list.getString("id");
                        String title = list.getString("title");
                        String content = list.getString("content");
                        String create_time = list.getString("create_time");
                        String type = list.getString("type");
                        JSONObject author = (JSONObject) list.get("author");
                        String username = author.getString("username");
                        someMessages.add(new Messages(id, username, title, content, create_time, type));
                    }
                    home_list.setAdapter(messageAdapter2);
                    messageAdapter2.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void submit() {
        // validate
        String search = home_et_search.getText().toString().trim();
        if (TextUtils.isEmpty(search)) {
            Toast.makeText(this, "搜索词不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        getSomeList(search);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("title", messages.get(position).getTilte());
        intent.putExtra("content", messages.get(position).getContent());
        intent.putExtra("time", messages.get(position).getTime());
        intent.putExtra("type", messages.get(position).getType());
        intent.putExtra("username", messages.get(position).getUser());
        startActivity(intent);
    }
}
