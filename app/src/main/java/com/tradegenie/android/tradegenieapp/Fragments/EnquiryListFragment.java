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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterEnquiryList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterMyLead;
import com.tradegenie.android.tradegenieapp.Models.EnquiryListModel;
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

public class EnquiryListFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_enquiry_list)
    RecyclerView recyclerViewEnquiryList;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressBarEnquiryList)
    RelativeLayout ProgressBarEnquiryList;

    private SessionManager mSessionManager;
    private ArrayList<EnquiryListModel> mEnquiryListModelArrayList;
    private AdapterEnquiryList mAdapterEnquiryList;

    private LinearLayoutManager linearLayoutManager;

    int OFFSET = 0;
    //ProgressDialog progressBar;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    public EnquiryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enquiry_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.my_enquiries));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());


        //Check personal details and business details
        if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("")||mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")){

            //UtilityMethods.showInfoToast(getActivity(),"Please complete your profile details.");


            /*PersonalDetailsFragment personalDetailsFragment=new PersonalDetailsFragment();
            Constants.mMainActivity.ChangeFragments(personalDetailsFragment,"PersonalDetailsFragment");*/

            Log.e("check==========>","PersonalDetailsFragment");

        }else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_NAME).equals("")||mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")){

            UtilityMethods.showInfoToast(getActivity(),"Please complete your business details first");


            BusinessDetailsFragment businessDetailsFragment=new BusinessDetailsFragment();
            Constants.mMainActivity.ChangeFragments(businessDetailsFragment,"BusinessDetailsFragment");

            Log.e("check==========>","BusinessDetailsFragment");

        }


        mEnquiryListModelArrayList = new ArrayList<>();
        mAdapterEnquiryList = new AdapterEnquiryList(getActivity(), mEnquiryListModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewEnquiryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewEnquiryList.setAdapter(mAdapterEnquiryList);
        recyclerViewEnquiryList.setLayoutManager(linearLayoutManager);

        if (InternetConnection.isInternetAvailable(getActivity())) {
            OFFSET = 0;
            UtilityMethods.tuchOff(ProgressBarEnquiryList);
            getEnquiryList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        recyclerViewEnquiryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Log.e("size", "size " + mEnquiryListModelArrayList.size());

                            OFFSET = OFFSET + 10;

                            if (OFFSET >= mEnquiryListModelArrayList.size()) {
                                //progressBar = new ProgressDialog(getActivity());
                                //progressBar.setMessage("Please wait");
                                //progressBar.setCancelable(false);
                                // progressBar.show();

                                getEnquiryList();
                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }

                }


            }
        });


        return view;
    }


    //Method to get Enquiry List
    private void getEnquiryList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ENQUIRY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getEnquiryList Seller-->>" + response);

                loading = true;
                try {
                    // progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (OFFSET == 0) {
                    mEnquiryListModelArrayList.clear();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArrayMyenquiry = jsonObject.getJSONArray("myenquiry");

                        for (int i = 0; i < jsonArrayMyenquiry.length(); i++) {

                            JSONObject jsonObjectMyenquiry = jsonArrayMyenquiry.getJSONObject(i);

                            EnquiryListModel enquiryListModel = new EnquiryListModel();

                            enquiryListModel.setPk_id(jsonObjectMyenquiry.getString("pk_id"));
                            enquiryListModel.setProduct_name(jsonObjectMyenquiry.getString("product_name"));
                            if (jsonObjectMyenquiry.isNull("qty")){
                                enquiryListModel.setQty("-");
                            }else {
                                enquiryListModel.setQty(jsonObjectMyenquiry.getString("qty"));
                            }
                            enquiryListModel.setPrice_from(jsonObjectMyenquiry.getString("price_from"));
                            enquiryListModel.setPrice_to(jsonObjectMyenquiry.getString("price_to"));
                            enquiryListModel.setCreated_date(jsonObjectMyenquiry.getString("created_date"));
                            enquiryListModel.setCurrency(jsonObjectMyenquiry.getString("currency"));
                            enquiryListModel.setDescripation(jsonObjectMyenquiry.getString("descripation"));
                            enquiryListModel.setLead_id(jsonObjectMyenquiry.getString("lead_id"));
                            if (jsonObjectMyenquiry.isNull("title")){
                                enquiryListModel.setPreferred("-");
                            }else {
                                enquiryListModel.setPreferred(jsonObjectMyenquiry.getString("title"));
                            }
                            if (jsonObjectMyenquiry.isNull("type")) {
                                enquiryListModel.setType_of_buyer("-");
                            }else {
                                enquiryListModel.setType_of_buyer(jsonObjectMyenquiry.getString("type"));
                            }
                            enquiryListModel.setPaid_status(jsonObjectMyenquiry.getString("paid_status"));
                            enquiryListModel.setUserName(jsonObjectMyenquiry.getString("userName"));
                            enquiryListModel.setUA_pkey(jsonObjectMyenquiry.getString("UA_pkey"));
                            enquiryListModel.setUA_email(jsonObjectMyenquiry.getString("UA_email"));
                            enquiryListModel.setUA_mobile(jsonObjectMyenquiry.getString("UA_mobile"));
                            enquiryListModel.setSeller_fkid(jsonObjectMyenquiry.getString("seller_fkid"));
                            if (jsonObjectMyenquiry.isNull("attributes_type_name")){
                                enquiryListModel.setAttributes_type_name("-");
                            }else {
                                enquiryListModel.setAttributes_type_name(jsonObjectMyenquiry.getString("attributes_type_name"));
                            }

                            mEnquiryListModelArrayList.add(enquiryListModel);


                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterEnquiryList.notifyDataSetChanged();

                    if (mEnquiryListModelArrayList.size()==0){

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerViewEnquiryList.setVisibility(GONE);

                    }else {

                        textViewErrorMessage.setVisibility(GONE);
                        recyclerViewEnquiryList.setVisibility(VISIBLE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarEnquiryList);


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
                UtilityMethods.tuchOn(ProgressBarEnquiryList);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
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
}
