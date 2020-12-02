package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.tradegenie.android.tradegenieapp.Activity.SquareCropActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCityList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterGender;
import com.tradegenie.android.tradegenieapp.BuildConfig;
import com.tradegenie.android.tradegenieapp.Models.CityListModel;
import com.tradegenie.android.tradegenieapp.Models.DataPart;
import com.tradegenie.android.tradegenieapp.Models.GenderModel;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.KEY_USER_TYPE;


public class PersonalDetailsFragment extends Fragment {

    @BindView(R.id.relativeLayoutPersonalDetails)
    RelativeLayout relativeLayoutPersonalDetails;
    Unbinder unbinder;
    @BindView(R.id.edt_mobile_no)
    EditText edtMobileNo;
    @BindView(R.id.edt_p_email)
    EditText edtPEmail;
    @BindView(R.id.spinner_gender)
    Spinner spinnerGender;
    @BindView(R.id.txt_date_of_birth)
    TextView txtDateOfBirth;
    @BindView(R.id.iv_drop_down_date_of_birth)
    ImageView ivDropDownDateOfBirth;
    @BindView(R.id.txt_country)
    TextView txtCountry;
    @BindView(R.id.edt_p_address)
    EditText edtPAddress;
    @BindView(R.id.edt_pin)
    EditText edtPin;
    @BindView(R.id.edt_national_identification_no)
    EditText edtNationalIdentificationNo;
    @BindView(R.id.rel_profile_pic)
    RelativeLayout relProfilePic;
    @BindView(R.id.iv_view)
    View ivView;
    @BindView(R.id.iv_drop_down_view)
    ImageView ivDropDownView;
    @BindView(R.id.iv_view_txt_date_of_birth)
    View ivViewTxtDateOfBirth;
    @BindView(R.id.iv_view_spinner_email)
    View ivViewSpinnerEmail;
    @BindView(R.id.iv_drop_down)
    ImageView ivDropDown;
    @BindView(R.id.iv_profile_pic)
    CircleImageView ivProfilePic;
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.rel_date_of_birth)
    RelativeLayout relDateOfBirth;
    @BindView(R.id.rel_spinner_gender)
    RelativeLayout relSpinnerGender;
    @BindView(R.id.spinner_city)
    Spinner spinnerCity;
    @BindView(R.id.rel_city)
    RelativeLayout relCity;

    Calendar BirthdateCal;
    @BindView(R.id.astrix)
    TextView astrix;

    private SessionManager mSessionManager;

    //camera and gallery
    private Uri fileUri;
    private static final int TAKE_PICTURE = 1, SELECT_PICTURE = 2;
    private static final int CROPPED_IMAGE = 3;

    private static final String IMAGE_DIRECTORY_NAME = "PraiseLearning";
    //close camera and gallery

    private ArrayList<GenderModel> mTeacherGenderArrayList;
    private AdapterGender mAdapterTeacherGender;
    private String mGender;

    private ArrayList<CityListModel> mCityListModelArrayList;
    private AdapterCityList mAdapterCityList;
    private String mSelectedCity;
    int SelectedYear, SelectedMonth, SelectedDay;


    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        Constants.mMainActivity.setToolBarName("Profile Details");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.thumbnail_r = null;
        Constants.mMainActivity.ProfileSelected();
        BirthdateCal = Calendar.getInstance();


        mTeacherGenderArrayList = new ArrayList<>();
        mTeacherGenderArrayList.add(new GenderModel("0", "Select Gender"));
        mTeacherGenderArrayList.add(new GenderModel("1", "Male"));
        mTeacherGenderArrayList.add(new GenderModel("2", "Female"));
        mTeacherGenderArrayList.add(new GenderModel("3", "Other"));
        mAdapterTeacherGender = new AdapterGender(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mTeacherGenderArrayList);
        spinnerGender.setAdapter(mAdapterTeacherGender);

        mCityListModelArrayList = new ArrayList<>();
        mCityListModelArrayList.add(new CityListModel("0", "Select City", ""));
        mAdapterCityList = new AdapterCityList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCityListModelArrayList);
        spinnerCity.setAdapter(mAdapterCityList);

        spinnerCity.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();

                if (!mCityListModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedCity = mCityListModelArrayList.get(i).getPk_id();
                    Log.e("check", "gender----->" + mSelectedCity);
                } else {
                    mSelectedCity = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerGender.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();

                if (!mTeacherGenderArrayList.get(i).getId().equalsIgnoreCase("0")) {
                    mGender = mTeacherGenderArrayList.get(i).getValue();
                    Log.e("check", "gender----->" + mGender);
                } else {
                    mGender = "select";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (InternetConnection.isInternetAvailable(getActivity())) {

            getPersonalDetails();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e("onActivityResult", "requestCode" + requestCode);

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
                            Bitmap thumbnail = decodeSampledBitmapFromResource(
                                    picturePath, 500, 500);

                            // rotated
                            Constants.mMainActivity.thumbnail_r = imageOreintationValidator(thumbnail, picturePath);
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


    //Method to fill Personal Details
    private void CallUpdateProfileWebservice() {

        UtilityMethods.tuchOff(relativeLayoutPersonalDetails);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_PERSONAL_ACTION,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("Response", new String(response.data));
                        try {

                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            if (jsonObject.getString("error_code").equals("1")) {

                                UtilityMethods.showSuccessToast(getActivity(), "Profile details saved successfully");

                                if (InternetConnection.isInternetAvailable(getActivity())) {

                                    getPersonalDetails();


                                } else {

                                    UtilityMethods.showInternetDialog(getActivity());

                                }


                            } else if (jsonObject.getString("error_code").equals("2")) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, something went wrong");

                            } else if (jsonObject.getString("error_code").equals("10")) {

                                UtilityMethods.UserInactivePopup(getActivity());

                            } else {
                                UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                        UtilityMethods.tuchOn(relativeLayoutPersonalDetails);
                    }
                },
                new Response.ErrorListener() {
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
                            UtilityMethods.tuchOn(relativeLayoutPersonalDetails);

                        } catch (Exception e) {
                            e.printStackTrace();
                            UtilityMethods.tuchOn(relativeLayoutPersonalDetails);
                        }


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
                params.put("usertype", mSessionManager.getStringData(KEY_USER_TYPE));
                params.put("username", edtUserName.getText().toString());
                params.put("mobileno", edtMobileNo.getText().toString().trim());
                params.put("emailid", edtPEmail.getText().toString().trim());
                params.put("gender", mGender);
                params.put("dob", txtDateOfBirth.getText().toString());
                params.put("country", txtCountry.getText().toString());
                params.put("city", mSelectedCity);
                params.put("address", edtPAddress.getText().toString());
                params.put("pincode", edtPin.getText().toString());
                params.put("nationalidno", edtNationalIdentificationNo.getText().toString());
                Log.e(" params", " params " + params.toString());

                System.out.println("update profile Parameters is :-" + params);
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
                    //  params=null;
                    params.remove("profile");
                    System.out.println("Size of Parameters in else :-" + params.size());
                    return params;
                }


            }


        };

//adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(
                120000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    //Method to getPersonalDetails
    private void getPersonalDetails() {

        UtilityMethods.tuchOff(relativeLayoutPersonalDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PERSONAL_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getPersonalDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        mSessionManager.putStringData(Constants.KEY_SELLER_UID, jsonObject.getString("uid"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_USER_NAME, jsonObject.getString("user_name"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_EMAIL_ID, jsonObject.getString("emailid"));
                        mSessionManager.putStringData(KEY_USER_TYPE, jsonObject.getString("user_type"));
//                        if (jsonObject.getString("user_type").equals("seller")){
//                            astrix.setVisibility(GONE);
//                        }else {
//                            astrix.setVisibility(VISIBLE);
//                        }
                        mSessionManager.putStringData(Constants.KEY_SELLER_MOBILE_NO, jsonObject.getString("mobileno"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_COUNTRY, jsonObject.getString("country"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_COUNTRY_ID, jsonObject.getString("countryId"));

                        mSessionManager.putStringData(Constants.KEY_SELLER_CITY, jsonObject.getString("city"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_GENDER, jsonObject.getString("gender"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_DOB, jsonObject.getString("dob"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_ADDRESS, jsonObject.getString("address"));
                        mSessionManager.putStringData(Constants.KEY_SELLER_PROFILE, jsonObject.getString("profile_image") + jsonObject.getString("profile"));

                        String login_type = jsonObject.getString("login_type");

                        mSessionManager.putStringData(Constants.KEY_LOGIN_TYPE, login_type);

                        Log.e("check", "======>" + jsonObject.getString("profile_image") + jsonObject.getString("profile"));

                        edtUserName.setText(jsonObject.getString("user_name"));
                        txtDateOfBirth.setText(jsonObject.getString("dob"));
                        txtCountry.setText(jsonObject.getString("country"));
                        edtPAddress.setText(jsonObject.getString("address"));
                        edtPin.setText(jsonObject.getString("pincode"));
                        edtNationalIdentificationNo.setText(jsonObject.getString("national_id_no"));

                        String UA_City = jsonObject.getString("UA_City");


                        JSONArray JSONArrayCitylist = jsonObject.getJSONArray("citylist");

                        mCityListModelArrayList.clear();
                        mCityListModelArrayList.add(new CityListModel("0", "Select", ""));
                        for (int i = 0; i < JSONArrayCitylist.length(); i++) {

                            JSONObject CitylistJSONObject = JSONArrayCitylist.getJSONObject(i);

                            String pk_id = CitylistJSONObject.getString("pk_id");
                            String city_name = CitylistJSONObject.getString("city_name");
                            String country_fkid = CitylistJSONObject.getString("country_fkid");

                            CityListModel cityListModel = new CityListModel(pk_id, city_name, country_fkid);
                            mCityListModelArrayList.add(cityListModel);

                        }

                        mAdapterCityList.notifyDataSetChanged();


                        String profile_image = jsonObject.getString("profile_image");
                        String profile = jsonObject.getString("profile");

                        Log.e("check", "====>" + profile_image + profile);

                        try {
                            Picasso.get().load(profile_image + profile).placeholder(R.drawable.profile_image_change).into(ivProfilePic);
                        } catch (Exception e) {

                        }

                        for (int i = 0; i < mCityListModelArrayList.size(); i++) {

                            if (mCityListModelArrayList.get(i).getPk_id().equalsIgnoreCase(UA_City)) {


                                spinnerCity.setSelection(i);


                            }

                        }


                        String gender = jsonObject.getString("gender");

                        if (gender.equalsIgnoreCase("")) {

                            spinnerGender.setSelection(0);

                        } else if (gender.equalsIgnoreCase("male")) {

                            spinnerGender.setSelection(1);

                        } else if (gender.equalsIgnoreCase("female")) {
                            spinnerGender.setSelection(2);
                        } else if (gender.equalsIgnoreCase("other")) {
                            spinnerGender.setSelection(3);
                        }


                        if (jsonObject.getString("mobileno").equalsIgnoreCase("null") || jsonObject.getString("mobileno").equalsIgnoreCase("")) {


                        } else {

                            edtMobileNo.setText(jsonObject.getString("mobileno"));

                        }


                        if (jsonObject.getString("emailid").equalsIgnoreCase("null") || jsonObject.getString("emailid").equalsIgnoreCase("")) {


                        } else {

                            edtPEmail.setText(jsonObject.getString("emailid"));

                        }


                        if (login_type.equalsIgnoreCase("1")) {

                            edtPEmail.setKeyListener(null);


                        } else if (login_type.equalsIgnoreCase("2")) {

                            edtMobileNo.setKeyListener(null);


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

                UtilityMethods.tuchOn(relativeLayoutPersonalDetails);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showWarningToast(getActivity(), getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showWarningToast(getActivity(), getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showWarningToast(getActivity(), getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showWarningToast(getActivity(), getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutPersonalDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                //params.put("uid", "50");
                params.put("usertype", mSessionManager.getStringData(KEY_USER_TYPE));

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

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.e("inSampleSize", "inSampleSize______________in storage"
                + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
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

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, getActivity() will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
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

    //method for call Calender Dialog with different style
    public void calender() {

        // edtDob.setEnabled(false);
        final Calendar dob_date = Calendar.getInstance();
        final Calendar dob_currentDate = Calendar.getInstance();


        SelectedYear = dob_currentDate.get(Calendar.YEAR);
        SelectedDay = dob_currentDate.get(Calendar.MONTH);
        SelectedMonth = dob_currentDate.get(Calendar.DATE);


        int CurrentYear = dob_currentDate.get(dob_currentDate.YEAR);
        CurrentYear = CurrentYear - 13;
        dob_currentDate.set(Calendar.YEAR, CurrentYear);


        DatePickerDialog dob_atePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                dob_date.set(year, monthOfYear, dayOfMonth);
                //  edtDob.setClickable(true);
                txtDateOfBirth.setEnabled(true);
                BirthdateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                BirthdateCal.set(Calendar.MONTH, monthOfYear);
                BirthdateCal.set(Calendar.YEAR, year);

                SelectedYear = year;
                SelectedDay = dayOfMonth;
                SelectedMonth = monthOfYear;

                String mDay, mMonth;

                if (ValidDate()) {
                    if (dayOfMonth < 9) {
                        mDay = "0" + dayOfMonth;
                    } else {
                        mDay = String.valueOf(dayOfMonth);
                    }

                    if ((monthOfYear + 1) < 9) {
                        mMonth = "0" + (monthOfYear + 1);
                    } else {
                        mMonth = String.valueOf((monthOfYear + 1));
                    }

                    txtDateOfBirth.setText(mDay + "-" + mMonth + "-" + year);
                }
            }
        }, SelectedYear, SelectedMonth, SelectedDay);
        dob_atePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // edtAnniversaryDate.setEnabled(true);
            }
        });
        dob_atePickerDialog.getDatePicker().setMaxDate(dob_currentDate.getTimeInMillis());
        dob_atePickerDialog.show();

    }

    @OnClick({R.id.rel_profile_pic, R.id.btn_submit, R.id.rel_date_of_birth, R.id.rel_spinner_gender, R.id.rel_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_profile_pic:
                ShowCameraGallryOption();
                break;
            case R.id.btn_submit:

                keyBoardHide();

                if (validation()) {
                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        CallUpdateProfileWebservice();


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }
                    Log.e("test", "Success---->>> ");
                } else {

                    Log.e("test", "Failed---->>> ");
                }

                break;

            case R.id.rel_date_of_birth:
                calender();
                break;

            case R.id.rel_spinner_gender:
                keyBoardHide();
                spinnerGender.performClick();
                break;

            case R.id.rel_city:
                keyBoardHide();
                spinnerCity.performClick();
                break;
        }
    }

    private boolean validation() {

        String usertype = mSessionManager.getStringData(KEY_USER_TYPE);

        if (edtUserName.getText().toString().trim().equals("")) {
            edtUserName.setError("Please enter name");
            edtUserName.requestFocus();
            return false;
        } else if (!usertype.equals("seller") && edtMobileNo.getText().toString().equalsIgnoreCase("")) {
            edtMobileNo.setError("Please enter mobile no.");
            edtMobileNo.requestFocus();
            return false;
        } else if (!usertype.equals("seller") && edtMobileNo.getText().length() < 7) {
            edtMobileNo.setError("Please enter valid mobile no.");
            edtMobileNo.requestFocus();
            return false;
        } else if (edtPEmail.getText().toString().equalsIgnoreCase("")) {
            edtPEmail.setError("Please enter email");
            edtPEmail.requestFocus();
            return false;
        } else if (!UtilityMethods.isValidEmail(edtPEmail.getText().toString().trim())) {
            edtPEmail.setError("Please enter valid email");
            edtPEmail.requestFocus();
            return false;
        } else if (mSelectedCity.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select city");
            return false;
        }/*else if (mGender.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(),"Please select gender");
            return false;
        }else if (txtDateOfBirth.getText().toString().equalsIgnoreCase("")) {
            UtilityMethods.showInfoToast(getActivity(),"Please select date of birth");
            return false;
        }else if (mSelectedCity.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(),"Please select city");
            return false;
        }else if (edtPAddress.getText().toString().equalsIgnoreCase("")) {
            edtPAddress.setError("Please enter address");
            edtPAddress.requestFocus();
            return false;
        }else if (edtPin.getText().toString().equalsIgnoreCase("")) {
            edtPin.setError("Please enter pincode");
            edtPin.requestFocus();
            return false;
        }else if (edtNationalIdentificationNo.getText().toString().equalsIgnoreCase("")) {
            edtNationalIdentificationNo.setError("Please enter national identification no.");
            edtNationalIdentificationNo.requestFocus();
            return false;
        }*/
        return true;
    }


    public void keyBoardHide() {

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to check whether entered date is valid or not
    private boolean ValidDate() {
        //SetToday's Date forst
        Date d = new Date();
        Log.e("day", "day " + d.getDate() + " month " + d.getMonth() + " year " + d.getYear());
        Calendar CurrentCal = Calendar.getInstance();
        CurrentCal.set(Calendar.DAY_OF_MONTH, d.getDate());
        CurrentCal.set(Calendar.MONTH, d.getMonth());
        CurrentCal.set(Calendar.YEAR, CurrentCal.get(CurrentCal.YEAR));
        //  CurrentCal.set(Calendar.YEAR, d.getYear());
        Log.e("CurrentCal", "CurrentCal " + CurrentCal);
        Log.e("BirthdateCal", "BirthdateCal " + BirthdateCal);

        long DateDifference = CurrentCal.getTimeInMillis() - BirthdateCal.getTimeInMillis();
        Date date = new Date(DateDifference);
        System.out.println(date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd:mm:yyyy");

        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println("diff time format " + timeFormat.format(date));
        System.out.println("diff time format " + TimeUnit.DAYS.convert(DateDifference, TimeUnit.MILLISECONDS));


        long Days = TimeUnit.DAYS.convert(DateDifference, TimeUnit.MILLISECONDS);
        long years = Days / 365;

        //Date CurrentDate= new Date(Calendar.YEAR,Calendar.MONTH,Calendar.DATE);
        //Date BODate= new Date(BirthdateCal.YEAR,BirthdateCal.MONTH,BirthdateCal.DATE);


        if (CurrentCal.getTimeInMillis() < BirthdateCal.getTimeInMillis()) {
            UtilityMethods.showWarningToast(getActivity(), "Please enter valid birth date");
            return false;
        } else if (years < 13) {
            UtilityMethods.showWarningToast(getActivity(), "Age should be 13 or 13+");
            return false;
        }


        return true;
    }


}
