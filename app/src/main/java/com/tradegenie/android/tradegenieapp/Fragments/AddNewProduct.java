package com.tradegenie.android.tradegenieapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;;
import com.tradegenie.android.tradegenieapp.Activity.SquareCropActivity;
import com.tradegenie.android.tradegenieapp.Activity.SquareCropBorcherActivity;
import com.tradegenie.android.tradegenieapp.Activity.YoutubeActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterAddProductImages;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterAttributeList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterAttributeTypeList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSellerList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSpinnerModeofSupply;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSpinnerWithCheckbox;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSubCategoryList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterSubSubCategoryList;
import com.tradegenie.android.tradegenieapp.BuildConfig;
import com.tradegenie.android.tradegenieapp.DocUpload.FilePath;
import com.tradegenie.android.tradegenieapp.DocUpload.SingleUploadBroadcastReceiver;
import com.tradegenie.android.tradegenieapp.Models.AddProductImagesModel;
import com.tradegenie.android.tradegenieapp.Models.AddSpecificationModel;
import com.tradegenie.android.tradegenieapp.Models.AttributeModel;
import com.tradegenie.android.tradegenieapp.Models.AttributeTypeModel;
import com.tradegenie.android.tradegenieapp.Models.CategoryModel;
import com.tradegenie.android.tradegenieapp.Models.DataPart;
import com.tradegenie.android.tradegenieapp.Models.ModeOfSupplyModel;
import com.tradegenie.android.tradegenieapp.Models.SellerListModel;
import com.tradegenie.android.tradegenieapp.Models.StateVO;
import com.tradegenie.android.tradegenieapp.Models.SubCategoryModel;
import com.tradegenie.android.tradegenieapp.Models.SubSubCategoryModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;
import com.tradegenie.android.tradegenieapp.Utility.VolleyMultipartRequest;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class AddNewProduct extends Fragment implements View.OnClickListener, SingleUploadBroadcastReceiver.Delegate {

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    private RelativeLayout relativeLayoutAddNewProduct;
    private RelativeLayout rel_upload_brochure;
    private Spinner spinner_category;
    private Spinner spinner_sub_category;
    private Spinner spinner_sub_sub_category;
    private Spinner spinner_used_type;
    private Spinner spinner_mode_of_payment;
    private Spinner spinner_mode_of_supply;
    //private Spinner spinner_attribute_list;
    //private Spinner spinner_attribute_type_list;
    private LinearLayout linear_layout_specification_view;
    private EditText edt_production_name;
    private EditText edt_product_price;
    private EditText edt_video_link;
    private EditText edt_delivery_time;
    private EditText edt_packing_details;
    private EditText edt_production_capacity;
    private EditText edt_port_of_supply;
    private EditText edt_minimum_order_quantity;
    private EditText edt_sample_piece;
    private EditText edt_short_description;
    private EditText edt_keywords;
    private EditText edt_additional_feature;
    private EditText edt_description;
    private ImageView iv_video_link;
    private ImageView iv_image_one;
    private ImageView iv_image_two;
    private ImageView iv_image_three;
    private ImageView iv_drop_down_upload_brochure;
    private ImageView iv_clear;
    private TextView tv_add_more;
    private TextView txt_upload_file_name;
    private TextView tv_inr;
    private TextView tv_selected_mode_of_payment;
    private TextView tv_selected_mode_of_supply;
    private Button btn_submit;
    private RecyclerView recycler_add_images;
    private RelativeLayout rel_mode_of_payment;
    private RelativeLayout rel_mode_of_supply;
    private RelativeLayout rel_category;
    private RelativeLayout rel_sub_category;
    private RelativeLayout rel_sub_sub_category;
    private RelativeLayout rel_used_type;

    private int position;
    private String image_count;


    private ArrayList<AddProductImagesModel> mAddProductImagesModelArrayList;
    private AdapterAddProductImages mAdapterAddProductImages;
    LinearLayoutManager layoutManager;

    private ArrayList<CategoryModel> mCategoryModelArrayList;
    private AdapterCategoryList mAdapterCategoryList;
    private String mSelectedCategory;

    private ArrayList<SubCategoryModel> mSubCategoryModelArrayList;
    private AdapterSubCategoryList mAdapterSubCategoryList;
    private String mSelectedSubCategory;

    private ArrayList<AttributeModel> mAttributeModelArrayList;
    private AdapterAttributeList mAdapterAttributeModel;

    private ArrayList<SubSubCategoryModel> mSubSubCategoryModelArrayList;
    private AdapterSubSubCategoryList mAdapterSubSubCategoryList;
    private String mSelectedSubSubCategory;

    private ArrayList<SellerListModel> mSellerListModelArrayList;
    private AdapterSellerList mAdapterSellerList;
    private String mSelectedSeller;


    private String mSelectedModeOfPayment;
    private String mSelectedModeOfPaymentPk_id;
    private ArrayList<StateVO> mModeOfPaymentModelArrayList = new ArrayList<>();
    private AdapterSpinnerWithCheckbox mAdapterModeOfPaymentList;

    private ArrayList<ModeOfSupplyModel> mModeOfSupplyModelArrayList = new ArrayList<>();
    private AdapterSpinnerModeofSupply mAdapterModeOfSupplyList;
    private String mSelectedModeOfSupply;
    private String mSelectedModeOfSupplyPk_id;


    //camera and gallery
    private Uri fileUri;
    private static final int TAKE_PICTURE = 1, SELECT_PICTURE = 2;
    private static final int CROPPED_IMAGE = 3;
    private static final String IMAGE_DIRECTORY_NAME = "TradeGenie";
    //close camera and gallery

    //camera and gallery
    private static final int TAKE_PICTURE_BROCHURE = 11, SELECT_PICTURE_BROCHURE = 22;
    private static final int CROPPED_IMAGE_BROCHURE = 33;
    //close camera and gallery


    //Image request code
    private int PICK_DOC_REQUEST = 4;
    private int PICK_VIDEO_REQUEST = 5;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Uri to store the image uri
    private Uri filePath = null, VideofilePath;

    String productid = "";

    private JSONArray jsonArray;

    private String mCurrency;


    ArrayList<AddSpecificationModel> mSpecification = new ArrayList<>();


    private SessionManager mSessionManager;

    public AddNewProduct() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_product, container, false);

        relativeLayoutAddNewProduct = view.findViewById(R.id.relativeLayoutAddNewProduct);
        rel_upload_brochure = view.findViewById(R.id.rel_upload_brochure);
        spinner_category = view.findViewById(R.id.spinner_category);
        spinner_sub_category = view.findViewById(R.id.spinner_sub_category);
        spinner_sub_sub_category = view.findViewById(R.id.spinner_sub_sub_category);
        spinner_used_type = view.findViewById(R.id.spinner_used_type);
        spinner_mode_of_payment = view.findViewById(R.id.spinner_mode_of_payment);
        spinner_mode_of_supply = view.findViewById(R.id.spinner_mode_of_supply);
        btn_submit = view.findViewById(R.id.btn_submit);
        linear_layout_specification_view = view.findViewById(R.id.linear_layout_specification_view);
        edt_production_name = view.findViewById(R.id.edt_production_name);
        edt_product_price = view.findViewById(R.id.edt_product_price);
        edt_video_link = view.findViewById(R.id.edt_video_link);
        edt_delivery_time = view.findViewById(R.id.edt_delivery_time);
        edt_packing_details = view.findViewById(R.id.edt_packing_details);
        edt_production_capacity = view.findViewById(R.id.edt_production_capacity);
        edt_port_of_supply = view.findViewById(R.id.edt_port_of_supply);
        edt_minimum_order_quantity = view.findViewById(R.id.edt_minimum_order_quantity);
        edt_sample_piece = view.findViewById(R.id.edt_sample_piece);
        edt_short_description = view.findViewById(R.id.edt_short_description);
        edt_keywords = view.findViewById(R.id.edt_keywords);
        edt_additional_feature = view.findViewById(R.id.edt_additional_feature);
        edt_description = view.findViewById(R.id.edt_description);
        iv_video_link = view.findViewById(R.id.iv_video_link);
        iv_drop_down_upload_brochure = view.findViewById(R.id.iv_drop_down_upload_brochure);
        iv_clear = view.findViewById(R.id.iv_clear);
        tv_add_more = view.findViewById(R.id.tv_add_more);
        tv_inr = view.findViewById(R.id.tv_inr);
        txt_upload_file_name = view.findViewById(R.id.txt_upload_file_name);
        recycler_add_images = view.findViewById(R.id.recycler_add_images);
        tv_selected_mode_of_payment = view.findViewById(R.id.tv_selected_mode_of_payment);
        tv_selected_mode_of_supply = view.findViewById(R.id.tv_selected_mode_of_supply);
        rel_mode_of_payment = view.findViewById(R.id.rel_mode_of_payment);
        rel_mode_of_supply = view.findViewById(R.id.rel_mode_of_supply);
        rel_category = view.findViewById(R.id.rel_category);
        rel_sub_category = view.findViewById(R.id.rel_sub_category);
        rel_sub_sub_category = view.findViewById(R.id.rel_sub_sub_category);
        rel_used_type = view.findViewById(R.id.rel_used_type);
        rel_mode_of_payment = view.findViewById(R.id.rel_mode_of_payment);

        Constants.mMainActivity.setToolBarName(getString(R.string.add_product));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();

        Constants.mMainActivity.thumbnail_r = null;
        mSessionManager = new SessionManager(getActivity());
        requestStoragePermission();

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePath = null;
                txt_upload_file_name.setText("");
                iv_clear.setVisibility(GONE);
                iv_drop_down_upload_brochure.setVisibility(VISIBLE);
            }
        });

        edt_short_description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                     /*   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_short_description, InputMethodManager.SHOW_IMPLICIT);*/
                }
                return false;
            }
        });

        edt_keywords.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                     /*   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_short_description, InputMethodManager.SHOW_IMPLICIT);*/
                }
                return false;
            }
        });

        edt_additional_feature.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                     /*   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_short_description, InputMethodManager.SHOW_IMPLICIT);*/
                }
                return false;
            }
        });

        edt_description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                     /*   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_short_description, InputMethodManager.SHOW_IMPLICIT);*/
                }
                return false;
            }
        });


        spinner_category.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }

        });

        spinner_mode_of_payment.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }

        });

        spinner_mode_of_supply.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }

        });

        spinner_sub_category.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }

        });

        spinner_sub_sub_category.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }

        });

        spinner_used_type.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }

        });

        rel_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                spinner_category.performClick();

            }
        });

        rel_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                spinner_sub_category.performClick();

            }
        });

        rel_sub_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                spinner_sub_sub_category.performClick();

            }
        });

        rel_upload_brochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                ChooseCertificateDialogForDocument(getActivity());

            }
        });

        rel_mode_of_supply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                spinner_mode_of_supply.performClick();

            }
        });

        rel_mode_of_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                spinner_mode_of_payment.performClick();

            }
        });

        rel_used_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                spinner_used_type.performClick();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard();


                for (int i = 0; i < mAddProductImagesModelArrayList.size(); i++) {


                    Bitmap bitmap = mAddProductImagesModelArrayList.get(i).getDocumentBitmap();


                }


                jsonArray = new JSONArray();

                Log.e("check", "mSpecification.get(0).getSpecification() -------->" + mSpecification.get(0).getSpecification());


                if (validation()) {

//                    boolean isSpecificationCorrect = true;
//
//                    for (int k = mSpecification.size() - 1; k >= 0; k--) {
//
//                        if (mSpecification.get(k).getSpecification().equals("0") || mSpecification.get(k).getType().equals("0") || mSpecification.get(k).getSpecificationvalue().equals("")) {
//
//                            UtilityMethods.showInfoToast(getActivity(), "Please select product specification of row " + (k + 1));
//
//                            isSpecificationCorrect = false;
//                        }
//
//                    }


//                    if (isSpecificationCorrect) {


                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            CallAddNewProduct();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }
//                    }

                    Log.e("test", "Success---->>> ");
                } else {

                    Log.e("test", "Failed---->>> ");
                }


                for (int i = 0; i < mSpecification.size(); i++) {

                    if (mSpecification.get(i).isShouldAddToJsonArray()) {
                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("attribute", mSpecification.get(i).getSpecification());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jsonObject.put("value", mSpecification.get(i).getSpecificationvalue());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jsonObject.put("type", mSpecification.get(i).getType());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("-------------------", "Specification " + mSpecification.get(i).getSpecification() + " Specification value " + mSpecification.get(i).getSpecificationvalue() + " Specification type " + mSpecification.get(i).getType());

                        jsonArray.put(jsonObject);
                    }

                }
            }
        });

        tv_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSpecificationModel addSpecificationModel = new AddSpecificationModel();
                addSpecificationModel.setShouldAddToJsonArray(false);
                mSpecification.add(addSpecificationModel);
                mAdapterAttributeModel.notifyDataSetChanged();
                ShowSpecificationView(inflater, container);

            }
        });


        iv_video_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edt_video_link.getText().toString().length() < 10) {

                    UtilityMethods.showInfoToast(getActivity(), "Please enter valid youtube link");

                } else {

                    if (edt_video_link.getText().toString().contains("=")) {

                        String part0 = null; // this will contain "Fruit"
                        String part1 = null;
                        try {
                            String url = edt_video_link.getText().toString();
                            part0 = null;
                            String video_code1 = null;
                            String[] separated = url.split("=");
                            part0 = separated[0];
                            part1 = null;
                            part1 = separated[1];
                        } catch (Exception e) {
                            e.printStackTrace();

                            UtilityMethods.showInfoToast(getActivity(), "Please enter valid url");


                        }


                        Intent i = new Intent(getActivity(), YoutubeActivity.class);
                        Bundle bundle = new Bundle();

                        Log.e("check", "youtube part0==>" + part0);
                        Log.e("check", "youtube part1==>" + part1);

                        bundle.putString("video", part1);
                        i.putExtras(bundle);
                        startActivity(i);


                    } else if (edt_video_link.getText().toString().contains("/")) {

                        String part0 = null; // this will contain "Fruit"
                        String video_code1 = null;
                        try {
                            String url = edt_video_link.getText().toString();
                            part0 = null;
                            video_code1 = null;
                            String[] separated = url.split("//");
                            part0 = separated[0];
                            String part1 = null;
                            part1 = separated[1];
                            String[] video_code_url = part1.split("/");
                            String video_code0 = video_code_url[0];
                            video_code1 = video_code_url[1];
                        } catch (Exception e) {
                            e.printStackTrace();

                            UtilityMethods.showInfoToast(getActivity(), "Please enter valid url");


                        }


                        Intent i = new Intent(getActivity(), YoutubeActivity.class);
                        Bundle bundle = new Bundle();

                        Log.e("check", "youtube part0" + part0);
                        Log.e("check", "youtube part1" + video_code1);

                        bundle.putString("video", video_code1);
                        i.putExtras(bundle);
                        startActivity(i);

                    } else {

                        UtilityMethods.showInfoToast(getActivity(), "Please enter valid url");

                    }
                }

            }
        });

        mAddProductImagesModelArrayList = new ArrayList<>();
        mAddProductImagesModelArrayList.add(new AddProductImagesModel(null));
        mAdapterAddProductImages = new AdapterAddProductImages(getActivity(), mAddProductImagesModelArrayList, AddNewProduct.this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_add_images.setLayoutManager(layoutManager);
        recycler_add_images.setAdapter(mAdapterAddProductImages);

        mCategoryModelArrayList = new ArrayList<>();
        mCategoryModelArrayList.add(new CategoryModel("0", "Select", ""));
        mAdapterCategoryList = new AdapterCategoryList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mCategoryModelArrayList);
        spinner_category.setAdapter(mAdapterCategoryList);


        mAttributeModelArrayList = new ArrayList<>();
        mAttributeModelArrayList.add(new AttributeModel("0", "Select"));


        //Set adapter of spinner of load more specification's spinner
        mAdapterAttributeModel = new AdapterAttributeList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mAttributeModelArrayList);


        //Set default Record
        AddSpecificationModel addSpecificationModel = new AddSpecificationModel();
        addSpecificationModel.setShouldAddToJsonArray(false);
        mSpecification.add(addSpecificationModel);
        mAdapterAttributeModel.notifyDataSetChanged();
        //mAdapterAttributeTypeList.notifyDataSetChanged();
        ShowSpecificationView(inflater, container);


        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // mGender=mGenderArrayList.get(i).getValue();

                mSubSubCategoryModelArrayList.clear();
                mSubSubCategoryModelArrayList.add(new SubSubCategoryModel("0", "Select", ""));
                mAdapterSubSubCategoryList.notifyDataSetChanged();


                if (!mCategoryModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedCategory = mCategoryModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSelectedCategory----->" + mSelectedCategory);

                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        getSubcategoryAttribute();


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }
                } else {
                    mSelectedCategory = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSubCategoryModelArrayList = new ArrayList<>();
        mSubCategoryModelArrayList.add(new SubCategoryModel("0", "Select", ""));
        mAdapterSubCategoryList = new AdapterSubCategoryList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mSubCategoryModelArrayList);
        spinner_sub_category.setAdapter(mAdapterSubCategoryList);

        spinner_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mSelectedSubCategory = mSubCategoryModelArrayList.get(i).getPk_id();


                if (!mSubCategoryModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedSubCategory = mSubCategoryModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSelectedSubCategory----->" + mSelectedSubCategory);

                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        getSubSubcategoryAttribute();


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }


                } else {
                    mSelectedSubCategory = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mSubSubCategoryModelArrayList = new ArrayList<>();
        mSubSubCategoryModelArrayList.add(new SubSubCategoryModel("0", "Select", ""));
        mAdapterSubSubCategoryList = new AdapterSubSubCategoryList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mSubSubCategoryModelArrayList);
        spinner_sub_sub_category.setAdapter(mAdapterSubSubCategoryList);

        spinner_sub_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();


                if (!mSubSubCategoryModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    mSelectedSubSubCategory = mSubSubCategoryModelArrayList.get(i).getPk_id();
                    Log.e("check", "mSelectedSubSubCategory----->" + mSelectedSubSubCategory);


                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        GetKeywords(mSubSubCategoryModelArrayList.get(i).getPk_id());
                    } else {
                        UtilityMethods.showInternetDialog(getActivity());
                    }
                } else {
                    mSelectedSubSubCategory = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mSellerListModelArrayList = new ArrayList<>();
        mSellerListModelArrayList.add(new SellerListModel("0", "Select"));
        mAdapterSellerList = new AdapterSellerList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mSellerListModelArrayList);
        spinner_used_type.setAdapter(mAdapterSellerList);

        spinner_used_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mSelectedSeller = mSellerListModelArrayList.get(i).getUA_pkey();


                if (!mSellerListModelArrayList.get(i).getUA_pkey().equalsIgnoreCase("0")) {
                    mSelectedSeller = mSellerListModelArrayList.get(i).getUA_pkey();
                    Log.e("check", "gender----->" + mSelectedSeller);


                } else {
                    mSelectedSeller = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mModeOfPaymentModelArrayList.add(new StateVO("0", "Select", false));
        mAdapterModeOfPaymentList = new AdapterSpinnerWithCheckbox(getActivity(), R.layout.adapter_row_spinner_with_check_box, mModeOfPaymentModelArrayList, this);
        spinner_mode_of_payment.setAdapter(mAdapterModeOfPaymentList);

        spinner_mode_of_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mSelectedModeOfPayment = mModeOfPaymentModelArrayList.get(i).getPk_id();
                mSelectedModeOfPaymentPk_id = mModeOfPaymentModelArrayList.get(i).getPk_id();


                if (!mModeOfPaymentModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    //mSelectedModeOfPayment = mModeOfPaymentModelArrayList.get(i).getPk_id();
                    // Log.e("check", "gender----->" + mSelectedModeOfPayment);


                } else {
                    mSelectedModeOfPayment = "0";
                    mSelectedModeOfPaymentPk_id = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mModeOfSupplyModelArrayList.add(new ModeOfSupplyModel("0", "Select", false));
        mAdapterModeOfSupplyList = new AdapterSpinnerModeofSupply(getActivity(), R.layout.adapter_row_spinner_with_check_box, mModeOfSupplyModelArrayList, this);
        spinner_mode_of_supply.setAdapter(mAdapterModeOfSupplyList);

        spinner_mode_of_supply.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mSelectedModeOfSupply = mModeOfSupplyModelArrayList.get(i).getPk_id();
                mSelectedModeOfSupplyPk_id = mModeOfSupplyModelArrayList.get(i).getPk_id();


                if (!mModeOfSupplyModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                    //mSelectedModeOfSupply = mModeOfSupplyModelArrayList.get(i).getPk_id();
                    //Log.e("check", "gender----->" + mSelectedModeOfSupply);


                } else {
                    mSelectedModeOfSupply = "0";
                    mSelectedModeOfSupplyPk_id = "0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (InternetConnection.isInternetAvailable(getActivity())) {

            GetCategoryList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }


        if (InternetConnection.isInternetAvailable(getActivity())) {

            getSalesTypeList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        if (InternetConnection.isInternetAvailable(getActivity())) {

            getModeofpayList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        if (InternetConnection.isInternetAvailable(getActivity())) {

            getModeofSupplyList();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }


        if (InternetConnection.isInternetAvailable(getActivity())) {

            getCurrency();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        if (InternetConnection.isInternetAvailable(getActivity())) {

            getImageCount();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }


        return view;
    }

    //Method to get keywords
    private void GetKeywords(final String sub_subcategory_fkid) {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_PRODUCT_TAG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "GetCategoryList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        edt_keywords.setText(jsonObject.getString("keywords"));
                    } else if (jsonObject.getString("status").equals("0")) {
                    } else if (jsonObject.getString("status").equals("2")) {
                    } else if (jsonObject.getString("status").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterCategoryList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sub_subcategory_fkid", sub_subcategory_fkid);

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

    private void ShowSpecificationView(final LayoutInflater inflater, final ViewGroup container) {
        linear_layout_specification_view.removeAllViews();
        Log.e("mSpecification", "mSpecification size " + mSpecification.size());
        for (int i = 0; i < mSpecification.size(); i++) {
            View SpecificationView = inflater.inflate(R.layout.row_specification, container, false);
            final EditText edt_value = SpecificationView.findViewById(R.id.edt_value);
            ImageView img_delete = SpecificationView.findViewById(R.id.img_delete);
            final Spinner spinner_attribute_list = SpecificationView.findViewById(R.id.spinner_attribute_list);
            final Spinner spinner_attribute_type_list = SpecificationView.findViewById(R.id.spinner_attribute_type_list);
            RelativeLayout rel_attribute_type_list = SpecificationView.findViewById(R.id.rel_attribute_type_list);
            RelativeLayout rel_attribute_list = SpecificationView.findViewById(R.id.rel_attribute_list);
            edt_value.setTag(mSpecification.size() - 1);//Setting arraylist index position
            ArrayList<AttributeTypeModel> mAttributeTypeModelArrayList;
            final AdapterAttributeTypeList mAdapterAttributeTypeList;

            mAttributeTypeModelArrayList = mSpecification.get(i).getAttributeTypeModelList();

            if (mAttributeTypeModelArrayList == null) {
                mAttributeTypeModelArrayList = new ArrayList<>();

            }
            if (mAttributeTypeModelArrayList.size() == 0) {
                mAttributeTypeModelArrayList.add(new AttributeTypeModel("0", "Select"));
            }

            mAdapterAttributeTypeList = new AdapterAttributeTypeList(getActivity(), R.id.classNameTextView, R.layout.adapter_row_select_class_spinner_layout, mAttributeTypeModelArrayList);


            //final int position = Integer.parseInt(edt_value.getTag().toString());
            final int position = i;
            edt_value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    mSpecification.get(position).setSpecificationvalue(edt_value.getText().toString().trim());
                }
            });


            rel_attribute_type_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    spinner_attribute_type_list.performClick();
                }
            });

            rel_attribute_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    spinner_attribute_list.performClick();
                }
            });


            try {
                spinner_attribute_list.setAdapter(mAdapterAttributeModel);

                final ArrayList<AttributeTypeModel> finalMAttributeTypeModelArrayList = mAttributeTypeModelArrayList;
                spinner_attribute_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String mSelectedAttributeModel = mAttributeModelArrayList.get(i).getPk_id();

                        mSpecification.get(position).setSpecification(mAttributeModelArrayList.get(i).getPk_id());


                        if (!mAttributeModelArrayList.get(i).getPk_id().equalsIgnoreCase("0")) {
                            mSelectedAttributeModel = mAttributeModelArrayList.get(i).getPk_id();
                            Log.e("check", "gender----->" + mSelectedAttributeModel);

                            if (InternetConnection.isInternetAvailable(getActivity())) {

                                getAttributeTypeList(mSelectedAttributeModel, position, finalMAttributeTypeModelArrayList, mAdapterAttributeTypeList);


                            } else {

                                UtilityMethods.showInternetDialog(getActivity());

                            }


                        } else {
                            mSelectedAttributeModel = "0";
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                spinner_attribute_type_list.setAdapter(mAdapterAttributeTypeList);

                final ArrayList<AttributeTypeModel> finalMAttributeTypeModelArrayList1 = mAttributeTypeModelArrayList;
                spinner_attribute_type_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String mSelectedAttributeTypeList = finalMAttributeTypeModelArrayList1.get(i).getPk_id();
                        mSpecification.get(position).setType(finalMAttributeTypeModelArrayList1.get(i).getPk_id());


                        if (!finalMAttributeTypeModelArrayList1.get(i).getPk_id().equalsIgnoreCase("0")) {
                            mSelectedAttributeTypeList = finalMAttributeTypeModelArrayList1.get(i).getPk_id();
                            Log.e("check", "gender----->" + mSelectedAttributeTypeList);


                        } else {
                            mSelectedAttributeTypeList = "0";
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure delete this record?");
                    builder.setCancelable(true);

                    builder.setPositiveButton(

                            R.string.YES,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    if (mSpecification.size() == 1) {

                                        UtilityMethods.showInfoToast(getActivity(), "At least one specification is required");

                                    } else {

                                        mSpecification.remove(Integer.parseInt(edt_value.getTag().toString()));
                                        Log.e("position", "position " + edt_value.getTag().toString());
                                        linear_layout_specification_view.removeAllViews();
                                        Log.e("mSpecification", "mSpecification size " + mSpecification.size());

                                        ShowSpecificationView(inflater, container);
                                    }

                                }
                            });

                    builder.setNegativeButton(

                            R.string.NO,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                }
                            });


                    AlertDialog alert11 = builder.create();
                    alert11.setCanceledOnTouchOutside(false);
                    alert11.show();


                }
            });

            //Set values which are previously selected

            Log.e("-------------------", "Specification position" + " " + i + mSpecification.get(i).getSpecification() + " Specification value " + mSpecification.get(i).getSpecificationvalue() + " Specification type " + mSpecification.get(i).getType());
            edt_value.setText(mSpecification.get(i).getSpecificationvalue());
            for (int j = 0; j < mAttributeModelArrayList.size(); j++) {
                if (mAttributeModelArrayList.get(j).getPk_id().equals(mSpecification.get(i).getSpecification())) {
                    spinner_attribute_list.setSelection(j);
                }
            }

            for (int j = 0; j < mAttributeTypeModelArrayList.size(); j++) {
                if (mAttributeTypeModelArrayList.get(j).getPk_id().equals(mSpecification.get(i).getType())) {
                    spinner_attribute_type_list.setSelection(j);
                }
            }

            linear_layout_specification_view.addView(SpecificationView);
            mAdapterAttributeModel.notifyDataSetChanged();
            mAdapterAttributeTypeList.notifyDataSetChanged();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e("onActivityResult", "requestCode" + requestCode);

        if (requestCode == PICK_DOC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();


            String file = "DOC_" + filePath.toString().substring(filePath.toString().lastIndexOf("/") + 1);

            Log.e("check", "file" + file);

            String filename = file.substring(file.lastIndexOf("/") + 1);

            txt_upload_file_name.setText(filename);

            iv_drop_down_upload_brochure.setVisibility(GONE);
            iv_clear.setVisibility(VISIBLE);

        }

        switch (requestCode) {
            case SELECT_PICTURE:

                if (resultCode == RESULT_OK) {
                    if (data != null) {


                        try {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = getActivity().getContentResolver().query(
                                    selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();
                            //profileImage.setVisibility(View.VISIBLE);
                            Log.e("picturePath", "picturePath " + picturePath);
                            // Bitmap thumbnail =
                            // (BitmapFactory.decodeFile(picturePath));
                            Bitmap thumbnail = decodeSampledBitmapFromResource(
                                    picturePath, 500, 500);

                            // rotated
                            Constants.mMainActivity.thumbnail_r = imageOreintationValidator(
                                    thumbnail, picturePath);
                            //iv_image_one.setBackground(null);
                            //iv_image_one.setImageBitmap(Constants.mMainActivity.thumbnail_r);
                            //IsImageSet = true;


                            //  mAddProductImagesModelArrayList.get(position).setDocumentBitmap(Constants.mMainActivity.thumbnail_r);

                            //  mAdapterAddProductImages.notifyDataSetChanged();

                            GetCroppedImage();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {

                    previewCapturedImage();

                }

                break;
            case CROPPED_IMAGE:
                //iv_image_one.setBackground(null);
                //iv_image_one.setImageBitmap(Constants.mMainActivity.thumbnail_r);

                Bitmap bitmap = mAddProductImagesModelArrayList.get(position).getDocumentBitmap();


                Log.e("bitmap", "========>" + mAddProductImagesModelArrayList.get(position).getDocumentBitmap());

                if (Constants.mMainActivity.thumbnail_r != null) {

                    Log.e("sandeep", "========>");

                    Bitmap bitmapm = mAddProductImagesModelArrayList.get(position).getDocumentBitmap();


                    Log.e("bitmap", "========>" + mAddProductImagesModelArrayList.get(position).getDocumentBitmap());


                    mAddProductImagesModelArrayList.get(position).setDocumentBitmap(Constants.mMainActivity.thumbnail_r);
                    mAdapterAddProductImages.notifyDataSetChanged();

                }

            case SELECT_PICTURE_BROCHURE:


                if (resultCode == RESULT_OK) {
                    if (data != null) {


                        try {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = getActivity().getContentResolver().query(
                                    selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();
                            //profileImage.setVisibility(View.VISIBLE);
                            Log.e("picturePath", "picturePath " + picturePath);
                            // Bitmap thumbnail =
                            // (BitmapFactory.decodeFile(picturePath));
                            Bitmap thumbnail = decodeSampledBitmapFromResource(
                                    picturePath, 500, 500);

                            // rotated
                            Constants.mMainActivity.thumbnail_rb = imageOreintationValidator(
                                    thumbnail, picturePath);
                            //ivProfilePic.setBackground(null);
                            //ivProfilePic.setImageBitmap(Constants.mMainActivity.thumbnail_r);
                            //IsImageSet = true;


                            GetCroppedImageForBrocher();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case TAKE_PICTURE_BROCHURE:
                if (resultCode == RESULT_OK) {


                    previewCapturedImageForBrocher();

                }

                break;
            case CROPPED_IMAGE_BROCHURE:
                //iv_image_one.setBackground(null);
                //iv_image_one.setImageBitmap(Constants.mMainActivity.thumbnail_r);

                //ivProfilePic.setBackground(null);
                //ivProfilePic.setImageBitmap(Constants.mMainActivity.thumbnail_r);


                if (Constants.mMainActivity.thumbnail_rb != null) {
                    // ivProfilePic.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.default_user_profile));
                    txt_upload_file_name.setText("One Brochure Selected");

                }


                break;
        }
    }

    //Method to Get Category List
    private void GetCategoryList() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CATEGORY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "GetCategoryList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraycategory = jsonObject.getJSONArray("category");
                        mAdapterCategoryList.clear();
                        mCategoryModelArrayList.add(new CategoryModel("0", "Select", ""));
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String cat_name = jsonObjectcategory.getString("cat_name");

                            CategoryModel categoryModel = new CategoryModel(pk_id, cat_name, "");
                            mCategoryModelArrayList.add(categoryModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterCategoryList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
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

    //Method to get Subcategory Attribute
    private void getSubcategoryAttribute() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_SUBCATEGORY_ATTRIBUTE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSubcategoryAttribute -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraycategory = jsonObject.getJSONArray("Subcategory");
                        mSubCategoryModelArrayList.clear();
                        mSubCategoryModelArrayList.add(new SubCategoryModel("0", "Select", ""));
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String title = jsonObjectcategory.getString("title");

                            SubCategoryModel subCategoryModel = new SubCategoryModel(pk_id, title, "");
                            mSubCategoryModelArrayList.add(subCategoryModel);

                        }

                        JSONArray jsonArrayattribute = jsonObject.getJSONArray("attribute");
                        mAttributeModelArrayList.clear();
                        mAttributeModelArrayList.add(new AttributeModel("0", "Select"));
                        for (int i = 0; i < jsonArrayattribute.length(); i++) {

                            JSONObject jsonObjectattribute = jsonArrayattribute.getJSONObject(i);

                            String pk_id = jsonObjectattribute.getString("pk_id");
                            String title = jsonObjectattribute.getString("title");

                            AttributeModel attributeModel = new AttributeModel(pk_id, title);
                            mAttributeModelArrayList.add(attributeModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterSubCategoryList.notifyDataSetChanged();
                    mAdapterAttributeModel.notifyDataSetChanged();

                    spinner_sub_category.setSelection(0);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("category_id", mSelectedCategory);

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

    //Method to get Subcategory Attribute
    private void getSubSubcategoryAttribute() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUB_SUB_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSubSubcategoryAttribute -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraycategory = jsonObject.getJSONArray("Sub Subcategory");
                        mSubSubCategoryModelArrayList.clear();
                        mSubSubCategoryModelArrayList.add(new SubSubCategoryModel("0", "Select", ""));
                        for (int i = 0; i < jsonArraycategory.length(); i++) {

                            JSONObject jsonObjectcategory = jsonArraycategory.getJSONObject(i);

                            String pk_id = jsonObjectcategory.getString("pk_id");
                            String title = jsonObjectcategory.getString("title");

                            SubSubCategoryModel subSubCategoryModel = new SubSubCategoryModel(pk_id, title, "");
                            mSubSubCategoryModelArrayList.add(subSubCategoryModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());

                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterSubSubCategoryList.notifyDataSetChanged();

                    spinner_sub_sub_category.setSelection(0);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("subcategory_id", mSelectedSubCategory);

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

    //Method to get get Seller List
    private void getSalesTypeList() {


        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_SALES_TYPE_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getSalesTypeList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArraySeller = jsonObject.getJSONArray("sales_type");
                        mSellerListModelArrayList.clear();
                        mSellerListModelArrayList.add(new SellerListModel("0", "Select"));
                        for (int i = 0; i < jsonArraySeller.length(); i++) {

                            JSONObject jsonObjejsonArraySeller = jsonArraySeller.getJSONObject(i);

                            String pk_id = jsonObjejsonArraySeller.getString("pk_id");
                            String type = jsonObjejsonArraySeller.getString("type");

                            SellerListModel sellerListModel = new SellerListModel(pk_id, type);
                            mSellerListModelArrayList.add(sellerListModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterSellerList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


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

    //Method to get get Modeofpay List
    private void getModeofpayList() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MODEOFPAY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getModeofpayList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArrayModeofPay = jsonObject.getJSONArray("mode of pay");
                        mModeOfPaymentModelArrayList.clear();
                        mModeOfPaymentModelArrayList.add(new StateVO("0", "Select", false));
                        for (int i = 0; i < jsonArrayModeofPay.length(); i++) {

                            JSONObject jsonObjejsonArrayModeofPay = jsonArrayModeofPay.getJSONObject(i);

                            String pk_id = jsonObjejsonArrayModeofPay.getString("pk_id");
                            String mode_of_payment = jsonObjejsonArrayModeofPay.getString("mode_of_payment");

                            StateVO StateVO = new StateVO(pk_id, mode_of_payment, false);
                            mModeOfPaymentModelArrayList.add(StateVO);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    Log.e("check", "new======>" + mModeOfSupplyModelArrayList.size());

                    mAdapterModeOfPaymentList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


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

    //Method to get Mode ofSupply List
    private void getModeofSupplyList() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MODEOFSUPPLY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getModeofSupplyList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArrayModeofPay = jsonObject.getJSONArray("mode of supply");
                        mModeOfSupplyModelArrayList.clear();
                        mModeOfSupplyModelArrayList.add(new ModeOfSupplyModel("0", "Select", false));
                        for (int i = 0; i < jsonArrayModeofPay.length(); i++) {

                            JSONObject jsonObjejsonArrayModeofPay = jsonArrayModeofPay.getJSONObject(i);

                            String pk_id = jsonObjejsonArrayModeofPay.getString("pk_id");
                            String mode_of_supply = jsonObjejsonArrayModeofPay.getString("mode_of_supply");

                            ModeOfSupplyModel modeOfSupplyModel = new ModeOfSupplyModel(pk_id, mode_of_supply, false);
                            mModeOfSupplyModelArrayList.add(modeOfSupplyModel);
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterModeOfSupplyList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


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

    //Method to get Mode ofSupply List
    private void getAttributeTypeList(final String mSelectedAttributeModel, final int position, final ArrayList<AttributeTypeModel> mAttributeTypeModelArrayList, final AdapterAttributeTypeList mAdapterAttributeTypeList) {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_ATTRIBUTE_TYPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getAttributeTypeList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        JSONArray jsonArrayAttributeType = jsonObject.getJSONArray("attribute type");
                        mAttributeTypeModelArrayList.clear();
                        mAttributeTypeModelArrayList.add(new AttributeTypeModel("0", "Select"));
                        for (int i = 0; i < jsonArrayAttributeType.length(); i++) {

                            JSONObject jsonObjejsonAttributeType = jsonArrayAttributeType.getJSONObject(i);

                            String pk_id = jsonObjejsonAttributeType.getString("pk_id");
                            String title = jsonObjejsonAttributeType.getString("title");

                            AttributeTypeModel attributeTypeModel = new AttributeTypeModel(pk_id, title);
                            mAttributeTypeModelArrayList.add(attributeTypeModel);

                        }

                        mSpecification.get(position).setAttributeTypeModelList(mAttributeTypeModelArrayList);
                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                    mAdapterAttributeModel.notifyDataSetChanged();
                    mAdapterAttributeTypeList.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("attribute", mSelectedAttributeModel);

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


    private void CallAddNewProduct() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_PRODUCT_ADD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("Response", new String(response.data));
                        try {

                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            if (jsonObject.getString("error_code").equals("1")) {


                                productid = jsonObject.getString("productid");


                                if (filePath != null) {

                                    if (InternetConnection.isInternetAvailable(getActivity())) {


                                        uploadMultipart();


                                    } else {

                                        UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

                                        UtilityMethods.showInternetDialog(getActivity());

                                    }


                                } else if (Constants.mMainActivity.thumbnail_rb != null) {


                                    CallUploadbrochureImage();


                                } else {


                                    UtilityMethods.showSuccessToast(getActivity(), "Product added successfully");

                                    ProductListFragment productListFragment = new ProductListFragment();
                                    Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");
                                }

                                // ProductListFragment productListFragment = new ProductListFragment();
                                //Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");


                            } else if (jsonObject.getString("error_code").equals("10")) {
                                UtilityMethods.UserInactivePopup(getActivity());

                            } else if (jsonObject.getString("error_code").equals("11")) {

                                UtilityMethods.showWarningToast(getActivity(), "This product already exists");

                            } else {
                                UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                        UtilityMethods.tuchOn(relativeLayoutAddNewProduct);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                UtilityMethods.showInfoToast(getActivity(), "Sorry, Please Check your Internet Connection.");

                            } else if (error instanceof AuthFailureError) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, AuthFailureError.");
                            } else if (error instanceof ServerError) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, ServerError.");
                            } else if (error instanceof NetworkError) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, NetworkError.");
                            } else if (error instanceof ParseError) {

                            }
                            UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

                        } catch (Exception e) {
                            e.printStackTrace();
                            UtilityMethods.tuchOn(relativeLayoutAddNewProduct);
                        }


                    }
                }) {


            //* If you want to add more parameters with the image
            //* you can do it here
            //* here we have only one parameter with the image
            // which is tags
            // *
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fk_cat_id", mSelectedCategory);
                params.put("docCnt", image_count);
                params.put("fk_sub_cat_id", mSelectedSubCategory);
                params.put("fk_subp_cat_id", mSelectedSubSubCategory);
                params.put("product_name", edt_production_name.getText().toString());
                params.put("sales_type", mSelectedSeller);
                params.put("cost", edt_product_price.getText().toString());
                params.put("currency", mCurrency);
                params.put("modeofpay", mSelectedModeOfPaymentPk_id);
                params.put("pro_specification", jsonArray.toString());

                Log.e("check", "sandeep=====>" + jsonArray.toString());

                params.put("modeofsupply", mSelectedModeOfSupplyPk_id);
                params.put("yotube_link", edt_video_link.getText().toString());
                params.put("short_description", edt_short_description.getText().toString());
                params.put("keywords", edt_keywords.getText().toString());
                params.put("additional_features", edt_additional_feature.getText().toString());
                params.put("delivery_time", edt_delivery_time.getText().toString());
                params.put("packing_details", edt_packing_details.getText().toString());
                params.put("production_capacity", edt_production_capacity.getText().toString());
                params.put("port_of_supply", edt_port_of_supply.getText().toString());
                params.put("minimum_order_quantity", edt_minimum_order_quantity.getText().toString());
                params.put("sample_piece", edt_sample_piece.getText().toString());
                params.put("summernotecms", edt_description.getText().toString());
                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

                Log.e("params", "params " + params);
                System.out.println("update profile Parameters is :-" + params);
                return params;
            }

            // Here we are passing image by renaming it with a unique name
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                System.out.println("Size of Parameters in start :-" + params.size());
                Log.e("check", "=====>" + Constants.mMainActivity.thumbnail_r);


                long imagename = System.currentTimeMillis();

                for (int i = 0; i < mAddProductImagesModelArrayList.size(); i++) {


                    if (mAddProductImagesModelArrayList.get(i).getDocumentBitmap() != null) {
                        params.put("product_image_" + i, new DataPart(imagename + ".png", getFileDataFromDrawable(mAddProductImagesModelArrayList.get(i).getDocumentBitmap())));
                    }

                }

                System.out.println("Size of Parameters in if :-" + params.size());


                Log.e("params", "=======>" + params.toString());

                return params;

            }


        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(
                120000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void CallUploadbrochureImage() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_BROCHURE_IMAGE_UPDATE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("Response", new String(response.data));
                        try {

                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            if (jsonObject.getString("error_code").equals("1")) {

                                UtilityMethods.showSuccessToast(getActivity(), "Product added successfully");

                                ProductListFragment productListFragment = new ProductListFragment();
                                Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");


                            } else if (jsonObject.getString("error_code").equals("10")) {
                                UtilityMethods.UserInactivePopup(getActivity());

                            } else {
                                // UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                        UtilityMethods.tuchOn(relativeLayoutAddNewProduct);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                UtilityMethods.showInfoToast(getActivity(), "Sorry, Please Check your Internet Connection.");

                            } else if (error instanceof AuthFailureError) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, AuthFailureError.");
                            } else if (error instanceof ServerError) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, ServerError.");
                            } else if (error instanceof NetworkError) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, NetworkError.");
                            } else if (error instanceof ParseError) {

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

                    }
                }) {


            //* If you want to add more parameters with the image
            //* you can do it here
            //* here we have only one parameter with the image
            // which is tags
            // *
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("product_id", productid);

                System.out.println("update profile Parameters is :-" + params);
                return params;
            }


            // Here we are passing image by renaming it with a unique name
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                System.out.println("Size of Parameters in start :-" + params.size());

                long imagename = System.currentTimeMillis();

                params.put("fileBrochure", new DataPart(imagename + ".png", getFileDataFromDrawable(Constants.mMainActivity.thumbnail_rb)));
                System.out.println("Size of Parameters in if brochure :-" + params.size());
                //return params;

                return params;

            }


        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(
                120000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    //Method to show options for camera anf gallery
    public void ShowCameraGallryOption(int pos) {

        position = pos;

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Select image");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dialog.cancel();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        /*
                         * File photo = new
                         * File(Environment.getExternalStorageDirectory(),
                         * "Pic.jpg"); intent.putExtra(MediaStore.EXTRA_OUTPUT,
                         * Uri.fromFile(photo)); imageUri = Uri.fromFile(photo);
                         */
                        // startActivityForResult(intent,TAKE_PICTURE);

                        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Log.e("test", "file->" + fileUri);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);


                        intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intents, TAKE_PICTURE);


                    }
                });

        builder1.setNegativeButton(
                "Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Picture"),
                                SELECT_PICTURE);

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    //----------------------------------camera and gallery code start ------------------------------------------------------------------
    public Uri getOutputMediaFileUri(int type) {
        Log.e("test", "type==>>" + type);
        // return Uri.fromFile(getOutputMediaFile(type));
        //   return FileProvider.getUriForFile(getActivity(), getApplicationContext().getPackageName() + ".my.package.name.provider", getOutputMediaFile(type));


        return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.e("inSampleSize", "inSampleSize______________in storage"
                + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, getActivity() will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
    }


    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            source.recycle();
            Date d = new Date();
            CharSequence s = DateFormat
                    .format("MM-dd-yy-hh-mm-ss", d.getTime());
            String fullPath = Environment.getExternalStorageDirectory()
                    + "/Agropost/" + s.toString() + ".jpg";
            if ((fullPath != null) && (new File(fullPath).exists())) {
                new File(fullPath).delete();
            }
            bitmap = null;
            err.printStackTrace();
        }
        return bitmap;
    }


    private void previewCapturedImage() {
        try {
            // hide video preview

            //profileImage.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);


            // rotated
            Constants.mMainActivity.thumbnail_r = imageOreintationValidator(bitmap, fileUri.getPath());

            //iv_image_one.setBackground(null);
            //iv_image_one.setImageBitmap(Constants.mMainActivity.thumbnail_r);

         /*   mAddProductImagesModelArrayList.get(position).setDocumentBitmap(Constants.mMainActivity.thumbnail_r);
            mAdapterAddProductImages.notifyDataSetChanged();*/
            // IsImageSet = true;


            GetCroppedImage();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void previewCapturedImageForBrocher() {
        try {
            // hide video preview

            //profileImage.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

           /* Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500,
                    false);
*/
            // rotated
            Constants.mMainActivity.thumbnail_rb = imageOreintationValidator(bitmap, fileUri.getPath());

            //ivProfilePic.setBackground(null);
            //ivProfilePic.setImageBitmap(Constants.mMainActivity.thumbnail_r);
            // IsImageSet = true;


            GetCroppedImageForBrocher();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    //Method to get cropped image
    private void GetCroppedImage() {

        Intent intent = new Intent(getActivity(), SquareCropActivity.class);
        startActivityForResult(intent, CROPPED_IMAGE);
    }

    //Method to get cropped image
    private void GetCroppedImageForBrocher() {

        Intent intent = new Intent(getActivity(), SquareCropBorcherActivity.class);
        startActivityForResult(intent, CROPPED_IMAGE_BROCHURE);
    }


    public void uploadMultipart() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        //getting the actual path of the pdf
        String path = FilePath.getPath(getActivity(), filePath);

        Log.e("start", "document");

        Log.e("path", "path " + path);
        //Uploading code
        if (path == null) {

            Log.e("check", "path" + path);

            UtilityMethods.showInfoToast(getActivity(), "Please move your .pdf file to internal storage and retry");

        } else try {
            String uploadId = UUID.randomUUID().toString();

            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Constants.URL_BROCHURE_UPDATE)
                    .addFileToUpload(path, "fileBrochure") //Adding file
                    // .addParameter("teacher_id", mSessionManager.getStringData(Constants.KEY_TEACHER_ID)) //Adding text parameter to the request
                    .addParameter("product_id", productid) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            exc.printStackTrace();
            UtilityMethods.showInfoToast(getActivity(), exc.getMessage());
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                UtilityMethods.showInfoToast(getActivity(), "Permission granted now you can read the storage");
            } else {
                //Displaying another toast if permission is not granted
                UtilityMethods.showInfoToast(getActivity(), "Oops you just denied the permission");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(getActivity());
    }

    @Override
    public void onProgress(int progress) {

        Log.e("progress", "progress " + progress);
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

        Log.e("uploadedBytes", "uploadedBytes " + uploadedBytes);
        Log.e("totalBytes", "totalBytes " + totalBytes);
    }

    @Override
    public void onError(Exception exception) {
        UtilityMethods.showInfoToast(getActivity(), "" + exception.toString());
        Log.e("exception", "exception " + exception.toString());
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        Log.e("serverResponseCode", "serverResponseCode " + serverResponseCode);
        try {
            String str = new String(serverResponseBody, "UTF-8");
            Log.e("serverResponseBody", "serverResponseBody[] " + str);


            try {
                JSONObject jsonObj = new JSONObject(str);

                if (jsonObj.getString("error_code").equals("1")) {

                    UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

                    UtilityMethods.showSuccessToast(getActivity(), "Product added successfully");

                    ProductListFragment productListFragment = new ProductListFragment();
                    Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");


                } else {
                    UtilityMethods.showInfoToast(getActivity(), "" + jsonObj.getString("message"));

                }

            } catch (Exception e) {
                e.printStackTrace();
                //UtilityMethods.showInfoToast(getActivity(), "Server Error");

            }
            UtilityMethods.tuchOn(relativeLayoutAddNewProduct);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled() {
        Log.e("onCancelled", "onCancelled ");
    }


    @Override
    public void onClick(View view) {

    }

    //method to show file chooser
    private void showFileChooser() {
        // Intent intent = new Intent();
        // intent.setType("*/*");
        //   intent.setAction(Intent.ACTION_GET_CONTENT);
        //  startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_DOC_REQUEST);

        String[] mimeTypes = {"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, ""), PICK_DOC_REQUEST);


    }

    //Method to get currency
    private void getCurrency() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_CURRENCY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getCurrency -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        JSONObject jsonObjectcurrency = jsonObject.getJSONObject("currency");


                        mCurrency = jsonObjectcurrency.getString("currency");


                        tv_inr.setText(mCurrency);


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

                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

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

    //Method to get image Count
    private void getImageCount() {

        UtilityMethods.tuchOff(relativeLayoutAddNewProduct);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_IMAGE_COUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getImageCount -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        image_count = jsonObject.getString("image_count");

                        mAddProductImagesModelArrayList.clear();

                        for (int i = 0; i < Integer.parseInt(image_count); i++) {

                            AddProductImagesModel addProductImagesModel = new AddProductImagesModel(null);
                            mAddProductImagesModelArrayList.add(addProductImagesModel);

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

                mAdapterAddProductImages.notifyDataSetChanged();
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);


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
                UtilityMethods.tuchOn(relativeLayoutAddNewProduct);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

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

    private boolean validation() {


        /*String check_image = "fill";


        for (int i = 0; i < mAddProductImagesModelArrayList.size(); i++) {

            if (mAddProductImagesModelArrayList.get(i).getDocumentBitmap() == null) {

                check_image = "empty";

            }

        }*/

        Log.e("check", "mSpecification.size() -------->" + jsonArray.length());


        /*if (check_image.equalsIgnoreCase("empty")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select product images");
            return false;
        } else*/
        if (mSelectedCategory.equalsIgnoreCase("") || mSelectedCategory.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select category");
            return false;
        } else if (mSelectedSubCategory.equalsIgnoreCase("") || mSelectedSubCategory.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select sub category");
            return false;
        } else if (mSelectedSubSubCategory.equalsIgnoreCase("") || mSelectedSubSubCategory.equalsIgnoreCase("0")) {
            UtilityMethods.showInfoToast(getActivity(), "Please select sub subcategory");
            return false;
        } else if (edt_production_name.getText().toString().equalsIgnoreCase("")) {
            edt_production_name.setError("Please enter product name");
            edt_production_name.requestFocus();
            return false;
        } else if (edt_product_price.getText().toString().equalsIgnoreCase("0") || edt_product_price.getText().toString().equalsIgnoreCase("00") || edt_product_price.getText().toString().equalsIgnoreCase("000")) {
            edt_product_price.setError("Enter valid price");
            edt_product_price.requestFocus();
            return false;
        } else if (!edt_video_link.getText().toString().equals("") && edt_video_link.getText().toString().length() < 10) {

            edt_video_link.setError("Please enter valid youtube link");
            edt_video_link.requestFocus();
            return false;

        } else if (!edt_minimum_order_quantity.getText().toString().equalsIgnoreCase("") && (edt_minimum_order_quantity.getText().toString().equalsIgnoreCase("0"))) {
            edt_minimum_order_quantity.setError("Enter valid quantity");
            edt_minimum_order_quantity.requestFocus();
            return false;
        } else if (edt_short_description.getText().toString().equalsIgnoreCase("")) {
            edt_short_description.setError("Please enter short description");
            edt_short_description.requestFocus();
            return false;
        } /*else if (mSpecification.get(0).getSpecification().equals("0") || mSpecification.get(0).getType().equals("0") || mSpecification.get(0).getSpecificationvalue().equals("")) {

            UtilityMethods.showInfoToast(getActivity(), "Please select product specification");

            return false;
        }*/
        return true;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {

        if (bitmap != null) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            System.out.println(" with image:- " + byteArrayOutputStream.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            System.out.println(" with image:- " + byteArrayOutputStream.toByteArray());
            return byteArrayOutputStream.toByteArray();

        }


    }

    //to display selected course
    public void getModeofPayCount() {
        mSelectedModeOfPayment = "";
        mSelectedModeOfPaymentPk_id = "";
        for (int i = 0; i < mModeOfPaymentModelArrayList.size(); i++) {

            if (mModeOfPaymentModelArrayList.get(i).isSelected()) {

                //tvSelectedCourses.setVisibility(i);
                Log.e("count", "===========>" + i);
                if (mSelectedModeOfPayment.length() == 0) {
                    mSelectedModeOfPayment = mModeOfPaymentModelArrayList.get(i).getClass_name();
                    mSelectedModeOfPaymentPk_id = mModeOfPaymentModelArrayList.get(i).getPk_id();
                } else {
                    mSelectedModeOfPayment = mSelectedModeOfPayment + ", " + mModeOfPaymentModelArrayList.get(i).getClass_name();
                    mSelectedModeOfPaymentPk_id = mSelectedModeOfPaymentPk_id + ", " + mModeOfPaymentModelArrayList.get(i).getPk_id();

                }

            } else {
                Log.e("count", "not selected===========>" + i);
            }

        }
        tv_selected_mode_of_payment.setText(mSelectedModeOfPayment);
    }

    //to display selected course
    public void getModeofSupplyCount() {
        mSelectedModeOfSupply = "";
        mSelectedModeOfSupplyPk_id = "";
        for (int i = 0; i < mModeOfSupplyModelArrayList.size(); i++) {

            if (mModeOfSupplyModelArrayList.get(i).isSelected()) {

                //tvSelectedCourses.setVisibility(i);
                Log.e("count", "===========>" + i);
                if (mSelectedModeOfSupply.length() == 0) {
                    mSelectedModeOfSupply = mModeOfSupplyModelArrayList.get(i).getClass_name();
                    mSelectedModeOfSupplyPk_id = mModeOfSupplyModelArrayList.get(i).getPk_id();
                } else {
                    mSelectedModeOfSupply = mSelectedModeOfSupply + ", " + mModeOfSupplyModelArrayList.get(i).getClass_name();
                    mSelectedModeOfSupplyPk_id = mSelectedModeOfSupplyPk_id + ", " + mModeOfSupplyModelArrayList.get(i).getPk_id();
                }

            } else {
                Log.e("count", "not selected===========>" + i);
            }

        }
        tv_selected_mode_of_supply.setText(mSelectedModeOfSupply);

        Log.e("check", "===========>" + mSelectedModeOfSupply);
    }

    //Hide Keyboard
    public void hideKeyboard() {


        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    public void ChooseCertificateDialogForDocument(Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Choose option");
        builder.setCancelable(true);

        builder.setPositiveButton(

                R.string.Image,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ShowCameraGallryOptionForBrocher();


                    }
                });

        builder.setNegativeButton(

                R.string.pdf_doc,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        showFileChooser();

                    }
                });


        AlertDialog alert11 = builder.create();
        alert11.setCanceledOnTouchOutside(true);
        alert11.show();
    }

    //Method to show options for camera anf gallery
    private void ShowCameraGallryOptionForBrocher() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Select image");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dialog.cancel();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        /*
                         * File photo = new
                         * File(Environment.getExternalStorageDirectory(),
                         * "Pic.jpg"); intent.putExtra(MediaStore.EXTRA_OUTPUT,
                         * Uri.fromFile(photo)); imageUri = Uri.fromFile(photo);
                         */
                        // startActivityForResult(intent,TAKE_PICTURE);

                        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Log.e("test", "file->" + fileUri);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);


                        intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intents, TAKE_PICTURE_BROCHURE);


                    }
                });

        builder1.setNegativeButton(
                "Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Picture"),
                                SELECT_PICTURE_BROCHURE);

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
