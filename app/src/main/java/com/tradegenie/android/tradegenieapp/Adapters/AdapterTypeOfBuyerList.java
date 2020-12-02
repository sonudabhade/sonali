package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Models.PreferredModel;
import com.tradegenie.android.tradegenieapp.Models.TypeOfBuyerModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;


public class AdapterTypeOfBuyerList extends ArrayAdapter<TypeOfBuyerModel> {


    private Context mContext;
    private ArrayList<TypeOfBuyerModel> mTypeOfBuyerModelArrayList;
    private LayoutInflater mLayoutInflater;
    private int mResource;
    private int mTextViewResourceId;






    public AdapterTypeOfBuyerList(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<TypeOfBuyerModel> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext=context;
        mTypeOfBuyerModelArrayList=objects;
        mTextViewResourceId=textViewResourceId;
        mResource=resource;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        super.getCount();
        return mTypeOfBuyerModelArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.adapter_gender_spinner_row_select,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.classNameTextView);
        mTextView.setText(mTypeOfBuyerModelArrayList.get(position).getBuyer());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView=mLayoutInflater.inflate(R.layout.adapter_gender_spinner_row_select,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.classNameTextView);
        mTextView.setText(mTypeOfBuyerModelArrayList.get(position).getBuyer());
        return convertView;
    }

}