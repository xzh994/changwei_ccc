package com.example.lwxg.changweistory;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lwxg.changweistory.Receiver.ShowNotificationReceiver;
import com.example.lwxg.changweistory.activity.EnterActivity;
import com.example.lwxg.changweistory.activity.HpActivity;
import com.example.lwxg.changweistory.activity.InfoActivity;
import com.example.lwxg.changweistory.activity.MessageBoardActivity;
import com.example.lwxg.changweistory.datepicker.CustomDatePicker;
import com.example.lwxg.changweistory.datepicker.DateFormatUtils;
import com.example.lwxg.changweistory.usermessage.UserActivity;
import com.example.lwxg.changweistory.adapter.MessageAdapter;
import com.example.lwxg.changweistory.adapter.RankListAdapter;
import com.example.lwxg.changweistory.data.TimeData;
import com.example.lwxg.changweistory.model.DownloadInfo;
import com.example.lwxg.changweistory.model.Messages;
import com.example.lwxg.changweistory.model.User;
import com.example.lwxg.changweistory.down.DownLoadObserver;
import com.example.lwxg.changweistory.down.DownloadManager;
import com.example.lwxg.changweistory.util.EasyDownloadUtil;
import com.example.lwxg.changweistory.util.NetTool;
import com.example.lwxg.changweistory.util.ToastUtils;
import com.example.lwxg.changweistory.util.ZXingUtils;
import com.haibin.calendarview.CalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Context context;
    private TimeData timeData;
    private MessageAdapter messageAdapter;
    private List<Messages> messages;
    private List<Messages> zs_ms;
    private Bitmap QRCode;

    private NavigationView nav;
    private DrawerLayout activity_na;
    private ImageView main_menu;
    private ListView main_tuijian;
    private TextView main_tjsx;
    private TextView main_name;

    private float thisAPKVolume = 0f;
    private float maxAPKVolume = 0f;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 2;


    private void initData() {
        messages.clear();
        String url = context.getString(R.string.url) + "Cw/BlogServlet?action=selectBlog_list";
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, (String json) -> {
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
                refreshHomePage();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void refreshHomePage() {
        Collections.shuffle(messages);
        zs_ms.clear();
        for (int i = 0; i < Math.min(5, messages.size()); i++)
            zs_ms.add(messages.get(i));
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        timeData = new TimeData(context);
        messages = new ArrayList<>();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);


        thisAPKVolume = MyApp.sharedPreferences.getFloat("thisAPKVolume", 0);
        maxAPKVolume = MyApp.sharedPreferences.getFloat("maxAPKVolume", 0);
        initData();
        initQRCode();
        initView();
        initUser();
        announcement();
        checkForUpdate();
        setDrawerLeftEdgeSize((Activity) context, activity_na, 1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor e = MyApp.sharedPreferences.edit();
        e.putFloat("thisAPKVolume", thisAPKVolume);
        e.putFloat("maxAPKVolume", maxAPKVolume);
        if (!e.commit())
            ToastUtils.INSTANCE.showToast(context, "下载进度保存失败！");
    }

    /**
     * 抽屉滑动范围控制
     *
     * @param activity
     * @param drawerLayout
     * @param displayWidthPercentage 占全屏的份额0~1
     */
    private void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)
            return;
        try {
            // find ViewDragHelper and set it accessible
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            // find edgesize and set is accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // set new edgesize
            // Point displaySize = new Point();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
            Log.e("NoSuchFieldException", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("IllegalArgument", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("IllegalAccessException", e.getMessage());
        }
    }

    private void initQRCode() {
        String url = context.getString(R.string.url) + "Cw/UserServlet?action=about";
        FormBody body = new FormBody.Builder().build();
        NetTool.netPost(handler, url, body, (String json) -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    String msg = jsonObject.getString("msg");
                    QRCode = ZXingUtils.createQRCode(msg, 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.changwei));
                } else
                    ToastUtils.INSTANCE.showToast(context, "网络错误！");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    //公告
    private void announcement() {
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selVersion";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, (String json) -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    String list = jsonObject.getString("list");
                    diologAnnouncement(list);
                } else
                    ToastUtils.INSTANCE.showToast(context, "网络错误！");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void diologAnnouncement(String list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.dialog_announcement, null);
        TextView main_gg = v.findViewById(R.id.main_gg);
        main_gg.setText(list);
        show = builder
                .setPositiveButton("取消", null)
                .setView(v)
                .setTitle("公告")
                .create();
        show.show();
    }

    //检查更新
    private void checkForUpdate() {
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selVersion";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, (String json) -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    double msg = Double.parseDouble(jsonObject.getString("msg"));
                    if (msg > Double.parseDouble(getAppVersionName(context)))
                        remindUpdate2(msg + "");
                    else
                        ToastUtils.INSTANCE.showToast(context, "已经是最新版本了！当前版本：" + msg);
                } else
                    ToastUtils.INSTANCE.showToast(context, "网络错误！");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    //跳转浏览器更新
    private void remindUpdate(final String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.dialog_new_version, null);
        show = builder
                .setNegativeButton("更新", (DialogInterface dialog, int which) -> {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(context.getString(R.string.url) + "cw_ccc_official" + msg + ".apk")
                    );
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setView(v)
                .setTitle("有新版本可以使用了")
                .create();
        show.show();
    }

    //在app内部更新
    private void remindUpdate2(final String msg) {
        final String url = context.getString(R.string.url) + "cw_ccc_official" + msg + ".apk";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.dialog_announcement, null);
        show = builder
                .setPositiveButton("更新", (DialogInterface dialog, int which) ->
                        showDownLoading(url)
                )
                .setView(v)
                .setTitle("有新版本可以使用了")
                .create();
        show.show();
    }

    private void showDownLoading(final String url) {
        View v = View.inflate(context, R.layout.dialog_new_version, null);
        final ProgressBar dialog_new_version_pb = v.findViewById(R.id.dialog_new_version_pb);
        final TextView dialog_new_version_tv = v.findViewById(R.id.dialog_new_version_tv);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        show = builder.setCancelable(false)
                .setPositiveButton("取消", null)
                .setView(v)
                .setTitle("下载中")
                .create();
        show.show();

        EasyDownloadUtil.get().download(url, Environment.getExternalStorageDirectory().getAbsolutePath(), "speak.apk", new EasyDownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                //下载完成进行相关逻辑操作
                openFile(file);
                if (show != null) {
                    show.cancel();
                    show = null;
                }
            }

            @Override
            public void onDownloading(final int progress) {
                runOnUiThread(() -> {
                    dialog_new_version_pb.setProgress(progress);
                    dialog_new_version_tv.setText(progress + "%");
                });
            }

            @Override
            public void onDownloadFailed(final Exception e) {
                //下载异常进行相关提示操作
                Log.e("MainActivity", "(MainActivity.java:357)" + e);
            }
        });
    }

    private void openFile(final File file) {
        // TODO Auto-generated method stub
        if (file != null) {
            Log.i("MainActivity", file.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                Log.i(TAG, uri.toString());
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            startActivity(intent);
            new Handler().postDelayed(() -> {
                if (file.exists())
                    file.delete();
                finish();
            }, 10000);
        } else {
            ToastUtils.INSTANCE.showToast(context, "安装出错，请重新下载");
        }
    }

    private void initUser() {
        Log.i("cw_ccc", timeData.selectId());
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selAll&id=" + timeData.selectId();
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, (String json) -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    ToastUtils.INSTANCE.showToast(context, "登录成功");
                    JSONObject user = (JSONObject) jsonObject.get("list");
                    String u_name = user.getString("name");
                    String u_day = user.getString("day");
                    main_name.setText(u_name + "---day" + u_day);
                } else {
                    ToastUtils.INSTANCE.showToast(context, "获取数据失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                refreshHomePage();
                ToastUtils.INSTANCE.showToast(context, "推荐内容已刷新！");
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
        phbList = new ArrayList<>();
        zs_ms = new ArrayList<>();

        main_menu = findViewById(R.id.main_menu);
        main_menu.setOnClickListener(this);
        nav = findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener((@NonNull MenuItem item) -> {
            //item.setChecked(true);
            //Toast.makeText(MainActivity.this, item.getTitle().toString() );
            show();
            setTab(item);
            return true;
        });
        messageAdapter = new MessageAdapter(context, zs_ms);
        activity_na = findViewById(R.id.activity_na);
        activity_na.setOnClickListener(this);
        main_name = findViewById(R.id.main_name);
        main_name.setOnClickListener(this);
        main_tjsx = findViewById(R.id.main_tjsx);
        main_tjsx.setOnClickListener(this);

        main_tuijian = findViewById(R.id.main_tuijian);
        main_tuijian.addFooterView(View.inflate(context, R.layout.foot_ms_list, null));
        main_tuijian.setAdapter(messageAdapter);
        main_tuijian.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(context, InfoActivity.class);
            intent.putExtra("id", messages.get(position).getId());
            intent.putExtra("title", messages.get(position).getTilte());
            intent.putExtra("content", messages.get(position).getContent());
            intent.putExtra("time", messages.get(position).getTime());
            intent.putExtra("type", messages.get(position).getType());
            intent.putExtra("username", messages.get(position).getUser());
            startActivity(intent);
        });
    }

    private CustomDatePicker mDatePicker;


    private void setTab(MenuItem item) {
        switch (item.getTitle().toString()) {
            default:
                break;
            case "签到打卡":
                markDay();
                break;
            case "等级排行榜":
                dialogRank();
                break;
            case "检查更新":
                checkForUpdate();
//                openFile(new File("/storage/emulated/0/speak.apk"));
                break;
            case "返回登录":
                startActivity(new Intent(context, EnterActivity.class));
                finish();
                break;
            case "说话":
                startActivity(new Intent(context, MessageBoardActivity.class));
                break;
            case "关于":
                ToastUtils.INSTANCE.showToast(context, "github地址");
                dialogQRCode();
                break;
            case "下载最新apk":
                downNewVersionAPK();
                break;
            case "个人中心":
                startActivity(new Intent(context, UserActivity.class));
                break;
            case "日历":
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = View.inflate(context, R.layout.view_calender, null);
                CalendarView calendar = view.findViewById(R.id.calendar);
                TextView calendar_year = view.findViewById(R.id.calendar_year);
                calendar_year.setText(String.valueOf(calendar.getCurYear()) + calendar.getCurMonth());
                initDatePicker(calendar, calendar_year);
                Button make_calendar = view.findViewById(R.id.make_calendar);
                make_calendar.setOnClickListener(v -> {
                    mDatePicker.show(calendar.getCurYear() + "-" + calendar.getCurMonth() + "-" + calendar.getCurDay());

//                    calendar.scrollToCalendar(2000, 12, 12);
//                    calendar_year.setText(String.valueOf(calendar.getCurYear()) + calendar.getCurMonth());
                });

                show = builder
                        .setTitle("日历")
                        .setView(view)
                        .setPositiveButton("确定", null)
                        .create();
                show.show();
                break;
        }
    }

    private void initDatePicker(CalendarView calendarView, TextView calendar_year) {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();


        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String[] thisData = DateFormatUtils.long2Str(timestamp, false).split("-");
                if (calendarView != null) {
                    calendarView.scrollToCalendar(Integer.parseInt(thisData[0]), Integer.parseInt(thisData[1]), Integer.parseInt(thisData[2]));
                    calendar_year.setText(DateFormatUtils.long2Str(timestamp, false));
                    runOnUiThread(() -> ToastUtils.INSTANCE.showToast(context, DateFormatUtils.long2Str(timestamp, false)));
                }

            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }


    private void downNewVersionAPK() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.dialog_new_version, null);
        final ProgressBar dialog_new_version_pb = v.findViewById(R.id.dialog_new_version_pb);
        final TextView dialog_new_version_tv = v.findViewById(R.id.dialog_new_version_tv);
        final Button dialog_new_version_begin = v.findViewById(R.id.dialog_new_version_begin);
        final Button dialog_new_version_stop = v.findViewById(R.id.dialog_new_version_stop);
        dialog_new_version_begin.setVisibility(View.VISIBLE);
        dialog_new_version_stop.setVisibility(View.VISIBLE);
        if (thisAPKVolume != 0) {
            dialog_new_version_begin.setText("继续");
            dialog_new_version_pb.setMax((int) maxAPKVolume);
            dialog_new_version_pb.setProgress((int) thisAPKVolume);
            dialog_new_version_tv.setText((thisAPKVolume * 100 / maxAPKVolume) + "%");
        }

        dialog_new_version_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.INSTANCE.showToast(context, "开始下载！");
                dialog_new_version_begin.setText("继续");

                DownloadManager.getInstance().download(context.getString(R.string.apk_down_url), new DownLoadObserver() {
                    @Override
                    public void onNext(DownloadInfo value) {
                        super.onNext(value);
                        runOnUiThread(() -> {
                                    thisAPKVolume = (float) value.getProgress();
                                    maxAPKVolume = (float) value.getTotal();
                                    dialog_new_version_pb.setMax((int) value.getTotal());
                                    dialog_new_version_pb.setProgress((int) value.getProgress());
                                    dialog_new_version_tv.setText((thisAPKVolume * 100 / maxAPKVolume) + "%");
                                }
                        );
                    }

                    @Override
                    public void onComplete() {
                        thisAPKVolume = 0;
                        maxAPKVolume = 0;
                        if (downloadInfo != null) {
                            if (show != null) {
                                show.cancel();
                                show = null;
                            }
                            //openFile(new File("/storage/emulated/0/Download/cw_ccc_official.apk"));
                            openFile(new File(MyApp.sContext.getFilesDir(), context.getString(R.string.apk_down_name)));
                            ToastUtils.INSTANCE.showToast(context, downloadInfo.getFileName() + "-DownloadComplete");

                        }
                    }
                });
            }
        });

        dialog_new_version_stop.setOnClickListener((View view) -> {
            DownloadManager.getInstance().cancel(context.getString(R.string.apk_down_url));
            ToastUtils.INSTANCE.showToast(context, "暂停下载！");
        });
        show = builder
                .setCancelable(false)
                .setView(v)
                .setNegativeButton("关闭", null)
                .create();
        show.show();
    }


    private List<User> phbList;
    private RankListAdapter phbListAdapteradapter = null;

    //排行榜
    private void dialogRank() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.dialog_cc_rank, null);
        final ListView phb_list = v.findViewById(R.id.phb_list);
        ImageView cc_phb_refresh = v.findViewById(R.id.cc_phb_refresh);
        cc_phb_refresh.setOnClickListener((View view) -> {
            ToastUtils.INSTANCE.showToast(context, "已刷新！");
            phbListAdapteradapter.notifyDataSetChanged();
        });
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=selTop";
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, (String json) -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    ToastUtils.INSTANCE.showToast(context, "如果没数据请再试一次");
                    JSONArray jsonArray = (JSONArray) jsonObject.get("list");
                    phbList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject one = (JSONObject) jsonArray.get(i);
                        String name = one.getString("name");
                        int day = one.getInt("day");
                        User user = new User(name, day);
                        phbList.add(user);
                    }
                    phbListAdapteradapter = new RankListAdapter(context, phbList);
                    phb_list.setAdapter(phbListAdapteradapter);
                    phbListAdapteradapter.notifyDataSetChanged();
                    handler.sendEmptyMessageDelayed(100, 500);
                } else {
                    ToastUtils.INSTANCE.showToast(context, "获取数据失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        show = builder.setPositiveButton("取消", null).setView(v).create();
        show.show();
    }

    private void markDay() {
        SimpleDateFormat daka_time = new SimpleDateFormat("yyyy-MM-dd");
        String d_t = daka_time.format(new Date());
        Log.i("cw_ccc", d_t);
        String url2 = context.getString(R.string.url) + "Cw/UserServlet?action=addRecord&id=" + timeData.selectId() + "&time=" + d_t;
        FormBody body2 = new FormBody.Builder().build();
        NetTool.netPost(handler, url2, body2, (String json) -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    ToastUtils.INSTANCE.showToast(context, "打卡成功");
                } else if (status.equals("error2")) {
                    ToastUtils.INSTANCE.showToast(context, "今天已经打卡");
                } else {
                    ToastUtils.INSTANCE.showToast(context, "打卡失败（网络错误）");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void dialogQRCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.dialog_qrcode, null);
        ImageView erweima_img = v.findViewById(R.id.erweima_img);
        erweima_img.setImageBitmap(QRCode);
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

    private void verify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.verify_hp, null);
        final EditText yz_yz = v.findViewById(R.id.yz_yz);
        Button yz_bt = v.findViewById(R.id.yz_bt);

        yz_bt.setOnClickListener((View view) -> {
            if (TextUtils.isEmpty(yz_yz.getText())) {
                ToastUtils.INSTANCE.showToast(context, "不能为空");
                return;
            }
            String yzm = yz_yz.getText() + "";

            if (yzm.equals("国标米兰")) {
                startActivity(new Intent(context, HpActivity.class));
            } else {
                ToastUtils.INSTANCE.showToast(context, "卧底gck！");
            }
        });
        show = builder
                .setPositiveButton("取消", null)
                .setView(v)
                .setTitle("验证")
                .create();

        show.show();
    }

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
}
