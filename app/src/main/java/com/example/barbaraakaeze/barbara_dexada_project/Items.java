package com.example.barbaraakaeze.barbara_dexada_project;

import java.util.Objects;

/**
 * Created by Anita Akaeze on 2/19/2017.
 */
public class Items {
    private String uid;
    private String description;
   // private String email;
    private String longitude;
    private String latitude;
    private String elevation;
    private String dateTime;

    public Items() {
    }


    public Items(String uid, String description, String longitude, String latitude, String elevation, String dateTime) {
        this.uid = uid; //PrimaryKey
     //   this.email = email;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.dateTime = dateTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  /*  public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
