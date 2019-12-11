package com.example.lwxg.changweistory.broadcast

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.*
import cn.jpush.android.service.JPushMessageReceiver

class JPushMsgReceiver : JPushMessageReceiver() {

    override fun onMessage(context: Context?, customMessage: CustomMessage?) {
        Log.e(Tag, "[onMessage] " + customMessage!!)
    }

    override fun onNotifyMessageOpened(context: Context?, message: NotificationMessage?) {
        Log.e(Tag, "[onNotifyMessageOpened] " + message!!)
    }

    override fun onMultiActionClicked(context: Context, intent: Intent) {
        Log.e(Tag, "[onMultiActionClicked] 用户点击了通知栏按钮")
        val nActionExtra = intent.extras!!.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(Tag, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null")
            return
        }
        if (nActionExtra == "my_extra1") {
            Log.e(Tag, "[onMultiActionClicked] 用户点击通知栏按钮一")
        } else if (nActionExtra == "my_extra2") {
            Log.e(Tag, "[onMultiActionClicked] 用户点击通知栏按钮二")
        } else if (nActionExtra == "my_extra3") {
            Log.e(Tag, "[onMultiActionClicked] 用户点击通知栏按钮三")
        } else {
            Log.e(Tag, "[onMultiActionClicked] 用户点击通知栏按钮未定义")
        }
    }

    override fun onNotifyMessageArrived(context: Context?, message: NotificationMessage?) {
        Log.e(Tag, "[onNotifyMessageArrived] " + message!!)
    }

    override fun onNotifyMessageDismiss(context: Context?, message: NotificationMessage?) {
        Log.e(Tag, "[onNotifyMessageDismiss] " + message!!)
    }

    override fun onRegister(context: Context?, registrationId: String?) {
        Log.e(Tag, "[onRegister] " + registrationId!!)
    }

    override fun onConnected(context: Context?, isConnected: Boolean) {
        Log.e(Tag, "[onConnected] $isConnected")
        // 这里可以设置alias
    }

    override fun onCommandResult(context: Context?, cmdMessage: CmdMessage?) {
        Log.e(Tag, "[onCommandResult] " + cmdMessage!!)
    }

    override fun onTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        Log.d(Tag, "onTagOperatorResult!")
        super.onTagOperatorResult(context, jPushMessage)
    }

    override fun onCheckTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        Log.d(Tag, "onCheckTagOperatorResult")
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    override fun onAliasOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        Log.d(Tag, "onAliasOperatorResult==> alias is: ${jPushMessage?.alias}")
        super.onAliasOperatorResult(context, jPushMessage)
    }

    override fun onMobileNumberOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        Log.d(Tag, "onMobileNumberOperatorResult")
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    companion object {
        private const val Tag = "===JPushMsgReceiver==="
    }
}