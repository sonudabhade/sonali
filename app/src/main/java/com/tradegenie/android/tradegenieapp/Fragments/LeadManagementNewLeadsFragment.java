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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterNewLead;
import com.tradegenie.android.tradegenieapp.Models.NewLeadModel;
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

public class LeadManagementNewLeadsFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_new_lead)
    RecyclerView recyclerViewNewLead;
    @BindView(R.id.ProgressBarNewLead)
    RelativeLayout ProgressBarNewLead;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;

    int OFFSET = 0;
    //ProgressDialog progressBar;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private SessionManager mSessionManager;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<NewLeadModel> mNewLeadModelArrayList;
    private AdapterNewLead mAdapterNewLead;

    public LeadManagementNewLeadsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_leads, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.lead_management));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());

        mNewLeadModelArrayList = new ArrayList<>();
        mAdapterNewLead = new AdapterNewLead(getActivity(), mNewLeadModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewNewLead.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewNewLead.setAdapter(mAdapterNewLead);
        recyclerViewNewLead.setLayoutManager(linearLayoutManager);

        if (InternetConnection.isInternetAvailable(getActivity())) {
            OFFSET = 0;
            mNewLeadModelArrayList.clear();
            mAdapterNewLead.notifyDataSetChanged();
            UtilityMethods.tuchOff(ProgressBarNewLead);
            getNewLeads();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        recyclerViewNewLead.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Log.e("size", "size " + mNewLeadModelArrayList.size());

                            OFFSET = OFFSET + 10;

                            if (OFFSET >= mNewLeadModelArrayList.size()) {
                                //progressBar = new ProgressDialog(getActivity());
                                //progressBar.setMessage("Please wait");
                                //progressBar.setCancelable(false);
                                // progressBar.show();
                                UtilityMethods.tuchOff(ProgressBarNewLead);
                                getNewLeads();
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
                    mNewLeadModelArrayList.clear();
                    mAdapterNewLead.notifyDataSetChanged();

                    // UtilityMethods.tuchOff(ProgressBarNewLead);
                    getNewLeads();
                    Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";


                } else {

                    UtilityMethods.showInternetDialog(getActivity());


                }
            } catch (Exception e) {
                e.printStackTrace();
                Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";

            }


        }

    }

    //Method to get New Leads
    private void getNewLeads() {

        //UtilityMethods.tuchOff(ProgressBarNewLead);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NEW_LEADS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getNewLeads -->>" + response);

                loading = true;
                try {
                    // progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (OFFSET == 0) {
                    mNewLeadModelArrayList.clear();
                }
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONArray jsonArrayFinalarray = jsonObject.getJSONArray("finalarray");

                        for (int i = 0; i < jsonArrayFinalarray.length(); i++) {

                            JSONObject jsonObjectFinal = jsonArrayFinalarray.getJSONObject(i);

                            NewLeadModel newLeadModel = new NewLeadModel();

                            newLeadModel.setPk_id(jsonObjectFinal.getString("pk_id"));
                            newLeadModel.setProduct_name(jsonObjectFinal.getString("product_name"));
                            newLeadModel.setCurrency(jsonObjectFinal.getString("currency"));
                            newLeadModel.setPrice_from(jsonObjectFinal.getString("price_from"));
                            newLeadModel.setPrice_to(jsonObjectFinal.getString("price_to"));
                            newLeadModel.setQty(jsonObjectFinal.getString("qty"));
                            newLeadModel.setCreated_date(jsonObjectFinal.getString("created_date"));
                            newLeadModel.setFk_from_id(jsonObjectFinal.getString("fk_from_id"));
                            newLeadModel.setFk_conuntry_id(jsonObjectFinal.getString("fk_conuntry_id"));
                            newLeadModel.setLeads(jsonObjectFinal.getString("leads"));
                            newLeadModel.setPost_type(jsonObjectFinal.getString("post_type"));
                            newLeadModel.setEnquiry_type(jsonObjectFinal.getString("enquiry_type"));
                            newLeadModel.setEn_currency(jsonObjectFinal.getString("en_currency"));

                            mNewLeadModelArrayList.add(newLeadModel);


                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        //UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterNewLead.notifyDataSetChanged();

                    if (mNewLeadModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerViewNewLead.setVisibility(GONE);

                    } else {


                        textViewErrorMessage.setVisibility(GONE);
                        recyclerViewNewLead.setVisibility(VISIBLE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarNewLead);


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
                UtilityMethods.tuchOn(ProgressBarNewLead);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                //params.put("userid", "131");
                params.put("limit", "20");
                params.put("offset", String.valueOf(OFFSET));

                Log.e("params", "getNewLeads======>" + params);

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
