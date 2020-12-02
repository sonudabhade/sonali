package com.tradegenie.android.tradegenieapp.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.CompoundButton;
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
import com.tradegenie.android.tradegenieapp.Activity.PaymentForNewTenderActivity;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.URL_CHECK_REMAINING_SUBSCRIPTION_TENDER;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTenderDetailFragment extends Fragment {


    @BindView(R.id.txt_tender_name)
    TextView txtTenderName;
    @BindView(R.id.txt_tender_id)
    TextView txtTenderId;
    @BindView(R.id.txt_created_date)
    TextView txtCreatedDate;
    @BindView(R.id.txt_description)
    TextView txtDescription;
    @BindView(R.id.txt_tender_link)
    TextView txtTenderLink;
    @BindView(R.id.txt_tender_cost)
    TextView txtTenderCost;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_tender_amount)
    TextView tvTenderAmount;
    @BindView(R.id.linearLayoutTaxRow)
    LinearLayout linearLayoutTaxRow;
    @BindView(R.id.txt_grand_total)
    TextView txtGrandTotal;
    @BindView(R.id.linear_layout_pay_now)
    LinearLayout linearLayoutPayNow;
    @BindView(R.id.ProgressBarPostEnqList)
    RelativeLayout ProgressBarPostEnqList;
    Unbinder unbinder;

    String GrandTotal;

    SessionManager mSessionManager;

    String tender_id;
    @BindView(R.id.Lnr_tax)
    LinearLayout LnrTax;

    double TotalTax = 0;
    @BindView(R.id.radio_cash)
    RadioButton radioCash;
    @BindView(R.id.radio_online)
    RadioButton radioOnline;
    String pay_type = "";// 1-cash & 2- online
    @BindView(R.id.radio_subscription)
    RadioButton radioSubscription;
    private String currency = "";

    public NewTenderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_tender_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        tender_id = getArguments().getString("tender_id");
        Constants.mMainActivity.setToolBarName("Buy Tender");

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

        if (InternetConnection.isInternetAvailable(getActivity())) {
            UtilityMethods.tuchOff(ProgressBarPostEnqList);

            GetTenderDetails();
        } else {
            UtilityMethods.showInternetDialog(getActivity());
        }

        return view;
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

    //MEthod to get tender detail
    private void GetTenderDetails() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NEW_TENDER_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "Tender detail -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        txtTenderName.setText(jsonObject.getString("tender_name"));
                        txtTenderId.setText("Tender ID - " + tender_id);
                        txtCreatedDate.setText(jsonObject.getString("tender_date"));
                        txtDescription.setText(jsonObject.getString("descripation"));
                        txtTenderLink.setText(jsonObject.getString("tender_link"));
                        currency = jsonObject.getString("currency");
                        String cost = jsonObject.getString("cost");
                        if (cost.equals("0")){
                            radioOnline.setVisibility(GONE);
                        }else {
                            radioOnline.setVisibility(VISIBLE);
                        }
                        tvTenderAmount.setText(jsonObject.getString("cost") + " " + jsonObject.getString("currency"));
                        txtGrandTotal.setText(jsonObject.getString("grand_total") + " " + jsonObject.getString("currency"));

                        GrandTotal = jsonObject.getString("grand_total");

                        JSONArray tax_array = jsonObject.getJSONArray("tax_array");

                        for (int i = 0; i < tax_array.length(); i++) {

                            JSONObject taxObj = tax_array.getJSONObject(i);


                            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View subview = layoutInflater.inflate(R.layout.row_tax, null);

                            TextView txt_tax_name = subview.findViewById(R.id.txt_tax_name);
                            TextView txt_amt = subview.findViewById(R.id.txt_amt);


                            txt_amt.setText(taxObj.getString("tax_amount") + " " + jsonObject.getString("currency"));
                            txt_tax_name.setText(taxObj.getString("title"));


                            LnrTax.addView(subview);

                            TotalTax = TotalTax + Double.parseDouble(taxObj.getString("tax_amount"));

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

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        // UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                UtilityMethods.tuchOn(ProgressBarPostEnqList);


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
                UtilityMethods.tuchOn(ProgressBarPostEnqList);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("tender_id", tender_id);

                Log.e("params", "params" + Constants.URL_NEW_TENDER_DETAIL + " params ---> " + params.toString());
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

    //MEthod to pay for tender
//    private void PayForTender() {
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PAY_FOR_TENDER, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("response", "Tender detail -->>" + response);
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("error_code").equals("1")) {
//                        UtilityMethods.tuchOn(ProgressBarPostEnqList);
//                        UtilityMethods.showToast(getActivity(), getString(R.string.payment_done));
//                        FragmentManager fragmentManager = getFragmentManager();
//                        fragmentManager.popBackStack();
//
//
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
//
//                UtilityMethods.tuchOn(ProgressBarPostEnqList);
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
//                UtilityMethods.tuchOn(ProgressBarPostEnqList);
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
//                params.put("tender_id", tender_id);
//
//                Log.e("params", "params" + Constants.URL_PAY_FOR_TENDER + " params ---> " + params.toString());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.linear_layout_pay_now)
    public void onViewClicked() {

        if (validation()) {
            if (InternetConnection.isInternetAvailable(getActivity())) {
                UtilityMethods.tuchOff(ProgressBarPostEnqList);
                CheckLeadsAreAvailable();
            } else {
                UtilityMethods.showInternetDialog(getActivity());
            }
        }





        /*

        if (InternetConnection.isInternetAvailable(getActivity())) {
            UtilityMethods.tuchOff(ProgressBarPostEnqList);

            PayForTender();
        } else {
            UtilityMethods.showInternetDialog(getActivity());
        }
*/


    }

    private void CheckLeadsAreAvailable() {

        UtilityMethods.tuchOff(ProgressBarPostEnqList);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_REMAINING_SUBSCRIPTION_TENDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getNewEnquriyDetailView -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (Integer.parseInt(jsonObject.getString("subs_count")) != 0) {


                          /*  Log.e("leadpkid", "leadpkid " + pk_id);
                            Log.e("lead_rate", "lead_rate " + tvLeadAmount.getText().toString().trim());
                            Log.e("Grandtotal", "Grandtotal if subscri available " + GranTotal);


                            Intent intent = new Intent(getActivity(), PaymentForNewLeadActivity.class);
                            intent.putExtra("leadpkid", pk_id);
                            intent.putExtra("lead_rate", tvLeadAmount.getText().toString().trim());
                            intent.putExtra("Grandtotal", GranTotal);
                            startActivityForResult(intent, 10001);
*/

                            Log.e("currency", "currency : " + currency);

                            Intent intent = new Intent(getActivity(), PaymentForNewTenderActivity.class);
                            intent.putExtra("tender_id", tender_id);
                            intent.putExtra("Grandtotal", GrandTotal);
                            intent.putExtra("pay_type", pay_type);
                            intent.putExtra("currency", currency);
                            startActivityForResult(intent, 10001);


                        } else {


                            String lead_rate = jsonObject.getString("tender_amount");
                            double GrandTotalDouble = Double.parseDouble(lead_rate) + TotalTax;


                           /* Log.e("leadpkid", "leadpkid " + pk_id);
                            Log.e("lead_rate", "lead_rate " + lead_rate);
                            Log.e("Grandtotal", "Grandtotal " + GrandTotalDouble);


                            Intent intent = new Intent(getActivity(), PaymentForNewLeadActivity.class);
                            intent.putExtra("leadpkid", pk_id);
                            intent.putExtra("lead_rate", lead_rate);
                            intent.putExtra("Grandtotal", String.valueOf(GrandTotalDouble));
                            startActivityForResult(intent, 10001);
*/
                            Log.e("currency", "currency : " + currency);

                            Intent intent = new Intent(getActivity(), PaymentForNewTenderActivity.class);
                            intent.putExtra("tender_id", tender_id);
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

                UtilityMethods.tuchOn(ProgressBarPostEnqList);


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
                UtilityMethods.tuchOn(ProgressBarPostEnqList);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("tenderid", tender_id);

                Log.e("check", "======> " + URL_CHECK_REMAINING_SUBSCRIPTION_TENDER + " " + params);

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
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        }
    }

    @OnClick(R.id.txt_tender_link)
    public void onViewClickedTender() {


        if (!txtTenderLink.getText().toString().trim().equals("")) {
            String StrURL = "";
            if (!txtTenderLink.getText().toString().trim().startsWith("http://") && !txtTenderLink.getText().toString().trim().startsWith("https://")) {
                StrURL = String.valueOf(Uri.parse("http://" + txtTenderLink.getText().toString().trim()));
            } else {
                StrURL = txtTenderLink.getText().toString().trim();
            }

            if (!URLUtil.isValidUrl(StrURL.toLowerCase())) {
                Toast.makeText(getActivity(), " This is not a valid link", Toast.LENGTH_LONG).show();
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StrURL));
                startActivity(browserIntent);
            }
        }


    }
}
