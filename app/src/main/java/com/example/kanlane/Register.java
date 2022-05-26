package com.example.kanlane;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuthException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {
    //Переменные
    EditText mName, mEmail, mPassword, mUserBinder;
    Button mRegisterBtn;
    TextView mLoginbtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userData = "User";
    DatabaseReference mDataBase;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Для полноэкранного режима и скрывания шапки
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Переменные
        mName = findViewById(R.id.Name);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mUserBinder = findViewById(R.id.userBinder);
        mLoginbtn = findViewById(R.id.loginMainscreen);
        mRegisterBtn = findViewById(R.id.registerBtn);

        //Базы данных
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(userData);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //Кнопка регистрации
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Переменные для регистрации
                String id = mDataBase.getKey();
                String name = mName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String userBinder = mUserBinder.getText().toString().trim();

                //Проверка условий регистрации
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Введите почту...");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mEmail.setError("Введите пароль...");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    mName.setError("Введите имя...");
                    return;
                }
                if (password.length() < 8) {
                    mPassword.setError("Пароль должен состоять из 8-ми символов.");
                    return;
                }

                //Регистрация пользователя
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User newUser = new User(currentUser, id, name, email, password, userBinder);
                            mDataBase.push().setValue(newUser);

                            Toast.makeText(Register.this, "Пользователь зарегистрирован.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(Register.this, "Произошла ошибка. Повторите." +
                                    task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

       //В случае, если пользователь зарегистрирован
        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

}
