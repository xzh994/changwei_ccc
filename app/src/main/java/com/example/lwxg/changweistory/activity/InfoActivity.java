package com.example.lwxg.changweistory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lwxg.changweistory.R;


public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button info_back;
    private TextView info_title;
    private TextView info_name;
    private TextView info_time;
    private TextView info_type;
    private TextView info_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView();

        Intent intent = getIntent();
        info_title.setText(intent.getStringExtra("title"));
        info_name.setText(intent.getStringExtra("name"));
        info_time.setText(intent.getStringExtra("time"));
        info_type.setText(intent.getStringExtra("type"));
        info_content.setText("  "+intent.getStringExtra("content"));
    }

    private void initView() {
        info_back = (Button) findViewById(R.id.info_back);
        info_title = (TextView) findViewById(R.id.info_title);
        info_name = (TextView) findViewById(R.id.info_name);
        info_time = (TextView) findViewById(R.id.info_time);
        info_type = (TextView) findViewById(R.id.info_type);
        info_content = (TextView) findViewById(R.id.info_content);

        info_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_back:
                finish();
                break;
        }
    }
}
