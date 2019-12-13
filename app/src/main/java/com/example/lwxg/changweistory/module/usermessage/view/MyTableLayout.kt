package com.example.lwxg.changweistory.module.usermessage.view

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lwxg.changweistory.R.dimen.px20

class MyTableLayout : TabLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, def: Int) : super(context, attributeSet, def)

    init {
        post {
            try {
                //拿到tabLayout的mTabStrip属性
                val mTableSprip: LinearLayout = getChildAt(0) as LinearLayout

                for (index in 0 until mTableSprip.childCount) {
                    val tabView = mTableSprip.getChildAt(index)

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    val mTextViewField = tabView.javaClass.getDeclaredField("mTextView")
                    mTextViewField.isAccessible = true
                    val mTextView = mTextViewField.get(tabView) as TextView
                    tabView.setPadding(0, 0, 0, 0)

                    //因为我想要的效果是字多宽线就多宽，所以测量mTextView的宽度
                    val width: Int = mTextView.let {
                        if (it.width == 0) {
                            it.measure(0, 0)
                            return@let it.measuredWidth
                        }
                        it.width
                    }

                    //设置tab左右间距 注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    with(tabView.layoutParams as LinearLayout.LayoutParams) {
                        this.width = width
                        leftMargin = resources.getDimensionPixelSize(px20)
                        rightMargin = resources.getDimensionPixelSize(px20)
                        this
                    }.let { tabView.layoutParams = it }
                    tabView.invalidate()
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }

    }

}
