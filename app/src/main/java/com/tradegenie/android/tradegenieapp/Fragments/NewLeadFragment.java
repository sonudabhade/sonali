package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import com.tradegenie.android.tradegenieapp.Activity.PaymentForNewLeadActivity;
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
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.URL_CHECK_REMAINING_SUBSCRIPTION;

public class NewLeadFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.ProgressBarNewLeadDetailsView)
    RelativeLayout ProgressBarNewLeadDetailsView;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_lead_id)
    TextView tvLeadId;
    @BindView(R.id.tv_created_date)
    TextView tvCreatedDate;
    @BindView(R.id.tv_qty)
    TextView tvQty;
    @BindView(R.id.tv_price_range)
    TextView tvPriceRange;
    @BindView(R.id.tv_preferred)
    TextView tvPreferred;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_descripation)
    TextView tvDescripation;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.linearLayoutTaxRow)
    LinearLayout linearLayoutTaxRow;
    @BindView(R.id.tv_grand_total)
    TextView tvGrandTotal;
    @BindView(R.id.tv_lead_amount)
    TextView tvLeadAmount;
    @BindView(R.id.linear_layout_pay_now)
    LinearLayout linearLayoutPayNow;


    String GranTotal = "";
    @BindView(R.id.radio_cash)
    RadioButton radioCash;
    @BindView(R.id.radio_online)
    RadioButton radioOnline;
    @BindView(R.id.radio_subscription)
    RadioButton radioSubscription;

    private ArrayList<TaxModel> mTaxModelArrayList;
    private SessionManager mSessionManager;
    private String pk_id = "";

    double TotalTax = 0;

    String pay_type = "";// 1-cash & 2- online & 3 - subscription
    private String currency = "";

    public NewLeadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_lead, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.lbl_buy_lead));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());
        mTaxModelArrayList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            pk_id = bundle.getString("pk_id");

        }

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

        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            getNewEnquiryDetailView();


        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }


    //Method to get New Enquiry DetailView
    private void getNewEnquiryDetailView() {

        UtilityMethods.tuchOff(ProgressBarNewLeadDetailsView);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ENQUIRY_VIEWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "URL_ENQUIRY_VIEWS -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        currency = jsonObject.getString("currency");
                        try{
                            if (jsonObject.getString("post_type").equals("1")) {
                                tvProductName.setText(jsonObject.getString("product_name"));
                            }else {
                                tvProductName.setText(jsonObject.getString("enquiry_type"));
                            }
                        }catch (Exception e){e.printStackTrace();}
                        tvLeadId.setText("Enquiry ID - " + jsonObject.getString("lead_id"));
                        tvCreatedDate.setText(jsonObject.getString("created_date"));
                        tvQty.setText(jsonObject.getString("qty"));
                        tvPriceRange.setText(jsonObject.getString("en_currency") + " " + jsonObject.getString("price_from") + " to " + jsonObject.getString("price_to"));
                        tvPreferred.setText(jsonObject.getString("preferred"));
                        tvType.setText(jsonObject.getString("typeofbuyer"));
                        tvDescripation.setText(jsonObject.getString("description"));
                        //tvGrandTotal.setText(jsonObject.getString("grand_total"));
                        tvGrandTotal.setText(jsonObject.getString("grand_total") + " " + currency);
                        String leadrate = jsonObject.getString("leadrate");
                        if (leadrate.equals("0")){
                            radioOnline.setVisibility(GONE);
                        }else {
                            radioOnline.setVisibility(VISIBLE);
                        }
                        tvLeadAmount.setText(leadrate + " " + currency);
                        GranTotal = jsonObject.getString("grand_total");
                        JSONArray jsonArrayTaxarray = jsonObject.getJSONArray("tax_array");

                        mTaxModelArrayList.clear();
                        for (int i = 0; i < jsonArrayTaxarray.length(); i++) {


                            JSONObject jsonObjectTaxarray = jsonArrayTaxarray.getJSONObject(i);
                            TaxModel taxModel = new TaxModel();
                            taxModel.setTitle(jsonObjectTaxarray.getString("title"));
                            taxModel.setTax(jsonObjectTaxarray.getString("tax"));
                            taxModel.setFk_country_id(jsonObjectTaxarray.getString("fk_country_id"));
                            taxModel.setTax_amount(jsonObjectTaxarray.getString("tax_amount"));
                            taxModel.setCurrency(currency);

                            mTaxModelArrayList.add(taxModel);

                        }

                        if (jsonObject.getString("nofoleads").trim().isEmpty()){
                            radioSubscription.setVisibility(GONE);
                        }else {
                            int nofoleads = Integer.parseInt(jsonObject.getString("nofoleads"));
                            int used_leads = Integer.parseInt(jsonObject.getString("used_leads"));
                            if (nofoleads == used_leads) {
                                radioSubscription.setVisibility(GONE);
                            } else {
                                radioSubscription.setVisibility(VISIBLE);
                            }
                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    linearLayoutTaxRow.removeAllViews();
                    ShowTaxList();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarNewLeadDetailsView);


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
                UtilityMethods.tuchOn(ProgressBarNewLeadDetailsView);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("leadpkid", pk_id);

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void ShowTaxList() {


        //Display list view runtime inflate
        for (int i = 0; i < mTaxModelArrayList.size(); i++) {

            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.adapter_tax_row, null);

            linearLayoutTaxRow.addView(view);

            TextView tv_tax_name = view.findViewById(R.id.tv_tax_name);
            TextView tv_tax_amount = view.findViewById(R.id.tv_tax_amount);


            tv_tax_name.setText(mTaxModelArrayList.get(i).getTitle() + " ( " + mTaxModelArrayList.get(i).getTax() + " %)");
            tv_tax_amount.setText(mTaxModelArrayList.get(i).getTax_amount() + " " + mTaxModelArrayList.get(i).getCurrency());


            TotalTax = TotalTax + Double.parseDouble(mTaxModelArrayList.get(i).getTax_amount());
        }

    }

    //Method to get New Enquiry DetailView
//    private void CallPayNow() {
//
//        UtilityMethods.tuchOff(ProgressBarNewLeadDetailsView);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PAY_NOW, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("response", "CallPayNow -->>" + response);
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("error_code").equals("1")) {
//
//
//                        Constants.mMainActivity.WhichLeadFragmentIsopen = "My Leads Fragment";
//
//                        getActivity().getFragmentManager().popBackStack();
//
//
//                    } else if (jsonObject.getString("error_code").equals("0")) {
//                    } else if (jsonObject.getString("error_code").equals("2")) {
//                    } else if (jsonObject.getString("error_code").equals("10")) {
//
//                        UtilityMethods.UserInactivePopup(getActivity());
//                    } else {
//                        UtilityMethods.showInfoToast(getActivity(), "Leads are not available. Please check your Subscription package.");
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                UtilityMethods.tuchOn(ProgressBarNewLeadDetailsView);
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
//                UtilityMethods.tuchOn(ProgressBarNewLeadDetailsView);
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
//                params.put("leadpkid", pk_id);
//                params.put("lead_rate", tvLeadAmount.getText().toString().trim());
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


    @OnClick(R.id.linear_layout_pay_now)
    public void onViewClicked() {

        if (validation()) {
            if (InternetConnection.isInternetAvailable(getActivity())) {
                UtilityMethods.tuchOff(ProgressBarNewLeadDetailsView);
                CheckLeadsAreAvailable();
            } else {
                UtilityMethods.showInternetDialog(getActivity());
            }
        }




/*
        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            CallPayNow();


        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }*/
    }

    private boolean validation() {
        boolean isValid = false;
        if (radioCash.isChecked()) {
            isValid = true;
            pay_type = "1";
        } else if (radioOnline.isChecked()) {
            isValid = true;
            pay_type = "2";
        } else if (radioSubscription.isChecked()) {
            isValid = true;
            pay_type = "3";
        } else {
            UtilityMethods.showInfoToast(getActivity(), "Please select payment type.");
        }
        return isValid;
    }

    private void CheckLeadsAreAvailable() {

        UtilityMethods.tuchOff(ProgressBarNewLeadDetailsView);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_REMAINING_SUBSCRIPTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "URL_CHECK_REMAINING_SUBSCRIPTION -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (Integer.parseInt(jsonObject.getString("subs_count")) != 0) {


                            Log.e("currency", "currency : " + currency);
                            Log.e("leadpkid", "leadpkid " + pk_id);
                            Log.e("lead_rate", "lead_rate " + tvLeadAmount.getText().toString().trim());
                            Log.e("Grandtotal", "Grandtotal if subscri available " + GranTotal);


                            Intent intent = new Intent(getActivity(), PaymentForNewLeadActivity.class);
                            intent.putExtra("leadpkid", pk_id);
                            intent.putExtra("lead_rate", tvLeadAmount.getText().toString().trim());
                            intent.putExtra("Grandtotal", GranTotal);
                            intent.putExtra("pay_type", pay_type);
                            intent.putExtra("currency", currency);
                            startActivityForResult(intent, 10001);


                        } else {


                            String lead_rate = jsonObject.getString("lead_rate");
                            double GrandTotalDouble = Double.parseDouble(lead_rate) + TotalTax;

                            Log.e("currency", "currency : " + currency);
                            Log.e("leadpkid", "leadpkid " + pk_id);
                            Log.e("lead_rate", "lead_rate " + lead_rate);
                            Log.e("Grandtotal", "Grandtotal " + GrandTotalDouble);


                            Intent intent = new Intent(getActivity(), PaymentForNewLeadActivity.class);
                            intent.putExtra("leadpkid", pk_id);
                            intent.putExtra("lead_rate", lead_rate);
                            intent.putExtra("Grandtotal", String.valueOf(GrandTotalDouble));
                            intent.putExtra("pay_type", pay_type);
                            intent.putExtra("currency", currency);
                            startActivityForResult(intent, 10001);


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

                UtilityMethods.tuchOn(ProgressBarNewLeadDetailsView);


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
                UtilityMethods.tuchOn(ProgressBarNewLeadDetailsView);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("leadpkid", pk_id);

                Log.e("check", "======> " + URL_CHECK_REMAINING_SUBSCRIPTION + " " + params);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10001) {
            Constants.mMainActivity.WhichLeadFragmentIsopen = "My Leads Fragment";
            getFragmentManager().popBackStack();
        }
    }
}
