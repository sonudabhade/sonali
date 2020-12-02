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
import com.tradegenie.android.tradegenieapp.Models.TaxModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.URL_GET_LEADS;

public class MyLeadFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_lead_id)
    TextView tvLeadId;
    @BindView(R.id.tv_created_date)
    TextView tvCreatedDate;
    @BindView(R.id.tv_qty)
    TextView tvQty;
    @BindView(R.id.tv_price_range)
    TextView tvPriceRange;
    @BindView(R.id.tv_preferred)
    TextView tvPreferred;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_descripation)
    TextView tvDescripation;
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
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_transact_type)
    TextView tvTransactType;
    @BindView(R.id.tv_payment_date)
    TextView tvPaymentDate;
    @BindView(R.id.ProgressBarMyLeadDetailsView)
    RelativeLayout ProgressBarMyLeadDetailsView;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.linear_layout_message)
    LinearLayout linearLayoutMessage;
    @BindView(R.id.linear_layout_call)
    LinearLayout linearLayoutCall;
    @BindView(R.id.linear_layout_down_load)
    LinearLayout linearLayoutDownLoad;
    @BindView(R.id.lbl_paid_status)
    TextView lblPaidStatus;
    @BindView(R.id.ll_contact_details)
    LinearLayout llContactDetails;
    @BindView(R.id.linear_layout_description)
    LinearLayout linearLayoutDescription;
    private DownloadManager downloadManager;

    String fk_payment_id = "";


    private ArrayList<TaxModel> mTaxModelArrayList;
    private SessionManager mSessionManager;
    private String pk_id = "", fk_buyer_id = "", buyer_mobileno = "", chatlisting_pk_id = "";


    public MyLeadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_lead_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("My Lead");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());
        mTaxModelArrayList = new ArrayList<>();
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


        Bundle bundle = this.getArguments();
        if (bundle != null) {

            pk_id = bundle.getString("pk_id");

        }


        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            getMyEnquiryDetailView();


        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }


    //Method to get My Enquiry DetailView
    private void getMyEnquiryDetailView() {

        UtilityMethods.tuchOff(ProgressBarMyLeadDetailsView);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_LEADS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getNewEnquriyDetailView  -->>" + URL_GET_LEADS + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String currency = jsonObject.getString("currency");

                        fk_payment_id = jsonObject.getString("fk_payment_id");
                        tvPaymentDate.setText("Payment date: " + jsonObject.getString("payment_date"));
                        try {
                            if (jsonObject.getString("enquiry_type").equals("Product Requirement")) {
                                tvProductName.setText(jsonObject.getString("product_name"));
                            } else {
                                tvProductName.setText(jsonObject.getString("enquiry_type"));
                            }
                        }catch (Exception e){e.printStackTrace();}
                        tvLeadId.setText("Enquiry ID - " + jsonObject.getString("lead_id"));
                        tvCreatedDate.setText(jsonObject.getString("lead_date"));
                        tvQty.setText(jsonObject.getString("quantity"));
                        tvPriceRange.setText(jsonObject.getString("en_currency") + " " + jsonObject.getString("price_from") + " to " + jsonObject.getString("price_to"));
                        tvPreferred.setText(jsonObject.getString("prefered_type"));
                        tvType.setText(jsonObject.getString("type_of_buyer"));
                        tvDescripation.setText(jsonObject.getString("description"));
                        tvBuyerName.setText(jsonObject.getString("buyer_name"));
                        tvBuyerMobileno.setText(jsonObject.getString("buyer_mobileno"));
                        tvBuyerBuyerEmail.setText(jsonObject.getString("buyer_email"));
                        tvBuyerAddress.setText(jsonObject.getString("buyer_address"));
                        tvTransactType.setText("Payment type : " + jsonObject.getString("transact_type") + "  " + "Transaction ID : " + jsonObject.getString("transactionid"));
                        tvPrice.setText(jsonObject.getString("currency") + " " + jsonObject.getString("grand_total"));

                        fk_buyer_id = jsonObject.getString("fk_buyer_id");
                        buyer_mobileno = jsonObject.getString("buyer_mobileno");
                        chatlisting_pk_id = jsonObject.getString("chatlisting_pk_id");

                        try {
                            String pay_type_status = jsonObject.getString("pay_type_status"); //pay_type_status= 1 (for cash) ,2( for Online), 3 (for subscription)
                            linearLayoutDescription.setVisibility(VISIBLE);
                            if (pay_type_status.equals("1")) {
                                String paid_status = jsonObject.getString("paid_status");   //paid_status=1 (not paid),2(paid)
                                if (paid_status.equals("1")) {
                                    lblPaidStatus.setText("Unpaid");
                                    llContactDetails.setVisibility(GONE);
                                    linearLayoutDescription.setVisibility(GONE);
                                } else {
                                    llContactDetails.setVisibility(VISIBLE);
                                    lblPaidStatus.setText(getString(R.string.lbl_paid));
                                    linearLayoutDescription.setVisibility(VISIBLE);
                                }
                                tvTransactType.setText("Payment type : Cash");
                                tvPaymentDate.setVisibility(GONE);
                                linearLayoutDownLoad.setVisibility(GONE);
                            } else if (pay_type_status.equals("3")) {
                                tvTransactType.setText("Payment type : Subscription");
                                tvPaymentDate.setVisibility(GONE);
                                linearLayoutDownLoad.setVisibility(GONE);
                            } else {
                                tvTransactType.setText("Payment type : Online" + "  " + "Transaction ID : " + jsonObject.getString("transactionid"));
                                tvPaymentDate.setVisibility(VISIBLE);
                                linearLayoutDownLoad.setVisibility(VISIBLE);
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


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarMyLeadDetailsView);


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
                UtilityMethods.tuchOn(ProgressBarMyLeadDetailsView);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                //params.put("userid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("lead_id", pk_id);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.linear_layout_call, R.id.linear_layout_message, R.id.linear_layout_down_load})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_call:

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + buyer_mobileno));
                startActivity(callIntent);

                break;
            case R.id.linear_layout_message:

                ChatPersnoalFragment chatPersnoalFragment = new ChatPersnoalFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Chat_With", fk_buyer_id);
                bundle.putString("pk_id", chatlisting_pk_id);
                chatPersnoalFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(chatPersnoalFragment, "ChatPersnoalFragment");


                break;
            case R.id.linear_layout_down_load:


                if (InternetConnection.isInternetAvailable(getActivity())) {
                    UtilityMethods.showInfoToastDownload(getActivity(), "Downloading...");
                    DownloadInvoice(fk_payment_id);

                } else {
                    UtilityMethods.showInternetDialog(getActivity());
                }


                break;
        }
    }


    //Method to download invoice -----------------------------------------------
    private void DownloadInvoice(final String pk_id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DOWNLOAD_INVOICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "download invoice -->>" + response);


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String invoice_download = jsonObject.getString("invoice_download");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            // Do something for Pie and above versions
                            new DownloadTask(getActivity(), invoice_download);
                        } else {
                            // do something for phones running an SDK before Pie
                            getDownlodFile(invoice_download);
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

                //  UtilityMethods.tuchOn(ProgressBarPostEnqList);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(getActivity(), getActivity().getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                //UtilityMethods.tuchOn(ProgressBarPostEnqList);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("pay_id", pk_id);
                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

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
