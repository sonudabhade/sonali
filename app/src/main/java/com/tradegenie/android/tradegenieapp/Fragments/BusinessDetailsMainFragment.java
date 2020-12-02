package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tradegenie.android.tradegenieapp.Adapters.ViewPagerAdapter;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BusinessDetailsMainFragment extends Fragment {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private String Title = "";


    public BusinessDetailsMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_business_details_main, container, false);
        Constants.mMainActivity.setToolBarName(getString(R.string.business_details));
        Constants.mMainActivity.setToolBar(VISIBLE,GONE,GONE,GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();



        ViewPager viewPager = view.findViewById(R.id.viewpager);

        FragmentManager fragmentManager = getFragmentManager();

        try {

            if (Constants.mMainActivity.businessDetailsFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.businessDetailsFragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Constants.mMainActivity.businessDetailsOtherDetailsFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.businessDetailsOtherDetailsFragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("BackStackEntryCount()", "fragmentManager.getBackStackEntryCount() " + fragmentManager.getBackStackEntryCount());

        try {
            mFragmentList.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new ViewPagerAdapter(getFragmentManager(), mFragmentList, mFragmentTitleList);
        viewPager.setSaveFromParentEnabled(false);

        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

    //Set viewpager and add fragments in list
    private void setupViewPager(ViewPager viewPager) {

        try {
            mFragmentList.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

        mFragmentList.add(Constants.mMainActivity.businessDetailsFragment);
        mFragmentTitleList.add(getString(R.string.business_details));

        mFragmentList.add(Constants.mMainActivity.businessDetailsOtherDetailsFragment);
        mFragmentTitleList.add(getString(R.string.other_details));


        viewPager.setAdapter(adapter);
    }
}
