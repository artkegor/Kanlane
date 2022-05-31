package com.example.kanlane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


import com.example.kanlane.firebase.Sos;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button mAccountBtn;
    Button mSosButton;
    FirebaseAuth fAuth;
    DatabaseReference uidRef, dataBase;
    String userBinder;
    String sosMessage;
    FusedLocationProviderClient fusedLocationProviderClient;
    double longitude;
    double latitude;
    String mLongitude, mLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Запрашиваем разрешения
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);

        //Для полноэкранного режима и скрывания шапки.
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Базы данных
        mAccountBtn = findViewById(R.id.accountBtn);
        mSosButton = findViewById(R.id.sosButton);

        //Получение данных из базы данных Firebase
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataBase = FirebaseDatabase.getInstance().getReference();
        uidRef = dataBase.child("Users/").child(uid);

        //Получение из базы данных привязанный номер телефона
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    userBinder = snapshot.child("userBinder").getValue(String.class);
                } else {
                    Log.d("TAG", task.getException().getMessage());
                }
            }
        });

        //Получение текущей геолокации
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Кнопка SOS
        sosMessage = email + " попал в беду!";
        mSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference().child("Sos");
                Toast.makeText(MainActivity.this, "Сообщение отправлено!", Toast.LENGTH_SHORT).show();
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

    }


    //Отправка СМС
    private void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(userBinder, null, sosMessage, null, null);
    }

}
