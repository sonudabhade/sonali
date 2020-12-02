package com.tradegenie.android.tradegenieapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Fragments.BusinessDetailsOtherDetailsFragment;
import com.tradegenie.android.tradegenieapp.Models.UploadDocumentsModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterUploadedDocument extends RecyclerView.Adapter<AdapterUploadedDocument.MyViewHolderUploadDocuments> {


    public AlertDialog dialogBuilder;

    private Context mContext;
    private ArrayList<UploadDocumentsModel> mUploadDocumentsModelArrayList;
    private BusinessDetailsOtherDetailsFragment mBusinessDetailsOtherDetailsFragment;

    public AdapterUploadedDocument(Context mContext, ArrayList<UploadDocumentsModel> uploadDocumentsModelArrayList,BusinessDetailsOtherDetailsFragment businessDetailsOtherDetailsFragment) {
        this.mContext = mContext;
        this.mUploadDocumentsModelArrayList = uploadDocumentsModelArrayList;
        mBusinessDetailsOtherDetailsFragment=businessDetailsOtherDetailsFragment;
    }

    @NonNull
    @Override
    public MyViewHolderUploadDocuments onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_upload_document, null);
        return new MyViewHolderUploadDocuments(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderUploadDocuments myViewHolderUploadDocuments, int i) {

        Log.e("check","========>"+mUploadDocumentsModelArrayList.size());

        final UploadDocumentsModel uploadDocumentsModel=mUploadDocumentsModelArrayList.get(i);


        String docname = uploadDocumentsModel.getDocuments_name();
        String str = docname;
        String fileName = str.substring(0, str.lastIndexOf("."));
        final String extension = str.substring(str.lastIndexOf(".") + 1);

        if (extension.equalsIgnoreCase("pdf")){

            try {
                Picasso.get().load(R.drawable.pdfimage).placeholder(R.drawable.default_document).into(myViewHolderUploadDocuments.imgSpecialVideoView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (extension.equalsIgnoreCase("doc")){

            try {
                Picasso.get().load(R.drawable.docimage).placeholder(R.drawable.default_document).into(myViewHolderUploadDocuments.imgSpecialVideoView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

            try {
                Picasso.get().load(uploadDocumentsModel.getDocuments_name()).placeholder(R.drawable.default_document).into(myViewHolderUploadDocuments.imgSpecialVideoView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        myViewHolderUploadDocuments.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = mInflater.inflate(R.layout.dialog_view_document, null);
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

                ImageView imageView=dialogView.findViewById(R.id.iv_view_document);
                ImageView btn_download=dialogView.findViewById(R.id.iv_adapter_download);
                ImageView btn_delete=dialogView.findViewById(R.id.iv_adapter_delete);



                if (extension.equalsIgnoreCase("pdf")){

                    try {
                        Picasso.get().load(R.drawable.pdfimage).placeholder(R.drawable.default_document).into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if (extension.equalsIgnoreCase("doc")){

                    try {
                        Picasso.get().load(R.drawable.docimage).placeholder(R.drawable.default_document).into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {

                    try {
                        Picasso.get().load(uploadDocumentsModel.getDocuments_name()).placeholder(R.drawable.default_document).into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Do you really want to delete this document?");
                        builder.setCancelable(true);

                        builder.setPositiveButton(

                                R.string.YES,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (InternetConnection.isInternetAvailable(mContext)) {

                                            mBusinessDetailsOtherDetailsFragment.DeleteDocument(uploadDocumentsModel.getPk_id(),alertDialog);


                                        } else {

                                            UtilityMethods.showInternetDialog(mContext);

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

                btn_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mBusinessDetailsOtherDetailsFragment.getDownlodFile(uploadDocumentsModel.getDocuments_name());
                    }
                });



            }
        });




    }

    @Override
    public int getItemCount() {
        return mUploadDocumentsModelArrayList.size();
    }


    /*@NonNull
    @Override
    public MyViewHolderSpecialVideo onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_special_videos, null);
        return new MyViewHolderSpecialVideo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSpecialVideo myViewHolderSpecialVideo, final int i) {

        Log.e("Special Video name","---------->"+mSpecialVideoModelArrayList.get(i).getVideo_name());
        Log.e("Special Video thumb","---------->"+mSpecialVideoModelArrayList.get(i).getThumbimage());

        myViewHolderSpecialVideo.tvSpecialVideoView.setText(mSpecialVideoModelArrayList.get(i).getVideo_name());

        try {
            Picasso.get().load(mSpecialVideoModelArrayList.get(i).getThumbimage()).placeholder(R.mipmap.ic_launcher).into(myViewHolderSpecialVideo.imgSpecialVideoView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myViewHolderSpecialVideo.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, videoplayer.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", mSpecialVideoModelArrayList.get(i).getVideo());
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSpecialVideoModelArrayList.size();
    }*/


    public class MyViewHolderUploadDocuments extends RecyclerView.ViewHolder {

        @BindView(R.id.img_special_video_view)
        ImageView imgSpecialVideoView;
        @BindView(R.id.tv_special_video_view)
        TextView tvSpecialVideoView;

        public MyViewHolderUploadDocuments(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
