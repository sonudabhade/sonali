package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Models.NavItem;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

public class DrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NavItem> mNavItems;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        } else {
            view = convertView;
        }

        TextView titleView = view.findViewById(R.id.title);
        TextView subtitleView = view.findViewById(R.id.subTitle);
        ImageView iconView = view.findViewById(R.id.icon);
        //RelativeLayout container=view.findViewById(R.id.relativeLayout_container_drawer);


        titleView.setText(mNavItems.get(position).mTitle);
        subtitleView.setText(mNavItems.get(position).mSubtitle);
        iconView.setImageResource(mNavItems.get(position).mIcon);


        if (mNavItems.get(position).getMessageArrived() && mNavItems.get(position).mTitle.equals("Notification")) {
            iconView.setImageResource(R.drawable.notifications_red_dot);
        } else {
            iconView.setImageResource(mNavItems.get(position).mIcon);
        }


        if (mNavItems.get(position).getMessageArrived() && mNavItems.get(position).mTitle.equals(mContext.getString(R.string.lead_management))) {
            iconView.setImageResource(R.drawable.ic_lead_management_red_dot);
        } else {
            iconView.setImageResource(mNavItems.get(position).mIcon);
        }
        return view;
    }
}