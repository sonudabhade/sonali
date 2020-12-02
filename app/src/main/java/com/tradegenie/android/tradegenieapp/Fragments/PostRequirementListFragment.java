package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterPostRequirementList;
import com.tradegenie.android.tradegenieapp.Models.PostRequirementModel;
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

public class PostRequirementListFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_post_req)
    RecyclerView recyclerViewPostReq;
    @BindView(R.id.linear_layout_new_post)
    LinearLayout linearLayoutNewPost;

    int OFFSET = 0;


    String pk_id = "";
    //ProgressDialog progressBar;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressBarPostEnqList)
    RelativeLayout ProgressBarPostEnqList;
    private boolean loading = true;

    private SessionManager mSessionManager;

    private ArrayList<PostRequirementModel> mPostRequirementModelArrayList;
    private AdapterPostRequirementList mAdapterPostRequirementList;

    private LinearLayoutManager linearLayoutManager;

    public PostRequirementListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_requirement_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.post_requirement));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mPostRequirementModelArrayList = new ArrayList<>();
        mAdapterPostRequirementList = new AdapterPostRequirementList(getActivity(), mPostRequirementModelArrayList, pk_id);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPostReq.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewPostReq.setAdapter(mAdapterPostRequirementList);
        recyclerViewPostReq.setLayoutManager(linearLayoutManager);

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

               // UtilityMethods.showInfoToast(getActivity(), "Please complete your profile details.");


                /*BuyerPersonalDetailsFragment buyerPersonalDetailsFragment = new BuyerPersonalDetailsFragment();
                Constants.mMainActivity.ChangeFragments(buyerPersonalDetailsFragment, "BuyerPersonalDetailsFragment");*/


            }


        }


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {
            OFFSET = 0;
            UtilityMethods.tuchOff(ProgressBarPostEnqList);
            getPOSTENQUIRYLIST();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }

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
                            Log.e("size", "size " + mPostRequirementModelArrayList.size());

                            OFFSET = OFFSET + 10;

                            if (OFFSET >= mPostRequirementModelArrayList.size()) {
                                //progressBar = new ProgressDialog(getActivity());
                                //progressBar.setMessage("Please wait");
                                //progressBar.setCancelable(false);
                                // progressBar.show();

                                getPOSTENQUIRYLIST();
                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }

                }


            }
        });

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }


    //Method to get POST ENQUIRY LIST
    private void getPOSTENQUIRYLIST() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_POST_ENQUIRY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getPOSTENQUIRYLIST -->>" + response);

                loading = true;
                try {
                    //progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (OFFSET == 0) {
                    mPostRequirementModelArrayList.clear();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        // pk_id = jsonObject.getString("chatlisting_pk_id");

                        JSONArray jsonArrayMyEnquiryList = jsonObject.getJSONArray("MyEnquiryList");

                        for (int i = 0; i < jsonArrayMyEnquiryList.length(); i++) {

                            JSONObject jsonObjectMyEnquiryList = jsonArrayMyEnquiryList.getJSONObject(i);

                            PostRequirementModel postRequirementModel = new PostRequirementModel();

                            postRequirementModel.setLead_id(jsonObjectMyEnquiryList.getString("lead_id"));
                            postRequirementModel.setQty(jsonObjectMyEnquiryList.getString("qty"));
                            postRequirementModel.setCurrency(jsonObjectMyEnquiryList.getString("currency"));
                            /*postRequirementModel.setPrice_from(jsonObjectMyEnquiryList.getString("price_from"));
                            postRequirementModel.setPrice_to(jsonObjectMyEnquiryList.getString("price_to"));*/
                            postRequirementModel.setProduct_name(jsonObjectMyEnquiryList.getString("product_name"));
                            postRequirementModel.setCreated_date(jsonObjectMyEnquiryList.getString("created_date"));
                            postRequirementModel.setPost_type(jsonObjectMyEnquiryList.getString("post_type"));
                            postRequirementModel.setEnquiry_type(jsonObjectMyEnquiryList.getString("enquiry_type"));
                            postRequirementModel.setDescripation(jsonObjectMyEnquiryList.getString("descripation"));
                            postRequirementModel.setPreferred(jsonObjectMyEnquiryList.getString("title"));
                            postRequirementModel.setType_of_buyer(jsonObjectMyEnquiryList.getString("type"));
                            postRequirementModel.setAttributes_type_name(jsonObjectMyEnquiryList.getString("unit"));
                            postRequirementModel.setPaid_status(jsonObjectMyEnquiryList.getString("paid_status"));
                            postRequirementModel.setWaiting_for_response(jsonObjectMyEnquiryList.getString("waiting_for_response"));
                            try {
                                postRequirementModel.setContactDetailsArray(String.valueOf(jsonObjectMyEnquiryList.getJSONArray("contactlist")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                            //                            postRequirementModel.setPaid_status(jsonObjectMyEnquiryList.getString("paid_statuss"));
                       /*     postRequirementModel.setUserName(jsonObjectMyEnquiryList.getString("userName"));
                            postRequirementModel.setUA_email(jsonObjectMyEnquiryList.getString("UA_email"));
                            postRequirementModel.setUA_mobile(jsonObjectMyEnquiryList.getString("UA_mobile"));
                            postRequirementModel.setSeller_fkid(jsonObjectMyEnquiryList.getString("fk_to_id"));*/

                            mPostRequirementModelArrayList.add(postRequirementModel);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterPostRequirementList.notifyDataSetChanged();

                    try {
                        if (mPostRequirementModelArrayList.size() == 0) {
                            textViewErrorMessage.setVisibility(View.VISIBLE);
                            recyclerViewPostReq.setVisibility(GONE);
                        } else {
                            textViewErrorMessage.setVisibility(View.GONE);
                            recyclerViewPostReq.setVisibility(VISIBLE);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                } else {

                    params.put("uid", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }

                params.put("limit", "20");
                params.put("offset", String.valueOf(OFFSET));

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

    @OnClick(R.id.linear_layout_new_post)
    public void onViewClicked() {

        PostRequirementSearchFragment postRequirementSearchFragment = new PostRequirementSearchFragment();
        Constants.mMainActivity.ChangeFragments(postRequirementSearchFragment, "PostRequirementSearchFragment");

    }
}
