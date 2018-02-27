package com.example.two.vt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.two.ListViewAdapter;
import com.example.two.R;
import com.example.two.widget.SideCutListView;
import com.example.yintao.utils.LogUtil;

import java.util.ArrayList;

/**
 * TwoFragment
 */
public class TwoFragment extends Fragment
{
    private SideCutListView listView;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData()
    {
        for (int i = 0; i < 20; ++i)
        {
            data.add("B" + i);
        }
    }

    private void initView(View view)
    {
        listView = (SideCutListView) view.findViewById(R.id.slideListView);
        final ListViewAdapter adapter = new ListViewAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnSlideRemoveLister(new SideCutListView.RemoveListener()
        {
            @Override
            public void removeItem(SideCutListView.Direction direction, int position)
            {
                LogUtil.e("direction=" + direction + ",position=" + position);
                data.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                LogUtil.e("position=" + position);
            }
        });
    }


}
