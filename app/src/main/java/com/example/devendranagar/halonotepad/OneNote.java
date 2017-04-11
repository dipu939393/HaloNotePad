package com.example.devendranagar.halonotepad;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class OneNote extends Activity {
	private static final String TAG = "notepad";
	private DBHelper dbhelper;
	private String title = "defaultTitle";
	private int id = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onenote);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dbhelper = new DBHelper(getApplicationContext());
        TextView noteTitle = (TextView) findViewById(R.id.noteTitle);
        TextView noteContent = (TextView) findViewById(R.id.noteContent);
        TextView createdAt = (TextView) findViewById(R.id.createdAt);
        ImageView deleteNotesIV=(ImageView)findViewById(R.id.deleteNotesIV);
        ImageView editNotesIV=(ImageView)findViewById(R.id.editNotesIV);
        ImageView backToolBar=(ImageView)findViewById(R.id.backToolBar);
		Intent mIntent = getIntent();
        id = mIntent.getIntExtra("id", 0);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor c = dbhelper.getNote(db, id);
		db.close();
		title = c.getString(0);
        String content = c.getString(1);
        String date = c.getString(2);
		
		noteTitle.setText(title);
		noteContent.setText(content);
		createdAt.setText(date);

		deleteNotesIV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dbhelper.removeNote(id);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
			}
		});

        editNotesIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OneNote.this, CreateNote.class);
                i.putExtra("id", id);
                Log.d(TAG, title);
                i.putExtra("isEdit", true);
                startActivity(i);
            }
        });

        backToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
	}
}
