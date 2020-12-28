package com.list.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sharedpreferences.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    Button logout;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    FloatingActionButton add;
    FirebaseDatabase database;
    List<NotesModel> nm;
    DatabaseReference  myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         database = FirebaseDatabase.getInstance();
        myRef  = database.getReference("notes");

        logout = findViewById(R.id.logoutbutton);
        recyclerView = findViewById(R.id.recone);
        nm = new ArrayList<>();
        add = findViewById(R.id.addbutton);

     add.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             setupDialog();
         }
     });

     logout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             logOutActivity();
         }
     });



    }

    private void logOutActivity() {

        PreferenceManager.getInstance(MainActivity.this).clear();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    private void setupDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.add_note,null);
         final EditText titleEditText = view.findViewById(R.id.dialogTitle);
         final EditText descEditText = view.findViewById(R.id.dialogDesc);
        Button addHere = view.findViewById(R.id.dialogButton);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        addHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String Desc = descEditText.getText().toString();
                NotesModel notesModel = new NotesModel();
                notesModel.setTitle(title);
                notesModel.setDescription(Desc);


                nm.add(notesModel);
                NotesAdapter notesAdapter = new NotesAdapter(nm, getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(notesAdapter);

                myRef.setValue(title);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();



    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
