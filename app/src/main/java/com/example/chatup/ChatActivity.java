package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private static DatabaseReference reference;
    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;

    EditText etMessageSent;
    TextView etMessageDisplay;
    Button sendMessageBtn;
    private String name;
    private String temp_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        request_user_name();

        etMessageSent = (EditText) findViewById(R.id.etMessageSent);
        etMessageDisplay = (TextView) findViewById(R.id.etMessageDisplay);
        sendMessageBtn = (Button) findViewById(R.id.sendMessageBtn);

        reference = FirebaseDatabase.getInstance("https://chat-up-663ab-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Chat");

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Saving messages to database with chosen username.
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = reference.push().getKey();
                reference.updateChildren(map);

                DatabaseReference message_root = reference.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("username", name);
                map2.put("message", etMessageSent.getText().toString());

                message_root.updateChildren(map2);

                etMessageSent.setText("");

            }
        });

    reference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            append_chat_conversation(dataSnapshot);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            append_chat_conversation(dataSnapshot);

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }

    // Appends messages and display them in TextView.
    private String chat_msg,chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            etMessageDisplay.append(chat_user_name +" : "+chat_msg +" \n");
        }


    }

    // Creates and alertdialog to enter an username.
    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name:");

        final EditText input_field = new EditText(this);

        builder.setView(input_field);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = input_field.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_user_name();
            }
        });

        builder.show();
    }

}
