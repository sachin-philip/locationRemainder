package com.ajce.reminder;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class App extends Application {

	private static App singleton;
	
	public String user;
	public Double latitude;
	public Double longitude;
	public Location location; 
	public String provider = LocationManager.GPS_PROVIDER;
	public LocationListener listener;
	
	LocationManager locationManager;
	PendingIntent pendingIntent_prox=null;
	
	public App getInstance(){
		return singleton;
	}
	
	@Override public void onCreate() {
		super.onCreate();
		singleton = this;
	}	

	public String getUser(){
		return user;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public Double getLatitude(){
		return latitude;
	}
	
	public Double getLongitude(){
		return longitude;
	}
	
	public void setLocation(Location loc) {
		location = loc;
	}
	
	public void setLatitude(Double lat){
		latitude = lat;
	}
	
	public void setLongitude(Double lng) {
		longitude = lng;
	}
	
	public void setProvider(String pvdr) {
		provider = pvdr;
	}
	
	public String getProvider(){
		return provider;
	}
	
	
	
	public void startMonitoring(String pvdr){
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		provider = pvdr;
		location = locationManager.getLastKnownLocation(provider);
		listener = new LocationListener() {
			@Override public void onLocationChanged(Location lc) {
				Log.i("LOCATION CHANGED", "Lat:"+lc.getLatitude()+"Lng:"+lc.getLongitude());
				latitude = lc.getLatitude();
				longitude = lc.getLongitude();
				location = lc;
			}
			@Override public void onStatusChanged(String provider, int status, Bundle extras) {
				showToast(provider+" status changed");
			}
			@Override public void onProviderEnabled(String provider) {
				showToast(provider+" enabled");
			}
			@Override public void onProviderDisabled(String provider) {
				showToast(provider+" disabled");
			}			
		};
		locationManager.requestLocationUpdates(provider, 2000, 10, listener);
	}
	
	public void stopMonitoring(String pvdr) {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		provider = pvdr;
		locationManager.removeUpdates(listener);
	}
	
	public void addAlert(String note,int id, Float radius, Long expiry, Double latude, Double lotude) {
		
		String string_action_prox = "PROXIMITY_ALERT"+id;
		Intent intent_prox = new Intent(string_action_prox);
		intent_prox.putExtra("id", id);
		int request_code = id;
		intent_prox.putExtra("reqCode", request_code);
		intent_prox.putExtra("note", note);
		
		
		pendingIntent_prox = PendingIntent.getBroadcast(
				getApplicationContext(),
				request_code,
				intent_prox,
				PendingIntent.FLAG_CANCEL_CURRENT);		
		
		locationManager.addProximityAlert(latude, lotude, radius, expiry, pendingIntent_prox);
		
		IntentFilter filter = new IntentFilter(string_action_prox);
		registerReceiver( new ProximityItentReceiver(), filter );

	}
	
	public void removeAllAlerts() {	
		if(pendingIntent_prox!=null){
			locationManager.removeProximityAlert(pendingIntent_prox);
		}
		
	}
	
	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	

}
