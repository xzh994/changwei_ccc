package com.example.lwxg.changweistory.util;

import android.os.Handler;
import android.util.Log;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//访问网络工具类
public class NetTool {
    // FormBody body = new FormBody.Builder()  .add("userPwd", "123").build();
    public static void netPost(final Handler handler, final String url, final FormBody body, final NetBack netBack) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(url).post(body).build();
                    Response response = new OkHttpClient().newCall(request).execute();
                    final String result = response.body().string();
                    Log.i("MyThread", "result:" + result);
                    handler.post(new Runnable() {//运行到Ui线程
                        @Override
                        public void run() {
                            netBack.onBack(result);
                        }
                    });
                } catch (Exception e) {
                    Log.i("MyThread", "Exception:" + e);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public interface NetBack {

        void onBack(String json);//
    }
}
