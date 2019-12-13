package com.example.lwxg.changweistory.module.jpush

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.*

/**
 * 自定义JPush接收器
 *
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class JPushReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val bundle = intent.extras
            Log.d(Tag, "[MyReceiver] onReceive - " + intent.action + ", extras: ")

            if (JPushInterface.ACTION_REGISTRATION_ID == intent.action) {
                val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                Log.d(Tag, "[MyReceiver] 接收Registration Id : " + regId!!)
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action) {
                Log.d(Tag, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle!!.getString(JPushInterface.EXTRA_MESSAGE)!!)
//                processCustomMessage(context, bundle)

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action) {
                Log.d(Tag, "[MyReceiver] 接收到推送下来的通知")
                val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                Log.d(Tag, "[MyReceiver] 接收到推送下来的通知的ID: $notifactionId")

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action) {
                Log.d(Tag, "[MyReceiver] 用户点击打开了通知")

//                //打开自定义的Activity
//                val i = Intent(context, TestActivity::class.java)
//                i.putExtras(bundle!!)
//                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                context.startActivity(i)

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.action) {
                Log.d(
                        Tag,
                        "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle!!.getString(JPushInterface.EXTRA_EXTRA)!!
                )
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE == intent.action) {
                val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                Log.w(Tag, "[MyReceiver]" + intent.action + " connected state change to " + connected)
            } else {
                Log.d(Tag, "[MyReceiver] Unhandled intent - " + intent.action!!)
            }
        } catch (e: Exception) {

        }
    }

    companion object {
        private const val Tag = "===JPushReceiver==="
    }

}



