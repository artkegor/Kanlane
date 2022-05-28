package com.example.kanlane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button mAccountBtn;
    Button mSosButton;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    public String emailFrom, emailTo;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference uidRef, dataBase;
    String userData = "User";
    String currentUser;
    String userBinder;

    //EmailTo это Email
    //EmailFrom это UserBinder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Для полноэкранного режима и скрывания шапки.
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Базы данных
        mAccountBtn = findViewById(R.id.accountBtn);
        mSosButton = findViewById(R.id.sosButton);

        //Получение данных из базы данных Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataBase = FirebaseDatabase.getInstance().getReference();
        uidRef = dataBase.child("Users/").child(uid);

        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    userBinder = snapshot.child("userBinder").getValue(String.class);
                }else{
                        Log.d("TAG", task.getException().getMessage());
                    }
            }
        });

        //Кнопка SOS
        mSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference().child("Sos");
                Toast.makeText(MainActivity.this, "Сообщение отправлено!", Toast.LENGTH_LONG).show();
                Sos sos = new Sos(email, userBinder);
                databaseReference.push().setValue(sos);
            }
        });

        //Кнопка настроек аккаунта (переход в Account.activity)
        mAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Account.class));

            }
        });

        /*private void listenChanges() {
            FirebaseDatabase listenDatabase = FirebaseDatabase.getInstance("https://chat-app-5b288-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference reference = listenDatabase.getReference(request);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dbLog("Data Changed", snapshot.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dbLog("DB Error!", getColoredSpanned(error.getMessage(), "#cc0000"));
                }
            });
        }*/
    }

}