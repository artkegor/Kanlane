package com.example.kanlane;
import java.util.Date;


public class Sos {
    public String emailto;
    public String emailfrom;
    public long time;

    public Sos(String emailto, String emailfrom){
        this.emailto = emailto;
        this.emailfrom = emailfrom;
        this.time = System.currentTimeMillis();


    }
}
