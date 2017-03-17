package com.example.barbaraakaeze.barbara_dexada_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ListViewActivity extends  AppCompatActivity {

    private List<Items> list_items = new ArrayList<>();
    private ListView list_data;
    private ProgressBar circular_progress;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Items selecteditem;
    private EditText input_description, input_longitude, input_latitude, input_elevation,input_date, input_time;
    private TextView dateTimeText;

    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        //Instantiate Toolbar
        /*Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Items");
        setSupportActionBar(toolbar);*/

        //Handle Search
       /* searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Filter as typing commences
                list_data.getTextFilter();
                return false;
            }
        });*/

        circular_progress = (ProgressBar) findViewById(R.id.circular_progress2);
        list_data = (ListView) findViewById(R.id.list_data2);

        //Firebase data handling
                initializesFirebase();
                addsFirebaseEventListener();

        list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Items items = (Items)  parent.getItemAtPosition(i);
                selecteditem = items;

                    Intent loadMainActivity = new Intent(ListViewActivity.this, MainActivity.class);

                    loadMainActivity.putExtra("description", items.getDescription());
                    loadMainActivity.putExtra("longitude", items.getLongitude());
                    loadMainActivity.putExtra("latitude", items.getLatitude());
                    loadMainActivity.putExtra("elevation", items.getElevation());
                    startActivity(loadMainActivity);


            }
        });


      //  list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {

          //  @Override
            //Will load selected items onClick
           // public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Items items = (Items)adapterView.getItemAtPosition(i);
               // selecteditem = items;


          //  }
      //  });

        }

    private void addsFirebaseEventListener() {
        //Progressbar
        circular_progress.setVisibility(View.VISIBLE);
        list_data.setVisibility(View.INVISIBLE);

        //Post data to list view when entered in Edit text
        databaseReference.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //If there is any content in the DB, post data to list
                if (list_items.size() > 0) {

                }
                list_items.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Items items = postSnapshot.getValue(Items.class);
                    list_items.add(items);

                }

                //Pass data from Database in to listView
                ListViewAdapter adapter = new ListViewAdapter(ListViewActivity.this, list_items);
                list_data.setAdapter(adapter);



                //if data is populated in listView, hide progressBar
                circular_progress.setVisibility(View.INVISIBLE);
                list_data.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });
        }

            private void initializesFirebase() {
                FirebaseApp.initializeApp(this);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
            }

    }

