package com.ajce.reminder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddReminder extends Activity {
	
	EditText et_reminderNote, et_reminderRadius, et_reminderExpiry;
	TextView tv_reminderLatitude, tv_reminderLongitude, tv_reminderAddress;
	Button btn_AddReminder;
	
	String address;
	Double latitude, longitude;
	float radius;
	long expiry;
	PendingIntent pendingIntent;
	
	App app;
	
	int id;
	ProximityItentReceiver receiver;
	

	
	@Override protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		app = (App)getApplicationContext();
		setContentView(R.layout.add_reminder);
		
		Intent intentReceived = this.getIntent();
		Bundle bundleReceived = intentReceived.getExtras();
		
		et_reminderNote = (EditText)findViewById(R.id.et_reminderNote);
		et_reminderRadius = (EditText)findViewById(R.id.et_reminderRadius);
		et_reminderExpiry = (EditText)findViewById(R.id.et_reminderExpiry);
		tv_reminderLatitude = (TextView)findViewById(R.id.tv_reminderLatitude);
		tv_reminderLongitude = (TextView)findViewById(R.id.tv_reminderLongitude);
		tv_reminderAddress = (TextView)findViewById(R.id.tv_reminderAddress);
		
		latitude = bundleReceived.getDouble("latitude");
		longitude = bundleReceived.getDouble("longitude");
		address = bundleReceived.getString("address");
		
		tv_reminderLatitude.setText(String.valueOf(latitude));
		tv_reminderLongitude.setText(String.valueOf(longitude));
		tv_reminderAddress.setText(address);
		
		
	}

	public void onclick_btn_AddReminder(View v) {
		DbHelper dbHelper = new DbHelper(this);
		String n = et_reminderNote.getText().toString();		
		String r = et_reminderRadius.getText().toString();
		radius = Float.parseFloat(r);
		String e = et_reminderExpiry.getText().toString();
		expiry = Long.parseLong(e);
		String la = String.valueOf(latitude);
		String lo = String.valueOf(longitude);
		String a = address;
		if((n.matches(""))||(r.matches(""))||(e.matches(""))) {
			showToast("Please provide all details!");			
		}
		else {
			if(dbHelper.insertOnerow(n,r,e,la,lo,a)) {
				showToast("inserted");
				DbHelper dbHelper2 = new DbHelper(this);
				ContentValues cv = dbHelper2.loadARow(n);
				id = cv.getAsInteger("id");
				
				app.getInstance().startMonitoring(app.getInstance().provider);
				app.getInstance().addAlert(n,id,radius,expiry,latitude,longitude);
				
				
				finish();
			}
			else 
				showToast("not inserted");			
		}
	}
	
	
	
	public void showToast(String msg) {
		Toast.makeText(AddReminder.this, msg, 1500).show();
	}
	
	
}
