package com.tradegenie.android.tradegenieapp.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.tradegenie.android.tradegenieapp.Activity.SplashScreenActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterLanguageList;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.USER_LANG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    @BindView(R.id.txt_logout)
    TextView txtLogout;
    @BindView(R.id.spinner_language)
    Spinner spinnerLanguage;
    @BindView(R.id.iv_view_spinner_language)
    View ivViewSpinnerLanguage;
    @BindView(R.id.iv_drop_down_language)
    ImageView ivDropDownLanguage;
    @BindView(R.id.rel_language)
    RelativeLayout relLanguage;
    Unbinder unbinder;


    SessionManager mSessionManager;
    String SelectedLanguage;
    ArrayList<String> mLanguageArrayList;


    AdapterLanguageList mAdapterLanguageList;

    @BindView(R.id.ProgressBarCategoryFragment)
    RelativeLayout ProgressBarCategoryFragment;
    int check = 0;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.settings));
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        relLanguage.setVisibility(View.GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mLanguageArrayList = new ArrayList<>();
        mLanguageArrayList.add("English");
        mLanguageArrayList.add("Sinhala");
       /* mLanguageArrayList.add("Marathi");
        mLanguageArrayList.add("Hindi");*/
        mAdapterLanguageList = new AdapterLanguageList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mLanguageArrayList);
        spinnerLanguage.setAdapter(mAdapterLanguageList);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();

                if(++check > 1) {

                    SelectedLanguage = mLanguageArrayList.get(i);

                    if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                    {
                        UtilityMethods.tuchOff(ProgressBarCategoryFragment);
                        UpdateMySelectedLanguage();
                    } else {
                        UtilityMethods.showInternetDialog(getActivity());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {
            UtilityMethods.tuchOff(ProgressBarCategoryFragment);
            GetMySelectedLanguage();

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

    @OnClick({R.id.txt_logout, R.id.rel_language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_logout:

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Do you really want to logout?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                        {
                            UtilityMethods.tuchOff(ProgressBarCategoryFragment);
                            getRemoveOTP();

                        } else {

                            UtilityMethods.showInternetDialog(getActivity());
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
                break;
            case R.id.rel_language:


                break;
        }
    }

    //Method to get language
    private void GetMySelectedLanguage() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_USER_LANGUAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "URL_GET_USER_LANGUAGE -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (jsonObject.getString("language").equals("")) {
                            SelectedLanguage = "English";
                        } else {
                            SelectedLanguage = jsonObject.getString("language");
                        }

                        boolean ISLanguageSelected = false;
                        for (int i = 0; i < mLanguageArrayList.size(); i++) {
                            if (mLanguageArrayList.get(i).equals(SelectedLanguage)) {
                                spinnerLanguage.setSelection(i);
                                ISLanguageSelected = true;
                                break;
                            }
                        }

                        //if language is not present in our dropdown
                        if (!ISLanguageSelected) {
                            SelectedLanguage = "English";
                            spinnerLanguage.setSelection(0);
                        }
                        relLanguage.setVisibility(View.VISIBLE);
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

                UtilityMethods.tuchOn(ProgressBarCategoryFragment);


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
                UtilityMethods.tuchOn(ProgressBarCategoryFragment);

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


                Log.e("check", "URL_GET_USER_LANGUAGE ======>" + params);

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

    //Method to update language
    private void UpdateMySelectedLanguage() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_USER_LANGUAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "URL_UPDATE_USER_LANGUAGE -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {
                        mSessionManager.putStringData(USER_LANG, SelectedLanguage);
                        setLocale();
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

                UtilityMethods.tuchOn(ProgressBarCategoryFragment);


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
                UtilityMethods.tuchOn(ProgressBarCategoryFragment);

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
                params.put("language", SelectedLanguage);


                Log.e("check", "URL_UPDATE_USER_LANGUAGE ======>" + params);

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

    public void setLocale() {
        try {
            String lang = SelectedLanguage;
            if (lang.equalsIgnoreCase("Sinhala")){
                lang = "si";
            }else {
                lang = "en";
            }
            Locale myLocale = new Locale(lang);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
            startActivity(intent);
            getActivity().finishAffinity();
        }catch (Exception e){e.printStackTrace();}
    }
    //Method to get Remove OTP
    private void getRemoveOTP() {

        //UtilityMethods.tuchOff(relativeLayoutAboutUs);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_OTP_EMPLTY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getRemoveOTP -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        FragmentManager fm = getActivity().getFragmentManager(); // or 'getSupportFragmentManager();'
                        int count = fm.getBackStackEntryCount();
                        for (int i = 0; i < count; ++i) {
                            fm.popBackStack();
                        }

                        mSessionManager.putStringData(Constants.KEY_SELLER_USER_NAME, "");

                        mSessionManager.removeAll();

                        Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();

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

                // UtilityMethods.tuchOn(relativeLayoutAboutUs);


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
                // UtilityMethods.tuchOn(relativeLayoutAboutUs);

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
}
