package com.example.kanlane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Account extends AppCompatActivity {

    Button mAccountBtn;
    FirebaseAuth fAuth;
    DatabaseReference mDatabase;
    String User_Bind;
    EditText mUser_Bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        //База данных
        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    //Кнопка Выйти
    public void logout (View view){
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

}