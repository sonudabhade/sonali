package com.tradegenie.android.tradegenieapp.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentForNewTenderActivity extends AppCompatActivity implements PaymentResultListener {

    String tender_id;
    Double Grandtotal;

    SessionManager mSessionManager;
    @BindView(R.id.ProgressBarRecommendedSubscription)
    RelativeLayout ProgressBarRecommendedSubscription;
    String pay_type, currency = "";;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_for_subscription);
        ButterKnife.bind(this);

        mSessionManager = new SessionManager(PaymentForNewTenderActivity.this);

        tender_id = getIntent().getStringExtra("tender_id");
        pay_type = getIntent().getStringExtra("pay_type");
        currency = getIntent().getStringExtra("currency");
        String GrandtotalStr = getIntent().getStringExtra("Grandtotal");
        Grandtotal = Double.parseDouble(GrandtotalStr);

        if (pay_type.equals("2")) {
            startPayment();
        }else {
            PayForTender();
        }
    }


    //MEthod to pay for tender
    private void PayForTender() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PAY_FOR_TENDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "Tender detail -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {
                        UtilityMethods.tuchOn(ProgressBarRecommendedSubscription);
                        UtilityMethods.showToast(PaymentForNewTenderActivity.this, getString(R.string.payment_done));
                        Intent intent = new Intent();

                        setResult(10001, intent);
                        finish();
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(PaymentForNewTenderActivity.this);
                    } else {
                        UtilityMethods.showInfoToast(PaymentForNewTenderActivity.this, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                UtilityMethods.tuchOn(ProgressBarRecommendedSubscription);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(PaymentForNewTenderActivity.this, getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(PaymentForNewTenderActivity.this, getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(PaymentForNewTenderActivity.this, getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(PaymentForNewTenderActivity.this, getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(ProgressBarRecommendedSubscription);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("tender_id", tender_id);
                params.put("pay_type", pay_type);

                Log.e("params", "params" + Constants.URL_PAY_FOR_TENDER + " params ---> " + params.toString());
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

        Volley.newRequestQueue(PaymentForNewTenderActivity.this).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    //Method of razor pay payment gateway
    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = PaymentForNewTenderActivity.this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Tradegenie");
            options.put("description", "Tender Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", currency);
           /* options.put("prefill.email", mSessionManager.getStringData(Constants.EMAIL_ID));
            options.put("prefill.contact", mSessionManager.getStringData(Constants.MOBILE_NUMBER));*/
            options.put("amount", String.valueOf(Grandtotal * 100));


            Log.e("EMAIL_ID", "EMAIL_ID " + mSessionManager.getStringData(Constants.KEY_SELLER_EMAIL_ID));
            Log.e("MOBILE_NUMBER", "MOBILE_NUMBER " + mSessionManager.getStringData(Constants.KEY_SELLER_MOBILE_NO));
            JSONObject preFill = new JSONObject();
            preFill.put("email", mSessionManager.getStringData(Constants.KEY_SELLER_EMAIL_ID));
            preFill.put("contact", mSessionManager.getStringData(Constants.KEY_SELLER_MOBILE_NO));
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {

        Log.e("razorpayPaymentID", "razorpayPaymentID " + razorpayPaymentID);
        try {
            if (InternetConnection.isInternetAvailable(PaymentForNewTenderActivity.this)) //returns true if internet available
            {
                UtilityMethods.tuchOff(ProgressBarRecommendedSubscription);
                PayForTender();


            } else {

                UtilityMethods.showInternetDialog(PaymentForNewTenderActivity.this);
            }


        } catch (Exception e) {
            Log.e("Purchase course", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        if (i == Checkout.PAYMENT_CANCELED){
            Log.e("Purchase Error", "Cancelled payment");
            finish();
        }
    }

}
