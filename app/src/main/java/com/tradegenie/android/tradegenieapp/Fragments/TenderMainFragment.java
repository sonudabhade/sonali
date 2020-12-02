package com.tradegenie.android.tradegenieapp.Fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TenderMainFragment extends Fragment {


    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;


    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private ViewPagerAdapter adapter;


    public TenderMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tender_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        FragmentManager fragmentManager = getFragmentManager();

        try {

            if (Constants.mMainActivity.tenderListFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.tenderListFragment).commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Constants.mMainActivity.newTenderFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.newTenderFragment).commit();
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


        if (Constants.mMainActivity.WhichTenderFragmentIsopen.equals("My Tender")) {

            viewPager.setCurrentItem(1);
            Constants.mMainActivity.WhichTenderFragmentIsopen = "";
        } else if (Constants.mMainActivity.WhichTenderFragmentIsopen.equals("")) {

            viewPager.setCurrentItem(0);
        }

        //Constants.mMainActivity.ShowLeadSelectedSelected();


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
        mFragmentList.add(Constants.mMainActivity.newTenderFragment);
        mFragmentTitleList.add("New Tenders");

        mFragmentList.add(Constants.mMainActivity.tenderListFragment);
        mFragmentTitleList.add("My Tenders");


        viewPager.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
