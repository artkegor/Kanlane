package com.example.kanlane.firebase;

public class User {
    public String UID, id, name, email, userBinder, FcmToken;

    public User(){}

    public User(String UID, String id, String name, String email, String userBinder){
        this.UID = UID;
        this.id = id;
        this.name = name;
        this.email = email;
        this.userBinder = userBinder;
    }
}
