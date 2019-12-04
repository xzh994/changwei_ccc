package com.example.lwxg.changweistory.util

import android.content.Context
import android.util.Log
import com.example.lwxg.changweistory.MyApp
import java.io.Serializable

object LogUtils : Serializable {

    fun i(context: Context, text: String?) {
        Log.i(context.toString(), text);
    }

    private fun readResolve(): Any {
        return LogUtils
    }
}