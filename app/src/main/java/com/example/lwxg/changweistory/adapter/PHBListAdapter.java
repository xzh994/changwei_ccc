package com.example.lwxg.changweistory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lwxg.changweistory.R;
import com.example.lwxg.changweistory.model.User;

import java.util.List;

public class PHBListAdapter extends BaseAdapter {
    Context context;
    List<User> list;

    public PHBListAdapter(Context context, List<User> list) {
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
        convertView = View.inflate(context, R.layout.list_ccc_phb, null);
        TextView list_phb1 = convertView.findViewById(R.id.list_phb1);
        TextView list_phb2 = convertView.findViewById(R.id.list_phb2);
        TextView list_phb3 = convertView.findViewById(R.id.list_phb3);
        list_phb1.setText((position + 1) + "");
        list_phb2.setText(list.get(position).getName() + "");
        list_phb3.setText("day" + list.get(position).getDay() + "");
        return convertView;
    }
}
