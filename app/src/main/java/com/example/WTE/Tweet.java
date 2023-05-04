package com.example.snapchatclone;

import android.util.Log;
import android.widget.Toast;

public class Tweet {
    private String sender;
    private String id;
    private String tweet;
    private String Date_and_Time;
    private String sport;
    public Tweet(){

    }
    public Tweet(String sender_in,String id_in,String tweet_in,String Date_and_Time_in, String sport_in){
        this.sender = sender_in;
        this.id = id_in;
        this.tweet = tweet_in;
        this.Date_and_Time = Date_and_Time_in;
        this.sport = sport_in;
    }

    public String getSender() {
        return sender;
    }

    public String getId() {
        return id;
    }

    public String getTweet() {
        return tweet;
    }

    public void setDate_and_Time(String date_and_Time) {
        Date_and_Time = date_and_Time;
    }

    public String getDate_and_Time() {
        //Log.e("Date and Time","hi");
        return Date_and_Time;
    }

    public String getSport() {
        return sport;
    }
}
