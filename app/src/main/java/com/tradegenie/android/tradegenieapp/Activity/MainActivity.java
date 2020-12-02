package com.tradegenie.android.tradegenieapp.Activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCityList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterHomeCountyBusinessList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSubSubCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.DrawerListAdapter;
import com.tradegenie.android.tradegenieapp.Fragments.BusinessDetailsFragment;
import com.tradegenie.android.tradegenieapp.Fragments.BusinessDetailsMainFragment;
import com.tradegenie.android.tradegenieapp.Fragments.BusinessDetailsOtherDetailsFragment;
import com.tradegenie.android.tradegenieapp.Fragments.BuyerEnquiryListFragment;
import com.tradegenie.android.tradegenieapp.Fragments.BuyerFavouritesFragment;
import com.tradegenie.android.tradegenieapp.Fragments.BuyerHomeFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ChatListFragment;
import com.tradegenie.android.tradegenieapp.Fragments.DashBoardFragment;
import com.tradegenie.android.tradegenieapp.Fragments.EnquiryListFragment;
import com.tradegenie.android.tradegenieapp.Fragments.FavouritesFragment;
import com.tradegenie.android.tradegenieapp.Fragments.HelpSupportFragment;
import com.tradegenie.android.tradegenieapp.Fragments.HomeFragment;
import com.tradegenie.android.tradegenieapp.Fragments.LeadManagementMyLeadFragment;
import com.tradegenie.android.tradegenieapp.Fragments.LeadManagementNewLeadsFragment;
import com.tradegenie.android.tradegenieapp.Fragments.LeadManagmentMainFragment;
import com.tradegenie.android.tradegenieapp.Fragments.MySubscriptionFragment;
import com.tradegenie.android.tradegenieapp.Fragments.MySubscriptionMainFragment;
import com.tradegenie.android.tradegenieapp.Fragments.MySubscriptionRecommendedFragment;
import com.tradegenie.android.tradegenieapp.Fragments.NewTenderFragment;
import com.tradegenie.android.tradegenieapp.Fragments.PostRequirementListFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductListApprovedFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductListBuyFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductListFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductListPendingFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductListRejectedFragment;
import com.tradegenie.android.tradegenieapp.Fragments.SellerNotificationFragment;
import com.tradegenie.android.tradegenieapp.Fragments.SellerTransactionListFragment;
import com.tradegenie.android.tradegenieapp.Fragments.SettingsFragment;
import com.tradegenie.android.tradegenieapp.Fragments.TenderListAddFragment;
import com.tradegenie.android.tradegenieapp.Fragments.TenderListFragment;
import com.tradegenie.android.tradegenieapp.Fragments.TenderMainFragment;
import com.tradegenie.android.tradegenieapp.Models.CityListModel;
import com.tradegenie.android.tradegenieapp.Models.CountryListBusinessModel;
import com.tradegenie.android.tradegenieapp.Models.NavItem;
import com.tradegenie.android.tradegenieapp.Models.SubSubCategoryModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.e;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.USER_LANG;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_Toolbar_menu)
    ImageView imgToolbarMenu;
    @BindView(R.id.txt_ToolBarName)
    TextView txtToolBarName;
    @BindView(R.id.mainContent)
    RelativeLayout mainContent;
    @BindView(R.id.profileImage_drawer)
    CircleImageView profileImageDrawer;
    @BindView(R.id.txt_Name_drawer)
    TextView txtNameDrawer;
    @BindView(R.id.relativeLayout_profile_drawer)
    RelativeLayout relativeLayoutProfileDrawer;
    @BindView(R.id.navList)
    ListView navList;
    @BindView(R.id.drawerPane)
    LinearLayout mDrawerPane;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.img_dashboard)
    ImageView imgDashboard;
    @BindView(R.id.txt_dashboard)
    TextView txtDashboard;
    @BindView(R.id.lnr_home)
    LinearLayout lnrHome;
    @BindView(R.id.rel_home)
    RelativeLayout relHome;
    @BindView(R.id.img_order)
    ImageView imgOrder;
    @BindView(R.id.txt_order)
    TextView txtOrder;
    @BindView(R.id.lnr_favourite)
    LinearLayout lnrFavourite;
    @BindView(R.id.rel_favourite)
    RelativeLayout relFavourite;
    @BindView(R.id.img_deliveries)
    ImageView imgDeliveries;
    @BindView(R.id.txt_deliveries)
    TextView txtDeliveries;
    @BindView(R.id.lnr_my_products)
    LinearLayout lnrMyProducts;
    @BindView(R.id.rel_my_products)
    RelativeLayout relMyProducts;
    @BindView(R.id.img_payments)
    ImageView imgPayments;
    @BindView(R.id.txt_payments)
    TextView txtPayments;
    @BindView(R.id.img_clients)
    ImageView imgClients;
    @BindView(R.id.txt_clients)
    TextView txtClients;
    @BindView(R.id.lnr_profile)
    LinearLayout lnrProfile;
    @BindView(R.id.rel_profile)
    RelativeLayout relProfile;
    @BindView(R.id.Lnr_bottom)
    LinearLayout LnrBottom;
    @BindView(R.id.lnr_message)
    LinearLayout lnrMessage;
    @BindView(R.id.rel_message)
    public RelativeLayout relMessage;
    @BindView(R.id.toolbar_first)
    RelativeLayout toolbarFirst;
    @BindView(R.id.toolbar_second)
    LinearLayout toolbarSecond;
    @BindView(R.id.toolbar_third)
    LinearLayout toolbarThird;
    @BindView(R.id.toolbar_four)
    LinearLayout toolbarFour;
    @BindView(R.id.spinner_sub_sub_category)
    Spinner spinnerSubSubCategory;
    @BindView(R.id.iv_sub_sub_category)
    ImageView ivSubSubCategory;
    @BindView(R.id.iv_delete_notification)
    public ImageView ivDeleteNotification;
    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;
    @BindView(R.id.ProgressBarMainActivity)
    RelativeLayout ProgressBarMainActivity;
    @BindView(R.id.spinner_city)
    Spinner spinnerCity;
    @BindView(R.id.rel_country)
    RelativeLayout relCountry;
    @BindView(R.id.rel_city)
    RelativeLayout relCity;
    @BindView(R.id.btn_become_a_seller)
    Button btnBecomeASeller;
    @BindView(R.id.img_Toolbar_filter_home_four)
    public ImageView imgToolbarFilterHomeFour;
    @BindView(R.id.rel_spinner_sub_category)
    public RelativeLayout relSpinnerSubCategory;
    @BindView(R.id.rel_search_home)
    RelativeLayout relSearchHome;
    @BindView(R.id.rel_search_sub_cat)
    RelativeLayout relSearchSubCat;
    @BindView(R.id.edt_search_product)
    public EditText edtSearchProduct;
    @BindView(R.id.iv_mic_gray_four)
    ImageView ivMicGrayFour;
    @BindView(R.id.txt_listened)
    TextView txtListened;
    @BindView(R.id.linearLayoutContinerToast)
    LinearLayout linearLayoutContinerToast;
    @BindView(R.id.img_Toolbar_menu_home)
    ImageView imgToolbarMenuHome;
    @BindView(R.id.iv_view_country)
    View ivViewCountry;
    @BindView(R.id.iv_drop_down_country)
    ImageView ivDropDownCountry;
    @BindView(R.id.iv_search_gray)
    ImageView ivSearchGray;
    @BindView(R.id.iv_mic_gray)
    ImageView ivMicGray;
    @BindView(R.id.iv_view_city)
    View ivViewCity;
    @BindView(R.id.iv_drop_down_city)
    ImageView ivDropDownCity;
    @BindView(R.id.img_Toolbar_menu_home_third)
    ImageView imgToolbarMenuHomeThird;
    @BindView(R.id.iv_search_gray_third)
    ImageView ivSearchGrayThird;
    @BindView(R.id.iv_mic_gray_third)
    ImageView ivMicGrayThird;
    @BindView(R.id.view_line_third)
    View viewLineThird;
    @BindView(R.id.iV_location_gray_third)
    ImageView iVLocationGrayThird;
    @BindView(R.id.img_Toolbar_menu_home_four)
    ImageView imgToolbarMenuHomeFour;
    @BindView(R.id.iv_search_gray_four)
    ImageView ivSearchGrayFour;
    @BindView(R.id.view_line_four)
    View viewLineFour;
    @BindView(R.id.iV_location_gray_four)
    ImageView iVLocationGrayFour;
    @BindView(R.id.rel_search_product_view)
    RelativeLayout relSearchProductView;
    @BindView(R.id.iv_view_two)
    View ivViewTwo;
    @BindView(R.id.iv_view)
    View ivView;
    @BindView(R.id.iv_drop_down)
    ImageView ivDropDown;
    @BindView(R.id.txt_my_enquiry)
    TextView txtMyEnquiry;
    @BindView(R.id.Lnr_drawer_info)
    LinearLayout LnrDrawerInfo;
    @BindView(R.id.Lnr_post_requirement)
    LinearLayout LnrPostRequirement;

    public DrawerListAdapter mDrawerListAdapter;
    public ArrayList<NavItem> mNavItems = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView toggle;
    private ImageView toggle_home;
    private ImageView toggle_home_third;
    private ImageView toggle_home_four;
    private FragmentManager fragmentManager;
    public TextView mTxt_ToolBarName;
    public View mToolbar;
    private Fragment fragment = null;
    public static String WhichLogin = "";
    private SessionManager mSessionManager;

    String before_search= "------------------------------------------------------------------------find";
    boolean IsBackPopupToBeDisplyed = true;
    Bundle bundle;
    private String loginUser = "", redirect_type = "";

    public Bitmap thumbnail_r;
    public Bitmap thumbnail_rb;

    public String WhichFragmentIsopen = "";
    public String WhichLeadFragmentIsopen = "";
    public String WhichTenderFragmentIsopen = "";
    public Boolean OneTimeRun = true;
    private String business_city;

    public BusinessDetailsFragment businessDetailsFragment;
    public BusinessDetailsOtherDetailsFragment businessDetailsOtherDetailsFragment;

    public ProductListPendingFragment productListPendingFragment;
    public ProductListApprovedFragment productListApprovedFragment;
    public ProductListRejectedFragment productListRejectedFragment;

    public MySubscriptionFragment mySubscriptionFragment;
    public MySubscriptionRecommendedFragment mySubscriptionRecommendedFragment;

    public LeadManagementMyLeadFragment leadManagementMyLeadFragment;
    public LeadManagementNewLeadsFragment leadManagementNewLeadsFragment;

    public TenderListFragment tenderListFragment;
    public NewTenderFragment newTenderFragment;

    public ProductListBuyFragment productListBuyFragment;
    public HomeFragment homeFragment;
    public BuyerHomeFragment buyerHomeFragment;

    public SellerNotificationFragment sellerNotificationFragment;


    private ArrayList<SubSubCategoryModel> mSubSubCategoryModelArrayList;
    private AdapterSubSubCategoryList mAdapterSubSubCategoryList;
    private String mSelectedSubSubCategory;

    private ArrayList<CountryListBusinessModel> mCountryListModelArrayList;
    private AdapterHomeCountyBusinessList mAdapterHomeCountyBusinessList;
    private String mSelectedCountry;


    private ArrayList<CityListModel> mCityListModelArrayList;
    private AdapterCityList mAdapterCityList;
    private String mSelectedCity;

    public CountDownTimer countDownTimer;
    public boolean isSearchFromMainActivity = false;
    //Voice Search
    String VoiceSearch = "";

    String BeforeSerach = "";
    boolean isTyping = false;
    private Timer timer = new Timer();
    private final long DELAY = 1000; // milliseconds
    private int selectedCountryPosition = -1, selectedCityPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toggle = findViewById(R.id.img_Toolbar_menu);
        toggle_home = findViewById(R.id.img_Toolbar_menu_home);
        toggle_home_third = findViewById(R.id.img_Toolbar_menu_home_third);
        toggle_home_four = findViewById(R.id.img_Toolbar_menu_home_four);
        mTxt_ToolBarName = findViewById(R.id.txt_ToolBarName);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mToolbar = findViewById(R.id.toolbar);
        Constants.mMainActivity = MainActivity.this;
        fragmentManager = getFragmentManager();
        mSessionManager = new SessionManager(MainActivity.this);

        if (1.1 > 1.2) {
            Log.e("1.1 is greater", "1.1 is greater");
        } else {
            Log.e("1.2 is greater", "1.2 is greater");

        }


        businessDetailsFragment = new BusinessDetailsFragment();
        businessDetailsOtherDetailsFragment = new BusinessDetailsOtherDetailsFragment();

        productListApprovedFragment = new ProductListApprovedFragment();
        productListPendingFragment = new ProductListPendingFragment();
        productListRejectedFragment = new ProductListRejectedFragment();

        mySubscriptionFragment = new MySubscriptionFragment();
        mySubscriptionRecommendedFragment = new MySubscriptionRecommendedFragment();

        leadManagementMyLeadFragment = new LeadManagementMyLeadFragment();
        leadManagementNewLeadsFragment = new LeadManagementNewLeadsFragment();

        tenderListFragment = new TenderListFragment();
        newTenderFragment = new NewTenderFragment();

        mCountryListModelArrayList = new ArrayList<>();
        mAdapterHomeCountyBusinessList = new AdapterHomeCountyBusinessList(MainActivity.this, R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCountryListModelArrayList);
        spinnerCountry.setAdapter(mAdapterHomeCountyBusinessList);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mCountryListModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedCountry = mCountryListModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSelectedCountry----->" + mSelectedCountry);

                    mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY, mSelectedCountry);
                    mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY, "");


                    if (mCountryListModelArrayList.get(i).getPk_id().equals("1")) {

                        relCity.setEnabled(false);
                        spinnerCity.setEnabled(false);

//                        relCity.setVisibility(View.INVISIBLE);
                        relCity.setVisibility(View.GONE);
                    } else {
                        relCity.setEnabled(true);
                        spinnerCity.setEnabled(true);
                        relCity.setVisibility(View.VISIBLE);
                    }
                    if (InternetConnection.isInternetAvailable(MainActivity.this)) {


                        mCityListModelArrayList.clear();
                        mCityListModelArrayList.add(new CityListModel("0", "Select", ""));

                        spinnerCity.setSelection(0);


                        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {


                            homeFragment.getBanners();


                        } else {

                            buyerHomeFragment.getBanners();

                        }


                        getCityList(false);


                    } else {

                        UtilityMethods.showInternetDialog(MainActivity.this);

                    }
                } else {
                    mSelectedCountry = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCountry.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    Toast.makeText(MainActivity.this,"down", Toast.LENGTH_LONG).show();
                    // Load your spinner here
                    if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                    {

                        getCountryList(true);

                    } else {

                        UtilityMethods.showInternetDialog(MainActivity.this);
                    }
                }
                return false;
            }

        });
        relCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                {

                    getCountryList(true);

                } else {

                    UtilityMethods.showInternetDialog(MainActivity.this);
                }

//                spinnerCountry.performClick();

            }
        });

        spinnerCity.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    Toast.makeText(MainActivity.this,"down", Toast.LENGTH_LONG).show();
                    // Load your spinner here
                    if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                    {

                        getCityList(true);

                    } else {

                        UtilityMethods.showInternetDialog(MainActivity.this);
                    }
                }
                return false;
            }

        });
        relCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityList(true);
//                spinnerCity.performClick();

            }
        });

        LnrDrawerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_enquiry_gray);

                    imgDeliveries.setImageResource(R.drawable.my_product_grey);

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgClients.setImageResource(R.drawable.ic_profile_yellow);

                    /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                    ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment"); */

                   // BusinessDetailsMainFragment businessDetailsMainFragment = new BusinessDetailsMainFragment();
                   // ChangeFragments(businessDetailsMainFragment, "BusinessDetailsMainFragment");


                    OpenHome();
                     System.out.println("------------------------------------------------------------------------------------------------profiledetails");



                } else {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_favourite_grey);

                    imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);
                    txtMyEnquiry.setTextColor(getResources().getColor(R.color.colorTextHint));

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgClients.setImageResource(R.drawable.ic_profile_yellow);

                   /* BuyerPersonalDetailsFragment buyerPersonalDetailsFragment = new BuyerPersonalDetailsFragment();
                    ChangeFragments(buyerPersonalDetailsFragment, "BuyerPersonalDetailsFragment");
*/
                    BuyerOpenHome();

                    System.out.println("------------------------------------------------------------------------------------------------profiledetails");


                }


            }
        });

        mCityListModelArrayList = new ArrayList<>();
        mCityListModelArrayList.add(new CityListModel("0", "Select", ""));
        mAdapterCityList = new AdapterCityList(MainActivity.this, R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCityListModelArrayList);
        spinnerCity.setAdapter(mAdapterCityList);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mCityListModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedCity = mCityListModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSelectedCity----->" + mSelectedCountry);

                    mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY, mSelectedCity);


                    if (InternetConnection.isInternetAvailable(MainActivity.this)) {


                        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                            homeFragment.getBanners();


                        } else {

                            buyerHomeFragment.getBanners();

                        }


                    } else {

                        UtilityMethods.showInternetDialog(MainActivity.this);

                    }

                } else {
                    mSelectedCity = "0";
                    mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY, "");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mSubSubCategoryModelArrayList = new ArrayList<>();
        mAdapterSubSubCategoryList = new AdapterSubSubCategoryList(MainActivity.this, R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mSubSubCategoryModelArrayList);
        spinnerSubSubCategory.setAdapter(mAdapterSubSubCategoryList);

        spinnerSubSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mSubSubCategoryModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSubSubCategoryModelArrayList----->" + mSelectedSubSubCategory);

                    try {

                        Log.e("check", "image Url==>" + mSubSubCategoryModelArrayList.get(i).getSubcat_image());

                        Picasso.get().load(mSubSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.ic_launcher_foreground).into(ivSubSubCategory);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //setAdapter();

                    if (InternetConnection.isInternetAvailable(MainActivity.this)) {

                        //productListBuyFragment.GetProductDetails(mSelectedSubSubCategory, "");


                    } else {

                        UtilityMethods.showInternetDialog(MainActivity.this);

                    }
                } else {

                    Log.e("check", "worng");
                    //mSelectedCategory = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bundle = getIntent().getExtras();
        if (bundle != null) {

            loginUser = (String) bundle.getString("loginUser");
            redirect_type = (String) bundle.getString("redirect_type");

            if (redirect_type == null) {

                redirect_type = "";
            }

        }


        Log.e("check", "KEY_SELLER_USER_NAME========>" + mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME));


        if (redirect_type.equalsIgnoreCase("Approved")) {

            ProductListFragment productListFragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("redirect_type", "Approved");
            productListFragment.setArguments(bundle);
            Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");

        } else if (redirect_type.equalsIgnoreCase("Rejected")) {

            ProductListFragment productListFragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("redirect_type", "Rejected");
            productListFragment.setArguments(bundle);
            Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");

        } else if (redirect_type.equalsIgnoreCase("productadd")) {

            ProductListFragment productListFragment = new ProductListFragment();
            Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");

        } else if (redirect_type.equalsIgnoreCase("TenderRejected")) {


            TenderListFragment tenderListFragment = new TenderListFragment();
            Constants.mMainActivity.ChangeFragments(tenderListFragment, "TenderListFragment");

        } else if (redirect_type.equalsIgnoreCase("buyTender")) {


           /* TenderListFragment tenderListFragment = new TenderListFragment();
            Constants.mMainActivity.ChangeFragments(tenderListFragment, "TenderListFragment");
*/


            WhichTenderFragmentIsopen = "My Tender";
            TenderMainFragment tenderMainFragment = new TenderMainFragment();
            Constants.mMainActivity.ChangeFragments(tenderMainFragment, "TenderMainFragment");

        } else if (redirect_type.equalsIgnoreCase("newlead")) {

            Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";

            ShowLeadSelectedSelected();
            LeadManagmentMainFragment leadManagmentMainFragment = new LeadManagmentMainFragment();
            Constants.mMainActivity.ChangeFragments(leadManagmentMainFragment, "LeadManagmentMainFragment");

        } else if (redirect_type.equalsIgnoreCase("mylead")) {

            Constants.mMainActivity.WhichLeadFragmentIsopen = "My Leads Fragment";

            ShowLeadSelectedSelected();
            LeadManagmentMainFragment leadManagmentMainFragment = new LeadManagmentMainFragment();
            Constants.mMainActivity.ChangeFragments(leadManagmentMainFragment, "LeadManagmentMainFragment");

        } else if (redirect_type.equalsIgnoreCase("leadreceived")) {

            Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";
            ShowLeadSelectedSelected();
            LeadManagmentMainFragment leadManagmentMainFragment = new LeadManagmentMainFragment();
            Constants.mMainActivity.ChangeFragments(leadManagmentMainFragment, "LeadManagmentMainFragment");

        } else if (redirect_type.equalsIgnoreCase("newtenderslist")) {

            TenderMainFragment tenderMainFragment = new TenderMainFragment();
            Constants.mMainActivity.ChangeFragments(tenderMainFragment, "TenderMainFragment");

        } else if (redirect_type.equalsIgnoreCase("enquiry")) {


            if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                EnquiryListFragment enquiryListFragment = new EnquiryListFragment();
                Constants.mMainActivity.ChangeFragments(enquiryListFragment, "EnquiryListFragment");


            } else {

                BuyerEnquiryListFragment buyerEnquiryListFragment = new BuyerEnquiryListFragment();
                Constants.mMainActivity.ChangeFragments(buyerEnquiryListFragment, "BuyerEnquiryListFragment");
            }


        } else if (redirect_type.equalsIgnoreCase("subscription")) {

         /*   PostRequirementListFragment postRequirementListFragment = new PostRequirementListFragment();
            Constants.mMainActivity.ChangeFragments(postRequirementListFragment, "PostRequirementListFragment");
*/


            MySubscriptionMainFragment mySubscriptionMainFragment = new MySubscriptionMainFragment();
            Constants.mMainActivity.ChangeFragments(mySubscriptionMainFragment, "MySubscriptionMainFragment");

        } else if (redirect_type.equalsIgnoreCase("post requirement")) {

            PostRequirementListFragment postRequirementListFragment = new PostRequirementListFragment();
            Constants.mMainActivity.ChangeFragments(postRequirementListFragment, "PostRequirementListFragment");


        } else if (redirect_type.equalsIgnoreCase("notification")) {

            SellerNotificationFragment sellerNotificationFragment = new SellerNotificationFragment();
            Constants.mMainActivity.ChangeFragments(sellerNotificationFragment, "SellerNotificationFragment");


        } else if (redirect_type.equalsIgnoreCase("subscription_buy")) {

            MySubscriptionMainFragment mySubscriptionMainFragment = new MySubscriptionMainFragment();
            Constants.mMainActivity.ChangeFragments(mySubscriptionMainFragment, "MySubscriptionMainFragment");

        } else if (redirect_type.equalsIgnoreCase("chat_insert")) {
            relMessage.performClick();
            ChatListFragment chatListFragment = new ChatListFragment();
            Constants.mMainActivity.ChangeFragments(chatListFragment, "ChatListFragment");

        } else if (redirect_type.equalsIgnoreCase("added")) {

            ProductListFragment productListFragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("redirect_type", "Approved");
            productListFragment.setArguments(bundle);
            Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");

        } else {

            Log.e("check", "KEY_USER_TYPE" + mSessionManager.getStringData(Constants.KEY_USER_TYPE));

            if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {


                OpenHome();

            } else {

                BuyerOpenHome();

            }


        }

        //}


        //initialize drawer list adapter
        mDrawerListAdapter = new DrawerListAdapter(MainActivity.this, mNavItems);
        navList.setAdapter(mDrawerListAdapter);

        //Set drawer for seller and buyer
        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_enquiry_gray);
            imgDeliveries.setImageResource(R.drawable.my_product_grey);
            txtOrder.setText(R.string.leads);
            txtMyEnquiry.setVisibility(View.GONE);
            SetSellerDrawer();

        } else {

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_favourite_grey);
            imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);
            txtOrder.setText("Favorites");


            txtMyEnquiry.setVisibility(View.VISIBLE);

            SetBuyerDrawer();

        }


        toggle_home_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggle.performClick();

            }
        });

        toggle_home_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggle.performClick();

            }
        });

        toggle_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggle.performClick();

            }
        });

        //Open and close side drawer
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {


                    mDrawerLayout.openDrawer(GravityCompat.START);
                    SetDrawerDetails();


                } else {
                    mDrawerLayout.closeDrawer(mDrawerPane);
                }
            }
        });

        // Drawer Item click listeners
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });


        relativeLayoutProfileDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        SetDrawerDetails();


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();


                SetDrawerDetails();
                Log.e("onDrawerOpened", "onDrawerOpened");

            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };


        if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {

            getCountryList(false);

        } else {

            UtilityMethods.showInternetDialog(MainActivity.this);
        }


        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().length() > 0) {

                    try {
//                        if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
//                        {
////                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
////                            requestQueue.cancelAll("PRODUCT_DETAILS");
//                            productListBuyFragment.OFFSET = 0;
//                            productListBuyFragment.GetProductDetails("", edtSearchProduct.getText().toString());
//                        } else {
//                            UtilityMethods.showInternetDialog(MainActivity.this);
//                        }


                        if (!isTyping) {
                            Log.d(TAG, "started typing");
                            // Send notification for start typing event
                            isTyping = true;
                        }
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        isTyping = false;
                                        Log.d(TAG, "stopped typing");
                                        timer.cancel();


                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {


                                                try {
//                                                    if (!BeforeSerach.equals(edtSearchProduct.getText().toString().trim())) {
                                                        if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                                                        {
                                //                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                //                            requestQueue.cancelAll("PRODUCT_DETAILS");
                                                            productListBuyFragment.OFFSET = 0;
                                                            productListBuyFragment.GetProductDetails("search","", edtSearchProduct.getText().toString(), true);
                                                        } else {
                                                            UtilityMethods.showInternetDialog(MainActivity.this);
                                                        }

//                                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


//                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }
                                },
                                DELAY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearchProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    edtSearchProduct.setText(edtSearchProduct.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        //---------------------------------------------------------------------------------------
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {


            }

            @Override
            public void onError(int i) {

            }


            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    //editText.setText(matches.get(0));
                    //   Toast.makeText(Home_Activity.this, "" + matches.get(0));

               /* Intent intent = new Intent(Home_Activity.this, VoiceSearchActivity.class);
                startActivity(intent);*/


                    //OFFSET = 0;

               /* recycler_view.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                web_Voicesearch.setVisibility(View.GONE);
                IsVoiceSearchOpen = true;
               */
                    Log.e("chexk", "test VoiceSearch-->> " + VoiceSearch);
                VoiceSearch = matches.get(0);
                if (!VoiceSearch.equalsIgnoreCase("")) {
                    Log.e("chexk", "not null VoiceSearch-->> " + VoiceSearch);
                    txtListened.setText(VoiceSearch.substring(0, 1).toUpperCase() + VoiceSearch.substring(1));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /* Create an Intent that will start the Menu-Activity. */
                            linearLayoutContinerToast.setVisibility(View.GONE);
                            edtSearchProduct.setText(VoiceSearch);

                        }
                    }, 2000);
                } else {
                    txtListened.setText(VoiceSearch);
                    Log.e("chexk", "VoiceSearch-->> " + VoiceSearch);


                }

                //Toast.makeText(Home_Activity.this, ""+VoiceSearch);


                Log.e("VoiceSearch", "VoiceSearch " + VoiceSearch);

                //ShowToast();
                // CallVoiceSearchResultWebservice();
            }

            @Override
            public void onPartialResults(Bundle bundle) {


            }

            @Override
            public void onEvent(int i, Bundle bundle) {


            }
        });
        //---------------------------------------------------------------------------------------


        ivMicGrayFour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                /*InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    imm.hideSoftInputFromWindow(Objects.requireNonNull(MainActivity.this.getCurrentFocus()).getWindowToken(), 0);
                }*/

                try {
                    InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                VoiceSearch = "";
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //top_layout.setVisibility(View.GONE);
                        mSpeechRecognizer.stopListening();
                        // edtSearch.setHint("You will see input here");
                        Log.e("Click", "MotionEvent.ACTION_UP");
                        linearLayoutContinerToast.setVisibility(View.GONE);
                        //ShowToast();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        //top_layout.setVisibility(View.GONE);
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        Log.e("Click", "MotionEvent.ACTION_DOWN");
                       /* edtSearch.setText("");
                        edtSearch.setHint("Listening...");*/
                        linearLayoutContinerToast.setVisibility(View.VISIBLE);
                        VoiceSearch = "";
                        txtListened.setText("");
                        // Toast.makeText(Home_Activity.this, "Listening...", Toast.LENGTH_LONG).show();
                        // ShowToast();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (VoiceSearch.equalsIgnoreCase("")) {
                                    /* Create an Intent that will start the Menu-Activity. */
                                    linearLayoutContinerToast.setVisibility(View.GONE);
                                    edtSearchProduct.setText(VoiceSearch);
                                }


                            }
                        }, 5000);
                        break;
                    case MotionEvent.ACTION_BUTTON_RELEASE:
                        Log.e("Click", "ACTION_BUTTON_RELEASE");
                        break;
                }
                return false;

            }
        });
    }


    //Method for seller drawer
    public void SetSellerDrawer() {

        btnBecomeASeller.setVisibility(View.GONE);
        mNavItems.clear();


        Log.e("SELLER", "SELLER_NATURE_OF_BUSINESS " + mSessionManager.getStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS));

        if (mSessionManager.getStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS).equals("3")) {


            {

                mNavItems.add(0, new NavItem(getString(R.string.home), "", R.drawable.ic_home, true, false));
                mNavItems.add(1, new NavItem(getString(R.string.dashboard), "", R.drawable.ic_dashboard, true, false));
                mNavItems.add(2, new NavItem(getString(R.string.my_profile), "", R.drawable.ic_my_profile, false, false));
                mNavItems.add(3, new NavItem(getString(R.string.business_details), "", R.drawable.ic_business_details, false, false));
                mNavItems.add(4, new NavItem(getString(R.string.my_favourite), "", R.drawable.ic_my_favorites, false, false));
                mNavItems.add(5, new NavItem(getString(R.string.my_enquiries), "", R.drawable.ic_enquiry, false, false));
                mNavItems.add(6, new NavItem(getString(R.string.post_requirement), "", R.drawable.my_enquiries, false, false));
                mNavItems.add(7, new NavItem(getString(R.string.my_products), "", R.drawable.ic_my_products, false, false));
                mNavItems.add(8, new NavItem(getString(R.string.lead_management), "", R.drawable.ic_lead_management, false, false));
                mNavItems.add(9, new NavItem(getString(R.string.my_subscriptions), "", R.drawable.ic_my_subscription, false, false));
                mNavItems.add(10, new NavItem(getString(R.string.payment_history), "", R.drawable.ic_payment_history, false, false));
                mNavItems.add(11, new NavItem(getString(R.string.post_tender), "", R.drawable.my_enquiries, false, false));
                mNavItems.add(12, new NavItem(getString(R.string.lbl_tender), "", R.drawable.tender, false, false));
                mNavItems.add(13, new NavItem(getString(R.string.lbl_notification), "", R.drawable.ic_notification, false, false));
                mNavItems.add(14, new NavItem(getString(R.string.help_support), "", R.drawable.ic_about_us, false, false));
               /* mNavItems.add(15, new NavItem("Privacy policy", "", R.drawable.ic_privacy_policy, false, false));
                mNavItems.add(16, new NavItem("Important Links", "", R.drawable.ic_important_links, false, false));
               */
                mNavItems.add(15, new NavItem(getString(R.string.settings), "", R.drawable.settings, false, false));
            }

        } else {

            mNavItems.add(0, new NavItem(getString(R.string.home), "", R.drawable.ic_home, true, false));
            mNavItems.add(1, new NavItem(getString(R.string.dashboard), "", R.drawable.ic_dashboard, true, false));
            mNavItems.add(2, new NavItem(getString(R.string.my_profile), "", R.drawable.ic_my_profile, false, false));
            mNavItems.add(3, new NavItem(getString(R.string.business_details), "", R.drawable.ic_business_details, false, false));
            mNavItems.add(4, new NavItem(getString(R.string.my_favourite), "", R.drawable.ic_my_favorites, false, false));
            mNavItems.add(5, new NavItem(getString(R.string.my_enquiries), "", R.drawable.ic_enquiry, false, false));
            mNavItems.add(6, new NavItem(getString(R.string.post_requirement), "", R.drawable.my_enquiries, false, false));
            mNavItems.add(7, new NavItem(getString(R.string.my_products), "", R.drawable.ic_my_products, false, false));
            mNavItems.add(8, new NavItem(getString(R.string.lead_management), "", R.drawable.ic_lead_management, false, false));
            mNavItems.add(9, new NavItem(getString(R.string.my_subscriptions), "", R.drawable.ic_my_subscription, false, false));
            mNavItems.add(10, new NavItem(getString(R.string.payment_history), "", R.drawable.ic_payment_history, false, false));
            mNavItems.add(11, new NavItem(getString(R.string.lbl_tender), "", R.drawable.tender, false, false));
            mNavItems.add(12, new NavItem(getString(R.string.lbl_notification), "", R.drawable.ic_notification, false, false));
            mNavItems.add(13, new NavItem(getString(R.string.help_support), "", R.drawable.ic_about_us, false, false));
           /* mNavItems.add(14, new NavItem("Privacy policy", "", R.drawable.ic_privacy_policy, false, false));
            mNavItems.add(15, new NavItem("Important Links", "", R.drawable.ic_important_links, false, false));
            */
            mNavItems.add(14, new NavItem(getString(R.string.settings), "", R.drawable.settings, false, false));
        }

        mDrawerListAdapter.notifyDataSetChanged();

    }

    //Method for buyer drawer
    public void SetBuyerDrawer() {

        btnBecomeASeller.setVisibility(View.VISIBLE);

        mNavItems.clear();
        mNavItems.add(0, new NavItem(getString(R.string.home), "", R.drawable.ic_home, true, false));
        mNavItems.add(1, new NavItem(getString(R.string.my_profile), "", R.drawable.ic_my_profile, false, false));
        mNavItems.add(2, new NavItem(getString(R.string.my_favourite), "", R.drawable.ic_my_favorites, false, false));
        mNavItems.add(3, new NavItem(getString(R.string.my_enquiries), "", R.drawable.ic_enquiry, false, false));
        mNavItems.add(4, new NavItem(getString(R.string.post_requirement), "", R.drawable.my_enquiries, false, false));
        mNavItems.add(5, new NavItem(getString(R.string.lbl_notification), "", R.drawable.ic_notification, false, false));
        mNavItems.add(6, new NavItem(getString(R.string.help_support), "", R.drawable.ic_about_us, false, false));
      /*  mNavItems.add(7, new NavItem("Privacy policy", "", R.drawable.ic_privacy_policy, false, false));
        mNavItems.add(8, new NavItem("Important Links", "", R.drawable.ic_important_links, false, false));
      */
        mNavItems.add(7, new NavItem(getString(R.string.settings), "", R.drawable.settings, false, false));
        mDrawerListAdapter.notifyDataSetChanged();


    }

    //Method to set drawer details
    public void SetDrawerDetails() {

        if (mSessionManager.getBooleanData(Constants.IS_NOTIFICATION_RECEIVED)) {

            ShowNotificationBadge();
        } else {
            HideNotificationBadge();
        }


        String key_type = mSessionManager.getStringData(Constants.KEY_USER_TYPE);

        if (key_type.equalsIgnoreCase("seller")) {


            txtNameDrawer.setText(mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME));


            //For drawer
            try {

                Picasso.get().load(mSessionManager.getStringData(Constants.KEY_SELLER_PROFILE)).placeholder(R.drawable.default_user_profile).into(profileImageDrawer);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (key_type.equalsIgnoreCase("buyer")) {


            txtNameDrawer.setText(mSessionManager.getStringData(Constants.KEY_BUYER_USER_NAME));


            //For drawer
            try {

                Picasso.get().load(mSessionManager.getStringData(Constants.KEY_BUYER_PROFILE)).placeholder(R.drawable.default_user_profile).into(profileImageDrawer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Open dashboard on activity start
    private void OpenHome() {

        txtDashboard.setTextColor(getResources().getColor(R.color.colorBlack));
        imgDashboard.setImageResource(R.drawable.ic_home_yellow);

        txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgOrder.setImageResource(R.drawable.ic_enquiry_gray);
        txtOrder.setText(getString(R.string.leads));


        imgDeliveries.setImageResource(R.drawable.my_product_grey);

        txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgPayments.setImageResource(R.drawable.ic_message_grey);

        txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgClients.setImageResource(R.drawable.ic_profile_gry);


        HomeFragment homeFragment = new HomeFragment();
        ChangeFragments(homeFragment, "HomeFragment");

    }

    //Open dashboard on activity start for buyer
    private void BuyerOpenHome() {

        txtDashboard.setTextColor(getResources().getColor(R.color.colorBlack));
        imgDashboard.setImageResource(R.drawable.ic_home_yellow);

        txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgOrder.setImageResource(R.drawable.ic_favourite_grey);
        txtOrder.setText("Favorites");


        imgDeliveries.setImageResource(R.drawable.my_product_grey);

        txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgPayments.setImageResource(R.drawable.ic_message_grey);

        txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgClients.setImageResource(R.drawable.ic_profile_gry);


        BuyerHomeFragment buyerHomeFragment = new BuyerHomeFragment();
        ChangeFragments(buyerHomeFragment, "BuyerHomeFragment");

    }

    //Open dashboard on activity start
    public void OpenProductList() {

        txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgDashboard.setImageResource(R.drawable.ic_home_grey);

        txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgOrder.setImageResource(R.drawable.ic_favourite_grey);
        txtOrder.setText("Favorites");


        imgDeliveries.setImageResource(R.drawable.ic_my_product);

        txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgPayments.setImageResource(R.drawable.ic_message_grey);

        txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
        imgClients.setImageResource(R.drawable.ic_profile_gry);


        ProductListFragment productListFragment = new ProductListFragment();
        ChangeFragments(productListFragment, "ProductListFragment");

    }

    //Open dashboard on activity start
    public void OpenProductListSetIcon() {


        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {


            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_enquiry_gray);
            txtOrder.setText(getString(R.string.leads));


            imgDeliveries.setImageResource(R.drawable.ic_my_product);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);


        } else {


            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_favourite_grey);
            txtOrder.setText("Favorites");


            imgDeliveries.setImageResource(R.drawable.ic_my_product);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);

        }


    }


    //Open dashboard on activity start
    private void OpenDefault() {

        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_enquiry_gray);
            txtOrder.setText(getString(R.string.leads));


            imgDeliveries.setImageResource(R.drawable.my_product_grey);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);

        } else {

            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_favourite_grey);
            txtOrder.setText("Favorites");


            imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);

        }


    }


    private void selectItemFromDrawer(int position) {
        Fragment fragment = null;

        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {


            if (mSessionManager.getStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS).equals("3")) {


                {

                    switch (position) {
                        case 0:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            fragment = new HomeFragment();
                            relHome.performClick();
                            hideKeyBoard();
                            break;
                        case 1:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            fragment = new DashBoardFragment();
                            hideKeyBoard();
                            OpenDefault();
                            break;
                        case 2:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            //fragment = new PersonalDetailsFragment();
                            hideKeyBoard();
                            relProfile.performClick();
                            break;

                        case 3:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            fragment = new BusinessDetailsMainFragment();
                            hideKeyBoard();
                            OpenDefault();

                            break;

                        case 4:

                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            hideKeyBoard();

                            FavouritesFragment favouritesFragment = new FavouritesFragment();
                            Constants.mMainActivity.ChangeFragments(favouritesFragment, "FavouritesFragment");


                            break;


                        case 5:
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            fragment = new EnquiryListFragment();

                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 6:
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            fragment = new PostRequirementListFragment();

                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 7:
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            WhichFragmentIsopen = "Fragment Pending";
                            OpenProductList();
                            hideKeyBoard();
                            break;

                        case 8:
                            mDrawerLayout.closeDrawer(GravityCompat.START);


                            relFavourite.performClick();
                            break;

                        case 9:
                            mDrawerLayout.closeDrawer(GravityCompat.START);


                            //    UtilityMethods.showInfoToast(MainActivity.this,"Comming soon...");
                            MySubscriptionMainFragment mySubscriptionMainFragment = new MySubscriptionMainFragment();
                            ChangeFragments(mySubscriptionMainFragment, "MySubscriptionMainFragment");
                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 10:

                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            fragment = new SellerTransactionListFragment();

                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 11:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
//Add tender


                            fragment = new TenderListAddFragment();

                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 12:
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            fragment = new TenderMainFragment();

                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 13:
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            fragment = new SellerNotificationFragment();

                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 14:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            fragment = new HelpSupportFragment();
                            hideKeyBoard();
                            OpenDefault();
                            break;

                       /* case 15:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            fragment = new PrivacyAndPolicyFragment();
                            hideKeyBoard();
                            OpenDefault();
                            break;

                        case 16:
                            mDrawerLayout.closeDrawer(GravityCompat.START);


                            UtilityMethods.showInfoToast(MainActivity.this, "Coming Soon");


                            hideKeyBoard();
                            OpenDefault();
                            break;

*/
                        case 15:

                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            fragment = new SettingsFragment();
                            hideKeyBoard();
                            OpenDefault();

/*

                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                            builder1.setMessage("Do you really want to logout?");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                                    {

                                        getRemoveOTP();

                                    } else {

                                        UtilityMethods.showInternetDialog(MainActivity.this);
                                    }
                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
*/

                            break;


                    }
                }

            } else {

                switch (position) {
                    case 0:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new HomeFragment();
                        relHome.performClick();
                        hideKeyBoard();
                        break;
                    case 1:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new DashBoardFragment();
                        hideKeyBoard();
                        OpenDefault();
                        break;
                    case 2:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        //fragment = new PersonalDetailsFragment();
                        hideKeyBoard();
                        relProfile.performClick();
                        break;

                    case 3:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new BusinessDetailsMainFragment();
                        hideKeyBoard();
                        OpenDefault();

                        break;

                    case 4:

                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        hideKeyBoard();

                        FavouritesFragment favouritesFragment = new FavouritesFragment();
                        Constants.mMainActivity.ChangeFragments(favouritesFragment, "FavouritesFragment");


                        break;


                    case 5:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        fragment = new EnquiryListFragment();

                        hideKeyBoard();
                        OpenDefault();
                        break;

                    case 6:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        fragment = new PostRequirementListFragment();

                        hideKeyBoard();
                        OpenDefault();
                        break;

                    case 7:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        WhichFragmentIsopen = "Fragment Pending";
                        OpenProductList();
                        hideKeyBoard();
                        break;

                    case 8:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                    /*Constants.mMainActivity.WhichLeadFragmentIsopen = "My Leads Fragment";

                    LeadManagmentMainFragment leadManagmentMainFragment = new LeadManagmentMainFragment();
                    ChangeFragments(leadManagmentMainFragment, "LeadManagmentMainFragment");

                    hideKeyBoard();
                    OpenDefault();*/
                        relFavourite.performClick();
                        break;

                    case 9:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        MySubscriptionMainFragment mySubscriptionMainFragment = new MySubscriptionMainFragment();
                        ChangeFragments(mySubscriptionMainFragment, "MySubscriptionMainFragment");

                        hideKeyBoard();
                        OpenDefault();
                        break;

                    case 10:

                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        fragment = new SellerTransactionListFragment();

                        hideKeyBoard();
                        OpenDefault();
                        break;

                    case 11:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        fragment = new TenderMainFragment();

                        hideKeyBoard();
                        OpenDefault();
                        break;

                    case 12:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        fragment = new SellerNotificationFragment();

                        hideKeyBoard();
                        OpenDefault();
                        break;

                    case 13:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new HelpSupportFragment();
                        hideKeyBoard();
                        OpenDefault();
                        break;

                 /*   case 14:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new PrivacyAndPolicyFragment();
                        hideKeyBoard();
                        OpenDefault();
                        break;

                    case 15:
                        mDrawerLayout.closeDrawer(GravityCompat.START);


                        UtilityMethods.showInfoToast(MainActivity.this, "Coming Soon");


                        hideKeyBoard();
                        OpenDefault();
                        break;

*/
                    case 14:

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new SettingsFragment();
                        hideKeyBoard();
                        OpenDefault();
                       /* final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                        builder1.setMessage("Do you really want to logout?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                                {

                                    getRemoveOTP();

                                } else {

                                    UtilityMethods.showInternetDialog(MainActivity.this);
                                }
                            }
                        });
                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
*/
                        break;


                }
            }

        } else {


            switch (position) {
                case 0:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    fragment = new BuyerHomeFragment();
                    relHome.performClick();
                    hideKeyBoard();
                    break;


                case 1:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    //fragment = new PersonalDetailsFragment();
                    hideKeyBoard();
                    relProfile.performClick();
                    break;


                case 2:

                    mDrawerLayout.closeDrawer(GravityCompat.START);

                    hideKeyBoard();

                    relFavourite.performClick();


                    break;


                case 3:
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                    fragment = new BuyerEnquiryListFragment();

                    hideKeyBoard();
                    relMyProducts.performClick();
                    // OpenDefault();
                    break;

                case 4:
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                    fragment = new PostRequirementListFragment();

                    hideKeyBoard();
                    OpenDefault();
                    break;


                case 5:
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                    fragment = new SellerNotificationFragment();

                    hideKeyBoard();
                    OpenDefault();
                    break;

                case 6:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    fragment = new HelpSupportFragment();
                    hideKeyBoard();
                    OpenDefault();
                    break;

               /* case 7:
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                    PrivacyAndPolicyFragment privacyAndPolicyFragment = new PrivacyAndPolicyFragment();
                    ChangeFragments(privacyAndPolicyFragment, "PrivacyAndPolicyFragment");

                    hideKeyBoard();
                    OpenDefault();
                    break;

                case 8:
                    mDrawerLayout.closeDrawer(GravityCompat.START);


                    UtilityMethods.showInfoToast(MainActivity.this, "Coming Soon");


                    hideKeyBoard();
                    OpenDefault();
                    break;
*/
                case 7:

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    fragment = new SettingsFragment();
                    hideKeyBoard();
                    OpenDefault();

                    break;


            }

        }


        if (position != 8) {
            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainContent, fragment)
                        .addToBackStack(mNavItems.get(position).mTitle)
                        .commit();
            }
        }
        navList.setItemChecked(position, true);
        //setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    //Method to replace the fragments with it's tag and backstack for whole project.
    public void ChangeFragments(Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainContent, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    //Method to replace the fragments with it's tag and backstack for whole project.
    public void ChangeFragmentsAdd(Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.mainContent, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @OnClick({R.id.rel_home, R.id.rel_favourite, R.id.rel_my_products, R.id.rel_message, R.id.rel_profile, R.id.iv_delete_notification, R.id.img_Toolbar_filter_home_four, R.id.rel_search_home, R.id.rel_search_sub_cat, R.id.btn_become_a_seller, R.id.iv_mic_gray_four, R.id.Lnr_post_requirement})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.rel_home:
                // mToolbar.setVisibility(View.GONE);


                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgDashboard.setImageResource(R.drawable.ic_home_yellow);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_enquiry_gray);
                    txtOrder.setText(getString(R.string.leads));

                    imgDeliveries.setImageResource(R.drawable.my_product_grey);

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);

                    HomeFragment homeFragment = new HomeFragment();
                    ChangeFragments(homeFragment, "HomeFragment");

                } else {


                    txtDashboard.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgDashboard.setImageResource(R.drawable.ic_home_yellow);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_favourite_grey);

                    imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);
                    txtMyEnquiry.setTextColor(getResources().getColor(R.color.colorTextHint));

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);

//                    BuyerHomeFragment buyerHomeFragment = new BuyerHomeFragment();
//                    ChangeFragments(buyerHomeFragment, "BuyerHomeFragment");

                    HomeFragment homeFragment = new HomeFragment();
                    ChangeFragments(homeFragment, "HomeFragment");

                }


                break;
            case R.id.rel_favourite:

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgOrder.setImageResource(R.drawable.ic_enquiry_yellow);

                    imgDeliveries.setImageResource(R.drawable.my_product_grey);

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);

                    Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";

                    LeadManagmentMainFragment leadManagmentMainFragment = new LeadManagmentMainFragment();
                    ChangeFragments(leadManagmentMainFragment, "LeadManagmentMainFragment");

                } else {


                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgOrder.setImageResource(R.drawable.ic_favourite_yellow);

                    imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);
                    txtMyEnquiry.setTextColor(getResources().getColor(R.color.colorTextHint));

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);

                    BuyerFavouritesFragment buyerFavouritesFragment = new BuyerFavouritesFragment();
                    ChangeFragments(buyerFavouritesFragment, "BuyerFavouritesFragment");

                }

                break;
            case R.id.rel_my_products:

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_enquiry_gray);
                    txtOrder.setText(getString(R.string.leads));

                    imgDeliveries.setImageResource(R.drawable.ic_my_product);

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);

                    WhichFragmentIsopen = "Fragment Pending";

                    ProductListFragment productListFragment = new ProductListFragment();
                    ChangeFragments(productListFragment, "ProductListFragment");

                } else {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_favourite_grey);

                    imgDeliveries.setImageResource(R.drawable.ic_my_enquiry);
                    txtMyEnquiry.setTextColor(getResources().getColor(R.color.colorBlack));

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);


                    BuyerEnquiryListFragment enquiryListFragment = new BuyerEnquiryListFragment();
                    ChangeFragments(enquiryListFragment, "BuyerEnquiryListFragment");

                }


                break;
            case R.id.rel_message:

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_enquiry_gray);

                    imgDeliveries.setImageResource(R.drawable.my_product_grey);

                    txtPayments.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgPayments.setImageResource(R.drawable.ic_message_yellow);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);

                    ChatListFragment chatListFragment = new ChatListFragment();
                    ChangeFragments(chatListFragment, "ChatListFragment");

                } else {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_favourite_grey);

                    imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);
                    txtMyEnquiry.setTextColor(getResources().getColor(R.color.colorTextHint));

                    txtPayments.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgPayments.setImageResource(R.drawable.ic_message_yellow);

                    txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgClients.setImageResource(R.drawable.ic_profile_gry);

                    ChatListFragment chatListFragment = new ChatListFragment();
                    ChangeFragments(chatListFragment, "ChatListFragment");
                }


                break;
            case R.id.rel_profile:


                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_enquiry_gray);

                    imgDeliveries.setImageResource(R.drawable.my_product_grey);

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgClients.setImageResource(R.drawable.ic_profile_yellow);

                    //PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                    //ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");

                   HomeFragment homeFragment = new HomeFragment();
                   ChangeFragments(homeFragment, "homeFragment");


                } else {

                    txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgDashboard.setImageResource(R.drawable.ic_home_grey);

                    txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgOrder.setImageResource(R.drawable.ic_favourite_grey);

                    imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);
                    txtMyEnquiry.setTextColor(getResources().getColor(R.color.colorTextHint));

                    txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
                    imgPayments.setImageResource(R.drawable.ic_message_grey);

                    txtClients.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgClients.setImageResource(R.drawable.ic_profile_yellow);

                    /*BuyerPersonalDetailsFragment buyerPersonalDetailsFragment = new BuyerPersonalDetailsFragment();
                    ChangeFragments(buyerPersonalDetailsFragment, "BuyerPersonalDetailsFragment");*/

                    BuyerHomeFragment buyerHomeFragment = new BuyerHomeFragment();
                    ChangeFragments(buyerHomeFragment, "buyerHomeFragment");

//                    HomeFragment homeFragment = new HomeFragment();
//                    ChangeFragments(homeFragment, "homeFragment");


                }


                break;

            case R.id.iv_delete_notification:

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Do you really want to delete all the notifications?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sellerNotificationFragment.CallDeleteNotification();


                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();


                break;


            case R.id.img_Toolbar_filter_home_four:

                productListBuyFragment.IsFilterSelected = true;
                productListBuyFragment.openFilterDialog();

                break;

            case R.id.rel_search_home:
                Log.e("RESPONSE","rel_search_home");
                edtSearchProduct.requestFocus();
                openKeyboard();
                ProductListBuyFragment productListBuyFragment = new ProductListBuyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mSelectedCategory", "");
                bundle.putString("mSelectedSubCategory", "");
                bundle.putString("mSelectedSubSubCategory", "");
                bundle.putString("FromHomeScreen", "FromHomeScreen");
                productListBuyFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(productListBuyFragment, "ProductListBuyFragment");


                break;

            case R.id.rel_search_sub_cat:


                ProductListBuyFragment productListBuyFragment1 = new ProductListBuyFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("mSelectedCategory", "");
                bundle1.putString("mSelectedSubCategory", "");
                bundle1.putString("mSelectedSubSubCategory", "");
                bundle1.putString("FromHomeScreen", "FromHomeScreen");
                productListBuyFragment1.setArguments(bundle1);
                Constants.mMainActivity.ChangeFragments(productListBuyFragment1, "ProductListBuyFragment");


                break;

            case R.id.btn_become_a_seller:

                final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Are you sure you want to become a seller?");
                builder2.setCancelable(true);
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                        {

                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            CheckSeller();

                        } else {

                            UtilityMethods.showInternetDialog(MainActivity.this);
                        }


                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert12 = builder2.create();
                alert12.show();


                break;


            case R.id.Lnr_post_requirement:

                PostRequirementListFragment fragment = new PostRequirementListFragment();
                ChangeFragments(fragment, "PostRequirementListFragment");
                hideKeyBoard();
                OpenDefault();
                break;


        }
    }

    public void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtSearchProduct, InputMethodManager.SHOW_IMPLICIT);
    }

    public void SelectMessageTab() {
        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_enquiry_gray);

            imgDeliveries.setImageResource(R.drawable.my_product_grey);

            txtPayments.setTextColor(getResources().getColor(R.color.colorBlack));
            imgPayments.setImageResource(R.drawable.ic_message_yellow);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);


        } else {

            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_favourite_grey);

            imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);

            txtPayments.setTextColor(getResources().getColor(R.color.colorBlack));
            imgPayments.setImageResource(R.drawable.ic_message_yellow);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);


        }


    }

    public void setToolBarName(String toolBarName) {
        mTxt_ToolBarName.setText(toolBarName);
    }

    public void setToolBar(int toolbarone, int toolbartwo, int toolbarthree, int toolbarfour) {
        toolbarFirst.setVisibility(toolbarone);
        toolbarSecond.setVisibility(toolbartwo);
        toolbarThird.setVisibility(toolbarthree);
        toolbarFour.setVisibility(toolbarfour);
    }

    public void setNotificationButton(int deleteButton) {

        ivDeleteNotification.setVisibility(deleteButton);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {


            FragmentManager fragmentManager = getFragmentManager();

            Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainContent);

            if (currentFragment instanceof OnBackPressedListener)
                ((OnBackPressedListener) currentFragment).onBackPressed();

            Log.e("Tag", "Tag " + currentFragment.getTag());

            if (currentFragment.getTag() == null) {
                if (fragmentManager.getBackStackEntryCount() > 1) {

                    fragmentManager.popBackStack();
                } else {
                    finishAffinity();
                    System.exit(0);

                }
            } else {

                if (currentFragment.getTag().equals("AddNewProduct") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (currentFragment.getTag().equals("EnquiryFragment") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (currentFragment.getTag().equals("AddNewProductEdit") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (currentFragment.getTag().equals("BuyerPersonalDetailsFragment") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (currentFragment.getTag().equals("PersonalDetailsFragment") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (currentFragment.getTag().equals("BusinessDetailsFragment") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (currentFragment.getTag().equals("AddNewTenderFragment") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (currentFragment.getTag().equals("PostRequirementDetailsFragment") && IsBackPopupToBeDisplyed) {

//                    UtilityMethods.showBackPressDialog(MainActivity.this);
//                    IsBackPopupToBeDisplyed = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Do you want to exit?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IsBackPopupToBeDisplyed = false;
                                    sweetAlertDialog.dismissWithAnimation();
                                    onBackPressed();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    IsBackPopupToBeDisplyed = true;
                    if (fragmentManager.getBackStackEntryCount() > 1) {

                        fragmentManager.popBackStack();
                    } else {
                        finishAffinity();
                        System.exit(0);

                    }
                }
            }
            Log.e("popping BACKSTRACK===> ", "" + fragmentManager.getBackStackEntryCount());
        }
    }

    public Fragment getVisibleFragment() {
        try {
            FragmentManager fragmentManager = getFragmentManager();

            return fragmentManager.findFragmentById(R.id.mainContent);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    //Method to get Remove OTP
//    private void getRemoveOTP() {
//
//        //UtilityMethods.tuchOff(relativeLayoutAboutUs);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_OTP_EMPLTY, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("response", "getRemoveOTP -->>" + response);
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("error_code").equals("1")) {
//
//                        FragmentManager fm = getFragmentManager(); // or 'getSupportFragmentManager();'
//                        int count = fm.getBackStackEntryCount();
//                        for (int i = 0; i < count; ++i) {
//                            fm.popBackStack();
//                        }
//
//                        mSessionManager.putStringData(Constants.KEY_SELLER_USER_NAME, "");
//
//                        mSessionManager.removeAll();
//
//                        Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//
//                    } else if (jsonObject.getString("error_code").equals("0")) {
//                    } else if (jsonObject.getString("error_code").equals("2")) {
//                    } else if (jsonObject.getString("error_code").equals("10")) {
//
//                        UtilityMethods.UserInactivePopup(MainActivity.this);
//                    } else {
//                        UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // UtilityMethods.tuchOn(relativeLayoutAboutUs);
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                try {
//
//                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.check_internet_problem));
//
//                    } else if (error instanceof AuthFailureError) {
//
//                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.auth_fail));
//                    } else if (error instanceof ServerError) {
//
//                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.server_error));
//                    } else if (error instanceof NetworkError) {
//
//                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.network_error));
//                    } else if (error instanceof ParseError) {
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//                // UtilityMethods.tuchOn(relativeLayoutAboutUs);
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {
//
//                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
//
//
//                } else {
//
//                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));
//
//                }
//
//
//                Log.e("check", "======>" + params);
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//
//        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }

    //Method to hide KeyBoard
    public void hideKeyBoard() {


        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to get Country List
    public void getCountryList(boolean showDialog) {

        //UtilityMethods.tuchOff(ProgressBarMainActivity);

        //set country and city
        if (!showDialog && mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {
            //  mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY, mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY));
            mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY, "1");
            mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY, mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_CITY));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_COUNTRY_LIST_HOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getCountryList -->>" + response);

                try {

                    mCountryListModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String flag_path = jsonObject.getString("flag_path");


                        JSONArray countrylistJSONArray = jsonObject.getJSONArray("countrylist");
                        for (int i = 0; i < countrylistJSONArray.length(); i++) {

                            JSONObject countrylistJSONObject = countrylistJSONArray.getJSONObject(i);
                            String pk_id = countrylistJSONObject.getString("pk_id");
                            String country_name = countrylistJSONObject.getString("country_name");
                            String country_flag = flag_path + countrylistJSONObject.getString("country_flag");

                            CountryListBusinessModel countryListBusinessModel = new CountryListBusinessModel(pk_id, country_name, country_flag);
                            mCountryListModelArrayList.add(countryListBusinessModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                        UtilityMethods.UserInactivePopup(MainActivity.this);
                    } else {
                        UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
                    }

                    mAdapterHomeCountyBusinessList.notifyDataSetChanged();


                    String business_country;
                    if (showDialog){
                        for (int i = 0; i < mCountryListModelArrayList.size(); i++) {
                            if (mSelectedCountry.equals(mCountryListModelArrayList.get(i).getPk_id())) {

                                spinnerCountry.setSelection(i);
                                break;

                            }
                        }
                        spinnerCountry.performClick();
                    }else {
                        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    /*    business_country = mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY);

                        for (int i = 0; i < mCountryListModelArrayList.size(); i++) {

                            Log.e("compare", "" + business_country + "=======" + mCountryListModelArrayList.get(i).getPk_id());


                            if (business_country.equals(mCountryListModelArrayList.get(i).getPk_id())) {

                                spinnerCountry.setSelection(i);
                                break;

                            }


                        }*/

                            for (int i = 0; i < mCountryListModelArrayList.size(); i++) {


                                if ("1".equals(mCountryListModelArrayList.get(i).getPk_id())) {

                                    spinnerCountry.setSelection(i);
                                    break;

                                }


                            }

                        } else {

/*
                        business_country = mSessionManager.getStringData(Constants.KEY_BUYER_COUNTRY);

                        for (int i = 0; i < mCountryListModelArrayList.size(); i++) {

                            Log.e("compare", "" + business_country + "=======" + mCountryListModelArrayList.get(i).getPk_id());


                            if (business_country.equals(mCountryListModelArrayList.get(i).getCountry_name())) {

                                spinnerCountry.setSelection(i);
                                break;

                            }


                        }*/

                            for (int i = 0; i < mCountryListModelArrayList.size(); i++) {


                                if ("1".equals(mCountryListModelArrayList.get(i).getPk_id())) {

                                    spinnerCountry.setSelection(i);
                                    break;

                                }


                            }


                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                UtilityMethods.tuchOn(ProgressBarMainActivity);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(ProgressBarMainActivity);

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

        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to get City List
    private void getCityList(boolean showDialog) {

        // UtilityMethods.tuchOff(ProgressBarMainActivity);

        Log.e("check", "getCityList");
        Log.e("check", "mSelectedCountry" + mSelectedCountry);
        Log.e("check", "mSelectedCountry" + mSelectedCountry);

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

                        }


                        String business_city;
                        if (OneTimeRun) {

                            Log.e("check", "One");

                            business_city = mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_CITY);

                            OneTimeRun = false;

                        } else {

                            Log.e("check", "two");

                            business_city = mSessionManager.getStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY);

                        }
                        Log.e("check", "business_city" + business_city);

                        if (showDialog){
                            spinnerCity.performClick();
                        }
                        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                            for (int i = 0; i < mCityListModelArrayList.size(); i++) {


                                Log.e("compare", "seller--->" + mCityListModelArrayList.get(i).getPk_id() + "<===========>" + business_city);

                                if (mCityListModelArrayList.get(i).getPk_id().equalsIgnoreCase(business_city)) {


                                    spinnerCity.setSelection(i);

                                    break;

                                }

                            }

                        } else {

                            for (int i = 0; i < mCityListModelArrayList.size(); i++) {


                                Log.e("compare", "--->" + mCityListModelArrayList.get(i).getCountry_name() + "<===========>" + mSessionManager.getStringData(Constants.KEY_BUYER_CITY));

                                if (mCityListModelArrayList.get(i).getCountry_name().equalsIgnoreCase(mSessionManager.getStringData(Constants.KEY_BUYER_CITY))) {


                                    spinnerCity.setSelection(i);

                                    break;
                                }

                            }

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                        UtilityMethods.UserInactivePopup(MainActivity.this);
                    } else {
                        UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
                    }

                    mAdapterCityList.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   UtilityMethods.tuchOn(ProgressBarMainActivity);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                //  UtilityMethods.tuchOn(ProgressBarMainActivity);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("country", mSelectedCountry);

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

        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to get Subcategory Attribute
    public void getSubSubcategoryAttribute(final RelativeLayout Progressbar, final String mSelectedSubCategory, final String mSelectedSubSubCategory) {

        UtilityMethods.tuchOff(Progressbar);

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

                        UtilityMethods.UserInactivePopup(MainActivity.this);

                    } else {
                        UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
                    }


                    mAdapterSubSubCategoryList.notifyDataSetChanged();

                    for (int i = 0; i < mSubSubCategoryModelArrayList.size(); i++) {

                        Log.e("spinner", "compare" + mSubSubCategoryModelArrayList.get(i).getPk_id() + "====" + mSelectedSubSubCategory);
                        if (mSubSubCategoryModelArrayList.get(i).getPk_id().equals(mSelectedSubSubCategory)) {
                            try {
                                Picasso.get().load(mSubSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.ic_launcher_foreground).into(ivSubSubCategory);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            spinnerSubSubCategory.setSelection(i);
                            break;
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(Progressbar);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(MainActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(Progressbar);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("subcategory_id", mSelectedSubCategory);

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

        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to Resend OTP
    private void SendOTP() {

        UtilityMethods.tuchOff(ProgressBarMainActivity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RE_SEND_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "ResendOTP -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        //Start Otp Dialog

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater mInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = mInflater.inflate(R.layout.dialog_become_seller, null);
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


                        ImageView iv_cancel = alertDialog.findViewById(R.id.iv_cancel);
                        final TextView tv_email_or_mobile_otp = alertDialog.findViewById(R.id.tv_email_or_mobile_otp);
                        final TextView edt_otp_one = alertDialog.findViewById(R.id.edt_otp_one);
                        final TextView edt_otp_two = alertDialog.findViewById(R.id.edt_otp_two);
                        final TextView edt_otp_three = alertDialog.findViewById(R.id.edt_otp_three);
                        final TextView edt_otp_four = alertDialog.findViewById(R.id.edt_otp_four);
                        final EditText edt_otp = alertDialog.findViewById(R.id.edt_otp);
                        final TextView tv_timer = alertDialog.findViewById(R.id.tv_timer);
                        TextView tv_resend_otp = alertDialog.findViewById(R.id.tv_resend_otp);
                        Button btn_verify = alertDialog.findViewById(R.id.btn_verify);


                        if (mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("1")) {

                            tv_email_or_mobile_otp.setText(mSessionManager.getStringData(Constants.KEY_BUYER_EMAIL_ID));


                        } else if ((mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("2"))) {

                            tv_email_or_mobile_otp.setText(mSessionManager.getStringData(Constants.KEY_BUYER_MOBILE_NO));


                        }

                        btn_verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!edt_otp_one.getText().toString().equalsIgnoreCase("") && !edt_otp_two.getText().toString().equalsIgnoreCase("") && !edt_otp_three.getText().toString().equalsIgnoreCase("") && !edt_otp_four.getText().toString().equalsIgnoreCase("")) {
                                    if (InternetConnection.isInternetAvailable(MainActivity.this)) {


                                        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                        progressDialog.setCancelable(false);
                                        progressDialog.setMessage("Wait...");
                                        progressDialog.show();

                                        OTPVerify(edt_otp.getText().toString(), progressDialog);

                                    } else {

                                        UtilityMethods.showInternetDialog(MainActivity.this);

                                    }

                                } else {

                                    UtilityMethods.showInfoToast(MainActivity.this, "Please enter OTP");

                                }

                            }
                        });

                        edt_otp_one.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                edt_otp.requestFocus();
                                edt_otp.setSelection(edt_otp.getText().toString().trim().length());

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(edt_otp, InputMethodManager.SHOW_IMPLICIT);

                                //scroll.fullScroll(View.FOCUS_DOWN);

                            }
                        });
                        edt_otp_two.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                edt_otp.requestFocus();
                                edt_otp.setSelection(edt_otp.getText().toString().trim().length());

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(edt_otp, InputMethodManager.SHOW_IMPLICIT);

                                //  scroll.fullScroll(View.FOCUS_DOWN);

                            }
                        });
                        edt_otp_three.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                edt_otp.requestFocus();
                                edt_otp.setSelection(edt_otp.getText().toString().trim().length());

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(edt_otp, InputMethodManager.SHOW_IMPLICIT);

                                //   scroll.fullScroll(View.FOCUS_DOWN);

                            }
                        });
                        edt_otp_four.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                edt_otp.requestFocus();
                                edt_otp.setSelection(edt_otp.getText().toString().trim().length());

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(edt_otp, InputMethodManager.SHOW_IMPLICIT);

                                //   scroll.fullScroll(View.FOCUS_DOWN);

                            }
                        });


                        edt_otp.addTextChangedListener(new TextWatcher() {


                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // TODO Auto-generated method stub

                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                                // TODO Auto-generated method stub

                            }

                            public void afterTextChanged(Editable s) {
                                // TODO Auto-generated method stub


                                String EnteredOTP = edt_otp.getText().toString().trim();

                                try {
                                    edt_otp_one.setText(EnteredOTP.substring(0, 1));
                                } catch (Exception e) {
                                    edt_otp_one.setText("");

                                }
                                try {
                                    edt_otp_two.setText(EnteredOTP.substring(1, 2));
                                } catch (Exception e) {
                                    edt_otp_two.setText("");
                                }
                                try {
                                    edt_otp_three.setText(EnteredOTP.substring(2, 3));
                                } catch (Exception e) {
                                    edt_otp_three.setText("");
                                }
                                try {
                                    edt_otp_four.setText(EnteredOTP.substring(3, 4));
                                } catch (Exception e) {
                                    edt_otp_four.setText("");
                                }


                            }

                        });

                        tv_resend_otp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                edt_otp.setText("");

                                ReSendOTP();
                                ShowTimer(alertDialog, tv_timer);


                            }
                        });

                        iv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        //Show Timer for dialog dismiss
                        ShowTimer(alertDialog, tv_timer);

                        //End Otp Dialog


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                        //edt_otp.setText("");

                        UtilityMethods.showSuccessToast(MainActivity.this, "OTP sent successfully");

                    } else if (jsonObject.getString("error_code").equals("8")) {

                        UtilityMethods.showInfoToast(MainActivity.this, "This email id already registered with another country");

                    } else {
                        //UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarMainActivity);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(ProgressBarMainActivity);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                Log.e("check", "==========>" + mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE));

                if (mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("1")) {

                    params.put("emailid", mSessionManager.getStringData(Constants.KEY_BUYER_EMAIL_ID));


                } else if ((mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("2"))) {

                    params.put("mobileno", mSessionManager.getStringData(Constants.KEY_BUYER_MOBILE_NO));


                }


                params.put("usertype", "seller");


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

        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to Resend OTP
    private void ReSendOTP() {

        UtilityMethods.tuchOff(ProgressBarMainActivity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RE_SEND_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "ResendOTP -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        UtilityMethods.showSuccessToast(MainActivity.this, "OTP sent successfully");


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                        //edt_otp.setText("");

                        UtilityMethods.showSuccessToast(MainActivity.this, "OTP sent successfully");

                    } else if (jsonObject.getString("error_code").equals("10")) {


                    } else {
                        UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarMainActivity);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(ProgressBarMainActivity);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if (mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("1")) {

                    params.put("emailid", mSessionManager.getStringData(Constants.KEY_BUYER_EMAIL_ID));


                } else if ((mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("2"))) {

                    params.put("mobileno", mSessionManager.getStringData(Constants.KEY_BUYER_MOBILE_NO));


                }


                params.put("usertype", "seller");


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

        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to Check Seller
    private void CheckSeller() {

        UtilityMethods.tuchOff(ProgressBarMainActivity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_CHECK_SELLER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CheckSeller -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
                        {

                            SendOTP();

                        } else {

                            UtilityMethods.showInternetDialog(MainActivity.this);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {


                        if (mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("1")) {

                            UtilityMethods.showSuccessToast(MainActivity.this, "This email id already exits");


                        } else if ((mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("2"))) {

                            UtilityMethods.showSuccessToast(MainActivity.this, "This mobile number already exits");


                        }

                    } else if (jsonObject.getString("error_code").equals("10")) {


                    } else {
                        UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarMainActivity);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(ProgressBarMainActivity);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if (mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("1")) {

                    params.put("emailid", mSessionManager.getStringData(Constants.KEY_BUYER_EMAIL_ID));


                } else if ((mSessionManager.getStringData(Constants.KEY_LOGIN_TYPE).equals("2"))) {

                    params.put("mobileno", mSessionManager.getStringData(Constants.KEY_BUYER_MOBILE_NO));


                }

                params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                params.put("usertype", "seller");


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

        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to Resend OTP
    private void OTPVerify(final String otp, final ProgressDialog progressDialog) {

        UtilityMethods.tuchOff(ProgressBarMainActivity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_BECOME_SELLER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "OTPVerify -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_CITY, mSessionManager.getStringData(Constants.KEY_BUYER_CITY));
                        mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_COUNTY, mSessionManager.getStringData(Constants.KEY_BUYER_COUNTRY));
                        mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY, mSessionManager.getStringData(Constants.KEY_BUYER_CITY));
                        //  mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY, mSessionManager.getStringData(Constants.KEY_BUYER_COUNTRY));
                        mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY, "1");

                        mSessionManager.putStringData(Constants.KEY_SELLER_UID, mSessionManager.getStringData(Constants.KEY_BUYER_UID));
                        mSessionManager.putStringData(Constants.KEY_SELLER_USER_NAME, mSessionManager.getStringData(Constants.KEY_BUYER_USER_NAME));
                        mSessionManager.putStringData(Constants.KEY_SELLER_EMAIL_ID, mSessionManager.getStringData(Constants.KEY_BUYER_EMAIL_ID));
                        mSessionManager.putStringData(Constants.KEY_SELLER_MOBILE_NO, mSessionManager.getStringData(Constants.KEY_BUYER_MOBILE_NO));
                        mSessionManager.putStringData(Constants.KEY_SELLER_COUNTRY, mSessionManager.getStringData(Constants.KEY_BUYER_COUNTRY));
                        mSessionManager.putStringData(Constants.KEY_SELLER_COUNTRY_ID, jsonObject.getString("countryId"));

                        mSessionManager.putStringData(Constants.KEY_SELLER_CITY, mSessionManager.getStringData(Constants.KEY_BUYER_CITY));
                        mSessionManager.putStringData(Constants.KEY_SELLER_GENDER, mSessionManager.getStringData(Constants.KEY_BUYER_GENDER));
                        mSessionManager.putStringData(Constants.KEY_SELLER_DOB, mSessionManager.getStringData(Constants.KEY_BUYER_DOB));
                        mSessionManager.putStringData(Constants.KEY_SELLER_ADDRESS, mSessionManager.getStringData(Constants.KEY_BUYER_ADDRESS));
                        mSessionManager.putStringData(Constants.KEY_SELLER_PROFILE, mSessionManager.getStringData(Constants.KEY_BUYER_PROFILE));


                        mSessionManager.putStringData(Constants.KEY_USER_TYPE, "seller");

                        try {
                            mSessionManager.putStringData(Constants.USER_LANG, "");
                        }catch (Exception e){e.printStackTrace();}
                        setLocale();
                        mSessionManager.putStringData(Constants.KEY_BUYER_UID, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_USER_NAME, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_EMAIL_ID, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_MOBILE_NO, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_COUNTRY, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_CITY, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_GENDER, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_ADDRESS, "");
                        mSessionManager.putStringData(Constants.KEY_BUYER_PROFILE, "");


                        UtilityMethods.showSuccessToast(MainActivity.this, "You have successfully become a seller");


                        //Reload Activity
                        finish();
                        startActivity(getIntent());

                    } else if (jsonObject.getString("error_code").equals("0")) {


                        //edtOtp.setText("");

                        UtilityMethods.showInfoToast(MainActivity.this, "Please enter correct OTP");


                    } else if (jsonObject.getString("error_code").equals("2")) {

                        //edtOtp.setText("");

                        UtilityMethods.showInfoToast(MainActivity.this, "Please enter correct OTP");


                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.showWarningToast(MainActivity.this, "Something went wrong, please contact to admin");

                    } else {
                        UtilityMethods.showInfoToast(MainActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                UtilityMethods.tuchOn(ProgressBarMainActivity);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(MainActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                progressDialog.dismiss();
                UtilityMethods.tuchOn(ProgressBarMainActivity);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));
                params.put("otp", otp);

                /*if (!mSessionManager.getStringData(Constants.KEY_FCM_ID).equals("")) {
                    params.put("fcm_token", mSessionManager.getStringData(Constants.KEY_FCM_ID));
                } else {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    params.put("fcm_token", token);
                    mSessionManager.putStringData(Constants.KEY_FCM_ID, token);

                }*/


                Log.e("check", "params---->" + params);

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

        Volley.newRequestQueue(MainActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setLocale() {
        try {
            String lang = mSessionManager.getStringData(USER_LANG);
            if (lang.equalsIgnoreCase("Sinhala")){
                lang = "si";
            }else {
                lang = "en";
            }
            Locale myLocale = new Locale(lang);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }catch (Exception e){e.printStackTrace();}
    }

    //Method to show dialog for otp
    private void ShowTimer(final AlertDialog alertDialog, final TextView tvTimer) {

        //cancel the old countDownTimer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(600000, 1000) {

            public void onTick(long millisUntilFinished) {

                String text = String.format(Locale.getDefault(), "Time Remaining %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                tvTimer.setText(text);


            }


            public void onFinish() {
                try {
                    this.cancel();
                    alertDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
        //edtOtpOne.requestFocus();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (InternetConnection.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {

            versionCheck();

        } else {

            UtilityMethods.showInternetDialog(MainActivity.this);
        }

        RefreshBottomMenuForNotificationDot();
    }

    //MEthod to refresh the bottom menu
    public void RefreshBottomMenuForNotificationDot() {

        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            if (mSessionManager.getBooleanData(Constants.IS_NOTIFICATION_RECEIVED_LEADS)) {
                imgOrder.setImageResource(R.drawable.ic_enquiry_gray_red_dot);
            } else {
                imgOrder.setImageResource(R.drawable.ic_enquiry_gray);
            }

            if (mSessionManager.getBooleanData(Constants.IS_NOTIFICATION_RECEIVED_MESSAGE)) {
                imgPayments.setImageResource(R.drawable.ic_message_grey_red_dot);
            } else {
                imgPayments.setImageResource(R.drawable.ic_message_grey);
            }
        } else {
            if (mSessionManager.getBooleanData(Constants.IS_NOTIFICATION_RECEIVED_MESSAGE)) {
                imgPayments.setImageResource(R.drawable.ic_message_grey_red_dot);
            } else {
                imgPayments.setImageResource(R.drawable.ic_message_grey);
            }


        }

    }

    String currentVersion;

    private void versionCheck() {
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.e("currentVersion", "currentVersion " + currentVersion);
        new GetVersionCode().execute("");
    }


    //Method to show post enquiry button in toolbar
    public void setToolBarPostEnquiry() {
        LnrPostRequirement.setVisibility(View.VISIBLE);
    }

    //Method to hide post enquiry button in toolbar
    public void setToolBarPostEnquiryHide() {

        LnrPostRequirement.setVisibility(View.GONE);
    }

    public class GetVersionCode extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.tradegenie.android.tradegenieapp" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
             /*   Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
*/


            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            Log.e("currentVersion", "currentVersion " + currentVersion);
            Log.e("onlineVersion", "onlineVersion " + onlineVersion);


            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show anything
                    showForceUpdateDialog(onlineVersion);
                } else {
                   /* Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);


                    finish();*/


                }
            } else {


            }
            //Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }

        public void showForceUpdateDialog(String latestVersion) {


            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            intent.putExtra("Version", latestVersion);
            startActivity(intent);
        }


    }

    public void ProfileSelected() {


        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_enquiry_gray);

            imgDeliveries.setImageResource(R.drawable.my_product_grey);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorBlack));
            imgClients.setImageResource(R.drawable.ic_profile_yellow);


        } else {

            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgOrder.setImageResource(R.drawable.ic_favourite_grey);

            imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorBlack));
            imgClients.setImageResource(R.drawable.ic_profile_yellow);

        }


    }

    public void ShowLeadSelectedSelected() {


        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorBlack));
            imgOrder.setImageResource(R.drawable.ic_enquiry_yellow);

            imgDeliveries.setImageResource(R.drawable.my_product_grey);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);

            // Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";


        } else {


            txtDashboard.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgDashboard.setImageResource(R.drawable.ic_home_grey);

            txtOrder.setTextColor(getResources().getColor(R.color.colorBlack));
            imgOrder.setImageResource(R.drawable.ic_favourite_yellow);

            imgDeliveries.setImageResource(R.drawable.ic_my_enquiry_gray);

            txtPayments.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgPayments.setImageResource(R.drawable.ic_message_grey);

            txtClients.setTextColor(getResources().getColor(R.color.colorTextHint));
            imgClients.setImageResource(R.drawable.ic_profile_gry);


        }


    }


    //Show notification badge
    public void ShowNotificationBadge() {

        if (mSessionManager.getBooleanData(Constants.IS_NOTIFICATION_RECEIVED)) {

            if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                if (mSessionManager.getStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS).equals("3")) {
                    mNavItems.get(13).setMessageArrived(true);
                } else {
                    mNavItems.get(12).setMessageArrived(true);
                }

            } else {
                mNavItems.get(5).setMessageArrived(true);

            }


            mDrawerListAdapter.notifyDataSetChanged();
        }


        if (mSessionManager.getBooleanData(Constants.IS_NOTIFICATION_RECEIVED_LEADS)) {

            if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {


                if (mSessionManager.getStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS).equals("3")) {
                    mNavItems.get(8).setMessageArrived(true);
                } else {
                    mNavItems.get(8).setMessageArrived(true);
                }

            }


            mDrawerListAdapter.notifyDataSetChanged();
        }
    }

    //Hide notification badge
    public void HideNotificationBadge() {
        if (!mSessionManager.getBooleanData(Constants.IS_NOTIFICATION_RECEIVED)) {

            if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {


                mNavItems.get(13).setMessageArrived(false);

            } else {
                mNavItems.get(5).setMessageArrived(false);

            }


            mDrawerListAdapter.notifyDataSetChanged();
            // imgNotificationBadge.setVisibility(View.VISIBLE);
        } else {

        }
        //  imgNotificationBadge.setVisibility(View.GONE);
    }
}
