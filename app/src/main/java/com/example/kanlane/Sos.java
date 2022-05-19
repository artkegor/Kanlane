package com.example.kanlane;
import java.util.Date;


public class Sos {
    public User emailto;
    public User emailfrom;
    public long time;

    public Sos(User emailto, User emailfrom){
        this.emailto = emailto;
        this.emailfrom = emailfrom;
        this.time = System.currentTimeMillis();


    }
}
