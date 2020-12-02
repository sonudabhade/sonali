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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterFavoritesList;
import com.tradegenie.android.tradegenieapp.Models.FavoritesListModel;
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

public class FavouritesFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_favourites)
    RecyclerView recyclerViewFavourites;
    @BindView(R.id.ProgressBarFavouritesList)
    RelativeLayout ProgressBarFavouritesList;

    public LinearLayoutManager linearLayoutManager;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;

    private SessionManager mSessionManager;

    private ArrayList<FavoritesListModel> mFavoritesListModelArrayList;
    private AdapterFavoritesList mAdapterFavoritesList;

    int OFFSET = 0;
    //ProgressDialog progressBar;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.my_favourite));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        Constants.mMainActivity.setNotificationButton(GONE);
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

        mFavoritesListModelArrayList = new ArrayList<>();
        mAdapterFavoritesList = new AdapterFavoritesList(getActivity(), mFavoritesListModelArrayList, FavouritesFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewFavourites.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewFavourites.setAdapter(mAdapterFavoritesList);
        recyclerViewFavourites.setLayoutManager(linearLayoutManager);
        if (InternetConnection.isInternetAvailable(getActivity())) {
            OFFSET = 0;
            UtilityMethods.tuchOff(ProgressBarFavouritesList);
            getFavoritesList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        recyclerViewFavourites.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Log.e("size", "size " + mFavoritesListModelArrayList.size());

                            OFFSET = OFFSET + 10;

                            if (OFFSET >= mFavoritesListModelArrayList.size()) {
                                //progressBar = new ProgressDialog(getActivity());
                                //progressBar.setMessage("Please wait");
                                //progressBar.setCancelable(false);
                                // progressBar.show();

                                getFavoritesList();
                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }

                }


            }
        });

        return view;
    }




    //Method to get Favorites List
    public void getFavoritesList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FAVOURITES_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getFavoritesList -->>" + response);

                loading = true;
                try {
                    // progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (OFFSET == 0) {
                    mFavoritesListModelArrayList.clear();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String upload_path=jsonObject.getString("upload_path");

                        JSONArray jsonArrayFavouritelist = jsonObject.getJSONArray("favouritelist");

                        for (int i = 0; i < jsonArrayFavouritelist.length(); i++) {

                            JSONObject jsonObjectFavouritelist = jsonArrayFavouritelist.getJSONObject(i);


                            FavoritesListModel favoritesListModel = new FavoritesListModel();

                            favoritesListModel.setCountry_name(jsonObjectFavouritelist.getString("country_name"));
                            favoritesListModel.setPk_id(jsonObjectFavouritelist.getString("pk_id"));
                            favoritesListModel.setProduct_name(jsonObjectFavouritelist.getString("product_name"));
                            favoritesListModel.setCost(jsonObjectFavouritelist.getString("cost"));
                            favoritesListModel.setUpload_brochure(jsonObjectFavouritelist.getString("upload_brochure"));
                            favoritesListModel.setComp_name(jsonObjectFavouritelist.getString("comp_name"));
                            favoritesListModel.setAddress(jsonObjectFavouritelist.getString("address"));
                            favoritesListModel.setPincode(jsonObjectFavouritelist.getString("pincode"));
                            favoritesListModel.setCurrency(jsonObjectFavouritelist.getString("currency"));
                            favoritesListModel.setImage_name(upload_path+""+jsonObjectFavouritelist.getString("image_name"));
                            favoritesListModel.setFavorite("1");
                            mFavoritesListModelArrayList.add(favoritesListModel);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterFavoritesList.notifyDataSetChanged();

                    try {
                        if (mFavoritesListModelArrayList.size() == 0) {
                            textViewErrorMessage.setVisibility(VISIBLE);
                        } else {
                            textViewErrorMessage.setVisibility(GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarFavouritesList);


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
                UtilityMethods.tuchOn(ProgressBarFavouritesList);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")){

                    params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                }else {

                    params.put("uid", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }

                // params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
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

    //Method to Call Set Favourite
    public void CallSetFavourite(final String productid) {

        //UtilityMethods.tuchOff(relativeLayoutProductView);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FEVORITE_SUBMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallSetFavourite" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (InternetConnection.isInternetAvailable(getActivity())) {
                            OFFSET = 0;
                            UtilityMethods.tuchOff(ProgressBarFavouritesList);
                            getFavoritesList();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

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

                // UtilityMethods.tuchOn(relativeLayoutProductView);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(getActivity(), getActivity().getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                // UtilityMethods.tuchOn(relativeLayoutProductView);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("productid", productid);
                params.put("utype", mSessionManager.getStringData(Constants.KEY_USER_TYPE));

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
}
