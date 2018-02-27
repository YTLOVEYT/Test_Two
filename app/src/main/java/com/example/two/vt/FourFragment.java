package com.example.two.vt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.two.R;

/**
 * FourFragment
 */
public class FourFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view)
    {

    }

}
