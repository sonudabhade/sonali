package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
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
import com.tradegenie.android.tradegenieapp.Activity.MainActivity;
import com.tradegenie.android.tradegenieapp.Activity.SquareCropActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCityList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCountyBusinessList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterFirmList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterNatureBusiness;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterYearList;
import com.tradegenie.android.tradegenieapp.BuildConfig;
import com.tradegenie.android.tradegenieapp.Models.BusinessNatureModel;
import com.tradegenie.android.tradegenieapp.Models.CityListModel;
import com.tradegenie.android.tradegenieapp.Models.CountryListBusinessModel;
import com.tradegenie.android.tradegenieapp.Models.DataPart;
import com.tradegenie.android.tradegenieapp.Models.FirmListModel;
import com.tradegenie.android.tradegenieapp.Models.YearModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;
import com.tradegenie.android.tradegenieapp.Utility.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
//import static com.tradegenie.android.tradegenieapp.Fragments.PersonalDetailsFragment.decodeSampledBitmapFromResource;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.URL_BUSINESS_DETAILS_ACTION;

public class BusinessDetailsFragment extends Fragment {

    @BindView(R.id.relativeLayoutBusinessDetails)
    RelativeLayout relativeLayoutBusinessDetails;
    Unbinder unbinder;
    @BindView(R.id.edt_company_name)
    EditText edtCompanyName;
    @BindView(R.id.spinner_firm)
    Spinner spinnerFirm;
    @BindView(R.id.iv_view)
    View ivView;
    @BindView(R.id.iv_drop_down)
    ImageView ivDropDown;
    @BindView(R.id.spinner_nature_of_business)
    Spinner spinnerNatureOfBusiness;
    @BindView(R.id.edt_no_employee)
    EditText edtNoEmployee;
    @BindView(R.id.edt_annual_turnover)
    EditText edtAnnualTurnover;
    @BindView(R.id.edt_business_mobile_no)
    EditText edtBusinessMobileNo;
    @BindView(R.id.edt_gst_no)
    EditText edtGstNo;
    @BindView(R.id.edt_tax_no)
    EditText edtTaxNo;
    @BindView(R.id.edt_iec_no)
    EditText edtIecNo;
    @BindView(R.id.edt_cin_no)
    EditText edtCinNo;
    @BindView(R.id.edt_tan_no)
    EditText edtTanNo;
    @BindView(R.id.edt_email_id)
    EditText edtEmailId;
    @BindView(R.id.edt_pin_code_no)
    EditText edtPinCodeNo;
    @BindView(R.id.edt_website)
    EditText edtWebsite;
    @BindView(R.id.spinner_city)
    Spinner spinnerCity;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_about_company)
    EditText edtAboutCompany;
    @BindView(R.id.iv_profile_pic)
    CircleImageView ivProfilePic;
    @BindView(R.id.rel_profile_pic)
    RelativeLayout relProfilePic;
    @BindView(R.id.edt_agent_code)
    EditText edtAgentCode;
    @BindView(R.id.btn_submit_business_details)
    Button btnSubmitBusinessDetails;
    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;
    @BindView(R.id.rel_spinner_firm)
    RelativeLayout relSpinnerFirm;
    @BindView(R.id.rel_nature_of_business)
    RelativeLayout relNatureOfBusiness;
    @BindView(R.id.rel_country)
    RelativeLayout relCountry;
    @BindView(R.id.rel_city)
    RelativeLayout relCity;

    String fk_city_id = "";
    @BindView(R.id.spinner_year)
    Spinner spinnerYear;
    @BindView(R.id.iv_view_year)
    View ivViewYear;
    @BindView(R.id.iv_drop_down_year)
    ImageView ivDropDownYear;
    @BindView(R.id.rel_spinner_year)
    RelativeLayout relSpinnerYear;
    @BindView(R.id.iv_view_country)
    View ivViewCountry;
    @BindView(R.id.iv_drop_down_country)
    ImageView ivDropDownCountry;
    @BindView(R.id.edt_whats_app_number)
    EditText edtWhatsAppNumber;
    @BindView(R.id.edt_viber_number)
    EditText edtViberNumber;

    private SessionManager mSessionManager;

    //camera and gallery
    private Uri fileUri;
    private static final int TAKE_PICTURE = 1, SELECT_PICTURE = 2;
    private static final int CROPPED_IMAGE = 3;

    private static final String IMAGE_DIRECTORY_NAME = "TradeGenie";
    //close camera and gallery

    private ArrayList<CountryListBusinessModel> mCountryListModelArrayList;
    private AdapterCountyBusinessList mAdapterCountyList;
    private String mSelectedCountry = "";

    private ArrayList<CityListModel> mCityListModelArrayList;
    private AdapterCityList mAdapterCityList;
    private String mSelectedCity;

    private ArrayList<FirmListModel> mFirmListModelArrayList;
    private AdapterFirmList mAdapterFirmList;
    private String mSelectedFrim;


    private ArrayList<BusinessNatureModel> mBusinessNatureModelArrayList;
    private AdapterNatureBusiness mAdapterNatureBusiness;
    private String mSelectedNatureBusiness;


    private ArrayList<YearModel> mYearModelArrayList;
    private AdapterYearList mAdapterYearList;
    private String mSelectedYear = "Select";


    public BusinessDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bussiness_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.business_details));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();

        Constants.mMainActivity.thumbnail_r = null;

        mSessionManager = new SessionManager(getActivity());


        mCountryListModelArrayList = new ArrayList<>();
        mCountryListModelArrayList.add(new CountryListBusinessModel("0", "Select", ""));
        mAdapterCountyList = new AdapterCountyBusinessList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCountryListModelArrayList);
        spinnerCountry.setAdapter(mAdapterCountyList);

        mCityListModelArrayList = new ArrayList<>();
        mCityListModelArrayList.add(new CityListModel("0", "Select", ""));
        mAdapterCityList = new AdapterCityList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCityListModelArrayList);
        spinnerCity.setAdapter(mAdapterCityList);

        mFirmListModelArrayList = new ArrayList<>();
        mFirmListModelArrayList.add(new FirmListModel("0", "Select"));
        mAdapterFirmList = new AdapterFirmList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mFirmListModelArrayList);
        spinnerFirm.setAdapter(mAdapterFirmList);

        mBusinessNatureModelArrayList = new ArrayList<>();
        mBusinessNatureModelArrayList.add(new BusinessNatureModel("0", "Select "));
        mAdapterNatureBusiness = new AdapterNatureBusiness(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mBusinessNatureModelArrayList);
        spinnerNatureOfBusiness.setAdapter(mAdapterNatureBusiness);


        mYearModelArrayList = new ArrayList<>();
        mYearModelArrayList.add(new YearModel("Select"));
        mAdapterYearList = new AdapterYearList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mYearModelArrayList);
        spinnerYear.setAdapter(mAdapterYearList);


        final ArrayList<String> mYearList = new ArrayList<>();
        Log.e("YEAR", "YEAR " + Calendar.getInstance().get(Calendar.YEAR));
        for (int i = 1900; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            mYearList.add(String.valueOf(i));
        }
        Collections.reverse(mYearList);
        for (int j = 0; j < mYearList.size(); j++) {
            Log.e("YearList", "YearList " + mYearList.get(j));
            YearModel yearModel = new YearModel(mYearList.get(j));
            mYearModelArrayList.add(yearModel);

        }

        mAdapterYearList.notifyDataSetChanged();

        if (InternetConnection.isInternetAvailable(getActivity())) {

            getFirmList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mCountryListModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedCountry = mCountryListModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSelectedCountry----->" + mSelectedCountry);
                    if (InternetConnection.isInternetAvailable(getActivity())) {


                        mCityListModelArrayList.clear();
                        mCityListModelArrayList.add(new CityListModel("0", "Select", ""));

                        spinnerCity.setSelection(0);


                        getCityList("");


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }
                } else {
                    mSelectedCountry = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mCityListModelArrayList.get(i).getCountry_name().equalsIgnoreCase("0")) {
                    mSelectedCity = mCityListModelArrayList.get(i).getPk_id();
                    Log.e("check", "City----->" + mSelectedCity);
                } else {
                    mSelectedCity = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFirm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mFirmListModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedFrim = mFirmListModelArrayList.get(i).getPk_id();
                    Log.e("check", "Firm----->" + mSelectedFrim);
                } else {
                    mSelectedFrim = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerNatureOfBusiness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mBusinessNatureModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedNatureBusiness = mBusinessNatureModelArrayList.get(i).getPk_id();
                    Log.e("mSelectedNatureBusiness", "mSelectedNatureBusiness----->" + mSelectedNatureBusiness);
                } else {
                    mSelectedNatureBusiness = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mYearModelArrayList.get(i).getYear().equalsIgnoreCase("Select")) {
                    mSelectedYear = mYearModelArrayList.get(i).getYear();
                    Log.e("check", "mSelectedCountry----->" + mSelectedCountry);

                } else {
                    mSelectedCountry = "Select";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerYear.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });

        spinnerCity.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });

        spinnerCountry.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });

        spinnerFirm.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });

        spinnerNatureOfBusiness.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PICTURE:
                Bitmap bitmap = null;
                if (resultCode == RESULT_OK) {
                    if (data != null) {


                        try {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = getActivity().getContentResolver().query(
                                    selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();
                            //profileImage.setVisibility(View.VISIBLE);
                            Log.e("picturePath", "picturePath " + picturePath);
                            // Bitmap thumbnail =
                            // (BitmapFactory.decodeFile(picturePath));

                            /*Bitmap thumbnail = decodeSampledBitmapFromResource(
                                   picturePath, 500, 500);
*/
                            // rotated

                            //Constants.mMainActivity.thumbnail_r = imageOreintationValidator(thumbnail, picturePath);

                            //ivProfilePic.setBackground(null);
                            //ivProfilePic.setImageBitmap(Constants.mMainActivity.thumbnail_r);
                            //IsImageSet = true;

                            GetCroppedImage();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {

                    previewCapturedImage();

                }

                break;
            case CROPPED_IMAGE:
                ivProfilePic.setBackground(null);

                if (Constants.mMainActivity.thumbnail_r != null) {

                    ivProfilePic.setImageBitmap(Constants.mMainActivity.thumbnail_r);


                }


                break;
        }
    }

    //Method to getPersonalDetails
    private void getBusinessDetails() {

        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_BUSINESS_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getBusinessDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        edtCompanyName.setText(jsonObject.getString("comp_name"));
                        edtNoEmployee.setText(jsonObject.getString("employee"));
                        edtAnnualTurnover.setText(jsonObject.getString("annual_turnover"));
                        edtBusinessMobileNo.setText(jsonObject.getString("mobile_no"));
                        edtGstNo.setText(jsonObject.getString("gst_no"));
                        edtTaxNo.setText(jsonObject.getString("tax_no"));
                        edtIecNo.setText(jsonObject.getString("iec_no"));
                        edtCinNo.setText(jsonObject.getString("cin_no"));
                        edtTanNo.setText(jsonObject.getString("tan_no"));
                        edtEmailId.setText(jsonObject.getString("email_id"));
                        edtPinCodeNo.setText(jsonObject.getString("pincode"));
                        edtWebsite.setText(jsonObject.getString("cmp_webname"));
                        edtAddress.setText(jsonObject.getString("address"));
                        edtAboutCompany.setText(jsonObject.getString("about_company"));
                        edtAgentCode.setText(jsonObject.getString("agent_code"));
                        edtWhatsAppNumber.setText(jsonObject.getString("watsp_number"));
                        edtViberNumber.setText(jsonObject.getString("viber_number"));

                        fk_city_id = jsonObject.getString("fk_city_id");
                        mSelectedCity = jsonObject.getString("fk_city_id");

                        mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_CITY, mSelectedCity);


                        String fk_firm_id = jsonObject.getString("fk_firm_id");
                        Log.e("Pratibha", "--------------------------------------------");
                        for (int i = 0; i < mFirmListModelArrayList.size(); i++) {

                            Log.e("Pratibha", "firm----------" + fk_firm_id + " " + mFirmListModelArrayList.get(i).getPk_id());

                            if (mFirmListModelArrayList.get(i).getPk_id().equalsIgnoreCase(fk_firm_id)) {


                                spinnerFirm.setSelection(i);


                            }

                        }
                        Log.e("Pratibha", "--------------------------------------------");


                        String fk_nb_id = jsonObject.getString("fk_nb_id");

                        mSessionManager.putStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS, fk_nb_id);
                        Log.e("Pratibha", "--------------------------------------------");
                        for (int i = 0; i < mBusinessNatureModelArrayList.size(); i++) {

                            Log.e("Pratibha", "nature----------" + fk_nb_id + " " + mBusinessNatureModelArrayList.get(i).getNature_business());

                            if (mBusinessNatureModelArrayList.get(i).getPk_id().equalsIgnoreCase(fk_nb_id)) {


                                spinnerNatureOfBusiness.setSelection(i);


                            }

                        }
                        Log.e("Pratibha", "--------------------------------------------");

                        String fk_conuntry_id = jsonObject.getString("fk_conuntry_id");


                        mSelectedCountry = jsonObject.getString("fk_conuntry_id");
                        if (mSelectedCountry.equals("") || mSelectedCountry == null) {
                            spinnerCountry.setEnabled(true);
                        } else {
                            spinnerCountry.setEnabled(false);
                        }


                        mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_COUNTY, mSelectedCountry);


                        //Set country spinner
                        for (int h = 0; h < mCountryListModelArrayList.size(); h++) {

                            if (mSelectedCountry.equals(mCountryListModelArrayList.get(h).getPk_id())) {

                                spinnerCountry.setSelection(h);
                            }
                        }

/*
                        Log.e("check", "------------>" + fk_city_id);
                        if (fk_city_id.equalsIgnoreCase("")) {


                        } else {

                            getCityList(fk_city_id);
                        }*/


                        /*//Fix Country
                        if (!jsonObject.getString("fk_conuntry_id").equals("")) {

                            Log.e("check", "enter========>");

                            spinnerCountry.setEnabled(false);
                            spinnerCountry.setClickable(false);
                            ivViewCountry.setVisibility(GONE);
                            ivDropDownCountry.setVisibility(GONE);
                            relCountry.setEnabled(false);

                        }*/

                        Log.e("Pratibha", "--------------------------------------------");
                        for (int i = 0; i < mCountryListModelArrayList.size(); i++) {

                            Log.e("Pratibha", "country ----------" + fk_conuntry_id + " " + mCountryListModelArrayList.get(i).getPk_id());

                            if (mCountryListModelArrayList.get(i).getPk_id().equalsIgnoreCase(fk_conuntry_id)) {


                                spinnerCountry.setSelection(i);


                            }

                        }
                        Log.e("Pratibha", "--------------------------------------------");


                        String profile_image = jsonObject.getString("profile_image");
                        String profile = jsonObject.getString("image");

                        Log.e("check", "====>" + profile_image + profile);

                        try {
                            Picasso.get().load(profile_image + profile).placeholder(R.drawable.profile_image_change).into(ivProfilePic);
                        } catch (Exception e) {

                        }

                        //////**********/////

                        String established_year = jsonObject.getString("established_year");

                        for (int i = 0; i < mYearModelArrayList.size(); i++) {


                            if (mYearModelArrayList.get(i).getYear().equals(established_year)) {


                                spinnerYear.setSelection(i);


                            }

                        }


                        /******/


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

                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);


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
                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                //params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                //params.put("usertype", mSessionManager.getStringData(Constants.KEY_SELLER_USER_TYPE));

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

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to getPersonalDetails
    private void getBusinessDetailsAfterUpdate() {

        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_BUSINESS_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getPersonalDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        edtCompanyName.setText(jsonObject.getString("comp_name"));
                        edtNoEmployee.setText(jsonObject.getString("employee"));
                        edtAnnualTurnover.setText(jsonObject.getString("annual_turnover"));
                        edtBusinessMobileNo.setText(jsonObject.getString("mobile_no"));
                        edtGstNo.setText(jsonObject.getString("gst_no"));
                        edtTaxNo.setText(jsonObject.getString("tax_no"));
                        edtIecNo.setText(jsonObject.getString("iec_no"));
                        edtCinNo.setText(jsonObject.getString("cin_no"));
                        edtTanNo.setText(jsonObject.getString("tan_no"));
                        edtEmailId.setText(jsonObject.getString("email_id"));
                        edtPinCodeNo.setText(jsonObject.getString("pincode"));
                        edtWebsite.setText(jsonObject.getString("cmp_webname"));
                        edtAddress.setText(jsonObject.getString("address"));
                        edtAboutCompany.setText(jsonObject.getString("about_company"));
                        edtAgentCode.setText(jsonObject.getString("agent_code"));
                        edtWhatsAppNumber.setText(jsonObject.getString("watsp_number"));
                        edtViberNumber.setText(jsonObject.getString("viber_number"));

                        fk_city_id = jsonObject.getString("fk_city_id");
                        mSelectedCity = jsonObject.getString("fk_city_id");

                        mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_CITY, mSelectedCity);


                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            startActivity(new Intent(getActivity(), MainActivity.class));


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }


                        String fk_firm_id = jsonObject.getString("fk_firm_id");
                        Log.e("Pratibha", "--------------------------------------------");
                        for (int i = 0; i < mFirmListModelArrayList.size(); i++) {

                            Log.e("Pratibha", "firm----------" + fk_firm_id + " " + mFirmListModelArrayList.get(i).getPk_id());

                            if (mFirmListModelArrayList.get(i).getPk_id().equalsIgnoreCase(fk_firm_id)) {


                                spinnerFirm.setSelection(i);


                            }

                        }
                        Log.e("Pratibha", "--------------------------------------------");


                        String fk_nb_id = jsonObject.getString("fk_nb_id");
                        Log.e("Pratibha", "--------------------------------------------");
                        for (int i = 0; i < mBusinessNatureModelArrayList.size(); i++) {

                            Log.e("Pratibha", "nature----------" + fk_nb_id + " " + mBusinessNatureModelArrayList.get(i).getNature_business());

                            if (mBusinessNatureModelArrayList.get(i).getPk_id().equalsIgnoreCase(fk_nb_id)) {


                                spinnerNatureOfBusiness.setSelection(i);


                            }

                        }
                        Log.e("Pratibha", "--------------------------------------------");

                        String fk_conuntry_id = jsonObject.getString("fk_conuntry_id");


                        mSelectedCountry = jsonObject.getString("fk_conuntry_id");
                        if (mSelectedCountry.equals("") || mSelectedCountry == null) {
                            spinnerCountry.setEnabled(true);
                        } else {
                            spinnerCountry.setEnabled(false);
                        }

                        mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_COUNTY, mSelectedCountry);


                        //Set country spinner
                        for (int h = 0; h < mCountryListModelArrayList.size(); h++) {

                            if (mSelectedCountry.equals(mCountryListModelArrayList.get(h).getPk_id())) {

                                spinnerCountry.setSelection(h);
                            }
                        }

/*
                        Log.e("check", "------------>" + fk_city_id);
                        if (fk_city_id.equalsIgnoreCase("")) {


                        } else {

                            getCityList(fk_city_id);
                        }*/


                        Log.e("Pratibha", "--------------------------------------------");
                        for (int i = 0; i < mCountryListModelArrayList.size(); i++) {

                            Log.e("Pratibha", "country ----------" + fk_conuntry_id + " " + mCountryListModelArrayList.get(i).getPk_id());

                            if (mCountryListModelArrayList.get(i).getPk_id().equalsIgnoreCase(fk_conuntry_id)) {


                                spinnerCountry.setSelection(i);


                            }

                        }
                        Log.e("Pratibha", "--------------------------------------------");


                        String profile_image = jsonObject.getString("profile_image");
                        String profile = jsonObject.getString("image");

                        Log.e("check", "====>" + profile_image + profile);

                        try {
                            Picasso.get().load(profile_image + profile).placeholder(R.drawable.profile_image_change).into(ivProfilePic);
                        } catch (Exception e) {

                        }

                        //////**********/////

                        String established_year = jsonObject.getString("established_year");
                        Log.e("Pratibha", "--------------------------------------------");
                        for (int i = 0; i < mYearModelArrayList.size(); i++) {

                            Log.e("Pratibha", "nature----------" + established_year + " " + mBusinessNatureModelArrayList.get(i).getNature_business());

                            if (mYearModelArrayList.get(i).getYear().equals(established_year)) {


                                spinnerYear.setSelection(i);


                            }

                        }


                        /******/


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

                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);


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
                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                //params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                //params.put("usertype", mSessionManager.getStringData(Constants.KEY_SELLER_USER_TYPE));

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

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //Method to show options for camera anf gallery
    private void ShowCameraGallryOption() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Select image");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dialog.cancel();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        /*
                         * File photo = new
                         * File(Environment.getExternalStorageDirectory(),
                         * "Pic.jpg"); intent.putExtra(MediaStore.EXTRA_OUTPUT,
                         * Uri.fromFile(photo)); imageUri = Uri.fromFile(photo);
                         */
                        // startActivityForResult(intent,TAKE_PICTURE);

                        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Log.e("test", "file->" + fileUri);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);


                        intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intents, TAKE_PICTURE);


                    }
                });

        builder1.setNegativeButton(
                "Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Picture"),
                                SELECT_PICTURE);

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    //----------------------------------camera and gallery code start ------------------------------------------------------------------
    public Uri getOutputMediaFileUri(int type) {
        Log.e("test", "type==>>" + type);
        // return Uri.fromFile(getOutputMediaFile(type));
        //   return FileProvider.getUriForFile(getActivity(), getApplicationContext().getPackageName() + ".my.package.name.provider", getOutputMediaFile(type));


        return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            source.recycle();
            Date d = new Date();
            CharSequence s = DateFormat
                    .format("MM-dd-yy-hh-mm-ss", d.getTime());
            String fullPath = Environment.getExternalStorageDirectory()
                    + "/Agropost/" + s.toString() + ".jpg";
            if ((fullPath != null) && (new File(fullPath).exists())) {
                new File(fullPath).delete();
            }
            bitmap = null;
            err.printStackTrace();
        }
        return bitmap;
    }

    //Method to get cropped image
    private void GetCroppedImage() {

        Intent intent = new Intent(getActivity(), SquareCropActivity.class);
        startActivityForResult(intent, CROPPED_IMAGE);
    }

    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);


            Constants.mMainActivity.thumbnail_r = imageOreintationValidator(bitmap, fileUri.getPath());


            ivProfilePic.setBackground(null);


            GetCroppedImage();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {

        if (bitmap != null) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            System.out.println(" with image:- " + byteArrayOutputStream.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            System.out.println(" with image:- " + byteArrayOutputStream.toByteArray());
            return byteArrayOutputStream.toByteArray();

        }


    }

    private void CallUpdateProfileWebservice() {

        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_BUSINESS_DETAILS_ACTION,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // pDialog.dismiss();
                        Log.e("response", "response of update business details--> " + new String(response.data));
                        try {

                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            if (jsonObject.getString("error_code").equals("1")) {

                                mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_NAME, jsonObject.getString("comp_name"));
                                mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_COUNTY, jsonObject.getString("fk_conuntry_id"));
                                mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_CITY, jsonObject.getString("fk_city_id"));
                                mSessionManager.putStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS, jsonObject.getString("fk_nb_id"));


                                UtilityMethods.showSuccessToast(getActivity(), "Business Details saved successfully");

                                Constants.mMainActivity.SetSellerDrawer();
                                /*HomeFragment homeFragment=new HomeFragment();
                                Constants.mMainActivity.ChangeFragments(homeFragment,"HomeFragment");*/

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);

                            } else if (jsonObject.getString("error_code").equals("10")) {
                                UtilityMethods.UserInactivePopup(getActivity());

                            } else {
                                UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                        UtilityMethods.tuchOn(relativeLayoutBusinessDetails);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());


                        // pDialog.dismiss();


                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            UtilityMethods.showInfoToast(getActivity(), "Sorry, Please Check your Internet Connection.");
                        } else if (error instanceof AuthFailureError) {
                            UtilityMethods.showInfoToast(getActivity(), "Sorry, AuthFailureError.");
                            //TODO
                        } else if (error instanceof ServerError) {
                            UtilityMethods.showInfoToast(getActivity(), "Sorry, ServerError.");
                            //TODO
                        } else if (error instanceof NetworkError) {
                            UtilityMethods.showInfoToast(getActivity(), "Sorry, NetworkError.");
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }


                        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);


                    }
                }) {


            //* If you want to add more parameters with the image
            //* you can do it here
            //* here we have only one parameter with the image
            // which is tags
            // *
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("compname", edtCompanyName.getText().toString());
                params.put("estbyear", mSelectedYear);
                params.put("firm", mSelectedFrim);
                params.put("bnature", mSelectedNatureBusiness);
                params.put("employee", edtNoEmployee.getText().toString().trim());
                params.put("aturnover", edtAnnualTurnover.getText().toString().trim());
                params.put("mobileno", edtBusinessMobileNo.getText().toString());
                params.put("country", mSelectedCountry);
                params.put("gstno", edtGstNo.getText().toString());
                params.put("taxno", edtTaxNo.getText().toString().trim());
                params.put("iecno", edtIecNo.getText().toString().trim());
                params.put("cinno", edtCinNo.getText().toString().trim());
                params.put("tanno", edtTanNo.getText().toString().trim());
                params.put("emailid", edtEmailId.getText().toString().trim());
                params.put("pincode", edtPinCodeNo.getText().toString().trim());
                params.put("webname", edtWebsite.getText().toString().trim());
                params.put("city", mSelectedCity);
                params.put("address", edtAddress.getText().toString().trim());
                params.put("cmpabout", edtAboutCompany.getText().toString().trim());
                params.put("agentcode", edtAgentCode.getText().toString().trim());
                params.put("whatspno", edtWhatsAppNumber.getText().toString().trim());
                params.put("viberno", edtViberNumber.getText().toString().trim());
                Log.e(" params", " params " + params.toString());

               // Log.e("check save business details", "========>" + params);

                return params;
            }


            // Here we are passing image by renaming it with a unique name

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                System.out.println("Size of Parameters in start :-" + params.size());

                Log.e("check", "=====>" + Constants.mMainActivity.thumbnail_r);

                if (Constants.mMainActivity.thumbnail_r != null) {
                    long imagename = System.currentTimeMillis();
                    params.put("profile", new DataPart(imagename + ".png", getFileDataFromDrawable(Constants.mMainActivity.thumbnail_r)));
                    System.out.println("Size of Parameters in if :-" + params.size());
                    return params;
                } else {
                    //   params=null;
                    params.remove("profile");
                    System.out.println("Size of Parameters in else :-" + params.size());
                    return params;
                }


            }


        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);

    }

    @OnClick({R.id.rel_profile_pic, R.id.btn_submit_business_details, R.id.rel_spinner_firm, R.id.rel_nature_of_business, R.id.rel_country, R.id.rel_city, R.id.rel_spinner_year})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_profile_pic:
                ShowCameraGallryOption();
                break;
            case R.id.btn_submit_business_details:

                if (validation()) {
                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        keyBoardHide();

                        CallUpdateProfileWebservice();


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }
                    Log.e("test", "Success---->>> ");
                } else {

                    Log.e("test", "Failed---->>> ");
                }


                break;

            case R.id.rel_spinner_firm:

                spinnerFirm.performClick();
                keyBoardHide();

                break;

            case R.id.rel_nature_of_business:

                spinnerNatureOfBusiness.performClick();
                keyBoardHide();


                break;
            case R.id.rel_country:

                if (mSelectedCountry.equals("") || mSelectedCountry == null) {
                    spinnerCountry.performClick();
                    keyBoardHide();
                    spinnerCountry.setEnabled(true);
                } else {
                    spinnerCountry.setEnabled(false);
                }

                break;
            case R.id.rel_city:

                spinnerCity.performClick();
                keyBoardHide();

                break;

            case R.id.rel_spinner_year:

                spinnerYear.performClick();
                keyBoardHide();

                break;
        }
    }

    private boolean validation() {

        Log.e("check", "=========================>" + mSelectedCountry);

        if (edtCompanyName.getText().toString().trim().equals("")) {
            edtCompanyName.setError("Please enter company name");
            edtCompanyName.requestFocus();
            return false;
        }/* else if (mSelectedYear.equals("Select")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select year");
            return false;
        } else if (mSelectedFrim.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select Legal status of Firm");
            return false;
        }*/ else if (mSelectedNatureBusiness.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select nature of business");
            return false;
        } else if (mSelectedCountry.equalsIgnoreCase("0") || mSelectedCountry.equalsIgnoreCase("")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select country");
            return false;
        } else if (mSelectedCity.equalsIgnoreCase("0") || mSelectedCity.equalsIgnoreCase("")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select city");
            return false;
        }/* else if (edtAddress.getText().toString().trim().equalsIgnoreCase("")) {
            UtilityMethods.showInfoToast(getActivity(), "Please enter address.");
            return false;
        } else if (!edtEmailId.getText().toString().equalsIgnoreCase("") && !UtilityMethods.isValidEmail(edtEmailId.getText().toString().trim())) {

            edtEmailId.setError("Please enter valid email address");
            edtEmailId.requestFocus();
            return false;

        }*/ else if (edtBusinessMobileNo.getText().toString().equals("") || (edtBusinessMobileNo.getText().length() < 10)) {

            edtBusinessMobileNo.setError("Please enter valid mobile no.");
            edtBusinessMobileNo.requestFocus();
            return false;

        }/* else if (!edtWhatsAppNumber.getText().toString().equals("") && (edtWhatsAppNumber.getText().length() < 9)) {

            edtWhatsAppNumber.setError("Please enter valid whatsapp no.");
            edtWhatsAppNumber.requestFocus();
            return false;

        } else if (!edtViberNumber.getText().toString().equals("") && (edtViberNumber.getText().length() < 9)) {

            edtViberNumber.setError("Please enter valid viber no.");
            edtViberNumber.requestFocus();
            return false;

        } else if (!edtWebsite.getText().toString().equals("") && !URLUtil.isValidUrl(edtWebsite.getText().toString())) {

            edtWebsite.setError("Enter valid website URL");
            edtWebsite.requestFocus();

            return false;
        }*/
        return true;
    }

    //MEthod to get Firm List
    private void getFirmList() {

        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FIRM_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getFirmList -->>" + response);

                try {
                    mFirmListModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONArray JSONArrayfirmlist = jsonObject.getJSONArray("firmlist");

                        mFirmListModelArrayList.add(new FirmListModel("0", "Select"));
                        for (int i = 0; i < JSONArrayfirmlist.length(); i++) {

                            JSONObject firmlistJSONObject = JSONArrayfirmlist.getJSONObject(i);

                            String pk_id = firmlistJSONObject.getString("pk_id");
                            String legal_firm = firmlistJSONObject.getString("legal_firm");


                            FirmListModel firmListModel = new FirmListModel(pk_id, legal_firm);
                            mFirmListModelArrayList.add(firmListModel);


                        }

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            getNatureBusinessList();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterFirmList.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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
                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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

    //MEthod to get Nature Business List
    private void getNatureBusinessList() {

        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NATURE_BUSINESS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getNatureBusinessList -->>" + response);

                try {
                    mBusinessNatureModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONArray JSONArraybusiness_nature = jsonObject.getJSONArray("businessnature");
                        mBusinessNatureModelArrayList.add(new BusinessNatureModel("0", "Select"));
                        for (int i = 0; i < JSONArraybusiness_nature.length(); i++) {

                            JSONObject firmlistJSONObject = JSONArraybusiness_nature.getJSONObject(i);

                            String pk_id = firmlistJSONObject.getString("pk_id");
                            String nature_business = firmlistJSONObject.getString("nature_business");


                            BusinessNatureModel businessNatureModel = new BusinessNatureModel(pk_id, nature_business);
                            mBusinessNatureModelArrayList.add(businessNatureModel);

                        }

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            getCountryList();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterNatureBusiness.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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
                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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

    //MEthod to get Country List
    private void getCountryList() {

        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_COUNTRY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getCountryList -->>" + response);

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
                        }


                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            getBusinessDetails();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterCountyList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                // UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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
                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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

    //MEthod to get City List
    private void getCityList(final String fk_city_id) {

        UtilityMethods.tuchOff(relativeLayoutBusinessDetails);

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

                        for (int i = 0; i < mCityListModelArrayList.size(); i++) {


                            Log.e("loop", "select City" + mCityListModelArrayList.get(i).getPk_id() + "====" + fk_city_id);

                            if (mCityListModelArrayList.get(i).getPk_id().equalsIgnoreCase(mSelectedCity)) {


                                spinnerCity.setSelection(i);


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
                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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
                UtilityMethods.tuchOn(relativeLayoutBusinessDetails);

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

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void keyBoardHide() {

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
