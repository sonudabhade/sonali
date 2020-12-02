package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCurrencyList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterPreferredList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterTypeOfBuyerList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterUnitList;
import com.tradegenie.android.tradegenieapp.Models.CurrencyModel;
import com.tradegenie.android.tradegenieapp.Models.PostRequirementEnquiryModel;
import com.tradegenie.android.tradegenieapp.Models.PreferredModel;
import com.tradegenie.android.tradegenieapp.Models.SelectedProductModel;
import com.tradegenie.android.tradegenieapp.Models.TypeOfBuyerModel;
import com.tradegenie.android.tradegenieapp.Models.UnitModel;
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

public class PostRequirementDetailsFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.linearLayoutProductListDetails)
    LinearLayout linearLayoutProductListDetails;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressbarPostRequirementDetails)
    RelativeLayout ProgressbarPostRequirementDetails;
    @BindView(R.id.linear_layout_send_enq)
    LinearLayout linearLayoutSendEnq;


    private SessionManager mSessionManager;
    private ArrayList<SelectedProductModel> mSelectedProductModelArrayList;

    private ArrayList<PreferredModel> mPreferredModelArrayList;
    private AdapterPreferredList mAdapterPreferredList;

    private ArrayList<TypeOfBuyerModel> mTypeOfBuyerModelArrayList;
    private AdapterTypeOfBuyerList mAdapterTypeOfBuyerList;

    private ArrayList<PostRequirementEnquiryModel> mPostRequirementEnquiryModelArrayList;

    private JSONArray jsonArray;

    public PostRequirementDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_requirement_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.post_requirement));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());
        mSelectedProductModelArrayList = new ArrayList<>();
        mPostRequirementEnquiryModelArrayList = new ArrayList<>();


        mPreferredModelArrayList = new ArrayList<>();
        mPreferredModelArrayList.add(new PreferredModel("0", "Select"));
        mAdapterPreferredList = new AdapterPreferredList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mPreferredModelArrayList);


        mTypeOfBuyerModelArrayList = new ArrayList<>();
        mTypeOfBuyerModelArrayList.add(new TypeOfBuyerModel("0", "Select"));
        mAdapterTypeOfBuyerList = new AdapterTypeOfBuyerList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mTypeOfBuyerModelArrayList);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mSelectedProductModelArrayList = (ArrayList<SelectedProductModel>) bundle.getSerializable("mSelectedProductModelArrayList");

        }

        for (int i = 0; i < mSelectedProductModelArrayList.size(); i++) {

            mPostRequirementEnquiryModelArrayList.add(new PostRequirementEnquiryModel());

        }

        Log.e("check", "Size=======>" + mSelectedProductModelArrayList.size());

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


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            CallgetSalesTypeList();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            linearLayoutProductListDetails.removeAllViews();
            ShowListOfSearchProduct();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.linear_layout_send_enq)
    public void onViewClicked() {

        Boolean validation = true;

        jsonArray = new JSONArray();


       // for (int i = 0; i < mSelectedProductModelArrayList.size(); i++) {
        for (int i = mSelectedProductModelArrayList.size()-1; i >= 0; i--) {


            JSONObject jsonObject = new JSONObject();

            try {

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    jsonObject.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                } else {

                    jsonObject.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonObject.put("fk_subsubcat", mPostRequirementEnquiryModelArrayList.get(i).getFk_subsubcat());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                if (mPostRequirementEnquiryModelArrayList.get(i).getDescription() == null || mPostRequirementEnquiryModelArrayList.get(i).getDescription().equals("")) {

                    validation = false;

                    UtilityMethods.showInfoToast(getActivity(), "Please enter description in " + mPostRequirementEnquiryModelArrayList.get(i).getSubsubcat_name() + " product");


                } else {

                    jsonObject.put("descripation", mPostRequirementEnquiryModelArrayList.get(i).getDescription());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
//
//                if (mPostRequirementEnquiryModelArrayList.get(i).getTypeofbuyer().equals("0")) {
//
//                    validation = false;
//
//                    UtilityMethods.showInfoToast(getActivity(), "Please select type of buyer in " + mPostRequirementEnquiryModelArrayList.get(i).getSubsubcat_name() + " product");
//
//
//                } else {
//
                    jsonObject.put("type_of_buyer", mPostRequirementEnquiryModelArrayList.get(i).getTypeofbuyer());
//
//                }
//
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
//
//                Log.e("check", "==========>" + mPostRequirementEnquiryModelArrayList.get(i).getPreferred());
//
//                if (mPostRequirementEnquiryModelArrayList.get(i).getPreferred().equals("0")) {
//
//                    validation = false;
//
//                    UtilityMethods.showInfoToast(getActivity(), "Please select preferred in " + mPostRequirementEnquiryModelArrayList.get(i).getSubsubcat_name() + " product");
//
//
//                } else {
//
                    jsonObject.put("preferred", mPostRequirementEnquiryModelArrayList.get(i).getPreferred());
//
//                }
//
            } catch (JSONException e) {
                e.printStackTrace();
            }


           /* try {

                if (mPostRequirementEnquiryModelArrayList.get(i).getCurrency() == null) {

                    validation = false;

                    UtilityMethods.showInfoToast(getActivity(), "Please select currency in " + mPostRequirementEnquiryModelArrayList.get(i).getSubsubcat_name() + " product");


                } else {

                    jsonObject.put("currency", mPostRequirementEnquiryModelArrayList.get(i).getCurrency());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
*/

            try {
//
//
//                if (mPostRequirementEnquiryModelArrayList.get(i).getUnit() == null || mPostRequirementEnquiryModelArrayList.get(i).getUnit().equals("") || mPostRequirementEnquiryModelArrayList.get(i).getUnit().equals("0")) {
//
//                    validation = false;
//
//                    UtilityMethods.showInfoToast(getActivity(), "Please enter unit in " + mPostRequirementEnquiryModelArrayList.get(i).getSubsubcat_name() + " product");
//
//                } else {
//
//
                    jsonObject.put("unit", mPostRequirementEnquiryModelArrayList.get(i).getUnit());
//
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
//
//
                if (mPostRequirementEnquiryModelArrayList.get(i).getQuntity() == null || mPostRequirementEnquiryModelArrayList.get(i).getQuntity().equals("")) {
//
//                    validation = false;
//
//                    UtilityMethods.showInfoToast(getActivity(), "Please enter quantity in " + mPostRequirementEnquiryModelArrayList.get(i).getSubsubcat_name() + " product");
//
                    jsonObject.put("qty", "");
                } else {
//
//                    if (Integer.parseInt(mPostRequirementEnquiryModelArrayList.get(i).getQuntity()) > 0) {
//
                        jsonObject.put("qty", mPostRequirementEnquiryModelArrayList.get(i).getQuntity());
//
//
//                    } else {
//
//                        validation = false;
//
//                        UtilityMethods.showInfoToast(getActivity(), "Please enter valid quantity in " + mPostRequirementEnquiryModelArrayList.get(i).getSubsubcat_name() + " product");
//
//                    }
//
//
                }
//
//
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Log.e("-------------------", "Specification " + mSpecification.get(i).getSpecification() + " Specification value " + mSpecification.get(i).getSpecificationvalue() + " Specification type " + mSpecification.get(i).getType());

            jsonArray.put(jsonObject);


        }


        Log.e("check", "jsonArray" + jsonArray.toString());


        if (validation == true) {

            if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
            {

                CallSendEnquiry();

            } else {

                UtilityMethods.showInternetDialog(getActivity());
            }

        }


    }

    private void ShowListOfSearchProduct() {

        //Display list view runtime inflate
        for (int i = 0; i < mSelectedProductModelArrayList.size(); i++) {

            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.adapter_post_requirement, null);

            linearLayoutProductListDetails.addView(view);

            TextView tv_product_name = view.findViewById(R.id.tv_product_name);
            final RelativeLayout rel_unit = view.findViewById(R.id.rel_unit);
            RelativeLayout rel_currency = view.findViewById(R.id.rel_currency);
            RelativeLayout rel_preferred = view.findViewById(R.id.rel_preferred);
            final RelativeLayout rel_buyer = view.findViewById(R.id.rel_buyer);
            ImageView iv_cancel = view.findViewById(R.id.iv_cancel);
            final Spinner spinner_currency = view.findViewById(R.id.spinner_currency);
            final Spinner spinner_unit = view.findViewById(R.id.spinner_unit);
            final Spinner spinner_preferred = view.findViewById(R.id.spinner_preferred);
            final Spinner spinner_buyer = view.findViewById(R.id.spinner_buyer);
            final EditText edt_quantity = view.findViewById(R.id.edt_quantity);
            final EditText edt_from_price = view.findViewById(R.id.edt_from_price);
            final EditText edt_to_price = view.findViewById(R.id.edt_to_price);
            final EditText edt_description = view.findViewById(R.id.edt_description);


            tv_product_name.setText(mSelectedProductModelArrayList.get(i).getSub_catname());
            mPostRequirementEnquiryModelArrayList.get(i).setFk_subsubcat(mSelectedProductModelArrayList.get(i).getSub_cat_pkid());
            mPostRequirementEnquiryModelArrayList.get(i).setSubsubcat_name(mSelectedProductModelArrayList.get(i).getSub_catname());

            //Declaration
            final ArrayList<CurrencyModel> mCurrencyModelArrayList;
            AdapterCurrencyList mAdapterCurrencyList;


            //Initialization
            mCurrencyModelArrayList = new ArrayList<>();
            mCurrencyModelArrayList.add(new CurrencyModel("0", "Select"));
            mCurrencyModelArrayList.add(new CurrencyModel("1", "INR"));
            mCurrencyModelArrayList.add(new CurrencyModel("2", "LKR"));
            mCurrencyModelArrayList.add(new CurrencyModel("2", "USD"));
            mAdapterCurrencyList = new AdapterCurrencyList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCurrencyModelArrayList);
            spinner_currency.setAdapter(mAdapterCurrencyList);
            final int finalI3 = i;
            spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                    // mGender=mGenderArrayList.get(i).getValue();


                    if (!mCurrencyModelArrayList.get(index).getCurrency().equalsIgnoreCase("Select")) {
                        // mSelectedCurrency[0] = mCurrencyModelArrayList.get(i).getCurrency();

                        mPostRequirementEnquiryModelArrayList.get(finalI3).setCurrency(mCurrencyModelArrayList.get(index).getCurrency());


                    } else {
                        //mSelectedCurrency[0] = "Select";

                        //mPostRequirementEnquiryModelArrayList.get(finalI3).setCurrency("0");

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            final ArrayList<UnitModel> mUnitModelArrayList;
            AdapterUnitList mAdapterUnitList;
            //final String[] mSelectedUnit = new String[1];

            mUnitModelArrayList = new ArrayList<>();
            mUnitModelArrayList.add(new UnitModel("0", "Select"));
            mAdapterUnitList = new AdapterUnitList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mUnitModelArrayList);
            spinner_unit.setAdapter(mAdapterUnitList);
            final int finalI2 = i;
            spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                    // mGender=mGenderArrayList.get(i).getValue();


                    if (!mUnitModelArrayList.get(index).getPk_id().equalsIgnoreCase("0")) {
                        //mSelectedUnit[0] = mUnitModelArrayList.get(finalI2).getPk_id();

                        mPostRequirementEnquiryModelArrayList.get(finalI2).setUnit(mUnitModelArrayList.get(index).getPk_id());


                    } else {
                        //mSelectedUnit[0] = "0";
                        mPostRequirementEnquiryModelArrayList.get(finalI2).setUnit("0");

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            final int finalI1 = i;
            edt_quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    mPostRequirementEnquiryModelArrayList.get(finalI1).setQuntity(edt_quantity.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edt_from_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    mPostRequirementEnquiryModelArrayList.get(finalI1).setPricefrom(edt_from_price.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edt_to_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    mPostRequirementEnquiryModelArrayList.get(finalI1).setPriceto(edt_to_price.getText().toString());


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edt_description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    mPostRequirementEnquiryModelArrayList.get(finalI1).setDescription(edt_description.getText().toString());


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            spinner_preferred.setAdapter(mAdapterPreferredList);
            final int finalI4 = i;
            spinner_preferred.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                    // mGender=mGenderArrayList.get(i).getValue();


                    if (!mPreferredModelArrayList.get(index).getPk_id().equalsIgnoreCase("0")) {
                        // mSelectedPreferred[0] = mPreferredModelArrayList.get(i).getPk_id();

                        mPostRequirementEnquiryModelArrayList.get(finalI4).setPreferred(mPreferredModelArrayList.get(index).getPk_id());


                    } else {
                        //mSelectedPreferred[0] = "0";

                        mPostRequirementEnquiryModelArrayList.get(finalI4).setPreferred("0");


                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinner_buyer.setAdapter(mAdapterTypeOfBuyerList);
            final int finalI5 = i;
            spinner_buyer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                    // mGender=mGenderArrayList.get(i).getValue();


                    if (!mTypeOfBuyerModelArrayList.get(index).getPk_id().equalsIgnoreCase("0")) {
                        // mSelectedTypeOfBuyer[0] = mTypeOfBuyerModelArrayList.get(i).getPk_id();

                        mPostRequirementEnquiryModelArrayList.get(finalI5).setTypeofbuyer(mTypeOfBuyerModelArrayList.get(index).getPk_id());


                    } else {
                        // mSelectedTypeOfBuyer[0] = "0";

                        mPostRequirementEnquiryModelArrayList.get(finalI5).setTypeofbuyer("0");

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
            {

                CallUnitList(mSelectedProductModelArrayList.get(i).getPk_id(), spinner_unit, mUnitModelArrayList, mAdapterUnitList);

            } else {

                UtilityMethods.showInternetDialog(getActivity());
            }

            spinner_unit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    keyBoardHide();
                    return false;
                }
            });

            spinner_currency.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    keyBoardHide();
                    return false;
                }
            });

            spinner_preferred.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    keyBoardHide();
                    return false;
                }
            });

            spinner_buyer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    keyBoardHide();
                    return false;
                }
            });

            rel_unit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    spinner_unit.performClick();
                    keyBoardHide();

                }
            });

            rel_currency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    spinner_currency.performClick();
                    keyBoardHide();

                }
            });

            rel_preferred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    spinner_preferred.performClick();
                    keyBoardHide();

                }
            });

            rel_buyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    spinner_buyer.performClick();
                    keyBoardHide();

                }
            });

            final int finalI = i;
            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mSelectedProductModelArrayList.size() == 1) {

                        UtilityMethods.showInfoToast(getActivity(), "This is last product");

                    } else {


                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Do you really want to remove this product?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                linearLayoutProductListDetails.removeAllViews();
                                mSelectedProductModelArrayList.remove(finalI);

                                Log.e("check", "=========>" + mCurrencyModelArrayList.size());


                                if (mSelectedProductModelArrayList.size() == 0) {

                                    textViewErrorMessage.setVisibility(VISIBLE);
                                    scrollview.setVisibility(GONE);
                                } else {


                                    textViewErrorMessage.setVisibility(GONE);
                                    scrollview.setVisibility(VISIBLE);
                                    ShowListOfSearchProduct();


                                }

                            }
                        });
                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }

                }
            });


        }

    }

    //Method to get SalesType List
    private void CallgetSalesTypeList() {

        UtilityMethods.tuchOff(ProgressbarPostRequirementDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_TYPE_OF_BUYER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallgetSalesTypeList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONArray jsonArraySalesType = jsonObject.getJSONArray("type_of_buyer");

                        mTypeOfBuyerModelArrayList.clear();
                        mTypeOfBuyerModelArrayList.add(new TypeOfBuyerModel("0", "Select"));
                        for (int i = 0; i < jsonArraySalesType.length(); i++) {


                            JSONObject jsonObjectSalesType = jsonArraySalesType.getJSONObject(i);

                            String pk_id = jsonObjectSalesType.getString("pk_id");
                            String type = jsonObjectSalesType.getString("type");

                            TypeOfBuyerModel typeOfBuyerModel = new TypeOfBuyerModel(pk_id, type);
                            mTypeOfBuyerModelArrayList.add(typeOfBuyerModel);

                        }

                        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                        {

                            CallgetpreferedTypeList();

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

                    mAdapterTypeOfBuyerList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);


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
                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


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

    //Method to get preferedType List
    private void CallgetpreferedTypeList() {

        UtilityMethods.tuchOff(ProgressbarPostRequirementDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PREFEREED_TYPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallgetpreferedTypeList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

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


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterPreferredList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);


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
                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


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

    //Method to get Unit List
    private void CallUnitList(final String cat_id, final Spinner spinner, final ArrayList<UnitModel> AdapterUnitList, final AdapterUnitList mAdapterUnitList) {

        UtilityMethods.tuchOff(ProgressbarPostRequirementDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ENQUIRY_ASSIGN_ATTRIBUTE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallUnitList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONArray jsonArrayAttributeTypes = jsonObject.getJSONArray("attribute_array");

                        AdapterUnitList.clear();
                        AdapterUnitList.add(new UnitModel("0", "Select"));
                        for (int i = 0; i < jsonArrayAttributeTypes.length(); i++) {


                            JSONObject jsonObjectSalesType = jsonArrayAttributeTypes.getJSONObject(i);

                            String pk_id = jsonObjectSalesType.getString("pk_id");
                            String attributes_type = jsonObjectSalesType.getString("attribute_name");

                            UnitModel unitModel = new UnitModel(pk_id, attributes_type);
                            AdapterUnitList.add(unitModel);

                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterUnitList.notifyDataSetChanged();
                    //spinner.setAdapter(mAdapterUnitList);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);


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
                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("cat_id", cat_id);

                Log.e("check", "params" + params);

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


    //Method to  SendEnquiry
    private void CallSendEnquiry() {

        UtilityMethods.tuchOff(ProgressbarPostRequirementDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_POSTENQUIRY_SUBMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallSendEnquiry -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        getActivity().getFragmentManager().popBackStack();

                        PostRequirementListFragment postRequirementListFragment = new PostRequirementListFragment();
                        Constants.mMainActivity.ChangeFragments(postRequirementListFragment, "PostRequirementListFragment");

                        UtilityMethods.showSuccessToast(getActivity(), "Post requirement successfully submitted");


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("3")) {

                        UtilityMethods.showInfoToast(getActivity(), "Sorry!! No products available for this category.");

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterPreferredList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);


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
                UtilityMethods.tuchOn(ProgressbarPostRequirementDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("enquirypost", jsonArray.toString());

                Log.e("check", "params===========>" + params.toString());

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

    //Hide Keyboard
    public void keyBoardHide() {

        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
