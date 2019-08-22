package com.example.lwxg.changweistory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.model.Messages;

import java.util.List;

public class MyMessagesAdapter extends BaseAdapter {
    Context context;
    List<Messages> list;

    public MyMessagesAdapter(Context context, List<Messages> list) {
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
        convertView = View.inflate(context, R.layout.list_mymessage_message, null);
        TextView home_lsit_title = convertView.findViewById(R.id.myblog_lsit_title);
        TextView home_list_time = convertView.findViewById(R.id.myblog_list_time);
        TextView home_list_cotent = convertView.findViewById(R.id.myblog_list_cotent);
        Button list_bt_delete = convertView.findViewById(R.id.list_bt_delete);
        home_lsit_title.setText(list.get(position).getTilte());
        home_list_time.setText(list.get(position).getTime());
        home_list_cotent.setText(list.get(position).getContent());
        list_bt_delete.setTag(list.get(position).getId() + "");
        list_bt_delete.setOnClickListener((View.OnClickListener) context);

        return convertView;
    }
}
