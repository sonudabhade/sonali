package com.tradegenie.android.tradegenieapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterTenderList;
import com.tradegenie.android.tradegenieapp.Models.TenderModel;
import com.tradegenie.android.tradegenieapp.Models.TypeOfBuyerModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONException;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TenderListFragment extends Fragment {


    @BindView(R.id.recycler_view_post_req)
    RecyclerView recyclerViewPostReq;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressBarPostEnqList)
    RelativeLayout ProgressBarPostEnqList;
    Unbinder unbinder;
    @BindView(R.id.linear_layout_new_tender)
    LinearLayout linearLayoutNewTender;
    private ArrayList<TenderModel> mTenderArrayList;
    AdapterTenderList mAdapterTenderList;
    private LinearLayoutManager linearLayoutManager;

    SessionManager mSessionManager;
    int OFFSET = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;


    public TenderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tender_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        Constants.mMainActivity.setToolBarName(getString(R.string.lbl_tender));

        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mTenderArrayList = new ArrayList<>();

        /*mTenderArrayList.add(new TenderModel());
        mTenderArrayList.add(new TenderModel());
        mTenderArrayList.add(new TenderModel());
        mTenderArrayList.add(new TenderModel());*/


        if (InternetConnection.isInternetAvailable(getActivity())) {
            UtilityMethods.tuchOff(ProgressBarPostEnqList);
            OFFSET = 0;
            GetTenderList();
        } else {
            UtilityMethods.showInternetDialog(getActivity());
        }


        mAdapterTenderList = new AdapterTenderList(getActivity(), mTenderArrayList,"MyPurchasedTenders");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPostReq.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewPostReq.setAdapter(mAdapterTenderList);
        recyclerViewPostReq.setLayoutManager(linearLayoutManager);


        recyclerViewPostReq.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Log.e("size", "size " + mTenderArrayList.size());

                            if (OFFSET <= mTenderArrayList.size()) {


                                if (InternetConnection.isInternetAvailable(getActivity())) {
                                    UtilityMethods.tuchOff(ProgressBarPostEnqList);
                                    GetTenderList();

                                } else {
                                    UtilityMethods.showInternetDialog(getActivity());
                                }

                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }

                }


                //To animate bottom view
                if (dx > 0) {
                    System.out.println("Scrolled Right");
                } else if (dx < 0) {
                    System.out.println("Scrolled Left");
                } else {
                    System.out.println("No Horizontal Scrolled");
                }


            }
        });


        return view;
    }

    //Method to get my tender list
    private void GetTenderList() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MY_TENDER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading = true;
                Log.e("response", "URL_MY_TENDER_LIST -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {
                        textViewErrorMessage.setVisibility(GONE);

                        JSONArray finalsResult = jsonObject.getJSONArray("finalsResult");

                        for (int j = 0; j < finalsResult.length(); j++) {
                            JSONObject tenderObj = finalsResult.getJSONObject(j);

                            TenderModel tenderModel = new TenderModel();
                            tenderModel.setPk_id(tenderObj.getString("pk_id"));
                            tenderModel.setFk_user_id(tenderObj.getString("fk_user_id"));
                            tenderModel.setTender_name(tenderObj.getString("tender_name"));
                            tenderModel.setCost(tenderObj.getString("purchase_total_amount"));


                            tenderModel.setCurrency(tenderObj.getString("currency"));
                            tenderModel.setCreated_date(tenderObj.getString("created_date"));


                            mTenderArrayList.add(tenderModel);
                        }
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        // UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                mAdapterTenderList.notifyDataSetChanged();
                OFFSET = OFFSET + 20;
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
                params.put("limit", "20");
                params.put("offset", String.valueOf(OFFSET));

                Log.e("params", "params" + Constants.URL_MY_TENDER_LIST + " params ---> " + params.toString());
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

    @OnClick(R.id.linear_layout_new_tender)
    public void onViewClicked() {

        AddNewTenderFragment addNewTenderFragment = new AddNewTenderFragment();

        Constants.mMainActivity.ChangeFragments(addNewTenderFragment, "AddNewTenderFragment");
    }
}
