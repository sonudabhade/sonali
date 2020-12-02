package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.tradegenie.android.tradegenieapp.Activity.PaymentForUpgradeActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterMySubscription;
import com.tradegenie.android.tradegenieapp.Models.MySubscriptionModel;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MySubscriptionFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recyclerview_my_subscription)
    RecyclerView recyclerviewMySubscription;

    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressBarMySubscription)
    RelativeLayout ProgressBarMySubscription;

    private ArrayList<MySubscriptionModel> mMySubscriptionModelArrayList;
    private ArrayList<ServicesModel> mServicesModelArrayList;
    private AdapterMySubscription mAdapterMySubscription;
    private SessionManager mSessionManager;

    private ArrayList<TaxModel> mTaxModelArrayList;

    Integer[] bgImage;
    public int counter = 0;
    private String Services = "";

    String pay_type = "";// 1-cash & 2- online
    private DownloadManager downloadManager;

    public MySubscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_subcription, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("My Subscription");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mTaxModelArrayList = new ArrayList<>();

        mMySubscriptionModelArrayList = new ArrayList<>();
        mServicesModelArrayList = new ArrayList<>();
        mAdapterMySubscription = new AdapterMySubscription(getActivity(), mMySubscriptionModelArrayList, MySubscriptionFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewMySubscription.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerviewMySubscription.setAdapter(mAdapterMySubscription);
        recyclerviewMySubscription.setLayoutManager(linearLayoutManager);
        mSessionManager = new SessionManager(getActivity());
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


        //----------------------------------------------------------------------
        bgImage = new Integer[10];
        bgImage[0] = R.drawable.bg_gradient_one;
        bgImage[1] = R.drawable.bg_gradient_ten;
        bgImage[2] = R.drawable.bg_gradient_three;
        bgImage[3] = R.drawable.bg_gradient_four;
        bgImage[4] = R.drawable.bg_gradient_five;
        bgImage[5] = R.drawable.bg_gradient_six;
        bgImage[6] = R.drawable.bg_gradient_seven;
        bgImage[7] = R.drawable.bg_gradient_eight;
        bgImage[8] = R.drawable.bg_gradient_nine;
        bgImage[9] = R.drawable.bg_gradient_two;
        //----------------------------------------------------------------------


        //----------------------------------------------------------------------


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            counter = 0;
            CallMySubscriptionList();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


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
                    CallMySubscriptionList();

                } else {

                    UtilityMethods.showInternetDialog(getActivity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


    //Method to Call My SubscriptionList
    public void CallMySubscriptionList() {

        UtilityMethods.tuchOff(ProgressBarMySubscription);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MY_SUBSCRIPTION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallMySubscriptionList -->>" + response);

                try {
                    mMySubscriptionModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraySubscripations = jsonObject.getJSONArray("subscripations");

                        for (int i = 0; i < jsonArraySubscripations.length(); i++) {

                            JSONObject jsonObjectSubscripations = jsonArraySubscripations.getJSONObject(i);

                            MySubscriptionModel mySubscriptionModel = new MySubscriptionModel();

                            mySubscriptionModel.setNofoleads(jsonObjectSubscripations.getString("nofoleads"));
                            mySubscriptionModel.setPk_id(jsonObjectSubscripations.getString("pk_id"));
                            mySubscriptionModel.setHeading(jsonObjectSubscripations.getString("heading"));
                            mySubscriptionModel.setUsed_total_leads(jsonObjectSubscripations.getString("used_total_leads"));
                            mySubscriptionModel.setStatus(jsonObjectSubscripations.getString("status"));

                            if (jsonObjectSubscripations.getString("subscription_active").equals("1")) {
                                mySubscriptionModel.setDays(jsonObjectSubscripations.getString("days"));
                            } else {
                                mySubscriptionModel.setDays(jsonObjectSubscripations.getString("number_date_days"));
                            }

                            mySubscriptionModel.setPrice(jsonObjectSubscripations.getString("grand_total"));
                            mySubscriptionModel.setDescription(jsonObjectSubscripations.getString("description"));
                            mySubscriptionModel.setNoofimages(jsonObjectSubscripations.getString("noofimages"));
                            mySubscriptionModel.setStart_date(jsonObjectSubscripations.getString("start_date"));
                            mySubscriptionModel.setEnd_date(jsonObjectSubscripations.getString("end_date"));
                            mySubscriptionModel.setLead_used(jsonObjectSubscripations.getString("lead_used"));
                            mySubscriptionModel.setCurrency(jsonObjectSubscripations.getString("currency"));
                            mySubscriptionModel.setFk_sid(jsonObjectSubscripations.getString("fk_sid"));
                            try {
                                mySubscriptionModel.setInvoice_url(jsonObjectSubscripations.getString("invoice"));
                            }catch (Exception e){e.printStackTrace();}
                            mySubscriptionModel.setBackground(bgImage[counter]);


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
                            mySubscriptionModel.setServices(Services);

                            mySubscriptionModel.setPaid_status(jsonObjectSubscripations.getString("paid_status"));
                            mySubscriptionModel.setPay_type_status(jsonObjectSubscripations.getString("pay_type_status"));
                            mMySubscriptionModelArrayList.add(mySubscriptionModel);


                        }

                        /*JSONArray jsonArrayServices=jsonObject.getJSONArray("services");

                        for (int i=0;i<jsonArrayServices.length();i++){

                            JSONObject jsonObjectServices=jsonArrayServices.getJSONObject(i);

                            String pk_id=jsonObjectServices.getString("pk_id");
                            String services=jsonObjectServices.getString("services");


                        }*/


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                    }

                    mAdapterMySubscription.notifyDataSetChanged();

                    if (mMySubscriptionModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerviewMySubscription.setVisibility(GONE);

                    } else {

                        textViewErrorMessage.setVisibility(GONE);
                        recyclerviewMySubscription.setVisibility(VISIBLE);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarMySubscription);


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
                UtilityMethods.tuchOn(ProgressBarMySubscription);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("country", mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY));
                //params.put("country", "7");
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

    //Method to Call Upgrade Subscription
    public void CallUpgradeSubscription(final String sub_id, final String subscripation_id) {

        UtilityMethods.tuchOff(ProgressBarMySubscription);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUBSCRIPTION_DETALS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallUpgradeSubscription -->>" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraySubscripations = jsonObject.getJSONArray("subscripations");

                        for (int i = 0; i < jsonArraySubscripations.length(); i++) {

                            JSONObject jsonObjectSubscripations = jsonArraySubscripations.getJSONObject(i);


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

                            String currency = jsonObjectSubscripations.getString("currency");
                            JSONArray jsonArrayTaxarray = jsonObjectSubscripations.getJSONArray("tax_final");

                            mTaxModelArrayList.clear();
                            for (int j = 0; j < jsonArrayTaxarray.length(); j++) {


                                JSONObject jsonObjectTaxarray = jsonArrayTaxarray.getJSONObject(j);
                                TaxModel taxModel = new TaxModel();
                                taxModel.setTitle(jsonObjectTaxarray.getString("title"));
                                taxModel.setTax(jsonObjectTaxarray.getString("tax"));
                                taxModel.setFk_country_id(jsonObjectTaxarray.getString("fk_country_id"));
                                taxModel.setTax_amount(jsonObjectTaxarray.getString("tax_amount"));
                                taxModel.setCurrency(jsonObjectSubscripations.getString("currency"));

                                mTaxModelArrayList.add(taxModel);

                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View dialogView = mInflater.inflate(R.layout.dialog_subscription_upgrade, null);
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


                            RadioButton radio_cash = alertDialog.findViewById(R.id.radio_cash);
                            RadioButton radio_online = alertDialog.findViewById(R.id.radio_online);
                            ImageView iv_cancel = alertDialog.findViewById(R.id.iv_cancel);
                            TextView tv_title = alertDialog.findViewById(R.id.tv_title);
                            TextView tv_cost = alertDialog.findViewById(R.id.tv_cost);
                            TextView tv_leads_count = alertDialog.findViewById(R.id.tv_leads_count);
                            TextView tv_image_count = alertDialog.findViewById(R.id.tv_image_count);
                            TextView tv_days = alertDialog.findViewById(R.id.tv_days);
                            TextView tv_description = alertDialog.findViewById(R.id.tv_description);
                            TextView tv_title_services = alertDialog.findViewById(R.id.tv_title_services);
                            TextView tv_services = alertDialog.findViewById(R.id.tv_services);
                            TextView tv_grand_total = alertDialog.findViewById(R.id.tv_grand_total);
                            TextView tv_amount = alertDialog.findViewById(R.id.tv_amount);
                            LinearLayout linearLayoutTaxRow = alertDialog.findViewById(R.id.linearLayoutTaxRow);
                            LinearLayout linear_layout_upgrade_now = alertDialog.findViewById(R.id.linear_layout_upgrade_now);


                            if (Services.equals("")) {

                                tv_title_services.setVisibility(View.GONE);
                                tv_services.setVisibility(View.GONE);

                            } else {

                                tv_title_services.setVisibility(View.VISIBLE);
                                tv_services.setVisibility(View.VISIBLE);
                                tv_services.setText(Services);
                            }


                            tv_title.setText(jsonObjectSubscripations.getString("heading"));
                            String price = jsonObjectSubscripations.getString("price");
                            if (price.equals("0")){
                                radio_online.setVisibility(GONE);
                            }else {
                                radio_online.setVisibility(VISIBLE);
                            }
                            tv_cost.setText("Cost - " + jsonObjectSubscripations.getString("currency") + " " + price);

                            if (jsonObjectSubscripations.getString("type_date_days").equals("1")) {
                                tv_days.setText("Days - " + jsonObjectSubscripations.getString("number_date_days"));
                            } else if (jsonObjectSubscripations.getString("type_date_days").equals("2")) {
                                tv_days.setText("Days - " + jsonObjectSubscripations.getString("days"));
                            }


                            tv_leads_count.setText("Number of Leads - " + jsonObjectSubscripations.getString("nofoleads"));
                            tv_image_count.setText(getString(R.string.lbl_number_of_images) + " - " + jsonObjectSubscripations.getString("noofimages"));
                            tv_description.setText(jsonObjectSubscripations.getString("description"));
                            tv_amount.setText(jsonObjectSubscripations.getString("price") + " " + jsonObjectSubscripations.getString("currency"));
                            tv_grand_total.setText(jsonObjectSubscripations.getString("grandtotal") + " " + jsonObjectSubscripations.getString("currency"));

                            String grandTotal = jsonObjectSubscripations.getString("grandtotal");

                            linearLayoutTaxRow.removeAllViews();
                            for (int k = 0; k < mTaxModelArrayList.size(); k++) {

                                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = vi.inflate(R.layout.adapter_tax_row, null);

                                linearLayoutTaxRow.addView(view);
                                TextView tv_tax_name = view.findViewById(R.id.tv_tax_name);
                                TextView tv_tax_amount = view.findViewById(R.id.tv_tax_amount);


                                tv_tax_name.setText(mTaxModelArrayList.get(k).getTitle() + " ( " + mTaxModelArrayList.get(k).getTax() + " %)");
                                tv_tax_amount.setText(mTaxModelArrayList.get(k).getTax_amount() + " " + mTaxModelArrayList.get(k).getCurrency());


                            }

                            iv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alertDialog.dismiss();

                                }
                            });

                            linear_layout_upgrade_now.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (validation()) {
                                        alertDialog.dismiss();

                                        //Call payment first and then upgrade
                                        if (InternetConnection.isInternetAvailable(getActivity())) {
                                            Log.e("currency", "currency : " + currency);

                                            Intent intent = new Intent(getActivity(), PaymentForUpgradeActivity.class);
                                            intent.putExtra("sub_id", sub_id);
                                            intent.putExtra("subscripation_id", subscripation_id);
                                            intent.putExtra("amt", grandTotal);
                                            intent.putExtra("pay_type", pay_type);
                                            intent.putExtra("currency", currency);
                                            startActivity(intent);

                                        } else {
                                            UtilityMethods.showInternetDialog(getActivity());
                                        }
                                    }

                                    //  CallUpgradeNow(sub_id, subscripation_id);

                                }

                                private boolean validation() {
                                    boolean isValid = false;
                                    if (radio_cash.isChecked()) {
                                        isValid = true;
                                        pay_type = "1";
                                    } else if (radio_online.isChecked()) {
                                        isValid = true;
                                        pay_type = "2";
                                    }else {
                                        UtilityMethods.showInfoToast(getActivity(), "Please select payment type.");
                                    }
                                    return isValid;
                                }
                            });


                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                    }

                    mAdapterMySubscription.notifyDataSetChanged();

                    if (mMySubscriptionModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerviewMySubscription.setVisibility(GONE);

                    } else {

                        textViewErrorMessage.setVisibility(GONE);
                        recyclerviewMySubscription.setVisibility(VISIBLE);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarMySubscription);


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
                UtilityMethods.tuchOn(ProgressBarMySubscription);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("country", mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY));
                //params.put("country", "7");
                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("subscripation_id", subscripation_id);


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

    public void downloadInvoice(String invoice_download) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DOWNLOAD_INVOICE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("response", "download invoice -->>" + response);
//
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("error_code").equals("1")) {
//
//                        String invoice_download = jsonObject.getString("invoice_download");
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                            // Do something for Pie and above versions
//                            new DownloadTask(getActivity(), invoice_download);
//                        } else {
//                            // do something for phones running an SDK before Pie
//                            getDownlodFile(invoice_download);
//                        }
//
//                    } else if (jsonObject.getString("error_code").equals("0")) {
//                    } else if (jsonObject.getString("error_code").equals("2")) {
//                    } else if (jsonObject.getString("error_code").equals("10")) {
//
//                        UtilityMethods.UserInactivePopup(getActivity());
//                    } else {
//                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                //  UtilityMethods.tuchOn(ProgressBarPostEnqList);
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
//                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.check_internet_problem));
//
//                    } else if (error instanceof AuthFailureError) {
//
//                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.auth_fail));
//                    } else if (error instanceof ServerError) {
//
//                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.server_error));
//                    } else if (error instanceof NetworkError) {
//
//                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.network_error));
//                    } else if (error instanceof ParseError) {
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//                //UtilityMethods.tuchOn(ProgressBarPostEnqList);
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//
//                params.put("pay_id", fk_sid);
//                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Do something for Pie and above versions
            new DownloadTask(getActivity(), invoice_download);
        } else {
            // do something for phones running an SDK before Pie
            getDownlodFile(invoice_download);
        }
    }

    class DownloadTask {
        private static final String TAG = "Download Task";
        private Context context;

        private String downloadUrl = "", downloadFileName = "";
        private ProgressDialog progressDialog;

        public DownloadTask(Context context, String downloadUrl) {
            this.context = context;

            this.downloadUrl = downloadUrl;


            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL

            File myFile = new File(downloadFileName);

            if (myFile.exists()) {
                Log.e("path", "*******is exists******");
                Log.e("path", myFile.toString());
                Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", myFile);
                Log.e("uri", "uri-->>>" + uri.getPath());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (downloadUrl.toString().contains(".doc") || downloadUrl.toString().contains(".docx")) {
                    // Word document
                    intent.setDataAndType(uri, "application/msword");
                    //new
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else if (downloadUrl.toString().contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(uri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else if (downloadUrl.toString().contains(".ppt") || downloadUrl.toString().contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                    //new
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else if (downloadUrl.toString().contains(".xls") || downloadUrl.toString().contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                    //new
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                getActivity().startActivity(intent);

            } else {
                Log.e(TAG, downloadFileName);

                //Start Downloading Task
                new DownloadingTask().execute();
            }
        }

        private class DownloadingTask extends AsyncTask<Void, Void, Void> {

            File apkStorage = null;
            File outputFile = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (outputFile != null) {
                        progressDialog.dismiss();

                        UtilityMethods.showInfoToast(context, "Invoice has been downloaded successfully..");


                        if (outputFile.exists()) {
                            Log.e("path", "*******is exists******");
                            Log.e("path", outputFile.toString());
                            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", outputFile);
                            Log.e("uri", "uri-->>>" + uri.getPath());
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (outputFile.toString().contains(".doc") || outputFile.toString().contains(".docx")) {
                                // Word document
                                intent.setDataAndType(uri, "application/msword");
                                //new
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else if (outputFile.toString().contains(".pdf")) {
                                // PDF file
                                intent.setDataAndType(uri, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else if (outputFile.toString().contains(".ppt") || outputFile.toString().contains(".pptx")) {
                                // Powerpoint file
                                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                                //new
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else if (outputFile.toString().contains(".xls") || outputFile.toString().contains(".xlsx")) {
                                // Excel file
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                //new
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            getActivity().startActivity(intent);

                        }


                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 3000);

                        Log.e(TAG, "Download Failed");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    //Change button text if exception occurs

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

                }


                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage());

                    }


                    //Get File if SD card is present
                    if (new CheckForSDCard().isSDCardPresent()) {
                        apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "Tradegenie");
                    } else
                        Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e(TAG, "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                }

                return null;
            }
        }
    }

    class CheckForSDCard {
        //Check If SD Card is present or not method
        public boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }

    //Methods for Download file
    public void getDownlodFile(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File myFile = new File(Constants.KEY_PATH_OF_QUATATION + "/" + fileName);
        // File myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + fileName);
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
            getActivity().startActivity(intent);

        } else {
            Log.e("path", "*******Not exists******");
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
            request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);
            long refrance = downloadManager.enqueue(request);
        }

    }
//Method to download invoice -----------------------------------------------

    //Method Call Upgrade Now
//    private void CallUpgradeNow(final String sub_id, final String subscriptionid) {
//
//        UtilityMethods.tuchOff(ProgressBarMySubscription);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUBSCRIPTION_UPDATE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("response", "CallUpgradeNow -->>" + response);
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("error_code").equals("1")) {
//
//
//                        UtilityMethods.showSuccessToast(getActivity(), "Subscription upgrade successfully");
//                        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
//                        {
//
//                            counter = 0;
//                            CallMySubscriptionList();
//
//                        } else {
//
//                            UtilityMethods.showInternetDialog(getActivity());
//                        }
//
//
//                    } else if (jsonObject.getString("error_code").equals("0")) {
//                    } else if (jsonObject.getString("error_code").equals("2")) {
//                    } else if (jsonObject.getString("error_code").equals("10")) {
//
//                        UtilityMethods.UserInactivePopup(getActivity());
//                    } else {
//                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                UtilityMethods.tuchOn(ProgressBarMySubscription);
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
//                UtilityMethods.tuchOn(ProgressBarMySubscription);
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
//                params.put("country", mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY));
//                params.put("sub_id", sub_id);
//                params.put("subscriptionid", subscriptionid);
//
//                Log.e("CallUpgradeNow", "params======>" + params);
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
}
