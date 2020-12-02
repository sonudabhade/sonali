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
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProductListFragment extends Fragment {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private String Title = "";
    private String Approved="",Rejected="";
    private SessionManager mSessionManager;




    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_list, container, false);
        Constants.mMainActivity.setToolBarName("Product List");
        Constants.mMainActivity.setToolBar(VISIBLE,GONE,GONE,GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.OpenProductListSetIcon();
        mSessionManager=new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();

        //Check personal details and business details
        if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("")||mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")){

           // UtilityMethods.showInfoToast(getActivity(),"Please complete your profile details.");


            /*PersonalDetailsFragment personalDetailsFragment=new PersonalDetailsFragment();
            Constants.mMainActivity.ChangeFragments(personalDetailsFragment,"PersonalDetailsFragment");*/

            Log.e("check==========>","PersonalDetailsFragment");

        }else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_NAME).equals("")||mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")){

            UtilityMethods.showInfoToast(getActivity(),"Please complete your business details first");


            BusinessDetailsFragment businessDetailsFragment=new BusinessDetailsFragment();
            Constants.mMainActivity.ChangeFragments(businessDetailsFragment,"BusinessDetailsFragment");

            Log.e("check==========>","BusinessDetailsFragment");

        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            try {
                Approved = bundle.getString("redirect_type");
            } catch (Exception e) {
                Approved="default";
                e.printStackTrace();

            }

            try {
                Rejected = bundle.getString("redirect_type");
            } catch (Exception e) {
                Rejected="default";
                e.printStackTrace();


            }


        }

        ViewPager viewPager = view.findViewById(R.id.viewpager);

        FragmentManager fragmentManager = getFragmentManager();

        try {

            if (Constants.mMainActivity.productListPendingFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.productListPendingFragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Constants.mMainActivity.productListApprovedFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.productListApprovedFragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Constants.mMainActivity.productListRejectedFragment != null) {
                fragmentManager.beginTransaction().remove(Constants.mMainActivity.productListRejectedFragment).commit();
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

        Log.e("check","Fragment"+Constants.mMainActivity.WhichFragmentIsopen);

        if (Approved.equalsIgnoreCase("Approved")){

            viewPager.setCurrentItem(1);


        }else if(Rejected.equalsIgnoreCase("Rejected")){

            viewPager.setCurrentItem(2);
        }else if(Constants.mMainActivity.WhichFragmentIsopen.equals("Fragment Approved")){

            viewPager.setCurrentItem(1);
        }else if(Constants.mMainActivity.WhichFragmentIsopen.equals("Fragment Pending")){

            viewPager.setCurrentItem(0);
        }else if(Constants.mMainActivity.WhichFragmentIsopen.equals("Fragment Rejected")){

            viewPager.setCurrentItem(2);
        }else {

            viewPager.setCurrentItem(0);
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            try {
                Constants.mMainActivity.productListApprovedFragment.getProductListApproved();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    //Set viewpager and add fragments in list
    private void setupViewPager(ViewPager viewPager) {

        try {
            mFragmentList.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

        mFragmentList.add(Constants.mMainActivity.productListPendingFragment);
        mFragmentTitleList.add("Pending");

        mFragmentList.add(Constants.mMainActivity.productListApprovedFragment);
        mFragmentTitleList.add("Approved");

        mFragmentList.add(Constants.mMainActivity.productListRejectedFragment);
        mFragmentTitleList.add("Rejected");

        viewPager.setAdapter(adapter);
    }
}
