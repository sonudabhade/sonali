package com.tradegenie.android.tradegenieapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Fragments.MySubscriptionRecommendedFragment;
import com.tradegenie.android.tradegenieapp.Models.RecommendedModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterMySubscriptionRecommanded extends RecyclerView.Adapter<AdapterMySubscriptionRecommanded.MyViewHolderRecommanded> {


    private Context mContext;
    private ArrayList<RecommendedModel> mRecommendedModelArrayList;
    private MySubscriptionRecommendedFragment mMySubscriptionRecommendedFragment;


    public AdapterMySubscriptionRecommanded(Context mContext, ArrayList<RecommendedModel> recommendedModelArrayList, MySubscriptionRecommendedFragment mySubscriptionRecommendedFragment) {
        this.mContext = mContext;
        this.mRecommendedModelArrayList = recommendedModelArrayList;
        mMySubscriptionRecommendedFragment = mySubscriptionRecommendedFragment;

    }

    @NonNull
    @Override
    public MyViewHolderRecommanded onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_subscription_recommanded, null);
        return new MyViewHolderRecommanded(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderRecommanded myViewHolderRecommanded, final int i) {


        myViewHolderRecommanded.tvTitle.setText(mRecommendedModelArrayList.get(i).getHeading());
        myViewHolderRecommanded.tvDescription.setText(mRecommendedModelArrayList.get(i).getDescription());
        myViewHolderRecommanded.tvCost.setText(mRecommendedModelArrayList.get(i).getCurrency() + " " + mRecommendedModelArrayList.get(i).getPrice());
        myViewHolderRecommanded.linearLayoutSubscription.setBackground(mContext.getResources().getDrawable(mRecommendedModelArrayList.get(i).getBackground()));
        myViewHolderRecommanded.tvDays.setText(mRecommendedModelArrayList.get(i).getDays() + " " + mContext.getString(R.string.lbl_days));

        myViewHolderRecommanded.linearLayoutSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myViewHolderRecommanded.linearLayoutBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetConnection.isInternetAvailable(mContext)) //returns true if internet available
                {

                    mMySubscriptionRecommendedFragment.ShowTaxList(i, mRecommendedModelArrayList.get(i).getPk_id());


                } else {

                    UtilityMethods.showInternetDialog(mContext);
                }


            }
        });

        myViewHolderRecommanded.tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = mInflater.inflate(R.layout.dialog_recommeneded_subscription_popup, null);
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


                ImageView iv_cancel = alertDialog.findViewById(R.id.iv_cancel);
                TextView tv_title = alertDialog.findViewById(R.id.tv_title);
                TextView tv_cost = alertDialog.findViewById(R.id.tv_cost);
                TextView tv_leads_count = alertDialog.findViewById(R.id.tv_leads_count);
                TextView tv_image_count = alertDialog.findViewById(R.id.tv_image_count);
                TextView tv_days = alertDialog.findViewById(R.id.tv_days);
                TextView tv_description = alertDialog.findViewById(R.id.tv_description);
                TextView tv_title_services = alertDialog.findViewById(R.id.tv_title_services);
                TextView tv_services = alertDialog.findViewById(R.id.tv_services);

                String serices = mRecommendedModelArrayList.get(i).getServices();

                if (serices.equals("")) {

                    tv_title_services.setVisibility(View.GONE);
                    tv_services.setVisibility(View.GONE);

                } else {

                    tv_title_services.setVisibility(View.VISIBLE);
                    tv_services.setVisibility(View.VISIBLE);
                    tv_services.setText(serices);
                }


                tv_title.setText(mRecommendedModelArrayList.get(i).getHeading());
                tv_cost.setText("Cost - " + mRecommendedModelArrayList.get(i).getCurrency() + " " + mRecommendedModelArrayList.get(i).getPrice());
                tv_days.setText("Days - " + mRecommendedModelArrayList.get(i).getDays());
                tv_leads_count.setText("Number of Leads - " + mRecommendedModelArrayList.get(i).getNofoleads());
                tv_image_count.setText(mContext.getString(R.string.lbl_number_of_images) + " - " + mRecommendedModelArrayList.get(i).getNoofimages());
                tv_description.setText(mRecommendedModelArrayList.get(i).getDescription());


                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecommendedModelArrayList.size();
    }


    public class MyViewHolderRecommanded extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.tv_details)
        TextView tvDetails;
        @BindView(R.id.tv_cost)
        TextView tvCost;
        @BindView(R.id.linear_layout_subscription)
        LinearLayout linearLayoutSubscription;
        @BindView(R.id.tv_buy_now)
        TextView tvBuyNow;
        @BindView(R.id.linear_layout_buy_now)
        LinearLayout linearLayoutBuyNow;
        @BindView(R.id.tv_days)
        TextView tvDays;

        public MyViewHolderRecommanded(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
