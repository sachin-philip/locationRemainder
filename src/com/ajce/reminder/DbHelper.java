package com.ajce.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper {
	
	SQLiteDatabase _database;
	Cursor _c;
	
	public DbHelper(Context context) {
		super(context, "reminderdatabase.db", null, 1);
	}
	
	@Override public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table reminders("+
				" id integer primary key autoincrement" +
				", note string unique" +
				", radius string" +
				", expiry string" +
				", lat string" +
				", lon string" +
				", address string)");
		db.execSQL("create table checklist(" + BaseColumns._ID + " integer primary key autoincrement, task string unique)");
	}
	
	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public boolean insertOnerow(String note, String radius, String expiry, String lat, String lon, String address) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("note", note);
		cv.put("radius", radius);
		cv.put("expiry", expiry);
		cv.put("lat", lat);
		cv.put("lon", lon);
		cv.put("address", address);			
		if (database.insert("reminders", null, cv) > 0) {
			database.close();
			return true;
		} else {
			database.close();
			return false;
		}
	}
	
	public int deleterow(String note) {
		SQLiteDatabase database = this.getWritableDatabase();
		int del = database.delete("reminders", "note='" + note + "'", null);
		database.close();
		return del;
	}
	
	public int updaterow(String note,String radius, String expiry){
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("radius", radius);
		cv.put("expiry", expiry);
		int up = database.update("reminders", cv, "note='" + note, null);
        database.close();
		return up;
	}	
	
	public ContentValues loadARow(String notename) {
		_database = this.getReadableDatabase();
		_c = _database.query("reminders", null, "note='"+notename+"'", null, null, null, null);
		if(_c.getCount()>0){
			_c.moveToFirst();
			ContentValues cv = new ContentValues();
			cv.put("id", _c.getInt(0));
			cv.put("note", _c.getString(1));
			cv.put("radius", _c.getString(2));
			cv.put("expiry", _c.getString(3));
			cv.put("lat", _c.getString(4));
			cv.put("lon", _c.getString(5));
			cv.put("address", _c.getString(6));
			_c.close();
			_database.close();
			return cv;
		}
		else {
			_c.close();
			_database.close();
			return null;
		}
	}
	
	public Cursor loadtable(){
		_database = this.getReadableDatabase();
		_c = _database.query("reminders", null, null,null,null,null,null);		
		return _c;
	}
	
	public boolean deleteAll(){
		_database = this.getWritableDatabase();
		if(_database.delete("reminders", null, null)>0){
			_database.close();
			return true;
		}
		else {
			_database.close();
			return false;
		}
		
		
	}
			
	public void close(){
		_c.close();
		_database.close();
	}
	
	public boolean addTask(String task) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("task", task);			
		if (database.insert("checklist", null, cv) > 0) {
			database.close();
			return true;
		} else {
			database.close();
			return false;
		}
	}
	
	public void deleteTask(String task ) {
		
	}
	
	
}
