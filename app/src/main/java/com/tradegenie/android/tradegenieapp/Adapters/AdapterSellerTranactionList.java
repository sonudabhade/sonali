package com.tradegenie.android.tradegenieapp.Adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.tradegenie.android.tradegenieapp.Fragments.MyLeadFragment;
import com.tradegenie.android.tradegenieapp.Models.PostRequirementModel;
import com.tradegenie.android.tradegenieapp.Models.SellerTransactionModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 *
 */
public class AdapterSellerTranactionList extends RecyclerView.Adapter<AdapterSellerTranactionList.MyViewHolderSellerTranactionList> {

    SessionManager mSessionManager;
    private Context mContext;
    private ArrayList<SellerTransactionModel> mSellerTransactionModelArrayList;
    private DownloadManager downloadManager;

    public AdapterSellerTranactionList(Context mContext, ArrayList<SellerTransactionModel> SellerTransactionModelArrayList) {
        this.mContext = mContext;
        this.mSellerTransactionModelArrayList = SellerTransactionModelArrayList;
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mSessionManager = new SessionManager(mContext);
    }

    @NonNull
    @Override
    public MyViewHolderSellerTranactionList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_seller_transaction, null);
        return new MyViewHolderSellerTranactionList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSellerTranactionList myViewHolderSellerTranactionList, final int i) {

        myViewHolderSellerTranactionList.tvOrderId.setText("Order ID " + mSellerTransactionModelArrayList.get(i).getPk_id());
        myViewHolderSellerTranactionList.tvTransactionId.setText(mSellerTransactionModelArrayList.get(i).getTransactionid());
        if (mSellerTransactionModelArrayList.get(i).getLead_subscription().equals("Subscription")) {
            myViewHolderSellerTranactionList.tv_lead_subscription.setText("Subscription");

        }else if (mSellerTransactionModelArrayList.get(i).getLead_subscription().equals("tender")) {
            myViewHolderSellerTranactionList.tv_lead_subscription.setText(mContext.getString(R.string.lbl_tender));

        } else {
            myViewHolderSellerTranactionList.tv_lead_subscription.setText("Lead");
        }
        //   myViewHolderSellerTranactionList.tvTotalAmount.setText(mSellerTransactionModelArrayList.get(i).getCurrency() + " " + mSellerTransactionModelArrayList.get(i).getAmount());
        myViewHolderSellerTranactionList.tvTotalAmount.setText(mSellerTransactionModelArrayList.get(i).getCurrency() + " " + mSellerTransactionModelArrayList.get(i).getGrand_total());

        String date = mSellerTransactionModelArrayList.get(i).getCreated_date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        date = spf.format(newDate);
        myViewHolderSellerTranactionList.tvDate.setText(date);


        myViewHolderSellerTranactionList.Lnr_download_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UtilityMethods.showInfoToastDownload(mContext, "Downloading...");
                DownloadInvoice(mSellerTransactionModelArrayList.get(i).getPk_id());
            }
        });

        if (mSellerTransactionModelArrayList.get(i).getPaymentType().equals("2")){
            myViewHolderSellerTranactionList.Lnr_download_invoice.setVisibility(VISIBLE);
            myViewHolderSellerTranactionList.lbl_payment_type.setText("Payment type : Online");
        }else {
            myViewHolderSellerTranactionList.Lnr_download_invoice.setVisibility(GONE);
            if (mSellerTransactionModelArrayList.get(i).getPaymentType().equals("1")){
                myViewHolderSellerTranactionList.lbl_payment_type.setText("Payment type : Cash");
            }else if (mSellerTransactionModelArrayList.get(i).getPaymentType().equals("3")){
                myViewHolderSellerTranactionList.lbl_payment_type.setText("Payment type : Subscription");
            }
            myViewHolderSellerTranactionList.Lnr_download_invoice.setClickable(false);
        }
    }


    @Override
    public int getItemCount() {
        return mSellerTransactionModelArrayList.size();
    }


    public class MyViewHolderSellerTranactionList extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order_id)
        TextView tvOrderId;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_total_amount)
        TextView tvTotalAmount;
        @BindView(R.id.tv_transaction_id)
        TextView tvTransactionId;
        @BindView(R.id.tv_lead_subscription)
        TextView tv_lead_subscription;
        @BindView(R.id.lbl_payment_type)
        TextView lbl_payment_type;
        @BindView(R.id.Lnr_download_invoice)
        LinearLayout Lnr_download_invoice;

        public MyViewHolderSellerTranactionList(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            // Do something for Pie and above versions
                            new DownloadTask(mContext, invoice_download);
                        } else {
                            // do something for phones running an SDK before Pie
                            getDownlodFile(invoice_download);
                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(mContext);
                    } else {
                        UtilityMethods.showInfoToast(mContext, jsonObject.getString("message"));
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
                        UtilityMethods.showInfoToast(mContext, mContext.getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(mContext, mContext.getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(mContext, mContext.getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(mContext, mContext.getString(R.string.network_error));
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

        Volley.newRequestQueue(mContext).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", myFile);
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
                mContext.startActivity(intent);

            } else {
                Log.e(TAG, downloadFileName);

                //Start Downloading Task
                new DownloadTask.DownloadingTask().execute();
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
                            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", outputFile);
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
                            mContext.startActivity(intent);

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
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", myFile);
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
            mContext.startActivity(intent);

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
