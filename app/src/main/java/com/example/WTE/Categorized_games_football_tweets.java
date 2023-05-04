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
import android.widget.FrameLayout;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Categorized_games_football_tweets extends AppCompatActivity {
    List<String> tweets_per_team;
    List<Tweet> list_of_tweets;
    ListView view;
    TextView text;
    FirebaseAuth auth;
    String sport;
    DatabaseReference tweets;
    FrameLayout flayout;
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
                    String id = tweets.push().getKey();
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    Toast.makeText(getApplicationContext(),mydate,Toast.LENGTH_SHORT).show();
                    Tweet Tweet = new Tweet(sender, id, tweet, mydate,sport);
                    Tweet.setDate_and_Time(mydate);
                    tweets.child(id).setValue(Tweet);
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
        setContentView(R.layout.activity_categorized_games_tweets);
        Intent intent = getIntent();
        view = findViewById(R.id.listView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.umich_wte_logo_resized);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        list_of_tweets = new ArrayList<>();
        text = new TextView(this);
        text.setEnabled(false);
        tweets_per_team = new ArrayList<>();
        flayout = findViewById(R.id.framelayout);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,tweets_per_team);
        final String game = intent.getStringExtra("game");
        tweets = FirebaseDatabase.getInstance().getReference("Tweets");
        tweets.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(),"No tweets for this game",Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setTitle(" " + game + " Tweets");
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    if(game.equals("Ohio State")) {
                        flayout.setBackgroundResource(R.drawable.ohiostate);
                    }
                    if(game.equals("Michigan State")) {
                        flayout.setBackgroundResource(R.drawable.spartan);
                    }
                    if(game.equals("Western Michigan")) {
                        flayout.setBackgroundResource(R.drawable.western);
                    }
                    if(game.equals("Rutgers")) {
                        flayout.setBackgroundResource(R.drawable.rutgers);
                    }
                    if(game.equals("Washington")) {
                        flayout.setBackgroundResource(R.drawable.washington);
                    }
                    if(game.equals("Northwestern")) {
                        flayout.setBackgroundResource(R.drawable.northwestern);
                    }
                    if(game.equals("Northern Illinois")) {
                        flayout.setBackgroundResource(R.drawable.northern);
                    }
                }
                if(dataSnapshot.exists()) {
                    tweets_per_team.clear();
                    list_of_tweets.clear();
                    for (DataSnapshot tweet : dataSnapshot.getChildren()) {
                        Tweet Tweet = tweet.getValue(Tweet.class);
                        if(Tweet.getSport().equals("Football")) {
                            String[] split_tweet = Tweet.getTweet().split(" ");
                            for (int a = 0; a < split_tweet.length; ++a) {
                                if (game.equals("Ohio State")) {
                                    if (split_tweet[a].equals("Ohio") || split_tweet[a].equals("ohio") || split_tweet[a].equals("OHIO")
                                            || split_tweet[a].equals("OSU") || split_tweet[a].equals("Osu")
                                            || split_tweet[a].equals("osu") || split_tweet[a].equals("Buckeyes") || split_tweet[a].equals("buckeyes")
                                            || split_tweet[a].equals("OSU.") || split_tweet[a].equals("Osu.")
                                            || split_tweet[a].equals("osu.") || split_tweet[a].equals("OSU,") || split_tweet[a].equals("Osu,")
                                            || split_tweet[a].equals("osu,") || split_tweet[a].equals("OSU!") || split_tweet[a].equals("Osu!")
                                            || split_tweet[a].equals("osu!") || split_tweet[a].equals("OSU?") || split_tweet[a].equals("Osu?")
                                            || split_tweet[a].equals("osu?")
                                            || split_tweet[a].equals("Buckeyes.") || split_tweet[a].equals("buckeyes.")
                                            || split_tweet[a].equals("Buckeyes!") || split_tweet[a].equals("buckeyes!")
                                            || split_tweet[a].equals("Buckeyes?") || split_tweet[a].equals("buckeyes?")
                                            || split_tweet[a].equals("Buckeyes,") || split_tweet[a].equals("buckeyes,")) {
                                        if (split_tweet[a + 1] != null && (split_tweet[a].equals("Ohio") || split_tweet[a].equals("ohio") || split_tweet[a].equals("OHIO")) && (split_tweet[a + 1].equals("State") || split_tweet[a + 1].equals("state")
                                                || split_tweet[a + 1].equals("State!") || split_tweet[a + 1].equals("state!") ||
                                                split_tweet[a + 1].equals("State.") || split_tweet[a + 1].equals("state.")
                                                || split_tweet[a + 1].equals("State?") || split_tweet[a + 1].equals("state?") ||
                                                split_tweet[a+1].equals("State,") || split_tweet[a+1].equals("state,") ||
                                                split_tweet[a+1].equals("STATE") || split_tweet[a+1].equals("STATE.") || split_tweet[a+1].equals("STATE?")
                                                || split_tweet[a+1].equals("STATE!") || split_tweet[a+1].equals("STATE,")))
                                         {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" +Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                        else if (((split_tweet[a].equals("Ohio") || split_tweet[a].equals("ohio") || split_tweet[a].equals("OHIO"))
                                                && split_tweet[a+1] != null) && (!split_tweet[a+1].equals("State") || !split_tweet[a+1].equals("state") || !split_tweet[a+1].equals("STATE"))) {
                                            continue;
                                        }
                                        else {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" + Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                    }
                                    getSupportActionBar().setTitle(" Ohio State Tweets");
                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    flayout.setBackgroundResource(R.drawable.ohiostate);
                                }
                                if (game.equals("Michigan State")) {
                                    if (split_tweet[a].equals("Michigan") || split_tweet[a].equals("michigan") || split_tweet[a].equals("MICHIGAN")
                                            || split_tweet[a].equals("MSU") || split_tweet[a].equals("Msu")
                                            || split_tweet[a].equals("msu") || split_tweet[a].equals("Spartans") || split_tweet[a].equals("spartans")
                                            || split_tweet[a].equals("MSU.") || split_tweet[a].equals("Msu.")
                                            || split_tweet[a].equals("msu.") || split_tweet[a].equals("MSU?") || split_tweet[a].equals("Msu?")
                                            || split_tweet[a].equals("msu?") || split_tweet[a].equals("MSU!") || split_tweet[a].equals("Msu!")
                                            || split_tweet[a].equals("msu!") || split_tweet[a].equals("MSU,") || split_tweet[a].equals("Msu,")
                                            || split_tweet[a].equals("msu,")
                                            || split_tweet[a].equals("Spartans.") || split_tweet[a].equals("spartans.")
                                            || split_tweet[a].equals("Spartans!") || split_tweet[a].equals("spartans!")
                                            || split_tweet[a].equals("Spartans?") || split_tweet[a].equals("spartans?")
                                            || split_tweet[a].equals("Spartans,") || split_tweet[a].equals("spartans,")) {
                                        if (split_tweet[a + 1] != null && (split_tweet[a].equals("Michigan") || split_tweet[a].equals("michigan") || split_tweet[a].equals("MICHIGAN")) && (split_tweet[a + 1].equals("State") || split_tweet[a + 1].equals("state")
                                                || split_tweet[a + 1].equals("State.") || split_tweet[a + 1].equals("state.") ||
                                                split_tweet[a + 1].equals("State!") || split_tweet[a + 1].equals("state!")
                                                || split_tweet[a + 1].equals("State?") || split_tweet[a + 1].equals("state?")
                                                || split_tweet[a + 1].equals("State,") || split_tweet[a + 1].equals("state,")
                                                || split_tweet[a+1].equals("STATE") || split_tweet[a+1].equals("STATE.") || split_tweet[a+1].equals("STATE?")
                                                || split_tweet[a+1].equals("STATE!") || split_tweet[a+1].equals("STATE,"))) {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" +Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                        else if (((split_tweet[a].equals("Michigan") || split_tweet[a].equals("michigan") || split_tweet[a].equals("MICHIGAN"))
                                                && split_tweet[a+1] != null) && (!split_tweet[a+1].equals("State") || !split_tweet[a+1].equals("state") || !split_tweet[a+1].equals("STATE"))) {
                                            continue;
                                        }
                                        else {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" + Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                    }
                                    getSupportActionBar().setTitle(" Michigan State Tweets");
                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    flayout.setBackgroundResource(R.drawable.spartan);
                                }
                                if (game.equals("Western Michigan")) {
                                    if (split_tweet[a].equals("Western") || split_tweet[a].equals("western") ||
                                            split_tweet[a].equals("WESTERN")
                                            || split_tweet[a].equals("WMU") || split_tweet[a].equals("Wmu")
                                            || split_tweet[a].equals("wmu") || split_tweet[a].equals("Broncos") || split_tweet[a].equals("broncos")
                                            || split_tweet[a].equals("WMU.") || split_tweet[a].equals("Wmu.")
                                            || split_tweet[a].equals("wmu.") || split_tweet[a].equals("WMU,") || split_tweet[a].equals("Wmu,")
                                            || split_tweet[a].equals("wmu,") || split_tweet[a].equals("WMU!") || split_tweet[a].equals("Wmu!")
                                            || split_tweet[a].equals("wmu!") || split_tweet[a].equals("WMU?") || split_tweet[a].equals("Wmu?")
                                            || split_tweet[a].equals("wmu?")
                                            || split_tweet[a].equals("Broncos.") || split_tweet[a].equals("broncos.")
                                            || split_tweet[a].equals("Broncos!") || split_tweet[a].equals("broncos!")
                                            || split_tweet[a].equals("Broncos?") || split_tweet[a].equals("broncos?")
                                            || split_tweet[a].equals("Broncos,") || split_tweet[a].equals("broncos,")) {
                                        if (split_tweet[a + 1] != null && (split_tweet[a].equals("Western") || split_tweet[a].equals("western")) && (split_tweet[a + 1].equals("Michigan") || split_tweet[a + 1].equals("michigan")
                                                || split_tweet[a+1].equals("MICHIGAN")
                                                || split_tweet[a + 1].equals("Michigan.") || split_tweet[a + 1].equals("michigan.") ||
                                                split_tweet[a + 1].equals("Michigan?") || split_tweet[a + 1].equals("michigan?")
                                                || split_tweet[a + 1].equals("Michigan!") || split_tweet[a + 1].equals("michigan!")
                                                || split_tweet[a + 1].equals("Michigan,") || split_tweet[a + 1].equals("michigan,")
                                        || split_tweet[a+1].equals("MICHIGAN") || split_tweet[a+1].equals("MICHIGAN.")
                                                || split_tweet[a+1].equals("MICHIGAN!") || split_tweet[a+1].equals("MICHIGAN?") || split_tweet[a+1].equals("MICHIGAN,"))) {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" +Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                        else if (((split_tweet[a].equals("Western") || split_tweet[a].equals("western") || split_tweet[a].equals("WESTERN"))
                                                && split_tweet[a+1] != null) && (!split_tweet[a+1].equals("Michigan") || !split_tweet[a+1].equals("michigan")
                                        || !split_tweet[a+1].equals("MICHIGAN"))) {
                                            continue;
                                        }
                                        else {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" +Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                    }
                                    getSupportActionBar().setTitle(" Western Michigan Tweets");
                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    flayout.setBackgroundResource(R.drawable.western);
                                } if (game.equals("Northern Illinois")) {
                                    if (split_tweet[a].equals("Northern") || split_tweet[a].equals("northern") || split_tweet[a].equals("NORTHERN")
                                            || split_tweet[a].equals("NIU") || split_tweet[a].equals("Niu")
                                            || split_tweet[a].equals("niu")
                                            || split_tweet[a].equals("NIU.") || split_tweet[a].equals("Niu.")
                                            || split_tweet[a].equals("niu.") || split_tweet[a].equals("NIU,") || split_tweet[a].equals("Niu,")
                                            || split_tweet[a].equals("niu,") || split_tweet[a].equals("NIU!") || split_tweet[a].equals("Niu!")
                                            || split_tweet[a].equals("niu!") || split_tweet[a].equals("NIU?") || split_tweet[a].equals("Niu?")
                                            || split_tweet[a].equals("niu?")
                                            ) {
                                        if (split_tweet[a + 1] != null && (split_tweet[a].equals("Northern") || split_tweet[a].equals("northern") || split_tweet[a].equals("NORTHERN")) && (split_tweet[a + 1].equals("Illinois") || split_tweet[a + 1].equals("illinois")
                                                || split_tweet[a + 1].equals("Illinois!") || split_tweet[a + 1].equals("illinois!") ||
                                                split_tweet[a + 1].equals("Illinois.") || split_tweet[a + 1].equals("illinois.")
                                                || split_tweet[a + 1].equals("Illinois?") || split_tweet[a + 1].equals("illinois?") ||
                                                split_tweet[a+1].equals("Illinois,") || split_tweet[a+1].equals("illinois,") ||
                                                split_tweet[a+1].equals("ILLINOIS") || split_tweet[a+1].equals("ILLINOIS.") || split_tweet[a+1].equals("ILLINOIS?")
                                                || split_tweet[a+1].equals("ILLINOIS!") || split_tweet[a+1].equals("ILLINOIS,")))
                                        {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" +Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                        else if (((split_tweet[a].equals("Northern") || split_tweet[a].equals("northern") || split_tweet[a].equals("NORTHERN"))
                                                && split_tweet[a+1] != null) && (!split_tweet[a+1].equals("Illinois") || !split_tweet[a+1].equals("illinois") || !split_tweet[a+1].equals("ILLINOIS"))) {
                                            continue;
                                        }
                                        else {
                                            tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                    Tweet.getSport() + "\n" + Tweet.getTweet());
                                            list_of_tweets.add(Tweet);
                                            break;
                                        }
                                    }
                                    getSupportActionBar().setTitle(" Northern Illinois Tweets");
                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    flayout.setBackgroundResource(R.drawable.northern);
                                } else if (game.equals("Rutgers")) {
                                    if (split_tweet[a].equals(game) || split_tweet[a].equals("rutgers") || split_tweet[a].equals("RUTGERS")
                                            || split_tweet[a].equals("Rutgers.") || split_tweet[a].equals("rutgers.")
                                            || split_tweet[a].equals("RUTGERS.") || split_tweet[a].equals("Rutgers?")
                                            || split_tweet[a].equals("rutgers?") || split_tweet[a].equals("RUTGERS?")
                                            || split_tweet[a].equals("Rutgers!")|| split_tweet[a].equals("rutgers!")
                                            || split_tweet[a].equals("RUTGERS!") || split_tweet[a].equals("Rutgers,")
                                            || split_tweet[a].equals("rutgers,") || split_tweet[a].equals("RUTGERS,")) {
                                        tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                Tweet.getSport() + "\n" +Tweet.getTweet());
                                        list_of_tweets.add(Tweet);
                                        break;
                                    }
                                    getSupportActionBar().setTitle(" Rutgers Tweets");
                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    flayout.setBackgroundResource(R.drawable.rutgers);

                                }
                                else if (game.equals("Northwestern")) {
                                    if (split_tweet[a].equals(game) || split_tweet[a].equals("northwestern") || split_tweet[a].equals("NU")
                                            || split_tweet[a].equals("Wildcats") || split_tweet[a].equals("wildcats") ||
                                            split_tweet[a].equals("Wildcats.") || split_tweet[a].equals("wildcats.") ||
                                            split_tweet[a].equals("Wildcats?") || split_tweet[a].equals("wildcats?") ||
                                            split_tweet[a].equals("Wildcats!") || split_tweet[a].equals("wildcats!") ||
                                            split_tweet[a].equals("Wildcats,") || split_tweet[a].equals("wildcats,") ||
                                            split_tweet[a].equals("WILDCATS")  || split_tweet[a].equals("WILDCATS.")
                                            || split_tweet[a].equals("WILDCATS,") || split_tweet[a].equals("WILDCATS!")
                                            || split_tweet[a].equals("WILDCATS?") || split_tweet[a].equals("NORTHWESTERN")
                                            || split_tweet[a].equals("NORTHWESTERN,") || split_tweet[a].equals("NORTHWESTERN.")
                                            || split_tweet[a].equals("NORTHWESTERN?") || split_tweet[a].equals("NORTHWESTERN!") ||
                                            split_tweet[a].equals("Northwestern.") || split_tweet[a].equals("Northwestern?")
                                            ||split_tweet[a].equals("Northwestern!") ||split_tweet[a].equals("Northwestern,")
                                            || split_tweet[a].equals("northwestern.") || split_tweet[a].equals("northwestern?")
                                            ||split_tweet[a].equals("northwestern!") ||split_tweet[a].equals("northwestern,")) {
                                        tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                Tweet.getSport() + "\n" + Tweet.getTweet());
                                        list_of_tweets.add(Tweet);
                                        break;
                                    }
                                    getSupportActionBar().setTitle(" Northwestern Tweets");
                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    flayout.setBackgroundResource(R.drawable.northwestern);

                                }
                                else if (game.equals("Washington")) {
                                    if (split_tweet[a].equals(game) || split_tweet[a].equals("washington") || split_tweet[a].equals("WU")
                                            || split_tweet[a].equals("Huskies") || split_tweet[a].equals("huskies") ||
                                            split_tweet[a].equals("Huskies.") || split_tweet[a].equals("huskies.") ||
                                            split_tweet[a].equals("Huskies?") || split_tweet[a].equals("huskies?") ||
                                            split_tweet[a].equals("Huskies!") || split_tweet[a].equals("huskies!") ||
                                            split_tweet[a].equals("Huskies,") || split_tweet[a].equals("huskies,") ||
                                            split_tweet[a].equals("HUSKIES")  || split_tweet[a].equals("HUSKIES.")
                                            || split_tweet[a].equals("HUSKIES,") || split_tweet[a].equals("HUSKIES!")
                                            || split_tweet[a].equals("HUSKIES?") || split_tweet[a].equals("WASHINGTON")
                                            || split_tweet[a].equals("WASHINGTON,") || split_tweet[a].equals("WASHINGTON.")
                                            || split_tweet[a].equals("WASHINGTON?") || split_tweet[a].equals("WASHINGTON!") ||
                                            split_tweet[a].equals("Washington.") || split_tweet[a].equals("Washington?")
                                            ||split_tweet[a].equals("Washington!") ||split_tweet[a].equals("Washington,")
                                            || split_tweet[a].equals("washington.") || split_tweet[a].equals("washington?")
                                            ||split_tweet[a].equals("washington!") ||split_tweet[a].equals("washington,")) {
                                        tweets_per_team.add(Tweet.getSender() + "\n" + Tweet.getDate_and_Time() + "\n" +
                                                Tweet.getSport() + "\n" + Tweet.getTweet());
                                        list_of_tweets.add(Tweet);
                                        break;
                                    }
                                    getSupportActionBar().setTitle(" Washington Tweets");
                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    flayout.setBackgroundResource(R.drawable.washington);

                                }

                            }
                        }
                    }
                }
                if(tweets_per_team.size() == 0){
                    text.setEnabled(true);
                    text.setText("No tweets exist");
                }
                Collections.reverse(tweets_per_team);
                Collections.reverse(list_of_tweets);
                view.setAdapter(adapter);
                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String username = list_of_tweets.get(position).getSender();
                        String tweet = list_of_tweets.get(position).getTweet();
                        String sport = list_of_tweets.get(position).getSport();
                        String Date_and_Time = list_of_tweets.get(position).getDate_and_Time();
                        Intent intent = new Intent(getApplicationContext(), Indiv_Tweet.class);
                        intent.putExtra("sender", username);
                        intent.putExtra("tweet", tweet);
                        intent.putExtra("sport",sport);
                        intent.putExtra("Date_and_Time",Date_and_Time);
                        startActivity(intent);
                        //Log.i("User", tweets_list_for_reference.get(position).getSender());
                        //Log.i("Tweet", tweets_list_for_reference.get(position).getTweet());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
