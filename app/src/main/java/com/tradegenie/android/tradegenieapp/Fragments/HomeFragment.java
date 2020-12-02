package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterHomePageItemList;
import com.tradegenie.android.tradegenieapp.Adapters.HomePostRequirementAdapter;
import com.tradegenie.android.tradegenieapp.Adapters.MyViewPagerAdapter;
import com.tradegenie.android.tradegenieapp.Adapters.SliderAdapterExample;
import com.tradegenie.android.tradegenieapp.Models.AdsItem;
import com.tradegenie.android.tradegenieapp.Models.AdvCategoryModel;
import com.tradegenie.android.tradegenieapp.Models.CategoryModel;
import com.tradegenie.android.tradegenieapp.Models.HomePostEnquiryModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HomeFragment extends Fragment {

    MyViewPagerAdapter myViewPagerAdapter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.relative_layout_view_pager)
    RelativeLayout relativeLayoutViewPager;
    Unbinder unbinder;
    @BindView(R.id.reyclerview_home_items)
    RecyclerView reyclerviewHomeItems;
    @BindView(R.id.relativeLayoutHome)
    RelativeLayout relativeLayoutHome;
    @BindView(R.id.imageSlider)
    SliderView imageSlider;
    @BindView(R.id.cardSlider)
    CardView cardSlider;
    @BindView(R.id.rv_post_req)
    RecyclerView rvPostReq;
//    @BindView(R.id.linear_layout_top)
//    LinearLayout linearLayoutTop;

    private ArrayList<AdsItem> adsArrayList;
    private TextView dots[];

    int NUM_PAGES, currentPage;

    Timer timer;

    private SessionManager mSessionManager;


    //private ArrayList<HomePageItemModel> mHomePageItemModelArrayList;
    private ArrayList<CategoryModel> mCategoryModelArrayList;
    private AdapterHomePageItemList mAdapterHomePageItemList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        Constants.mMainActivity.setToolBarName(getString(R.string.home));
        Constants.mMainActivity.setToolBar(GONE, VISIBLE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.homeFragment = HomeFragment.this;
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());


        //Check personal details and business details
        if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")) {

            //UtilityMethods.showInfoToast(getActivity(), "Please complete your profile details.");


            /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
            Constants.mMainActivity.ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");*/

            Log.e("check==========>", "PersonalDetailsFragment");

        } else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")) {

            UtilityMethods.showInfoToast(getActivity(), "Please complete your business details first");


            BusinessDetailsFragment businessDetailsFragment = new BusinessDetailsFragment();
            Constants.mMainActivity.ChangeFragments(businessDetailsFragment, "BusinessDetailsFragment");

            Log.e("check==========>", "BusinessDetailsFragment");

        } else {

            adsArrayList = new ArrayList();
            myViewPagerAdapter = new MyViewPagerAdapter(getActivity(), adsArrayList);
            viewPager.setClipToPadding(false);
            viewPager.setPadding(30, 0, 30, 0);
            viewPager.setAdapter(myViewPagerAdapter);
            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
            // adding bottom dots
            NUM_PAGES = adsArrayList.size();
            addBottomDots(0);
            //SetSliderAutoTimer();


            mCategoryModelArrayList = new ArrayList<>();
            mAdapterHomePageItemList = new AdapterHomePageItemList(getActivity(), mCategoryModelArrayList);
            reyclerviewHomeItems.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            reyclerviewHomeItems.setAdapter(mAdapterHomePageItemList);

            if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
            {

                getBanners();

            } else {

                UtilityMethods.showInternetDialog(getActivity());
            }

            if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
            {

                requestHomePostRequirements();

            } else {

                UtilityMethods.showInternetDialog(getActivity());
            }


            if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
            {

                GetCategoryList();

            } else {

                UtilityMethods.showInternetDialog(getActivity());
            }

        }


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            currentPage = position;
            addBottomDots(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    //Method for add dots on bottom
    private void addBottomDots(int currentPage) {
        dots = new TextView[adsArrayList.size()];

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorGray));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorWhite));
    }

    //Method to set timer to slider
    private void SetSliderAutoTimer() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                try {
                    viewPager.setCurrentItem(currentPage++, true);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);
    }

    //Method to Get Category List
    private void GetCategoryList() {

        UtilityMethods.tuchOff(relativeLayoutHome);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CATEGORY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "GetCategoryList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String path = jsonObject.getString("path");

                        JSONArray jsonArraycategory = jsonObject.getJSONArray("category");
                        mCategoryModelArrayList.clear();
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String cat_name = jsonObjectcategory.getString("cat_name");
                            String cat_image = jsonObjectcategory.getString("cat_image");

                            CategoryModel categoryModel = new CategoryModel(pk_id, cat_name, path + "" + cat_image);
                            mCategoryModelArrayList.add(categoryModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterHomePageItemList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutHome);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutHome);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                Log.e("check", "======>" + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method for get Adv Banner
    public void getBanners() {
        UtilityMethods.tuchOff(relativeLayoutHome);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_BANNER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("test", "response getBanners -->>" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String error_code = jsonObject.getString("error_code");


                    adsArrayList.clear();
                    myViewPagerAdapter.notifyDataSetChanged();


                    myViewPagerAdapter = new MyViewPagerAdapter(getActivity(), adsArrayList);
                    viewPager.setClipToPadding(false);
                    viewPager.setPadding(30, 0, 30, 0);
                    viewPager.setAdapter(myViewPagerAdapter);
                    viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


                    if (error_code.equalsIgnoreCase("1")) {

                        String banner_path = jsonObject.getString("banner_path");

                        JSONArray bannerlistJsonArray = jsonObject.getJSONArray("bannerlist");
                        for (int i = 0; i < bannerlistJsonArray.length(); i++) {

                            JSONObject jsonObjectBannerlist = bannerlistJsonArray.getJSONObject(i);

                            String pk_id = jsonObjectBannerlist.getString("pk_id");
                            String img = jsonObjectBannerlist.getString("img");
                            String status = jsonObjectBannerlist.getString("status");
                            String url = jsonObjectBannerlist.getString("url");
                            String price = jsonObjectBannerlist.getString("price");
                            String fromDate = jsonObjectBannerlist.getString("fromDate");
                            String toDate = jsonObjectBannerlist.getString("toDate");

                            AdsItem adsItem = new AdsItem(pk_id, banner_path + img, status, url, price, fromDate, toDate);
                            adsArrayList.add(adsItem);

                        }

                    } else if (error_code.equalsIgnoreCase("2")) {


                    } else if (error_code.equalsIgnoreCase("3")) {

                    } else if (error_code.equalsIgnoreCase("4")) {

                        adsArrayList.clear();

                    } else if (error_code.equalsIgnoreCase("5")) {

                    } else if (error_code.equalsIgnoreCase("10")) {
                        UtilityMethods.UserInactivePopup(getActivity());

                    } else if (error_code.equalsIgnoreCase("0")) {


                    }

                    myViewPagerAdapter.notifyDataSetChanged();


                    if (adsArrayList.size() == 0) {

                        //  relativeLayoutViewPager.setVisibility(GONE);
                        cardSlider.setVisibility(GONE);
                    } else {

                        //  relativeLayoutViewPager.setVisibility(VISIBLE);
                        cardSlider.setVisibility(VISIBLE);

                        //notify banner and set banner
                        NUM_PAGES = adsArrayList.size();
                        //popularVideoViewPagerHomeFragment.getAdapter().notifyDataSetChanged();
                        myViewPagerAdapter.notifyDataSetChanged();
                        addBottomDots(0);
                        //popularVideoViewPagerHomeFragment.setAdapter(myViewPagerPopularVideoAdapter);
                        SetSliderAutoTimer();


//new slider

                        ArrayList<AdvCategoryModel> adsArrayListinflate = new ArrayList();

                        for (int h = 0; h < adsArrayList.size(); h++) {
                            AdvCategoryModel advCategoryModel = new AdvCategoryModel("", adsArrayList.get(h).getImg(), adsArrayList.get(h).getUrl());

                            adsArrayListinflate.add(advCategoryModel);

                        }


                        SliderAdapterExample adapter = new SliderAdapterExample(getActivity(), adsArrayListinflate);

                        imageSlider.setSliderAdapter(adapter);

                        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        imageSlider.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        imageSlider.setIndicatorSelectedColor(Color.WHITE);
                        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                        imageSlider.startAutoCycle();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //UtilityMethods.showInfoToast(getActivity(), "Server Error");

                }

                UtilityMethods.tuchOn(relativeLayoutHome);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(getActivity(), "Sorry, Please Check your Internet Connection.");

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, AuthFailureError.");
                    } else if (error instanceof ServerError) {

                        // UtilityMethods.showInfoToast(getActivity(), "Sorry, ServerError.");
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, NetworkError.");
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                UtilityMethods.tuchOn(relativeLayoutHome);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("cid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY));
                    params.put("cityid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY));

                } else {

                    params.put("cid", mSessionManager.getStringData(Constants.KEY_BUYER_COUNTRY));
                    params.put("cityid", mSessionManager.getStringData(Constants.KEY_BUYER_CITY));

                }


                Log.e("test", "getBanners-->>" + params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //mRequestQueue.add(stringRequest);

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(
                120000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void requestHomePostRequirements() {
        UtilityMethods.tuchOff(relativeLayoutHome);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_HOME_REQUIREMENT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "URL_GET_HOME_REQUIREMENT_LIST -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {
                        String base_url = "";
                        if (!jsonObject.isNull("base_url")){
                            base_url = jsonObject.getString("base_url");
                        }
                        JSONArray post_enq_list = jsonObject.getJSONArray("post_enq_list");
                        List<HomePostEnquiryModel> postEnquiryModelList = new ArrayList<>();
                        for (int i = 0; i < post_enq_list.length(); i++) {

                            JSONObject jsonObjectcategory = post_enq_list.getJSONObject(i);
                            HomePostEnquiryModel postEnquiryModel = new HomePostEnquiryModel();
                            postEnquiryModel.setQty(jsonObjectcategory.getString("qty"));
                            postEnquiryModel.setEnquiry_type(jsonObjectcategory.getString("enquiry_type"));
                            postEnquiryModel.setCat_image_path(base_url + jsonObjectcategory.getString("cat_image_path"));
                            postEnquiryModelList.add(postEnquiryModel);

                        }

                        setHomePostRequirements(postEnquiryModelList);
                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutHome);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutHome);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("buyer")) {
                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));
                } else {
                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                }
                Log.e("check", "URL_GET_HOME_REQUIREMENT_LIST ======>" + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    HomePostRequirementAdapter homePostRequirementAdapter;
    int count = 0;
    int[] prevPos = {-1};
    Handler handler;
    Runnable runnable = null;

    private void setHomePostRequirements(List<HomePostEnquiryModel> postEnquiryModelList) {
        homePostRequirementAdapter = new HomePostRequirementAdapter(postEnquiryModelList);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//                super.smoothScrollToPosition(recyclerView, state, position);
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 500f;// Change this value (default=25f)
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPostReq.setLayoutManager(layoutManager);
        rvPostReq.setAdapter(homePostRequirementAdapter);
        homePostRequirementAdapter.notifyDataSetChanged();
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvPostReq);
        rvPostReq.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            int count = 0;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try{
                    count = getCurrentItem();
                }catch (Exception e){e.printStackTrace();}
            }

        });
        autoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (null != handler && null != runnable) {
                handler.removeCallbacks(runnable);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            autoScroll();
        }catch (Exception e){e.printStackTrace();}
    }

    private int getCurrentItem(){
        try {
            return ((LinearLayoutManager) rvPostReq.getLayoutManager())
                    .findFirstVisibleItemPosition();
        }catch (Exception e){e.printStackTrace();}
        return 0;
    }

    public void autoScroll(){
//        Log.e("AUTOSCROLL","home");
        if (true){
            return;
        }
        try {
            if (null == handler){
                handler = new Handler();
            }
            final int speedScroll = 5000;
            runnable = new Runnable() {
                @Override
                public void run() {
//                    Log.e("POSITION", ""+ count);
//                    Log.e("POSITION", "prevPos : "+ prevPos[0]);
                    if (prevPos[0] == count){

                    }else {
                        if (count == homePostRequirementAdapter.getItemCount()) {
                            count = 0;
                            prevPos[0] = -1;
                        }
                        if (count < homePostRequirementAdapter.getItemCount()) {
                            if (null != rvPostReq) {
                                prevPos[0] = count;
                                rvPostReq.smoothScrollToPosition(++count);
                                handler.postDelayed(this, speedScroll);
                            }
                        }
                    }
                }
            };
            handler.postDelayed(runnable, speedScroll);
        }catch (Exception e){e.printStackTrace();}
    }
}
