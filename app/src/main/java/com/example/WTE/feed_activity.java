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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class feed_activity extends AppCompatActivity {
    DatabaseReference tweets_ref;
    List<String> list_of_tweets;
    ListView view;
    List<Tweet> tweets_list_for_reference;
    //String mydate;
    FirebaseAuth auth;
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
                    Toast.makeText(getApplicationContext(),mydate,Toast.LENGTH_SHORT).show();
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
        else if(item.getItemId() == R.id.your_tweets){
            Intent intent = new Intent(getApplicationContext(),your_tweets.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.Logout){
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(getApplicationContext(), settings.class);
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
        setContentView(R.layout.activity_feed_activity);
        auth = FirebaseAuth.getInstance();
        view = findViewById(R.id.ListView);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.umich_wte_logo_resized);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        list_of_tweets = new ArrayList<>();
        //mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        tweets_list_for_reference = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_tweets);
        tweets_ref = FirebaseDatabase.getInstance().getReference("Tweets");
        tweets_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(), "No Tweets Yet", Toast.LENGTH_SHORT).show();
                }
                if (dataSnapshot.exists()) {
                    list_of_tweets.clear();
                    tweets_list_for_reference.clear();
                    for (DataSnapshot tweet : dataSnapshot.getChildren()) {
                        Tweet Tweet = tweet.getValue(Tweet.class);

                        tweets_list_for_reference.add(Tweet);
                        list_of_tweets.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                Tweet.getSport() + "\n" + Tweet.getTweet());
                    }
                    Collections.reverse(list_of_tweets);
                    Collections.reverse(tweets_list_for_reference);
                    view.setAdapter(adapter);
                    view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String username = tweets_list_for_reference.get(position).getSender();
                            String tweet = tweets_list_for_reference.get(position).getTweet();
                            String sport = tweets_list_for_reference.get(position).getSport();
                            String Date_and_Time = tweets_list_for_reference.get(position).getDate_and_Time();
                            Intent intent = new Intent(getApplicationContext(), Indiv_Tweet.class);
                            intent.putExtra("sender", username);
                            intent.putExtra("tweet", tweet);
                            intent.putExtra("sport",sport);
                            intent.putExtra("Date_and_Time",Date_and_Time);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
