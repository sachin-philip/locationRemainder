package com.ajce.reminder;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LocationBasedReminderActivity extends Activity implements Runnable {

	TextView tv_mylatitude, tv_mylongitude, tv_myaddress, tv_address;
	Spinner spn_provider;
	EditText et_address;
	Button btn_startProvider, btn_reverseGeocode, btn_addReminderHere, btn_forwardGeocode, btn_addReminderAt, btn_reviewReminders;
	Button btn_refresh;
	Location lastknownloc;
	String myAddress;	
	ProgressDialog pd;
	Geocoder gc;
	int viewId;	
	String errorMsgRevGeocode;
	boolean revGeocodeSuccess;
	boolean revGeocodeNullvalue;	
	String errorMsgForwardGeocode;
	String enteredAddress;
	String geocodedAddress;
	boolean forwardGeocodeSuccess;
	boolean forwardGeocodeNullvalue;	
	Double mylatitude, mylongitude;
	Double geocodedlatitude, geocodedlongitude;
	ListView list;	
	App app;

	@Override public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		app = (App)getApplicationContext();
		setContentView(R.layout.main);		
		tv_mylatitude = (TextView) findViewById(R.id.tv_mylatitude);
		tv_mylongitude = (TextView) findViewById(R.id.tv_mylongitude);
		tv_myaddress = (TextView) findViewById(R.id.tv_myaddress);
		tv_address = (TextView) findViewById(R.id.tv_address);
		spn_provider = (Spinner) findViewById(R.id.spn_provider);
		et_address = (EditText) findViewById(R.id.et_address);		
		btn_refresh = (Button)findViewById(R.id.btn_refresh);
		btn_startProvider = (Button) findViewById(R.id.btn_startProvider);
		btn_reverseGeocode = (Button) findViewById(R.id.btn_reverseGeocode);
		btn_addReminderHere = (Button) findViewById(R.id.btn_addReminderHere);
		btn_forwardGeocode = (Button) findViewById(R.id.btn_forwardGeocode);
		btn_addReminderAt = (Button) findViewById(R.id.btn_addReminderAt);
		btn_reviewReminders = (Button) findViewById(R.id.btn_reviewReminders);
		btn_startProvider.setEnabled(true);
		btn_refresh.setEnabled(false);
		btn_reverseGeocode.setEnabled(false);
		btn_addReminderHere.setEnabled(false);
		btn_forwardGeocode.setEnabled(true);
		btn_addReminderAt.setEnabled(false);
		btn_reviewReminders.setEnabled(true);
		final String[] items = new String[] { "gps provider", "network provider" };
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_provider.setAdapter(ad);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		DbHelper dbHelper = new DbHelper(this);
		dbHelper.deleteAll();
	}

	public void onclick_btn_startProvider(View v) {		
		if (spn_provider.getSelectedItemPosition() == 0) {			
			app.getInstance().startMonitoring(LocationManager.GPS_PROVIDER);
		} else if (spn_provider.getSelectedItemPosition() == 1) {
			app.getInstance().startMonitoring(LocationManager.NETWORK_PROVIDER);
		}
		btn_refresh.setEnabled(true);
	}
	
	public void onclick_btn_stopProvider(View v) {		
		if (spn_provider.getSelectedItemPosition() == 0) {			
			app.getInstance().stopMonitoring(LocationManager.GPS_PROVIDER);
		} else if (spn_provider.getSelectedItemPosition() == 1) {
			app.getInstance().stopMonitoring(LocationManager.NETWORK_PROVIDER);
		}
		btn_refresh.setEnabled(false);
	}
	
	public void onclick_btn_refresh(View v){
		lastknownloc = app.getInstance().location;
		if (lastknownloc != null) {
			tv_mylatitude.setText(String.valueOf(lastknownloc.getLatitude()));
			tv_mylongitude.setText(String.valueOf(lastknownloc.getLongitude()));
			btn_reverseGeocode.setEnabled(true);
		} else {
			tv_mylatitude.setText("null");
			tv_mylongitude.setText("null");
			btn_reverseGeocode.setEnabled(false);
		}
	}

	public void onclick_btn_reverseGeocode(View v) {		
		pd = ProgressDialog.show(this, "Please Wait..", "Reverse Geocoding...", true, true);
		viewId = v.getId();		
		Thread thread = new Thread(this);		
		thread.start();
	}
	
	public void onclick_btn_forwardGeocode(View v) {		
		pd = ProgressDialog.show(this, "Please Wait..", "Geocoding...", true, true);
		viewId = v.getId();		
		Thread thread = new Thread(this);		
		thread.start();
	}
	
	public void run() {		
		
		if(viewId==R.id.btn_reverseGeocode) {	
			
			gc = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(lastknownloc.getLatitude(), lastknownloc.getLongitude(), 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);
					mylatitude = address.getLatitude();			//	setting intentextra1
					mylongitude = address.getLongitude();		//	setting intentextra2 
					for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
						sb.append(address.getAddressLine(i)).append("\n");
					}
					if(address.getLocality()!=null)
						sb.append(address.getLocality()).append("\n");
					if(address.getPostalCode()!=null)
						sb.append(address.getPostalCode()).append("\n");
					if(address.getCountryName()!=null)
						sb.append(address.getCountryName());
					myAddress = sb.toString();					//	setting intentextra3	
					revGeocodeSuccess = true;
					revGeocodeNullvalue = false;
				} 
				else {
					revGeocodeSuccess = true;
					revGeocodeNullvalue = true;				
				}
			} 
			catch (Exception e) {
				errorMsgRevGeocode = e.toString();
				revGeocodeSuccess = false;
			}		
			handler.sendEmptyMessage(0);
		}		
		
		else if (viewId==R.id.btn_forwardGeocode) {
			
			enteredAddress = et_address.getText().toString();
			gc = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocationName(enteredAddress, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);
					geocodedlatitude = address.getLatitude();		//	setting intentextra1
					geocodedlongitude = address.getLongitude();		//	setting intentextra2 
					for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
						sb.append(address.getAddressLine(i)).append("\n");
					}
					if(address.getLocality()!=null)
						sb.append(address.getLocality()).append("\n");
					if(address.getPostalCode()!=null)
						sb.append(address.getPostalCode()).append("\n");
					if(address.getCountryName()!=null)
						sb.append(address.getCountryName());
					geocodedAddress = sb.toString();				//	setting intentextra3
					forwardGeocodeSuccess = true;
					forwardGeocodeNullvalue = false;
				} 
				else {
					forwardGeocodeSuccess = true;
					forwardGeocodeNullvalue = true;				
				}
			} 
			catch (Exception e) {
				errorMsgForwardGeocode = e.toString();
				forwardGeocodeSuccess = false;	
			}		
			handler.sendEmptyMessage(0);
		}
	}

	private Handler handler = new Handler() {
		@Override public void handleMessage(Message msg) {
			pd.dismiss();			
			
			if(viewId==R.id.btn_reverseGeocode) {
				if(revGeocodeSuccess) {
					if(revGeocodeNullvalue) {
						tv_myaddress.setText("Where are you?!\nNo location found!");
						btn_addReminderHere.setEnabled(false);
					}
					else {
						tv_myaddress.setText(myAddress);
						btn_addReminderHere.setEnabled(true);
					}
				}
				else {				
					tv_myaddress.setText("Sorry! Some error occured!\nCheck your data connection and try again!");
					showToast(errorMsgRevGeocode);
					btn_addReminderHere.setEnabled(false);
				}
			}
			else if(viewId==R.id.btn_forwardGeocode) {
				if(forwardGeocodeSuccess) {
					if(forwardGeocodeNullvalue) {
						tv_address.setText("Sorry! No such places recorded yet!");
						btn_addReminderAt.setEnabled(false);
					}
					else {
						tv_address.setText(geocodedAddress);
						btn_addReminderAt.setEnabled(true);
					}
				}
				else {				
					tv_address.setText("Error! Probably network fault. Check your data connection and try again!");
					showToast(errorMsgForwardGeocode);
					btn_addReminderAt.setEnabled(false);
				}
			}
		}
	};

	public void onclick_btn_addReminderHere(View v) {
		Intent intent_addReminderHere = new Intent(this, AddReminder.class);		
		intent_addReminderHere.putExtra("latitude", mylatitude);
		intent_addReminderHere.putExtra("longitude", mylongitude);
		intent_addReminderHere.putExtra("address", myAddress);
		startActivity(intent_addReminderHere);
	}
	
	public void onclick_btn_addReminderAt(View v) {
		Intent intent_addReminderAt = new Intent(this, AddReminder.class);		
		intent_addReminderAt.putExtra("latitude", geocodedlatitude );
		intent_addReminderAt.putExtra("longitude", geocodedlongitude);
		intent_addReminderAt.putExtra("address", geocodedAddress);
		startActivity(intent_addReminderAt);
	}

	public void onclick_btn_reviewReminders(View v) {
		startActivity(new Intent(this, ReminderList.class));		
	}

	public void showToast(String msg) {
		Toast.makeText(LocationBasedReminderActivity.this, msg, 1500).show();
	}

	
}