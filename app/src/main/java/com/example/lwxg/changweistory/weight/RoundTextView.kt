package com.example.lwxg.changweistory.weight

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.widget.TextView


class RoundTextView : TextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, def: Int) : super(context, attributeSet, def) {
        var rtvBorderWidth: Int = 0
        var rtvBorderColor: Int = 0
        var rtvRadius: Float = 0F
        var rtvBgColor: Int = 0

        val attributes: Unit = context.theme.obtainStyledAttributes(attributeSet, com.example.lwxg.changweistory.R.styleable.RoundTextView, def, 0).let {
            rtvBorderWidth = it.getDimensionPixelSize(com.example.lwxg.changweistory.R.styleable.RoundTextView_rtvBorderWidth, Color.BLACK)
            rtvBorderColor = it.getColor(com.example.lwxg.changweistory.R.styleable.RoundTextView_rtvBorderColor, Color.BLACK);
            rtvRadius = it.getDimension(com.example.lwxg.changweistory.R.styleable.RoundTextView_rtvRadius, 0F);
            rtvBgColor = it.getColor(com.example.lwxg.changweistory.R.styleable.RoundTextView_rtvBgColor, Color.WHITE);
            it?.recycle()
        }

        val gd = GradientDrawable().apply {
            setColor(rtvBgColor)
            cornerRadius = rtvRadius
            if (rtvBorderWidth > 0) {
                setStroke(rtvBorderWidth, rtvBorderColor)
            }
        }
        this.setBackground(gd)
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        val myGrad = background as GradientDrawable
        myGrad.setColor(color)
    }

}

