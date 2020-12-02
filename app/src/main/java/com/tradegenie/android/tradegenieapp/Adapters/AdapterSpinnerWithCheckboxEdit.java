package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Fragments.AddNewProduct;
import com.tradegenie.android.tradegenieapp.Fragments.AddNewProductEdit;
import com.tradegenie.android.tradegenieapp.Models.StateVO;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterSpinnerWithCheckboxEdit extends ArrayAdapter<StateVO> {

    private Context mContext;
    public ArrayList<StateVO> listState;
    private AdapterSpinnerWithCheckboxEdit mAdapterSpinnerWithCheckbox;
    private boolean isFromView = false;
    private AddNewProductEdit mAddNewProductEdit;


    public AdapterSpinnerWithCheckboxEdit(Context context, int resource, List<StateVO> objects, AddNewProductEdit addNewProductEdit) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<StateVO>) objects;
        this.mAdapterSpinnerWithCheckbox = this;
        mAddNewProductEdit = addNewProductEdit;
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
        LayoutInflater layoutInflator = LayoutInflater.from(mContext);
        convertView = layoutInflator.inflate(R.layout.adapter_row_spinner_with_check_box, null);
        holder = new ViewHolder();
        holder.mTextView = (TextView) convertView
                .findViewById(R.id.text);
        holder.mCheckBox = (CheckBox) convertView
                .findViewById(R.id.checkbox);
        convertView.setTag(holder);
        /*} else {
            holder = (ViewHolder) convertView.getTag();
        }*/

        holder.mTextView.setText(listState.get(position).getClass_name());

        // To check weather checked event fire from getview() or user input
        isFromView = true;


        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }

        Log.e("error", "sandeep===> position " + position + " " + listState.get(position).isSelected());

        if (listState.get(position).isSelected()) {

            holder.mCheckBox.setChecked(true);

        } else {

            holder.mCheckBox.setChecked(false);

        }


        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                Log.e("error", "onCheckedChanged " + position + " " + isChecked);

                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }

                mAddNewProductEdit.getModeofPayCount();
            }
        });


        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }


}
