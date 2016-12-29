package com.example.asus.hillplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 首页viewpager的适配器
 * Created by asus-cp on 2016-12-28.
 */

public class MusicFragmentAdapter  extends FragmentPagerAdapter{

    private List<Fragment> mFragments;

    private List<String> mTitles;

    public MusicFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> mTitles) {
        super(fm);
        mFragments = fragments;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
