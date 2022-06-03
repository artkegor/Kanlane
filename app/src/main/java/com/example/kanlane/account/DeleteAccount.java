package com.example.kanlane.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kanlane.R;
import com.example.kanlane.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccount extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        mEmail = findViewById(R.id.email_delete);
        mPassword = findViewById(R.id.password_delete);
        mSubmit = findViewById(R.id.delete_acc_btn);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteuser(mEmail.getText().toString(), mPassword.getText().toString());

            }
        });
    }

    private void deleteuser(String email, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("AccountDelete", "Account Deleted!");
                                Intent accDeleted = new Intent(DeleteAccount.this, Register.class);
                                startActivity(accDeleted);
                                Toast.makeText(DeleteAccount.this, "Пользователь удален", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        }
    }
}