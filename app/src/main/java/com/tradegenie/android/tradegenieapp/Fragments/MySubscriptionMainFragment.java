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
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MySubscriptionMainFragment extends Fragment {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private String Title = "";

    private SessionManager mSessionManager;


    public MySubscriptionMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_subscription_main, container, false);
        Constants.mMainActivity.setToolBarName("My Subscription");
        Constants.mMainActivity.setToolBar(VISIBLE,GONE,GONE,GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager=new SessionManager(getActivity());


        //Check personal details and business details
        if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("")||mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")){

            //UtilityMethods.showInfoToast(getActivity(),"Please complete your profile details.");

            /*PersonalDetailsFragment personalDetailsFragment=new PersonalDetailsFragment();
            Constants.mMainActivity.ChangeFragments(personalDetailsFragment,"PersonalDetailsFragment")*/;

            Log.e("check==========>","PersonalDetailsFragment");

        }else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_NAME).equals("")||mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")){

            UtilityMethods.showInfoToast(getActivity(),"Please complete your business details first");


            BusinessDetailsFragment businessDetailsFragment=new BusinessDetailsFragment();
            Constants.mMainActivity.ChangeFragments(businessDetailsFragment,"BusinessDetailsFragment");

            Log.e("check==========>","BusinessDetailsFragment");

        }


        ViewPager viewPager = view.findViewById(R.id.viewpager);

        FragmentManager fragmentManager = getFragmentManager();

        try {

            if (Constants.mMainActivity.mySubscriptionFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.mySubscriptionFragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Constants.mMainActivity.mySubscriptionRecommendedFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.mySubscriptionRecommendedFragment).commit();
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

        mFragmentList.add(Constants.mMainActivity.mySubscriptionFragment);
        mFragmentTitleList.add("My Subscription");

        mFragmentList.add(Constants.mMainActivity.mySubscriptionRecommendedFragment);
        mFragmentTitleList.add("Recommended");


        viewPager.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
            {

                Constants.mMainActivity.mySubscriptionFragment.counter = 0;
                Constants.mMainActivity.mySubscriptionFragment.CallMySubscriptionList();

            } else {

                UtilityMethods.showInternetDialog(getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
