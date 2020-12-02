package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSellerNotification;
import com.tradegenie.android.tradegenieapp.Models.SellerNotificationModel;
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

public class SellerNotificationFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_seller_notification)
    RecyclerView recyclerViewSellerNotification;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressNotificationFragment)
    RelativeLayout ProgressNotificationFragment;

    private SessionManager mSessionManager;


    private ArrayList<SellerNotificationModel> mSellerNotificationModelArrayList;
    private AdapterSellerNotification mAdapterSellerNotification;
    private LinearLayoutManager linearLayoutManager;


    public SellerNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Notification");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        Constants.mMainActivity.sellerNotificationFragment = SellerNotificationFragment.this;
        mSessionManager = new SessionManager(getActivity());
        mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED, false);

        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            //Check personal details and business details
            if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")) {

                /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                Constants.mMainActivity.ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");*/


            } else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")) {


                BusinessDetailsFragment businessDetailsFragment = new BusinessDetailsFragment();
                Constants.mMainActivity.ChangeFragments(businessDetailsFragment, "BusinessDetailsFragment");


            }

        } else {

            if (mSessionManager.getStringData(Constants.KEY_BUYER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_BUYER_COUNTRY).equals("")) {

                //UtilityMethods.showInfoToast(getActivity(), "Please complete your profile details.");


                /*BuyerPersonalDetailsFragment buyerPersonalDetailsFragment = new BuyerPersonalDetailsFragment();
                Constants.mMainActivity.ChangeFragments(buyerPersonalDetailsFragment, "BuyerPersonalDetailsFragment");*/


            }


        }

        mSellerNotificationModelArrayList = new ArrayList<>();
        mAdapterSellerNotification = new AdapterSellerNotification(getActivity(), mSellerNotificationModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSellerNotification.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewSellerNotification.setAdapter(mAdapterSellerNotification);
        //recyclerViewProductListPending.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewSellerNotification.setLayoutManager(linearLayoutManager);


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            getNotification();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED, false);
        Constants.mMainActivity.HideNotificationBadge();
    }

    //Method to get About Us
    private void getNotification() {

        UtilityMethods.tuchOff(ProgressNotificationFragment);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NOTIFICATION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getNotification -->>" + response);

                try {

                    mSellerNotificationModelArrayList.clear();

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArrayNotificationlist = jsonObject.getJSONArray("notificationlist");

                        for (int i = 0; i < jsonArrayNotificationlist.length(); i++) {

                            JSONObject jsonObjectNotificationlist = jsonArrayNotificationlist.getJSONObject(i);

                            String pk_id = jsonObjectNotificationlist.getString("pk_id");
                            String user_id = jsonObjectNotificationlist.getString("user_id");
                            String message = jsonObjectNotificationlist.getString("message");
                            String created_date = jsonObjectNotificationlist.getString("created_date");
                            String subject = jsonObjectNotificationlist.getString("subject");

                            SellerNotificationModel sellerNotificationModel = new SellerNotificationModel(pk_id, user_id, message, created_date, subject);
                            mSellerNotificationModelArrayList.add(sellerNotificationModel);
                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterSellerNotification.notifyDataSetChanged();

                    if (mSellerNotificationModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        Constants.mMainActivity.setNotificationButton(GONE);


                    } else {

                        textViewErrorMessage.setVisibility(GONE);
                        Constants.mMainActivity.setNotificationButton(VISIBLE);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressNotificationFragment);


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
                UtilityMethods.tuchOn(ProgressNotificationFragment);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

                } else {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }


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

    //Method to get About Us
    public void CallDeleteNotification() {

        UtilityMethods.tuchOff(ProgressNotificationFragment);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NOTIFICATION_CLEAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getNotification -->>" + response);

                try {

                    mSellerNotificationModelArrayList.clear();

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        UtilityMethods.showSuccessToast(getActivity(), "Notification Deleted Successfully");

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterSellerNotification.notifyDataSetChanged();

                    if (mSellerNotificationModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        Constants.mMainActivity.ivDeleteNotification.setVisibility(GONE);

                    } else {

                        textViewErrorMessage.setVisibility(GONE);
                        Constants.mMainActivity.ivDeleteNotification.setVisibility(VISIBLE);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressNotificationFragment);


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
                UtilityMethods.tuchOn(ProgressNotificationFragment);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                } else {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }


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
    public void onPause() {
        super.onPause();
        Constants.mMainActivity.setNotificationButton(GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
