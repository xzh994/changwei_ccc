package com.example.lwxg.changweistory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwxg.changweistory.Receiver.ShowNotificationReceiver;
import com.example.lwxg.changweistory.activity.HpActivity;
import com.example.lwxg.changweistory.data.TimeData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context = this;
    private ImageView main_menu;
    private NavigationView nav;
    private DrawerLayout activity_na;
    private TextView main_sctime;
    TimeData timeData;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String daka = simpleDateFormat.format(new Date());
                Date d1 = null, d2 = null;
                try {
                    d1 = simpleDateFormat.parse(daka);
                    d2 = simpleDateFormat.parse(timeData.selectTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long times = d1.getTime() - d2.getTime();
                main_sctime.setText("距离上次手冲已有  " + times / 1000 + "  秒");
            } else {
                Intent intent = new Intent();
                ShowNotificationReceiver s = new ShowNotificationReceiver();
                s.onReceive(context, intent);
            }

        }
    };
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeData = new TimeData(context);
        initView();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String t = timeData.selectTime();
                if (t != null && !t.equals("")) {
                    handler.sendEmptyMessage(0);
                }
            }
        }, 0, 1000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu://点击菜单，跳出侧滑菜单
                show();
                break;
        }
    }

    private void show() {
        if (activity_na.isDrawerOpen(nav)) {
            activity_na.closeDrawer(nav);
        } else {
            activity_na.openDrawer(nav);
        }
    }

    private void initView() {
        main_menu = (ImageView) findViewById(R.id.main_menu);
        main_menu.setOnClickListener(this);
        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);
//                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                show();
                setTab(item);
                return true;
            }
        });
        activity_na = (DrawerLayout) findViewById(R.id.activity_na);
        activity_na.setOnClickListener(this);
        main_sctime = (TextView) findViewById(R.id.main_sctime);
        main_sctime.setOnClickListener(this);
    }

    private void setTab(MenuItem item) {
        switch (item.getTitle().toString()) {
            default:
                break;
            case "手冲打卡":
                Toast.makeText(MainActivity.this, "打卡成功！", Toast.LENGTH_SHORT).show();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String daka = simpleDateFormat.format(new Date());
                timeData.add(daka);
                int time = (int) Math.random() * 10000 + 5000;
                handler.sendEmptyMessageDelayed(1, time);
                break;
            case "手冲计时":
                Toast.makeText(context, "功能还未开放。。。", Toast.LENGTH_SHORT).show();
                break;
            //......
            case "手冲小助手":
                yanZheng();
                break;
            case "关于":
                Toast.makeText(context, "国标米兰我马玄黄", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    AlertDialog show;

    private void yanZheng() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.yanzheng_hp, null);
        final EditText yz_yz = v.findViewById(R.id.yz_yz);
        Button yz_bt = v.findViewById(R.id.yz_bt);

        yz_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(yz_yz.getText())) {
                    Toast.makeText(context, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String yzm = yz_yz.getText() + "";

                if (yzm.equals("国标米兰")) {
                    startActivity(new Intent(context, HpActivity.class));
                } else {
                    Toast.makeText(context, "卧底gck！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        show = builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setView(v).setTitle("验证").create();

        show.show();

    }
}
