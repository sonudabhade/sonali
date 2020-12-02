package com.tradegenie.android.tradegenieapp.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    private List<Fragment> mFragmentList;
    private List<String> mFragmentTitleList;

    public ViewPagerAdapter(FragmentManager manager, List<Fragment> mFragmentList, List<String> mFragmentTitleList) {
        super(manager);

        this.mFragmentList = mFragmentList;
        this.mFragmentTitleList = mFragmentTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {

        return mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}