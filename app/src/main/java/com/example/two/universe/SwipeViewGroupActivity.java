package com.example.two.universe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.two.R;
import com.example.two.widget.MyHeader;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class SwipeViewGroupActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private SpringView refreshLayout;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view_group);
        initViews();
        setAdapter();

    }

    private void setAdapter()
    {
        RecycleDataAdapter adapter = new RecycleDataAdapter(this, data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initViews()
    {
        refreshLayout = (SpringView) findViewById(R.id.refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        refreshLayout.setHeader(new MyHeader());
        refreshLayout.setFooter(new DefaultFooter(this));
        refreshLayout.setListener(new SpringView.OnFreshListener()
        {
            @Override
            public void onRefresh()
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                refreshLayout.setMoveTime(5);
                            }
                        });
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                refreshLayout.onFinishFreshAndLoad();
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onLoadmore()
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                refreshLayout.onFinishFreshAndLoad();
                            }
                        });
                    }
                }.start();
            }
        });
        data = new ArrayList<>();
        for (int i = 0; i < 20; ++i)
        {
            data.add("你不要脸" + i + "次");
        }
    }
}
