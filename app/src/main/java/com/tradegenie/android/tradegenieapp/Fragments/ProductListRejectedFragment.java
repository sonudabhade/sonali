package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterProductListRejected;
import com.tradegenie.android.tradegenieapp.Models.ProductListRejectedModel;
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

public class ProductListRejectedFragment extends Fragment {

    @BindView(R.id.recycler_view_product_list_rejected)
    RecyclerView recyclerViewProductListRejected;
    Unbinder unbinder;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.relativeLayoutProductListRejected)
    RelativeLayout relativeLayoutProductListRejected;

    private SessionManager mSessionManager;

    private ArrayList<ProductListRejectedModel> mProductListRejectedModelArrayList;
    private AdapterProductListRejected mAdapterProductListRejected;
    public LinearLayoutManager linearLayoutManager;

    int OFFSET = 0;
    //ProgressDialog progressBar;


    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    public ProductListRejectedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list_rejected, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Product List");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.OpenProductListSetIcon();
        Constants.mMainActivity.setToolBarPostEnquiryHide();

        mProductListRejectedModelArrayList = new ArrayList<>();
        mAdapterProductListRejected = new AdapterProductListRejected(getActivity(), mProductListRejectedModelArrayList, ProductListRejectedFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewProductListRejected.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewProductListRejected.setAdapter(mAdapterProductListRejected);
        recyclerViewProductListRejected.setLayoutManager(linearLayoutManager);
        mSessionManager = new SessionManager(getActivity());

        if (InternetConnection.isInternetAvailable(getActivity())) {
            OFFSET = 0;
            UtilityMethods.tuchOff(relativeLayoutProductListRejected);
            getProductListRejected();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        recyclerViewProductListRejected.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Log.e("size", "size " + mProductListRejectedModelArrayList.size());

                            OFFSET = OFFSET + 20;

                            if (OFFSET >= mProductListRejectedModelArrayList.size()) {
                                //progressBar = new ProgressDialog(getActivity());
                                //progressBar.setMessage("Please wait");
                                //progressBar.setCancelable(false);
                                //progressBar.show();

                                getProductListRejected();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            try {
                Constants.mMainActivity.productListRejectedFragment.getProductListRejected();
                Constants.mMainActivity.WhichFragmentIsopen = "Fragment Rejected";
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    //Method to get get ProductList Rejected
    private void getProductListRejected() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PRODUCT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getProductListRejected -->>" + response);

                loading = true;
                try {
                    //progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (OFFSET == 0) {
                    mProductListRejectedModelArrayList.clear();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        String product_image = jsonObject.getString("product_image");

                        JSONArray jsonArrayProductdata = jsonObject.getJSONArray("productdata");

                        for (int i = 0; i < jsonArrayProductdata.length(); i++) {

                            JSONObject jsonObjectProductdata = jsonArrayProductdata.getJSONObject(i);

                            String pk_id = jsonObjectProductdata.getString("pk_id");
                            String currency = jsonObjectProductdata.getString("currency");
                            String product_name = jsonObjectProductdata.getString("product_name");
                            String cost = jsonObjectProductdata.getString("cost");
                            String cat_name = jsonObjectProductdata.getString("cat_name");
                            String subcat_name = jsonObjectProductdata.getString("subcat_name");
                            String image_name = jsonObjectProductdata.getString("image_name");
                            String cancel_remark = jsonObjectProductdata.getString("cancel_remark");
                            ProductListRejectedModel productListRejectedModel = new ProductListRejectedModel(pk_id, currency, product_name, cost, cat_name, subcat_name, product_image + image_name, cancel_remark);
                            mProductListRejectedModelArrayList.add(productListRejectedModel);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterProductListRejected.notifyDataSetChanged();

                    try {
                        if (mProductListRejectedModelArrayList.size() == 0) {
                            textViewErrorMessage.setVisibility(View.VISIBLE);
                        } else {
                            textViewErrorMessage.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutProductListRejected);


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
                UtilityMethods.tuchOn(relativeLayoutProductListRejected);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("product_status", "5");
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

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to Delete Product
    public void DeleteProduct(final String product_id) {

        UtilityMethods.tuchOff(relativeLayoutProductListRejected);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PRODUCT_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "DeleteProduct -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            UtilityMethods.tuchOff(relativeLayoutProductListRejected);
                            OFFSET = 0;
                            getProductListRejected();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }
                        UtilityMethods.showSuccessToast(getActivity(), "Product deleted successfully");


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                        UtilityMethods.showInfoToast(getActivity(), "Products deleted failed, please try again.");

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutProductListRejected);


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
                UtilityMethods.tuchOn(relativeLayoutProductListRejected);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("product_id", product_id);


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

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
