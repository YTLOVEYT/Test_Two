package com.example.two.vt;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.two.R;
import com.example.yintao.utils.LogUtil;

/**
 * ThreeFragment
 */
public class ThreeFragment extends Fragment
{
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        LogUtil.e("onAttach31");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate32");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        initViews(view);
        LogUtil.e("onCreateView33");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e("onActivityCreated34");
    }

    @Override
    public void onStart()
    {
        super.onStart();
        LogUtil.e("onStart35");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LogUtil.e("onResume36");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        LogUtil.e("onPause37");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        LogUtil.e("onStop38");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        LogUtil.e("onDestroyView39");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LogUtil.e("onDestroy310");
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        LogUtil.e("onDetach311");
    }

    private void initViews(View view)
    {

    }

}
