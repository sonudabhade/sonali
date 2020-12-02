package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSellerTranactionList;
import com.tradegenie.android.tradegenieapp.Models.SellerTransactionModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SellerTransactionListFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_transaction_list)
    RecyclerView recyclerViewTransactionList;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressBarTransactionList)
    RelativeLayout ProgressBarTransactionList;
    @BindView(R.id.linear_start_date)
    LinearLayout linearStartDate;
    @BindView(R.id.linear_end_date)
    LinearLayout linearEndDate;
    @BindView(R.id.rel_filter)
    RelativeLayout relFilter;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;

    int from_day, from_month, from_year;


    private SessionManager mSessionManager;
    private ArrayList<SellerTransactionModel> mSellerTransactionModelArrayList;
    private AdapterSellerTranactionList mAdapterSellerTranactionList;

    private LinearLayoutManager linearLayoutManager;

    int OFFSET = 0;
    //ProgressDialog progressBar;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    public SellerTransactionListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_transaction_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.payment_history));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();

        //Check personal details and business details
        if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")) {

            UtilityMethods.showInfoToast(getActivity(), "Please complete your profile details.");


            /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
            Constants.mMainActivity.ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");*/

            Log.e("check==========>", "PersonalDetailsFragment");

        } else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")) {

            UtilityMethods.showInfoToast(getActivity(), "Please complete your business details first");


            BusinessDetailsFragment businessDetailsFragment = new BusinessDetailsFragment();
            Constants.mMainActivity.ChangeFragments(businessDetailsFragment, "BusinessDetailsFragment");

            Log.e("check==========>", "BusinessDetailsFragment");

        }


        mSellerTransactionModelArrayList = new ArrayList<>();
        mAdapterSellerTranactionList = new AdapterSellerTranactionList(getActivity(), mSellerTransactionModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewTransactionList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTransactionList.setAdapter(mAdapterSellerTranactionList);
        recyclerViewTransactionList.setLayoutManager(linearLayoutManager);

        if (InternetConnection.isInternetAvailable(getActivity())) {
            OFFSET = 0;
            UtilityMethods.tuchOff(ProgressBarTransactionList);
            getTransactionList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        recyclerViewTransactionList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Log.e("size", "size " + mSellerTransactionModelArrayList.size());

                            OFFSET = OFFSET + 10;

                            if (OFFSET >= mSellerTransactionModelArrayList.size()) {
                                //progressBar = new ProgressDialog(getActivity());
                                //progressBar.setMessage("Please wait");
                                //progressBar.setCancelable(false);
                                // progressBar.show();

                                getTransactionList();
                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }

                }


            }
        });


        return view;
    }


    //Method to get Transaction List
    private void getTransactionList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_PAYMENT_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getTransactionList -->>" + response);

                loading = true;
                try {
                    // progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (OFFSET == 0) {
                    mSellerTransactionModelArrayList.clear();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArrayTransactionHistory = jsonObject.getJSONArray("payment_date");

                        for (int i = 0; i < jsonArrayTransactionHistory.length(); i++) {

                            JSONObject jsonObjectTransactionHistory = jsonArrayTransactionHistory.getJSONObject(i);

                            SellerTransactionModel sellerTransactionModel = new SellerTransactionModel();


                            sellerTransactionModel.setLead_subscription(jsonObjectTransactionHistory.getString("lead_subscription"));
                            sellerTransactionModel.setGrand_total(jsonObjectTransactionHistory.getString("grand_total"));
                            sellerTransactionModel.setTransactionid(jsonObjectTransactionHistory.getString("transactionid"));
                            sellerTransactionModel.setAmount(jsonObjectTransactionHistory.getString("amount"));
                            sellerTransactionModel.setCreated_date(jsonObjectTransactionHistory.getString("created_date"));
                            sellerTransactionModel.setPk_id(jsonObjectTransactionHistory.getString("pk_id"));
                            sellerTransactionModel.setTransactionid(jsonObjectTransactionHistory.getString("transactionid"));
                            sellerTransactionModel.setCurrency(jsonObjectTransactionHistory.getString("currency"));
                            sellerTransactionModel.setPaymentType(jsonObjectTransactionHistory.getString("pay_type_status"));

                            mSellerTransactionModelArrayList.add(sellerTransactionModel);


                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        //UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterSellerTranactionList.notifyDataSetChanged();

                    if (mSellerTransactionModelArrayList.size() == 0) {

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerViewTransactionList.setVisibility(GONE);

                    } else {

                        textViewErrorMessage.setVisibility(GONE);
                        recyclerViewTransactionList.setVisibility(VISIBLE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarTransactionList);


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
                UtilityMethods.tuchOn(ProgressBarTransactionList);

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

                params.put("limit", "20");
                params.put("offset", String.valueOf(OFFSET));

                if (tvStartDate.getText().toString().equals("From Date")) {


                    params.remove("fromdate");


                } else {


                    params.put("fromdate", tvStartDate.getText().toString());

                }

                if (tvEndDate.getText().toString().equals("To Date")) {


                    params.remove("todate");


                } else {


                    params.put("todate", tvEndDate.getText().toString());


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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.linear_start_date, R.id.linear_end_date, R.id.rel_filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_start_date:

                FromDate();


                break;
            case R.id.linear_end_date:

                ToDate();

                break;
            case R.id.rel_filter:


                if (InternetConnection.isInternetAvailable(getActivity())) {
                    OFFSET = 0;
                    UtilityMethods.tuchOff(ProgressBarTransactionList);
                    getTransactionList();


                } else {

                    UtilityMethods.showInternetDialog(getActivity());

                }

                break;
        }
    }

    //method for call Calender Dialog with different style
    public void FromDate() {

        // Get Current Date
        final Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                currentDate.set(year, monthOfYear, dayOfMonth);


                String day = String.valueOf(dayOfMonth);
                String month = String.valueOf(monthOfYear);


                from_day = dayOfMonth;
                from_month = Integer.parseInt(month) + 1;
                from_year = year;

                tvStartDate.setText(day + "-" + String.valueOf(Integer.parseInt(month) + 1) + "-" + year);


            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    //method for call Calender Dialog with different style
    public void ToDate() {

        Log.e("check", "Date" + from_year + " " + from_month + " " + from_day);


        // Get Current Date
        final Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                currentDate.set(year, monthOfYear, dayOfMonth);

                String day = String.valueOf(dayOfMonth);
                String month = String.valueOf(monthOfYear);


                tvEndDate.setText(day + "-" + String.valueOf(Integer.parseInt(month) + 1) + "-" + year);


            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }
}
