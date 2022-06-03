package com.example.kanlane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


import com.example.kanlane.account.Account;
import com.example.kanlane.firebase.Sos;
import com.example.kanlane.notes.NoteEditorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    Button mSosButton;
    Button mCreateReminder;
    FirebaseAuth fAuth;
    DatabaseReference uidRef, dataBase;
    String userBinder;
    String sosMessage;
    String email;
    ListView noteListView;
    public static ArrayList<String> notes = new ArrayList<>();
    public static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {

            Intent creatingNote = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(creatingNote);
            return true;
        }
        if (item.getItemId() == R.id.account_settings){
            Intent accountSettings = new Intent(getApplicationContext(), Account.class);
            startActivity(accountSettings);

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Запрашиваем разрешения
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        //Получаем необходимые элементы интерфейса
        mSosButton = findViewById(R.id.sosButton);
        noteListView = findViewById(R.id.noteListView);

        //Работает!
        Log.d("Start", "App started!");

        //Напоминания
        SharedPreferences sharedPreferences = getApplicationContext().
                getSharedPreferences("com.example.kanlane.notes",
                        Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null) {
            notes.add("Пример заметки");
        }else{
            notes = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        noteListView.setAdapter(arrayAdapter);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent noteIntent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                noteIntent.putExtra("noteId", position);
                startActivity(noteIntent);
            }
        });

        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Вы уверены?")
                        .setMessage("Вы хотите удалить заметку?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().
                                        getSharedPreferences("com.example.kanlane.notes",
                                                Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();

                            }
                        }).setNegativeButton("Нет", null)
                        .show();

                return true;
            }
        });


        //Получение данных из базы данных Firebase
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataBase = FirebaseDatabase.getInstance().getReference();
        uidRef = dataBase.child("Users/").child(uid);
        email = user.getEmail();


        //Получаем из базы данных привязанный номер телефона
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

        //Кнопка SOS
        sosMessage = "Пользователь " + email + " попал в беду!";
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

    }

    //Отправка СМС
    private void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(userBinder, null, sosMessage, null, null);
    }

}
