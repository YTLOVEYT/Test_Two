package com.example.two.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.two.R;
import com.liaoinstan.springview.container.BaseHeader;

/**
 * Created by YinTao on 2018/1/24.
 */

public class MyHeader extends BaseHeader
{

    private View view;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup)
    {
        view = inflater.inflate(R.layout.header, null);
        return view;
    }

    @Override
    public void onPreDrag(View rootView)
    {

    }

    @Override
    public void onDropAnim(View rootView, int dy)
    {

    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown)
    {

    }

    @Override
    public void onStartAnim()
    {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishAnim()
    {
        view.setVisibility(View.GONE);
    }
}
