package com.example.snapchatclone;

public class Users {
    private String email;
    private String id;
    public Users(){

    }
    public Users(String email,String id){
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
