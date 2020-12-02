package com.tradegenie.android.tradegenieapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterChatList;
import com.tradegenie.android.tradegenieapp.Adapters.AdapterEnquiryList;
import com.tradegenie.android.tradegenieapp.Models.ChatListModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChatListFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view_chat_list)
    RecyclerView recyclerViewChatList;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.ProgressBarChatList)
    RelativeLayout ProgressBarChatList;

    private SessionManager mSessionManager;

    private LinearLayoutManager linearLayoutManager;

    private ArrayList<ChatListModel> mChatListModelArrayList;
    private AdapterChatList mAdapterChatList;


    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Chat");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        mSessionManager = new SessionManager(getActivity());

        mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED_MESSAGE, false);

        mChatListModelArrayList = new ArrayList<>();
        mAdapterChatList = new AdapterChatList(getActivity(), mChatListModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewChatList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewChatList.setAdapter(mAdapterChatList);
        recyclerViewChatList.setLayoutManager(linearLayoutManager);

        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            //Check personal details and business details
            if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_SELLER_COUNTRY).equals("") /*|| mSessionManager.getStringData(Constants.KEY_SELLER_CITY).equals("")*/) {

                //UtilityMethods.showInfoToast(getActivity(), "Please complete your profile details.");

                /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                Constants.mMainActivity.ChangeFragments(personalDetailsFragment, "PersonalDetailsFragment");*/


            } else if (mSessionManager.getStringData(Constants.KEY_SELLER_BUSINESS_COUNTY).equals("")) {

                UtilityMethods.showInfoToast(getActivity(), "Please complete your business details first");


                BusinessDetailsFragment businessDetailsFragment = new BusinessDetailsFragment();
                Constants.mMainActivity.ChangeFragments(businessDetailsFragment, "BusinessDetailsFragment");


            }

        } else {

            if (mSessionManager.getStringData(Constants.KEY_BUYER_USER_NAME).equals("") || mSessionManager.getStringData(Constants.KEY_BUYER_COUNTRY).equals("")) {

                UtilityMethods.showInfoToast(getActivity(), "Please complete your profile details.");


                /*BuyerPersonalDetailsFragment buyerPersonalDetailsFragment = new BuyerPersonalDetailsFragment();
                Constants.mMainActivity.ChangeFragments(buyerPersonalDetailsFragment, "BuyerPersonalDetailsFragment");*/


            }


        }

        if (InternetConnection.isInternetAvailable(getActivity())) //returns true if internet available
        {

            getChatList();

        } else {

            UtilityMethods.showInternetDialog(getActivity());
        }


        return view;
    }


    //Method to get chat list
    private void getChatList() {

        UtilityMethods.tuchOff(ProgressBarChatList);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHATING_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "getChatList -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String profilepic=jsonObject.getString("profilepic");

                        JSONArray jsonArrayUserresult=jsonObject.getJSONArray("userresult");

                        for (int i=0;i<jsonArrayUserresult.length();i++){

                            JSONObject jsonObjectUserresult=jsonArrayUserresult.getJSONObject(i);

                            if (jsonObjectUserresult.getString("fk_from_id").equals(jsonObjectUserresult.getString("fk_to_id"))){


                            }else {

                                ChatListModel chatListModel = new ChatListModel();
                                chatListModel.setPk_id(jsonObjectUserresult.getString("pk_id"));
                                chatListModel.setFk_from_id(jsonObjectUserresult.getString("fk_from_id"));
                                chatListModel.setFk_to_id(jsonObjectUserresult.getString("fk_to_id"));
                                chatListModel.setMessage(jsonObjectUserresult.getString("message"));
                                chatListModel.setUA_Image(profilepic + jsonObjectUserresult.getString("UA_Image"));
                                chatListModel.setUserName(jsonObjectUserresult.getString("userName"));
                                if(!jsonObjectUserresult.isNull("created_date")) {
                                    chatListModel.setMessageTime(ConvertDate(jsonObjectUserresult.getString("created_date")));
                                }

                              /*  if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                                    if (jsonObjectUserresult.getString("fk_from_id").equals(mSessionManager.getStringData(Constants.KEY_SELLER_UID))) {

                                        chatListModel.setUA_Image(profilepic + jsonObjectUserresult.getString("toimage"));
                                        chatListModel.setUserName(jsonObjectUserresult.getString("toname"));
                                    } else {
                                        chatListModel.setUA_Image(profilepic + jsonObjectUserresult.getString("UA_Image"));
                                        chatListModel.setUserName(jsonObjectUserresult.getString("userName"));
                                    }

                                } else {

                                    if (jsonObjectUserresult.getString("fk_from_id").equals(mSessionManager.getStringData(Constants.KEY_BUYER_UID))) {

                                        chatListModel.setUA_Image(profilepic + jsonObjectUserresult.getString("toimage"));
                                        chatListModel.setUserName(jsonObjectUserresult.getString("toname"));
                                    } else {
                                        chatListModel.setUA_Image(profilepic + jsonObjectUserresult.getString("UA_Image"));
                                        chatListModel.setUserName(jsonObjectUserresult.getString("userName"));
                                    }

                                }
*/

                                mChatListModelArrayList.add(chatListModel);

                            }
                        }


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        //UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("message"));
                    }

                    mAdapterChatList.notifyDataSetChanged();

                    if (mChatListModelArrayList.size()==0){

                        textViewErrorMessage.setVisibility(VISIBLE);
                        recyclerViewChatList.setVisibility(GONE);

                    }else {

                        textViewErrorMessage.setVisibility(GONE);
                        recyclerViewChatList.setVisibility(VISIBLE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                UtilityMethods.tuchOn(ProgressBarChatList);


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
                UtilityMethods.tuchOn(ProgressBarChatList);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));

                }else {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));

                }
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

    //Method to convert date
    private String ConvertDate(String created_at) {

        try {
            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = originalFormat.parse(created_at);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formattedDate = targetFormat.format(date);

            return formattedDate;
        }catch (Exception e){e.printStackTrace();}
        return "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
