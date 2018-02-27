package com.example.two.vt;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.two.R;

/** tabLayout+viewPager */
public class MainPageActivity extends AppCompatActivity
{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] tabsStr = new String[]{"会话", "通讯录", "发现", "我"};
    private int[] tabDrawables = new int[]{R.drawable.tab_chat_bg, R.drawable.tab_contact_list_bg, R.drawable.tab_find_bg, R.drawable.tab_profile_bg};
    private TabLayout.Tab[] tabs;
    private TextView oneLabel, twoLabel, threeLabel, fourLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        initViews();


    }

    private void initViews()
    {
        viewPager = (ViewPager) findViewById(R.id.vp_container);
        tabLayout = (TabLayout) findViewById(R.id.tb_layout);
        final Fragment[] fragments = new Fragment[]{new OneFragment(), new TwoFragment(), new ThreeFragment(), new FourFragment()};
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int position)
            {
                return fragments[position];
            }

            @Override
            public int getCount()
            {
                return fragments.length;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabs = new TabLayout.Tab[fragments.length];
        for (int i = 0; i < fragments.length; ++i)
        {
            tabs[i] = tabLayout.getTabAt(i);
            tabs[i].setCustomView(setCustomView(i, tabDrawables[i]));
        }
    }

    private View setCustomView(final int index, int drawable)
    {
        View view = getLayoutInflater().inflate(R.layout.tab_bottom, null);
        Button button = (Button) view.findViewById(R.id.tab_button);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, drawable, 0, 0);
        button.setText(tabsStr[index]);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tabs[index].select();
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.unread_msg_number);
        if (index == 0)
        {
            oneLabel = textView;
        }
        else if (index == 1)
        {
            twoLabel = textView;
        }
        else if (index == 2)
        {
            threeLabel = textView;
        }
        else if (index == 3)
        {
            fourLabel = textView;
        }
        return view;
    }

}
