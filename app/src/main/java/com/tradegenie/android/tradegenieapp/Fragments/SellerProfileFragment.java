package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSellersProduct;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterValidateData;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterViewCertificates;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterViewDocuments;
import com.tradegenie.android.tradegenieapp.Models.ProductListBuyModel;
import com.tradegenie.android.tradegenieapp.Models.UploadCertificateModel;
import com.tradegenie.android.tradegenieapp.Models.UploadDocumentsModel;
import com.tradegenie.android.tradegenieapp.Models.ValidateDataModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SellerProfileFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.iv_company_logo)
    ImageView ivCompanyLogo;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.ratings)
    RatingBar ratings;
    @BindView(R.id.tv_rateing)
    TextView tvRateing;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_company_website)
    TextView tvCompanyWebsite;
    //    @BindView(R.id.tv_company_address)
//    TextView tvCompanyAddress;
    @BindView(R.id.tv_company_est)
    TextView tvCompanyEst;
    @BindView(R.id.tv_nature)
    TextView tvNature;
    @BindView(R.id.tv_business_nature)
    TextView tvBusinessNature;
    @BindView(R.id.tv_total_emp)
    TextView tvTotalEmp;
    @BindView(R.id.tv_annual_turnover)
    TextView tvAnnualTurnover;
    @BindView(R.id.tv_firm)
    TextView tvFirm;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.linear_layout_about_us)
    LinearLayout linearLayoutAboutUs;
    @BindView(R.id.img_previous_certificate)
    ImageView imgPreviousCertificate;
    @BindView(R.id.recycler_add_document_certificate)
    RecyclerView recyclerAddDocumentCertificate;
    @BindView(R.id.img_next_certificate)
    ImageView imgNextCertificate;
    @BindView(R.id.linear_layout_certificate)
    LinearLayout linearLayoutCertificate;
    @BindView(R.id.ProgressBarSellerProfile)
    RelativeLayout ProgressBarSellerProfile;
    @BindView(R.id.recycler_add_document)
    RecyclerView recyclerAddDocument;
    @BindView(R.id.tv_certificate)
    TextView tvCertificate;
    @BindView(R.id.tv_document)
    TextView tvDocument;
    @BindView(R.id.linear_layout_document)
    LinearLayout linearLayoutDocument;
    @BindView(R.id.recyclerview_validate_data)
    RecyclerView recyclerviewValidateData;
    @BindView(R.id.img_previous_document)
    ImageView imgPreviousDocument;
    @BindView(R.id.img_next_document)
    ImageView imgNextDocument;
    @BindView(R.id.linear_layout_validate_data)
    LinearLayout linearLayoutValidate;
    @BindView(R.id.tv_rate_this_seller)
    TextView tvRateThisSeller;
    @BindView(R.id.tv_user_count)
    TextView tvUserCount;
    @BindView(R.id.txt_country)
    TextView txtCountry;
    @BindView(R.id.txt_city)
    TextView txtCity;
    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.tv_all_products)
    TextView tvAllProducts;
    @BindView(R.id.ll_establishment_year)
    LinearLayout llEstablishmentYear;
    @BindView(R.id.ll_total_employees)
    LinearLayout llTotalEmployees;
    @BindView(R.id.ll_amount_turnover)
    LinearLayout llAmountTurnover;
    @BindView(R.id.ll_legal_status)
    LinearLayout llLegalStatus;
    @BindView(R.id.tv_view_more)
    TextView tvViewMore;

    private String productid = "", seller_fkid = "", fk_bussiness_id = "", pk_id = "", rating_count = "", user_rating = "", no_of_users = "";

    private SessionManager mSessionManager;

    private ArrayList<UploadDocumentsModel> mUploadDocumentsModelArrayList;
    private AdapterViewDocuments mAdapterViewDocuments;
    LinearLayoutManager layoutManager;

    private ArrayList<UploadCertificateModel> mUploadCertificateModelArrayList;
    private AdapterViewCertificates mAdapterViewCertificates;
    LinearLayoutManager layoutManagercertificate;

    private ArrayList<ValidateDataModel> mValidateDataModelArrayList;
    private AdapterValidateData mAdapterValidateData;
    LinearLayoutManager linearLayoutValidateData;

    private DownloadManager downloadManager;

    private List<ProductListBuyModel> productListBuyModelList;
    private AdapterSellersProduct adapterSellersProduct;

    public SellerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.seller_business_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Seller Profile");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productid = bundle.getString("productid");
            seller_fkid = bundle.getString("seller_fkid");

        }

        mUploadDocumentsModelArrayList = new ArrayList<>();
        mAdapterViewDocuments = new AdapterViewDocuments(getActivity(), mUploadDocumentsModelArrayList, SellerProfileFragment.this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAddDocument.setLayoutManager(layoutManager);
        recyclerAddDocument.setAdapter(mAdapterViewDocuments);


        mUploadCertificateModelArrayList = new ArrayList<>();
        mAdapterViewCertificates = new AdapterViewCertificates(getActivity(), mUploadCertificateModelArrayList, SellerProfileFragment.this);
        layoutManagercertificate = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAddDocumentCertificate.setLayoutManager(layoutManagercertificate);
        recyclerAddDocumentCertificate.setAdapter(mAdapterViewCertificates);

        mValidateDataModelArrayList = new ArrayList<>();
        mAdapterValidateData = new AdapterValidateData(getActivity(), mValidateDataModelArrayList);
        linearLayoutValidateData = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerviewValidateData.setLayoutManager(linearLayoutValidateData);
        recyclerviewValidateData.setAdapter(mAdapterValidateData);

        productListBuyModelList = new ArrayList<>();
        adapterSellersProduct = new AdapterSellersProduct(getActivity(), productListBuyModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setAdapter(adapterSellersProduct);

        tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllAboutUs();
            }
        });

        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            getSellerDetails();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }

    private void showAllAboutUs() {
        try {
            tvViewMore.setVisibility(GONE);
            tvAboutUs.setMaxLines(Integer.MAX_VALUE);
            tvAboutUs.setEllipsize(null);
            tvAboutUs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }catch (Exception e){e.printStackTrace();}
    }


    //Method to get Seller Details
    private void getSellerDetails() {

        UtilityMethods.tuchOff(ProgressBarSellerProfile);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_BUSINESS_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSellerDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        txtCountry.setText(jsonObject.getString("country_name"));
                        txtCity.setText(jsonObject.getString("city_name"));
                        String document_url = jsonObject.getString("document_url");
                        String certificate_url = jsonObject.getString("certificate_url");
                        String profile_image = jsonObject.getString("profile_image");
                        String validate_path = jsonObject.getString("validate_path");


                        try {
                            Picasso.get().load(profile_image + jsonObject.getString("company_logo")).placeholder(R.drawable.default_document).into(ivCompanyLogo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray jsonArrayDocuments = jsonObject.getJSONArray("sellerDocument");
                        mUploadDocumentsModelArrayList.clear();
                        for (int i = 0; i < jsonArrayDocuments.length(); i++) {


                            JSONObject jsonObjectDocuments = jsonArrayDocuments.getJSONObject(i);
                            //String pk_id = jsonObjectDocuments.getString("pk_id");
                            String documents_name = jsonObjectDocuments.getString("documents_name");

                            UploadDocumentsModel uploadDocumentsModel = new UploadDocumentsModel("", document_url + documents_name);
                            mUploadDocumentsModelArrayList.add(uploadDocumentsModel);


                        }

                        JSONArray jsonArraycertificates = jsonObject.getJSONArray("sellerCertificate");
                        mUploadCertificateModelArrayList.clear();
                        for (int i = 0; i < jsonArraycertificates.length(); i++) {


                            JSONObject jsonObjectcertificates = jsonArraycertificates.getJSONObject(i);
                            //String pk_id = jsonObjectcertificates.getString("pk_id");
                            String certificate_name = jsonObjectcertificates.getString("certificate_name");

                            UploadCertificateModel uploadCertificateModel = new UploadCertificateModel("", certificate_url + certificate_name);
                            mUploadCertificateModelArrayList.add(uploadCertificateModel);


                        }

                        JSONArray jsonArrayuservalidateData = jsonObject.getJSONArray("uservalidateData");
                        mValidateDataModelArrayList.clear();
                        for (int i = 0; i < jsonArrayuservalidateData.length(); i++) {

                            ValidateDataModel validateDataModel = new ValidateDataModel();

                            JSONObject jsonObjectuservalidateData = jsonArrayuservalidateData.getJSONObject(i);

                            validateDataModel.setPk_id(jsonObjectuservalidateData.getString("pk_id"));
                            validateDataModel.setTitle(jsonObjectuservalidateData.getString("title"));
                            validateDataModel.setImage(validate_path + jsonObjectuservalidateData.getString("image"));

                            mValidateDataModelArrayList.add(validateDataModel);
                        }


                        tvCompanyName.setText(jsonObject.getString("company_name"));
                        tvSellerName.setText(jsonObject.getString("owner_name"));
                        if (jsonObject.getString("comp_no_of_employee").trim().isEmpty()) {
                            llTotalEmployees.setVisibility(GONE);
                        } else {
                            llTotalEmployees.setVisibility(VISIBLE);
                            tvTotalEmp.setText(jsonObject.getString("comp_no_of_employee"));
                        }
                        if (jsonObject.getString("comp_annual_tern_over").trim().isEmpty()) {
                            llAmountTurnover.setVisibility(GONE);
                        } else {
                            llAmountTurnover.setVisibility(VISIBLE);
                            tvAnnualTurnover.setText(jsonObject.getString("comp_annual_tern_over"));
                        }
                        if (jsonObject.getString("comp_firm").trim().isEmpty()) {
                            llLegalStatus.setVisibility(GONE);
                        } else {
                            llLegalStatus.setVisibility(VISIBLE);
                            tvFirm.setText(jsonObject.getString("comp_firm"));
                        }
                        tvCompanyWebsite.setText(jsonObject.getString("company_website"));
                        tvBusinessNature.setText(jsonObject.getString("comp_nature"));
                        if (jsonObject.getString("established_year").trim().isEmpty()) {
                            llEstablishmentYear.setVisibility(GONE);
                        } else {
                            llEstablishmentYear.setVisibility(VISIBLE);
                            tvCompanyEst.setText(jsonObject.getString("established_year"));
                        }
                        fk_bussiness_id = jsonObject.getString("fk_bussiness_id");


//                        if (jsonObject.getString("comp_address").equals("")) {
//
//                            tvCompanyAddress.setVisibility(GONE);
//
//                        } else {
//
//                            tvCompanyAddress.setText(jsonObject.getString("comp_address"));
//
//
//                        }


                        if (jsonObject.getString("comp_aboutus").equals("")) {

                            linearLayoutAboutUs.setVisibility(GONE);

                        } else {

                            tvAboutUs.setText(jsonObject.getString("comp_aboutus").trim());


                        }

                        try {
                            String base_url = "";
                            try {
                                base_url = jsonObject.getString("base_url");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JSONArray jsonArrayProductdata = jsonObject.getJSONArray("productlist");

                            for (int i = 0; i < jsonArrayProductdata.length(); i++) {


                                JSONObject jsonObjectProductdata = jsonArrayProductdata.getJSONObject(i);

                                ProductListBuyModel productListBuyModel = new ProductListBuyModel();
                                productListBuyModel.setFavorite(jsonObjectProductdata.getString("favorite"));
                                productListBuyModel.setMobile_no(jsonObjectProductdata.getString("mobile_no"));
                                //productListBuyModel.setBusspincode(jsonObjectProductdata.getString("pincode"));
                                productListBuyModel.setBussaddress(jsonObjectProductdata.getString("address"));
                                productListBuyModel.setEstablished_year(jsonObjectProductdata.getString("established_year"));
                                productListBuyModel.setComp_name(jsonObjectProductdata.getString("comp_name"));
                                productListBuyModel.setImage_name(base_url + jsonObjectProductdata.getString("image_name"));
                                productListBuyModel.setSub_subcat_name(jsonObjectProductdata.getString("sub_subcat_name"));
                                productListBuyModel.setCat_name(jsonObjectProductdata.getString("cat_name"));
                                productListBuyModel.setProduct_name(jsonObjectProductdata.getString("product_name"));
                                productListBuyModel.setCurrency(jsonObjectProductdata.getString("currency"));
                                productListBuyModel.setCountry(jsonObjectProductdata.getString("country_name"));
                                productListBuyModel.setCity(jsonObjectProductdata.getString("city_name"));
                                productListBuyModel.setCost(jsonObjectProductdata.getString("cost"));
                                productListBuyModel.setPk_id(jsonObjectProductdata.getString("pk_id"));
                                productListBuyModel.setSubcat_name(jsonObjectProductdata.getString("subcat_name"));

                                productListBuyModelList.add(productListBuyModel);

                            }
                            adapterSellersProduct.notifyDataSetChanged();

                            if (productListBuyModelList.size() > 0) {
                                rvProducts.setVisibility(VISIBLE);
                                tvAllProducts.setVisibility(VISIBLE);
                            } else {
                                rvProducts.setVisibility(GONE);
                                tvAllProducts.setVisibility(GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterViewCertificates.notifyDataSetChanged();
                    mAdapterViewDocuments.notifyDataSetChanged();
                    mAdapterValidateData.notifyDataSetChanged();

                    if (mUploadCertificateModelArrayList.size() == 0) {

                        linearLayoutCertificate.setVisibility(GONE);
                        tvCertificate.setVisibility(GONE);

                    } else {

                        linearLayoutCertificate.setVisibility(VISIBLE);
                        tvCertificate.setVisibility(VISIBLE);

                    }


                    if (mUploadDocumentsModelArrayList.size() == 0) {

                        linearLayoutDocument.setVisibility(GONE);
                        tvDocument.setVisibility(GONE);

                    } else {

                        linearLayoutDocument.setVisibility(VISIBLE);
                        tvDocument.setVisibility(VISIBLE);

                    }

                    if (mValidateDataModelArrayList.size() == 0) {

                        linearLayoutValidate.setVisibility(GONE);
                    } else {

                        linearLayoutValidate.setVisibility(VISIBLE);


                    }

                    if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                    {

                        getRatingDetails();

                    } else {

                        UtilityMethods.showInternetDialog(getActivity());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                //UtilityMethods.tuchOn(ProgressBarSellerProfile);


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
                UtilityMethods.tuchOn(ProgressBarSellerProfile);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("seller_id", seller_fkid);
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

    //Methods for Download file
    public void getDownlodFile(String url) {

        UtilityMethods.showInfoToast(getActivity(), "Downloading");

        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File myFile = new File(Constants.KEY_PATH_OF_QUATATION + "/" + fileName);
        // download code
        Log.e("path", "Path of download-->>" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + " file name-->>" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getName() + " file name-->>" + fileName);
        if (myFile.exists()) {
            Log.e("path", "*******is exists******");
            Log.e("path", myFile.toString());
            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", myFile);
            Log.e("uri", "uri-->>>" + uri.getPath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
                //new
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                //new
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
                //new
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(intent);

        } else {
            Log.e("path", "*******Not exists******");
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
            request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);
            Long refrance = downloadManager.enqueue(request);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //Method to get rating Details
    private void getRatingDetails() {

        //UtilityMethods.tuchOff(ProgressBarSellerProfile);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RATING_GET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getRatingDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        pk_id = jsonObject.getString("pk_id");
                        rating_count = jsonObject.getString("rating_count");
                        user_rating = jsonObject.getString("user_rating");
                        no_of_users = jsonObject.getString("no_of_users");

                        tvRateing.setText(rating_count);
                        ratings.setRating(Float.parseFloat(rating_count));


                        if (no_of_users.equals("1")) {
                            tvUserCount.setText("User " + no_of_users);
                        } else {
                            tvUserCount.setText("Users " + no_of_users);
                        }

                        if (user_rating.equals("")) {


                            tvRateThisSeller.setText("Rate this Seller");


                        } else {

                            tvRateThisSeller.setText("Update Rating");

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

                UtilityMethods.tuchOn(ProgressBarSellerProfile);


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
                UtilityMethods.tuchOn(ProgressBarSellerProfile);

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
                params.put("fk_seller_id", seller_fkid);
                params.put("fk_bussiness_id", fk_bussiness_id);
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

    //Method to get rating submit
    private void RatingSubmit(final float rating) {

        UtilityMethods.tuchOff(ProgressBarSellerProfile);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RATING_SUBMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getRatingDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                        {
                            productListBuyModelList.clear();
                            adapterSellersProduct.notifyDataSetChanged();
                            getSellerDetails();

                        } else {

                            UtilityMethods.showInternetDialog(getActivity());
                        }

                        UtilityMethods.showSuccessToast(getActivity(), "Rating submitted");

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

                UtilityMethods.tuchOn(ProgressBarSellerProfile);


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
                UtilityMethods.tuchOn(ProgressBarSellerProfile);

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
                params.put("pk_id", pk_id);
                params.put("fk_pid", productid);
                params.put("fk_seller_id", seller_fkid);
                params.put("fk_bussiness_id", fk_bussiness_id);
                params.put("rating", String.valueOf(rating));
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

    @OnClick({R.id.tv_rate_this_seller, R.id.tv_company_website})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_rate_this_seller:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = mInflater.inflate(R.layout.dialog_rating_submit, null);
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


                final RatingBar rating_bar = alertDialog.findViewById(R.id.rating_bar);
                Button btn_submit = alertDialog.findViewById(R.id.btn_submit);


                if (user_rating.equals("")) {


                } else {

                    rating_bar.setRating(Float.parseFloat(user_rating));

                }


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (rating_bar.getRating() > 0) {

                            if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
                            {
                                alertDialog.dismiss();
                                RatingSubmit(rating_bar.getRating());

                            } else {

                                UtilityMethods.showInternetDialog(getActivity());
                            }


                        } else {


                            UtilityMethods.showInfoToast(getActivity(), "Please give rating");

                        }
                    }

                });

                break;
            case R.id.tv_company_website:

                try {
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse(String.valueOf(tvCompanyWebsite.getText())));
                    getActivity().startActivity(httpIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }
}
