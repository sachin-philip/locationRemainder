package com.ajce.reminder;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Checklist extends ListActivity {
	
	CursorAdapter cursorAdapter;
	SQLiteDatabase database;
	Cursor cursor_data;
	View view_dialog;
	EditText et_task;
	
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
				
		view_dialog = getLayoutInflater().inflate(R.layout.layout_header, null);
		getListView().addHeaderView(view_dialog);
		getListView().setHeaderDividersEnabled(false);
		et_task = (EditText) view_dialog.findViewById(R.id.et_task);
		DbHelper helper = new DbHelper(this);
		database = helper.getWritableDatabase();
		cursor_data = database.query("checklist", new String[]{"_id","task"}, null, null, null, null, null);	
		cursorAdapter = new SimpleCursorAdapter(this, R.layout.layout_list, cursor_data, new String[]{"task"}, new int[] { R.id.listviewColumn1});
		setListAdapter(cursorAdapter);
		registerForContextMenu(getListView());	
		
	}
	
	public void onclick_btn_addTask(View v) {		
		ContentValues cv_row = new ContentValues();
		cv_row.put("task", et_task.getText().toString());
		try {
			database.insertOrThrow("checklist", null, cv_row);
			cursorAdapter.getCursor().requery();
			et_task.setText("");
		}
		catch (Exception e) {
			if(e.toString().contains("error code 19")) 
				showToast("Cant add that task again! It already exists. Please do check.");
			e.printStackTrace();
		}
	}
	
	@Override public void onDestroy() {
		super.onDestroy();
		cursor_data.close();
		database.close();
	}

	@Override public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Task over?");
		menu.setHeaderIcon(R.drawable.delete);
		menu.add(0, 1, 0, "Yes. Remove it.");
	}

	@Override public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		deleteTask(info.id); 
		return true;
	}
	
	private void deleteTask(final long rowId) {
		if (rowId > 0) {			
			try {
				database.delete("checklist", "_id="+rowId, null);
				cursorAdapter.getCursor().requery();					
			}
			catch (Exception e) {	
				e.printStackTrace();
			}		
		}
	}	
	
	public void showToast(String msg) {
		Toast.makeText(this, msg, 1500).show();
	}

	
}