package com.example.lwxg.changweistory.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.data.SomeDatas;
import com.example.lwxg.changweistory.model.Reply;
import com.example.lwxg.changweistory.util.LocalCacheUtils;
import com.example.lwxg.changweistory.weight.RoundImageView;

import java.util.List;

public class ReplyAdapter extends BaseAdapter {
    private Context context;
    private List<Reply> list;

    public ReplyAdapter(Context context, List<Reply> list) {
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
        convertView = View.inflate(context, R.layout.item_list_info_reply, null);
        TextView list_info_reply_name = convertView.findViewById(R.id.list_info_reply_name);
        TextView list_info_reply_day = convertView.findViewById(R.id.list_info_reply_day);
        TextView list_info_reply_time = convertView.findViewById(R.id.list_info_reply_time);
        TextView list_info_reply_content = convertView.findViewById(R.id.list_info_reply_content);
        RoundImageView list_info_reply_img = convertView.findViewById(R.id.list_info_reply_img);
        Glide.with(context)
                .load(list.get(position).getUser_head_image())
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(list_info_reply_img);
        list_info_reply_name.setText("" + list.get(position).getUser_name());
        list_info_reply_day.setText("day" + list.get(position).getUser_day());
        list_info_reply_time.setText("" + list.get(position).getTime());
        list_info_reply_content.setText("   " + list.get(position).getContent());

        return convertView;
    }
}
