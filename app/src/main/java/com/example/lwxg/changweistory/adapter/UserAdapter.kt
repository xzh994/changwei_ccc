package com.example.lwxg.changweistory.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lwxg.changweistory.R

@Suppress("UNREACHABLE_CODE")
class UserAdapter(val context: Context, val list: ArrayList<String>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyler_user, null, false))
//        return ViewHolder(View.inflate(context, R.layout.item_recyler_user, null))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.run {
            holder.user_item_menu.text = list[position]
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user_item_menu: TextView = itemView.findViewById(R.id.user_item_menu)
    }

}