package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.tradegenie.android.tradegenieapp.Activity.PaymentForSubscriptionActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterMySubscriptionRecommanded;
import com.tradegenie.android.tradegenieapp.Models.RecommendedModel;
import com.tradegenie.android.tradegenieapp.Models.ServicesModel;
import com.tradegenie.android.tradegenieapp.Models.TaxModel;
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

public class MySubscriptionRecommendedFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recyclerview_recommended)
    RecyclerView recyclerviewRecommended;
    @BindView(R.id.ProgressBarRecommendedSubscription)
    RelativeLayout ProgressBarRecommendedSubscription;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;

    private ArrayList<RecommendedModel> mRecommendedModelArrayList;
    private ArrayList<ServicesModel> mServicesModelArrayList;
    private AdapterMySubscriptionRecommanded mAdapterMySubscriptionRecommanded;
    private LinearLayoutManager linearLayoutManager;

    private SessionManager mSessionManager;
    String Services = "";
    private ArrayList<TaxModel> mTaxModelArrayList;

    Integer[] bgImage;
    private int counter = 0;

    public String grandtotal = "", price = "", currency = "";
    String pay_type = "";// 1-cash & 2- online

    public MySubscriptionRecommendedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_subcription_recommanded, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("My Subscription");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());
        mTaxModelArrayList = new ArrayList<>();


        mRecommendedModelArrayList = new ArrayList<>();
        mServicesModelArrayList = new ArrayList<>();
        mAdapterMySubscriptionRecommanded = new AdapterMySubscriptionRecommanded(getActivity(), mRecommendedModelArrayList, MySubscriptionRecommendedFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewRecommended.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerviewRecommended.setAdapter(mAdapterMySubscriptionRecommanded);
        recyclerviewRecommended.setLayoutManager(linearLayoutManager);
        mSessionManager = new SessionManager(getActivity());

        //----------------------------------------------------------------------
        bgImage = new Integer[10];
        bgImage[0] = R.drawable.bg_gradient_ten;
        bgImage[1] = R.drawable.bg_gradient_nine;
        bgImage[2] = R.drawable.bg_gradient_eight;
        bgImage[3] = R.drawable.bg_gradient_seven;
        bgImage[4] = R.drawable.bg_gradient_six;
        bgImage[5] = R.drawable.bg_gradient_five;
        bgImage[6] = R.drawable.bg_gradient_four;
        bgImage[7] = R.drawable.bg_gradient_three;
        bgImage[8] = R.drawable.bg_gradient_two;
        bgImage[9] = R.drawable.bg_gradient_one;
        //------------------------------------------------------------------------





        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            try {
                if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                {
                    counter = 0;
                    CallRecommendedSubscriptionList();

                } else {

                    UtilityMethods.showInternetDialog(getActivity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    //Method to Call Recommended SubscriptionList
    private void CallRecommendedSubscriptionList() {

        UtilityMethods.tuchOff(ProgressBarRecommendedSubscription);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUBSCRIPTION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallRecommenededSubscriptionList -->>" + response);

                try {
                    mRecommendedModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraySubscripations = jsonObject.getJSONArray("subscripations");

                        for (int i = 0; i < jsonArraySubscripations.length(); i++) {

                            JSONObject jsonObjectSubscripations = jsonArraySubscripations.getJSONObject(i);

                            RecommendedModel recommendedModel = new RecommendedModel();

                            recommendedModel.setPk_id(jsonObjectSubscripations.getString("pk_id"));
                            recommendedModel.setNofoleads(jsonObjectSubscripations.getString("nofoleads"));
                            recommendedModel.setHeading(jsonObjectSubscripations.getString("heading"));
                            // recommendedModel.setDays(jsonObjectSubscripations.getString("days"));


                            if (jsonObjectSubscripations.getString("type_date_days").equals("1")) {
                                recommendedModel.setDays(jsonObjectSubscripations.getString("number_date_days"));
                            } else if (jsonObjectSubscripations.getString("type_date_days").equals("2")) {
                                recommendedModel.setDays(jsonObjectSubscripations.getString("days"));
                            }


                            recommendedModel.setPrice(jsonObjectSubscripations.getString("price"));
                            recommendedModel.setStatus(jsonObjectSubscripations.getString("status"));
                            recommendedModel.setCountry_name(jsonObjectSubscripations.getString("country_name"));
                            recommendedModel.setDescription(jsonObjectSubscripations.getString("description"));
                            recommendedModel.setNoofimages(jsonObjectSubscripations.getString("noofimages"));
                            recommendedModel.setCurrency(jsonObjectSubscripations.getString("currency"));
                            recommendedModel.setBackground(bgImage[counter]);

                            recommendedModel.setPrice(jsonObjectSubscripations.getString("price"));
                            currency = jsonObjectSubscripations.getString("currency");


                            counter++;
                            if (counter == 9) {
                                counter = 0;
                            }

                            JSONArray jsonArrayServices = jsonObjectSubscripations.getJSONArray("services");

                            mServicesModelArrayList.clear();
                            for (int k = 0; k < jsonArrayServices.length(); k++) {

                                JSONObject jsonObjectServices = jsonArrayServices.getJSONObject(k);

                                ServicesModel servicesModel = new ServicesModel();

                                servicesModel.setPk_id(jsonObjectServices.getString("pk_id"));
                                servicesModel.setServices(jsonObjectServices.getString("services"));

                                mServicesModelArrayList.add(servicesModel);

                            }

                            Services = "";
                            for (int j = 0; j < mServicesModelArrayList.size(); j++) {

                                String str = mServicesModelArrayList.get(j).getServices();

                                if (Services.equals("")) {

                                    Services = str;

                                } else {

                                    Services = Services + ", " + str;

                                }


                            }


                            recommendedModel.setServices(Services);

                            mRecommendedModelArrayList.add(recommendedModel);

                            mRecommendedModelArrayList.get(i).setGrandtotal(jsonObjectSubscripations.getString("grandtotal"));


                            //mRecommendedModelArrayList.get(i).getmTaxModelArrayList().clear();

                            JSONArray jsonArraytax_final = jsonObjectSubscripations.getJSONArray("tax_final");


                            ArrayList<TaxModel> mTaxModelArrayList = new ArrayList<>();

                            for (int j = 0; j < jsonArraytax_final.length(); j++) {

                                JSONObject jsonObjecttax_final = jsonArraytax_final.getJSONObject(j);

                                TaxModel taxModel = new TaxModel();

                                taxModel.setTax(jsonObjecttax_final.getString("tax"));
                                taxModel.setTitle(jsonObjecttax_final.getString("title"));
                                taxModel.setTax_amount(jsonObjecttax_final.getString("tax_amount"));

                                mTaxModelArrayList.add(taxModel);
                            }

                            mRecommendedModelArrayList.get(i).setmTaxModelArrayList(mTaxModelArrayList);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                    }

                    mAdapterMySubscriptionRecommanded.notifyDataSetChanged();

                    if (mRecommendedModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerviewRecommended.setVisibility(GONE);

                    } else {

                        textViewErrorMessage.setVisibility(GONE);
                        recyclerviewRecommended.setVisibility(VISIBLE);

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
                UtilityMethods.tuchOn(ProgressBarRecommendedSubscription);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("country", mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY));
                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

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


    @Override
    public void onResume() {
        super.onResume();

        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {
            counter = 0;
            CallRecommendedSubscriptionList();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }
    }

    //Method to Call Buy Subscription
//    public void CallBuySubscription(final String subscriptionid) {
//
//        UtilityMethods.tuchOff(ProgressBarRecommendedSubscription);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUBS_BUYNOW, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("response", "CallBuySubscription -->>" + response);
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("error_code").equals("1")) {
//
//
//                        UtilityMethods.showSuccessToast(getActivity(), "Subscription purchased successfully");
//
//                        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
//                        {
//                            counter = 0;
//                            CallRecommendedSubscriptionList();
//
//                        } else {
//
//                            UtilityMethods.showInternetDialog(getActivity());
//                        }
//
//                    } else if (jsonObject.getString("error_code").equals("0")) {
//                    } else if (jsonObject.getString("error_code").equals("2")) {
//                    } else if (jsonObject.getString("error_code").equals("10")) {
//
//                        UtilityMethods.UserInactivePopup(getActivity());
//                    } else {
//                    }
//
//                    mAdapterMySubscriptionRecommanded.notifyDataSetChanged();
//
//                    if (mRecommendedModelArrayList.size() == 0) {
//
//                        textViewErrorMessage.setVisibility(VISIBLE);
//                        recyclerviewRecommended.setVisibility(GONE);
//
//                    } else {
//
//                        textViewErrorMessage.setVisibility(GONE);
//                        recyclerviewRecommended.setVisibility(VISIBLE);
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                UtilityMethods.tuchOn(ProgressBarRecommendedSubscription);
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
//                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.check_internet_problem));
//
//                    } else if (error instanceof AuthFailureError) {
//
//                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.auth_fail));
//                    } else if (error instanceof ServerError) {
//
//                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.server_error));
//                    } else if (error instanceof NetworkError) {
//
//                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.network_error));
//                    } else if (error instanceof ParseError) {
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//                UtilityMethods.tuchOn(ProgressBarRecommendedSubscription);
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("country", mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY));
//                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
//                params.put("subscriptionid", subscriptionid);
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
//        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }

    public void ShowTaxList(final int pos, final String subscriptionid) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = mInflater.inflate(R.layout.dialog_buy_subscription, null);
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


        LinearLayout linearLayoutTaxRow = alertDialog.findViewById(R.id.linearLayoutTaxRow);
        LinearLayout linear_layout_pay_now = alertDialog.findViewById(R.id.linear_layout_pay_now);
        TextView tv_subscription_amount = alertDialog.findViewById(R.id.tv_subscription_amount);
        TextView tv_grand_total = alertDialog.findViewById(R.id.tv_grand_total);
        TextView txt_name = alertDialog.findViewById(R.id.txt_name);
        ImageView iv_cancel = alertDialog.findViewById(R.id.iv_cancel);
        RadioButton radioCash = alertDialog.findViewById(R.id.radio_cash);
        RadioButton radioOnline = alertDialog.findViewById(R.id.radio_online);
        radioCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
//                    lblPaymentType.setText(getString(R.string.lbl_cash));
                }
            }
        });

        radioOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
//                    lblPaymentType.setText(getString(R.string.lbl_online));
                }
            }
        });

        tv_grand_total.setText(mRecommendedModelArrayList.get(pos).getGrandtotal() + " " + currency);
        tv_subscription_amount.setText(mRecommendedModelArrayList.get(pos).getPrice() + " " + currency);
        txt_name.setText(mRecommendedModelArrayList.get(pos).getHeading());
        if (mRecommendedModelArrayList.get(pos).getPrice().equals("0")){
            radioOnline.setVisibility(GONE);
        }else {
            radioOnline.setVisibility(VISIBLE);
        }

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        linear_layout_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                if (validation()) {
                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        Log.e("currency", "currency : " + mRecommendedModelArrayList.get(pos).getCurrency());
                        Intent intent = new Intent(getActivity(), PaymentForSubscriptionActivity.class);
                        intent.putExtra("subscriptionid", subscriptionid);
                        intent.putExtra("Grandtotal", mRecommendedModelArrayList.get(pos).getGrandtotal());
                        intent.putExtra("pay_type", pay_type);
                        intent.putExtra("currency", mRecommendedModelArrayList.get(pos).getCurrency());

                        startActivity(intent);

                    } else {
                        UtilityMethods.showInternetDialog(getActivity());
                    }
                }


            }

            private boolean validation() {
                boolean isValid = false;
                if (radioCash.isChecked()) {
                    isValid = true;
                    pay_type = "1";
                } else if (radioOnline.isChecked()) {
                    isValid = true;
                    pay_type = "2";
                } else {
                    UtilityMethods.showInfoToast(getActivity(), "Please select payment type.");
                }
                return isValid;
            }
        });

        linearLayoutTaxRow.removeAllViews();


        Log.e("check", "====>" + mRecommendedModelArrayList.get(pos).getmTaxModelArrayList().size());

        //Display list view runtime inflate
        for (int i = 0; i < mRecommendedModelArrayList.get(pos).getmTaxModelArrayList().size(); i++) {

            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.adapter_tax_row, null);

            linearLayoutTaxRow.addView(view);

            TextView tv_tax_name = view.findViewById(R.id.tv_tax_name);
            TextView tv_tax_amount = view.findViewById(R.id.tv_tax_amount);


            tv_tax_name.setText(mRecommendedModelArrayList.get(pos).getmTaxModelArrayList().get(i).getTitle() + " ( " + mRecommendedModelArrayList.get(pos).getmTaxModelArrayList().get(i).getTax() + " %)");
            tv_tax_amount.setText(mRecommendedModelArrayList.get(pos).getmTaxModelArrayList().get(i).getTax_amount() + " " + currency);


        }

    }


}
