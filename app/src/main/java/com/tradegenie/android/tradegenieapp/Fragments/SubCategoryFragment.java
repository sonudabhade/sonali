package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterAdvCategoryViewPager;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSpinnerSubCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSubCategoryItemList;
import com.tradegenie.android.tradegenieapp.Adapters.SliderAdapterExample;
import com.tradegenie.android.tradegenieapp.Models.AdvCategoryModel;
import com.tradegenie.android.tradegenieapp.Models.SubCategoryModel;
import com.tradegenie.android.tradegenieapp.Models.SubSubCategoryModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SubCategoryFragment extends Fragment {


    @BindView(R.id.reyclerview_category)
    RecyclerView reyclerviewCategory;
    Unbinder unbinder;
    @BindView(R.id.spinner_sub_category)
    Spinner spinnerSubCategory;
    @BindView(R.id.ProgressBarSubSubCategoryFragment)
    RelativeLayout ProgressBarSubSubCategoryFragment;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.iv_category)
    ImageView ivCategory;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.relative_layout_view_pager)
    RelativeLayout relativeLayoutViewPager;
    @BindView(R.id.imageSlider)
    SliderView imageSlider;
    @BindView(R.id.cardSlider)
    CardView cardSlider;
    @BindView(R.id.iv_view_two)
    View ivViewTwo;
    @BindView(R.id.iv_view)
    View ivView;
    @BindView(R.id.iv_drop_down)
    ImageView ivDropDown;
    @BindView(R.id.rel_spinner_sub_category)
    RelativeLayout relSpinnerSubCategory;

    private SessionManager mSessionManager;

    private ArrayList<SubCategoryModel> mSubCategoryModelArrayList;
    private AdapterSpinnerSubCategoryList mAdapterSpinnerSubCategoryList;
    private String mSelectedSubCategory;

    private ArrayList<SubSubCategoryModel> mSubSubCategoryModelArrayList;
    private AdapterSubCategoryItemList mAdapterSubCategoryItemList;

    private String mSelectedCategory = "";

    private ArrayList<AdvCategoryModel> adsArrayList;
    private AdapterAdvCategoryViewPager mAdapterAdvCategoryViewPager;
    private TextView dots[];
    int NUM_PAGES, currentPage;
    Timer timer;

    public SubCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        unbinder = ButterKnife.bind(this, view);

        Constants.mMainActivity.setToolBarName("");
        Constants.mMainActivity.setToolBar(GONE, GONE, VISIBLE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());


        if (mSelectedCategory.equals("")) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mSelectedSubCategory = bundle.getString("mSelectedSubCategory");
                mSelectedCategory = bundle.getString("mSelectedCategory");

            }
        }


        adsArrayList = new ArrayList();
        mAdapterAdvCategoryViewPager = new AdapterAdvCategoryViewPager(getActivity(), adsArrayList);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(30, 0, 30, 0);
        viewPager.setAdapter(mAdapterAdvCategoryViewPager);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        // adding bottom dots
        NUM_PAGES = adsArrayList.size();
        addBottomDots(0);
        //SetSliderAutoTimer();


        mSubCategoryModelArrayList = new ArrayList<>();
        mAdapterSpinnerSubCategoryList = new AdapterSpinnerSubCategoryList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mSubCategoryModelArrayList);
        spinnerSubCategory.setAdapter(mAdapterSpinnerSubCategoryList);
        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mSubCategoryModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedSubCategory = mSubCategoryModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSelectedCategory----->" + mSelectedSubCategory);
                    try {
                        Picasso.get().load(mSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.ic_launcher_foreground).into(ivCategory);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    setAdapter();

                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        getSubSubcategoryAttribute();

                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }

//                    if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
//                    {
//
//                        getAdvBanners();
//
//                    } else {
//
//                        UtilityMethods.showInternetDialog(getActivity());
//                    }
                } else {
                    //mSelectedSubCategory = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setAdapter();


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {
            getSubcategoryAttribute();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
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

    @OnClick(R.id.rel_spinner_sub_category)
    public void onViewClicked() {

        spinnerSubCategory.performClick();
    }

    //Method to get Subcategory Attribute
    private void getSubcategoryAttribute() {

        UtilityMethods.tuchOff(ProgressBarSubSubCategoryFragment);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_SUBCATEGORY_ATTRIBUTE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSubcategoryAttribute -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String path = jsonObject.getString("path");

                        JSONArray jsonArraycategory = jsonObject.getJSONArray("Subcategory");
                        mSubCategoryModelArrayList.clear();
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String title = jsonObjectcategory.getString("title");
                            String subcat_image = jsonObjectcategory.getString("subcat_image");

                            SubCategoryModel subCategoryModel = new SubCategoryModel(pk_id, title, path + "" + subcat_image);
                            mSubCategoryModelArrayList.add(subCategoryModel);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterSpinnerSubCategoryList.notifyDataSetChanged();

                    for (int i = 0; i < mSubCategoryModelArrayList.size(); i++) {

                        Log.e("spinner", "compare" + mSubCategoryModelArrayList.get(i).getPk_id() + "====" + mSelectedCategory);
                        if (mSubCategoryModelArrayList.get(i).getPk_id().equals(mSelectedSubCategory)) {

                            spinnerSubCategory.setSelection(i);

                            try {
                                Picasso.get().load(mSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.ic_launcher_foreground).into(ivCategory);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        }

                    }

                    if (mSubCategoryModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);

                    } else {

                        textViewErrorMessage.setVisibility(GONE);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                UtilityMethods.tuchOn(ProgressBarSubSubCategoryFragment);
//                if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
//                {
//                    getSubSubcategoryAttribute();
//
//                } else {
//
//                    UtilityMethods.showInternetDialog(getActivity());
//                }



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
                UtilityMethods.tuchOn(ProgressBarSubSubCategoryFragment);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("category_id", mSelectedCategory);

                Log.e("URL", "======>" + Constants.URL_GET_SUBCATEGORY_ATTRIBUTE);
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

    //Method to get Subcategory Attribute
    private void getSubSubcategoryAttribute() {

        UtilityMethods.tuchOff(ProgressBarSubSubCategoryFragment);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUB_SUB_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSubSubcategoryAttribute -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String path = jsonObject.getString("path");

                        JSONArray jsonArraycategory = jsonObject.getJSONArray("Sub Subcategory");
                        mSubSubCategoryModelArrayList.clear();
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String title = jsonObjectcategory.getString("title");
                            String subcat_image = jsonObjectcategory.getString("subcat_image");

                            SubSubCategoryModel subSubCategoryModel = new SubSubCategoryModel(pk_id, title, path + "" + subcat_image);
                            mSubSubCategoryModelArrayList.add(subSubCategoryModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterSubCategoryItemList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

//                UtilityMethods.tuchOn(ProgressBarSubSubCategoryFragment);
//                if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
//                {

                    getAdvBanners();

//                } else {
//
//                    UtilityMethods.showInternetDialog(getActivity());
//                }
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
                UtilityMethods.tuchOn(ProgressBarSubSubCategoryFragment);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("subcategory_id", mSelectedSubCategory);

                Log.e("URL", "======>" + Constants.URL_SUB_SUB_CATEGORY);
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

    private void setAdapter() {

        mSubSubCategoryModelArrayList = new ArrayList<>();
        mAdapterSubCategoryItemList = new AdapterSubCategoryItemList(getActivity(), mSubSubCategoryModelArrayList, mSelectedCategory, mSelectedSubCategory);
        reyclerviewCategory.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        reyclerviewCategory.setAdapter(mAdapterSubCategoryItemList);

    }

    /**
     * Get banner from web service
     */
    public void getAdvBanners() {
        UtilityMethods.tuchOff(ProgressBarSubSubCategoryFragment);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUB_CATEGORY_ADV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("test", "response getAdvBanners -->>" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String error_code = jsonObject.getString("error_code");


                    adsArrayList.clear();
                    if (error_code.equalsIgnoreCase("1")) {

                        String img_path = jsonObject.getString("img_path");

                        JSONArray bannerlistJsonArray = jsonObject.getJSONArray("cat_advertise_img");
                        for (int i = 0; i < bannerlistJsonArray.length(); i++) {

                            JSONObject jsonObjectBannerlist = bannerlistJsonArray.getJSONObject(i);

                            String advertiser_name = jsonObjectBannerlist.getString("advertiser_name");
                            String img = jsonObjectBannerlist.getString("img");
                            String url = jsonObjectBannerlist.getString("url");


                            AdvCategoryModel advCategoryModel = new AdvCategoryModel(advertiser_name, img_path + img, url);
                            adsArrayList.add(advCategoryModel);

                        }

                    } else if (error_code.equalsIgnoreCase("2")) {


                    } else if (error_code.equalsIgnoreCase("3")) {

                    } else if (error_code.equalsIgnoreCase("4")) {
                    } else if (error_code.equalsIgnoreCase("5")) {

                    } else if (error_code.equalsIgnoreCase("10")) {
                        //UtilityMethods.UserInactivePopup(getActivity());


                    } else if (error_code.equalsIgnoreCase("0")) {


                        UtilityMethods.showInfoToast(getActivity(), "Banner not found");

                    }

                    if (adsArrayList.size() == 0) {

                      //  relativeLayoutViewPager.setVisibility(GONE);
                        cardSlider.setVisibility(GONE);

                    } else {

                       // relativeLayoutViewPager.setVisibility(VISIBLE);
                        cardSlider.setVisibility(VISIBLE);

                        //notify banner and set banner
                        NUM_PAGES = adsArrayList.size();
                        //popularVideoViewPagerHomeFragment.getAdapter().notifyDataSetChanged();
                        mAdapterAdvCategoryViewPager.notifyDataSetChanged();
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

                UtilityMethods.tuchOn(ProgressBarSubSubCategoryFragment);
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
                UtilityMethods.tuchOn(ProgressBarSubSubCategoryFragment);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("test", "otp verification Params-->>" + params.toString());

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                } else {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }

                params.put("cid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY));
                params.put("cityid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY));                //params.put("catid", mSelectedCategory);
                params.put("catid", mSelectedCategory);
                //params.put("subcatid", mSelectedSubCategory);
                params.put("subcatid", mSelectedSubCategory);

                Log.e("URL", "params----->" + Constants.URL_SUB_CATEGORY_ADV);
                Log.e("params", "params----->" + params.toString());
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


}
