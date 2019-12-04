package com.example.lwxg.changweistory.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.lwxg.changweistory.R
import com.example.lwxg.changweistory.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    private val list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        with(user_recyler) {
            layoutManager = LinearLayoutManager(context)
            adapter = UserAdapter(context, list.apply {
                add("aaaa")
                add("bbbb")
                add("cccc")
                add("dddd")
            })
        }

    }
}
