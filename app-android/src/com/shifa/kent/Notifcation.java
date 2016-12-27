package com.shifa.kent;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Notifcation extends Activity {
	
	@SuppressLint("NewApi")
	public void ShowNotification()
	{
		Intent intent = new Intent(this, activity_kent.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// Build notification
		// Actions are just fake
		Notification noti = new Notification.Builder(this)
		        .setContentTitle("New mail from " + "test@gmail.com")
		        .setContentText("Subject")
		        .setSmallIcon(R.drawable.ic_report)
		        .setContentIntent(pIntent)
		        .addAction(R.drawable.ic_launcher, "Call", pIntent)
		        .addAction(R.drawable.ic_cateogry, "More", pIntent)
		        .addAction(R.drawable.ic_cateogryminus, "And more", pIntent).build();
		    
		  
		NotificationManager notificationManager = 
		  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti); 
		
		
	}
	
}
