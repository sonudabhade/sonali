package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class EnquiryDetailsFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_lead_id)
    TextView tvLeadId;
    @BindView(R.id.tv_created_date)
    TextView tvCreatedDate;
    @BindView(R.id.tv_qty)
    TextView tvQty;
    @BindView(R.id.tv_price_range)
    TextView tvPriceRange;
    @BindView(R.id.tv_preferred)
    TextView tvPreferred;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_descripation)
    TextView tvDescripation;
    @BindView(R.id.ProgressBarNewLeadDetailsView)
    RelativeLayout ProgressBarNewLeadDetailsView;
    @BindView(R.id.tv_qty_text)
    TextView tvQtyText;
    @BindView(R.id.tv_unit_text)
    TextView tvUnitText;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_price_text)
    TextView tvPriceText;
    @BindView(R.id.tv_preferred_text)
    TextView tvPreferredText;
    @BindView(R.id.tv_buyer_text)
    TextView tvBuyerText;
   /* @BindView(R.id.linearLayoutTaxRow)
    LinearLayout linearLayoutTaxRow;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_seller_mobileno)
    TextView tvSellerMobileno;
    @BindView(R.id.tv_buyer_seller_email)
    TextView tvBuyerSellerEmail;
    @BindView(R.id.tv_seller_address)
    TextView tvSellerAddress;
    @BindView(R.id.linear_layout_seller_details)
    LinearLayout linearLayoutSellerDetails;
    @BindView(R.id.linear_layout_call)
    LinearLayout linearLayoutCall;
    @BindView(R.id.linear_layout_message)
    LinearLayout linearLayoutMessage;*/

    String UA_mobile = "", seller_fkid = "";

    String pk_id = "";
    @BindView(R.id.Lnr_Seller_Details)
    LinearLayout LnrSellerDetails;
    @BindView(R.id.linearLayoutTaxRow)
    LinearLayout linearLayoutTaxRow;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_seller_mobileno)
    TextView tvSellerMobileno;
    @BindView(R.id.tv_buyer_seller_email)
    TextView tvBuyerSellerEmail;
    @BindView(R.id.tv_seller_address)
    TextView tvSellerAddress;
    @BindView(R.id.linear_layout_call)
    LinearLayout linearLayoutCall;
    @BindView(R.id.linear_layout_message)
    LinearLayout linearLayoutMessage;
    @BindView(R.id.linear_layout_seller_details)
    LinearLayout linearLayoutSellerDetails;

    public EnquiryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enquiry_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Enquiry Details");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            tvProductName.setText(bundle.getString("product_name"));
            tvLeadId.setText("Enquiry ID :- " + bundle.getString("lead_id"));


            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date StartDate = null;
            try {
                StartDate = spf.parse(bundle.getString("created_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");


            tvCreatedDate.setText(spf.format(StartDate));
            pk_id = bundle.getString("pk_id");
            tvQty.setText(bundle.getString("qty"));
            tvUnit.setText(bundle.getString("attributes_type_name"));
            tvPriceRange.setText(bundle.getString("currency") + " " + bundle.getString("price_from") + " to " + bundle.getString("price_to"));
            tvPreferred.setText(bundle.getString("preferred"));
            tvType.setText(bundle.getString("type_of_buyer"));
            tvDescripation.setText(bundle.getString("descripation"));

            try {
                String paid_status = bundle.getString("paid_status");

                if (paid_status.equals("2")) {
                    LnrSellerDetails.setVisibility(VISIBLE);



                    /*String ContactArray = bundle.getString("contactArray");
                    JSONArray jsonArray = new JSONArray(ContactArray);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View SellerView = mInflater.inflate(R.layout.row_seller_details, null);

                        TextView tv_seller_name = SellerView.findViewById(R.id.tv_seller_name);
                        TextView tv_seller_mobileno = SellerView.findViewById(R.id.tv_seller_mobileno);
                        TextView tv_buyer_seller_email = SellerView.findViewById(R.id.tv_buyer_seller_email);
                        TextView tv_seller_address = SellerView.findViewById(R.id.tv_seller_address);
                        LinearLayout linear_layout_call = SellerView.findViewById(R.id.linear_layout_call);
                        LinearLayout linear_layout_message = SellerView.findViewById(R.id.linear_layout_message);


                        tv_seller_name.setText(jsonObject.getString("userName"));
                    *//*    UA_mobile = jsonObject.getString("UA_mobile");
                        seller_fkid = jsonObject.getString("fk_to_id");*//*
                        tv_seller_mobileno.setText(jsonObject.getString("UA_mobile"));
                        tv_buyer_seller_email.setText(jsonObject.getString("UA_email"));


                        linear_layout_call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                try {
                                    callIntent.setData(Uri.parse("tel:" + jsonObject.getString("UA_mobile")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(callIntent);

                            }
                        });
                        linear_layout_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ChatPersnoalFragment chatPersnoalFragment = new ChatPersnoalFragment();
                                Bundle bundle = new Bundle();
                                try {
                                    bundle.putString("Chat_With", jsonObject.getString("fk_to_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                bundle.putString("pk_id", pk_id);
                                chatPersnoalFragment.setArguments(bundle);

                                Constants.mMainActivity.ChangeFragments(chatPersnoalFragment, "ChatPersnoalFragment");


                            }
                        });

                        LnrSellerDetails.addView(SellerView);
                    }
*/


                    tvSellerName.setText(bundle.getString("userName"));

                    tvSellerMobileno.setText(bundle.getString("UA_mobile"));
                    tvBuyerSellerEmail.setText(bundle.getString("UA_email"));


                    linearLayoutCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            try {
                                callIntent.setData(Uri.parse("tel:" + bundle.getString("UA_mobile")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startActivity(callIntent);

                        }
                    });
                    linearLayoutMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ChatPersnoalFragment chatPersnoalFragment = new ChatPersnoalFragment();
                            Bundle bundle1 = new Bundle();
                            try {
                                bundle1.putString("Chat_With", bundle.getString("UA_pkey"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            bundle1.putString("pk_id", pk_id);
                            chatPersnoalFragment.setArguments(bundle1);


                            Log.e("Chat_With","Chat_With "+bundle.getString("UA_pkey"));
                            Log.e("pk_id","pk_id "+pk_id);
                            Constants.mMainActivity.ChangeFragments(chatPersnoalFragment, "ChatPersnoalFragment");


                        }
                    });



                } else {

                    LnrSellerDetails.setVisibility(GONE);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

  /*  @OnClick({R.id.linear_layout_call, R.id.linear_layout_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_call:

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + UA_mobile));
                startActivity(callIntent);

                break;
            case R.id.linear_layout_message:

                ChatPersnoalFragment chatPersnoalFragment = new ChatPersnoalFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Chat_With", seller_fkid);
                bundle.putString("pk_id", pk_id);
                chatPersnoalFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(chatPersnoalFragment, "ChatPersnoalFragment");


                break;
        }
    }*/
}
