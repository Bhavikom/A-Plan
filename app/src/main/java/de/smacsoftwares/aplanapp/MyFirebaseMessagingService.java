package de.smacsoftwares.aplanapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

/**
 * Created by SSoft-13 on 18-02-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    PreferencesHelper preferences;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        //MyApplication.component(this).inject(this);
        preferences = new PreferencesHelper(this);
        if(remoteMessage.getData() != null && remoteMessage.getData().size() > 0){
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
            String doLogout = remoteMessage.getData().get("doLogout");
            String shouldAppDisable = remoteMessage.getData().get("shouldDisableApp");
            String body = remoteMessage.getData().get("body");
            String title = remoteMessage.getData().get("title");
            String actionType = remoteMessage.getData().get("actionType");
            preferences.saveNotificationTitle(title);
            preferences.saveNotificationBody(body);
            preferences.saveNotificationActionType(actionType);
            if(doLogout.equalsIgnoreCase("true")){
                preferences.saveAppLogout(true);
            }
            if(shouldAppDisable.equalsIgnoreCase("true")){
                preferences.saveAppDisalbe(true);
            }else {
                preferences.saveAppDisalbe(false);
                preferences.saveShowActiveDialog("yes");
            }
            SendNotification(title,body);

            /* this condition checks that app in background or not */
            /*if(GlobalClass.isAppIsInBackground(this)){
                if(shouldAppDisable.equalsIgnoreCase("true")){

                    if(preferences.isUserLooged()){
                        *//* will logout and disable application *//*
                        if(doLogout != null && doLogout.equalsIgnoreCase("true") ){
                            preferences.saveAppDisalbe(true);
                            preferences.saveAppLogout(true);
                        }
                        else {
                            *//* will disable application *//*
                            preferences.saveAppDisalbe(true);
                        }
                    }
                }
                else if(doLogout != null && doLogout.equalsIgnoreCase("true")){
                    *//* will logout application *//*
                    if(preferences.isUserLooged()){
                        preferences.saveAppLogout(true);
                    }

                }
                else if(shouldAppDisable.equalsIgnoreCase("false")){
                    *//* will disable application *//*
                    if(preferences.isUserLooged()){
                        if(preferences.isDisableApp()){
                                preferences.saveAppDisalbe(false);
                        }
                    }
                }
                Intent it = new Intent("intent.my.action");
                it.setComponent(new ComponentName(this.getPackageName(), HomeActivity.class.getName()));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.getApplicationContext().startActivity(it);
            }
            *//* in else when app in not background but in foreground when notification arrives*//*
            else {*/
                /* app will disable and logout both */
                if(shouldAppDisable.equalsIgnoreCase("true")){
                    if(doLogout != null && doLogout.equalsIgnoreCase("true")){
                        if(preferences.isUserLooged()){
                            if(HomeActivity.getInstace()!=null){
                                //HomeActivity.getInstace().showAppDisabled();
                                HomeActivity.getInstace().showAppLogout();
                            }
                        }
                    }
                    else {
                        /* app will only disabled */
                        if(preferences.isUserLooged()){
                            if(HomeActivity.getInstace()!=null){
                                HomeActivity.getInstace().showAppDisabled(false);
                            }
                        }
                    }
                }
                /* app will only logout */
                else if(doLogout != null && doLogout.equalsIgnoreCase("true")){
                    if(preferences.isUserLooged()){
                        if(HomeActivity.getInstace()!=null){
                            HomeActivity.getInstace().showAppLogout();
                        }
                    }

                }
                /* app will be in enabled mode from disabled mode */
                else if(shouldAppDisable.equalsIgnoreCase("false")){
                    if(preferences.isUserLooged()){
                        //if(preferences.isDisableApp()){
                            if(HomeActivity.getInstace()!=null){
                                preferences.saveShowActiveDialog("yes");
                                Intent i = new Intent(this,HomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                this.startActivity(i);
                                //startActivity(new Intent(this,HomeActivity.class));
                                preferences.saveAppDisalbe(false);

                            }
                        //}
                    }
                }
            //}

        }
    }
    /*public void SendNotification(String title,String body){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification notification = builder.setContentTitle(title)
                .setContentText(body)
                .setTicker("New Message Alert!")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.app_logo).build();
        //.setContentIntent(pendingIntent).build();
        int num = (int) System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(num, notification);
    }*/
    public void SendNotification(String title,String body){
        NotificationManager notificationManager;
        Notification myNotification;

        Intent myIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

        myNotification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setTicker("New Message Alert!!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.app_logo)
                .build();

        int num = (int) System.currentTimeMillis();
        notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, myNotification);
    }
}
