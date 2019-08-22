package com.example.lwxg.changweistory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwxg.changweistory.Receiver.ShowNotificationReceiver;
import com.example.lwxg.changweistory.activity.ClockActivity;
import com.example.lwxg.changweistory.activity.HpActivity;
import com.example.lwxg.changweistory.activity.MessageBoardActivity;
import com.example.lwxg.changweistory.adapter.PHBListAdapter;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.model.User;
import com.example.lwxg.changweistory.util.NetTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;

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

                String ftime = f(times);

                main_sctime.setText("距离上次手冲已有  " + ftime);
            } else if (msg.what == 100) {
                adapter.notifyDataSetChanged();
            } else {
                Intent intent = new Intent();
                ShowNotificationReceiver s = new ShowNotificationReceiver();
                s.onReceive(context, intent);
            }

        }
    };

    private String f(long times) {
        times = times / 1000;
        int day = 0;
        int hour = 0;
        int minute = 0;
        if (times > 60 * 60 * 24) {
            day = (int) (times / (60 * 60 * 24));
            times = times % (60 * 60 * 24);
        }
        if (times > 60 * 60) {
            hour = (int) (times) / (60 * 60);
            times %= (60 * 60);
        }
        if (times > 60) {
            minute = (int) (times) / 60;
            times %= 60;
        }
        return day + "天" + hour + "小时" + minute + "分钟" + times + "秒";


    }

    Timer timer = new Timer();
    private TextView main_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.i("cw_ccc", getAppVersionName(context)+"");
        timeData = new TimeData(context);
        initView();
        initUser();
        gongGao();
        jcUpdate();

        //侧滑头像昵称没效果
//        View head_view = View.inflate(context, R.layout.head, null);
//        TextView head_name = head_view.findViewById(R.id.head_name);
//        head_name.setText(n + "");


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

    //公告
    private void gongGao() {
        String url2 = "http://116.62.110.51:8080/Cw/UserServlet?action=selVersion";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String list = jsonObject.getString("list");
                        diologGG(list);
                    } else {
                        Toast.makeText(context, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void diologGG(String list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.new_version, null);
        TextView main_gg = v.findViewById(R.id.main_gg);
        main_gg.setText(list + "");
        show = builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setView(v).setTitle("公告").create();
        show.show();
    }

    //检查更新
    private void jcUpdate() {
        String url2 = "http://116.62.110.51:8080/Cw/UserServlet?action=selVersion";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String msg = jsonObject.getString("msg");
                        if (!msg.equals(getAppVersionName(context))) {
                            remindUpdate();
                        } else {
                            Toast.makeText(context, "已经是最新版本了！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void remindUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.new_version, null);
        show = builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://116.62.110.51:8080/cw_ccc_official" + getAppVersionName(context) + ".apk")
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setView(v).setTitle("有新版本可以使用了").create();
        show.show();
    }

    private void initUser() {
        Log.i("cw_ccc", timeData.selectId());
        String url2 = "http://116.62.110.51:8080/Cw/UserServlet?action=selAll&id=" + timeData.selectId();
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                        JSONObject user = (JSONObject) jsonObject.get("list");
                        String u_name = user.getString("name");
                        String u_day = user.getString("day");
                        main_name.setText(u_name + "---day" + u_day);
                    } else {
                        Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        main_name = (TextView) findViewById(R.id.main_name);
        main_name.setOnClickListener(this);
    }

    private void setTab(MenuItem item) {
        switch (item.getTitle().toString()) {
            default:
                break;

            case "开始计时":
                Toast.makeText(MainActivity.this, "开始！", Toast.LENGTH_SHORT).show();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String daka = simpleDateFormat.format(new Date());
                timeData.add(daka);
                int time = (int) Math.random() * 10000 + 5000;
                handler.sendEmptyMessageDelayed(1, time);
                break;
            case "今日未冲打卡":
                daka();
                break;
            case "手冲计时":
                startActivity(new Intent(context, ClockActivity.class));
                break;
            case "冲冲排行榜":
                phb();
                break;
            case "检查更新":
                jcUpdate();
                break;
            case "肠胃说话":
                startActivity(new Intent(context, MessageBoardActivity.class));
                break;
            //......
//            case "手冲小助手":
//                yanZheng();
//                Toast.makeText(context, "小助手功能已关闭", Toast.LENGTH_SHORT).show();
//                break;
            case "关于":
                Toast.makeText(context, "qq群648445631", Toast.LENGTH_SHORT).show();
                erweima();
                break;
        }
    }

    List<User> list = new ArrayList<User>();
    PHBListAdapter adapter = null;

    //排行榜
    private void phb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.cc_phb, null);
        ListView phb_list = v.findViewById(R.id.phb_list);
//        Button phb_rfls = v.findViewById(R.id.phb_rfls);
//        phb_rfls.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
//                adapter = new PHBListAdapter(context, list);
//                handler.sendEmptyMessage(100);
//            }
//        });
        Toast.makeText(context, "如果没数据请再试一次", Toast.LENGTH_SHORT).show();
        String url2 = "http://116.62.110.51:8080/Cw/UserServlet?action=selTop";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = (JSONArray) jsonObject.get("list");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject one = (JSONObject) jsonArray.get(i);
                            String name = one.getString("name");
                            int day = one.getInt("day");
                            User user = new User(name, day);
                            list.add(user);
                        }
                        adapter = new PHBListAdapter(context, list);
                        adapter.notifyDataSetChanged();
                        handler.sendEmptyMessage(100);
                    } else {
                        Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        phb_list.setAdapter(adapter);

        show = builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setView(v).create();

        show.show();
    }

    private void daka() {
        SimpleDateFormat daka_time = new SimpleDateFormat("yyyy-MM-dd");
        String d_t = daka_time.format(new Date());
        Log.i("cw_ccc", d_t);
        String url2 = "http://116.62.110.51:8080/Cw/UserServlet?action=addRecord&id=" + timeData.selectId() + "&time=" + d_t;
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "打卡成功", Toast.LENGTH_SHORT).show();
                        initUser();
                    } else if (status.equals("error2")) {
                        Toast.makeText(context, "今天已经打卡", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("error")) {
                        Toast.makeText(context, "打卡失败（网络错误）", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void erweima() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.erweima, null);

        show = builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setView(v).setTitle("二维码").create();

        show.show();
    }

    AlertDialog show;

    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }

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
