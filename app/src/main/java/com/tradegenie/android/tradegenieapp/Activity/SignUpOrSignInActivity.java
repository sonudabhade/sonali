package com.tradegenie.android.tradegenieapp.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCountyList;
import com.tradegenie.android.tradegenieapp.Models.CountryListModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpOrSignInActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.spinner_country_list)
    Spinner spinnerCountryList;
    @BindView(R.id.Linear_layout_country_list)
    RelativeLayout LinearLayoutCountryList;
    @BindView(R.id.edt_Mobile_Number_SignInFragment)
    EditText edtMobileNumberSignInFragment;
    @BindView(R.id.edt_email_SignInFragment)
    EditText edtEmailSignInFragment;
    @BindView(R.id.checkBoxTermAndCondition)
    CheckBox checkBoxTermAndCondition;
    @BindView(R.id.relativeLayoutSignup)
    RelativeLayout relativeLayoutSignup;
    @BindView(R.id.txt_terms_and_conditions)
    TextView txtTermsAndConditions;

    String cms_text;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.linear_edt_mobile)
    LinearLayout linearEdtMobile;


    private ArrayList<CountryListModel> mCountryListModelArrayList;
    private AdapterCountyList mAdapterCountryList;
    private String mSelectedCountry = "";

    public String usertype, login_type;
    public AlertDialog dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_or_sign_in);
        ButterKnife.bind(this);

        usertype = getIntent().getStringExtra("usertype");


        mCountryListModelArrayList = new ArrayList<>();
        mAdapterCountryList = new AdapterCountyList(SignUpOrSignInActivity.this, R.id.txt_country_name, R.layout.adapter_country_list_row, mCountryListModelArrayList, this);
        spinnerCountryList.setAdapter(mAdapterCountryList);
        spinnerCountryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mSelectedCountry = mCountryListModelArrayList.get(i).getPk_id();


                if (!mCountryListModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedCountry = mCountryListModelArrayList.get(i).getPk_id();
/*
                    if (mCountryListModelArrayList.get(i).getPk_id().equalsIgnoreCase("3")) {

                        linearEdtMobile.setVisibility(View.VISIBLE);
                        edtEmailSignInFragment.setVisibility(View.GONE);

                        edtEmailSignInFragment.setText("");
                        edtMobileNumberSignInFragment.setText("");

                    } else {

                        linearEdtMobile.setVisibility(View.GONE);
                        edtEmailSignInFragment.setVisibility(View.VISIBLE);

                        edtEmailSignInFragment.setText("");
                        edtMobileNumberSignInFragment.setText("");
                    }*/

                    linearEdtMobile.setVisibility(View.GONE);
                    edtEmailSignInFragment.setVisibility(View.VISIBLE);

                    edtEmailSignInFragment.setText("");
                    edtMobileNumberSignInFragment.setText("");


                } else {
                    mSelectedCountry = "0";
                }

                Log.e("check", "------->" + mSelectedCountry);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*terms and conditions start*/
        String first = " I accept ";
        String next = "<u><font color='#4B73FD'>terms and conditions</font></u>";
        txtTermsAndConditions.setText(Html.fromHtml(first + next));
        /*terms and conditions end*/


        if (InternetConnection.isInternetAvailable(SignUpOrSignInActivity.this)) {

            //UtilityMethods.tuchOff(relativeLayoutMyVideos);
            getCountryList();

        } else {

            UtilityMethods.showInternetDialog(SignUpOrSignInActivity.this);

        }


    }

    @OnClick(R.id.btn_sign_in)
    public void onViewClicked() {


        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mSelectedCountry.equalsIgnoreCase("0")) {

            UtilityMethods.showInfoToast(this, "Select Country");

        } else {

           /* if (mSelectedCountry.equalsIgnoreCase("3")) {

                if (validation()) {
                    if (InternetConnection.isInternetAvailable(SignUpOrSignInActivity.this)) {

                        SignUpSignIn();


                    } else {

                        UtilityMethods.showInternetDialog(SignUpOrSignInActivity.this);

                    }
                    Log.e("test", "Success---->>> ");
                } else {

                    Log.e("test", "Failed---->>> ");
                }

            } else {*/
                if (validationOtherCountry()) {
                    if (InternetConnection.isInternetAvailable(SignUpOrSignInActivity.this)) {

                        SignUpSignIn();


                    } else {

                        UtilityMethods.showInternetDialog(SignUpOrSignInActivity.this);

                    }
                    Log.e("test", "Success---->>> ");
                } else {

                    Log.e("test", "Failed---->>> ");
                }

            /* }*/


        }


    }

    //MEthod to get myVideo list
    private void getCountryList() {

        UtilityMethods.tuchOff(relativeLayoutSignup);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_COUNTRY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getCountryList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String flag_path = jsonObject.getString("flag_path");

                        mCountryListModelArrayList.add(new CountryListModel("0", "Select Country", ""));
                        JSONArray countrylistJSONArray = jsonObject.getJSONArray("countrylist");
                        for (int i = 0; i < countrylistJSONArray.length(); i++) {

                            JSONObject countrylistJSONObject = countrylistJSONArray.getJSONObject(i);
                            String pk_id = countrylistJSONObject.getString("pk_id");
                            String country_name = countrylistJSONObject.getString("country_name");
                            String country_flag = flag_path + countrylistJSONObject.getString("country_flag");

                            CountryListModel countryListModel = new CountryListModel(pk_id, country_name, country_flag);
                            mAdapterCountryList.add(countryListModel);
                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(SignUpOrSignInActivity.this);


                    } else {
                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, jsonObject.getString("message"));
                    }

                    mAdapterCountryList.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                UtilityMethods.tuchOn(relativeLayoutSignup);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutSignup);

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

        Volley.newRequestQueue(SignUpOrSignInActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @OnClick(R.id.Linear_layout_country_list)
    public void onViewClickeded() {

        spinnerCountryList.performClick();
    }

    private boolean validation() {


        if (edtMobileNumberSignInFragment.getText().toString().trim().equals("")) {
            edtMobileNumberSignInFragment.setError("Please enter mobile no.");
            edtMobileNumberSignInFragment.requestFocus();
            return false;
        } else if (edtMobileNumberSignInFragment.getText().length() < 9) {
            edtMobileNumberSignInFragment.setError("Please enter valid mobile no.");
            edtMobileNumberSignInFragment.requestFocus();
            return false;
        } else if (!checkBoxTermAndCondition.isChecked()) {
            UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, "Accept terms and conditions");
            return false;
        }
        return true;
    }

    private boolean validationOtherCountry() {


        if (edtEmailSignInFragment.getText().toString().trim().equals("")) {
            edtEmailSignInFragment.setError("Enter Email");
            edtEmailSignInFragment.requestFocus();
            return false;
        } else if (!UtilityMethods.isValidEmail(edtEmailSignInFragment.getText().toString().trim())) {
            edtEmailSignInFragment.setError("Please enter valid email address");
            edtEmailSignInFragment.requestFocus();
            return false;
        } else if (!checkBoxTermAndCondition.isChecked()) {
            UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, "Accept terms and conditions");
            return false;
        }
        return true;
    }

    //MEthod to SignUpSignIn
    private void SignUpSignIn() {

        UtilityMethods.tuchOff(relativeLayoutSignup);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "SignUpSignIn -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        Intent intent = new Intent(SignUpOrSignInActivity.this, OtpVerificationActivity.class);

                        if (mSelectedCountry.equalsIgnoreCase("3")) {

                            intent.putExtra("MobileFromSignIn", edtMobileNumberSignInFragment.getText().toString());
                            intent.putExtra("usertype", usertype);
                            intent.putExtra("login_type", jsonObject.getString("login_type"));
                            intent.putExtra("new_type", jsonObject.getString("new_type"));
                            intent.putExtra("otp_send", jsonObject.getString("otp"));
                            intent.putExtra("country", mSelectedCountry);

                        } else {

                            intent.putExtra("EmailFromSignIn", edtEmailSignInFragment.getText().toString());
                            intent.putExtra("usertype", usertype);
                            intent.putExtra("login_type", jsonObject.getString("login_type"));
                            intent.putExtra("new_type", jsonObject.getString("new_type"));
                            intent.putExtra("otp_send", jsonObject.getString("otp"));
                            intent.putExtra("country", mSelectedCountry);


                        }

                        startActivity(intent);

                        UtilityMethods.showSuccessToast(SignUpOrSignInActivity.this, "OTP sent successfully");

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                        Intent intent = new Intent(SignUpOrSignInActivity.this, OtpVerificationActivity.class);

                        if (mSelectedCountry.equalsIgnoreCase("3")) {

                            intent.putExtra("MobileFromSignIn", edtMobileNumberSignInFragment.getText().toString());
                            intent.putExtra("usertype", usertype);
                            intent.putExtra("login_type", jsonObject.getString("login_type"));
                            intent.putExtra("new_type", jsonObject.getString("new_type"));
                            intent.putExtra("otp_send", jsonObject.getString("otp"));
                            intent.putExtra("country", mSelectedCountry);


                        } else {

                            intent.putExtra("EmailFromSignIn", edtEmailSignInFragment.getText().toString());
                            intent.putExtra("usertype", usertype);
                            intent.putExtra("login_type", jsonObject.getString("login_type"));
                            intent.putExtra("new_type", jsonObject.getString("new_type"));
                            intent.putExtra("otp_send", jsonObject.getString("otp"));
                            intent.putExtra("country", mSelectedCountry);

                        }

                        startActivity(intent);

                        UtilityMethods.showSuccessToast(SignUpOrSignInActivity.this, "OTP sent successfully");


                    } else if (jsonObject.getString("error_code").equals("4")) {

                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUpOrSignInActivity.this);
                        builder1.setMessage("User is inactive. Please contact to TradeGenie support team");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(

                                R.string.OK,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.setCanceledOnTouchOutside(false);
                        alert11.show();


                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(SignUpOrSignInActivity.this);

                    } else if (jsonObject.getString("error_code").equals("8")) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, "This email id already registered with another country");

                    } else {
                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutSignup);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutSignup);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (!edtEmailSignInFragment.getText().toString().equalsIgnoreCase("")) {

                    params.put("emailid", edtEmailSignInFragment.getText().toString());

                } else {

                    params.remove("emailid");

                }

               /* if (!edtMobileNumberSignInFragment.getText().toString().equalsIgnoreCase("")) {

                    params.put("mobileno",         edtMobileNumberSignInFragment.getText().toString());

                } else {

                    params.remove("mobileno");

                }*/

                params.put("usertype", usertype);
                params.put("country", mSelectedCountry);

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

        Volley.newRequestQueue(SignUpOrSignInActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to get Terms And Conditions
    private void getTermsAndConditions() {

        UtilityMethods.tuchOff(relativeLayoutSignup);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getTermsAndConditions -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String cms_title = jsonObject.getString("cms_title");
                        cms_text = jsonObject.getString("cms_text");


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(SignUpOrSignInActivity.this);
                    } else {
                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutSignup);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(SignUpOrSignInActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutSignup);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("cms_id", "5");

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

        Volley.newRequestQueue(SignUpOrSignInActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @OnClick(R.id.txt_terms_and_conditions)
    public void onViewClickededed() {


        Intent intent = new Intent(SignUpOrSignInActivity.this, TermsAndConditionsActivity.class);
        startActivity(intent);
       // Log.e("Login Error", "Login Error");
        Toast toast = Toast.makeText(getApplicationContext(),
                "Login Error",
                Toast.LENGTH_SHORT);

        toast.show();

    }
}
