package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCurrencyList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterPreferredList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterTypeOfBuyerList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterUnitList;
import com.tradegenie.android.tradegenieapp.Models.CurrencyModel;
import com.tradegenie.android.tradegenieapp.Models.PreferredModel;
import com.tradegenie.android.tradegenieapp.Models.TypeOfBuyerModel;
import com.tradegenie.android.tradegenieapp.Models.UnitModel;
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

public class EnquiryFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.btn_send_enquiry)
    Button btnSendEnquiry;
    @BindView(R.id.rel_unit)
    RelativeLayout relUnit;
    @BindView(R.id.rel_preferred)
    RelativeLayout relPreferred;
    @BindView(R.id.rel_buyer)
    RelativeLayout relBuyer;
    @BindView(R.id.spinner_unit)
    Spinner spinnerUnit;
    @BindView(R.id.spinner_preferred)
    Spinner spinnerPreferred;
    @BindView(R.id.spinner_buyer)
    Spinner spinnerBuyer;
    String productid;
    @BindView(R.id.edt_quantity)
    EditText edtQuantity;
    @BindView(R.id.edt_from_price)
    EditText edtFromPrice;
    @BindView(R.id.edt_to_price)
    EditText edtToPrice;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.ProgressBarEnquiry)
    RelativeLayout ProgressBarEnquiry;
    @BindView(R.id.iv_product_image)
    ImageView ivProductImage;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.iv_view_spinner_unit)
    View ivViewSpinnerUnit;
    @BindView(R.id.iv_drop_down_unit)
    ImageView ivDropDownUnit;
    @BindView(R.id.spinner_currency)
    Spinner spinnerCurrency;
    @BindView(R.id.rel_currency)
    RelativeLayout relCurrency;
    @BindView(R.id.rel_location)
    RelativeLayout relLocation;
    @BindView(R.id.iv_view_spinner_currency)
    View ivViewSpinnerCurrency;
    @BindView(R.id.iv_drop_down_currency)
    ImageView ivDropDownCurrency;
    @BindView(R.id.iv_view_spinner_preferred)
    View ivViewSpinnerPreferred;
    @BindView(R.id.iv_drop_down_preferred)
    ImageView ivDropDownPreferred;
    @BindView(R.id.iv_view_spinner_buyer)
    View ivViewSpinnerBuyer;
    @BindView(R.id.iv_drop_down_buyer)
    ImageView ivDropDownBuyer;

    private SessionManager mSessionManager;


    private ArrayList<UnitModel> mUnitModelArrayList;
    private AdapterUnitList mAdapterUnitList;
    private String mSelectedUnit;


    private ArrayList<PreferredModel> mPreferredModelArrayList;
    private AdapterPreferredList mAdapterPreferredList;
    private String mSelectedPreferred;


    private ArrayList<TypeOfBuyerModel> mTypeOfBuyerModelArrayList;
    private AdapterTypeOfBuyerList mAdapterTypeOfBuyerList;
    private String mSelectedTypeOfBuyer;

    private ArrayList<CurrencyModel> mCurrencyModelArrayList;
    private AdapterCurrencyList mAdapterCurrencyList;
    private String mSelectedCurrency="";

    private String fk_seller_id = "";


    public EnquiryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enquiry, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.lbl_enquiry));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productid = bundle.getString("PK_id");

        }


        mUnitModelArrayList = new ArrayList<>();
        mUnitModelArrayList.add(new UnitModel("0", "Select"));
        mAdapterUnitList = new AdapterUnitList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mUnitModelArrayList);
        spinnerUnit.setAdapter(mAdapterUnitList);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mUnitModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedUnit = mUnitModelArrayList.get(i).getPk_id();

                } else {
                    mSelectedUnit = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mPreferredModelArrayList = new ArrayList<>();
        mPreferredModelArrayList.add(new PreferredModel("0", "Select"));
        mAdapterPreferredList = new AdapterPreferredList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mPreferredModelArrayList);
        spinnerPreferred.setAdapter(mAdapterPreferredList);
        spinnerPreferred.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mPreferredModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedPreferred = mPreferredModelArrayList.get(i).getPk_id();

                } else {
                    mSelectedPreferred = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mTypeOfBuyerModelArrayList = new ArrayList<>();
        mTypeOfBuyerModelArrayList.add(new TypeOfBuyerModel("0", "Select"));
        mAdapterTypeOfBuyerList = new AdapterTypeOfBuyerList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mTypeOfBuyerModelArrayList);
        spinnerBuyer.setAdapter(mAdapterTypeOfBuyerList);
        spinnerBuyer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mTypeOfBuyerModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedTypeOfBuyer = mTypeOfBuyerModelArrayList.get(i).getPk_id();

                } else {
                    mSelectedTypeOfBuyer = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mCurrencyModelArrayList = new ArrayList<>();
        mCurrencyModelArrayList.add(new CurrencyModel("0", "Select"));
        mCurrencyModelArrayList.add(new CurrencyModel("1", "INR"));
        mCurrencyModelArrayList.add(new CurrencyModel("2", "LKR"));
        mCurrencyModelArrayList.add(new CurrencyModel("2", "USD"));
        mAdapterCurrencyList = new AdapterCurrencyList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCurrencyModelArrayList);
        spinnerCurrency.setAdapter(mAdapterCurrencyList);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();


                if (!mCurrencyModelArrayList.get(i).getCurrency().equalsIgnoreCase("Select")) {
                    mSelectedCurrency = mCurrencyModelArrayList.get(i).getCurrency();

                } else {
                    mSelectedCurrency = "Select";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerUnit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });
        spinnerPreferred.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });
        spinnerBuyer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });
        spinnerCurrency.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoardHide();
                return false;
            }

        });

        relCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerCurrency.performClick();
                keyBoardHide();

            }
        });

        relUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                spinnerUnit.performClick();
                keyBoardHide();

            }
        });
        relPreferred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                spinnerPreferred.performClick();
                keyBoardHide();

            }
        });
        relBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                spinnerBuyer.performClick();
                keyBoardHide();

            }
        });


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {
            CallEnquiryDetails();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_send_enquiry)
    public void onViewClicked() {

        if (validation()) {
            if (InternetConnection.isInternetAvailable(getActivity())) {

                keyBoardHide();

                CallSendEnquiry();


            } else {

                UtilityMethods.showInternetDialog(getActivity());

            }
            Log.e("test", "Success---->>> ");
        } else {

            Log.e("test", "Failed---->>> ");
        }


    }

    public void keyBoardHide() {

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to Call Send Enquiry
    private void CallSendEnquiry() {

        UtilityMethods.tuchOff(ProgressBarEnquiry);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ENQUIRY_SUBMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallSendEnquiry -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        getActivity().getFragmentManager().popBackStack();


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = mInflater.inflate(R.layout.dialog_enquiry_popup, null);
                        builder.setView(dialogView);
                        builder.setCancelable(true);

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Window window = alertDialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();
                        wlp.gravity = Gravity.CENTER;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                        window.setAttributes(wlp);

                        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
                        alertDialog.show();

                        LinearLayout tv_chat_now = dialogView.findViewById(R.id.tv_chat_now);

                        tv_chat_now.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                ChatPersnoalFragment chatPersnoalFragment = new ChatPersnoalFragment();
                                Bundle bundle = new Bundle();
                                Log.e("check", "fk_seller_id" + fk_seller_id);
                                bundle.putString("Chat_With", fk_seller_id);
                                chatPersnoalFragment.setArguments(bundle);
                                Constants.mMainActivity.ChangeFragments(chatPersnoalFragment, "ChatPersnoalFragment");

                                alertDialog.dismiss();

                            }
                        });


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

                UtilityMethods.tuchOn(ProgressBarEnquiry);


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
                UtilityMethods.tuchOn(ProgressBarEnquiry);

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

                params.put("utype", mSessionManager.getStringData(Constants.KEY_USER_TYPE));
                params.put("pid", productid);
                params.put("qty", edtQuantity.getText().toString());
                params.put("unit", mSelectedUnit);
                params.put("currency", mSelectedCurrency);
                params.put("price_from", edtFromPrice.getText().toString());
                params.put("price_to", edtToPrice.getText().toString());
                params.put("preferred", mSelectedPreferred);
                params.put("type_of_buyer", mSelectedTypeOfBuyer);
                params.put("descripation", edtDescription.getText().toString());

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

    //Method to Call Enquiry Details
    private void CallEnquiryDetails() {

        UtilityMethods.tuchOff(ProgressBarEnquiry);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ENQUIRY_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallEnquiryDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String product_image = jsonObject.getString("product_image");

                        JSONArray jsonArrayProductdata = jsonObject.getJSONArray("productdata");

                        String image_name = "";
                        for (int i = 0; i < jsonArrayProductdata.length(); i++) {

                            JSONObject jsonObjectProductdata = jsonArrayProductdata.getJSONObject(i);

                            String pk_id = jsonObjectProductdata.getString("pk_id");
                            String cost = jsonObjectProductdata.getString("cost");
                            String currency = jsonObjectProductdata.getString("currency");
                            tvProductName.setText(jsonObjectProductdata.getString("product_name"));
                            String cat_name = jsonObjectProductdata.getString("cat_name");
                            String subcat_name = jsonObjectProductdata.getString("subcat_name");
                            String sub_subcat_name = jsonObjectProductdata.getString("sub_subcat_name");
                            image_name = product_image + jsonObjectProductdata.getString("image_name");
                            tvCompanyName.setText(jsonObjectProductdata.getString("comp_name"));
                            String established_year = jsonObjectProductdata.getString("established_year");
                            fk_seller_id = jsonObjectProductdata.getString("fk_seller_id");
                            // String busspincode = jsonObjectProductdata.getString("busspincode ");
                            // String mobile_no = jsonObjectProductdata.getString("mobile_no ");

                            if (jsonObjectProductdata.getString("bussaddress").equals("")) {

                                relLocation.setVisibility(GONE);

                            } else {


                                tvAddress.setText(jsonObjectProductdata.getString("bussaddress"));

                            }

                            if (cost.equals("") || cost.equals("0")) {

                                tvCost.setVisibility(GONE);

                            } else {

                                tvCost.setText(currency + " " + cost);

                            }

                        }


                        try {
                            Picasso.get().load(image_name).placeholder(R.drawable.default_document).into(ivProductImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonArrayPreferreddata = jsonObject.getJSONArray("preferreddata");

                        mPreferredModelArrayList.clear();
                        mPreferredModelArrayList.add(new PreferredModel("0", "Select"));
                        for (int i = 0; i < jsonArrayPreferreddata.length(); i++) {


                            JSONObject jsonObjectPreferreddata = jsonArrayPreferreddata.getJSONObject(i);

                            String pk_id = jsonObjectPreferreddata.getString("pk_id");
                            String title = jsonObjectPreferreddata.getString("title");
                            String status = jsonObjectPreferreddata.getString("status");

                            PreferredModel preferredModel = new PreferredModel(pk_id, title);
                            mPreferredModelArrayList.add(preferredModel);

                        }

                        JSONArray jsonArraySalesType = jsonObject.getJSONArray("salesType");

                        mTypeOfBuyerModelArrayList.clear();
                        mTypeOfBuyerModelArrayList.add(new TypeOfBuyerModel("0", "Select"));
                        for (int i = 0; i < jsonArraySalesType.length(); i++) {


                            JSONObject jsonObjectSalesType = jsonArraySalesType.getJSONObject(i);

                            String pk_id = jsonObjectSalesType.getString("pk_id");
                            String type = jsonObjectSalesType.getString("type");

                            TypeOfBuyerModel typeOfBuyerModel = new TypeOfBuyerModel(pk_id, type);
                            mTypeOfBuyerModelArrayList.add(typeOfBuyerModel);

                        }

                        JSONArray jsonArrayAttributeTypes = jsonObject.getJSONArray("attributeTypes");

                        mUnitModelArrayList.clear();
                        mUnitModelArrayList.add(new UnitModel("0", "Select"));
                        for (int i = 0; i < jsonArrayAttributeTypes.length(); i++) {


                            JSONObject jsonObjectSalesType = jsonArrayAttributeTypes.getJSONObject(i);

                            String pk_id = jsonObjectSalesType.getString("pk_id");
                            String attributes_type = jsonObjectSalesType.getString("attributes_type");

                            UnitModel unitModel = new UnitModel(pk_id, attributes_type);
                            mUnitModelArrayList.add(unitModel);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterUnitList.notifyDataSetChanged();
                    mAdapterPreferredList.notifyDataSetChanged();
                    mAdapterTypeOfBuyerList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarEnquiry);


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
                UtilityMethods.tuchOn(ProgressBarEnquiry);

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

                params.put("utype", mSessionManager.getStringData(Constants.KEY_USER_TYPE));
                params.put("pid", productid);


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

    //Validation
    private boolean validation() {


        if (edtQuantity.getText().toString().equals("")) {
            edtQuantity.setError("Enter quantity");
            edtQuantity.requestFocus();
            return false;
        } else if (Integer.parseInt(edtQuantity.getText().toString()) < 1) {
            edtQuantity.setError("Enter valid quantity");
            edtQuantity.requestFocus();
            return false;
//        } else if (mSelectedUnit.equals("0")) {
//            UtilityMethods.showInfoToast(getActivity(), "Please select unit");
//            return false;
        }/* else if (mSelectedCurrency.equals("Select")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select currency");

            return false;
        } else if (edtFromPrice.getText().toString().equals("")) {
            edtFromPrice.setError("Enter From price");
            edtFromPrice.requestFocus();
            return false;
        } else if (Integer.parseInt(edtFromPrice.getText().toString()) <= 0) {
            edtFromPrice.setError("Enter valid from price");
            edtFromPrice.requestFocus();
            return false;
        } else if (!edtToPrice.getText().toString().equals("") && Integer.parseInt(edtToPrice.getText().toString()) < Integer.parseInt(edtFromPrice.getText().toString())) {
            edtToPrice.setError("From price is greater than To price");
            edtToPrice.requestFocus();
            return false;
        }*/ else if (mSelectedPreferred.equals("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select preferred");

            return false;
        } else if (mSelectedTypeOfBuyer.equals("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select type of buyer");

            return false;
        } else if (edtDescription.getText().toString().equals("")) {
            edtDescription.setError("Enter description");
            edtDescription.requestFocus();
            return false;
        }
        return true;
    }
}
