package com.example.kanlane.firebase;


public class Sos {
    public String phoneTo;
    public String emailFrom;
    public long time;

    public Sos(){}

    public Sos(String emailFrom, String phoneTo){
        this.emailFrom = emailFrom;
        this.phoneTo = phoneTo;
        this.time = System.currentTimeMillis();
    }


}
