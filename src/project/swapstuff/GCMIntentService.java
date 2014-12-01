package project.swapstuff;
//package project.swapstuff.model;


//549193984962-gn1blka0s4smd94f9h3enguee828qa3k.apps.googleusercontent.com
//AIzaSyCANTTZODAfw-R0fzKMDV7HlFoDUVom_sI


import project.swapstuff.model.CommonUtilities;
import project.swapstuff.model.ServerUtilities;
import project.swapstuff.model.Utills;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;


public class GCMIntentService extends GCMBaseIntentService {

	
	private static final String TAG = "GCMIntentService";
	String status;
	Bundle bun;

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
			Log.i(TAG, "Ignoring unregister callback");
		}

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		try {

			bun = intent.getExtras();
			String message = intent.getStringExtra("message");
			Log.e("+++++++++++++++GCM message++++++++++++++++++++++", message);
			
//		if(Utills.refreshMatch){
//			CommonUtilities.FragmentToOpen=1;
//			Intent notificationIntent = new Intent(context, MainActivitySwapStuff.class);
//			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//					| Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(notificationIntent);
//		}
		
			if(!Utills.chatActive)
			{
				CommonUtilities.FragmentToOpen=1;
				generateNotification(context, message);
			}
		

		} catch (Exception e) {
			Log.e(TAG, "Inside Exception onMessage -> " + e.toString());
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.swap_logo;

		int noti_no=0;
		if(message.contains("You"))
		{
			noti_no=1;
		}
		
		
		
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String title ="Swapstff"+"";
		Intent notificationIntent = new Intent(context, MainActivitySwapStuff.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
//		PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		 
		 if(defaultSound == null){
			 defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
             if(defaultSound == null){
            	 defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
             }
         }
		 

		Notification not = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(message)
				.setContentIntent(intent).setSmallIcon(icon)
				.setLights(Color.WHITE, 1, 2).setAutoCancel(true).setSound(defaultSound)
				.build();
		
		not.defaults |= Notification.DEFAULT_VIBRATE;
		not.defaults |= Notification.DEFAULT_SOUND;
		notificationManager.notify(noti_no, not);
		


	}
}
