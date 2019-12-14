package com.example.lwxg.changweistory.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.lwxg.changweistory.R;

import androidx.appcompat.app.AppCompatActivity;

public class HpActivity extends AppCompatActivity {
//    Context context = this;
//    String[] musicName = {"h1.mp4", "h2.mp4"};
//    int[] vedioURL = {R.raw.h1, R.raw.h2};
//    private ListView home_list;
//    int i = 0;
//    private VideoView home_vedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hp);
//        initView();
//        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, musicName);
//        home_list.setAdapter(adapter);
//        home_list.setOnItemClickListener(this);
//        home_vedio = (VideoView) findViewById(R.id.home_vedio);
//
//        //加载指定的视频文件
//        String path = "android.resource://" + getPackageName() + "/" + vedioURL[0];
//        home_vedio.setVideoPath(path);
//        MediaController mediaController = new MediaController(this);
//        home_vedio.setMediaController(mediaController);
//        home_vedio.requestFocus();
//        home_vedio.start();
//
    }
//
//    private void initView() {
//        home_list = (ListView) findViewById(R.id.home_list);
//        home_vedio = (VideoView) findViewById(R.id.home_vedio);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (position == 0) {
//            String path = "android.resource://" + getPackageName() + "/" + vedioURL[0];
//            home_vedio.setVideoPath(path);
//            MediaController mediaController = new MediaController(this);
//            home_vedio.setMediaController(mediaController);
//            home_vedio.requestFocus();
//            home_vedio.start();
//
//        } else {
//            String path = "android.resource://" + getPackageName() + "/" + vedioURL[1];
//            home_vedio.setVideoPath(path);
//            MediaController mediaController = new MediaController(this);
//            home_vedio.setMediaController(mediaController);
//            home_vedio.requestFocus();
//            home_vedio.start();
//        }
//    }
}
