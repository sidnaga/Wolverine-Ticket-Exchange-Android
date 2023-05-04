package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class List_of_Games_football extends AppCompatActivity {
    ListView view;
    List<String> games;
    List<Tweet> tweets_list_for_reference;
    //String mydate;
    DatabaseReference tweets_ref;
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
        else if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(getApplicationContext(), settings.class);
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
        setContentView(R.layout.activity_list_of__games);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.umich_wte_logo_resized);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        view = findViewById(R.id.games);
        tweets_ref = FirebaseDatabase.getInstance().getReference("Tweets");
        games = new ArrayList();
        games.add("Western Michigan");
        games.add("Washington");
        games.add("Northern Illinois");
        games.add("Rutgers");
        games.add("Michigan State");
        games.add("Northwestern");
        games.add("Ohio State");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,games){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(20);

                // Generate ListView Item using TextView
                return view;
            }
        };
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Categorized_games_football_tweets.class);
                String game = games.get(position);
                intent.putExtra("game",game);
                startActivity(intent);
            }
        });
    }
}
