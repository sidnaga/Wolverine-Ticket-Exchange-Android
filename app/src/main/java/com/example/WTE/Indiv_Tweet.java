package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class Indiv_Tweet extends AppCompatActivity {
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
                    sports2 = sports[which];
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
                    Tweet Tweet = new Tweet(sender, id, tweet, mydate,sports2);
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
        setContentView(R.layout.activity_indiv__tweet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.umich_wte_logo_resized);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        auth = FirebaseAuth.getInstance();
        view = findViewById(R.id.ListView);
        public_replies_list = new ArrayList<public_reply>();
        num_tweets_and_replies = findViewById(R.id.textView2);
        replies_ref = FirebaseDatabase.getInstance().getReference("Replies");
        tweets_ref = FirebaseDatabase.getInstance().getReference("Tweets");
        public_replies = FirebaseDatabase.getInstance().getReference("Public Replies");
        one_text = new ArrayList();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,one_text);
        Intent intent = getIntent();
        sender = intent.getStringExtra("sender");
        tweet = intent.getStringExtra("tweet");
        sport = intent.getStringExtra("sport");
        Date_and_Time = intent.getStringExtra("Date_and_Time");
        one_text.add(sender + "\n" + Date_and_Time + "\n" + sport + "\n" + tweet);
        public_replies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    view.setAdapter(adapter);
                    num_tweets_and_replies.setText("0" + " replies");
                }
                if(dataSnapshot.exists()){
                    one_text.clear();
                    public_replies_list.clear();
                    one_text.add(sender + "\n" + Date_and_Time + "\n" + sport + "\n" + tweet);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        public_reply pub_reply = snapshot.getValue(public_reply.class);
                        if(pub_reply.getThread().equals(tweet)) {
                            one_text.add(pub_reply.getPerson_of_reply() + "\n" + pub_reply.getTime_and_date() + "\n" + pub_reply.getReply());
                            public_replies_list.add(pub_reply);
                        }
                    }
                    //Collections.reverse(one_text);
                    if(one_text.size()==2){
                        num_tweets_and_replies.setText(Integer.toString(1) + " reply");
                    }
                    else {
                        num_tweets_and_replies.setText(Integer.toString(one_text.size() - 1) + " replies");
                    }
                    view.setAdapter(adapter);
                    view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(),indiv_pub_reply.class);
                            public_reply pub_rep = (public_reply) public_replies_list.get(position-1);
                            intent.putExtra("person_of_reply",pub_rep.getPerson_of_reply());
                            intent.putExtra("date_and_time",pub_rep.getTime_and_date());
                            intent.putExtra("reply",pub_rep.getReply());
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
    public void reply_public(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send a Reply");
        final EditText tweetEditText = new EditText(this);
        builder.setView(tweetEditText);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reply = tweetEditText.getText().toString();
                String sending = auth.getCurrentUser().getEmail().toString();
                //String receiving = sender;
                String id = replies_ref.push().getKey();
                mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                public_reply Reply = new public_reply(sender,tweet,sending,reply,mydate);
                Reply.setTime_and_date(mydate);
                public_replies.child(id).setValue(Reply);
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
