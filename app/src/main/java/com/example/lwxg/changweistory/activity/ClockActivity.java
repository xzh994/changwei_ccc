package com.example.lwxg.changweistory.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwxg.changweistory.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import androidx.appcompat.app.AppCompatActivity;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private TextView clock_back;
    private TextView clock_time;
    private Button clock_bt;
    private Button clock_upload;
    private Timer timer;
    private boolean jishi = false;
    private SimpleDateFormat simpleDateFormat;
    private String beginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        initView();
    }

    private void initView() {
        timer = new Timer();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        clock_back = findViewById(R.id.clock_back);
        clock_time = findViewById(R.id.clock_time);
        clock_bt = findViewById(R.id.clock_bt);
        clock_upload = findViewById(R.id.clock_upload);

        clock_back.setOnClickListener(this);
        clock_bt.setOnClickListener(this);
        clock_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clock_bt:
                if (!jishi) {
                    beginTime = simpleDateFormat.format(new Date());
                    clock_time.setText("正在计时。。。");
                    clock_bt.setText("停止计时");
                    jishi = !jishi;
                } else {
                    clock_bt.setText("开始");
                    String endTime = simpleDateFormat.format(new Date());
                    Date d1 = null, d2 = null;
                    try {
                        d1 = simpleDateFormat.parse(beginTime);
                        d2 = simpleDateFormat.parse(endTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (d2 == null || d1 == null)
                        return;
                    long times = d2.getTime() - d1.getTime();
                    Log.i("aaaaa", times + "");
                    clock_time.setText(times / 1000 + "." + (times) / 10 % 100 + "秒");
                    jishi = !jishi;
                }

                break;
            case R.id.clock_upload:
                Toast.makeText(context, "功能未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clock_back:
                finish();
                break;
        }
    }
}
