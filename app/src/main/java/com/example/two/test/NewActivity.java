package com.example.two.test;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.two.R;
import com.example.two.universe.RecycleDataAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewActivity extends AppCompatActivity
{
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.collapseToolBar)
    CollapsingToolbarLayout collapseToolBar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;

    ArrayList<String> data = new ArrayList<>();
    private RecycleDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);

        for (int i = 0; i < 20; ++i)
        {
            data.add("ACCCCB" + i);
        }
        adapter = new RecycleDataAdapter(this, data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(manager);
        recycleView.setAdapter(adapter);
    }
}
