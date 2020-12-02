package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterAttributeList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCityList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCountyBusinessList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCurrencyList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSpinnerSubCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSubCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSubSubCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.SliderAdapterExample;
import com.tradegenie.android.tradegenieapp.Models.AdvCategoryModel;
import com.tradegenie.android.tradegenieapp.Models.AttributeModel;
import com.tradegenie.android.tradegenieapp.Models.CategoryModel;
import com.tradegenie.android.tradegenieapp.Models.CityListModel;
import com.tradegenie.android.tradegenieapp.Models.CountryListBusinessModel;
import com.tradegenie.android.tradegenieapp.Models.CurrencyModel;
import com.tradegenie.android.tradegenieapp.Models.ProductListBuyModel;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProductListBuyFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressBarProductBuy)
    RelativeLayout ProgressBarProductBuy;

    RelativeLayout ProgressBarProductBuyOrignal;

    @BindView(R.id.textViewFilterErrorMessage)
    TextView textViewFilterErrorMessage;
    @BindView(R.id.Lnr_UserPost)
    LinearLayout LnrUserPost;
    @BindView(R.id.loadMore)
    TextView loadMore;

    int advCount = 0;
    @BindView(R.id.spinner_sub_sub_category_main)
    Spinner spinnerSubSubCategoryMain;
    @BindView(R.id.iv_view)
    View ivView;
    @BindView(R.id.iv_drop_down)
    ImageView ivDropDown;
    @BindView(R.id.rel_spinner_sub_category)
    RelativeLayout relSpinnerSubCategory;
    LinearLayout Lnr_city;
    @BindView(R.id.iv_category)
    ImageView ivCategory;

    private SessionManager mSessionManager;
    public boolean IsFilterSelected = false;


    private ArrayList<ProductListBuyModel> mProductListBuyModelArrayList;
    /*private AdapterProductListBuy mAdapterProductListBuy;
    public LinearLayoutManager linearLayoutManager;*/

    //Declaration for category
    private ArrayList<CategoryModel> mCategoryModelArrayList;
    private AdapterCategoryList mAdapterCategoryList;


    boolean IsFirstTime = true;


    private ArrayList<SubCategoryModel> mSubCategoryModelArrayList;
    private AdapterSubCategoryList mAdapterSubCategoryList;


    private ArrayList<AttributeModel> mAttributeModelArrayList;
    private AdapterAttributeList mAdapterAttributeModel;


    private ArrayList<SubSubCategoryModel> mSubSubCategoryModelArrayList;
    private AdapterSubSubCategoryList mAdapterSubSubCategoryList;

    //private Spinner spinnercategory, spinnerSubCategory, spinnerSubSubCategory, spinnerCountry, spinnerCity;


    private AdapterSpinnerSubCategoryList mAdapterSpinnerSubCategoryList;


    private ArrayList<CountryListBusinessModel> mCountryListModelArrayList;
    private AdapterCountyBusinessList mAdapterCountyList;

    private ArrayList<CityListModel> mCityListModelArrayList;
    private AdapterCityList mAdapterCityList;

    private ArrayList<AdvCategoryModel> adsArrayList;

    TextView txt_category, txt_sub_category, txt_currenncy, txt_sub_sub_category, txt_country, txt_city;

    private String minPrice, maxPrice;
    public int OFFSET = 0;
    ProgressDialog progressBar;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    //private boolean loading = true;

    private String mSelectedCategory = "", mSelectedSubCategory = "", mSelectedSubSubCategory = "", mSelectedCurrency = "", mSelectedCountry = "", mSelectedCity = "", mSelectedFromPrice = "", mSelectedToPrice = "";
    private String mSelectedCategoryName = "", mSelectedSubCategoryName = "", mSelectedSubSubCategoryName = "", mSelectedCurrencyName = "", mSelectedCountryName = "", mSelectedCityName = "";
    private int mSelectedCategoryPosition = 0, mSelectedSubCategoryPosition = 0, mSelectedSubSubCategoryPosition = 0, mSelectedCurrencyPosition = 0, mSelectedCountryPosition = 0, mSelectedCityPosition = 0;

    private ArrayList<CurrencyModel> mCurrencyModelArrayList;
    private AdapterCurrencyList mAdapterCurrencyList;


    boolean IsRatingChecked = false;
    private String pricefrom, priceto, FromHomeScreen, Search = "";

    private Boolean aBoolean = true, FilterClick = false;
    private int spinnerCnt = 0;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e("TAG","onAttachFragment");
        try {
            if (fragment instanceof ProductListBuyFragment) {
                try {
                    Constants.mMainActivity.setToolBarName("Product");
                    Constants.mMainActivity.setToolBarPostEnquiryHide();
                    Constants.mMainActivity.setToolBar(GONE, GONE, GONE, VISIBLE);
                    Constants.mMainActivity.setNotificationButton(GONE);
                    Constants.mMainActivity.relSpinnerSubCategory.setVisibility(VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public ProductListBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list_buy, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Product");
        Constants.mMainActivity.setToolBarPostEnquiryHide();

        Constants.mMainActivity.setToolBar(GONE, GONE, GONE, VISIBLE);
        Constants.mMainActivity.setNotificationButton(GONE);
        ProgressBarProductBuyOrignal = view.findViewById(R.id.ProgressBarProductBuy);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.relSpinnerSubCategory.setVisibility(VISIBLE);
        Constants.mMainActivity.productListBuyFragment = ProductListBuyFragment.this;
//        Constants.mMainActivity.edtSearchProduct.setText("");
        Constants.mMainActivity.openKeyboard();

        advCount = 0;
        OFFSET = 0;


        Log.e("Pratibha", "onCreateView ProductListBuyFragment");

        mCategoryModelArrayList = new ArrayList<>();
        mCategoryModelArrayList.add(new CategoryModel("0", "Select", ""));
        mAdapterCategoryList = new AdapterCategoryList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCategoryModelArrayList);


        mSubCategoryModelArrayList = new ArrayList<>();
        mSubCategoryModelArrayList.add(new SubCategoryModel("0", "Select", ""));
        mAdapterSubCategoryList = new AdapterSubCategoryList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mSubCategoryModelArrayList);

        mAttributeModelArrayList = new ArrayList<>();
        mAttributeModelArrayList.add(new AttributeModel("0", "Select"));


        //Set adapter of spinner of load more specification's spinner
        mAdapterAttributeModel = new AdapterAttributeList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mAttributeModelArrayList);


        mSubSubCategoryModelArrayList = new ArrayList<>();
        mSubSubCategoryModelArrayList.add(new SubSubCategoryModel("0", "Select", ""));
        mAdapterSubSubCategoryList = new AdapterSubSubCategoryList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mSubSubCategoryModelArrayList);

        mCountryListModelArrayList = new ArrayList<>();
        mCountryListModelArrayList.add(new CountryListBusinessModel("0", "Select", ""));
        mAdapterCountyList = new AdapterCountyBusinessList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCountryListModelArrayList);

        mCityListModelArrayList = new ArrayList<>();
        mCityListModelArrayList.add(new CityListModel("0", "Select", ""));
        mAdapterCityList = new AdapterCityList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCityListModelArrayList);

        adsArrayList = new ArrayList<>();


        //Condition for spinner selection
        /*if (mSelectedCategory.equals("")) {*/
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mSelectedCategory = bundle.getString("mSelectedCategory");
            mSelectedSubCategory = bundle.getString("mSelectedSubCategory");
            mSelectedSubSubCategory = bundle.getString("mSelectedSubSubCategory");
//            mSelectedSubCategory = bundle.getString("mSelectedSubCategory");
            FromHomeScreen = bundle.getString("FromHomeScreen");
            mSelectedCountry = mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY);
            mSelectedCity = mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY);

            Log.e("mSelectedSubSubCategory", "mSelectedSubSubCategory " + mSelectedSubSubCategory);

            if (FromHomeScreen.equals("FromHomeScreen")) {

                Constants.mMainActivity.relSpinnerSubCategory.setVisibility(GONE);

            }

        }
        /*}*/


//        Constants.mMainActivity.getSubSubcategoryAttribute(ProgressBarProductBuy, mSelectedSubCategory, mSelectedSubSubCategory);


        spinnerSubSubCategoryMain.setAdapter(mAdapterSubSubCategoryList);
        spinnerSubSubCategoryMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                //  mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                //  mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();

                if (++spinnerCnt > 1) {

                    if (!mSubSubCategoryModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                        //     mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                        mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                        mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                        mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                        try {
//                            ivCategory.setImageURI(Uri.parse(mSubSubCategoryModelArrayList.get(i).getSubcat_image()));
                            Picasso.get().load(mSubSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.ic_launcher_foreground).into(ivCategory);
                        }catch (Exception e){e.printStackTrace();}
                        Log.e("check", "mSelectedSubCategory Spinner----->" + mSelectedSubCategory);

                        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                        {

                            advCount = 0;
                            OFFSET = 0;
                            mProductListBuyModelArrayList.clear();
                            adsArrayList.clear();
                            getAdvBanners();

                        } else {

                            UtilityMethods.showInternetDialog(getActivity());
                        }


                    } else {
                        // mSelectedSubSubCategory = "0";
                        // mSelectedSubSubCategory = "0";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.e("SIZE", "mSubSubCategoryModelArrayList.size() " + mSubSubCategoryModelArrayList.size());
//        if (mSubSubCategoryModelArrayList.size() == 1) {
//            relSpinnerSubCategory.setVisibility(GONE);
//        } else {
//            relSpinnerSubCategory.setVisibility(VISIBLE);
//        }
        mProductListBuyModelArrayList = new ArrayList<>();


//        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
//        {
//
//            getAdvBanners();
//
//        } else {
//
//            UtilityMethods.showInternetDialog(getActivity());
//        }


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {
            GetCategoryList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }

//        if (InternetConnection.isInternetAvailable(getActivity())) {
//
//            getSubcategoryAttribute();
//
//
//        } else {
//
//            UtilityMethods.showInternetDialog(getActivity());
//
//        }

//        if (InternetConnection.isInternetAvailable(getActivity())) {
//
//            getSubSubcategoryAttribute();
//
//
//        } else {
//
//            UtilityMethods.showInternetDialog(getActivity());
//
//        }

        if (InternetConnection.isInternetAvailable(getActivity())) {

            getCountryList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }


        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OFFSET = OFFSET + 10;
/*
                if (InternetConnection.isInternetAvailable(getActivity())) {
                    GetProductDetails(mSelectedSubSubCategory, Search);
                } else {
                    UtilityMethods.showInternetDialog(getActivity());
                }*/

                if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                {
                    advCount = 0;
                    getAdvBannersLoadMore();

                } else {

                    UtilityMethods.showInternetDialog(getActivity());
                }

            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void openFilterDialog() {

        aBoolean = true;

        Log.e("Pratibha", "mSubSubCategoryModelArrayList.size()" + mSubSubCategoryModelArrayList.size());

        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);

        mBottomSheetDialog.setContentView(R.layout.dialog_filter); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        CrystalRangeSeekbar priceSeekbar = mBottomSheetDialog.findViewById(R.id.priceSeekbar);

        final TextView tv_from_price_range = mBottomSheetDialog.findViewById(R.id.tv_from_price_range);
        final TextView tv_to_price_range = mBottomSheetDialog.findViewById(R.id.tv_to_price_range);
        final EditText edt_from_price = mBottomSheetDialog.findViewById(R.id.edt_from_price);
        final EditText edt_to_price = mBottomSheetDialog.findViewById(R.id.edt_to_price);
        final CheckBox chkRating = mBottomSheetDialog.findViewById(R.id.chk_rating);
      /*  spinnercategory = mBottomSheetDialog.findViewById(R.id.spinner_category);
        spinnerSubCategory = mBottomSheetDialog.findViewById(R.id.spinner_sub_category);
        spinnerSubSubCategory = mBottomSheetDialog.findViewById(R.id.spinner_sub_sub_category);
        spinnerCountry = mBottomSheetDialog.findViewById(R.id.spinner_country);
        spinnerCity = mBottomSheetDialog.findViewById(R.id.spinner_city);


       */


        Spinner spinnerCurrency = mBottomSheetDialog.findViewById(R.id.spinner_currency);
        View ivViewSpinnerCurrency = mBottomSheetDialog.findViewById(R.id.iv_view_spinner_currency);
        ImageView ivDropDownCurrency = mBottomSheetDialog.findViewById(R.id.iv_drop_down_currency);
        RelativeLayout relCurrency = mBottomSheetDialog.findViewById(R.id.rel_currency);

        Lnr_city = mBottomSheetDialog.findViewById(R.id.Lnr_city);

        ProgressBarProductBuy = mBottomSheetDialog.findViewById(R.id.ProgressLoading);

        ImageView iv_drop_down_category = mBottomSheetDialog.findViewById(R.id.iv_drop_down_category);
        ImageView iv_drop_down_sub_category = mBottomSheetDialog.findViewById(R.id.iv_drop_down_sub_category);
        ImageView iv_drop_down_sub_sub_category = mBottomSheetDialog.findViewById(R.id.iv_drop_down_sub_sub_category);
        ImageView iv_drop_down_currenncy = mBottomSheetDialog.findViewById(R.id.iv_drop_down_currenncy);
        ImageView iv_drop_down_country = mBottomSheetDialog.findViewById(R.id.iv_drop_down_country);
        ImageView iv_drop_down_city = mBottomSheetDialog.findViewById(R.id.iv_drop_down_city);
        ImageView iv_cancel = mBottomSheetDialog.findViewById(R.id.iv_cancel);


        txt_category = mBottomSheetDialog.findViewById(R.id.txt_category);
        txt_sub_category = mBottomSheetDialog.findViewById(R.id.txt_sub_category);
        txt_currenncy = mBottomSheetDialog.findViewById(R.id.txt_currenncy);
        txt_sub_sub_category = mBottomSheetDialog.findViewById(R.id.txt_sub_sub_category);
        txt_country = mBottomSheetDialog.findViewById(R.id.txt_country);
        txt_city = mBottomSheetDialog.findViewById(R.id.txt_city);


        mCurrencyModelArrayList = new ArrayList<>();
        mCurrencyModelArrayList.add(new CurrencyModel("0", "Select"));
        mCurrencyModelArrayList.add(new CurrencyModel("1", "INR"));
        mCurrencyModelArrayList.add(new CurrencyModel("2", "LKR"));
        mCurrencyModelArrayList.add(new CurrencyModel("2", "USD"));
/*
        mAdapterCurrencyList = new AdapterCurrencyList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCurrencyModelArrayList);
        spinnerCurrency.setAdapter(mAdapterCurrencyList);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mCurrencyModelArrayList.get(i).getCurrency().equalsIgnoreCase("Select")) {
                    mSelectedCurrency = mCurrencyModelArrayList.get(i).getCurrency();

                } else {
                    mSelectedCurrency = "Select";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


       /* ivDropDownCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCurrency.performClick();
            }
        });*/

        LinearLayout linear_filter = mBottomSheetDialog.findViewById(R.id.linear_filter);

        try {
            priceSeekbar.setMinValue(Float.parseFloat(minPrice));
            priceSeekbar.setMaxValue(Float.parseFloat(maxPrice));
        } catch (Exception e) {

        }


        pricefrom = "0";
        priceto = "0";

        try {
            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();
                }
            });

            iv_drop_down_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCategoryDropdown();
                }
            });

            iv_drop_down_sub_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowSubCategoryDropdown();
                }
            });

            iv_drop_down_sub_sub_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowSubSubCategoryDropdown();
                }
            });
            iv_drop_down_currenncy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCurrencyDropdown();
                }
            });

            iv_drop_down_country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCountryDropdown();
                }
            });

            iv_drop_down_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCityDropdown();
                }
            });


            txt_currenncy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCurrencyDropdown();
                }
            });

            txt_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCategoryDropdown();
                }
            });

            txt_sub_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowSubCategoryDropdown();
                }
            });

            txt_sub_sub_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowSubSubCategoryDropdown();
                }
            });

            txt_country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCountryDropdown();
                }
            });

            txt_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    ShowCityDropdown();
                }
            });


            // set listener
            priceSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                @Override
                public void valueChanged(Number minValue, Number maxValue) {

                  /*  pricefrom = String.valueOf(minValue);
                    priceto = String.valueOf(maxValue);

                    tv_from_price_range.setText("Min " + NumberFormat.getNumberInstance(Locale.US).format(minValue));
                    tv_to_price_range.setText("Max " + NumberFormat.getNumberInstance(Locale.US).format(maxValue));
               */
                }
            });


            edt_from_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    pricefrom = edt_from_price.getText().toString().trim();
                    if (edt_from_price.getText().toString().trim().equals("")) {
                        pricefrom = "";
                        mSelectedFromPrice = "";
                    } else {
                        pricefrom = edt_from_price.getText().toString().trim();
                        mSelectedFromPrice = edt_from_price.getText().toString().trim();
                    }
                }
            });

            edt_to_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    priceto = edt_to_price.getText().toString().trim();
                    if (edt_to_price.getText().toString().trim().equals("")) {
                        priceto = "";
                        mSelectedToPrice = "";
                    } else {
                        priceto = edt_to_price.getText().toString().trim();
                        mSelectedToPrice = edt_to_price.getText().toString().trim();
                    }
                }
            });

            edt_from_price.setText(mSelectedFromPrice);
            edt_to_price.setText(mSelectedToPrice);
            chkRating.setChecked(IsRatingChecked);
            SetFilterValues();


            linear_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (chkRating.isChecked()) {
                        IsRatingChecked = true;
                    } else {
                        IsRatingChecked = false;
                    }

                    if (!pricefrom.equals("") && !priceto.equals("")) {


                        if (mSelectedCurrency.equals("Select")) {

                            UtilityMethods.showWarningToast(getActivity(), "Please select currency.");

                        } else {


                            if (Integer.parseInt(pricefrom) > Integer.parseInt(priceto)) {
                                edt_to_price.setError("Please enter valid range.");
                                edt_to_price.requestFocus();
                            } else {
                                mBottomSheetDialog.hide();
                                PerformFilterClick();
                            }
                        }
                    } else if (pricefrom.equals("") && !priceto.equals("")) {

                        edt_from_price.setError("Please enter valid range.");
                        edt_from_price.requestFocus();

                    } else if (priceto.equals("") && !pricefrom.equals("")) {

                        edt_to_price.setError("Please enter valid range.");
                        edt_to_price.requestFocus();

                    } else {
                        //if both are empty
                        mBottomSheetDialog.hide();
                        PerformFilterClick();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void PerformFilterClick() {
        {


            if (InternetConnection.isInternetAvailable(getActivity())) {

                ProgressBarProductBuy = ProgressBarProductBuyOrignal;

                FilterClick = true;

                Constants.mMainActivity.relSpinnerSubCategory.setVisibility(GONE);


                OFFSET = 0;
                //GetProductDetails(mSelectedSubSubCategory, Search);

                if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                {

                    advCount = 0;
                    getAdvBanners();

                } else {

                    UtilityMethods.showInternetDialog(getActivity());
                }


            } else {

                UtilityMethods.showInternetDialog(getActivity());

            }
        }
    }

    private void ShowCountryDropdown() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (null != getActivity().getCurrentFocus())
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                    .getApplicationWindowToken(), 0);


        final CharSequence[] StringmCategoryModelArrayList = new CharSequence[mCountryListModelArrayList.size()];
        for (int i = 0; i < mCountryListModelArrayList.size(); i++) {
            StringmCategoryModelArrayList[i] = mCountryListModelArrayList.get(i).getCountry_name();
        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select Country");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mBuilder.setSingleChoiceItems(StringmCategoryModelArrayList, mSelectedCountryPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txt_country.setText(StringmCategoryModelArrayList[i]);
                mSelectedCountryPosition = i;
                mSelectedCountry = mCountryListModelArrayList.get(i).getPk_id();
                mSelectedCountryName = mCountryListModelArrayList.get(i).getCountry_name();


                mCityListModelArrayList.clear();

                mSelectedCityPosition = 0;
                mSelectedCity = "";
                txt_city.setText("");

                if (mSelectedCountry.equals("1")) {
                    Lnr_city.setVisibility(GONE);
                    dialogInterface.dismiss();
                } else {
                    Lnr_city.setVisibility(VISIBLE);
                    if (InternetConnection.isInternetAvailable(getActivity())) {
                        getCityList("");
                        dialogInterface.dismiss();
                    } else {
                        UtilityMethods.showInternetDialog(getActivity());
                    }
                }


            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void ShowCityDropdown() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (null != getActivity().getCurrentFocus())
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                    .getApplicationWindowToken(), 0);


        final CharSequence[] StringmCategoryModelArrayList = new CharSequence[mCityListModelArrayList.size()];
        for (int i = 0; i < mCityListModelArrayList.size(); i++) {
            StringmCategoryModelArrayList[i] = mCityListModelArrayList.get(i).getCountry_name();
        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select City");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mBuilder.setSingleChoiceItems(StringmCategoryModelArrayList, /*mSelectedCityPosition*/-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txt_city.setText(StringmCategoryModelArrayList[i]);
                mSelectedCityPosition = i;
                mSelectedCity = mCityListModelArrayList.get(i).getPk_id();
                mSelectedCityName = mCityListModelArrayList.get(i).getCountry_name();


            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void ShowCategoryDropdown() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (null != getActivity().getCurrentFocus())
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                    .getApplicationWindowToken(), 0);


        final CharSequence[] StringmCategoryModelArrayList = new CharSequence[mCategoryModelArrayList.size()];
        for (int i = 0; i < mCategoryModelArrayList.size(); i++) {
            StringmCategoryModelArrayList[i] = mCategoryModelArrayList.get(i).getCat_name();
        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select Category");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mBuilder.setSingleChoiceItems(StringmCategoryModelArrayList, mSelectedCategoryPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txt_category.setText(StringmCategoryModelArrayList[i]);
                mSelectedCategoryPosition = i;
                mSelectedCategory = mCategoryModelArrayList.get(i).getPk_id();
                mSelectedCategoryName = mCategoryModelArrayList.get(i).getCat_name();

                mSubCategoryModelArrayList.clear();
                mSubSubCategoryModelArrayList.clear();

                mSelectedSubCategoryPosition = 0;
                mSelectedSubSubCategoryPosition = 0;


                mSelectedSubCategory = "";
                mSelectedSubSubCategory = "";

                mSelectedSubCategoryName = "";
                mSelectedSubSubCategoryName = "";


                txt_sub_category.setText("");
                txt_sub_sub_category.setText("");

                if (InternetConnection.isInternetAvailable(getActivity())) {
                    getSubcategoryAttribute();
                    dialogInterface.dismiss();
                } else {
                    UtilityMethods.showInternetDialog(getActivity());
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void ShowSubCategoryDropdown() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (null != getActivity().getCurrentFocus())
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                    .getApplicationWindowToken(), 0);


        final CharSequence[] StringmCategoryModelArrayList = new CharSequence[mSubCategoryModelArrayList.size()];
        for (int i = 0; i < mSubCategoryModelArrayList.size(); i++) {
            StringmCategoryModelArrayList[i] = mSubCategoryModelArrayList.get(i).getTitle();
        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select Sub Category");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mBuilder.setSingleChoiceItems(StringmCategoryModelArrayList, mSelectedSubCategoryPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txt_sub_category.setText(StringmCategoryModelArrayList[i]);
                mSelectedSubCategoryPosition = i;
                mSelectedSubCategory = mSubCategoryModelArrayList.get(i).getPk_id();
                mSelectedSubCategoryName = mSubCategoryModelArrayList.get(i).getTitle();


                mSubSubCategoryModelArrayList.clear();

                mSelectedSubSubCategoryPosition = 0;


                mSelectedSubSubCategory = "";
                mSelectedSubSubCategoryName = "";


                txt_sub_sub_category.setText("");

                if (InternetConnection.isInternetAvailable(getActivity())) {
                    getSubSubcategoryAttribute();
                    dialogInterface.dismiss();
                } else {
                    UtilityMethods.showInternetDialog(getActivity());
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void ShowSubSubCategoryDropdown() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (null != getActivity().getCurrentFocus())
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                    .getApplicationWindowToken(), 0);


        final CharSequence[] StringmCategoryModelArrayList = new CharSequence[mSubSubCategoryModelArrayList.size()];
        for (int i = 0; i < mSubSubCategoryModelArrayList.size(); i++) {
            StringmCategoryModelArrayList[i] = mSubSubCategoryModelArrayList.get(i).getTitle();
        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select Sub Sub Category");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mBuilder.setSingleChoiceItems(StringmCategoryModelArrayList, mSelectedSubSubCategoryPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txt_sub_sub_category.setText(StringmCategoryModelArrayList[i]);
                mSelectedSubSubCategoryPosition = i;
                mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                mSelectedSubSubCategoryName = mSubSubCategoryModelArrayList.get(i).getTitle();

                dialogInterface.dismiss();

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void ShowCurrencyDropdown() {
        Log.e("Position", "mSelectedCurrencyPosition " + mSelectedCurrencyPosition);
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (null != getActivity().getCurrentFocus())
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                    .getApplicationWindowToken(), 0);


        final CharSequence[] StringmCurrencyModelArrayList = new CharSequence[mCurrencyModelArrayList.size()];
        for (int i = 0; i < mCurrencyModelArrayList.size(); i++) {
            StringmCurrencyModelArrayList[i] = mCurrencyModelArrayList.get(i).getCurrency();
        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select Currency");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mBuilder.setSingleChoiceItems(StringmCurrencyModelArrayList, mSelectedCurrencyPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txt_currenncy.setText(StringmCurrencyModelArrayList[i]);
                mSelectedCurrencyPosition = i;
                mSelectedCurrency = mCurrencyModelArrayList.get(i).getCurrency();
                mSelectedCurrencyName = mCurrencyModelArrayList.get(i).getCurrency();

                dialogInterface.dismiss();

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void SetFilterValues() {


        Log.e("Pratibha---", "mSelectedCategory " + mSelectedCategory);
        Log.e("Pratibha---", "mSelectedSubCategory " + mSelectedSubCategory);
        Log.e("Pratibha---", "mSelectedSubSubCategory " + mSelectedSubSubCategory);
        Log.e("Pratibha---", "mSelectedCountry " + mSelectedCountry);
        Log.e("Pratibha---", "mSelectedCity " + mSelectedCity);


        txt_category.setText(mSelectedCategoryName);
        txt_sub_category.setText(mSelectedSubCategoryName);
        txt_sub_sub_category.setText(mSelectedSubSubCategoryName);
        txt_country.setText(mSelectedCountryName);
        txt_city.setText(mSelectedCityName);
        txt_currenncy.setText(mSelectedCurrencyName);


        if (mSelectedCountry.equals("1")) {
            Lnr_city.setVisibility(GONE);
        } else {
            Lnr_city.setVisibility(VISIBLE);
        }
    }


    //Method to Get Category List
    private void GetCategoryList() {

        // UtilityMethods.tuchOff(ProgressBarProductBuy);

        UtilityMethods.tuchOff(ProgressBarProductBuy);
        Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CATEGORY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "GetCategoryList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraycategory = jsonObject.getJSONArray("category");
                        mAdapterCategoryList.clear();
                        mCategoryModelArrayList.add(new CategoryModel("0", "Select", ""));
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String cat_name = jsonObjectcategory.getString("cat_name");

                            CategoryModel categoryModel = new CategoryModel(pk_id, cat_name, "");
                            mCategoryModelArrayList.add(categoryModel);

                            if (!mSelectedCategory.equals("") && mSelectedCategory.equals(jsonObjectcategory.getString("pk_id"))) {

                                mSelectedCategoryName = jsonObjectcategory.getString("cat_name");
                                mSelectedCategoryPosition = i + 1;

                            }
                        }

                        mAdapterCategoryList.notifyDataSetChanged();
                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            getSubcategoryAttribute();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    UtilityMethods.tuchOn(ProgressBarProductBuy);
                    Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);


                }


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
                UtilityMethods.tuchOn(ProgressBarProductBuy);
                Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("buyer")) {
                    params.put("userid", mSessionManager.getStringData(Constants.KEY_BUYER_UID));
                } else {
                    params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                }

                Log.e("check", "URL_CATEGORY_LIST======>" + Constants.URL_CATEGORY_LIST);
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
    private void getSubcategoryAttribute() {

        UtilityMethods.tuchOff(ProgressBarProductBuy);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_SUBCATEGORY_ATTRIBUTE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSubcategoryAttribute -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraycategory = jsonObject.getJSONArray("Subcategory");
                        mSubCategoryModelArrayList.clear();
                        mSubCategoryModelArrayList.add(new SubCategoryModel("0", "Select", ""));
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String title = jsonObjectcategory.getString("title");

                            SubCategoryModel subCategoryModel = new SubCategoryModel(pk_id, title, "");
                            mSubCategoryModelArrayList.add(subCategoryModel);

                            if (!mSelectedSubCategory.equals("") && mSelectedSubCategory.equals(jsonObjectcategory.getString("pk_id"))) {

                                mSelectedSubCategoryName = jsonObjectcategory.getString("title");
                                mSelectedSubCategoryPosition = i + 1;

                            }
                        }

                        JSONArray jsonArrayattribute = jsonObject.getJSONArray("attribute");
                        mAttributeModelArrayList.clear();
//                        mAttributeModelArrayList.add(new AttributeModel("0", "Select"));
                        for (int i = 0; i < jsonArrayattribute.length(); i++) {

                            JSONObject jsonObjectattribute = jsonArrayattribute.getJSONObject(i);

                            String pk_id = jsonObjectattribute.getString("pk_id");
                            String title = jsonObjectattribute.getString("title");

                            AttributeModel attributeModel = new AttributeModel(pk_id, title);
                            mAttributeModelArrayList.add(attributeModel);
                        }

                        mAdapterSubCategoryList.notifyDataSetChanged();
                        mAdapterAttributeModel.notifyDataSetChanged();

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            getSubSubcategoryAttribute();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }

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

                UtilityMethods.tuchOn(ProgressBarProductBuy);


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
                UtilityMethods.tuchOn(ProgressBarProductBuy);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("category_id", ""+ mSelectedCategory);

                Log.e("check", "URL_GET_SUBCATEGORY_ATTRIBUTE======>" + Constants.URL_GET_SUBCATEGORY_ATTRIBUTE);
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

        UtilityMethods.tuchOff(ProgressBarProductBuy);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUB_SUB_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSubSubcategoryAttribute -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String imagePath = jsonObject.getString("path");
                        JSONArray jsonArraycategory = jsonObject.getJSONArray("Sub Subcategory");
                        mSubSubCategoryModelArrayList.clear();
                        mSubSubCategoryModelArrayList.add(new SubSubCategoryModel("0", "Select", ""));
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String title = jsonObjectcategory.getString("title");
                            String subcatImgUrl = imagePath + jsonObjectcategory.getString("subcat_image");
                            SubSubCategoryModel subSubCategoryModel = new SubSubCategoryModel(pk_id, title, subcatImgUrl);
                            mSubSubCategoryModelArrayList.add(subSubCategoryModel);

                            if (!mSelectedSubSubCategory.equals("") && mSelectedSubSubCategory.equals(jsonObjectcategory.getString("pk_id"))) {

                                mSelectedSubSubCategoryName = jsonObjectcategory.getString("title");
                                mSelectedSubSubCategoryPosition = i + 1;

                            }
                        }


                        Log.e("mSubSubCategoryArray", "mSubSubCategoryModelArrayList.size() " + mSubSubCategoryModelArrayList.size());
                        if (mSubSubCategoryModelArrayList.size() == 1) {
                            relSpinnerSubCategory.setVisibility(GONE);
                        } else {
                            relSpinnerSubCategory.setVisibility(VISIBLE);
                        }
                        mAdapterSubSubCategoryList.notifyDataSetChanged();
                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


//                    mAdapterSubSubCategoryList.notifyDataSetChanged();

                    //Set Spinner for sub sub category

                    if (!FilterClick) {
                        Log.e("FILTER", "if");
                        Log.e("Pratibha---", "mSelectedSubSubCategory " + mSelectedSubSubCategory);
                        for (int i = 0; i < mSubSubCategoryModelArrayList.size(); i++) {
                            Log.e("compare", mSubSubCategoryModelArrayList.get(i).getPk_id() + "====" + mSelectedSubSubCategory);
                            if (mSubSubCategoryModelArrayList.get(i).getPk_id().equals(mSelectedSubSubCategory)) {
                                Log.e("SubSubCategory", "inside");
                                Log.e("SubSubCategory", "SubSubCategory " + mSubSubCategoryModelArrayList.get(i).getTitle());
                                try {
                                    spinnerSubSubCategoryMain.setSelection(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try{
                                    try {
                                        Picasso.get().load(mSubSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.ic_launcher_foreground).into(ivCategory);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }catch (Exception e){e.printStackTrace();}
                                break;
                            }
                        }
                    } else {
                        Log.e("FILTER", "else");

                        for (int i = 0; i < mSubSubCategoryModelArrayList.size(); i++) {


                            Log.e("compare", mSubSubCategoryModelArrayList.get(i).getPk_id() + "====" + mSelectedSubSubCategory);


                            if (mSubSubCategoryModelArrayList.get(i).getPk_id().equals(mSelectedSubSubCategory)) {


                                try {
                                    spinnerSubSubCategoryMain.setSelection(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try{
                                    try {
                                        Picasso.get().load(mSubSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.ic_launcher_foreground).into(ivCategory);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }catch (Exception e){e.printStackTrace();}

                                break;

                            }


                        }


                    }


                } catch (Exception e) {
                    Log.e("SubSubCategory", "Exception ");
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarProductBuy);


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
                UtilityMethods.tuchOn(ProgressBarProductBuy);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("subcategory_id", mSelectedSubCategory);

                Log.e("check", "URL_SUB_SUB_CATEGORY======>" + Constants.URL_SUB_SUB_CATEGORY);
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

    //Method to get Country List
    private void getCountryList() {

        UtilityMethods.tuchOff(ProgressBarProductBuy);

        // StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_COUNTRY_LIST, new Response.Listener<String>() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_COUNTRY_LIST_HOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getCountryList -->>" + response);
                Log.e("response", "URL_COUNTRY_LIST_HOME -->>" + Constants.URL_COUNTRY_LIST_HOME);

                try {

                    mCountryListModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String flag_path = jsonObject.getString("flag_path");


                        mCountryListModelArrayList.add(new CountryListBusinessModel("0", "Select", ""));
                        JSONArray countrylistJSONArray = jsonObject.getJSONArray("countrylist");
                        for (int i = 0; i < countrylistJSONArray.length(); i++) {

                            JSONObject countrylistJSONObject = countrylistJSONArray.getJSONObject(i);
                            String pk_id = countrylistJSONObject.getString("pk_id");
                            String country_name = countrylistJSONObject.getString("country_name");
                            String country_flag = flag_path + countrylistJSONObject.getString("country_flag");

                            CountryListBusinessModel countryListBusinessModel = new CountryListBusinessModel(pk_id, country_name, country_flag);
                            mCountryListModelArrayList.add(countryListBusinessModel);

                            if (!mSelectedCountry.equals("") && mSelectedCountry.equals(countrylistJSONObject.getString("pk_id"))) {

                                mSelectedCountry = countrylistJSONObject.getString("pk_id");
                                mSelectedCountryName = countrylistJSONObject.getString("country_name");
                                mSelectedCountryPosition = i + 1;


                            }

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterCountyList.notifyDataSetChanged();


                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        getMinMaxPrice();


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);

                }
                UtilityMethods.tuchOn(ProgressBarProductBuy);

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
                UtilityMethods.tuchOn(ProgressBarProductBuy);
                Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

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

    private void getCityList(final String fk_city_id) {

        //Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(false);
        UtilityMethods.tuchOff(ProgressBarProductBuy);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CITY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getCityList -->>" + response);

                try {
                    mCityListModelArrayList.clear();
                    mCityListModelArrayList.add(new CityListModel("0", "Select", ""));

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONArray JSONArrayCitylist = jsonObject.getJSONArray("city_name");
                        for (int i = 0; i < JSONArrayCitylist.length(); i++) {

                            JSONObject CitylistJSONObject = JSONArrayCitylist.getJSONObject(i);

                            String pk_id = CitylistJSONObject.getString("pk_id");
                            String city_name = CitylistJSONObject.getString("city_name");
                            //String country_fkid = CitylistJSONObject.getString("country_fkid");

                            CityListModel cityListModel = new CityListModel(pk_id, city_name, "");
                            mCityListModelArrayList.add(cityListModel);
                            if (!mSelectedCity.equals("") && (mSelectedCity.equals(CitylistJSONObject.getString("pk_id")) || mSelectedCity.equals(CitylistJSONObject.getString("city_name")))) {

                                mSelectedCity = CitylistJSONObject.getString("city_name");
                                mSelectedCityPosition = i + 1;

                            }
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterCityList.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarProductBuy);
                Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);


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
                UtilityMethods.tuchOn(ProgressBarProductBuy);
                Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("country", mSelectedCountry);

                Log.e("cityList webservice", "URL_CITY_LIST : params" + Constants.URL_CITY_LIST);
                Log.e("cityList webservice", "params" + params);
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


    private void getMinMaxPrice() {


        UtilityMethods.tuchOff(ProgressBarProductBuy);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MIN_MAX_PRICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "Min Max Price -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        minPrice = jsonObject.getString("min_cost");
                        maxPrice = jsonObject.getString("max_cost");

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                    {

                        getCityList("");


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);

                }

                UtilityMethods.tuchOn(ProgressBarProductBuy);


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
                Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);
                UtilityMethods.tuchOn(ProgressBarProductBuy);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                Log.e("check", "URL_MIN_MAX_PRICE======>" + Constants.URL_MIN_MAX_PRICE);
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

    public void GetProductDetails(String from, String SelectedSubSubCategory, final String search, boolean isSearchFromMainActivity) {
        Log.e("From"+from,"\tSearchString : "+search );
        Constants.mMainActivity.isSearchFromMainActivity = isSearchFromMainActivity;
        UtilityMethods.tuchOff(ProgressBarProductBuy);
        Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(false);
        mSelectedSubSubCategory = SelectedSubSubCategory;
        mSelectedSubSubCategory = SelectedSubSubCategory;
        Search = search;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FILTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("From"+from,"\tSearchString : "+search );
                Log.e("response", "GetProductDetails -->>   " + Search +"\t"+ response);

                hideKeyboard();
                if (OFFSET == 0) {
                    try {
                        mProductListBuyModelArrayList.clear();
                        // LnrUserPost.removeAllViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String total_count = jsonObject.getString("total_count");
                        //if (Integer.parseInt(total_count) > 10) {


                        String product_image = jsonObject.getString("product_image");

                        JSONArray jsonArrayProductdata = jsonObject.getJSONArray("filter_result");

                        for (int i = 0; i < jsonArrayProductdata.length(); i++) {


                            JSONObject jsonObjectProductdata = jsonArrayProductdata.getJSONObject(i);

                            ProductListBuyModel productListBuyModel = new ProductListBuyModel();

                            productListBuyModel.setFavorite(jsonObjectProductdata.getString("favorite"));
                            productListBuyModel.setMobile_no(jsonObjectProductdata.getString("mobile_no"));
                            //productListBuyModel.setBusspincode(jsonObjectProductdata.getString("pincode"));
                            productListBuyModel.setBussaddress(jsonObjectProductdata.getString("address"));
                            productListBuyModel.setEstablished_year(jsonObjectProductdata.getString("established_year"));
                            productListBuyModel.setComp_name(jsonObjectProductdata.getString("comp_name"));
                            productListBuyModel.setImage_name(product_image + jsonObjectProductdata.getString("image_name"));
                            productListBuyModel.setSub_subcat_name(jsonObjectProductdata.getString("sub_subcat_name"));
                            productListBuyModel.setCat_name(jsonObjectProductdata.getString("subcat_name"));
                            productListBuyModel.setProduct_name(jsonObjectProductdata.getString("product_name"));
                            productListBuyModel.setCurrency(jsonObjectProductdata.getString("currency"));
                            productListBuyModel.setCountry(jsonObjectProductdata.getString("country_name"));
                            productListBuyModel.setCity(jsonObjectProductdata.getString("city_name"));
                            productListBuyModel.setCost(jsonObjectProductdata.getString("cost"));
                            productListBuyModel.setPk_id(jsonObjectProductdata.getString("pk_id"));
                            productListBuyModel.setSubcat_name(jsonObjectProductdata.getString("subcat_name"));

                            mProductListBuyModelArrayList.add(productListBuyModel);

                        }
                        if (Integer.parseInt(total_count) > mProductListBuyModelArrayList.size()) {
                            loadMore.setVisibility(VISIBLE);
                        } else {
                            loadMore.setVisibility(GONE);
                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {
                        loadMore.setVisibility(GONE);
                    } else if (jsonObject.getString("error_code").equals("2")) {
                        loadMore.setVisibility(GONE);
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {

                        loadMore.setVisibility(GONE);
                        //UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterSubCategoryList.notifyDataSetChanged();
                    mAdapterAttributeModel.notifyDataSetChanged();


                    ShowProductList();

                    if (mProductListBuyModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(GONE);
                        textViewFilterErrorMessage.setVisibility(VISIBLE);

                    } else {


                        textViewErrorMessage.setVisibility(GONE);
                        textViewFilterErrorMessage.setVisibility(GONE);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Constants.mMainActivity.imgToolbarFilterHomeFour.setEnabled(true);
                UtilityMethods.tuchOn(ProgressBarProductBuy);


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
                UtilityMethods.tuchOn(ProgressBarProductBuy);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (FilterClick) {

                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    } else {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                    }

                    params.put("search", search);
                    params.put("catid", mSelectedCategory);
                    params.put("subcatid", mSelectedSubCategory);
                    params.put("subsubcatid", mSelectedSubSubCategory);
                    params.put("cid", mSelectedCountry);

                    if (IsFilterSelected) {
                        params.put("cityid", mSelectedCity);
                    } else {
                        params.put("cityid", "");
                    }
                    params.put("pricefrom", pricefrom);
                    params.put("priceto", priceto);
                    params.put("currency", mSelectedCurrency);


                    if (!pricefrom.equals("")) {
                        params.put("budget_range_status", "1");
                    } else {
                        params.put("budget_range_status", "2");
                    }


                    params.put("limit", "10");
                    params.put("offset", String.valueOf(OFFSET));

                    if (IsRatingChecked) {
                        params.put("high_to_low", "1");
                    } else {
                        params.put("high_to_low", "2");
                    }


                } else {

                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    } else {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                    }

                    params.put("search", search);
                    params.put("catid", mSelectedCategory);
                    params.put("subcatid", mSelectedSubCategory);
                    params.put("subsubcatid", mSelectedSubSubCategory);
                    params.put("limit", "10");
                    params.put("offset", String.valueOf(OFFSET));
                    params.put("utype", mSessionManager.getStringData(Constants.KEY_USER_TYPE));
                    params.put("cid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY));
                    params.put("cityid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY));
//                    Log.e("check", "Product Params======>" + params);
                }
                Log.e("check", "URL_FILTER======>" + Constants.URL_FILTER);
                Log.e("check", "Filter Params======>" + params);

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

        Volley.newRequestQueue(getActivity()).add(stringRequest).setTag("PRODUCT_DETAILS").setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    //Hide Keyboard
    public void hideKeyboard() {


        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    private void ShowProductList() {
        advCount = 0;
        LnrUserPost.removeAllViews();
        LayoutInflater layoutInflater;
        Log.e("RESPONSE", "SIZE : "+mProductListBuyModelArrayList.size());
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
                if (price.equals("0")){
                    tv_cost.setText(mProductListBuyModelArrayList.get(j).getCurrency() + " - Enquiry for cost");
                }else {

                    tv_cost.setText(mProductListBuyModelArrayList.get(j).getCurrency() + " " + productListBuyModel.getCost());
                }

            } else {

                tv_cost.setVisibility(GONE);


            }

            if (!location.equals("")) {

                tv_location.setText(/*productListBuyModel.getBussaddress() + ", " + */productListBuyModel.getCity());


            } else {

                //    linear_layout_location.setVisibility(View.GONE);
                tv_location.setText(productListBuyModel.getCity());

            }

            tv_counntry.setText(productListBuyModel.getCountry());

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
                    bundle.putBoolean("isFromList", true);
                    productViewFragment.setArguments(bundle);
//                    Constants.mMainActivity.ChangeFragments(productViewFragment, "ProductViewFragment");
                    Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

                }
            });

            Linear_layout_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ProductViewFragment productViewFragment = new ProductViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PK_id", productListBuyModel.getPk_id());
                    bundle.putBoolean("isFromList", true);
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


//                            SliderAdapterExample adapter = new SliderAdapterExample(getActivity(), adsArrayListinflate);
//
//                            sliderView.setSliderAdapter(adapter);
//
//                            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                            sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
//                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                            sliderView.setIndicatorSelectedColor(Color.WHITE);
//                            sliderView.setIndicatorUnselectedColor(Color.GRAY);
//                            sliderView.startAutoCycle();
                        }
                        SliderAdapterExample adapter = new SliderAdapterExample(getActivity(), adsArrayListinflate);

                        sliderView.setSliderAdapter(adapter);

                        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.startAutoCycle();
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

    //Method to Call Set Favourite
    private void CallSetFavourite(final String productid) {

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
                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                // UtilityMethods.tuchOn(relativeLayoutProductView);

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
                params.put("utype", mSessionManager.getStringData(Constants.KEY_USER_TYPE));

                Log.e("check", "URL_FEVORITE_SUBMIT=======>" + Constants.URL_FEVORITE_SUBMIT);
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

    public void getAdvBanners() {
        UtilityMethods.tuchOff(ProgressBarProductBuy);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUB_SUB_CATEGORY_ADV, new Response.Listener<String>() {
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
                        Log.e("Pratibha", "adsArrayList.size() " + adsArrayList.size());

                    } else if (error_code.equalsIgnoreCase("2")) {


                    } else if (error_code.equalsIgnoreCase("3")) {

                    } else if (error_code.equalsIgnoreCase("4")) {
                    } else if (error_code.equalsIgnoreCase("5")) {

                    } else if (error_code.equalsIgnoreCase("10")) {
                        //UtilityMethods.UserInactivePopup(getActivity());


                    } else if (error_code.equalsIgnoreCase("0")) {


                        UtilityMethods.showInfoToast(getActivity(), "Banner not found");

                    }



                    /*if (adsArrayList.size() == 0) {

                        relativeLayoutViewPager.setVisibility(GONE);

                    } else {

                        relativeLayoutViewPager.setVisibility(VISIBLE);

                        //notify banner and set banner
                        NUM_PAGES = adsArrayList.size();
                        //popularVideoViewPagerHomeFragment.getAdapter().notifyDataSetChanged();
                        mAdapterAdvCategoryViewPager.notifyDataSetChanged();
                        addBottomDots(0);
                        //popularVideoViewPagerHomeFragment.setAdapter(myViewPagerPopularVideoAdapter);
                        SetSliderAutoTimer();

                    }*/

//                    if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
//                    {
//                        UtilityMethods.tuchOff(ProgressBarProductBuy);
                    OFFSET = 0;
                    GetProductDetails("getAdvBanners",mSelectedSubSubCategory, Search, false);


//                    } else {
//
//                        UtilityMethods.showInternetDialog(getActivity());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //UtilityMethods.showInfoToast(getActivity(), "Server Error");

                }

//                UtilityMethods.tuchOn(ProgressBarProductBuy);
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

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, ServerError.");
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, NetworkError.");
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                UtilityMethods.tuchOn(ProgressBarProductBuy);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("test", "otp verification Params-->>" + params.toString());


                if (FilterClick) {

                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    } else {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                    }

                    params.put("cid", mSelectedCountry);
                    params.put("cityid", mSelectedCity);
                    params.put("catid", mSelectedCategory);
                    params.put("subcatid", mSelectedSubCategory);
                    params.put("subsubcatid", mSelectedSubSubCategory);

                    Log.e("check", "======>" + params);

                } else {

                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    } else {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                    }

                    params.put("cid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY));
                    params.put("cityid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY));                //params.put("catid", mSelectedCategory);
                    params.put("catid", mSelectedCategory);
                    params.put("subcatid", mSelectedSubCategory);
                    params.put("subsubcatid", mSelectedSubSubCategory);

                }


                Log.e("params", "URL_SUB_SUB_CATEGORY_ADV1----->" + Constants.URL_SUB_SUB_CATEGORY_ADV);
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

//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //mRequestQueue.add(stringRequest);

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(
                120000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void getAdvBannersLoadMore() {
        UtilityMethods.tuchOff(ProgressBarProductBuy);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUB_SUB_CATEGORY_ADV, new Response.Listener<String>() {
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



                    /*if (adsArrayList.size() == 0) {

                        relativeLayoutViewPager.setVisibility(GONE);

                    } else {

                        relativeLayoutViewPager.setVisibility(VISIBLE);

                        //notify banner and set banner
                        NUM_PAGES = adsArrayList.size();
                        //popularVideoViewPagerHomeFragment.getAdapter().notifyDataSetChanged();
                        mAdapterAdvCategoryViewPager.notifyDataSetChanged();
                        addBottomDots(0);
                        //popularVideoViewPagerHomeFragment.setAdapter(myViewPagerPopularVideoAdapter);
                        SetSliderAutoTimer();

                    }*/

                    if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                    {

                        UtilityMethods.tuchOff(ProgressBarProductBuy);

                        GetProductDetails("getAdvBannersLoadMorem",mSelectedSubSubCategory, Search, false);


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //UtilityMethods.showInfoToast(getActivity(), "Server Error");

                }

                UtilityMethods.tuchOn(ProgressBarProductBuy);
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

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, ServerError.");
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, NetworkError.");
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                UtilityMethods.tuchOn(ProgressBarProductBuy);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("test", "otp verification Params-->>" + params.toString());


                if (FilterClick == true) {

                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    } else {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                    }

                    params.put("cid", mSelectedCountry);
                    params.put("cityid", mSelectedCity);
                    params.put("catid", mSelectedCategory);
                    params.put("subcatid", mSelectedSubCategory);
                    params.put("subsubcatid", mSelectedSubSubCategory);

                    Log.e("check", "======>" + params);

                } else {

                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    } else {

                        params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                    }

                    params.put("cid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY));
                    params.put("cityid", mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY));                //params.put("catid", mSelectedCategory);
                    params.put("catid", mSelectedCategory);
                    params.put("subcatid", mSelectedSubCategory);
                    params.put("subsubcatid", mSelectedSubSubCategory);

                }


                Log.e("params", "URL_SUB_SUB_CATEGORY_ADV View More----->" + Constants.URL_SUB_SUB_CATEGORY_ADV);
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

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Pratibha", "onResume");


    }


    public void onBackPressOfProductView() {
        try {
            Constants.mMainActivity.setToolBarName("Product");
            Constants.mMainActivity.setToolBarPostEnquiryHide();

            Constants.mMainActivity.setToolBar(GONE, GONE, GONE, VISIBLE);
            Constants.mMainActivity.setNotificationButton(GONE);
            Constants.mMainActivity.relSpinnerSubCategory.setVisibility(VISIBLE);
            if (FromHomeScreen.equals("FromHomeScreen")) {

                Constants.mMainActivity.relSpinnerSubCategory.setVisibility(GONE);

            }
        }catch (Exception e){e.printStackTrace();}
    }
}