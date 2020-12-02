package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.tradegenie.android.tradegenieapp.Activity.MainActivity;
import com.tradegenie.android.tradegenieapp.Models.SearchPostRequirementModel;
import com.tradegenie.android.tradegenieapp.Models.SearchSubCatArrayModel;
import com.tradegenie.android.tradegenieapp.Models.SelectedProductModel;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.android.volley.VolleyLog.TAG;

public class PostRequirementSearchFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.iv_product_search)
    ImageView ivProductSearch;
    @BindView(R.id.ProgressbarPostRequirement)
    RelativeLayout ProgressbarPostRequirement;
    @BindView(R.id.linearLayoutProductList)
    LinearLayout linearLayoutProductList;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.linear_layout_pay_now)
    LinearLayout linearLayoutPayNow;
    @BindView(R.id.linear_layout_pay_now_duplicate)
    LinearLayout linearLayoutPayNowDuplicate;

    private ArrayList<SearchPostRequirementModel> mSearchPostRequirementModelArrayList;
    private ArrayList<SelectedProductModel> mSelectedProductModelArrayList;

    private SessionManager mSessionManager;

//    String BeforeSerach = "";
    boolean isTyping = false;
    private Timer timer = new Timer();
    private final long DELAY = 1000; // milliseconds

    public PostRequirementSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_requirement_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Post New Requirement");
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        mSearchPostRequirementModelArrayList = new ArrayList<>();
        mSelectedProductModelArrayList = new ArrayList<>();

        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            //Check personal details and business details
            if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")) {

                /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                Constants.mMainActivity.ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");*/


            } else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")) {


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


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                /*if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                {

                    searchKeyword();

                } else {

                    UtilityMethods.showInternetDialog(getActivity());
                }
*/
                try{
                    if (!isTyping) {
                        Log.d(TAG, "started typing");
                        // Send notification for start typing event
                        isTyping = true;
                    }
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    isTyping = false;
                                    Log.d(TAG, "stopped typing");
                                    timer.cancel();


                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {


                                            try {
//                                                if (!BeforeSerach.equals(edtSearch.getText().toString().trim())) {
                                                    if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                                                    {
                                                        //                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                        //                            requestQueue.cancelAll("PRODUCT_DETAILS");
                                                        searchKeyword();
                                                    } else {
                                                        UtilityMethods.showInternetDialog(getActivity());
                                                    }

//                                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


//                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }
                            },
                            DELAY);
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            searchKeyword();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }


    //Method to search Keyword
    private void searchKeyword() {

        UtilityMethods.tuchOff(ProgressbarPostRequirement);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_POST_REQ_PRODUCT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "searchKeyword -->>" + response);

                try {
                    mSearchPostRequirementModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraySearchresult = jsonObject.getJSONArray("search_result");

                        for (int i = 0; i < jsonArraySearchresult.length(); i++) {

                            JSONObject jsonObjectSearchResult = jsonArraySearchresult.getJSONObject(i);

                            SearchPostRequirementModel searchPostRequirementModel = new SearchPostRequirementModel();

                            searchPostRequirementModel.setPk_id(jsonObjectSearchResult.getString("pk_id"));
                            searchPostRequirementModel.setCat_name(jsonObjectSearchResult.getString("cat_name"));

                            JSONArray jsonArraySubcatarray = jsonObjectSearchResult.getJSONArray("subcatarray");

                            ArrayList<SearchSubCatArrayModel> mSearchSubCatArrayModelArrayList = new ArrayList<>();
                            for (int j = 0; j < jsonArraySubcatarray.length(); j++) {

                                JSONObject jsonObjectSubcatarray = jsonArraySubcatarray.getJSONObject(j);

                                SearchSubCatArrayModel searchSubCatArrayModel = new SearchSubCatArrayModel();

                                searchSubCatArrayModel.setPk_id(jsonObjectSearchResult.getString("pk_id"));
                                searchSubCatArrayModel.setSub_cat_pkid(jsonObjectSubcatarray.getString("sub_cat_pkid"));
                                searchSubCatArrayModel.setSub_catname(jsonObjectSubcatarray.getString("sub_catname"));
                                searchSubCatArrayModel.setSelected(false);

                                mSearchSubCatArrayModelArrayList.add(searchSubCatArrayModel);

                            }

                            searchPostRequirementModel.setmSearchSubCatArrayModelArrayList(mSearchSubCatArrayModelArrayList);


                            mSearchPostRequirementModelArrayList.add(searchPostRequirementModel);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    Log.e("check", "mSearchPostRequirementModelArrayList" + mSearchPostRequirementModelArrayList.size());

                    if (mSearchPostRequirementModelArrayList.size() == 0) {

                        scrollview.setVisibility(GONE);
                        textViewErrorMessage.setVisibility(VISIBLE);


                    } else {

                        scrollview.setVisibility(VISIBLE);
                        textViewErrorMessage.setVisibility(GONE);
                        linearLayoutProductList.removeAllViews();
                        ShowListOfSearchProduct();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressbarPostRequirement);


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
                UtilityMethods.tuchOn(ProgressbarPostRequirement);

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

                params.put("search", edtSearch.getText().toString());

                Log.e("check", "params" + params.toString());

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

    private void ShowListOfSearchProduct() {


        //Display list view runtime inflate
        for (int i = 0; i < mSearchPostRequirementModelArrayList.size(); i++) {

            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.adapter_search_product, null);


            //linearLayoutProductList.addView(view);


            TextView tv_category_name = view.findViewById(R.id.tv_category_name);
            LinearLayout linearLayoutsubcatergory = view.findViewById(R.id.linearLayoutsubcatergory);


            tv_category_name.setText(mSearchPostRequirementModelArrayList.get(i).getCat_name());

            if (mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().size() != 0) {

                linearLayoutProductList.addView(view);

                for (int j = 0; j < mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().size(); j++) {

                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View subview = layoutInflater.inflate(R.layout.adapter_post_req_sub_category, null);

                    linearLayoutsubcatergory.addView(subview);


                    final CheckBox check_box_sub_category = subview.findViewById(R.id.check_box_sub_category);
                    TextView tv_sub_category_name = subview.findViewById(R.id.tv_sub_category_name);


                /*if (mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().size()!=0){

                    linearLayoutProductList.addView(view);

                }*/

                    tv_sub_category_name.setText(mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().get(j).getSub_catname());
                    final int finalI = i;
                    final int finalJ = j;

                    check_box_sub_category.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                            if (isChecked) {

                                mSearchPostRequirementModelArrayList.get(finalI).getmSearchSubCatArrayModelArrayList().get(finalJ).setSelected(true);

                            } else {


                                mSearchPostRequirementModelArrayList.get(finalI).getmSearchSubCatArrayModelArrayList().get(finalJ).setSelected(false);

                            }

                            mSelectedProductModelArrayList.clear();

                            for (int i = 0; i < mSearchPostRequirementModelArrayList.size(); i++) {

                                for (int j = 0; j < mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().size(); j++) {


                                    if (mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().get(j).getSelected().equals(true)) {

                                        SelectedProductModel selectedProductModel = new SelectedProductModel();


                                        selectedProductModel.setPk_id((mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().get(j).getPk_id()));
                                        selectedProductModel.setSub_cat_pkid((mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().get(j).getSub_cat_pkid()));
                                        selectedProductModel.setSub_catname((mSearchPostRequirementModelArrayList.get(i).getmSearchSubCatArrayModelArrayList().get(j).getSub_catname()));

                                        mSelectedProductModelArrayList.add(selectedProductModel);


                                    }


                                }

                            }

                            Log.e("check", "mSelectedProductModelArrayList" + mSelectedProductModelArrayList.size());


                        }
                    });

                }


            } else {


            }
        }

    }


    @OnClick({R.id.iv_product_search, R.id.linear_layout_pay_now, R.id.linear_layout_pay_now_duplicate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_product_search:
                if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                {

                    try {
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    searchKeyword();

                } else {

                    UtilityMethods.showInternetDialog(getActivity());
                }
                break;
            case R.id.linear_layout_pay_now:

                if (mSelectedProductModelArrayList.size() == 0) {

                    UtilityMethods.showInfoToast(getActivity(), "Requirement not found.");

                } else {

                    PostRequirementDetailsFragment postRequirementDetailsFragment = new PostRequirementDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mSelectedProductModelArrayList", mSelectedProductModelArrayList);
                    postRequirementDetailsFragment.setArguments(bundle);
                    Constants.mMainActivity.ChangeFragments(postRequirementDetailsFragment, "PostRequirementDetailsFragment");
                }
                break;
         case R.id.linear_layout_pay_now_duplicate:

                if (mSelectedProductModelArrayList.size() == 0) {

                    UtilityMethods.showInfoToast(getActivity(), "Requirement not found.");

                } else {

                    PostRequirementDetailsFragment postRequirementDetailsFragment = new PostRequirementDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mSelectedProductModelArrayList", mSelectedProductModelArrayList);
                    postRequirementDetailsFragment.setArguments(bundle);
                    Constants.mMainActivity.ChangeFragments(postRequirementDetailsFragment, "PostRequirementDetailsFragment");
                }
                break;
        }
    }
}
