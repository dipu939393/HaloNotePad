package com.example.devendranagar.halonotepad;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateNote extends Activity {
	private static final String TAG = "notepad";
	private EditText titleEditText;
	private EditText contentEditText;

	private boolean isEdit;
	private DBHelper dbhelper;
	private String editTitle;
    int id;
    Button addNoteToDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createlayout);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        addNoteToDB = (Button)findViewById(R.id.addNoteToDB);
        titleEditText = (EditText)findViewById(R.id.TitleEditText);
		contentEditText = (EditText) findViewById(R.id.body);
		ImageView saveNotesIV=(ImageView)findViewById(R.id.saveNotesIV);
        ImageView backToolBar=(ImageView)findViewById(R.id.backToolBar);

		dbhelper = new DBHelper(getApplicationContext());

		Intent mIntent = getIntent();

		editTitle = mIntent.getStringExtra("title");

        id = mIntent.getIntExtra("id", 0);
		isEdit = mIntent.getBooleanExtra("isEdit", false);

        if(isEdit) {
			Log.d(TAG, "isEdit");
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor c = dbhelper.getNote(db, id);
			db.close();
			titleEditText.setText(c.getString(0));
			contentEditText.setText(c.getString(1));
			addNoteToDB.setText(getResources().getString(R.string.updateNoteButton));
		}


		addNoteToDB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = titleEditText.getText().toString();
				String content = contentEditText.getText().toString();
				if (title.equals("") || content.equals("")) {
					Toast.makeText(getApplicationContext(),  getResources().getString(R.string.validation), Toast.LENGTH_LONG).show();
					return;
				}

				if (!isEdit) {
					dbhelper = new DBHelper(getApplicationContext());
					dbhelper.addNote(title, content);
					finish();
				} else {
					dbhelper.updateNote(title, content, editTitle);
					finish();
				}
			}
		});

		saveNotesIV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = titleEditText.getText().toString();
				String content = contentEditText.getText().toString();
				if (title.equals("") || content.equals("")) {
					Toast.makeText(getApplicationContext(),  getResources().getString(R.string.validation), Toast.LENGTH_LONG).show();
					return;
				}

				//adding note to db
				if (!isEdit) {
					dbhelper = new DBHelper(getApplicationContext());
					dbhelper.addNote(title, content);
					finish();
				} else {
					dbhelper.updateNote(title, content, editTitle);
					finish();
				}
			}
		});

        backToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);




                startActivity(intent);
            }
        });
	}

	@Override
	protected void onPause() {
		super.onPause();
		dbhelper.close();
	}
}
