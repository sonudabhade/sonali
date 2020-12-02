package com.tradegenie.android.tradegenieapp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tradegenie.android.tradegenieapp.Utility.Constants.USER_LANG;

public class OtpVerificationActivity extends AppCompatActivity {

    @BindView(R.id.edt_otp_one)
    TextView edtOtpOne;
    @BindView(R.id.edt_otp_two)
    TextView edtOtpTwo;
    @BindView(R.id.edt_otp_three)
    TextView edtOtpThree;
    @BindView(R.id.edt_otp_four)
    TextView edtOtpFour;
    @BindView(R.id.btn_verify)
    Button btnVerify;
    @BindView(R.id.tv_resend_otp)
    TextView tvResendOtp;
    @BindView(R.id.tv_email_or_mobile_otp)
    TextView tvEmailOrMobileOtp;
    @BindView(R.id.relativeLayoutOTPVerification)
    RelativeLayout relativeLayoutOTPVerification;
    @BindView(R.id.edt_otp)
    EditText edtOtp;
    @BindView(R.id.tv_timer)
    TextView tvTimer;

    private String MobileFromSignIn = "null";
    private String EmailFromSignIn = "null";
    private String usertype = "null", new_type = "", login_type = "", otp_send = "", country = "";

    public Bundle bundle;

    private SessionManager mSessionManager;
    public CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);

        MobileFromSignIn = getIntent().getStringExtra("MobileFromSignIn");
        EmailFromSignIn = getIntent().getStringExtra("EmailFromSignIn");
        usertype = getIntent().getStringExtra("usertype");
        new_type = getIntent().getStringExtra("new_type");
        login_type = getIntent().getStringExtra("login_type");
        otp_send = getIntent().getStringExtra("otp_send");
        country = getIntent().getStringExtra("country");

        mSessionManager = new SessionManager(OtpVerificationActivity.this);

        if (MobileFromSignIn == null) {

            tvEmailOrMobileOtp.setText(EmailFromSignIn);

        } else {

            tvEmailOrMobileOtp.setText("+94 " + MobileFromSignIn);

        }


        edtOtpOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtOtp.requestFocus();
                edtOtp.setSelection(edtOtp.getText().toString().trim().length());

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtOtp, InputMethodManager.SHOW_IMPLICIT);

                //scroll.fullScroll(View.FOCUS_DOWN);

            }
        });
        edtOtpTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtOtp.requestFocus();
                edtOtp.setSelection(edtOtp.getText().toString().trim().length());

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtOtp, InputMethodManager.SHOW_IMPLICIT);

                //  scroll.fullScroll(View.FOCUS_DOWN);

            }
        });
        edtOtpThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtOtp.requestFocus();
                edtOtp.setSelection(edtOtp.getText().toString().trim().length());

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtOtp, InputMethodManager.SHOW_IMPLICIT);

                //   scroll.fullScroll(View.FOCUS_DOWN);

            }
        });
        edtOtpFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtOtp.requestFocus();
                edtOtp.setSelection(edtOtp.getText().toString().trim().length());

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtOtp, InputMethodManager.SHOW_IMPLICIT);

                //   scroll.fullScroll(View.FOCUS_DOWN);

            }
        });


        edtOtp.addTextChangedListener(new TextWatcher() {


            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


                String EnteredOTP = edtOtp.getText().toString().trim();

                try {
                    edtOtpOne.setText(EnteredOTP.substring(0, 1));
                } catch (Exception e) {
                    edtOtpOne.setText("");

                }
                try {
                    edtOtpTwo.setText(EnteredOTP.substring(1, 2));
                } catch (Exception e) {
                    edtOtpTwo.setText("");
                }
                try {
                    edtOtpThree.setText(EnteredOTP.substring(2, 3));
                } catch (Exception e) {
                    edtOtpThree.setText("");
                }
                try {
                    edtOtpFour.setText(EnteredOTP.substring(3, 4));
                } catch (Exception e) {
                    edtOtpFour.setText("");
                }


            }

        });

        ShowTimer();

    }

    @OnClick(R.id.btn_verify)
    public void onBtnVerifyClicked() {

        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!edtOtpOne.getText().toString().equalsIgnoreCase("") && !edtOtpTwo.getText().toString().equalsIgnoreCase("") && !edtOtpThree.getText().toString().equalsIgnoreCase("") && !edtOtpFour.getText().toString().equalsIgnoreCase("")) {
            if (InternetConnection.isInternetAvailable(OtpVerificationActivity.this)) {



                OTPVerify();

            } else {

                UtilityMethods.showInternetDialog(OtpVerificationActivity.this);
                System.out.println("-----------------------------------------------------------------------------------------------------------------------otp verification button");


            }

        } else {

            UtilityMethods.showInfoToast(OtpVerificationActivity.this, "Please enter OTP");

        }


    }

    @OnClick(R.id.tv_resend_otp)
    public void onTvResendOtpClicked() {

        if (InternetConnection.isInternetAvailable(OtpVerificationActivity.this)) {

            edtOtpOne.requestFocus();
            ResendOTP();

        } else {

            UtilityMethods.showInternetDialog(OtpVerificationActivity.this);

        }

    }

    //Method to Resend OTP
    private void ResendOTP() {

        UtilityMethods.tuchOff(relativeLayoutOTPVerification);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RE_SEND_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "ResendOTP -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        otp_send = jsonObject.getString("otp");

                        edtOtp.setText("");
                        System.out.println("-----------------------------------------------------------------------------------------------------------------------otp");


                        ShowTimer();

                        UtilityMethods.showSuccessToast(OtpVerificationActivity.this, "OTP sent successfully");


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                        edtOtp.setText("");

                        UtilityMethods.showSuccessToast(OtpVerificationActivity.this, "OTP sent successfully");

                    } else if (jsonObject.getString("error_code").equals("10")) {


                    } else {
                        UtilityMethods.showInfoToast(OtpVerificationActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutOTPVerification);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutOTPVerification);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                Log.e("check", "===>" + MobileFromSignIn);
                Log.e("check", "===>" + EmailFromSignIn);

                if (MobileFromSignIn != null) {

                    params.put("mobileno", MobileFromSignIn);

                } else {

                    params.remove("mobileno");

                }

                if (EmailFromSignIn != null) {

                    params.put("emailid", EmailFromSignIn);

                } else {

                    params.remove("emailid");

                }


                params.put("usertype", usertype);


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

        Volley.newRequestQueue(OtpVerificationActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to Resend OTP
    private void OTPVerify() {

        UtilityMethods.tuchOff(relativeLayoutOTPVerification);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_OTP_VERIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "OTPVerify -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        if (jsonObject.getString("user_type").equals("seller")) {

                            mSessionManager.putStringData(Constants.KEY_SELLER_UID, jsonObject.getString("uid"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_USER_NAME, jsonObject.getString("user_Name"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_EMAIL_ID, jsonObject.getString("emailid"));
                            mSessionManager.putStringData(Constants.KEY_USER_TYPE, jsonObject.getString("user_type"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_MOBILE_NO, jsonObject.getString("mobileno"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_COUNTRY, jsonObject.getString("country"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_COUNTRY_ID, jsonObject.getString("fk_country_id"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_CITY, jsonObject.getString("city"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_GENDER, jsonObject.getString("gender"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_DOB, jsonObject.getString("dob"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_ADDRESS, jsonObject.getString("address"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_NATURE_OF_BUSINESS, jsonObject.getString("fk_nb_id"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_PROFILE, jsonObject.getString("profile_image_url") + jsonObject.getString("profile_image"));

                            mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_NAME, jsonObject.getString("bussiness_name"));
                            mSessionManager.putStringData(Constants.KEY_LOGIN_TYPE, jsonObject.getString("login_type"));

                            mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_CITY, jsonObject.getString("fk_city_id"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_BUSINESS_COUNTY, jsonObject.getString("country_id"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_CITY, jsonObject.getString("fk_city_id"));
                            mSessionManager.putStringData(Constants.KEY_SELLER_TEMP_BUSINESS_COUNTY, jsonObject.getString("country_id"));


                            mSessionManager.putStringData(Constants.KEY_USER_TYPE, "seller");
                            try {
                                mSessionManager.putStringData(USER_LANG, jsonObject.getString("language"));
                            }catch (Exception e){e.printStackTrace();}
                            setLocale();

                            Intent intent = new Intent(OtpVerificationActivity.this, MainActivity.class);

                            System.out.println("-----------------------------------------------------------------------------------------------------------------------otp verification before log in");


                            if (jsonObject.getString("user_Name").equalsIgnoreCase("null")) {

                                intent.putExtra("loginUser", "new");
                            } else {

                                intent.putExtra("loginUser", "old");

                            }

                            startActivity(intent);

                            UtilityMethods.showSuccessToast(OtpVerificationActivity.this, "You have logged in ");

                            System.out.println("-----------------------------------------------------------------------------------------------------------------------otp verification log in");

                        }else if (jsonObject.getString("user_type").equals("buyer")){

                            mSessionManager.putStringData(Constants.KEY_BUYER_UID, jsonObject.getString("uid"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_USER_NAME, jsonObject.getString("user_Name"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_EMAIL_ID, jsonObject.getString("emailid"));
                            mSessionManager.putStringData(Constants.KEY_USER_TYPE, jsonObject.getString("user_type"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_MOBILE_NO, jsonObject.getString("mobileno"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_COUNTRY, jsonObject.getString("country"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_CITY, jsonObject.getString("city"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_GENDER, jsonObject.getString("gender"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_DOB, jsonObject.getString("dob"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_ADDRESS, jsonObject.getString("address"));
                            mSessionManager.putStringData(Constants.KEY_BUYER_PROFILE, jsonObject.getString("profile_image_url") + jsonObject.getString("profile_image"));
                            mSessionManager.putStringData(Constants.KEY_LOGIN_TYPE, jsonObject.getString("login_type"));




                            mSessionManager.putStringData(Constants.KEY_USER_TYPE, "buyer");
                            try {
                                mSessionManager.putStringData(USER_LANG, jsonObject.getString("language"));
                            }catch (Exception e){e.printStackTrace();}
                            setLocale();

                            Intent intent = new Intent(OtpVerificationActivity.this, MainActivity.class);

                            startActivity(intent);

                            UtilityMethods.showSuccessToast(OtpVerificationActivity.this, "You have logged in successfully");





                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {


                        edtOtp.setText("");

                        UtilityMethods.showInfoToast(OtpVerificationActivity.this, "Please enter correct OTP");


                    } else if (jsonObject.getString("error_code").equals("2")) {

                        edtOtp.setText("");

                        UtilityMethods.showInfoToast(OtpVerificationActivity.this, "Please enter correct OTP");


                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.showWarningToast(OtpVerificationActivity.this, "Something went wrong, please contact to admin");

                    } else {
                        UtilityMethods.showInfoToast(OtpVerificationActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutOTPVerification);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(OtpVerificationActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutOTPVerification);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if (MobileFromSignIn != null) {

                    params.put("mobileno", MobileFromSignIn);

                } else {

                    params.remove("mobileno");

                }

                if (EmailFromSignIn != null) {

                    params.put("emailid", EmailFromSignIn);

                } else {

                    params.remove("emailid");

                }
                params.put("usertype", usertype);
                params.put("new_type", new_type);
                params.put("login_type", login_type);
                params.put("otp", edtOtpOne.getText().toString() + edtOtpTwo.getText().toString() + edtOtpThree.getText().toString() + edtOtpFour.getText().toString());
                params.put("otp_send", otp_send);
                params.put("country", country);

                if (!mSessionManager.getStringData(Constants.KEY_FCM_ID).equals("")) {
                    params.put("fcm_token", mSessionManager.getStringData(Constants.KEY_FCM_ID));
                } else {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    params.put("fcm_token", token);
                    mSessionManager.putStringData(Constants.KEY_FCM_ID, token);

                }


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

        Volley.newRequestQueue(OtpVerificationActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void setLocale() {
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
    private void ShowTimer() {

        //cancel the old countDownTimer
        if(countDownTimer!=null){
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
                    OTPExpiredDialogShow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
        edtOtpOne.requestFocus();

    }

    public void OTPExpiredDialogShow(){

        AlertDialog.Builder builder = new AlertDialog.Builder(OtpVerificationActivity.this);
        LayoutInflater mInflater = (LayoutInflater) OtpVerificationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = mInflater.inflate(R.layout.dialog_otp_expired, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.show();


        LinearLayout linear_layout_ok = alertDialog.findViewById(R.id.linear_layout_ok);


        linear_layout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });



    }
}
