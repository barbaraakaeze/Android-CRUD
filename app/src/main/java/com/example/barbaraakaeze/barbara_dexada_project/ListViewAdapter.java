package com.example.barbaraakaeze.barbara_dexada_project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anita Akaeze on 2/19/2017.
 */
//REMINDER: Update and include all properties in the table
public class ListViewAdapter extends BaseAdapter {

    Activity activity;
    List<Items> listItems;
    LayoutInflater inflater;

    public ListViewAdapter(Activity activity, List<Items> listItems) {
        this.activity = activity;
        this.listItems = listItems;
    }

    //Get number of items in the list
    @Override
    public int getCount() {
        return listItems.size();
    }

    //Get individual item in list
    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    //Get item ID
    @Override
    public long getItemId(int i) {
        return i;
    }

    //Get all data and enter in listView
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview, null);

        //Set all data from text view into listView
        TextView txtDescription = (TextView) itemView.findViewById(R.id.list_description);
        // TextView txtEmail = (TextView)itemView.findViewById(R.id.list_email);
        TextView txtLongitude = (TextView) itemView.findViewById(R.id.list_logitude);
        TextView txtLatitude = (TextView) itemView.findViewById(R.id.list_latitude);
        TextView txtElevation = (TextView) itemView.findViewById(R.id.list_elevation);
        TextView txtDateTime = (TextView) itemView.findViewById(R.id.list_datetime);


        txtDescription.setText(listItems.get(i).getDescription());
        // txtEmail.setText(listItems.get(i).getEmail());
        txtLatitude.setText(listItems.get(i).getLatitude());
        txtLongitude.setText(listItems.get(i).getLongitude());
        txtElevation.setText(listItems.get(i).getElevation());
        txtDateTime.setText(listItems.get(i).getDateTime());

        return itemView;
    }


}
