package com.tradegenie.android.tradegenieapp.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.Volley;
import com.tradegenie.android.tradegenieapp.Activity.SquareCropBorcherActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterCurrencyList;
import com.tradegenie.android.tradegenieapp.BuildConfig;
import com.tradegenie.android.tradegenieapp.DocUpload.FilePath;
import com.tradegenie.android.tradegenieapp.DocUpload.SingleUploadBroadcastReceiver;
import com.tradegenie.android.tradegenieapp.Models.CurrencyModel;
import com.tradegenie.android.tradegenieapp.Models.DataPart;
import com.tradegenie.android.tradegenieapp.Models.TenderDocModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;
import com.tradegenie.android.tradegenieapp.Utility.VolleyMultipartRequest;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewTenderFragment extends Fragment implements SingleUploadBroadcastReceiver.Delegate {

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    Unbinder unbinder;
    @BindView(R.id.edt_tender_name)
    EditText edtTenderName;
    @BindView(R.id.edt_tender_description)
    EditText edtTenderDescription;
    @BindView(R.id.edt_tender_link)
    EditText edtTenderLink;
    @BindView(R.id.edt_tender_cost)
    EditText edtTenderCost;
    @BindView(R.id.txt_add)
    TextView txtAdd;

    @BindView(R.id.tv_add_more)
    TextView tvAddMore;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.relativeLayoutProductListRejected)
    RelativeLayout relativeLayoutProductListRejected;


    SessionManager mSessionManager;

    ArrayList<TenderDocModel> mTenderDocArrayList;
    @BindView(R.id.Lnr_doc)
    LinearLayout LnrDoc;
    @BindView(R.id.spinner_currency)
    Spinner spinnerCurrency;
    @BindView(R.id.iv_view_spinner_currency)
    View ivViewSpinnerCurrency;
    @BindView(R.id.iv_drop_down_currency)
    ImageView ivDropDownCurrency;
    @BindView(R.id.rel_currency)
    RelativeLayout relCurrency;


    private int PICK_DOC_REQUEST = 1;
    //camera and gallery
    private Uri fileUri;
    private static final int TAKE_PICTURE = 1;
    private static final int CROPPED_IMAGE = 3;
    private static final String IMAGE_DIRECTORY_NAME = "TradeGenie";
    //close camera and gallery
    private Uri filePath = null;

    //camera and gallery
    private static final int TAKE_PICTURE_BROCHURE = 11, SELECT_PICTURE_BROCHURE = 22;
    private static final int CROPPED_IMAGE_BROCHURE = 33;

    //close camera and gallery


    private ArrayList<CurrencyModel> mCurrencyModelArrayList;
    private AdapterCurrencyList mAdapterCurrencyList;
    private String mSelectedCurrency = "Select";


    public AddNewTenderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_tender, container, false);

        Constants.mMainActivity.setToolBarName(getString(R.string.post_tender));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        unbinder = ButterKnife.bind(this, view);

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


        mTenderDocArrayList = new ArrayList<>();
        mSessionManager = new SessionManager(getActivity());

        if (InternetConnection.isInternetAvailable(getActivity())) {
            UtilityMethods.tuchOff(relativeLayoutProductListRejected);
            ClearAllOldTenderFiles();
        } else {
            UtilityMethods.showInternetDialog(getActivity());
        }
        return view;
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
        UtilityMethods.tuchOn(relativeLayoutProductListRejected);

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

                    UtilityMethods.tuchOn(relativeLayoutProductListRejected);

                    //  UtilityMethods.showSuccessToast(getActivity(), "Product added successfully");

                    int position = mTenderDocArrayList.size() - 1;
                    mTenderDocArrayList.get(position).setId(jsonObj.getString("doc_id"));
                    mTenderDocArrayList.get(position).setFilename(jsonObj.getString("image_name"));


                    RefreshDocList();

                } else {
                    UtilityMethods.showInfoToast(getActivity(), "" + jsonObj.getString("message"));

                }

            } catch (Exception e) {
                e.printStackTrace();
                //UtilityMethods.showInfoToast(getActivity(), "Server Error");

            }
            UtilityMethods.tuchOn(relativeLayoutProductListRejected);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled() {
        UtilityMethods.tuchOn(relativeLayoutProductListRejected);
        Log.e("onCancelled", "onCancelled ");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txt_add, R.id.tv_add_more, R.id.btn_submit, R.id.iv_drop_down_currency})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_drop_down_currency:

                spinnerCurrency.performClick();
                break;
            case R.id.txt_add:

                hideKeyboard();
                ChooseCertificateDialogForDocument(getActivity());

                break;
            case R.id.tv_add_more:

                hideKeyboard();
                ChooseCertificateDialogForDocument(getActivity());

                break;
            case R.id.btn_submit:


                if (Valid()) {

                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        UtilityMethods.tuchOff(relativeLayoutProductListRejected);
                        AddTender();
                    } else {
                        UtilityMethods.showInternetDialog(getActivity());
                    }

                }

                break;
        }
    }

    //Hide Keyboard
    public void hideKeyboard() {


        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    //Method to update details of user
    private void ClearAllOldTenderFiles() {

        {
            UtilityMethods.tuchOff(relativeLayoutProductListRejected);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_TENDER_ALL_FILES_DELETE,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.e("Response", new String(response.data));

                            try {

                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                if (jsonObject.getString("error_code").equals("1")) {


                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }

                            UtilityMethods.tuchOn(relativeLayoutProductListRejected);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {

                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    UtilityMethods.showToast(getActivity(), "Sorry, Please Check your Internet Connection.");

                                } else if (error instanceof AuthFailureError) {

                                    UtilityMethods.showToast(getActivity(), "Sorry, AuthFailureError.");
                                } else if (error instanceof ServerError) {

                                    UtilityMethods.showToast(getActivity(), "Sorry, ServerError.");
                                } else if (error instanceof NetworkError) {

                                    UtilityMethods.showToast(getActivity(), "Sorry, NetworkError.");
                                } else if (error instanceof ParseError) {

                                }
                                UtilityMethods.tuchOn(relativeLayoutProductListRejected);

                            } catch (Exception e) {
                                e.printStackTrace();
                                UtilityMethods.tuchOn(relativeLayoutProductListRejected);
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
                    params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    System.out.println("update profile Parameters is :-" + params);
                    return params;
                }


                // Here we are passing image by renaming it with a unique name
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    System.out.println("Size of Parameters in start :-" + params.size());
                    if (Constants.mMainActivity.thumbnail_rb != null) {
                        long imagename = System.currentTimeMillis();
                        params.put("documentUpload", new DataPart(imagename + ".png", getFileDataFromDrawable(Constants.mMainActivity.thumbnail_rb)));
                        System.out.println("update profile Parameters is :-" + params);
                        return params;
                    } else {
                        //   params=null;
                        params.remove("documentUpload");
                        System.out.println("update profile Parameters is :-" + params);
                        return params;
                    }


                }


            };

            //adding the request to volley
            Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(
                    120000,
                    1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }


    //Method to update details of user
    private void AddTenderImage() {

        {
            UtilityMethods.tuchOff(relativeLayoutProductListRejected);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_ADD_DOC,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.e("Response", new String(response.data));

                            try {

                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                if (jsonObject.getString("error_code").equals("1")) {


                                    int position = mTenderDocArrayList.size() - 1;
                                    mTenderDocArrayList.get(position).setId(jsonObject.getString("doc_id"));
                                    mTenderDocArrayList.get(position).setFilename(jsonObject.getString("image_name"));


                                    RefreshDocList();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }

                            UtilityMethods.tuchOn(relativeLayoutProductListRejected);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {

                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    UtilityMethods.showToast(getActivity(), "Sorry, Please Check your Internet Connection.");

                                } else if (error instanceof AuthFailureError) {

                                    UtilityMethods.showToast(getActivity(), "Sorry, AuthFailureError.");
                                } else if (error instanceof ServerError) {

                                    UtilityMethods.showToast(getActivity(), "Sorry, ServerError.");
                                } else if (error instanceof NetworkError) {

                                    UtilityMethods.showToast(getActivity(), "Sorry, NetworkError.");
                                } else if (error instanceof ParseError) {

                                }
                                UtilityMethods.tuchOn(relativeLayoutProductListRejected);

                            } catch (Exception e) {
                                e.printStackTrace();
                                UtilityMethods.tuchOn(relativeLayoutProductListRejected);
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
                    params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                    System.out.println("update profile Parameters is :-" + params);
                    return params;
                }


                // Here we are passing image by renaming it with a unique name
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    System.out.println("Size of Parameters in start :-" + params.size());
                    if (Constants.mMainActivity.thumbnail_rb != null) {
                        long imagename = System.currentTimeMillis();
                        params.put("documentUpload", new DataPart(imagename + ".png", getFileDataFromDrawable(Constants.mMainActivity.thumbnail_rb)));
                        System.out.println("update profile Parameters is :-" + params);
                        return params;
                    } else {
                        //   params=null;
                        params.remove("documentUpload");
                        System.out.println("update profile Parameters is :-" + params);
                        return params;
                    }


                }


            };

            //adding the request to volley
            Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(
                    120000,
                    1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
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

    public String getImagePath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();


        String path = null;

        if (cursor != null && cursor.moveToFirst()) {
            try {
                Log.e("cursor", "cursor " + cursor);
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                //String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return path;
    }


    public void uploadMultipart() {

        Log.e("uploadMultipart","uploadMultipart");
        UtilityMethods.tuchOff(relativeLayoutProductListRejected);

        //getting the actual path of the pdf
        String path = FilePath.getPath(getActivity(), filePath);
        Log.e("filePath", "filePath " + filePath);

        Log.e("start", "document");

        Log.e("path", "path " + path);
        Log.e("VERSION", "VERSION " + android.os.Build.VERSION.SDK_INT);
        Log.e("VERSION_CODES", "VERSION_CODES " + android.os.Build.VERSION_CODES.O);


     /*   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            path = path.replace("/storage/emulated/0/Download", "/storage/emulated/0/storage/emulated/0/Download");
            //working for my device
            Log.e("path", "path " + path);
        }*/





    /*    String path = getImagePath(filePath);
        Log.e("filePath", "filePath " + filePath);
        Log.e("path", "path " + path);
*/




       /* path = path.substring(path.lastIndexOf('/'), path.length());//Create file name by picking download file name from URL
        File myFile = new File(path);

        Log.e("downloadFileName","downloadFileName "+path);
        Log.e("myFile","myFile "+myFile);
        Log.e("myFile","myFile.exists() "+myFile.exists());


        try {
            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", myFile);
            Log.e("uri", "uri-->>>" + uri.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
/*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivity(intent);

*/

        //Uploading code
        if (path == null) {

            Log.e("check", "path" + path);

            UtilityMethods.showInfoToast(getActivity(), "Please move your .pdf file to internal storage and retry");

        } else try {
            String uploadId = UUID.randomUUID().toString();

            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            //  path=path.replace("/storage/emulated/0/Download/","/Internal StorageInternal Storage/Download/");
            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Constants.URL_ADD_DOC)
                    .addFileToUpload(path, "documentUpload") //Adding file
                    // .addParameter("teacher_id", mSessionManager.getStringData(Constants.KEY_TEACHER_ID)) //Adding text parameter to the request
                    .addParameter("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID)) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            exc.printStackTrace();
           // UtilityMethods.showInfoToast(getActivity(), exc.getMessage());


           // UtilityMethods.tuchOn(relativeLayoutProductListRejected);


            path = path.replace("/storage/emulated/0/Download", "/storage/emulated/0/storage/emulated/0/Download");
            //working for my device
            Log.e("path", "path " + path);



            //Uploading code
            if (path == null) {

                Log.e("check", "path" + path);

                UtilityMethods.showInfoToast(getActivity(), "Please move your .pdf file to internal storage and retry");

            } else try {
                String uploadId = UUID.randomUUID().toString();

                uploadReceiver.setDelegate(this);
                uploadReceiver.setUploadID(uploadId);

                //  path=path.replace("/storage/emulated/0/Download/","/Internal StorageInternal Storage/Download/");
                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, Constants.URL_ADD_DOC)
                        .addFileToUpload(path, "documentUpload") //Adding file
                        // .addParameter("teacher_id", mSessionManager.getStringData(Constants.KEY_TEACHER_ID)) //Adding text parameter to the request
                        .addParameter("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID)) //Adding text parameter to the request
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exce) {
                exce.printStackTrace();
                UtilityMethods.showInfoToast(getActivity(), exce.getMessage());

                 UtilityMethods.tuchOn(relativeLayoutProductListRejected);


            }
        }
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

    //Method to add tender
    private void AddTender() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SUBMIT_TENDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "response " + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        UtilityMethods.showSuccessToast(getActivity(), "Tender has been submitted successfully.");

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();

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

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("fk_country_id", mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY_ID));
                params.put("tender_name", edtTenderName.getText().toString().trim());
                params.put("descripation", edtTenderDescription.getText().toString().trim());
                params.put("tender_link", edtTenderLink.getText().toString().trim());
                params.put("cost", edtTenderCost.getText().toString().trim());
                params.put("currency", mSelectedCurrency);

                String documentUpload = "";
                for (int k = 0; k < mTenderDocArrayList.size(); k++) {
                    if (documentUpload.equals("")) {
                        documentUpload = mTenderDocArrayList.get(k).getId();
                    } else {
                        documentUpload = documentUpload + "," + mTenderDocArrayList.get(k).getId();
                    }
                }
                params.put("documentUpload", documentUpload);


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

    //Method to delete doc which are selected
    private void DeleteDoc(final String id, final String filename, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CLEAR_TENDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "response " + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        mTenderDocArrayList.remove(position);

                        RefreshDocList();
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

                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("doc_id", id);
                params.put("doc_name", filename);


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

    //MEthod to check whether entered data is valid or not
    private boolean Valid() {

        if (edtTenderName.getText().toString().trim().equals("")) {
            edtTenderName.setError(getString(R.string.valid_tender_name));
            edtTenderName.requestFocus();
            return false;
        } else if (edtTenderDescription.getText().toString().trim().equals("")) {
            edtTenderDescription.setError(getString(R.string.valid_description));
            edtTenderDescription.requestFocus();
            return false;
        } else if (edtTenderLink.getText().toString().trim().equals("")) {
            edtTenderLink.setError(getString(R.string.valid_tender_link));
            edtTenderLink.requestFocus();
            return false;
        } else if (!Patterns.WEB_URL.matcher(edtTenderLink.getText().toString().trim()).matches()) {
            edtTenderLink.setError(getString(R.string.invalid_tender_link));
            edtTenderLink.requestFocus();
            return false;
        } else if (mSelectedCurrency.equals("Select")) {
            UtilityMethods.showInfoToast(getActivity(), getString(R.string.select_currency));
            return false;
        } else if (edtTenderCost.getText().toString().equals("")) {
            edtTenderCost.setError(getString(R.string.valid_cost));
            edtTenderCost.requestFocus();
            return false;
        } else if (mTenderDocArrayList.size() == 0) {
            UtilityMethods.showInfoToast(getActivity(), getString(R.string.select_doc));
            return false;
        }
        return true;
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

            // txt_upload_file_name.setText(filename);

            TenderDocModel tenderDocModel = new TenderDocModel();
            tenderDocModel.setFilename(filename);
            mTenderDocArrayList.add(tenderDocModel);


            //  RefreshDocList();

            Log.e("filePath","filePath "+filePath);

            uploadMultipart();

        }

        switch (requestCode) {


            case SELECT_PICTURE_BROCHURE:


                if (resultCode == RESULT_OK) {
                    if (data != null) {


                        try {

                            filePath = data.getData();
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
                    // txt_upload_file_name.setText("One Brochure Selected");
                    TenderDocModel tenderDocModel = new TenderDocModel();
                    tenderDocModel.setFilename("img_tender");
                    mTenderDocArrayList.add(tenderDocModel);

                    AddTenderImage();
                    //RefreshDocList();
                    // uploadMultipart();
                }


                break;
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
    private void GetCroppedImageForBrocher() {

        Intent intent = new Intent(getActivity(), SquareCropBorcherActivity.class);
        startActivityForResult(intent, CROPPED_IMAGE_BROCHURE);
    }


    //MEthod to display documnet list
    private void RefreshDocList() {

        LnrDoc.removeAllViews();
        for (int i = 0; i < mTenderDocArrayList.size(); i++) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = mInflater.inflate(R.layout.row_add_document, null);

            TextView txt_doc_name = view.findViewById(R.id.txt_doc_name);
            ImageView img_delete = view.findViewById(R.id.img_delete);

            txt_doc_name.setText(mTenderDocArrayList.get(i).getFilename());

            final int finalI = i;
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (InternetConnection.isInternetAvailable(getActivity())) {
                        UtilityMethods.tuchOff(relativeLayoutProductListRejected);

                        DeleteDoc(mTenderDocArrayList.get(finalI).getId(), mTenderDocArrayList.get(finalI).getFilename(), finalI);

                    } else {
                        UtilityMethods.showInternetDialog(getActivity());
                    }
                }
            });


            LnrDoc.addView(view);
        }

        if (mTenderDocArrayList.size() == 0) {
            txtAdd.setVisibility(VISIBLE);
            tvAddMore.setVisibility(GONE);
        } else {
            txtAdd.setVisibility(GONE);
            tvAddMore.setVisibility(VISIBLE);
        }
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


}
