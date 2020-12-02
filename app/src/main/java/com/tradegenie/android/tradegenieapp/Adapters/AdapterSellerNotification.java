package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Models.SellerNotificationModel;
import com.tradegenie.android.tradegenieapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 *
 */
public class AdapterSellerNotification extends RecyclerView.Adapter<AdapterSellerNotification.MyViewHolderProductListPending> {



    private Context mContext;
    private ArrayList<SellerNotificationModel> mSellerNotificationModelArrayList;

    public AdapterSellerNotification(Context mContext, ArrayList<SellerNotificationModel> sellerNotificationModelArrayList) {
        this.mContext = mContext;
        this.mSellerNotificationModelArrayList = sellerNotificationModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderProductListPending onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_notification_item, null);
        return new MyViewHolderProductListPending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderProductListPending myViewHolderProductListPending, int i) {

        //2019-06-24 16:59:08

        //Log.e("check","date"+mSellerNotificationModelArrayList.get(i).getCreated_date());


        String date = mSellerNotificationModelArrayList.get(i).getCreated_date();
        String subject = mSellerNotificationModelArrayList.get(i).getSubject();

        if (subject.equals("null")){

            myViewHolderProductListPending.tvNotificationSubject.setVisibility(View.GONE);

        }else {

            myViewHolderProductListPending.tvNotificationSubject.setVisibility(View.VISIBLE);
            myViewHolderProductListPending.tvNotificationSubject.setText(subject);


        }

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        date = spf.format(newDate);


        myViewHolderProductListPending.tvDateAdapterStudentNotification.setText(date);


        myViewHolderProductListPending.tvNotificationAdapterStudentNotification.setText(mSellerNotificationModelArrayList.get(i).getMessage());


    }

    @Override
    public int getItemCount() {
        return mSellerNotificationModelArrayList.size();
    }


    public class MyViewHolderProductListPending extends RecyclerView.ViewHolder {

        @BindView(R.id.teacher_profile_ImageView_Adapter_TeacherList)
        CircleImageView teacherProfileImageViewAdapterTeacherList;
        @BindView(R.id.tv_notification_subject)
        TextView tvNotificationSubject;
        @BindView(R.id.tv_notification_adapter_student_notification)
        TextView tvNotificationAdapterStudentNotification;
        @BindView(R.id.tv_date_adapter_student_notification)
        TextView tvDateAdapterStudentNotification;
        @BindView(R.id.linerLayout_notication_text)
        LinearLayout linerLayoutNoticationText;
        public MyViewHolderProductListPending(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
