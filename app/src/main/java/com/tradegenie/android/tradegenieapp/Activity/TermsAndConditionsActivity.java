package com.tradegenie.android.tradegenieapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsAndConditionsActivity extends AppCompatActivity {

    @BindView(R.id.txt_data)
    JustifiedTextView txtData;
    @BindView(R.id.realtiveLayoutTermsAndPolicyPraiseLearning)
    RelativeLayout realtiveLayoutTermsAndPolicyPraiseLearning;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);


        if (InternetConnection.isInternetAvailable(TermsAndConditionsActivity.this)) {

            //UtilityMethods.tuchOff(relativeLayoutMyVideos);
            getTermsAndConditions();

        } else {

            UtilityMethods.showInternetDialog(TermsAndConditionsActivity.this);

        }
    }


    //Method to get Terms And Conditions
    private void getTermsAndConditions() {

        UtilityMethods.tuchOff(realtiveLayoutTermsAndPolicyPraiseLearning);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getTermsAndConditions -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String cms_title = jsonObject.getString("cms_title");
                        String cms_text = jsonObject.getString("cms_text");

                        Spanned sp = Html.fromHtml(cms_text);
                        txtData.setText(sp);


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(TermsAndConditionsActivity.this);
                    } else {
                        UtilityMethods.showInfoToast(TermsAndConditionsActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(realtiveLayoutTermsAndPolicyPraiseLearning);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(TermsAndConditionsActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(TermsAndConditionsActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(TermsAndConditionsActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(TermsAndConditionsActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(realtiveLayoutTermsAndPolicyPraiseLearning);

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

        Volley.newRequestQueue(TermsAndConditionsActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @OnClick(R.id.iv_cancel)
    public void onViewClicked() {

        finish();
    }
}
