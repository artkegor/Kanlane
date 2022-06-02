package com.example.kanlane.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.kanlane.MainActivity;
import com.example.kanlane.R;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        EditText editText = findViewById(R.id.noteEditText);

        Intent noteIntent = getIntent();
        noteId = noteIntent.getIntExtra("noteId", -1);

        if (noteId != -1){
            editText.setText(MainActivity.notes.get(noteId));
        } else {

            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();

        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().
                        getSharedPreferences("com.example.kanlane.notes",
                                Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(MainActivity.notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}