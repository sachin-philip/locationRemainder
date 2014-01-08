package com.ajce.reminder;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Review extends Activity {
	
	EditText et_n, et_a, et_r, et_e;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		setContentView(R.layout.review);
		et_n=(EditText)findViewById(R.id.et_n);
		et_a=(EditText)findViewById(R.id.et_a);
		et_r=(EditText)findViewById(R.id.et_r);
		et_e=(EditText)findViewById(R.id.et_e);
		
		String ourRemName = this.getIntent().getExtras().getString("remindernote");		
		DbHelper dbHelper =  new DbHelper(this);
		ContentValues cv = dbHelper.loadARow(ourRemName);	
		if(cv!=null) {
			et_n.setText(cv.getAsString("note"));
			et_a.setText(cv.getAsString("address"));
			et_r.setText(cv.getAsString("radius"));
			et_e.setText(cv.getAsString("expiry"));
		}
		else {
			showToast("No?!!!!!!!!!");
		}
	}
	
	public void onclick_btn_ok(View v) {
		finish();
	}
	
	

	public void showToast(String msg) {
		Toast.makeText(Review.this, msg, 1500).show();
	}
}
