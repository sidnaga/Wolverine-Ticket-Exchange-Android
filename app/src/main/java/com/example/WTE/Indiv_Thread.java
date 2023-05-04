package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Indiv_Thread extends AppCompatActivity {
    String other_user;
    //String tweet;
    ListView view;
    List<String> one_text;
    DatabaseReference replies_ref;
    DatabaseReference tweets_ref;
    FirebaseAuth auth;
    //TextView other_person;
    String mydate;
    //DatabaseReference tweets_ref;
    String sport;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.tweet){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Send a Tweet");
            final EditText tweetEditText = new EditText(this);
            builder.setView(tweetEditText);
            final String[] sports = {"Basketball","Hockey","Football"};
            builder.setSingleChoiceItems(sports, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sport = sports[which];
                }
            });
            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String tweet = tweetEditText.getText().toString();
                    String sender = auth.getCurrentUser().getEmail().toString();
                    String id = tweets_ref.push().getKey();
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    //Toast.makeText(getApplicationContext(),mydate,Toast.LENGTH_SHORT).show();
                    Tweet Tweet = new Tweet(sender, id, tweet, mydate,sport);
                    Tweet.setDate_and_Time(mydate);
                    tweets_ref.child(id).setValue(Tweet);
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
        else if(item.getItemId() == R.id.feed){
            Intent intent = new Intent(getApplicationContext(),feed_activity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(getApplicationContext(), settings.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.your_tweets){
            Intent intent = new Intent(getApplicationContext(),your_tweets.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.Logout){
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.games){
            Intent intent = new Intent(getApplicationContext(), sports.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.convos){
            Intent intent = new Intent(getApplicationContext(), OngoingConvosUsers.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiv__thread);
        //other_person = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        view = findViewById(R.id.ListView);
        replies_ref = FirebaseDatabase.getInstance().getReference("Replies");
        tweets_ref = FirebaseDatabase.getInstance().getReference("Tweets");
        one_text = new ArrayList();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,one_text);
        Intent intent = getIntent();
        other_user = intent.getStringExtra("other_user");
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(other_user);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //other_person.setText(other_user);
        replies_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                one_text.clear();
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    Reply reply = users.getValue(Reply.class);
                    //Log.i("person receiving",reply.getPerson_sending());
                    //Log.i("person receiving",reply.getPerson_receiving());
                    if ((reply.getPerson_receiving().equals(auth.getCurrentUser().getEmail()) && reply.getPerson_sending().equals(other_user))
                            || (reply.getPerson_sending().equals(auth.getCurrentUser().getEmail()) && reply.getPerson_receiving().equals(other_user))) {
                       one_text.add(reply.getPerson_sending() + "\n" + reply.getDate_and_Time() + "\n" + reply.getReply());
                    }
                }
                Collections.reverse(one_text);
                view.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //tweet = intent.getStringExtra("tweet");
        //one_text.add(sender + "\n" + tweet);
    }
    public void reply(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send a Reply");
        final EditText tweetEditText = new EditText(this);
        builder.setView(tweetEditText);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reply = tweetEditText.getText().toString();
                String sending = auth.getCurrentUser().getEmail().toString();
                String receiving = other_user;
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
