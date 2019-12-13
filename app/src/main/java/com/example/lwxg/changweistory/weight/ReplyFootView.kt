package com.example.lwxg.changweistory.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.example.lwxg.changweistory.R
import kotlinx.android.synthetic.main.view_reply_foot.view.*

class ReplyFootView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, def: Int) : super(context, attributeSet, def)

    fun getEtContent(): EditText? {
        return reply_foot_view_content
    }

    fun getBtConfirm(): Button? {
        return reply_foot_view_confirm
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_reply_foot, this, true)
    }
}