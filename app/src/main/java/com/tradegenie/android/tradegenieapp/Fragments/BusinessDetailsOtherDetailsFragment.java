package com.tradegenie.android.tradegenieapp.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.Volley;
import com.tradegenie.android.tradegenieapp.Activity.SquareCropActivity;
import com.tradegenie.android.tradegenieapp.Activity.YoutubeActivity;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterUploadedCertificate;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterUploadedDocument;
import com.tradegenie.android.tradegenieapp.BuildConfig;
import com.tradegenie.android.tradegenieapp.DocUpload.FilePath;
import com.tradegenie.android.tradegenieapp.DocUpload.SingleUploadBroadcastReceiver;
import com.tradegenie.android.tradegenieapp.Models.DataPart;
import com.tradegenie.android.tradegenieapp.Models.UploadCertificateModel;
import com.tradegenie.android.tradegenieapp.Models.UploadDocumentsModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;
import com.tradegenie.android.tradegenieapp.Utility.VolleyMultipartRequest;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
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
//import static com.tradegenie.android.tradegenieapp.Fragments.PersonalDetailsFragment.decodeSampledBitmapFromResource;

public class BusinessDetailsOtherDetailsFragment extends Fragment implements View.OnClickListener, SingleUploadBroadcastReceiver.Delegate {

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    @BindView(R.id.recycler_add_document)
    RecyclerView recyclerAddDocument;
    Unbinder unbinder;
    @BindView(R.id.recycler_add_document_certificate)
    RecyclerView recyclerAddDocumentCertificate;
    @BindView(R.id.linear_layout_upload_document)
    LinearLayout linearLayoutUploadDocument;
    @BindView(R.id.edt_video_link)
    EditText edtVideoLink;
    @BindView(R.id.iv_video_link)
    ImageView ivVideoLink;
    @BindView(R.id.relativeLayoutOtherDetails)
    RelativeLayout relativeLayoutOtherDetails;
    @BindView(R.id.btn_submit_video_link)
    Button btnSubmitVideoLink;
    @BindView(R.id.linear_layout_new_certificate)
    LinearLayout linearLayoutNewCertificate;
    @BindView(R.id.linear_layout_document)
    LinearLayout linearLayoutDocument;
    @BindView(R.id.linear_layout_certificate)
    LinearLayout linearLayoutCertificate;

    private SessionManager mSessionManager;

    private DownloadManager downloadManager;


    private ArrayList<UploadDocumentsModel> mUploadDocumentsModelArrayList;
    private AdapterUploadedDocument mAdapterUploadedDocument;
    LinearLayoutManager layoutManager;

    private ArrayList<UploadCertificateModel> mUploadCertificateModelArrayList;
    private AdapterUploadedCertificate mAdapterUploadedCertificate;
    LinearLayoutManager layoutManagercertificate;

    private String selectFrom = "";

    //camera and gallery
    private Uri fileUri;
    private static final int TAKE_PICTURE = 1, SELECT_PICTURE = 2;
    private static final int CROPPED_IMAGE = 3;


    private static final String IMAGE_DIRECTORY_NAME = "TradeGenie";
    //close camera and gallery

    //------------------------------------------------------------------------------------------------------------------

    //Image request code
    private int PICK_DOC_REQUEST = 4;
    private int PICK_DOC_REQUEST_DOCUMENT = 5;
    private int PICK_VIDEO_REQUEST = 2;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Uri to store the image uri
    private Uri filePath, VideofilePath;


    //------------------------------------------------------------------------------------------------------------------


    public BusinessDetailsOtherDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_details_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName(getString(R.string.business_details));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());

        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


        mUploadDocumentsModelArrayList = new ArrayList<>();
        mAdapterUploadedDocument = new AdapterUploadedDocument(getActivity(), mUploadDocumentsModelArrayList, BusinessDetailsOtherDetailsFragment.this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAddDocument.setLayoutManager(layoutManager);
        recyclerAddDocument.setAdapter(mAdapterUploadedDocument);


        mUploadCertificateModelArrayList = new ArrayList<>();
        mAdapterUploadedCertificate = new AdapterUploadedCertificate(getActivity(), mUploadCertificateModelArrayList, BusinessDetailsOtherDetailsFragment.this);
        layoutManagercertificate = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAddDocumentCertificate.setLayoutManager(layoutManagercertificate);
        recyclerAddDocumentCertificate.setAdapter(mAdapterUploadedCertificate);

        requestStoragePermission();


        if (InternetConnection.isInternetAvailable(getActivity())) {

            GetOtherDetails();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_DOC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String filename = "DOC_" + filePath.toString().substring(filePath.toString().lastIndexOf("/") + 1);

            /*if (InternetConnection.isInternetAvailable(getActivity())) {

                getDocumentList();

            } else {

                UtilityMethods.showInternetDialog(getActivity());

            }*/

            if (InternetConnection.isInternetAvailable(getActivity())) {

                uploadMultipart();

            } else {

                UtilityMethods.showInternetDialog(getActivity());

            }

        }

        if (requestCode == PICK_DOC_REQUEST_DOCUMENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String filename = "DOC_" + filePath.toString().substring(filePath.toString().lastIndexOf("/") + 1);


            if (InternetConnection.isInternetAvailable(getActivity())) {

                uploadMultipartForDocument();

            } else {

                UtilityMethods.showInternetDialog(getActivity());

            }

        }


        switch (requestCode) {
            case SELECT_PICTURE:
                Bitmap bitmap = null;
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
                            /*Bitmap thumbnail = decodeSampledBitmapFromResource(
                                    picturePath, 500, 500);*/

                            // rotated
                            /*Constants.mMainActivity.thumbnail_r = imageOreintationValidator(
                                    thumbnail, picturePath);*/
                            //ivProfilePic.setBackground(null);
                            //ivProfilePic.setImageBitmap(Constants.mMainActivity.thumbnail_rb);
                            //IsImageSet = true;

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

                if (Constants.mMainActivity.thumbnail_r != null && selectFrom.equalsIgnoreCase("document")) {

                    Log.e("check", "call==========>");

                    showUploadDocumentDialog(getActivity());


                } else if (Constants.mMainActivity.thumbnail_r != null && selectFrom.equalsIgnoreCase("certificate")) {

                    showUploadCertificateDialog(getActivity());

                } else {

                    //UtilityMethods.showInfoToast(getActivity(), "Something went wrong");

                }


                break;


        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_previous, R.id.img_next, R.id.img_previous_certificate, R.id.img_next_certificate, R.id.linear_layout_upload_document, R.id.btn_submit_video_link, R.id.iv_video_link, R.id.linear_layout_new_certificate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_previous:

                recyclerAddDocument.getLayoutManager().scrollToPosition(layoutManager.findFirstVisibleItemPosition() - 1);

                break;
            case R.id.img_next:

                recyclerAddDocument.getLayoutManager().scrollToPosition(layoutManager.findLastVisibleItemPosition() + 1);

                break;
            case R.id.img_previous_certificate:

                recyclerAddDocumentCertificate.getLayoutManager().scrollToPosition(layoutManagercertificate.findFirstVisibleItemPosition() - 1);

                break;
            case R.id.img_next_certificate:

                recyclerAddDocumentCertificate.getLayoutManager().scrollToPosition(layoutManagercertificate.findLastVisibleItemPosition() + 1);

                break;

            case R.id.linear_layout_upload_document:

                ChooseCertificateDialogForDocument(getActivity());


                selectFrom = "document";

                break;
            case R.id.btn_submit_video_link:


                if (!edtVideoLink.getText().toString().equals("")) {

                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        SubmitImageURL();


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }

                } else {

                    UtilityMethods.showInfoToast(getActivity(), "Please enter valid youtube link");

                }


                break;
            case R.id.linear_layout_new_certificate:

                ChooseCertificateDialog(getActivity());

                selectFrom = "certificate";

                break;
        }
    }

    //Method to show options for camera anf gallery
    private void ShowCameraGallryOption() {

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

    //Method to get cropped image
    private void GetCroppedImage() {

        Intent intent = new Intent(getActivity(), SquareCropActivity.class);
        startActivityForResult(intent, CROPPED_IMAGE);
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

           /* Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500,
                    false);
*/
            // rotated
            Constants.mMainActivity.thumbnail_r = imageOreintationValidator(bitmap, fileUri.getPath());

            //ivProfilePic.setBackground(null);
            //ivProfilePic.setImageBitmap(Constants.mMainActivity.thumbnail_r);
            // IsImageSet = true;


            GetCroppedImage();
        } catch (NullPointerException e) {
            e.printStackTrace();
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


    @OnClick(R.id.iv_video_link)
    public void onViewClicked() {


        if (edtVideoLink.getText().toString().equals("")) {

            UtilityMethods.showInfoToast(getActivity(), "Please enter valid youtube link");

        } else {

            if (edtVideoLink.getText().toString().contains("=")){

                String part0 = null; // this will contain "Fruit"
                String part1 = null;
                try {
                    String url = edtVideoLink.getText().toString();
                    part0 = null;
                    String video_code1 = null;
                    String[] separated = url.split("=");
                    part0 = separated[0];
                    part1 = null;
                    part1 = separated[1];
                } catch (Exception e) {
                    e.printStackTrace();

                    UtilityMethods.showInfoToast(getActivity(),"Please enter valid url");


                }


                Intent i = new Intent(getActivity(), YoutubeActivity.class);
                Bundle bundle = new Bundle();

                Log.e("check", "youtube part0==>" + part0);
                Log.e("check", "youtube part1==>" + part1);

                bundle.putString("video", part1);
                i.putExtras(bundle);
                startActivity(i);


            }else if (edtVideoLink.getText().toString().contains("/")){

                String part0 = null; // this will contain "Fruit"
                String video_code1 = null;
                try {
                    String url = edtVideoLink.getText().toString();
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

                    UtilityMethods.showInfoToast(getActivity(),"Please enter valid url");


                }


                Intent i = new Intent(getActivity(), YoutubeActivity.class);
                Bundle bundle = new Bundle();

                Log.e("check", "youtube part0" + part0);
                Log.e("check", "youtube part1" + video_code1);

                bundle.putString("video", video_code1);
                i.putExtras(bundle);
                startActivity(i);

            }else {

                UtilityMethods.showInfoToast(getActivity(),"Please enter valid url");

            }


        }

    }

    //Method to Submit ImageURL
    private void SubmitImageURL() {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_VIDEO_LINK_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "SubmitImageURL -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            GetOtherDetails();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }

                        UtilityMethods.showSuccessToast(getActivity(), "Video link uploaded successfully");

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

                UtilityMethods.tuchOn(relativeLayoutOtherDetails);


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
                UtilityMethods.tuchOn(relativeLayoutOtherDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                params.put("video_link", edtVideoLink.getText().toString());

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

    public void showUploadDocumentDialog(Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to upload this document?");
        builder.setCancelable(true);

        builder.setPositiveButton(

                R.string.YES,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            CallUploadDocumentWebservice();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

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

    public void showUploadCertificateDialog(Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to upload this certificate?");
        builder.setCancelable(true);

        builder.setPositiveButton(

                R.string.YES,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            CallUploadCerticateWebservice();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

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

    public void ChooseCertificateDialog(Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Choose option");
        builder.setCancelable(true);

        builder.setPositiveButton(

                R.string.Image,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ShowCameraGallryOption();


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
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }

    public void ChooseCertificateDialogForDocument(Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Choose option");
        builder.setCancelable(true);

        builder.setPositiveButton(

                R.string.Image,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ShowCameraGallryOption();


                    }
                });

        builder.setNegativeButton(

                R.string.pdf_doc,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        showFileChooserForDocument();

                    }
                });


        AlertDialog alert11 = builder.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }


    private void CallUploadDocumentWebservice() {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_DOCUMENT,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("Response", new String(response.data));
                        try {

                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            if (jsonObject.getString("error_code").equals("1")) {

                                if (InternetConnection.isInternetAvailable(getActivity())) {

                                    GetOtherDetails();


                                } else {

                                    UtilityMethods.showInternetDialog(getActivity());

                                }

                                UtilityMethods.showSuccessToast(getActivity(), "Document Uploaded Successfully");


                            } else if (jsonObject.getString("error_code").equals("2")) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, something went wrong");

                            } else if (jsonObject.getString("error_code").equals("10")) {

                                UtilityMethods.UserInactivePopup(getActivity());

                            } else {
                                UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                        UtilityMethods.tuchOn(relativeLayoutOtherDetails);
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
                        UtilityMethods.tuchOn(relativeLayoutOtherDetails);


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

                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                System.out.println("update profile Parameters is :-" + params);
                return params;
            }


            // Here we are passing image by renaming it with a unique name
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                System.out.println("Size of Parameters in start :-" + params.size());
                Log.e("check", "=====>" + Constants.mMainActivity.thumbnail_r);

                if (Constants.mMainActivity.thumbnail_r != null) {
                    long imagename = System.currentTimeMillis();
                    params.put("fileDocument", new DataPart(imagename + ".png", getFileDataFromDrawable(Constants.mMainActivity.thumbnail_r)));
                    System.out.println("Size of Parameters in if :-" + params.size());
                    return params;
                } else {
                    //   params=null;
                    params.remove("fileDocument");
                    System.out.println("Size of Parameters in else :-" + params.size());
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


    private void CallUploadCerticateWebservice() {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_CERTIFICATE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("Response", new String(response.data));
                        try {

                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            if (jsonObject.getString("error_code").equals("1")) {

                                if (InternetConnection.isInternetAvailable(getActivity())) {

                                    GetOtherDetails();


                                } else {

                                    UtilityMethods.showInternetDialog(getActivity());

                                }

                                UtilityMethods.showSuccessToast(getActivity(), "Certificate Uploaded Successfully");


                            } else if (jsonObject.getString("error_code").equals("2")) {

                                UtilityMethods.showInfoToast(getActivity(), "Sorry, something went wrong");

                            } else if (jsonObject.getString("error_code").equals("10")) {

                                UtilityMethods.UserInactivePopup(getActivity());

                            } else {
                                UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                        UtilityMethods.tuchOn(relativeLayoutOtherDetails);
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
                        UtilityMethods.tuchOn(relativeLayoutOtherDetails);


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

                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                System.out.println("update profile Parameters is :-" + params);
                return params;
            }


            // Here we are passing image by renaming it with a unique name
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                System.out.println("Size of Parameters in start :-" + params.size());


                Log.e("check", "=====>" + Constants.mMainActivity.thumbnail_r);

                if (Constants.mMainActivity.thumbnail_r != null) {
                    long imagename = System.currentTimeMillis();
                    params.put("certificates", new DataPart(imagename + ".png", getFileDataFromDrawable(Constants.mMainActivity.thumbnail_r)));
                    System.out.println("Size of Parameters in if :-" + params.size());
                    return params;
                } else {
                    //   params=null;
                    params.remove("certificates");
                    System.out.println("Size of Parameters in else :-" + params.size());
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

    //method to show file chooser
    private void showFileChooserForDocument() {
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
        startActivityForResult(Intent.createChooser(intent, ""), PICK_DOC_REQUEST_DOCUMENT);


    }

    public void uploadMultipart() {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);

        Log.e("path", "path ======> " + FilePath.getPath(getActivity(), filePath));


        //getting the actual path of the pdf
        String path = FilePath.getPath(getActivity(), filePath);

        Log.e("path", "path ======> " + path);
        //Uploading code
        if (path == null) {

            UtilityMethods.showInfoToast(getActivity(), "Please move your .pdf file to internal storage and retry");
        } else try {
            String uploadId = UUID.randomUUID().toString();

            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Constants.URL_CERTIFICATE)
                    .addFileToUpload(path, "certificates") //Adding file
                    // .addParameter("teacher_id", mSessionManager.getStringData(Constants.KEY_TEACHER_ID)) //Adding text parameter to the request
                    .addParameter("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID)) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            exc.printStackTrace();
            UtilityMethods.showInfoToast(getActivity(), exc.getMessage());
        }
    }


    public void uploadMultipartForDocument() {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);

        Log.e("path", "path ======> " + FilePath.getPath(getActivity(), filePath));


        //getting the actual path of the pdf
        String path = FilePath.getPath(getActivity(), filePath);

        Log.e("path", "path ======> " + path);
        //Uploading code
        if (path == null) {

            UtilityMethods.showInfoToast(getActivity(), "Please move your .pdf file to internal storage and retry");
        } else try {
            String uploadId = UUID.randomUUID().toString();

            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Constants.URL_DOCUMENT)
                    .addFileToUpload(path, "fileDocument") //Adding file
                    // .addParameter("teacher_id", mSessionManager.getStringData(Constants.KEY_TEACHER_ID)) //Adding text parameter to the request
                    .addParameter("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID)) //Adding text parameter to the request
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

                    if (InternetConnection.isInternetAvailable(getActivity())) {

                        GetOtherDetails();


                    } else {

                        UtilityMethods.showInternetDialog(getActivity());

                    }


                    UtilityMethods.showSuccessToast(getActivity(), "Uploaded successfully");

                } else {
                    UtilityMethods.showInfoToast(getActivity(), "" + jsonObj.getString("message"));

                }

            } catch (Exception e) {
                e.printStackTrace();
                //UtilityMethods.showInfoToast(getActivity(), "Server Error");

            }
            UtilityMethods.tuchOff(relativeLayoutOtherDetails);
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

    //Method to Get OtherDetails
    private void GetOtherDetails() {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_OTHER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "GetOtherDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        String videolink = jsonObject.getString("videolink");
                        edtVideoLink.setText(videolink);

                        String document_url = jsonObject.getString("document_url");
                        String certificate_url = jsonObject.getString("certificate_url");

                        JSONArray jsonArrayDocuments = jsonObject.getJSONArray("documents");
                        mUploadDocumentsModelArrayList.clear();
                        for (int i = 0; i < jsonArrayDocuments.length(); i++) {


                            JSONObject jsonObjectDocuments = jsonArrayDocuments.getJSONObject(i);
                            String pk_id = jsonObjectDocuments.getString("pk_id");
                            String documents_name = jsonObjectDocuments.getString("documents_name");

                            UploadDocumentsModel uploadDocumentsModel = new UploadDocumentsModel(pk_id, document_url + documents_name);
                            mUploadDocumentsModelArrayList.add(uploadDocumentsModel);


                        }


                        JSONArray jsonArraycertificates = jsonObject.getJSONArray("certificates");
                        mUploadCertificateModelArrayList.clear();
                        for (int i = 0; i < jsonArraycertificates.length(); i++) {


                            JSONObject jsonObjectcertificates = jsonArraycertificates.getJSONObject(i);
                            String pk_id = jsonObjectcertificates.getString("pk_id");
                            String certificate_name = jsonObjectcertificates.getString("certificate_name");

                            UploadCertificateModel uploadCertificateModel = new UploadCertificateModel(pk_id, certificate_url + certificate_name);
                            mUploadCertificateModelArrayList.add(uploadCertificateModel);


                        }

                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterUploadedDocument.notifyDataSetChanged();
                    mAdapterUploadedCertificate.notifyDataSetChanged();


                    if (mUploadDocumentsModelArrayList.size() <= 0) {

                        linearLayoutDocument.setVisibility(View.GONE);

                    } else {

                        linearLayoutDocument.setVisibility(View.VISIBLE);


                    }

                    if (mUploadCertificateModelArrayList.size() <= 0) {

                        linearLayoutCertificate.setVisibility(View.GONE);

                    } else {

                        linearLayoutCertificate.setVisibility(View.VISIBLE);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutOtherDetails);


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
                UtilityMethods.tuchOn(relativeLayoutOtherDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                //params.put("video_link", edtVideoLink.getText().toString());

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

    //Method to Delete Document
    public void DeleteDocument(final String document_id, final AlertDialog alertDialog) {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DOCUMENT_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "DeleteDocument" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            GetOtherDetails();

                            alertDialog.dismiss();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }

                        UtilityMethods.showSuccessToast(getActivity(), "Document deleted successfully");

                    } else if (jsonObject.getString("error_code").equals("0")) {

                        UtilityMethods.showWarningToast(getActivity(), "Please enter all details");


                    } else if (jsonObject.getString("error_code").equals("2")) {

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, something went wrong ");

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterUploadedDocument.notifyDataSetChanged();
                    mAdapterUploadedCertificate.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutOtherDetails);


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
                UtilityMethods.tuchOn(relativeLayoutOtherDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("document_id", document_id);
                //params.put("video_link", edtVideoLink.getText().toString());

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

    //Method to Delete Certificate
    public void DeleteCertificate(final String certificates_id, final AlertDialog alertDialog) {

        UtilityMethods.tuchOff(relativeLayoutOtherDetails);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CERTIFICATES_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "DeleteCertificate" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            GetOtherDetails();

                            alertDialog.dismiss();


                        } else {

                            UtilityMethods.showInternetDialog(getActivity());

                        }
                        UtilityMethods.showSuccessToast(getActivity(), "Certificate deleted successfully");

                    } else if (jsonObject.getString("error_code").equals("0")) {

                        UtilityMethods.showWarningToast(getActivity(), "Please enter all details");


                    } else if (jsonObject.getString("error_code").equals("2")) {

                        UtilityMethods.showInfoToast(getActivity(), "Sorry, something went wrong ");

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());


                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterUploadedDocument.notifyDataSetChanged();
                    mAdapterUploadedCertificate.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutOtherDetails);


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
                UtilityMethods.tuchOn(relativeLayoutOtherDetails);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("certificates_id", certificates_id);
                //params.put("video_link", edtVideoLink.getText().toString());

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

}
