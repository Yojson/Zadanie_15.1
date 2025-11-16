package com.example.zadanie_151;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText titleInput;
    private EditText noteInput;
    private EditText deleteIdInput;
    private EditText updateIdInput;
    private EditText updateNoteInput;
    private Button saveButton;
    private Button deleteButton;
    private Button updateButton;
    private TextView notesDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        titleInput = findViewById(R.id.titleInput);
        noteInput = findViewById(R.id.noteInput);
        deleteIdInput = findViewById(R.id.deleteIdInput);
        updateIdInput = findViewById(R.id.updateIdInput);
        updateNoteInput = findViewById(R.id.updateNoteInput);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);
        notesDisplay = findViewById(R.id.notesDisplay);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { add_note(); }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { delete_note(); }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { update_note(); }
        });

        load_notes();
    }

    private void add_note() {
        String titleText = titleInput.getText().toString().trim();
        String noteText = noteInput.getText().toString().trim();

        if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(noteText)) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, titleText);
        values.put(DatabaseHelper.COLUMN_NOTE, noteText);

        db.insert(DatabaseHelper.TABLE_NOTES, null, values);
        db.close();

        titleInput.setText("");
        noteInput.setText("");
        load_notes();
    }

    private void delete_note() {
        String idToDelete = deleteIdInput.getText().toString().trim();
        if (TextUtils.isEmpty(idToDelete)) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NOTES,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{idToDelete});
        db.close();

        deleteIdInput.setText("");
        load_notes();
    }

    private void update_note() {
        String idToUpdate = updateIdInput.getText().toString().trim();
        String newNoteText = updateNoteInput.getText().toString().trim();

        if (TextUtils.isEmpty(idToUpdate) || TextUtils.isEmpty(newNoteText)) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE, newNoteText);

        db.update(DatabaseHelper.TABLE_NOTES,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{idToUpdate});
        db.close();

        updateIdInput.setText("");
        updateNoteInput.setText("");
        load_notes();
    }

    private void load_notes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_NOTE
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NOTES,
                projection,
                null, null, null, null,
                DatabaseHelper.COLUMN_ID + " DESC"
        );

        StringBuilder notes = new StringBuilder();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE));
                notes.append("ID: ").append(id).append("\n")
                        .append("Tytuł: ").append(title).append("\n")
                        .append("Treść: ").append(note).append("\n\n");
            }
            cursor.close();
        }
        db.close();

        notesDisplay.setText(notes.toString());
    }
}
