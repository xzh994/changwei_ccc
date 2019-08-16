package com.example.lwxg.changweistory.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lwxg.changweistory.MainActivity;
import com.example.lwxg.changweistory.R;

public class LoginActivity extends AppCompatActivity {
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler.sendEmptyMessageDelayed(0, 2000);

    }
}
