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
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterTenderList;
import com.tradegenie.android.tradegenieapp.Models.ProductImageViewModel;
import com.tradegenie.android.tradegenieapp.Models.ProductSpecificationModel;
import com.tradegenie.android.tradegenieapp.Models.TenderModel;
import com.tradegenie.android.tradegenieapp.Models.ViewPagerProductImageModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TenderListAddFragment extends Fragment {


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

    public TenderListAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tender_list_add, container, false);
        unbinder = ButterKnife.bind(this, view);

        Constants.mMainActivity.setToolBarName(getString(R.string.lbl_tender));

        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mTenderArrayList = new ArrayList<>();



        mAdapterTenderList = new AdapterTenderList(getActivity(), mTenderArrayList,"MyPostedTenders");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPostReq.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewPostReq.setAdapter(mAdapterTenderList);
        recyclerViewPostReq.setLayoutManager(linearLayoutManager);

        if (InternetConnection.isInternetAvailable(getActivity())) {
            UtilityMethods.tuchOff(ProgressBarPostEnqList);

            GetTenderList();
        } else {
            UtilityMethods.showInternetDialog(getActivity());
        }
        return view;
    }


    //MEthod to get tender List
    private void GetTenderList() {

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UtilityMethods.tuchOff(ProgressBarPostEnqList);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MY_POST_TENDER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "URL_MY_POST_TENDER_LIST" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray finalsResult = jsonObject.getJSONArray("finalsResult");

                        for (int i = 0; i < finalsResult.length(); i++) {

                            JSONObject tenderObj = finalsResult.getJSONObject(i);
                            TenderModel tenderModel = new TenderModel();
                            tenderModel.setPk_id(tenderObj.getString("pk_id"));
                            tenderModel.setFk_user_id(tenderObj.getString("fk_user_id"));
                            tenderModel.setCurrency(tenderObj.getString("currency"));
                            tenderModel.setTender_name(tenderObj.getString("tender_name"));
                            tenderModel.setDescripation(tenderObj.getString("descripation"));
                            tenderModel.setTender_link(tenderObj.getString("tender_link"));
                            tenderModel.setCost(tenderObj.getString("cost"));
                            tenderModel.setPaid_status(tenderObj.getString("paid_status"));
                            tenderModel.setCreated_date(tenderObj.getString("created_date"));

                            mTenderArrayList.add(tenderModel);
                        }

                        mAdapterTenderList.notifyDataSetChanged();
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
                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(getActivity(), getString(R.string.network_error));
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

                Log.e("check", "=======>" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

// stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
