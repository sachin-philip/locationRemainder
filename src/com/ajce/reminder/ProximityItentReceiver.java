package com.ajce.reminder;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;

public class ProximityItentReceiver extends BroadcastReceiver {
	
	private static final int NOTIFICATION_ID = 1000;
	Boolean entering;

	@Override public void onReceive(Context context, Intent intent) {
		
//		int id = intent.getIntExtra("id", -1);
//		int reqcode = intent.getIntExtra("reqCode", -1);		
		String note = intent.getStringExtra("note");
		
		String enterOrExit = LocationManager.KEY_PROXIMITY_ENTERING;
		entering = intent.getBooleanExtra(enterOrExit, false);
		if (entering) {
			Log.d(getClass().getSimpleName(), "entering");
		} else {
			Log.d(getClass().getSimpleName(), "exiting");
		}

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent("PROX_ALERT"), 0);
		Notification notification = new Notification();

		notification.icon = R.drawable.rem;
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//notification.flags = Notification.FLAG_AUTO_CANCEL | notification.flags; 
		
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

//		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;

		notification.ledARGB = Color.WHITE;
		notification.ledOnMS = 1500;
		notification.ledOffMS = 1500;
//		Intent  intent2 = new Intent("PROX_ALERT");
//		context.startActivity(intent2);
		
		if (entering) {
			Log.d(getClass().getSimpleName(), "entering");
			notification.setLatestEventInfo(context, "Proximity Alert!", "Ur task is "+note, pendingIntent);

			notificationManager.notify(NOTIFICATION_ID, notification);
			
		} else {
//			Log.d(getClass().getSimpleName(), "exiting");
//			notification.setLatestEventInfo(context, "Proximity Alert!", "You are near ur target! "+note, pendingIntent);
//
//			notificationManager.notify(NOTIFICATION_ID, notification);
//			
//			Toast.makeText(context, "You are leaving ur target :: Ur task is "+note, Toast.LENGTH_LONG);
//			Toast.makeText(context, ":: Ur task is "+note, Toast.LENGTH_LONG);
		}
		

	}
	

}