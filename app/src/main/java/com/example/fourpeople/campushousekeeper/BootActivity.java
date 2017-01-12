package com.example.fourpeople.campushousekeeper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.fourpeople.campushousekeeper.fragment.page.AuctionFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.MallFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.PartTimeFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.PersonFragment;
import com.example.fourpeople.campushousekeeper.widget.ChangeColorIconWithText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BootActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        initEvent();

    }

    /**
     * 鍒濆鍖栨墍鏈変簨浠�
     */
    private void initEvent()
    {

        mViewPager.setOnPageChangeListener(this);

    }

    private void initDatas()
    {

        MallFragment mallFragment = new MallFragment();
        PartTimeFragment parttimeFragment = new PartTimeFragment();
        AuctionFragment auctionFragment = new AuctionFragment();
        PersonFragment personFragment = new PersonFragment();
        mTabs.add(mallFragment);
        mTabs.add(parttimeFragment);
        mTabs.add(auctionFragment);
        mTabs.add(personFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabs.get(position);
            }

        };
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);

    }


    @Override
    public void onClick(View v)
    {
        clickTab(v);

    }


    private void clickTab(View v)
    {
        resetOtherTabs();

        switch (v.getId())
        {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 閲嶇疆鍏朵粬鐨凾abIndicator鐨勯鑹�
     */
    private void resetOtherTabs()
    {
        for (int i = 0; i < mTabIndicators.size(); i++)
        {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
        // + positionOffset);
        if (positionOffset > 0)
        {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        // TODO Auto-generated method stub

    }
}
