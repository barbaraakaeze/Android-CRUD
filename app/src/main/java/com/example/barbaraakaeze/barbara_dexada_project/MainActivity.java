package com.example.barbaraakaeze.barbara_dexada_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

//REMINDER: to show CRUD functionalities, uncomment ListViewAdapter
//Comment Intent
public class MainActivity extends AppCompatActivity {

    //Button
    public Button addBtn;
    String h;
    //DATETIME picker
    Format formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    private TextView dateTimeText;
    //Replaced Button with EditText
    private EditText uid, input_description, input_longitude, input_latitude, input_elevation,input_date, input_time;
    private ListView list_data;
    private ProgressBar circular_progress;
    //Initialize Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //Create array to store list of items
    private List<Items> list_items = new ArrayList<>();
    //Store selected item before it is passed
    private Items selecteditem;
    String description;
    Bundle bundle;
    FirebaseDatabase firebaseConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);

        //progress bar control
        //UPDATE
        circular_progress = (ProgressBar)findViewById(R.id.circular_progress);
        input_description = (EditText)findViewById(R.id.description);
        //input_email = (EditText)findViewById(R.id.email);
        input_longitude = (EditText)findViewById(R.id.longitude);
        input_latitude = (EditText)findViewById(R.id.latitude);
        input_elevation = (EditText) findViewById(R.id.elevation);
        //CODE for DATETIME picker
        dateTimeText = (TextView) findViewById(R.id.txt_datetime);
        input_date = (EditText)findViewById(R.id.date);
        input_time = (EditText)findViewById(R.id.time);
        list_data = (ListView) findViewById(R.id.list_data);

        //Click Edit Text to generate calendar
        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        input_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTime();
            }
        });

        //Update DateTimeTextview
                updateDateTimeTextView();


         //SaveButton
        addBtn = (Button) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent launchListView = new Intent(MainActivity.this, ListViewActivity.class);
                startActivity(launchListView);


               /* try {
                   // h = dateTime.getTime().toString();
                    File root = new File(Environment.getExternalStorageDirectory(), "Items");

                    if (!root.exists()) {
                        root.mkdirs(); //create file folder
                    }
                    File filepath = new File(root, h + ".txt");
                    FileWriter writer = new FileWriter(filepath);
                   // for (String string : list_items);
                   // writer.write(string);
                    for (Items s :list_items) {
                        //writer.write(s);
                        String post = "File generated with name " + h + ".txt";
                        Toast.makeText(MainActivity.this, post, Toast.LENGTH_LONG).show();


                    }
                    writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }*/
            }
        });//End Button
        clickListener();
       //Firebase data handling
        initializeFirebase();
        addFirebaseEventListener();
        //get intent from ListViewActivity
        FirebaseApp.initializeApp(this);
        //receieve posted data
        this.getData();
    }

    public void getData() {
       // String key =
        bundle = getIntent().getExtras();

        if (bundle != null) {

            input_description.setText(bundle.getString("description"));
            input_longitude.setText(bundle.getString("longitude"));
            input_latitude.setText(bundle.getString("latitude"));
            input_elevation.setText(bundle.getString("elevation"));
            // dateTimeText.setText(bundle.getString("dateTimeText"));

        }
    }

     public void clickListener() {
         //Retrieve data to editText View on click for Update.
         list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {

             @Override
             //Will load selected items onClick
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Items items = (Items) adapterView.getItemAtPosition(i);
                 selecteditem = items;
                 input_description.setText(items.getDescription());
                 //  input_email.setText(items.getEmail());
                 input_longitude.setText(items.getLongitude());
                 input_latitude.setText(items.getLatitude());
                 input_elevation.setText(items.getElevation());
                 dateTimeText.setText(items.getDateTime());

             }
         });
     }

    //Generate DatePicker Method
    private void updateDate() {
        new DatePickerDialog(this, date, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }
    //Generate TimePicker Method
    private void updateTime() {
        new TimePickerDialog(this, time, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE),true).show();
    }
    //DateTime method
    private void updateDateTimeTextView() {
        dateTimeText.setText(formatDateTime.format(dateTime.getTime()));
    }
    //date = object storing user date selection
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateTimeTextView();
        }
    };

    //time = object storing user time selection
    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateDateTimeTextView();
        }
    };

    public void addFirebaseEventListener() {


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

                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        Items items = postSnapshot.getValue(Items.class);
                        list_items.add(items);

                    }

                //Pass data from Database in to listView
               ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list_items);
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

    public void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handle Add functionalities to DB
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // Intent loadNewActivity = new Intent(MainActivity.this, ListViewActivity.class);
        // startActivity(loadNewActivity);
        //when add button is clicked, add to DB


        if (item.getItemId() == R.id.menu_add) {
            createItem();
        } //when save button is clicked, add or update DB
        else if (item.getItemId() == R.id.menu_save){
            if (selecteditem != null) {

                 Items items = new Items(selecteditem.getUid(), input_description.getText().toString(),
                        input_longitude.getText().toString(), input_latitude.getText().toString(), input_elevation.getText().toString(), dateTimeText.getText().toString());
                 updateItems(items);

            }
        } //when delete button is clicked, remove from DB
        else if (item.getItemId() == R.id.menu_delete) {
            if (selecteditem != null) {
                deleteItems(selecteditem);
            }
        }
        return  true;
    }
    //REMINDER: Update with new properties
    //********* Possibly update to delete data using date not ID. *********//
    public void deleteItems(Items item) {

        databaseReference.child("items").child(item.getUid()).removeValue();

        clearEditText();
    }

    //REMINDER: Update with new properties
    public void updateItems(Items item) {

            databaseReference.child("items").child(item.getUid()).child("description").setValue(item.getDescription());
            databaseReference.child("items").child(item.getUid()).child("datetime").setValue(item.getDateTime());
            databaseReference.child("items").child(item.getUid()).child("longitude").setValue(item.getLongitude());
            databaseReference.child("items").child(item.getUid()).child("latitude").setValue(item.getLatitude());
            databaseReference.child("items").child(item.getUid()).child("elevation").setValue(item.getElevation());


        clearEditText();
    }

    //REMINDER: Update with new properties
    //Insert all items into database
    public void createItem() {
        //Load new activity on click

        Items item = new Items(UUID.randomUUID().toString(), input_description.getText().toString(), dateTimeText.getText().toString(),
                input_longitude.getText().toString(), input_latitude.getText().toString(), input_elevation.getText().toString());
        databaseReference.child("items").child(item.getUid()).setValue(item);
        //Clear editText box after data has been entered in DB
        clearEditText();
    }
    //Clear editText box after data has been entered in DB
    private void clearEditText() {
        input_description.setText("");
        dateTimeText.setText("");
        input_longitude.setText("");
        input_latitude.setText("");
        input_elevation.setText("");
    }

}

    
