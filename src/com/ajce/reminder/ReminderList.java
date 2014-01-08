package com.ajce.reminder;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.util.DynamicStringArray;

public class ReminderList extends ListActivity {
	
	String selected_reminder;
	String[] resultarray;
	Double latitude,longitude;
	App app;
	

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (App) getApplicationContext();
		
		registerForContextMenu(this.getListView());
		
		getListView().setCacheColorHint(Color.rgb(36, 33, 32));
		getListView().setBackgroundColor(Color.rgb(36, 33, 32));
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String rem = resultarray[position];
				Intent intent_viewAReminderDetail = new Intent(ReminderList.this, Review.class);
				intent_viewAReminderDetail.putExtra("remindernote", rem);	
				startActivity(intent_viewAReminderDetail);			
			}
		});
		
		loadList();
		
	}
	
	public void loadList() {
		DbHelper dbHelper = new DbHelper(this);
		Cursor c = dbHelper.loadtable();
		c.moveToFirst();
		int count = c.getCount();
		if (count > 0) {
			 DynamicStringArray dynamicStringArray = new DynamicStringArray();
			 while (c.isAfterLast() == false) {
				 dynamicStringArray.addString(c.getString(1));
				 c.moveToNext();
			 }
			 resultarray = dynamicStringArray.getStringArray();
			 this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultarray));

		}
		else{
			this.setListAdapter(null);
		}
		c.close();
		dbHelper.close();
	}
	
	
//	@Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);		
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
//		selected_reminder = resultarray[info.position];
//		menu.setHeaderTitle(selected_reminder);
//		menu.add(0, 1, 0, "Delete");		
//	}
//	
//	@Override public boolean onContextItemSelected(MenuItem item) {
//		if(item.getItemId()==1){
//			DbHelper dbHelper = new DbHelper(this);
//			if (dbHelper.deleterow(selected_reminder)>0){
//				showToast("Deleted "+selected_reminder);
//				loadList();
//			}
//		}		
//		return true;
//	}
	
	
	public void showToast(String msg) {
		Toast.makeText(ReminderList.this, msg, 1500).show();
	}
	
	//FOR OPTIONS MENU	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add(0,1,0,"Clear All");
			menu.add(0,2,0,"Back");
			menu.add(0,3,0,"Refresh");
			return true;
		}	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if(item.getItemId()==1) {
				DbHelper dbHelper = new DbHelper(this);
				dbHelper.deleteAll();
				app.getInstance().removeAllAlerts();
				loadList();
			}
			else if(item.getItemId()==2) {
				finish();
			}		
			else if(item.getItemId()==3){
				loadList();
			}
			return true;
		}
	//FOR OPTIONS MENU



}
