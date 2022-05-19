package com.example.kanlane;

public class User {
    public String id, name, email, password, user_binder;

    public User(){

    }

    public User(String id, String name, String email, String password, String user_binder){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.user_binder = user_binder;



    }
    public String get_my_email(){
        return this.email;
    }
    public String get_my_user_binder(){
        return this.user_binder;
    }


}
