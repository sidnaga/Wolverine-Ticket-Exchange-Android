package com.example.snapchatclone;

public class Reply {
    private String person_sending;
    private String person_receiving;
    private String id;
    private String reply;
    private String Date_and_Time;
    public Reply(){

    }
    public Reply(String person_sending_in,String person_receiving_in,String id_in,String reply,String date_and_Time_in){
        this.person_sending = person_sending_in;
        this.person_receiving = person_receiving_in;
        this.id = id_in;
        this.reply = reply;
        this.Date_and_Time = date_and_Time_in;
    }

    public String getPerson_sending() {
        return person_sending;
    }

    public String getPerson_receiving() {
        return person_receiving;
    }

    public String getId() {
        return id;
    }

    public String getReply() {
        return reply;
    }

    public void setDate_and_Time(String date_and_Time) {
        Date_and_Time = date_and_Time;
    }

    public String getDate_and_Time() {
        return Date_and_Time;
    }
}
