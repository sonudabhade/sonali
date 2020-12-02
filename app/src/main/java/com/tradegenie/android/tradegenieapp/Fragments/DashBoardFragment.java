package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterValidateData;
import com.tradegenie.android.tradegenieapp.Models.ValidateDataModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DashBoardFragment extends Fragment {

    @BindView(R.id.relativeLayoutDashBoard)
    RelativeLayout relativeLayoutDashBoard;
    Unbinder unbinder;
    @BindView(R.id.iv_company_banner)
    ImageView ivCompanyBanner;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.iv_edit_profile)
    ImageView ivEditProfile;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_mobile_no)
    TextView tvMobileNo;
    @BindView(R.id.tv_total_products)
    TextView tvTotalProducts;
    @BindView(R.id.tv_my_lead)
    TextView tvMyLead;
    @BindView(R.id.tv_exp_date)
    TextView tvExpDate;
    @BindView(R.id.btn_upgrade)
    Button btnUpgrade;
    @BindView(R.id.iv_lead_manager)
    ImageView ivLeadManager;
    @BindView(R.id.view_line_one)
    View viewLineOne;
    @BindView(R.id.tv_lead_for_business)
    TextView tvLeadForBusiness;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_subscription_name)
    TextView tv_subscription_name;
    @BindView(R.id.tv_subscription_type)
    TextView tv_subscription_type;
    @BindView(R.id.tv_new_message)
    TextView tv_new_message;
    @BindView(R.id.linear_layout_profile)
    LinearLayout linearLayoutProfile;
    @BindView(R.id.tv_lead_manager)
    TextView tvLeadManager;
    @BindView(R.id.linear_layout_total_product)
    LinearLayout linearLayoutTotalProduct;
    @BindView(R.id.linear_layout_my_lead)
    LinearLayout linearLayoutMyLead;
    @BindView(R.id.ral_lead_manager)
    RelativeLayout ralLeadManager;
    @BindView(R.id.linear_layout_chat)
    LinearLayout linearLayoutChat;
    @BindView(R.id.linear_layout_subscription)
    LinearLayout linearLayoutSubscription;
    @BindView(R.id.recyclerview_validate_data)
    RecyclerView recyclerviewValidateData;

    private SessionManager mSessionManager;


    private ArrayList<ValidateDataModel> mValidateDataModelArrayList;
    private AdapterValidateData mAdapterValidateData;
    LinearLayoutManager linearLayoutValidateData;


    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, view);

        Constants.mMainActivity.setToolBarName(getString(R.string.dashboard));
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());

        //Check personal details and business details
        if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("")) {

            //UtilityMethods.showInfoToast(getActivity(), "Please complete your profile details.");


            /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
            Constants.mMainActivity.ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");*/

            Log.e("check==========>", "PersonalDetailsFragment");

        } else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")) {

            UtilityMethods.showInfoToast(getActivity(), "Please complete your business details first");


            BusinessDetailsFragment businessDetailsFragment = new BusinessDetailsFragment();
            Constants.mMainActivity.ChangeFragments(businessDetailsFragment, "BusinessDetailsFragment");

            Log.e("check==========>", "BusinessDetailsFragment");

        }


        if (InternetConnection.isInternetAvailable(getActivity())) {

            getDashboardDetails();


        } else {

            UtilityMethods.showInternetDialog(getActivity());

        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //Method to get Dashboard Details
    private void getDashboardDetails() {

        UtilityMethods.tuchOff(relativeLayoutDashBoard);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DASHBOARD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getDashboardDetails -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                        String image = jsonObject.getString("image");
                        String comp_name = jsonObject.getString("comp_name");
                        String mobile_no = jsonObject.getString("mobile_no");
                        String user_name = jsonObject.getString("user_name");
                        String total_product = jsonObject.getString("total_product");
                        String my_leads = jsonObject.getString("my_leads");
                        String subscription_name = jsonObject.getString("subscription_name");
                        String subscription_type = jsonObject.getString("subscription_type");
                        String exp_date = jsonObject.getString("exp_date");
                        String lead_manager = jsonObject.getString("lead_manager");
                        String new_message = jsonObject.getString("new_message");
                        String rating = jsonObject.getString("rating");
                        String logo_image = jsonObject.getString("logo_image");


                        String profile_status = jsonObject.getString("profile_status");
                        if (profile_status.equalsIgnoreCase("2")) {


                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("Please complete your profile details.");
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(

                                    R.string.OK,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                           /* PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                                            Constants.mMainActivity.ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");
*/

                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.setCanceledOnTouchOutside(false);
                            alert11.show();


                        }

                        tvCompanyName.setText(comp_name);
                        tvMobileNo.setText(mobile_no);
                        tvUserName.setText(user_name);
                        tvTotalProducts.setText(total_product);
                        tvMyLead.setText(my_leads);
                        tv_subscription_name.setText(subscription_name);


                        if(subscription_type.equals("0")){

                            tv_subscription_type.setText("No active subscription");
                        }else{
                            tv_subscription_type.setText(subscription_type);
                        }
                        tvExpDate.setText(exp_date);
                        tvLeadManager.setText(lead_manager + " "+getString(R.string.lead_for_your_business));
                        //tv_new_message.setText(new_message);
                        tvRating.setText(rating);


                        try {
                            Picasso.get().load(logo_image + image).placeholder(R.drawable.default_document).into(ivCompanyBanner);
                        } catch (Exception e) {

                        }

                        /*ViewTreeObserver vto = linearLayoutProfile.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    linearLayoutProfile.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                    linearLayoutProfile.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                                int width  = linearLayoutProfile.getMeasuredWidth();
                                int height = linearLayoutProfile.getMeasuredHeight();

                                ViewGroup.LayoutParams params = linearLayoutProfile.getLayoutParams();
                                params.height = 130;
                                viewLine.setLayoutParams(params);

                            }
                        });*/
                        mValidateDataModelArrayList = new ArrayList<>();
                        JSONArray badges_title = jsonObject.getJSONArray("badges_title");
                        for (int j = 0; j < badges_title.length(); j++) {


                            ValidateDataModel validateDataModel = new ValidateDataModel();

                            JSONObject jsonObjectuservalidateData = badges_title.getJSONObject(j);

                            validateDataModel.setPk_id(jsonObjectuservalidateData.getString("pk_id"));
                            validateDataModel.setTitle(jsonObjectuservalidateData.getString("title"));
                            validateDataModel.setImage(jsonObject.getString("badges_path") + jsonObjectuservalidateData.getString("image"));

                            mValidateDataModelArrayList.add(validateDataModel);
                        }

                        mAdapterValidateData = new AdapterValidateData(getActivity(), mValidateDataModelArrayList);
                        linearLayoutValidateData = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerviewValidateData.setLayoutManager(linearLayoutValidateData);
                        recyclerviewValidateData.setAdapter(mAdapterValidateData);


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(relativeLayoutDashBoard);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showInfoToast(getActivity(), getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                UtilityMethods.tuchOn(relativeLayoutDashBoard);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("uid", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

                Log.e("check", "======>" + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @OnClick(R.id.iv_edit_profile)
    public void onViewClicked() {

        BusinessDetailsMainFragment businessDetailsMainFragment = new BusinessDetailsMainFragment();
        Constants.mMainActivity.ChangeFragments(businessDetailsMainFragment, "BusinessDetailsMainFragment");

    }

    @OnClick({R.id.linear_layout_total_product, R.id.linear_layout_my_lead, R.id.ral_lead_manager, R.id.linear_layout_chat, R.id.linear_layout_subscription})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_total_product:


                Constants.mMainActivity.WhichFragmentIsopen = "Fragment Approved";

                ProductListFragment productListFragment = new ProductListFragment();
                Constants.mMainActivity.ChangeFragments(productListFragment, "ProductListFragment");

                break;
            case R.id.linear_layout_my_lead:

                Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";

                LeadManagmentMainFragment leadManagmentMainFragment = new LeadManagmentMainFragment();
                Constants.mMainActivity.ChangeFragments(leadManagmentMainFragment, "LeadManagmentMainFragment");

                break;
            case R.id.ral_lead_manager:

                Constants.mMainActivity.WhichLeadFragmentIsopen = "My New Fragment";

                LeadManagmentMainFragment leadManagmentMainFragment1 = new LeadManagmentMainFragment();
                Constants.mMainActivity.ChangeFragments(leadManagmentMainFragment1, "LeadManagmentMainFragment");

                break;
            case R.id.linear_layout_chat:

                ChatListFragment chatListFragment = new ChatListFragment();
                Constants.mMainActivity.ChangeFragments(chatListFragment, "ChatListFragment");

                break;

            case R.id.linear_layout_subscription:

                MySubscriptionMainFragment mySubscriptionMainFragment = new MySubscriptionMainFragment();
                Constants.mMainActivity.ChangeFragments(mySubscriptionMainFragment, "MySubscriptionMainFragment");

                break;
        }
    }


}
