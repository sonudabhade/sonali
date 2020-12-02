package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Fragments.AddNewProduct;
import com.tradegenie.android.tradegenieapp.Fragments.AddNewProductEdit;
import com.tradegenie.android.tradegenieapp.Models.ModeOfSupplyModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterSpinnerModeofSupplyEdit extends ArrayAdapter<ModeOfSupplyModel> {

    private Context mContext;
    public ArrayList<ModeOfSupplyModel> listState;
    private AdapterSpinnerModeofSupplyEdit mAdapterSpinnerModeofSupply;
    private boolean isFromView = false;
    private AddNewProductEdit mAddNewProductEdit;


    public AdapterSpinnerModeofSupplyEdit(Context context, int resource, List<ModeOfSupplyModel> objects, AddNewProductEdit addNewProductEdit) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<ModeOfSupplyModel>) objects;
        this.mAdapterSpinnerModeofSupply = this;
        mAddNewProductEdit=addNewProductEdit;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.adapter_row_spinner_with_check_box, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getClass_name());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }

                mAddNewProductEdit.getModeofSupplyCount();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }


}
