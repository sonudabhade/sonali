package com.tradegenie.android.tradegenieapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Activity.PaymentForUpgradeActivity;
import com.tradegenie.android.tradegenieapp.Fragments.MySubscriptionFragment;
import com.tradegenie.android.tradegenieapp.Models.MySubscriptionModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 *
 */
public class AdapterMySubscription extends RecyclerView.Adapter<AdapterMySubscription.MyViewHolderMySubscription> {


    private Context mContext;
    private ArrayList<MySubscriptionModel> mMySubscriptionModelArrayList;
    private ArrayList<Integer> mIntegerArrayList;
    private MySubscriptionFragment mMySubscriptionFragment;


    public AdapterMySubscription(Context mContext, ArrayList<MySubscriptionModel> mySubscriptionModelArrayList, MySubscriptionFragment mySubscriptionFragment) {
        this.mContext = mContext;
        this.mMySubscriptionModelArrayList = mySubscriptionModelArrayList;
        mMySubscriptionFragment = mySubscriptionFragment;


        mIntegerArrayList = new ArrayList<>();
        mIntegerArrayList.add(1);
        mIntegerArrayList.add(2);
        mIntegerArrayList.add(3);
        mIntegerArrayList.add(4);
        mIntegerArrayList.add(5);
        mIntegerArrayList.add(6);
        mIntegerArrayList.add(7);
        mIntegerArrayList.add(8);
        mIntegerArrayList.add(9);
        mIntegerArrayList.add(10);
    }

    @NonNull
    @Override
    public MyViewHolderMySubscription onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_subscription, null);
        return new MyViewHolderMySubscription(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderMySubscription myViewHolderMySubscription, final int i) {


        Log.e("check", "===>" + mMySubscriptionModelArrayList.size());

        final MySubscriptionModel mySubscriptionModel = mMySubscriptionModelArrayList.get(i);

        myViewHolderMySubscription.linearLayoutSubscription.setBackground(mContext.getResources().getDrawable(mySubscriptionModel.getBackground()));
        myViewHolderMySubscription.tvTitle.setText(mySubscriptionModel.getHeading());
        myViewHolderMySubscription.tvDays.setText(mySubscriptionModel.getDays() + " " + mContext.getString(R.string.lbl_days));

        myViewHolderMySubscription.linearLayoutSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        try {
            String pay_type_status = mySubscriptionModel.getPay_type_status(); //pay_type_status= 1 (for cash) ,2( for Online)
            if (pay_type_status.equals("1")) {
                String paid_status = mySubscriptionModel.getPaid_status();   //paid_status=1(paid), 2(not paid)
                if (paid_status.equals("2")) {
                    myViewHolderMySubscription.tvTransactType.setText("Payment type : Cash Unpaid");
//                    lblPaidStatus.setText("");
                } else {
                    myViewHolderMySubscription.tvTransactType.setText("Payment type : Cash Paid");
//                    lblPaidStatus.setText("Paid");
                }
            } else {
                myViewHolderMySubscription.tvTransactType.setText("Payment type : Online Paid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  if (mMySubscriptionModelArrayList.get(i).getFk_sid().equals("1")) {

            myViewHolderMySubscription.tvUpgrade.setVisibility(View.GONE);
            myViewHolderMySubscription.view.setVisibility(View.GONE);

        } else if (mMySubscriptionModelArrayList.get(i).getFk_sid().equals("2")) {

            myViewHolderMySubscription.tvUpgrade.setVisibility(View.GONE);
            myViewHolderMySubscription.view.setVisibility(View.GONE);

        } else if (mMySubscriptionModelArrayList.get(i).getFk_sid().equals("3")) {


            myViewHolderMySubscription.tvUpgrade.setVisibility(View.GONE);
            myViewHolderMySubscription.view.setVisibility(View.GONE);

        }else{
            myViewHolderMySubscription.tvUpgrade.setVisibility(View.VISIBLE);
            myViewHolderMySubscription.view.setVisibility(View.VISIBLE);
        }*/


        if (mMySubscriptionModelArrayList.get(i).getStatus().equals("3") || mMySubscriptionModelArrayList.get(i).getUsed_total_leads().equals("2")) {
            myViewHolderMySubscription.tvUpgrade.setVisibility(View.VISIBLE);
            myViewHolderMySubscription.view.setVisibility(View.VISIBLE);
        } else {
            myViewHolderMySubscription.tvUpgrade.setVisibility(View.GONE);
            myViewHolderMySubscription.view.setVisibility(View.GONE);
        }

        if(mMySubscriptionModelArrayList.get(i).getFk_sid().equals("1") || mMySubscriptionModelArrayList.get(i).getFk_sid().equals("2") || mMySubscriptionModelArrayList.get(i).getFk_sid().equals("3")){
            myViewHolderMySubscription.tvUpgrade.setVisibility(View.GONE);
            myViewHolderMySubscription.view.setVisibility(View.GONE);
        }

        String start_date = mMySubscriptionModelArrayList.get(i).getStart_date();
        String end_date = mMySubscriptionModelArrayList.get(i).getEnd_date();

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        Date StartDate = null;
        Date EndDate = null;
        try {
            StartDate = spf.parse(start_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            EndDate = spf.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd/MM/yyyy");
        start_date = spf.format(StartDate);
        end_date = spf.format(EndDate);

        myViewHolderMySubscription.tvStartDate.setText(mContext.getString(R.string.lbl_start_date) + " - " + start_date);
        myViewHolderMySubscription.tvEndDate.setText("End Date - " + end_date);


        myViewHolderMySubscription.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = mInflater.inflate(R.layout.dialog_my_subscription_popup, null);
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


                LinearLayout linear_layout_down_load = alertDialog.findViewById(R.id.linear_layout_down_load);
                ImageView iv_cancel = alertDialog.findViewById(R.id.iv_cancel);
                TextView tv_title = alertDialog.findViewById(R.id.tv_title);
                TextView tv_cost = alertDialog.findViewById(R.id.tv_cost);
                TextView tv_start_date = alertDialog.findViewById(R.id.tv_start_date);
                TextView tv_end_date = alertDialog.findViewById(R.id.tv_end_date);
                TextView tv_description = alertDialog.findViewById(R.id.tv_description);
                TextView tv_title_services = alertDialog.findViewById(R.id.tv_title_services);
                TextView tv_services = alertDialog.findViewById(R.id.tv_services);
                TextView tv_number_of_leads = alertDialog.findViewById(R.id.tv_number_of_leads);
                TextView tv_image_count = alertDialog.findViewById(R.id.tv_image_count);
                TextView tv_transact_type_dialog = alertDialog.findViewById(R.id.tv_transact_type_dialog);

                String pay_type_status = mySubscriptionModel.getPay_type_status(); //pay_type_status= 1 (for cash) ,2( for Online)
                if (pay_type_status.equals("2")){
                    linear_layout_down_load.setVisibility(VISIBLE);
                }else {
                    linear_layout_down_load.setVisibility(GONE);
                }
                linear_layout_down_load.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetConnection.isInternetAvailable(mContext)) {
                            UtilityMethods.showInfoToastDownload(mContext, "Downloading...");
                            mMySubscriptionFragment.downloadInvoice(mySubscriptionModel.getInvoice_url());
                        } else {
                            UtilityMethods.showInternetDialog(mContext);
                        }
                    }
                });

                String serices = mMySubscriptionModelArrayList.get(i).getServices();
                String noofleads = mMySubscriptionModelArrayList.get(i).getNofoleads();
                String lead_used = mMySubscriptionModelArrayList.get(i).getLead_used();

                if (noofleads.equals("")) {

                    tv_number_of_leads.setVisibility(View.GONE);

                } else {

                    tv_number_of_leads.setText("Number of used Leads :- " + lead_used + "/" + noofleads);
                }

                if (serices.equals("")) {

                    tv_title_services.setVisibility(View.GONE);
                    tv_services.setVisibility(View.GONE);

                } else {

                    tv_title_services.setVisibility(View.VISIBLE);
                    tv_services.setVisibility(View.VISIBLE);
                    tv_services.setText(serices);
                }
                tv_title.setText(mMySubscriptionModelArrayList.get(i).getHeading());
                tv_cost.setText("Cost - " + mMySubscriptionModelArrayList.get(i).getCurrency() + " " + mMySubscriptionModelArrayList.get(i).getPrice());
                String start_date = mMySubscriptionModelArrayList.get(i).getStart_date();
                String end_date = mMySubscriptionModelArrayList.get(i).getEnd_date();
                tv_description.setText(mMySubscriptionModelArrayList.get(i).getDescription());
                tv_image_count.setText(mContext.getString(R.string.lbl_number_of_images) + " - " + mMySubscriptionModelArrayList.get(i).getNoofimages());


                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                Date StartDate = null;
                Date EndDate = null;
                try {
                    StartDate = spf.parse(start_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    EndDate = spf.parse(end_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                spf = new SimpleDateFormat("dd/MM/yyyy");
                start_date = spf.format(StartDate);
                end_date = spf.format(EndDate);

                tv_start_date.setText(mContext.getString(R.string.lbl_start_date) + " - " + start_date);
                tv_end_date.setText("End Date - " + end_date);

                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });

                try {
                    tv_transact_type_dialog.setText(myViewHolderMySubscription.tvTransactType.getText().toString());
                }catch (Exception e){e.printStackTrace();}
            }
        });

        myViewHolderMySubscription.tvUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mMySubscriptionFragment.CallUpgradeSubscription(mySubscriptionModel.getPk_id(), mySubscriptionModel.getFk_sid());

            }
        });

    }

    @Override
    public int getItemCount() {
        return mMySubscriptionModelArrayList.size();
    }


    public class MyViewHolderMySubscription extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_start_date)
        TextView tvStartDate;
        @BindView(R.id.tv_end_date)
        TextView tvEndDate;
        @BindView(R.id.tv_days)
        TextView tvDays;
        @BindView(R.id.linear_layout_subscription)
        LinearLayout linearLayoutSubscription;
        @BindView(R.id.tv_view_details)
        TextView tvViewDetails;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.tv_upgrade)
        TextView tvUpgrade;
        @BindView(R.id.tv_transact_type)
        TextView tvTransactType;
        @BindView(R.id.linear_layout_update_plan)
        LinearLayout linearLayoutUpdatePlan;

        public MyViewHolderMySubscription(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
