package com.example.kanlane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterLoginScreen;
    TextView mForgotPassword;
    FirebaseAuth fAuth;
    String email, userBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Переменные
        mEmail = findViewById(R.id.emailLogin);
        mPassword = findViewById(R.id.passwordLogin);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterLoginScreen = findViewById(R.id.registerLoginscreen);
        mForgotPassword = findViewById(R.id.forgotPassword);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        //Кнопка войти
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //Проверка условий входа в аккаунт
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Введите почту...");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mEmail.setError("Введите пароль...");
                    return;
                }

                if(password.length() < 8 ){
                    mPassword.setError("Пароль должен состоять из 8-ми символов.");
                    return;
                }

                //Аутентификация пользователя
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Login.this, "Вы вошли в аккаунт.",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(Login.this, "Произошла ошибка. Повторите." + task.getException()
                                    .getMessage(),Toast.LENGTH_LONG).show();
                        }
                        }

                });
            }

        });

        //В случае если пользователь еще не зарегистрирован
        mRegisterLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));

            }
        });

        //Забыли пароль
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Сбросить пароль.");
                passwordResetDialog.setMessage("Введите почту на которую мы отправим инструкцию по сбросу пароля");
                passwordResetDialog.setView(resetmail);

                passwordResetDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mailReset = resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mailReset).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Письмо с инструкцией по сбросу " +
                                        "пароля отправлено.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                passwordResetDialog.create().show();
            }
        });

    }
}