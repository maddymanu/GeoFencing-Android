package com.example.mapdemo;

import java.util.List;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

public class TransitionsIntentService extends IntentService {
	public static final String TRANSITION_INTENT_SERVICE = "ReceiveTransitionsIntentService";
	private List<Geofence> mGeofenceList;

	public TransitionsIntentService() {
		
		super(TRANSITION_INTENT_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
				
		// First check for errors
		if (LocationClient.hasError(intent)) {
			// Get the error code with a static method
			int errorCode = LocationClient.getErrorCode(intent);
			// Log the error
			Log.e("ReceiveTransitionsIntentService",
					"Location Services error: " + Integer.toString(errorCode));
			
			Log.i("LOC NOT", "Got an error");
			/*
			 * You can also send the error code to an Activity or Fragment with
			 * a broadcast Intent
			 */
			/*
			 * If there's no error, get the transition type and the IDs of the
			 * geofence or geofences that triggered the transition
			 */
		} else {
			// Get the type of transition (entry or exit)
		
			
			int transitionType = LocationClient.getGeofenceTransition(intent);
			
			
			// Test that a valid transition was reported
			if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER)
					|| (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
				
				List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
				Geofence current = triggerList.get(0);
				String[] triggerIds = new String[triggerList.size()];

				for (int i = 0; i < triggerIds.length; i++) {
					// Store the Id of each geofence
					triggerIds[i] = triggerList.get(i).getRequestId();
				}
				
				generateNotification(transitionType, current);
				//notification();
				newNotification(current);
				
				
				
				
				
				/*
				 * At this point, you can store the IDs for further use display
				 * them, or display the details associated with them.
				 */
			}
			// An invalid transition was reported
			else {
				
				Log.i("LOC NOT", "not a valid transition");
				
				Log.e("ReceiveTransitionsIntentService",
						"Geofence transition error: ");

			}
		}
	}
	
	public void newNotification(Geofence current){
		Intent inte = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, inte, 0);
		
		@SuppressWarnings("deprecation")
		NotificationCompat.Builder mBuilder =
		    new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
		    .setContentTitle(current.getRequestId())
		    .setContentText("Whyd u Enter Brah!");
		
		mBuilder.setContentIntent(pIntent);
		
		//not.flags = Notification.FLAG_AUTO_CANCEL;
		
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(0 , mBuilder.build());
	}
	
	private void generateNotification(int type , Geofence current) {
		//instead take a list as input and go through each element
		
		Log.i("LOC TRAN " , " " + type + " " + current.getRequestId());
    }

	private List<Geofence> getTriggeringGeofences(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("LOC NOT", "entering triggerin");
		notification();
		return null;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void notification() {
		Log.i("LOC NOT", "getting a not");
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setContentTitle("Flood Warning")
				.setContentText("You are in Danger Zone");
		

		Intent i = new Intent(this, MainActivity.class);

		/*
		 * The stack builder object will contain an artificial back stack for
		 * the started Activity. This ensures that navigating backward from the
		 * Activity leads out of your application to the Home screen.
		 */
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(i);
		PendingIntent pi = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pi);
		// Sets an ID for the notification
		int nId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(nId, mBuilder.build());

	}
}
