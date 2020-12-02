package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Activity.OnBackPressedListener;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterProductImageView;
import com.tradegenie.android.tradegenieapp.Adapters.ProductImageMyViewPagerAdapter;
import com.tradegenie.android.tradegenieapp.Adapters.SliderAdapterExample;
import com.tradegenie.android.tradegenieapp.Models.AdvCategoryModel;
import com.tradegenie.android.tradegenieapp.Models.ProductImageViewModel;
import com.tradegenie.android.tradegenieapp.Models.ProductListBuyModel;
import com.tradegenie.android.tradegenieapp.Models.ProductSpecificationModel;
import com.tradegenie.android.tradegenieapp.Models.ViewPagerProductImageModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProductViewFragment extends Fragment implements OnBackPressedListener {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_product_image)
    RecyclerView recyclerViewProductImage;
    @BindView(R.id.Linear_layout_enquiry)
    LinearLayout LinearLayoutEnquiry;
    @BindView(R.id.iv_main_product_image)
    ImageView ivMainProductImage;
    /*@BindView(R.id.Linear_layout_enquiry_one)
    LinearLayout LinearLayoutEnquiryOne;
    @BindView(R.id.Linear_layout_enquiry_two)
    LinearLayout LinearLayoutEnquiryTwo;
    @BindView(R.id.Linear_layout_enquiry_three)
    LinearLayout LinearLayoutEnquiryThree;
    @BindView(R.id.Linear_layout_view)
    LinearLayout LinearLayoutView;
    @BindView(R.id.Linear_layout_view2)
    LinearLayout LinearLayoutView2;
    @BindView(R.id.Linear_layout_view3)
    LinearLayout LinearLayoutView3;*/
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.relativeLayoutProductView)
    RelativeLayout relativeLayoutProductView;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_company_city)
    TextView tvCompanyCity;
    @BindView(R.id.tv_used_type)
    TextView tvUsedType;
    @BindView(R.id.tv_mode_of_payment)
    TextView tvModeOfPayment;
    @BindView(R.id.tv_mode_of_supply)
    TextView tvModeOfSupply;
    @BindView(R.id.Linear_layout_brochure)
    LinearLayout LinearLayoutBrochure;
    @BindView(R.id.linear_layout_use_type)
    TextView linearLayoutUseType;
    @BindView(R.id.tv_short_description)
    TextView tvShortDescription;
    @BindView(R.id.tv_long_description)
    TextView tvLongDescription;
    @BindView(R.id.tv_additional_features)
    TextView tvAdditionalFeatures;
    @BindView(R.id.linearLayoutVideoView)
    LinearLayout linearLayoutVideoView;
    @BindView(R.id.tv_Delivery_time)
    TextView tvDeliveryTime;
    @BindView(R.id.tv_packing_details)
    TextView tvPackingDetails;
    @BindView(R.id.tv_product_capacity)
    TextView tvProductCapacity;
    @BindView(R.id.tv_port_of_supply)
    TextView tvPortOfSupply;
    @BindView(R.id.tv_minimum_order_quantity)
    TextView tvMinimumOrderQuantity;
    @BindView(R.id.tv_sample_piece)
    TextView tvSamplePiece;
    @BindView(R.id.linear_layout_delivery_time)
    LinearLayout linearLayoutDeliveryTime;
    @BindView(R.id.linear_layout_packing_details)
    LinearLayout linearLayoutPackingDetails;
    @BindView(R.id.linear_layout_product_capacity)
    LinearLayout linearLayoutProductCapacity;
    @BindView(R.id.linear_layout_port_of_supply)
    LinearLayout linearLayoutPortOfSupply;
    @BindView(R.id.linear_layout_minimum_order_qnt)
    LinearLayout linearLayoutMinimumOrderQnt;
    @BindView(R.id.linear_layout_sample_piece)
    LinearLayout linearLayoutSamplePiece;
    @BindView(R.id.linear_layout_description)
    LinearLayout linearLayoutDescription;
    @BindView(R.id.linear_layout_additional_feature)
    LinearLayout linearLayoutAdditionalFeature;
    @BindView(R.id.linear_layout_youtube)
    LinearLayout linearLayoutYoutube;
    @BindView(R.id.iv_favourite)
    ImageView ivFavourite;
    @BindView(R.id.rel_location)
    RelativeLayout relLocation;
    @BindView(R.id.tv_mode_of_payment_title)
    TextView tvModeOfPaymentTitle;
    @BindView(R.id.linear_layout_mode_of_payment)
    LinearLayout linearLayoutModeOfPayment;
    @BindView(R.id.tv_mode_of_supply_title)
    TextView tvModeOfSupplyTitle;
    @BindView(R.id.linear_layout_mode_of_supply)
    LinearLayout linearLayoutModeOfSupply;
    @BindView(R.id.linear_layout_location)
    LinearLayout linearLayoutLocation;
    @BindView(R.id.linear_layout_seller_profile)
    RelativeLayout linearLayoutSellerProfile;
    @BindView(R.id.iv_company_icon)
    ImageView ivCompanyIcon;

    @BindView(R.id.Lnr_specification)
    LinearLayout LnrSpecification;

    @BindView(R.id.Lnr_other_details)
    LinearLayout LnrOtherDetails;
    @BindView(R.id.youtube_view)
    YouTubePlayerView youtubeView;
    @BindView(R.id.LnrUserPost)
    LinearLayout LnrUserPost;
    @BindView(R.id.tvCountryName)
    TextView tvCountryName;
    @BindView(R.id.tv_product_category)
    TextView tvProductCategory;
    @BindView(R.id.tv_view_more)
    TextView tvViewMore;


    private SessionManager mSessionManager;
    private DownloadManager downloadManager;
    private String productid;
    private int current_pos = 0;
    private String upload_brochure;
    private String yotube_link;
    private String fav_status = "";
    private String seller_fkid = "";

    ArrayList<ProductListBuyModel> mProductListBuyModelArrayList;

    private ArrayList<ProductImageViewModel> mProductImageViewModelArrayList;
    private AdapterProductImageView mAdapterProductImageView;

    public LinearLayoutManager linearLayoutManager;


    private ArrayList<ViewPagerProductImageModel> mViewPagerProductImageModelArrayList;
    private ProductImageMyViewPagerAdapter mProductImageMyViewPagerAdapter;


    private ArrayList<ProductSpecificationModel> mProductSpecificationModelArrayList;
    private boolean isFromList = false;

    public ProductViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Product Details");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


        mProductImageViewModelArrayList = new ArrayList<>();
        mViewPagerProductImageModelArrayList = new ArrayList<>();
        mAdapterProductImageView = new AdapterProductImageView(getActivity(), mProductImageViewModelArrayList, ProductViewFragment.this);
        mProductImageMyViewPagerAdapter = new ProductImageMyViewPagerAdapter(getActivity(), mViewPagerProductImageModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewProductImage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProductImage.setAdapter(mAdapterProductImageView);
        recyclerViewProductImage.setLayoutManager(linearLayoutManager);

        mProductSpecificationModelArrayList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productid = bundle.getString("PK_id");
            isFromList = bundle.getBoolean("isFromList");

        }

        LinearLayoutBrochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDownlodFile(upload_brochure);

            }
        });
        setUnderline();

        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {
            getProductDetails();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }

    private void setUnderline() {
        SpannableString contentRatingAvg = new SpannableString(tvViewMore.getText().toString());
        contentRatingAvg.setSpan(new UnderlineSpan(), 0, contentRatingAvg.length(), 0);
        tvViewMore.setText(contentRatingAvg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.iv_main_product_image)
    public void onViewClicked() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = mInflater.inflate(R.layout.dialog_product_image_view, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.show();


        ViewPager viewPager = dialogView.findViewById(R.id.view_pager);


        viewPager.setClipToPadding(false);
        viewPager.setAdapter(mProductImageMyViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setCurrentItem(current_pos);


    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            //addBottomDots(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @OnClick(R.id.Linear_layout_enquiry)
    public void onViewClickeded() {

        EnquiryFragment enquiryFragment = new EnquiryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PK_id", productid);
        enquiryFragment.setArguments(bundle);
        Constants.mMainActivity.ChangeFragments(enquiryFragment, "EnquiryFragment");


    }


    public void ChangeImage(int position) {

        // ivMainProductImage.setImageResource(mProductImageViewModelArrayList.get(position).getImage());

        current_pos = position;

        try {
            Picasso.get().load(mProductImageViewModelArrayList.get(position).getImage()).placeholder(R.drawable.ic_launcher_foreground).into(ivMainProductImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.iv_share, R.id.iv_favourite, R.id.tv_company_name, R.id.iv_company_icon, R.id.tv_view_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.iv_share:

                //shareapp();
                //UtilityMethods.showInfoToast(getActivity(), "Coming Soon");
                Log.e("Image", "Image " + mProductImageViewModelArrayList.get(0).getImage());

                if (mProductImageViewModelArrayList.get(0).getImage().equals("")) {

                    try {
                        Intent shareintent = new Intent(Intent.ACTION_SEND);
                        shareintent.setType("text/plain");
                        shareintent.putExtra(Intent.EXTRA_SUBJECT, "Trade Genie");
                        //String sAux = "Let me recommend you this application.This isonline learning app.Enter my code " +referral_code+ " to earn.\n\n";
                        String sAux = "TradeGenie\n\n";
                        sAux = sAux + tvProductName.getText().toString().trim() + "\n";
                        sAux = sAux + tvShortDescription.getText().toString().trim() + "\n\n";
                        sAux = sAux + "https://play.google.com/store/apps/details?id=com.tradegenie.android.tradegenieapp&hl=en \n\n";
                        shareintent.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(shareintent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                } else {

                    UtilityMethods.tuchOff(relativeLayoutProductView);
                    try {


                        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner(mProductImageViewModelArrayList.get(0).getImage());
                        asyncTaskRunner.execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

          /*  case R.id.tv_youtube_link:

                if (yotube_link.toString().contains("=")) {

                    String part0 = null; // this will contain "Fruit"
                    String part1 = null;
                    try {
                        String url = yotube_link.toString();
                        part0 = null;
                        String video_code1 = null;
                        String[] separated = url.split("=");
                        part0 = separated[0];
                        part1 = null;
                        part1 = separated[1];
                    } catch (Exception e) {
                        e.printStackTrace();

                        UtilityMethods.showInfoToast(getActivity(), "Please enter valid url");


                    }


                    Intent i = new Intent(getActivity(), YoutubeActivity.class);
                    Bundle bundle = new Bundle();

                    Log.e("check", "youtube part0==>" + part0);
                    Log.e("check", "youtube part1==>" + part1);

                    bundle.putString("video", part1);
                    i.putExtras(bundle);
                    startActivity(i);


                } else if (yotube_link.toString().contains("/")) {

                    String part0 = null; // this will contain "Fruit"
                    String video_code1 = null;
                    try {
                        String url = yotube_link.toString();
                        part0 = null;
                        video_code1 = null;
                        String[] separated = url.split("//");
                        part0 = separated[0];
                        String part1 = null;
                        part1 = separated[1];
                        String[] video_code_url = part1.split("/");
                        String video_code0 = video_code_url[0];
                        video_code1 = video_code_url[1];
                    } catch (Exception e) {
                        e.printStackTrace();

                        UtilityMethods.showInfoToast(getActivity(), "Please enter valid url");


                    }


                    Intent i = new Intent(getActivity(), YoutubeActivity.class);
                    Bundle bundle = new Bundle();

                    Log.e("check", "youtube part0" + part0);
                    Log.e("check", "youtube part1" + video_code1);

                    bundle.putString("video", video_code1);
                    i.putExtras(bundle);
                    startActivity(i);

                } else {

                    UtilityMethods.showInfoToast(getActivity(), "Please enter valid url");

                }


                break;
*/
            case R.id.iv_favourite:


                if (fav_status.equals("1")) {

                    ivFavourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_inactive));
                    fav_status = "2";
                    CallSetFavourite(productid);

                } else {

                    ivFavourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_active));
                    fav_status = "1";
                    CallSetFavourite(productid);

                }


                break;

            case R.id.tv_company_name:

                SellerProfileFragment sellerProfileFragment = new SellerProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("productid", productid);
                bundle.putString("seller_fkid", seller_fkid);
                sellerProfileFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(sellerProfileFragment, "SellerProfileFragmentz");

                break;

            case R.id.iv_company_icon:

                SellerProfileFragment sellerProfileFragment1 = new SellerProfileFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("productid", productid);
                bundle1.putString("seller_fkid", seller_fkid);
                sellerProfileFragment1.setArguments(bundle1);
                Constants.mMainActivity.ChangeFragments(sellerProfileFragment1, "SellerProfileFragmentz");

                break;

            case R.id.tv_view_more:

                SellerProfileFragment sellerProfileFragment2 = new SellerProfileFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("productid", productid);
                bundle2.putString("seller_fkid", seller_fkid);
                sellerProfileFragment2.setArguments(bundle2);
                Constants.mMainActivity.ChangeFragments(sellerProfileFragment2, "SellerProfileFragmentz");

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromList && null != Constants.mMainActivity.productListBuyFragment) {
            Constants.mMainActivity.productListBuyFragment.onBackPressOfProductView();
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String Imageurl;

        public AsyncTaskRunner(String Imageurl) {
            this.Imageurl = Imageurl;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL(Imageurl);
                Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                Log.e("keshav", "Bitmap " + myBitmap);

                if (myBitmap == null) {
                    UtilityMethods.showToast(getActivity(), "Image bitmap is null.");
                } else {
                    shareapp(myBitmap);
                }

            } catch (IOException e) {
                Log.e("keshav", "Exception " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            UtilityMethods.tuchOn(relativeLayoutProductView);
        }
    }


    //Share app menthod
    public void shareapp(Bitmap bitmap) {


        String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
        Uri bitmapUri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        String sAux = "TradeGenie\n";
        sAux = sAux + tvProductName.getText().toString().trim() + "\n";
        sAux = sAux + tvShortDescription.getText().toString().trim() + "\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.tradegenie.android.tradegenieapp&hl=en \n\n";

        intent.putExtra(Intent.EXTRA_TEXT, sAux);

        startActivity(Intent.createChooser(intent, "Share"));




      /*  try {
            Intent shareintent = new Intent(Intent.ACTION_SEND);
            shareintent.setType("text/plain");
            shareintent.putExtra(Intent.EXTRA_SUBJECT, "Trade Genie");
            //String sAux = "Let me recommend you this application.This isonline learning app.Enter my code " +referral_code+ " to earn.\n\n";
            String sAux = "TradeGenie\n";
            sAux = sAux +tvShortDescription.getText().toString().trim()+ "\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.tradegenie.android.tradegenieapp&hl=en \n\n";
            shareintent.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(shareintent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
*/
    }

    //Method to get Product Details
    private void getProductDetails() {

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UtilityMethods.tuchOff(relativeLayoutProductView);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PRODUCT_VIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getProductDetails" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String baseurlproductimages = jsonObject.getString("productimages");
                        seller_fkid = jsonObject.getString("seller_fkid");

                        JSONArray jsonArrayproductimagesname = jsonObject.getJSONArray("productimages_name");


                        if (jsonArrayproductimagesname.length() != 0) {

                            for (int i = 0; i < jsonArrayproductimagesname.length(); i++) {


                                JSONObject jsonObjectproductimagesname = jsonArrayproductimagesname.getJSONObject(i);

                                String pk_id = jsonObjectproductimagesname.getString("pk_id");
                                String image_name = baseurlproductimages + jsonObjectproductimagesname.getString("image_name");


                                ProductImageViewModel productImageViewModel = new ProductImageViewModel(pk_id, image_name);
                                mProductImageViewModelArrayList.add(productImageViewModel);

                                ViewPagerProductImageModel viewPagerProductImageModel = new ViewPagerProductImageModel(pk_id, image_name);
                                mViewPagerProductImageModelArrayList.add(viewPagerProductImageModel);
                            }
                        } else {

                            ProductImageViewModel productImageViewModel = new ProductImageViewModel("", "");
                            mProductImageViewModelArrayList.add(productImageViewModel);

                            ViewPagerProductImageModel viewPagerProductImageModel = new ViewPagerProductImageModel("", "");
                            mViewPagerProductImageModelArrayList.add(viewPagerProductImageModel);

                            ivMainProductImage.setImageResource(R.drawable.default_add_image);

                        }

                        fav_status = jsonObject.getString("fav_status");


                        final String favorite = jsonObject.getString("fav_status");

                        if (favorite.equals("1")) {

                            try {
                                Picasso.get().load(R.drawable.ic_favourite_active).placeholder(R.drawable.ic_favourite_active).into(ivFavourite);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                Picasso.get().load(R.drawable.ic_favourite_inactive).placeholder(R.drawable.ic_favourite_inactive).into(ivFavourite);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        yotube_link = jsonObject.getString("yotube_link");

                        if (yotube_link.toString().contains("=")) {

                            linearLayoutYoutube.setVisibility(VISIBLE);


                        } else if (yotube_link.toString().contains("/")) {

                            linearLayoutYoutube.setVisibility(VISIBLE);


                        } else {

                            linearLayoutYoutube.setVisibility(GONE);

                        }

                        final String[] VideoId = {""};
                        try {

                            youtubeView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(

                                    new AbstractYouTubePlayerListener() {

                                        @Override
                                        public void onReady() {

                                            // initializedYouTubePlayer.loadVideo(mYoutubeVideo.getVideoId(), 0);


                                            if (yotube_link.contains("watch?v=")) {

                                                VideoId[0] = yotube_link.substring(yotube_link.indexOf("watch?v=") + 8);

                                                Log.e("VideoId", "contains VideoId " + VideoId[0]);

                                            } else {


                                                VideoId[0] = yotube_link.substring(yotube_link.lastIndexOf("/") + 1);

                                                Log.e("VideoId", " VideoId " + VideoId[0]);
                                            }
                                            //   initializedYouTubePlayer.loadVideo("H_YiMco9KUQ", 0);


                                            initializedYouTubePlayer.loadVideo(VideoId[0], 0);

                                            try {

                                                if (initializedYouTubePlayer != null) {

                                                    initializedYouTubePlayer.pause();

                                                    if (initializedYouTubePlayer.addListener(new YouTubePlayerListener() {

                                                        @Override
                                                        public void onReady() {

                                                            System.out.println("initializedYouTubePlayer onReady - >>> ");

                                                            initializedYouTubePlayer.play();
                                                        }

                                                        @Override
                                                        public void onStateChange(int state) {

                                                        }

                                                        @Override
                                                        public void onPlaybackQualityChange(@NonNull String playbackQuality) {

                                                        }

                                                        @Override
                                                        public void onPlaybackRateChange(@NonNull String playbackRate) {

                                                        }

                                                        @Override
                                                        public void onError(int error) {

                                                        }

                                                        @Override
                                                        public void onApiChange() {

                                                        }

                                                        @Override
                                                        public void onCurrentSecond(float second) {

                                                        }

                                                        @Override
                                                        public void onVideoDuration(float duration) {

                                                        }

                                                        @Override
                                                        public void onVideoLoadedFraction(float loadedFraction) {

                                                        }

                                                        @Override
                                                        public void onVideoId(@NonNull String videoId) {

                                                        }
                                                    })) ;

                                                }
                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                        }
                                    }), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        tvProductName.setText(jsonObject.getString("product_name"));
                        tvCompanyName.setText(jsonObject.getString("comp_name"));
                        tvCountryName.setText(jsonObject.getString("country_name"));
                        tvUsedType.setText(jsonObject.getString("sales_type"));
                        if (jsonObject.getString("sales_type").equals("")) {
                            linearLayoutUseType.setVisibility(GONE);
                            tvUsedType.setVisibility(GONE);
                        }

                        if (jsonObject.getString("bussiness_location").equals("")) {

                            linearLayoutLocation.setVisibility(GONE);

                        } else {

                            tvCompanyCity.setText(jsonObject.getString("bussiness_location"));


                        }

                        if (jsonObject.getString("cost").equals("")) {

                            tvPrice.setVisibility(GONE);

                        } else {

                            tvPrice.setText(jsonObject.getString("currency") + " " + jsonObject.getString("cost"));

                        }

                        boolean ISDeliveryTimeAvailable = false;
                        boolean ISPackingDetailsAvailable = false;
                        boolean ISProductCapacityAvailable = false;
                        boolean ISPortOfSupplyAvailable = false;
                        boolean ISMinimumOrderQntAvailable = false;
                        boolean ISSamplePieceAvailable = false;


                        if (jsonObject.getString("delivery_time").equals("")) {

                            ISDeliveryTimeAvailable = false;
                            linearLayoutDeliveryTime.setVisibility(GONE);

                        } else {
                            ISDeliveryTimeAvailable = true;
                            tvDeliveryTime.setText(jsonObject.getString("delivery_time"));


                        }


                        if (jsonObject.getString("packing_details").equals("")) {
                            ISPackingDetailsAvailable = false;
                            linearLayoutPackingDetails.setVisibility(GONE);


                        } else {
                            ISPackingDetailsAvailable = true;
                            tvPackingDetails.setText(jsonObject.getString("packing_details"));


                        }


                        if (jsonObject.getString("production_capacity").equals("")) {
                            ISProductCapacityAvailable = false;
                            linearLayoutProductCapacity.setVisibility(GONE);


                        } else {
                            ISProductCapacityAvailable = true;

                            tvProductCapacity.setText(jsonObject.getString("production_capacity"));


                        }


                        if (jsonObject.getString("port_of_supply").equals("")) {
                            ISPortOfSupplyAvailable = false;
                            linearLayoutPortOfSupply.setVisibility(GONE);


                        } else {
                            ISPortOfSupplyAvailable = true;
                            tvPortOfSupply.setText(jsonObject.getString("port_of_supply"));


                        }


                        if (jsonObject.getString("minimum_order_quantity").equals("")) {
                            ISMinimumOrderQntAvailable = false;
                            linearLayoutMinimumOrderQnt.setVisibility(GONE);

                        } else {
                            ISMinimumOrderQntAvailable = true;
                            tvMinimumOrderQuantity.setText(jsonObject.getString("minimum_order_quantity"));


                        }


                        if (jsonObject.getString("sample_piece").equals("")) {
                            ISSamplePieceAvailable = false;
                            linearLayoutSamplePiece.setVisibility(GONE);

                        } else {
                            ISSamplePieceAvailable = true;
                            tvSamplePiece.setText(jsonObject.getString("sample_piece"));


                        }

                        if (!ISDeliveryTimeAvailable && !ISPackingDetailsAvailable && !ISProductCapacityAvailable && !ISPortOfSupplyAvailable
                                && !ISMinimumOrderQntAvailable && !ISSamplePieceAvailable) {

                            LnrOtherDetails.setVisibility(GONE);
                        } else {
                            LnrOtherDetails.setVisibility(VISIBLE);
                        }


                        JSONArray jsonArrayprospecification = jsonObject.getJSONArray("prospecification");


                        for (int i = 0; i < jsonArrayprospecification.length(); i++) {

                            JSONObject jsonObjectprospecification = jsonArrayprospecification.getJSONObject(i);
                            String pk_id = jsonObjectprospecification.getString("pk_id");
                            String attribute_name = jsonObjectprospecification.getString("attribute_name");
                            String attributes_type_name = jsonObjectprospecification.getString("attributes_type_name");
                            String attribute_fkid = jsonObjectprospecification.getString("attribute_fkid");
                            String attributetype_fkid = jsonObjectprospecification.getString("attributetype_fkid");
                            String value = jsonObjectprospecification.getString("value");


                            ProductSpecificationModel productSpecificationModel = new ProductSpecificationModel(pk_id, attribute_name, attributes_type_name, attribute_fkid, attributetype_fkid, value);
                            mProductSpecificationModelArrayList.add(productSpecificationModel);


                        }


                        if (jsonObject.getString("additional_features").equals("")) {

                            linearLayoutAdditionalFeature.setVisibility(GONE);


                        } else {

                            tvAdditionalFeatures.setText(jsonObject.getString("additional_features"));


                        }

                        if (jsonObject.getString("description").equals("") && jsonObject.getString("short_description").equals("")) {


                            linearLayoutDescription.setVisibility(GONE);

                        } else {

                            tvShortDescription.setText(jsonObject.getString("short_description"));
                            tvLongDescription.setText(jsonObject.getString("description"));

                        }
                        String catSubcatName = "";
                        if (!jsonObject.isNull("cat_name") && !jsonObject.getString("cat_name").isEmpty()) {
                            catSubcatName = jsonObject.getString("cat_name");
                        }
                        if (!jsonObject.isNull("subcat_name") && !jsonObject.getString("subcat_name").isEmpty()) {
                            catSubcatName = catSubcatName.concat(" - " + jsonObject.getString("subcat_name"));
                        }
                        if (!jsonObject.isNull("sub_subcat_name") && !jsonObject.getString("sub_subcat_name").isEmpty()) {
                            catSubcatName = catSubcatName.concat(" - " + jsonObject.getString("sub_subcat_name"));
                        }
                        tvProductCategory.setText(catSubcatName);

                        upload_brochure = jsonObject.getString("upload_brochure") + "" + jsonObject.getString("upload_brochure_name");


                        JSONArray jsonArraymodearray = jsonObject.getJSONArray("modearray");


                        if (jsonArraymodearray.length() == 0) {

                            linearLayoutModeOfPayment.setVisibility(GONE);


                        } else {

                            String modeOfPayment = "";
                            for (int i = 0; i < jsonArraymodearray.length(); i++) {


                                JSONObject jsonObjectaymodearray = jsonArraymodearray.getJSONObject(i);

                                String modeOf = jsonObjectaymodearray.getString("mode_of_payment");

                                if (modeOfPayment.equals("")) {
                                    modeOfPayment = modeOf;
                                } else {

                                    modeOfPayment = modeOfPayment + ", " + modeOf;
                                }

                            }

                            tvModeOfPayment.setText(modeOfPayment);
                            if (modeOfPayment.trim().equals("null,")) {
                                tvModeOfPayment.setText("-");
                                linearLayoutModeOfPayment.setVisibility(GONE);
                            }

                        }

                        JSONArray jsonArraymodesupplyarray = jsonObject.getJSONArray("modesupplyarray");

                        if (jsonArraymodesupplyarray.length() == 0) {

                            linearLayoutModeOfSupply.setVisibility(GONE);


                        } else {
                            String modeOfSupply = "";
                            for (int i = 0; i < jsonArraymodesupplyarray.length(); i++) {


                                JSONObject jsonObjectayjsonArraymodesupplyarray = jsonArraymodesupplyarray.getJSONObject(i);

                                String modesupply = jsonObjectayjsonArraymodesupplyarray.getString("mode_of_supply");

                                if (modeOfSupply.equals("")) {
                                    modeOfSupply = modesupply;
                                } else {
                                    modeOfSupply = modeOfSupply + ", " + modesupply;
                                }
                            }

                            tvModeOfSupply.setText(modeOfSupply);
                            if (modeOfSupply.trim().equals("null,")) {
                                tvModeOfSupply.setText("-");
                                linearLayoutModeOfSupply.setVisibility(GONE);
                            }
                        }
                        String upload_brochure_name = jsonObject.getString("upload_brochure_name");
                        if (upload_brochure_name.equalsIgnoreCase("")) {

                            LinearLayoutBrochure.setVisibility(GONE);

                        } else {

                            LinearLayoutBrochure.setVisibility(VISIBLE);


                        }

                        mProductListBuyModelArrayList = new ArrayList<>();
                        JSONArray related_product_list = jsonObject.getJSONArray("related_product_list");
                        for (int l = 0; l < related_product_list.length(); l++) {


                            JSONObject jsonObjectProductdata = related_product_list.getJSONObject(l);

                            ProductListBuyModel productListBuyModel = new ProductListBuyModel();

                            productListBuyModel.setFavorite(jsonObjectProductdata.getString("favorite"));
                            productListBuyModel.setMobile_no(jsonObjectProductdata.getString("mobile_no"));
                            //productListBuyModel.setBusspincode(jsonObjectProductdata.getString("pincode"));
                            productListBuyModel.setBussaddress(jsonObjectProductdata.getString("address"));
                            productListBuyModel.setEstablished_year(jsonObjectProductdata.getString("established_year"));
                            productListBuyModel.setComp_name(jsonObjectProductdata.getString("comp_name"));
                            productListBuyModel.setImage_name(baseurlproductimages + jsonObjectProductdata.getString("image_name"));
                            productListBuyModel.setSub_subcat_name(jsonObjectProductdata.getString("sub_subcat_name"));
                            productListBuyModel.setCat_name(jsonObjectProductdata.getString("cat_name"));
                            productListBuyModel.setProduct_name(jsonObjectProductdata.getString("product_name"));
                            productListBuyModel.setCurrency(jsonObjectProductdata.getString("currency"));
                            productListBuyModel.setCost(jsonObjectProductdata.getString("cost"));
                            productListBuyModel.setPk_id(jsonObjectProductdata.getString("pk_id"));
                            productListBuyModel.setSubcat_name(jsonObjectProductdata.getString("subcat_name"));
                            productListBuyModel.setCountry(jsonObjectProductdata.getString("country_name"));

                            mProductListBuyModelArrayList.add(productListBuyModel);

                        }


                        ShowProductList();

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    //For first image
                    try {
                        Picasso.get().load(mProductImageViewModelArrayList.get(0).getImage()).placeholder(R.drawable.ic_launcher_foreground).into(ivMainProductImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("check", "Array Size" + mViewPagerProductImageModelArrayList.size());

                    mAdapterProductImageView.notifyDataSetChanged();
                    mProductImageMyViewPagerAdapter.notifyDataSetChanged();

                    linearLayoutVideoView.removeAllViews();
                    ShowListOfSpecification();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutProductView);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutProductView);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                } else {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }
                params.put("productid", productid);

                Log.e("check", "=======>" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

// stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void ShowProductList() {
        int advCount = 0;
        ArrayList<AdvCategoryModel> adsArrayList = new ArrayList<>();
        LnrUserPost.removeAllViews();
        LayoutInflater layoutInflater;
        for (int j = 0; j < mProductListBuyModelArrayList.size(); j++) {

            layoutInflater = (LayoutInflater) getActivity().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View cview = layoutInflater.inflate(R.layout.adapter_product_list_buy, null);
            TextView tv_product_name = cview.findViewById(R.id.tv_product_name);
            TextView tv_company_name = cview.findViewById(R.id.tv_company_name);
            TextView tv_counntry = cview.findViewById(R.id.tv_counntry);
            TextView tv_cost = cview.findViewById(R.id.tv_cost);
            TextView tv_location = cview.findViewById(R.id.tv_location);
            LinearLayout linear_layout_location = cview.findViewById(R.id.linear_layout_location);
            final ImageView iv_favourite = cview.findViewById(R.id.iv_favourite);
            final ImageView iv_product_image = cview.findViewById(R.id.iv_product_image);
            final LinearLayout Linear_layout_enquiry = cview.findViewById(R.id.Linear_layout_enquiry);
            LinearLayout linear_layout_main = cview.findViewById(R.id.linear_layout_main);
            LinearLayout Linear_layout_view = cview.findViewById(R.id.Linear_layout_view);


            final ProductListBuyModel productListBuyModel = mProductListBuyModelArrayList.get(j);


            try {
                Picasso.get().load(productListBuyModel.getImage_name()).placeholder(R.drawable.default_document).into(iv_product_image);
            } catch (Exception e) {
                e.printStackTrace();
            }

            tv_product_name.setText(productListBuyModel.getProduct_name());
            tv_counntry.setText(productListBuyModel.getCountry());
            tv_company_name.setText(productListBuyModel.getComp_name());

            final String favorite = productListBuyModel.getFavorite();
            String location = productListBuyModel.getBussaddress();
            String price = productListBuyModel.getCost();

//            if (!price.equals("")) {
//
//                tv_cost.setText(mProductListBuyModelArrayList.get(j).getCurrency() + " " + productListBuyModel.getCost());
//
//
//            } else {
//
//                tv_cost.setVisibility(GONE);
//
//
//            }
            if (!price.equals("")) {
                if (price.equals("0")) {
                    tv_cost.setText(mProductListBuyModelArrayList.get(j).getCurrency() + " - Enquiry for cost");
                } else {
                    tv_cost.setText(mProductListBuyModelArrayList.get(j).getCurrency() + " " + productListBuyModel.getCost());
                }

            }

            if (!location.equals("")) {

                tv_location.setText(productListBuyModel.getBussaddress());


            } else {

                linear_layout_location.setVisibility(GONE);

            }

            if (favorite.equals("1")) {

                try {
                    Picasso.get().load(R.drawable.ic_favourite_active).placeholder(R.drawable.ic_favourite_active).into(iv_favourite);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    Picasso.get().load(R.drawable.ic_favourite_inactive).placeholder(R.drawable.ic_favourite_inactive).into(iv_favourite);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            final int finalJ = j;
            final int finalJ1 = j;
            iv_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (favorite.equals("1")) {

                        try {
                            Picasso.get().load(R.drawable.ic_favourite_inactive).placeholder(R.drawable.ic_favourite_inactive).into(iv_favourite);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mProductListBuyModelArrayList.get(finalJ).setFavorite("2");

                        CallSetFavourite(mProductListBuyModelArrayList.get(finalJ).getPk_id());

                        //notifyDataSetChanged();

                    } else {

                        try {
                            Picasso.get().load(R.drawable.ic_favourite_active).placeholder(R.drawable.ic_favourite_active).into(iv_favourite);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mProductListBuyModelArrayList.get(finalJ1).setFavorite("1");

                        CallSetFavourite(mProductListBuyModelArrayList.get(finalJ1).getPk_id());


                        //notifyDataSetChanged();

                    }


                }
            });

            Linear_layout_enquiry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EnquiryFragment enquiryFragment = new EnquiryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PK_id", productListBuyModel.getPk_id());
                    enquiryFragment.setArguments(bundle);
                    Constants.mMainActivity.ChangeFragments(enquiryFragment, "EnquiryFragment");


                }
            });

            linear_layout_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ProductViewFragment productViewFragment = new ProductViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PK_id", productListBuyModel.getPk_id());
                    productViewFragment.setArguments(bundle);
                    Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

                }
            });

            Linear_layout_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ProductViewFragment productViewFragment = new ProductViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PK_id", productListBuyModel.getPk_id());
                    productViewFragment.setArguments(bundle);
                    Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

                }
            });


            LayoutInflater layoutInflateradv;
            View advView = null;

            Log.e("Pratibha", "adsArrayList.size() " + adsArrayList.size() + " advCount " + advCount);
            if (advCount < adsArrayList.size()) {


                if (j % 3 == 0) {

                    Log.e("Pratibha", "advertisement in");

                    layoutInflateradv = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    advView = layoutInflateradv.inflate(R.layout.adapter_product_list_advertisment, null);

                    SliderView sliderView = advView.findViewById(R.id.imageSlider);

                    ArrayList<AdvCategoryModel> adsArrayListinflate = new ArrayList();


                    try {
                        for (int i = 0; i < 5; i++) {

                            adsArrayListinflate.add(adsArrayList.get(advCount));
                            advCount++;


                            SliderAdapterExample adapter = new SliderAdapterExample(getActivity(), adsArrayListinflate);

                            sliderView.setSliderAdapter(adapter);

                            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            sliderView.setIndicatorSelectedColor(Color.WHITE);
                            sliderView.setIndicatorUnselectedColor(Color.GRAY);
                            sliderView.startAutoCycle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (advView != null) {
                    try {
                        LnrUserPost.addView(advView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            LnrUserPost.addView(cview);


            //if (advCount)


        }

        LayoutInflater layoutInflateradv;
        View advView = null;


        if (mProductListBuyModelArrayList.size() != 0 && advCount < adsArrayList.size()) {


            layoutInflateradv = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            advView = layoutInflateradv.inflate(R.layout.adapter_product_list_advertisment, null);

            SliderView sliderView = advView.findViewById(R.id.imageSlider);

            ArrayList<AdvCategoryModel> adsArrayListinflate = new ArrayList();


            try {
                for (int i = advCount; i < adsArrayList.size(); i++) {

                    adsArrayListinflate.add(adsArrayList.get(advCount));
                    advCount++;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            SliderAdapterExample adapter = new SliderAdapterExample(getActivity(), adsArrayListinflate);

            sliderView.setSliderAdapter(adapter);

            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.startAutoCycle();
            LnrUserPost.addView(advView);

        }


    }

    //Methods for Download file
    public void getDownlodFile(String url) {

        Log.e("check", "=========>" + url);

        UtilityMethods.showInfoToast(getActivity(), "Downloading");

        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File myFile = new File(Constants.KEY_PATH_OF_QUATATION + "/" + fileName);
        // download code
        Log.e("path", "Path of download-->>" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + " file name-->>" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getName() + " file name-->>" + fileName);
        if (myFile.exists()) {
            Log.e("path", "*******is exists******");
            Log.e("path", myFile.toString());
            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", myFile);
            Log.e("uri", "uri-->>>" + uri.getPath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
                //new
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                //new
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
                //new
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(intent);

        } else {
            Log.e("path", "*******Not exists******");
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
            request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);
            Long refrance = downloadManager.enqueue(request);
        }

    }


    private void ShowListOfSpecification() {

        if (mProductSpecificationModelArrayList.size() == 0) {
            LnrSpecification.setVisibility(GONE);
        }

        //Display list view runtime inflate
        for (int i = 0; i < mProductSpecificationModelArrayList.size(); i++) {

            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.adapter_specfication_view, null);

            linearLayoutVideoView.addView(view);


            TextView tv_attribute = view.findViewById(R.id.tv_attribute);
            TextView tv_attribute_value = view.findViewById(R.id.tv_attribute_value);
            TextView tv_attribute_type = view.findViewById(R.id.tv_attribute_type);


            tv_attribute.setText(mProductSpecificationModelArrayList.get(i).getAttribute_name());
            tv_attribute_value.setText(mProductSpecificationModelArrayList.get(i).getValue());
            tv_attribute_type.setText(mProductSpecificationModelArrayList.get(i).getAttributes_type_name());

        }

    }

    //Method to Call Set Favourite
    private void CallSetFavourite(final String productId) {

        //UtilityMethods.tuchOff(relativeLayoutProductView);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FEVORITE_SUBMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallSetFavourite" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    //For first image
                    try {
                        Picasso.get().load(mProductImageViewModelArrayList.get(0).getImage()).placeholder(R.drawable.ic_launcher_foreground).into(ivMainProductImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                // UtilityMethods.tuchOn(relativeLayoutProductView);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutProductView);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                } else {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));


                }

                params.put("productid", productId);
                params.put("utype", mSessionManager.getStringData(Constants.KEY_USER_TYPE));

                Log.e("check", "=======>" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

// stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
