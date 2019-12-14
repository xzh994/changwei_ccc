package com.example.lwxg.changweistory.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.lwxg.changweistory.R;

import androidx.core.app.NotificationCompat;


public class ShowNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "RepeatReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "ShowNotificationReceiver onReceive");

        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentTitle("饭已经准备好")
//                .setTicker("")
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.mipmap.ic_launcher_round);//设置小图标;
//
//        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(2, builder.build());
//
//
        String id = "channel_001";
        String name = "name";
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//判断API
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(id)
                    .setContentTitle("这位肠胃")
                    .setContentIntent(pendingIntent)
                    .setContentText("你已经很久没有手冲了，快来手冲并打卡吧")
                    .setSmallIcon(R.mipmap.ic_launcher_round).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("这位肠胃")
                    .setContentText("你已经很久没有手冲了，快来手冲并打卡吧")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setOngoing(true)
                    .setChannelId(id);//无效
            notification = notificationBuilder.build();
        }

        notificationManager.notify(2, notification);
    }
}