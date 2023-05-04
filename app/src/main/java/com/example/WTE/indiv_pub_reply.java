package com.example.snapchatclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class indiv_pub_reply extends AppCompatActivity {
    String sender;
    String tweet;
    String sport;
    String Date_and_Time;
    ListView view;
    ArrayList one_text;
    ArrayList public_replies_list;
    DatabaseReference replies_ref;
    DatabaseReference tweets_ref;
    DatabaseReference public_replies;
    FirebaseAuth auth;
    String mydate;
    String sports2;
    TextView num_tweets_and_replies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiv_pub_reply);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.umich_wte_logo_resized);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        auth = FirebaseAuth.getInstance();
        replies_ref = FirebaseDatabase.getInstance().getReference("Replies");
        view = findViewById(R.id.view);
        one_text = new ArrayList();
        Intent intent = getIntent();
        sender = intent.getStringExtra("person_of_reply");
        String Date_and_Time = intent.getStringExtra("date_and_time");
        String reply = intent.getStringExtra("reply");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,one_text);
        one_text.add(sender + "\n" + Date_and_Time + "\n" + reply);
        view.setAdapter(adapter);


    }
    public void reply_private(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send a Reply");
        final EditText tweetEditText = new EditText(this);
        builder.setView(tweetEditText);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reply = tweetEditText.getText().toString();
                String sending = auth.getCurrentUser().getEmail().toString();
                String receiving = sender;
                String id = replies_ref.push().getKey();
                mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Reply Reply = new Reply(sending,receiving,id,reply,mydate);
                Reply.setDate_and_Time(mydate);
                replies_ref.child(id).setValue(Reply);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }
}
