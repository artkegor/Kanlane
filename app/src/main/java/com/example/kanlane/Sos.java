package com.example.kanlane;
import android.database.Cursor;

import java.util.Date;


public class Sos {
    public String emailTo;
    public String emailFrom;
    public long time;

    public Sos(){}

    public Sos(String emailFrom, String emailTo){
        this.emailTo = emailTo;
        this.emailFrom = emailFrom;
        this.time = System.currentTimeMillis();
    }


}
