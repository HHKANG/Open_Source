package com.example.samsung.os;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SAMSUNG on 2017-09-25.
 */

public class listview_text_adapter extends BaseAdapter{
    private Context context;
    private int layoutid;
    private ArrayList<listview_text> list;
    private LayoutInflater inflater;

    public listview_text_adapter(Context context, int layoutid, ArrayList<listview_text> list){
        this.context = context;
        this.layoutid = layoutid;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_listview_text, parent, false);
        }

        listview_text item = list.get(position);

        TextView text = (TextView) convertView.findViewById(R.id.listview_text);
        text.setText(item.getText());

        return convertView;
    }
}