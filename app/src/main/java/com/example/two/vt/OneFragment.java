package com.example.two.vt;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.two.R;
import com.example.two.RecycleViewAdapter;
import com.example.yintao.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

/**
 * OneFragment
 */
public class OneFragment extends Fragment
{
    private RecyclerView recyclerView;
    List<String> data = new ArrayList<>();
    private RecycleViewAdapter adapter;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        LogUtil.e("onAttach1");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate2");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        initViews(view);
        LogUtil.e("onCreateView3");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e("onActivityCreated4");
        for (int i = 0; i < 10; ++i)
        {
            data.add("A" + i);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onStart()
    {
        super.onStart();
        LogUtil.e("onStart5");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LogUtil.e("onResume6");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        LogUtil.e("onPause7");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        LogUtil.e("onStop8");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        LogUtil.e("onDestroyView9");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LogUtil.e("onDestroy10");
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        LogUtil.e("onDetach11");
    }

    private void initViews(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        adapter = new RecycleViewAdapter(data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

}
