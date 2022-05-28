package com.example.kanlane;

public class User {
    public String UID, id, name, email, password, userBinder;

    public User(){}

    public User(String UID, String id, String name, String email, String password, String userBinder){
        this.UID = UID;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userBinder = userBinder;
    }
}
