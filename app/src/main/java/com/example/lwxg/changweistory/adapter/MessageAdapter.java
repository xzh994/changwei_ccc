package com.example.lwxg.changweistory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.model.Messages;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    Context context;
    List<Messages> list;

    public MessageAdapter(Context context, List<Messages> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.list_home_message, null);
        TextView home_lsit_user = convertView.findViewById(R.id.home_lsit_user);
        TextView home_lsit_title = convertView.findViewById(R.id.home_lsit_title);
        TextView home_list_time = convertView.findViewById(R.id.home_list_time);
        TextView home_list_cotent = convertView.findViewById(R.id.home_list_cotent);
        home_lsit_user.setText("用户：" + list.get(position).getUser());
        home_lsit_title.setText("标题：" + list.get(position).getTilte());
        home_list_time.setText("" + list.get(position).getTime());
        home_list_cotent.setText("正文：" + list.get(position).getContent());

        return convertView;
    }
}
