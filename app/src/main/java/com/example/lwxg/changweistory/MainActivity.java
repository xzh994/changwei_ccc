package com.example.lwxg.changweistory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwxg.changweistory.Receiver.ShowNotificationReceiver;
import com.example.lwxg.changweistory.activity.EnterActivity;
import com.example.lwxg.changweistory.activity.HpActivity;
import com.example.lwxg.changweistory.activity.InfoActivity;
import com.example.lwxg.changweistory.activity.MessageBoardActivity;
import com.example.lwxg.changweistory.adapter.MessageAdapter;
import com.example.lwxg.changweistory.adapter.PHBListAdapter;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.model.Messages;
import com.example.lwxg.changweistory.model.User;
import com.example.lwxg.changweistory.util.NetTool;
import com.example.lwxg.changweistory.util.ZXingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private TimeData timeData;

    private MessageAdapter messageAdapter;
    private List<Messages> messages;
    private List<Messages> zs_ms;
    private Timer timer = new Timer();
    private Bitmap erWeiMa;


    private NavigationView nav;
    private DrawerLayout activity_na;
    private ImageView main_menu;
    private ListView main_tuijian;
    private TextView main_tjsx;
    private TextView main_name;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
            } else if (msg.what == 100) {
                phbListAdapteradapter.notifyDataSetChanged();
            } else if (msg.what == 99) {
                messageAdapter.notifyDataSetChanged();
            } else {
                Intent intent = new Intent();
                ShowNotificationReceiver s = new ShowNotificationReceiver();
                s.onReceive(context, intent);
            }

        }
    };

    private void initData() {
        messages.clear();
        String url = context.getString(R.string.url) + "Cw/BlogServlet?action=selectBlog_list";
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
                    shuaXinShouYe();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void shuaXinShouYe() {
        Collections.shuffle(messages);
        zs_ms.clear();
        for (int i = 0; i < Math.min(5, messages.size()); i++) {
            zs_ms.add(messages.get(i));
        }
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.i("cw_ccc", getAppVersionName(context)+"");
        timeData = new TimeData(context);
        messages = new ArrayList<>();
        initData();
        initErWeiMa();
        initView();
        initUser();
        gongGao();
        jcUpdate();


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

    private void initErWeiMa() {
        String url = context.getString(R.string.url) + "Cw/UserServlet?action=about";
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String msg = jsonObject.getString("msg");
                        erWeiMa = ZXingUtils.createQRCode(msg, 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.changwei));
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

    //公告
    private void gongGao() {
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selVersion";
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
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selVersion";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        double msg = Double.parseDouble(jsonObject.getString("msg"));
                        if (msg > Double.parseDouble(getAppVersionName(context))) {
                            remindUpdate(msg + "");
                        } else {
//                            Toast.makeText(context, "已经是最新版本了！当前版本：" + msg, Toast.LENGTH_LONG).show();
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

    private void remindUpdate(final String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.new_version, null);
        show = builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.url) + "cw_ccc_official" + msg + ".apk")
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setView(v).setTitle("有新版本可以使用了").create();
        show.show();
    }

    private void initUser() {
        Log.i("cw_ccc", timeData.selectId());
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selAll&id=" + timeData.selectId();
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
            case R.id.main_tjsx://点击菜单，跳出侧滑菜单
                shuaXinShouYe();
                Toast.makeText(context, "推荐内容已刷新！", Toast.LENGTH_SHORT).show();
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
        phbList = new ArrayList<User>();
        zs_ms = new ArrayList<>();

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
        messageAdapter = new MessageAdapter(context, zs_ms);
        activity_na = (DrawerLayout) findViewById(R.id.activity_na);
        activity_na.setOnClickListener(this);
        main_name = (TextView) findViewById(R.id.main_name);
        main_name.setOnClickListener(this);
        main_tjsx = findViewById(R.id.main_tjsx);
        main_tjsx.setOnClickListener(this);

        main_tuijian = (ListView) findViewById(R.id.main_tuijian);
        main_tuijian.addFooterView(View.inflate(context, R.layout.ms_list_foot, null));
        main_tuijian.setAdapter(messageAdapter);
        main_tuijian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("id", messages.get(position).getId());
                intent.putExtra("title", messages.get(position).getTilte());
                intent.putExtra("content", messages.get(position).getContent());
                intent.putExtra("time", messages.get(position).getTime());
                intent.putExtra("type", messages.get(position).getType());
                intent.putExtra("username", messages.get(position).getUser());
                startActivity(intent);
            }
        });
    }

    private void setTab(MenuItem item) {
        switch (item.getTitle().toString()) {
            default:
                break;
            case "签到打卡":
                daKa();
                break;
            case "等级排行榜":
                phb();
                break;
            case "检查更新":
                jcUpdate();
                break;
            case "返回登录":
                startActivity(new Intent(context, EnterActivity.class));
                finish();
                break;
            case "说话":
                startActivity(new Intent(context, MessageBoardActivity.class));
                break;
            case "关于":
//                Toast.makeText(context, "11", Toast.LENGTH_SHORT).show();
                erweima();
                break;
        }
    }

    private List<User> phbList;
    private PHBListAdapter phbListAdapteradapter = null;

    //排行榜
    private void phb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.cc_phb, null);
        final ListView phb_list = v.findViewById(R.id.phb_list);
        ImageView cc_phb_refresh = v.findViewById(R.id.cc_phb_refresh);
        cc_phb_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "已刷新！", Toast.LENGTH_SHORT).show();
                phbListAdapteradapter.notifyDataSetChanged();
            }
        });
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selTop";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, new NetTool.NetBack() {
            @Override
            public void onBack(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "如果没数据请再试一次", Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = (JSONArray) jsonObject.get("list");
                        phbList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject one = (JSONObject) jsonArray.get(i);
                            String name = one.getString("name");
                            int day = one.getInt("day");
                            User user = new User(name, day);
                            phbList.add(user);
                        }
                        phbListAdapteradapter = new PHBListAdapter(context, phbList);
                        phb_list.setAdapter(phbListAdapteradapter);
                        phbListAdapteradapter.notifyDataSetChanged();
                        handler.sendEmptyMessageDelayed(100, 500);
                    } else {
                        Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        show = builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setView(v).create();

        show.show();
    }

    private void daKa() {
        SimpleDateFormat daka_time = new SimpleDateFormat("yyyy-MM-dd");
        String d_t = daka_time.format(new Date());
        Log.i("cw_ccc", d_t);
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=addRecord&id=" + timeData.selectId() + "&time=" + d_t;
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
        ImageView erweima_img = v.findViewById(R.id.erweima_img);
        erweima_img.setImageBitmap(erWeiMa);
        show = builder.setView(v).setPositiveButton("取消", null).setTitle("关于").create();
        show.show();
    }

    private AlertDialog show;

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
