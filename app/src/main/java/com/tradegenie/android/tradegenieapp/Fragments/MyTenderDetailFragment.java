package com.tradegenie.android.tradegenieapp.Fragments;


import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTenderDetailFragment extends Fragment {


    @BindView(R.id.txt_tender_name)
    TextView txtTenderName;
    @BindView(R.id.txt_tender_id)
    TextView txtTenderId;
    @BindView(R.id.txt_created_date)
    TextView txtCreatedDate;
    @BindView(R.id.txt_description)
    TextView txtDescription;
    @BindView(R.id.txt_tender_link)
    TextView txtTenderLink;
    @BindView(R.id.txt_tender_cost)
    TextView txtTenderCost;
    @BindView(R.id.linearLayoutTaxRow)
    LinearLayout linearLayoutTaxRow;
    @BindView(R.id.tv_buyer_name)
    TextView tvBuyerName;
    @BindView(R.id.tv_buyer_mobileno)
    TextView tvBuyerMobileno;
    @BindView(R.id.tv_buyer_buyer_email)
    TextView tvBuyerBuyerEmail;
    @BindView(R.id.tv_buyer_address)
    TextView tvBuyerAddress;
    @BindView(R.id.linear_layout_call)
    LinearLayout linearLayoutCall;
    @BindView(R.id.linear_layout_message)
    LinearLayout linearLayoutMessage;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.txt_payment_type_transaction_id)
    TextView txtPaymentTypeTransactionId;
    @BindView(R.id.txt_payment_date)
    TextView txtPaymentDate;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.linear_layout_down_load)
    LinearLayout linearLayoutDownLoad;
    Unbinder unbinder;
    @BindView(R.id.ProgressBarPostEnqList)
    RelativeLayout ProgressBarPostEnqList;

    SessionManager mSessionManager;

    String tender_id, From, Contact_person_id, invoice_link, chatlisting_pk_id;
    @BindView(R.id.Lnr_contact_payment_details)
    LinearLayout LnrContactPaymentDetails;
    @BindView(R.id.Lnr_doc)
    LinearLayout LnrDoc;
    @BindView(R.id.Lnr_doc_main)
    LinearLayout LnrDocMain;
    @BindView(R.id.Lnr_payment)
    LinearLayout LnrPayment;
    @BindView(R.id.lbl_paid_status)
    TextView lblPaidStatus;
    @BindView(R.id.linear_layout_description)
    LinearLayout linearLayoutDescription;
    private DownloadManager downloadManager;


    ArrayList<String> mDocArray;


    public MyTenderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_tender_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        mDocArray = new ArrayList<>();
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());
        tender_id = getArguments().getString("tender_id");
        From = getArguments().getString("From");

        Constants.mMainActivity.setToolBarName("My Tender");
        if (InternetConnection.isInternetAvailable(getActivity())) {
            UtilityMethods.tuchOff(ProgressBarPostEnqList);
            GetTenderDetail();
        } else {
            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }


    //Method to get my tender detail
    private void GetTenderDetail() {


        // StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MY_TENDER_DETAIL, new Response.Listener<String>() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MY_TENDER_DETAIL_MY_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "URL_MY_TENDER_DETAIL_MY_POST -->>" + response);

                try {

                    final JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (jsonObject.getString("paid_status").equals("2")) {
                            LnrContactPaymentDetails.setVisibility(GONE);
                            //  LnrDocMain.setVisibility(View.GONE);

                        } else {
                            LnrContactPaymentDetails.setVisibility(VISIBLE);
                        }

                        chatlisting_pk_id = jsonObject.getString("chatlisting_pk_id");
                        Contact_person_id = jsonObject.getString("message_to");
                        invoice_link = jsonObject.getString("invoice_link");
                        txtTenderName.setText(jsonObject.getString("tender_name"));
                        txtDescription.setText(jsonObject.getString("descripation"));
                        txtTenderLink.setText(jsonObject.getString("tender_link"));
                        txtPrice.setText(jsonObject.getString("currency") + " " + jsonObject.getString("grand_total"));
                        txtTenderCost.setText(jsonObject.getString("currency") + " " + jsonObject.getString("cost"));
                        txtPaymentTypeTransactionId.setText("Payment type: Online Transaction ID: " + jsonObject.getString("transactionid"));


                        try {
                            txtCreatedDate.setText(ConvertDate(jsonObject.getString("created_date")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            txtPaymentDate.setText("Payment Date: " + ConvertDate(jsonObject.getString("payment_date")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        txtTenderId.setText("Tender ID - " + jsonObject.getString("tender_id"));


                        tvBuyerName.setText(jsonObject.getString("contact_name"));
                        tvBuyerAddress.setText(jsonObject.getString("contact_address"));
                        tvBuyerBuyerEmail.setText(jsonObject.getString("contact_email"));
                        tvBuyerMobileno.setText(jsonObject.getString("contact_mobile"));


                        if (From.equals("MyPostedTenders")) {
                            LnrPayment.setVisibility(GONE);
                            //  LnrDocMain.setVisibility(View.GONE);

                        }

                        //Get and show documents ---------------------------------------------
                        JSONArray tender_doc = jsonObject.getJSONArray("tender_doc");
                        for (int j = 0; j < tender_doc.length(); j++) {
                            JSONObject tenderObj = tender_doc.getJSONObject(j);

                            mDocArray.add(jsonObject.getString("tender_doc_url") + tenderObj.getString("doc_name"));
                        }


                        if (mDocArray.size() > 0) {
                            for (int k = 0; k < mDocArray.size(); k++) {

                                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = vi.inflate(R.layout.row_document, null);


                                TextView txt_doc_name = view.findViewById(R.id.txt_doc_name);
                                RelativeLayout rel_download = view.findViewById(R.id.rel_download);


                                txt_doc_name.setText(mDocArray.get(k).substring(mDocArray.get(k).lastIndexOf("/") + 1));

                                final String invoice_download = mDocArray.get(k);
                                rel_download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (InternetConnection.isInternetAvailable(getActivity())) {
                                            UtilityMethods.showInfoToastDownload(getActivity(), "Downloading...");

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                                // Do something for Pie and above versions
                                                new DownloadTask(getActivity(), invoice_download);
                                            } else {
                                                // do something for phones running an SDK before Pie
                                                getDownlodFile(invoice_download);
                                            }

                                        } else {
                                            UtilityMethods.showInternetDialog(getActivity());
                                        }

                                    }
                                });

                                LnrDoc.addView(view);


                            }
                        }

                        try {
                            String pay_type_status = jsonObject.getString("pay_type_status"); //pay_type_status= 1 (for cash) ,2( for Online), 3 (for subscription)
                            linearLayoutDescription.setVisibility(VISIBLE);
                            if (pay_type_status.equals("1")) {
                                String paid_status = jsonObject.getString("paid_status");   //paid_status=1(paid), 2(not paid)
                                if (paid_status.equals("2")) {
                                    lblPaidStatus.setText("Unpaid");
                                    linearLayoutDescription.setVisibility(GONE);
                                } else {
                                    lblPaidStatus.setText(getString(R.string.lbl_paid));
                                    linearLayoutDescription.setVisibility(VISIBLE);
                                }
                                txtPaymentTypeTransactionId.setText("Payment type : Cash");
                                txtPaymentDate.setVisibility(GONE);
                                linearLayoutDownLoad.setVisibility(GONE);
                            } else if (pay_type_status.equals("3")) {
                                txtPaymentTypeTransactionId.setText("Payment type : Subscription");
                                txtPaymentDate.setVisibility(GONE);
                                linearLayoutDownLoad.setVisibility(GONE);
                            } else {
                                txtPaymentTypeTransactionId.setText("Payment type : Online" + "  " + "Transaction ID : " + jsonObject.getString("transactionid"));
                                txtPaymentDate.setVisibility(VISIBLE);
                                linearLayoutDownLoad.setVisibility(VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // ---------------------------------------------
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        // UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                UtilityMethods.tuchOn(ProgressBarPostEnqList);


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
                UtilityMethods.tuchOn(ProgressBarPostEnqList);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("tenderid", tender_id);

                Log.e("params", "params" + Constants.URL_MY_TENDER_DETAIL_MY_POST + " params ---> " + params.toString());
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

    //Method to convert date
    private String ConvertDate(String created_at) {

        DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = originalFormat.parse(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = targetFormat.format(date);

        return formattedDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.linear_layout_call, R.id.linear_layout_message, R.id.linear_layout_down_load, R.id.txt_tender_link})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_call:


                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tvBuyerMobileno.getText().toString().trim()));
                startActivity(callIntent);

                break;
            case R.id.txt_tender_link:


                if (!txtTenderLink.getText().toString().trim().equals("")) {
                    String StrURL = "";
                    if (!txtTenderLink.getText().toString().trim().startsWith("http://") && !txtTenderLink.getText().toString().trim().startsWith("https://")) {
                        StrURL = String.valueOf(Uri.parse("http://" + txtTenderLink.getText().toString().trim()));
                    } else {
                        StrURL = txtTenderLink.getText().toString().trim();
                    }

                    if (!URLUtil.isValidUrl(StrURL.toLowerCase())) {
                        Toast.makeText(getActivity(), " This is not a valid link", Toast.LENGTH_LONG).show();
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StrURL));
                        startActivity(browserIntent);
                    }
                }

                break;
            case R.id.linear_layout_message:


                ChatPersnoalFragment chatPersnoalFragment = new ChatPersnoalFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Chat_With", Contact_person_id);
                bundle.putString("pk_id", chatlisting_pk_id);
                chatPersnoalFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(chatPersnoalFragment, "ChatPersnoalFragment");


                break;
            case R.id.linear_layout_down_load:

                if (InternetConnection.isInternetAvailable(getActivity())) {
                    UtilityMethods.showInfoToastDownload(getActivity(), "Downloading...");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        // Do something for Pie and above versions
                        new DownloadTask(getActivity(), invoice_link);
                    } else {
                        // do something for phones running an SDK before Pie
                        getDownlodFile(invoice_link);
                    }


                } else {
                    UtilityMethods.showInternetDialog(getActivity());
                }

                break;
        }
    }


    //Method to download invoice -----------------------------------------------

    class DownloadTask {
        private static final String TAG = "Download Task";
        private Context context;

        private String downloadUrl = "", downloadFileName = "";
        private ProgressDialog progressDialog;

        public DownloadTask(Context context, String downloadUrl) {
            this.context = context;

            this.downloadUrl = downloadUrl;


            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL

            File myFile = new File(downloadFileName);
            Log.e("downloadFileName", "downloadFileName " + downloadFileName);
            Log.e("myFile", "myFile " + myFile);

            if (myFile.exists()) {
                Log.e("path", "*******is exists******");
                Log.e("path", myFile.toString());
                Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", myFile);
                Log.e("uri", "uri-->>>" + uri.getPath());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (downloadUrl.toString().contains(".doc") || downloadUrl.toString().contains(".docx")) {
                    // Word document
                    intent.setDataAndType(uri, "application/msword");
                    //new
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else if (downloadUrl.toString().contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(uri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else if (downloadUrl.toString().contains(".ppt") || downloadUrl.toString().contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                    //new
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else if (downloadUrl.toString().contains(".xls") || downloadUrl.toString().contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                    //new
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                getActivity().startActivity(intent);

            } else {
                Log.e(TAG, downloadFileName);

                //Start Downloading Task
                new DownloadingTask().execute();
            }
        }

        private class DownloadingTask extends AsyncTask<Void, Void, Void> {

            File apkStorage = null;
            File outputFile = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (outputFile != null) {
                        progressDialog.dismiss();

                        UtilityMethods.showInfoToast(context, "Invoice has been downloaded successfully..");


                        if (outputFile.exists()) {
                            Log.e("path", "*******is exists******");
                            Log.e("path", outputFile.toString());
                            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", outputFile);
                            Log.e("uri", "uri-->>>" + uri.getPath());
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (outputFile.toString().contains(".doc") || outputFile.toString().contains(".docx")) {
                                // Word document
                                intent.setDataAndType(uri, "application/msword");
                                //new
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else if (outputFile.toString().contains(".pdf")) {
                                // PDF file
                                intent.setDataAndType(uri, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else if (outputFile.toString().contains(".ppt") || outputFile.toString().contains(".pptx")) {
                                // Powerpoint file
                                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                                //new
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else if (outputFile.toString().contains(".xls") || outputFile.toString().contains(".xlsx")) {
                                // Excel file
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                //new
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            getActivity().startActivity(intent);

                        }


                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 3000);

                        Log.e(TAG, "Download Failed");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    //Change button text if exception occurs

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

                }


                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage());

                    }


                    //Get File if SD card is present
                    if (new CheckForSDCard().isSDCardPresent()) {
                        apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "Tradegenie");
                    } else
                        Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e(TAG, "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                }

                return null;
            }
        }
    }

    class CheckForSDCard {
        //Check If SD Card is present or not method
        public boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }

    //Methods for Download file
    public void getDownlodFile(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File myFile = new File(Constants.KEY_PATH_OF_QUATATION + "/" + fileName);
        // File myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + fileName);
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
            getActivity().startActivity(intent);

        } else {
            Log.e("path", "*******Not exists******");
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
            request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);
            long refrance = downloadManager.enqueue(request);
        }

    }
//Method to download invoice -----------------------------------------------


}
