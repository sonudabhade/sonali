package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Models.ChatMessage;
import com.tradegenie.android.tradegenieapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterPersnonalMessage extends RecyclerView.Adapter<AdapterPersnonalMessage.ChatHolder> {


    Context context;
    ArrayList<ChatMessage> mChatList;

    public AdapterPersnonalMessage(Context context, ArrayList<ChatMessage> mChatList) {
        this.context = context;
        this.mChatList = mChatList;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_persnoal_chat_layout, viewGroup, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder chatHolder, int i) {


        ChatMessage chatMessage = mChatList.get(i);
        // If the message is a received message.
        if (chatMessage.MSG_TYPE_RECEIVED.equals(chatMessage.getMsgType())) {
            // Show received message in left linearlayout.
            chatHolder.txt_admin_title.setVisibility(View.GONE);
            chatHolder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            chatHolder.leftMsgTextView.setText(chatMessage.getMsgContent());
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            chatHolder.rightMsgLayout.setVisibility(LinearLayout.GONE);

           /* String date=chatMessage.getDateTime();
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate = null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            date = spf.format(newDate);*/
            chatHolder.txt_date_left.setText(chatMessage.getDateTime());
        }
        // If the message is a sent message.
        else if (chatMessage.MSG_TYPE_SENT.equals(chatMessage.getMsgType())) {
            // Show sent message in right linearlayout.
            chatHolder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            chatHolder.rightMsgTextView.setText(chatMessage.getMsgContent());
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            chatHolder.leftMsgLayout.setVisibility(LinearLayout.GONE);

            /*Log.e("check","Date"+chatMessage.getDateTime());

            String date=chatMessage.getDateTime();
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate = null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            date = spf.format(newDate);*/
            chatHolder.txt_date_right.setText(chatMessage.getDateTime());

        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        LinearLayout leftMsgLayout;

        LinearLayout rightMsgLayout;

        TextView leftMsgTextView;

        TextView rightMsgTextView;

        TextView txt_date_left;

        TextView txt_date_right;

        TextView txt_admin_title;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            if (itemView != null) {
                leftMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_msg_layout);
                rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_msg_layout);
                leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
                rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
                txt_date_left = (TextView) itemView.findViewById(R.id.txt_date_left);
                txt_date_right = (TextView) itemView.findViewById(R.id.txt_date_right);
                txt_admin_title = (TextView) itemView.findViewById(R.id.txt_admin);
            }
        }
    }


}
