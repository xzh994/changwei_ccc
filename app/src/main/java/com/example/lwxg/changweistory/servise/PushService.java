package com.example.lwxg.changweistory.servise;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lwxg.changweistory.Receiver.ShowNotificationReceiver;


public class PushService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("PushService", "PushService onCreate");
        //用AlarmManager定时发送广播
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, ShowNotificationReceiver.class);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.currentThreadTimeMillis(), pendingIntent);

    }

}