package com.example.two.rl;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.two.R;
import com.example.two.vt.FourFragment;
import com.example.two.vt.OneFragment;
import com.example.two.vt.ThreeFragment;
import com.example.two.vt.TwoFragment;

public class MainPage2Activity extends AppCompatActivity
{
    private LinearLayout llContainer;
    private RadioGroup rgContainer;
    private RadioButton rbOne, rbTwo, rbThree, rbFour;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page2);
        initViews();
        setListeners();

    }

    private void initViews()
    {
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        rgContainer = (RadioGroup) findViewById(R.id.rg_container);
        rbOne = (RadioButton) findViewById(R.id.rb_one);
        rbTwo = (RadioButton) findViewById(R.id.rb_two);
        rbThree = (RadioButton) findViewById(R.id.rb_three);
        rbFour = (RadioButton) findViewById(R.id.rb_four);
        final Fragment[] fragments = new Fragment[]{new OneFragment(), new TwoFragment(), new ThreeFragment(), new FourFragment()};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_container, fragments[0])
                //                .add(R.id.ll_container, fragments[1])
                //                .add(R.id.ll_container, fragments[2])
                //                .add(R.id.ll_container, fragments[3])
                .show(fragments[0]).commit();
        rgContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.rb_one:
                        rbOne.setChecked(true);
                        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                        if (!fragments[0].isAdded())
                        {
                            t.add(R.id.ll_container, fragments[0] = new OneFragment());
                        }
                        t.hide(fragments[1]).hide(fragments[2]).hide(fragments[3]).show(fragments[0]).commit();
                        break;
                    case R.id.rb_two:
                        rbTwo.setChecked(true);
                        FragmentTransaction t2 = getSupportFragmentManager().beginTransaction();
                        if (!fragments[1].isAdded())
                        {
                            t2.add(R.id.ll_container, fragments[1] = new TwoFragment());
                        }
                        t2.hide(fragments[0]).hide(fragments[2]).hide(fragments[3]).show(fragments[1]).commit();
                        break;
                    case R.id.rb_three:
                        rbThree.setChecked(true);
                        FragmentTransaction t3 = getSupportFragmentManager().beginTransaction();
                        if (!fragments[2].isAdded())
                        {
                            t3.add(R.id.ll_container, fragments[2] = new ThreeFragment());
                        }
                        t3.hide(fragments[1]).hide(fragments[0]).hide(fragments[3]).show(fragments[2]).commit();
                        break;
                    case R.id.rb_four:
                        rbFour.setChecked(true);
                        FragmentTransaction t4 = getSupportFragmentManager().beginTransaction();
                        if (!fragments[3].isAdded())
                        {
                            t4.add(R.id.ll_container,fragments[3] =  new FourFragment());
                        }
                        t4.hide(fragments[1]).hide(fragments[2]).hide(fragments[0]).show(fragments[3]).commit();
                        break;
                }
            }
        });
    }

    private void setListeners()
    {

    }

}
