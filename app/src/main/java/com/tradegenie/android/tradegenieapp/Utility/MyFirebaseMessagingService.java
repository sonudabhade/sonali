package com.tradegenie.android.tradegenieapp.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tradegenie.android.tradegenieapp.Activity.MainActivity;
import com.tradegenie.android.tradegenieapp.R;

import org.json.JSONObject;

import java.util.Random;

import static com.tradegenie.android.tradegenieapp.Utility.Constants.OpenedChatId;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    SessionManager mSessionManager;
    private NotificationChannel mChannel;
    private NotificationManager notifManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        mSessionManager = new SessionManager(getApplicationContext());

        Log.e("----->", "onMessageReceived Data: " + remoteMessage.getData().toString());

        if (remoteMessage == null)
            return;


        if (remoteMessage.getData().toString() != null) {
            Log.e(TAG, "data Body: " + remoteMessage.getData().toString());

            String response = remoteMessage.getData().toString();
            try {

                String JSONResponse = response.substring((response.indexOf("{message=") + 6), response.length() - 1);

                Log.e("check", "JSONResponse=========>" + JSONResponse);

                JSONObject ResponseObj = new JSONObject(JSONResponse);

                //String topic_or_course_id=null,fk_student_id=null,fk_teacher_id = null, message = null, title = null, redirecttype = null, usertype = null;

                String message, pid, redirect_type, subject, from_id;

                try {
                    message = ResponseObj.getString("product");
                } catch (Exception e) {
                    message = "";

                }
                try {
                    pid = ResponseObj.getString("pid");
                } catch (Exception e) {
                    pid = "";

                }

                try {
                    redirect_type = ResponseObj.getString("redirect_type");
                } catch (Exception e) {
                    redirect_type = "";

                }
                try {
                    subject = ResponseObj.getString("subject");
                } catch (Exception e) {
                    subject = "";

                }
                try {
                    from_id = ResponseObj.getString("from_id");
                } catch (Exception e) {
                    from_id = "";

                }

                /*
                try {
                    redirecttype = ResponseObj.getString("redirecttype");
                } catch (Exception e) {
                    redirecttype = "";

                }
                try {
                    usertype = ResponseObj.getString("usertype");
                } catch (Exception e) {
                    usertype = "";

                }

                try {
                    topic_or_course_id = ResponseObj.getString("topic_or_course_id");
                } catch (Exception e) {
                    topic_or_course_id = "";

                }*/


                try {
                    mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (redirect_type.equalsIgnoreCase("Approved")) {


                } else if (redirect_type.equalsIgnoreCase("Rejected")) {


                } else if (redirect_type.equalsIgnoreCase("productadd")) {

                } else if (redirect_type.equalsIgnoreCase("notification")) {


                } else if (redirect_type.equalsIgnoreCase("newlead")) {

                    try {
                        mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED_LEADS, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (redirect_type.equalsIgnoreCase("leadreceived")) {
                    try {
                        mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED_LEADS, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (redirect_type.equalsIgnoreCase("enquiry")) {


                } else if (redirect_type.equalsIgnoreCase("subscription")) {
                    try {
                        mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED_LEADS, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (redirect_type.equalsIgnoreCase("post requirement")) {


                } else if (redirect_type.equalsIgnoreCase("subscription_buy")) {


                } else if (redirect_type.equalsIgnoreCase("chat_insert")) {

                    try {
                        mSessionManager.putBooleanData(Constants.IS_NOTIFICATION_RECEIVED_MESSAGE, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (redirect_type.equalsIgnoreCase("added")) {


                }


                try {
                    Constants.mMainActivity.RefreshBottomMenuForNotificationDot();
                } catch (Exception e) {

                }

                //Log.e("message", "message " + message + " title " + title + " redirecttype " + redirecttype);
                //handleNotification(topic_or_course_id,fk_student_id,fk_teacher_id,message, title, redirecttype, usertype);
                handleNotification(message, pid, redirect_type, subject, from_id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    //Parse the response and send notification
    private void handleNotification(String message, String pid, String redirect_type, String subject, String from_id) {

        Log.e("OpenedChatId","OpenedChatId "+OpenedChatId);
        Log.e("from_id","from_id "+from_id);
        if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

            if (redirect_type.equals("chat_insert") && from_id.equals(OpenedChatId)) {
                this.sendBroadcast(new Intent().setAction("notiBadge"));
                this.sendBroadcast(new Intent().setAction("bcNewMessage"));

            } else {
                SendNoti(message, pid, redirect_type, subject, from_id);
            }
        } else {
            if (redirect_type.equals("chat_insert") && from_id.equals(OpenedChatId)) {
                this.sendBroadcast(new Intent().setAction("notiBadge"));
                this.sendBroadcast(new Intent().setAction("bcNewMessage"));

            } else {
                SendNoti(message, pid, redirect_type, subject, from_id);
            }
        }

    }

    public void SendNoti(String message, String pid, String redirect_type, String subject, String from_id) {

        final int random = new Random().nextInt(1000);


        NotificationManager notifManager = null;
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder;
            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("pid", pid);
            intent.putExtra("redirect_type", redirect_type);
            intent.putExtra("from_id", from_id);
            intent.putExtra("message", message);
            /*intent.putExtra("title", title);
            intent.putExtra("redirecttype", redirecttype);
            intent.putExtra("usertype", usertype);
            intent.putExtra("topic_or_course_id", topic_or_course_id);*/
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;

            String id = "id_TradeGenie";
            // The user-visible name of the channel.
            CharSequence name = "TradeGenie";
            // The user-visible description of the channel.
            String description = "Notifications regarding TradeGenie";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            notifManager.createNotificationChannel(mChannel);
         /*   int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription(message);
                mChannel.enableVibration(true);
              //  notifManager.createNotificationChannel(mChannel);
            }*/
            builder = new NotificationCompat.Builder(this, id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            pendingIntent = PendingIntent.getActivity(this, random, intent, PendingIntent.FLAG_ONE_SHOT);


            builder.setContentTitle("TradeGenie")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorWhite))
                    .setSound(defaultSoundUri)
                    //.setSmallIcon(getNotificationIcon())
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("TradeGenie" + " " + subject).bigText(message));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.ic_notification_logo);
                builder.setColor(getResources().getColor(R.color.colorOTPText));
            } else {
                builder.setSmallIcon(R.drawable.ic_notification_logo);
            }
            Notification notification = builder.build();
            notifManager.notify(random, notification);

            this.sendBroadcast(new Intent().setAction("notiBadge"));
            this.sendBroadcast(new Intent().setAction("bcNewMessage"));

        } else {

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("pid", pid);
            intent.putExtra("redirect_type", redirect_type);
            intent.putExtra("message", message);
            /*intent.putExtra("title", title);
            intent.putExtra("redirecttype", redirecttype);
            intent.putExtra("usertype", usertype);
            intent.putExtra("topic_or_course_id", topic_or_course_id);*/
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = null;

            pendingIntent = PendingIntent.getActivity(this, random, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

            Log.e("Notification", "subject" + subject);


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("TradeGenie")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                    .setSound(defaultSoundUri)
                    .setSmallIcon(getNotificationIcon())
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("TradeGenie" + " " + subject).bigText(message));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.drawable.ic_notification_logo);
                notificationBuilder.setColor(getResources().getColor(R.color.colorOTPText));
            } else {
                notificationBuilder.setSmallIcon(R.drawable.ic_notification_logo);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(random, notificationBuilder.build());
            this.sendBroadcast(new Intent().setAction("notiBadge"));
            this.sendBroadcast(new Intent().setAction("bcNewMessage"));
        }


    }

    private int getNotificationIcon() {
        boolean whiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.ic_notification_logo : R.drawable.ic_notification_logo;


    }
}