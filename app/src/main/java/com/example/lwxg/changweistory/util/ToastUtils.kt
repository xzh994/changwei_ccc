package com.example.lwxg.changweistory.util

import android.content.Context
import android.widget.Toast
import com.example.lwxg.changweistory.MyApp
import java.io.Serializable

object ToastUtils : Serializable {


    fun showToast(context: Context, content: CharSequence?): Toast {
        return Toast.makeText(context, content, Toast.LENGTH_SHORT).apply { show() }
    }

    private fun readResolve(): Any {
        return ToastUtils
    }
}