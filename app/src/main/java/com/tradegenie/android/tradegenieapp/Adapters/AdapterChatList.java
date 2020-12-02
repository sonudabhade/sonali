package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Fragments.ChatPersnoalFragment;
import com.tradegenie.android.tradegenieapp.Models.ChatListModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 *
 */
public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyViewHolderChatList> {



    private Context mContext;
    private ArrayList<ChatListModel> mChatListModelArrayList;
    private SessionManager mSessionManager;

    public AdapterChatList(Context mContext, ArrayList<ChatListModel> ChatListModelArrayList) {
        this.mContext = mContext;
        this.mChatListModelArrayList = ChatListModelArrayList;
        mSessionManager=new SessionManager(mContext);
    }

    @NonNull
    @Override
    public MyViewHolderChatList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chat_list, null);
        return new MyViewHolderChatList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderChatList myViewHolderChatList, final int i) {

        try {
            Picasso.get().load(mChatListModelArrayList.get(i).getUA_Image()).placeholder(R.drawable.default_user_profile).into(myViewHolderChatList.ivProfileImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myViewHolderChatList.tvBuyerName.setText(mChatListModelArrayList.get(i).getUserName());
        myViewHolderChatList.tvMessage.setText(mChatListModelArrayList.get(i).getMessage());
        myViewHolderChatList.tvTime.setText(mChatListModelArrayList.get(i).getMessageTime());

        myViewHolderChatList.relativeLayoutTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ChatPersnoalFragment chatPersnoalFragment=new ChatPersnoalFragment();
                Bundle bundle=new Bundle();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    if (mChatListModelArrayList.get(i).getFk_to_id().equals(mSessionManager.getStringData(Constants.KEY_SELLER_UID))) {

                        bundle.putString("Chat_With", mChatListModelArrayList.get(i).getFk_from_id());
                        bundle.putString("pk_id", mChatListModelArrayList.get(i).getPk_id());


                    } else {

                        bundle.putString("Chat_With", mChatListModelArrayList.get(i).getFk_to_id());
                        bundle.putString("pk_id", mChatListModelArrayList.get(i).getPk_id());

                    }

                }else {

                    if (mChatListModelArrayList.get(i).getFk_to_id().equals(mSessionManager.getStringData(Constants.KEY_BUYER_UID))) {

                        bundle.putString("Chat_With", mChatListModelArrayList.get(i).getFk_from_id());
                        bundle.putString("pk_id", mChatListModelArrayList.get(i).getPk_id());


                    } else {

                        bundle.putString("Chat_With", mChatListModelArrayList.get(i).getFk_to_id());
                        bundle.putString("pk_id", mChatListModelArrayList.get(i).getPk_id());
                    }


                }

                chatPersnoalFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(chatPersnoalFragment,"ChatPersnoalFragment");

            }
        });

    }

    @Override
    public int getItemCount() {
        return mChatListModelArrayList.size();
    }


    public class MyViewHolderChatList extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_profile_image_view)
        CircleImageView ivProfileImageView;
        @BindView(R.id.tv_buyer_name)
        TextView tvBuyerName;
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.linerLayout_teacherInfo_Adapter_TeacherList)
        LinearLayout linerLayoutTeacherInfoAdapterTeacherList;
        @BindView(R.id.relative_layout_top)
        RelativeLayout relativeLayoutTop;
        @BindView(R.id.maintoolbar)
        LinearLayout maintoolbar;

        public MyViewHolderChatList(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
