package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterMyLead;
import com.tradegenie.android.tradegenieapp.Models.MyLeadModel;
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

public class LeadManagementMyLeadFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_my_lead)
    RecyclerView recyclerViewMyLead;
    @BindView(R.id.ProgressBarMyLead)
    RelativeLayout ProgressBarMyLead;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;

    private LinearLayoutManager linearLayoutManager;
    private SessionManager mSessionManager;

    private ArrayList<MyLeadModel> myLeadModelArrayList;
    private AdapterMyLead mAdapterMyLead;

    int OFFSET = 0;
    //ProgressDialog progressBar;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    public LeadManagementMyLeadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_leads, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.lead_management));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());


        myLeadModelArrayList = new ArrayList<>();
        mAdapterMyLead = new AdapterMyLead(getActivity(), myLeadModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewMyLead.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewMyLead.setAdapter(mAdapterMyLead);
        recyclerViewMyLead.setLayoutManager(linearLayoutManager);


        if (InternetConnection.isInternetAvailable(getActivity())) {
            OFFSET = 0;
            myLeadModelArrayList.clear();
            mAdapterMyLead.notifyDataSetChanged();
            UtilityMethods.tuchOff(ProgressBarMyLead);
            getMyLeads();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        recyclerViewMyLead.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.e("Last Item Wow !", "Last Item Wow !");
                            Log.e("size", "size " + myLeadModelArrayList.size());

                            OFFSET = OFFSET + 10;

                            if (OFFSET >= myLeadModelArrayList.size()) {
                                //progressBar = new ProgressDialog(getActivity());
                                //progressBar.setMessage("Please wait");
                                //progressBar.setCancelable(false);
                                // progressBar.show();

                                UtilityMethods.tuchOff(ProgressBarMyLead);

                                getMyLeads();
                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }

                }


            }
        });


        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            try {
                if (InternetConnection.isInternetAvailable(getActivity())) {

                    OFFSET = 0;
                    myLeadModelArrayList.clear();
                    mAdapterMyLead.notifyDataSetChanged();
                    // UtilityMethods.tuchOff(ProgressBarNewLead);
                    getMyLeads();
                    Constants.mMainActivity.WhichLeadFragmentIsopen = "My Leads Fragment";


                } else {

                    UtilityMethods.showInternetDialog(getActivity());


                }
            } catch (Exception e) {
                e.printStackTrace();

                Constants.mMainActivity.WhichLeadFragmentIsopen = "My Leads Fragment";

            }


        }

    }


    //Method to get My Leads
    private void getMyLeads() {

        // UtilityMethods.tuchOff(ProgressBarMyLead);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MY_LEADS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getMyLeads -->>" + response);

                loading = true;
                try {
                    // progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (OFFSET == 0) {
                    myLeadModelArrayList.clear();
                }
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONArray jsonArraymyleadlist = jsonObject.getJSONArray("myleadlist");

                        for (int i = 0; i < jsonArraymyleadlist.length(); i++) {

                            JSONObject jsonObjectmyleadlist = jsonArraymyleadlist.getJSONObject(i);

                            MyLeadModel myLeadModel = new MyLeadModel();


                            myLeadModel.setCurrency(jsonObjectmyleadlist.getString("currency"));
                            myLeadModel.setCreated_date(jsonObjectmyleadlist.getString("created_date"));
                            myLeadModel.setPk_id(jsonObjectmyleadlist.getString("pk_id"));
                            if (jsonObjectmyleadlist.getString("enquiry_type").equals("Product Requirement")) {
                                myLeadModel.setProduct_name(jsonObjectmyleadlist.getString("product_name"));
                            }else {
                                myLeadModel.setProduct_name(jsonObjectmyleadlist.getString("enquiry_type"));
                            }
                            myLeadModel.setHeading(jsonObjectmyleadlist.getString("heading"));
                            myLeadModel.setQuantity(jsonObjectmyleadlist.getString("quantity"));
                            myLeadModel.setPrice(jsonObjectmyleadlist.getString("price"));
                            myLeadModel.setComp_name(jsonObjectmyleadlist.getString("comp_name"));
                            myLeadModel.setAddress(jsonObjectmyleadlist.getString("address"));
                            myLeadModel.setPincode(jsonObjectmyleadlist.getString("pincode"));
                            myLeadModel.setPrice_from(jsonObjectmyleadlist.getString("price_from"));
                            myLeadModel.setPrice_to(jsonObjectmyleadlist.getString("price_to"));
                            myLeadModel.setEn_currency(jsonObjectmyleadlist.getString("en_currency"));


                            myLeadModelArrayList.add(myLeadModel);


                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        //UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterMyLead.notifyDataSetChanged();

                    if (myLeadModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerViewMyLead.setVisibility(GONE);

                    } else {


                        textViewErrorMessage.setVisibility(GONE);
                        recyclerViewMyLead.setVisibility(VISIBLE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarMyLead);


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
                UtilityMethods.tuchOn(ProgressBarMyLead);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                //params.put("userid", "241");
                params.put("limit", "10");
                params.put("offset", String.valueOf(OFFSET));

                Log.e("params", "getMyLeads======>" + params);

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
}
