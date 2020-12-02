package com.tradegenie.android.tradegenieapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.tradegenie.android.tradegenieapp.Adapters.AdapterPersnonalMessage;
import com.tradegenie.android.tradegenieapp.Models.ChatMessage;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.KeyboardUtils;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tradegenie.android.tradegenieapp.Utility.Constants.OpenedChatId;

public class ChatPersnoalFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.maintoolbar)
    LinearLayout maintoolbar;
    @BindView(R.id.chat_recycler_view)
    RecyclerView chatRecyclerView;
    @BindView(R.id.chat_input_msg)
    EditText chatInputMsg;
    @BindView(R.id.imgsend)
    ImageView imgsend;
    boolean isSwipeRefresh = false;
    @BindView(R.id.layout_chat_send_container)
    LinearLayout layoutChatSendContainer;
    ArrayList<ChatMessage> mchatlist = new ArrayList<>();
    AdapterPersnonalMessage adapterPersnonalMessage;
    @BindView(R.id.teacher_profile_student_personal_chat)
    CircleImageView teacherProfileStudentPersonalChat;
    ProgressDialog progressBar;
    @BindView(R.id.relative_layout_top)
    RelativeLayout relativeLayoutTop;
    @BindView(R.id.linerLayout_teacherInfo_Adapter_TeacherList)
    LinearLayout linerLayoutTeacherInfoAdapterTeacherList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.relativeLayoutChatPersonal)
    RelativeLayout relativeLayoutChatPersonal;
    @BindView(R.id.tv_buyer_name)
    TextView tvBuyerName;
    @BindView(R.id.tv_buyer_mobile_no)
    TextView tvBuyerMobileNo;
    @BindView(R.id.tv_buyer_address)
    TextView tvBuyerAddress;
    @BindView(R.id.linear_layout_location)
    LinearLayout linearLayoutLocation;
    private SessionManager mSessionManager;
    Bundle bundle;
    int OFFSET = 0;
    private String mChat_With = "", pk_id = "";


    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persnoal_chat_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarName("Message");
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setNotificationButton(GONE);
        mSessionManager = new SessionManager(getActivity());
        Constants.mMainActivity.setToolBarPostEnquiryHide();

        Constants.mMainActivity.SelectMessageTab();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatRecyclerView.setLayoutManager(layoutManager);
        getActivity().registerReceiver(this.broadCastNewMessage, new IntentFilter("bcNewMessage"));


        progressBar = new ProgressDialog(getActivity());
        progressBar.setMessage("Please wait");
        progressBar.setCancelable(false);


        adapterPersnonalMessage = new AdapterPersnonalMessage(getActivity(), mchatlist);
        chatRecyclerView.setAdapter(adapterPersnonalMessage);

        bundle = getArguments();
        if (bundle != null) {

            mChat_With = (String) bundle.getString("Chat_With");
            pk_id = (String) bundle.getString("pk_id");

        }
        OpenedChatId = mChat_With;

        if (InternetConnection.isInternetAvailable(getActivity())) {
            UtilityMethods.tuchOff(relativeLayoutChatPersonal);

            GetChatList();
        } else {
            UtilityMethods.showInternetDialog(getActivity());
        }


        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swiperefresh.setRefreshing(false);


                        if (InternetConnection.isInternetAvailable(getActivity())) {

                            isSwipeRefresh = true;
                            progressBar = new ProgressDialog(getActivity());
                            progressBar.setMessage("Please wait");
                            progressBar.setCancelable(false);
                            progressBar.show();
                            OFFSET = OFFSET + 50;
                            GetChatList();
                        } else {
                            UtilityMethods.showInternetDialog(getActivity());
                        }

                    }
                }
        );

        chatInputMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    maintoolbar.setVisibility(GONE);

                    chatRecyclerView.scrollToPosition(mchatlist.size() - 1);

                    chatInputMsg.requestFocus();

                    //  maintoolbar.setVisibility(VISIBLE);
                }
            }
        });

        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String msgContent = chatInputMsg.getText().toString().trim();
                if (!TextUtils.isEmpty(msgContent)) {


                    if (InternetConnection.isInternetAvailable(getActivity())) {


                        // Add a new sent message to the list.
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setMsgType(ChatMessage.MSG_TYPE_SENT);
                        chatMessage.setMsgContent(msgContent);
                        chatMessage.setDateTime(ConvertDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));


                        mchatlist.add(chatMessage);

                        int newMsgPosition = mchatlist.size() - 1;

                        // Notify recycler view insert one new data.
                        adapterPersnonalMessage.notifyItemInserted(newMsgPosition);

                        // Scroll RecyclerView to the last message.
                        chatRecyclerView.scrollToPosition(newMsgPosition);

                        // Empty the input edit text box.
                        chatInputMsg.setText("");

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                        maintoolbar.setVisibility(VISIBLE);

                        SendMessage(msgContent);
                    }


                }
            }
        });


        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                Log.d("keyboard", "keyboard visible: " + isVisible);

                if (!isVisible) {
                    try {
                        Log.e("check", "=========>" + isVisible);
                        maintoolbar.setVisibility(VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(broadCastNewMessage);
    }

    @Override
    public void onPause() {
        super.onPause();

        OpenedChatId = "";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public void ShowTopView() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        maintoolbar.setVisibility(VISIBLE);

    }

    BroadcastReceiver broadCastNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (InternetConnection.isInternetAvailable(getActivity())) {
                //  UtilityMethods.tuchOff(realtiveLayoutProgressRegister);
                mchatlist.clear();
                OFFSET = 0;
                adapterPersnonalMessage.notifyDataSetChanged();


                //  progressBar.show();
                GetChatList();
            } else {
                UtilityMethods.showInternetDialog(getActivity());
            }
        }
    };


    //Method to get chat list
    private void GetChatList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_CHAT_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "GetChatList response 555-->>" + response);


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {

                        String profilepic = jsonObject.getString("profilepic");

                        try {
                            imgsend.setEnabled(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {


                            JSONArray jsonArrayFromlist = jsonObject.getJSONArray("fromlist");

                            for (int i = 0; i < jsonArrayFromlist.length(); i++) {

                                JSONObject jsonObjectFromlist = jsonArrayFromlist.getJSONObject(i);

                                tvBuyerName.setText(jsonObjectFromlist.getString("userName"));
                                tvBuyerMobileNo.setText(jsonObjectFromlist.getString("UA_mobile"));

                                if (jsonObjectFromlist.getString("UA_Address").equals("")) {

                                    linearLayoutLocation.setVisibility(GONE);

                                } else {

                                    tvBuyerAddress.setText(jsonObjectFromlist.getString("UA_Address"));


                                }

                                profilepic = profilepic + jsonObjectFromlist.getString("UA_Image");
                            }


                            try {
                                Picasso.get().load(profilepic).placeholder(R.drawable.default_user_profile).into(teacherProfileStudentPersonalChat);
                            } catch (Exception e) {

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        int lastcount = 0, newMsgCount = 0;

                        if (isSwipeRefresh) {
//copy data in new arraylist and append existing array to it. and then copy all array to main array list

                            ArrayList<ChatMessage> mchatlistDummy = new ArrayList<>();

                            try {
                                JSONArray chat_data = jsonObject.getJSONArray("arraymsg");
                                for (int i = 0; i < chat_data.length(); i++) {
                                    JSONObject chatObj = chat_data.getJSONObject(i);
                                    ChatMessage chatMessage = new ChatMessage();

                                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                                        if (chatObj.getString("created_by").equals(mSessionManager.getStringData(Constants.KEY_SELLER_UID))) {
                                            //Msg sent by me..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_SENT);

                                        } else {
                                            //Msg sent by other..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_RECEIVED);
                                        }

                                    } else {

                                        if (chatObj.getString("created_by").equals(mSessionManager.getStringData(Constants.KEY_BUYER_UID))) {
                                            //Msg sent by me..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_SENT);

                                        } else {
                                            //Msg sent by other..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_RECEIVED);
                                        }

                                    }
                                    chatMessage.setMsgContent(chatObj.getString("message"));

                                    String date = chatObj.getString("created_date");
                                 /*   SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date newDate = null;
                                    try {
                                        newDate = spf.parse(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
                                    date = targetFormat.format(newDate);
*/


                                    chatMessage.setDateTime(ConvertDate(date));

                                    mchatlistDummy.add(chatMessage);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            newMsgCount = mchatlistDummy.size();

                            for (int j = 0; j < mchatlist.size(); j++) {
                                mchatlistDummy.add(mchatlist.get(j));
                            }


                            mchatlist.clear();
                            for (int k = 0; k < mchatlistDummy.size(); k++) {
                                mchatlist.add(mchatlistDummy.get(k));
                            }

                            lastcount = mchatlist.size() - newMsgCount;

                        } else {

                            try {
                                JSONArray chat_data = jsonObject.getJSONArray("arraymsg");
                                for (int i = 0; i < chat_data.length(); i++) {
                                    JSONObject chatObj = chat_data.getJSONObject(i);
                                    ChatMessage chatMessage = new ChatMessage();

                                    if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {


                                        if (chatObj.getString("created_by").equals(mSessionManager.getStringData(Constants.KEY_SELLER_UID))) {
                                            //Msg sent by me..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_SENT);

                                        } else {
                                            //Msg sent by other..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_RECEIVED);
                                        }

                                    } else {

                                        if (chatObj.getString("created_by").equals(mSessionManager.getStringData(Constants.KEY_BUYER_UID))) {
                                            //Msg sent by me..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_SENT);

                                        } else {
                                            //Msg sent by other..
                                            chatMessage.setMsgType(ChatMessage.MSG_TYPE_RECEIVED);
                                        }

                                    }
                                    chatMessage.setMsgContent(chatObj.getString("message"));
                                    String date = chatObj.getString("created_date");
                                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date newDate = null;
                                    try {
                                        newDate = spf.parse(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                    date = spf.format(newDate);

                                    chatMessage.setDateTime(date);
                                    mchatlist.add(chatMessage);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (OFFSET == 0) {
                            adapterPersnonalMessage = new AdapterPersnonalMessage(getActivity(), mchatlist);
                            chatRecyclerView.setAdapter(adapterPersnonalMessage);
                        } else {
                            adapterPersnonalMessage.notifyDataSetChanged();
                        }

                        if (isSwipeRefresh) {
                            chatRecyclerView.scrollToPosition(lastcount);

                        } else {
                            // Scroll RecyclerView to the last message.
                            chatRecyclerView.scrollToPosition(mchatlist.size() - 1);
                        }

                        isSwipeRefresh = false;

                        imgsend.setEnabled(true);
                    } else if (jsonObject.getString("error_code").equals("2")) {

                        /*final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("This teacher is not available to chat");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(

                                R.string.OK,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        ChatSupportFragment chatSupportFragment = new ChatSupportFragment();
                                        Constants.mMainActivity.changeFragment(chatSupportFragment, "ChatSupportFragment");

                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.setCanceledOnTouchOutside(false);
                        alert11.show();
*/
                    } else if (jsonObject.getString("error_code").equals("3")) {

                        /*String profilepic_base_url = jsonObject.getString("profilepic_base_url");

                        JSONObject teacherDataJSONObject = jsonObject.getJSONObject("teacherData");


                        txtTeacherNameStudentChat.setText(teacherDataJSONObject.getString("fullname"));
                        String profilepic = teacherDataJSONObject.getString("profilepic");

                        if (!profilepic.equals("null") || !profilepic.equals("")) {
                            try {
                                Picasso.get().load(profilepic_base_url + profilepic).placeholder(R.drawable.user_profile_toolbar_icon).into(teacherProfileStudentPersonalChat);

                            } catch (Exception e) {

                                Picasso.get().load(R.drawable.user_profile_toolbar_icon).placeholder(R.drawable.user_profile_toolbar_icon).into(teacherProfileStudentPersonalChat);

                            }

                        }*/
                    } else if (jsonObject.getString("error_code").equals("4")) {
                        UtilityMethods.showInfoToast(getActivity(), "");
                    } else if (jsonObject.getString("error_code").equals("10")) {
                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();


                }

                try {
                    progressBar.dismiss();
                } catch (Exception e) {

                }
                UtilityMethods.tuchOn(relativeLayoutChatPersonal);


                if (adapterPersnonalMessage == null) {
                    adapterPersnonalMessage = new AdapterPersnonalMessage(getActivity(), mchatlist);
                    chatRecyclerView.setAdapter(adapterPersnonalMessage);
                } else {
                    adapterPersnonalMessage.notifyDataSetChanged();
                }
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

                UtilityMethods.tuchOn(relativeLayoutChatPersonal);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("fk_from_id", mChat_With);
                    params.put("fk_to_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                    params.put("offset", String.valueOf(OFFSET));
                    params.put("limit", "50");

                } else {

                    params.put("fk_from_id", mChat_With);
                    params.put("fk_to_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));
                    params.put("offset", String.valueOf(OFFSET));
                    params.put("limit", "50");

                }

                Log.e("check", "=======>" + params);

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

        Volley.newRequestQueue(

                getActivity()).

                add(stringRequest).

                setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Method to convert date
    private String ConvertDate(String created_at) {


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
    }

    //Method to send the msg
    private void SendMessage(final String msgContent) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHAT_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "response -->>" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {
                    } else if (jsonObject.getString("error_code").equals("4")) {
                    } else if (jsonObject.getString("error_code").equals("10")) {
                        UtilityMethods.UserInactivePopup(getActivity());
                    } else {
                        UtilityMethods.showInfoToast(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }
                UtilityMethods.tuchOn(relativeLayoutChatPersonal);

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

                UtilityMethods.tuchOn(relativeLayoutChatPersonal);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("fk_from_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));
                    params.put("message", msgContent);
                    params.put("fk_to_id", mChat_With);
                    params.put("chatlisting_pk_id", pk_id);

                } else {


                    params.put("fk_from_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));
                    params.put("message", msgContent);
                    params.put("fk_to_id", mChat_With);
                    params.put("chatlisting_pk_id", pk_id);

                }

                Log.e("Params", "URL ==========> " + params.toString());
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

    @OnClick(R.id.chat_input_msg)
    public void onViewClicked() {
        Log.e("chat_input_msg", "chat_input_msg");
        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        maintoolbar.setVisibility(GONE);

        chatRecyclerView.scrollToPosition(mchatlist.size() - 1);

        chatInputMsg.requestFocus();
        // maintoolbar.setVisibility(VISIBLE);
    }
}
