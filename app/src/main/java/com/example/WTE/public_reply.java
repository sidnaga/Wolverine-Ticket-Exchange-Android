package com.example.snapchatclone;

public class public_reply {
    private String creator;
    private String thread;
    private String person_of_reply;
    private String reply;
    private String time_and_date;
    public public_reply(){

    }
    public public_reply(String creator_in,String thread_in,String person_of_reply,String reply,String time_and_date){
        this.creator = creator_in;
        this.thread = thread_in;
        this.person_of_reply = person_of_reply;
        this.reply = reply;
        this.time_and_date = time_and_date;
    }

    public String getCreator() {
        return creator;
    }

    public String getThread() {
        return thread;
    }

    public String getPerson_of_reply() {
        return person_of_reply;
    }

    public String getReply() {
        return reply;
    }

    public String getTime_and_date() {
        return time_and_date;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public void setPerson_of_reply(String person_of_reply) {
        this.person_of_reply = person_of_reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setTime_and_date(String time_and_date) {
        this.time_and_date = time_and_date;
    }
}
