package com.shifa.kent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.util.Timer;
import java.util.TimerTask;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCM Tutorial::Service";

    // Use your PROJECT ID from Google API into SENDER_ID
    public static final String SENDER_ID = "753494376728";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {

        Log.i(TAG, "onRegistered: registrationId=" + registrationId);
        Super_Library_AppClass SLAc = new Super_Library_AppClass(context);
        SLAc.SavePreference("gcmreg",registrationId);


    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {

        Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
    }
    protected String notificationResponse = "";

    protected void NotficationDataCallBack(String result,Context context) {

        try {
            Log.e(TAG, "Broadcase msg received " + result);



            if (!notificationResponse.equalsIgnoreCase(result)) {
                notificationResponse = result;
                Intent intent = new Intent(this, home_menu.class);
                // Pass data to the new activity
                intent.putExtra("message", result);
                // Starts the activity on notification click
                PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                // Create the notification with a notification builder
                Notification notification = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Shifa Repertory Homoeopathy")
                        .setContentText(result).setContentIntent(pIntent)
                        .getNotification();
                // Remove the notification on click
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(R.string.app_name, notification);

                {
                    // Wake Android Device when notification received
                    PowerManager pm = (PowerManager) context
                            .getSystemService(Context.POWER_SERVICE);
                    final PowerManager.WakeLock mWakelock = pm.newWakeLock(
                            PowerManager.FULL_WAKE_LOCK
                                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "GCM_PUSH");
                    mWakelock.acquire();

                    // Timer before putting Android Device to sleep mode.
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            mWakelock.release();
                        }
                    };
                    timer.schedule(task, 5000);
                }
            }

        } catch (Exception ex) {
            Log.e(TAG, "PostExecute ", ex);
        }
    }
    @Override
    protected void onMessage(Context context, Intent data) {
        String message;
        // Message from PHP server
        message = data.getStringExtra("message");
        // Open a new activity called GCMMessageView
        NotficationDataCallBack(message, context);




    }

    @Override
    protected void onError(Context arg0, String errorId) {

        Log.e(TAG, "onError: errorId=" + errorId);
    }

}