package com.example.devendranagar.halonotepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String TAG = "notepad";
    private ListView noteList;
    private int position = 0;
    SearchView searchView;
    private ArrayAdapter<String> adapter;
    private DBHelper dbhelper;
    private ArrayList<String> titles;
    private ArrayList<Item> items;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        ImageView addNotesIV=(ImageView)findViewById(R.id.addNotesIV);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateNote.class));
            }
        });
        addNotesIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateNote.class));
            }
        });

        noteList = (ListView) findViewById(R.id.noteList);
        dbhelper = new DBHelper(getApplicationContext());

        setNotes();
        this.registerForContextMenu(noteList);
        //searchView.clearFocus();
        //searchView.setQuery("", false);
        noteList.requestFocus();
     }

    public void setNotes() {
        titles = new ArrayList<>();
        items = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor notes = dbhelper.getNotes2(db);
        db.close();
        if (notes.moveToFirst()) {
            do {
                items.add(new Item(notes.getShort(0), notes.getString(1)));
            } while (notes.moveToNext());
        }
        for (Item i : items) {
            titles.add(i.getTitle());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        noteList.setAdapter(adapter);

        noteList.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        setNotes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        position = info.position;
        menu.setHeaderTitle(getResources().getString(R.string.CtxMenuHeader));
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

    }

   @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView tv = (TextView) noteList.getChildAt(position);
        String title = tv.getText().toString();
        switch (item.getItemId()) {
            case R.id.showNote:
                Intent mIntent = new Intent(this, OneNote.class);
                mIntent.putExtra("id", items.get(position).getId());
                startActivity(mIntent);
                break;

            case R.id.editNote:
                Intent i = new Intent(this, CreateNote.class);
                i.putExtra("id", items.get(position).getId());
                Log.d(TAG, title);
                i.putExtra("isEdit", true);
                startActivity(i);
                break;

            case R.id.removeNote:
                dbhelper.removeNote(items.get(position).getId());
                setNotes();
                break;

        }

        return false;

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        TextView tv = (TextView) arg1;
        String title = tv.getText().toString();
        Intent mIntent = new Intent(this, OneNote.class);
        mIntent.putExtra("title", title);
        mIntent.putExtra("id", items.get(arg2).getId());
        startActivity(mIntent);
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        ArrayList <String>newLIst=new ArrayList();
        for(int i=0;i<titles.size();i++){
            if(titles.get(i).toLowerCase().startsWith(newText.toLowerCase())){
                System.out.print("hhere"+titles.get(i));
                newLIst.add(titles.get(i));
            }
        }
        if(newLIst.size()>0)
        updateAdapter(newLIst);

        return false;
    }
    private  void updateAdapter(List<String> itemList){
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, itemList);
        noteList.setAdapter(adapter);
    }



    public void onBackPressed() {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);

    }

}