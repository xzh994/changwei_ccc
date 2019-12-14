package com.example.lwxg.changweistory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.adapter.ReplyAdapter;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.model.Reply;
import com.example.lwxg.changweistory.util.NetTool;
import com.example.lwxg.changweistory.util.ToastUtils;
import com.example.lwxg.changweistory.util.Util;
import com.example.lwxg.changweistory.weight.ReplyFootView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
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
    private ReplyFootView reply_foot;
    private EditText reply_foot_view_content;
    private Button reply_foot_view_confirm;

    public static final int LIST_SKIP_TOP = 0;
    public static final int LIST_SKIP_BUTTON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        intent = getIntent();
        initView();
        initData(LIST_SKIP_TOP);

    }

    private void initView() {
        blog_id = intent.getStringExtra("id");
        Log.i("infoActivity", "--blog_id:" + blog_id);

        info_back = findViewById(R.id.info_back);
        info_list = findViewById(R.id.info_list);
        reply_foot = findViewById(R.id.reply_foot);
        reply_foot_view_content = reply_foot.getEtContent();
        reply_foot_view_confirm = reply_foot.getBtConfirm();
        replies = new ArrayList<>();
        replyAdapter = new ReplyAdapter(context, replies);

        info_list.addHeaderView(initListHeader());
        info_list.setAdapter(replyAdapter);

        reply_foot_view_confirm.setOnClickListener(this);
        info_back.setOnClickListener(this);
    }

    private View initListHeader() {
        View listHeader = View.inflate(context, R.layout.header_info_list, null);
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

    private void initData(int listAction) {
        replies.clear();
        String url = context.getString(R.string.url) + "Cw/BlogServlet?action=selectReply&id=" + blog_id;
        Log.i("URL", "url:" + url);
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, (String json) -> {
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
                    String headImage = list.getString("user_head_image");
                    replies.add(new Reply(Integer.parseInt(id), content, time, name, day, headImage));
                }
                replyAdapter.notifyDataSetChanged();
                if (listAction == LIST_SKIP_BUTTON) {
                    info_list.setSelection(replies.size() - 1);
                    info_list.smoothScrollToPosition(replies.size() - 1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_back:
                finish();
                break;
            case R.id.reply_foot_view_confirm:
                insertReply();
                break;
        }
    }

    private void insertReply() {

        String user_id = data.selectId();
        String content = reply_foot_view_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = context.getString(R.string.url) + "Cw/BlogServlet?action=insertReply&user_id=" + user_id + "&blog_id=" + blog_id + "&content=" + content;
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, (String json) -> {
            try {
                Log.i("json", json + "");
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (!status.equals("error")) {
                    Toast.makeText(context, "回复成功", Toast.LENGTH_SHORT).show();
                    reply_foot_view_content.setText("");
                    Util.hindKeyboard(getCurrentFocus(), this);
                    initData(LIST_SKIP_BUTTON);
                } else {
                    ToastUtils.INSTANCE.showToast(context, "发送失败！");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtils.INSTANCE.showToast(context, "发送失败！");
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

    // 点击旁白隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            Util.shouldHideKeyboard(ev, getCurrentFocus(), this);
        return super.dispatchTouchEvent(ev);
    }

}
