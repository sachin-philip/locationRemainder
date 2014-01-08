package com.ajce.reminder;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Alert extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Toast.makeText(this, "U reached!!", 1500).show();
	}

}
