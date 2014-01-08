package com.ajce.reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class First extends Activity {
	
	
	App app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (App)getApplicationContext();		
		setContentView(R.layout.first);
	}
	
	public void onclick_reminders(View v) {
		Intent intent = new Intent(this, LocationBasedReminderActivity.class);
		startActivity(intent);
	}
	
	public void onclick_flashlight(View v) {
		Intent intent = new Intent(this, Flash.class);
		startActivity(intent);
	}
	
	public void onclick_checklist(View v) {
		Intent intent = new Intent(this, Checklist.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed () {
	    moveTaskToBack (true);
	}

}
