package com.example.devendranagar.halonotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

class DBHelper extends SQLiteOpenHelper {

	private static final int version = 1;
	private static final String DB_NAME = "notesDB";
	private static final String TABLE_NAME = "notes";
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "noteTitle";
	private static final String KEY_CONTENT = "noteContent";
	private static final String KEY_DATE = "date";
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_TITLE+" TEXT NOT NULL, "+KEY_CONTENT+" TEXT NOT NULL, "+KEY_DATE+" TEXT);";
	DBHelper(Context context) {
		super(context, DB_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
		onCreate(db);
	}

	void addNote(String title, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("noteTitle", title);
		cv.put("noteContent", content);
		cv.put("date", new Date().toString());
		
		db.insert(TABLE_NAME, null, cv);
		db.close();
	}

	Cursor getNotes2(SQLiteDatabase db) {
		Cursor c = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_TITLE}, null, null, null, null, "id DESC");
		c.moveToFirst();
		return c;
	}
	
	Cursor getNote(SQLiteDatabase db, int id) {
		Cursor c = db.query(TABLE_NAME, new String[] {KEY_TITLE, KEY_CONTENT, KEY_DATE}, KEY_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null);
		c.moveToFirst();
		return c;
	}
	
	void removeNote(int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
		db.close();
	}
	
	void updateNote(String title, String content, String editTitle) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("noteTitle", title);
		cv.put("noteContent", content);
		cv.put("date", new Date().toString());
		db.update(TABLE_NAME, cv, KEY_TITLE + " LIKE '" +  editTitle +  "'", null);
		db.close();
	}
}
