package com.example.lwxg.changweistory.usermessage

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.OnTabSelectedListener
import android.support.v4.app.Fragment
import com.example.lwxg.changweistory.BaseActivity
import com.example.lwxg.changweistory.R
import com.example.lwxg.changweistory.adapter.SimpleViewpagerAdapter
import com.example.lwxg.changweistory.usermessage.weight.ReceiveReplyFragment
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.view.*

class UserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        val fragments = ArrayList<Fragment>().apply {
            add(ReceiveReplyFragment().instance())
        }
        user_viewpager.adapter = SimpleViewpagerAdapter(getParentSupportFragmentManager(), fragments)

        with(user_table as TabLayout) {
            addOnTabSelectedListener(tabSelectedListener)
            val chat: TabLayout.Tab = this.newTab().setText("聊天")
            val message: TabLayout.Tab = this.newTab().setText("消息")
            addTab(chat)
            addTab(message)
            chat.select()
        }

    }

    private val tabSelectedListener = object : OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            tab?.position?.let { user_viewpager.setCurrentItem(it, false) }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
        }


    }
}
