package com.example.two;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by YinTao on 2018/1/18.
 */

public class ListViewAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<String> data;
    private LayoutInflater inflater;

    public ListViewAdapter(Context context, ArrayList<String> data)
    {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public String getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_contact_list, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = data.get(position);
        holder.textView.setText(s);
        return convertView;
    }

    class ViewHolder
    {
        TextView textView;
    }
}
