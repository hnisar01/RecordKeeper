package com.example.recordkeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    Button b1, b2;
    EditText ed1, ed2;
    int counter = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);

            b1 = (Button) findViewById(R.id.button1);
            b1.setOnClickListener(this::onClick);

            //find the inputs for the click event
            ed1 = (EditText) findViewById(R.id.editText1);
            ed2 = (EditText) findViewById(R.id.editText2);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {

            // Going from MainActivity to NotesEditorActivity
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v)
    {
        try {
            // this is cryptographically insecure, and not recommended
            // if (ed1.getText().toString().equals("admin") && ed2.getText().toString().equals("admin"))
            if (0==0)
            {
                setContentView(R.layout.activity_main);

                ListView listView = findViewById(R.id.listView);
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
                if (set == null) {
                    notes.add("Example note");
                } else {
                    notes = new ArrayList(set);
                }

                // Using custom listView Provided by Android Studio
                arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes);

                listView.setAdapter(arrayAdapter);

                listView.setOnItemClickListener((adapterView, view, i, l) -> {

                    // Going from MainActivity to NotesEditorActivity
                    Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                    intent.putExtra("noteId", i);
                    startActivity(intent);

                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        final int itemToDelete = i;
                        // To delete the data from the App
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure?")
                                .setMessage("Do you want to delete this note?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        notes.remove(itemToDelete);
                                        arrayAdapter.notifyDataSetChanged();
                                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                        HashSet<String> set = new HashSet(MainActivity.notes);
                                        sharedPreferences.edit().putStringSet("notes", set).apply();
                                    }
                                }).setNegativeButton("No", null).show();
                        return true;
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Wrong credentials, " + (counter-1) + " tries left", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "System error, " + (counter-1) + " tries left", Toast.LENGTH_SHORT).show();
        }
//disable button after 3 attempts for better security
        counter--;
        if (counter <= 0) {
            b1.setEnabled(false);
        }
    }
}

