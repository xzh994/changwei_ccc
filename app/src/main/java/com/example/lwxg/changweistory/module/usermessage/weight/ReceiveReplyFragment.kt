package com.example.lwxg.changweistory.module.usermessage.weight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lwxg.changweistory.R
import com.example.lwxg.changweistory.module.usermessage.adapter.ReceiveReplyAdapter
import kotlinx.android.synthetic.main.fragment_receive_reply.view.*

class ReceiveReplyFragment : Fragment() {
//    object ReceiveReplyFragment

    fun instance(): ReceiveReplyFragment {
        return ReceiveReplyFragment()
    }

    val list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_receive_reply, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.receive_reply_recyler) {
            layoutManager = LinearLayoutManager(context)
            adapter = ReceiveReplyAdapter(context, list.apply {
                add("item1")
                add("item2")
            })
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }
}