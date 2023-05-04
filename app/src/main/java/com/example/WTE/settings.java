package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class settings extends AppCompatActivity {
    Set<String> set_of_ongoing_users;
    DatabaseReference replies_ref;
    DatabaseReference tweets_ref;
    FirebaseAuth auth;
    ListView view;
    List<String> list_of_users;
    List<Reply> notifications_list;
    Integer notifications_rep_list_size;
    SharedPreferences sharedPreferences;
    SharedPreferences user_storage;
    public static final String SHARED_PREFS = "com.example.snapchatclone";
    String sport;
    //
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
                    //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
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
        else if(item.getItemId() == R.id.games){
            Intent intent = new Intent(getApplicationContext(), sports.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(getApplicationContext(), settings.class);
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
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.umich_wte_logo_resized);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        auth = FirebaseAuth.getInstance();
        tweets_ref = FirebaseDatabase.getInstance().getReference("Tweets");
    }
    public void startService(View view){
        Button button = findViewById(R.id.notifon);
        Intent serviceIntent = new Intent(getApplicationContext(),notservice.class);
        startService(serviceIntent);
        /*
        replies_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Rep : dataSnapshot.getChildren()) {
                    Reply reply = Rep.getValue(Reply.class);
                    if ((reply.getPerson_receiving().equals(user_storage.getString("User", " ")) || (reply.getPerson_sending().equals(user_storage.getString("User", " "))))) {
                        if (reply.getPerson_receiving().equals(user_storage.getString("User", " "))) {
                            set_of_ongoing_users.add(reply.getPerson_sending());
                            notifications_list.add(reply);
                        } else if (reply.getPerson_sending().equals(user_storage.getString("User", " "))) {
                            Log.i("person receiving", reply.getPerson_sending());
                            set_of_ongoing_users.add(reply.getPerson_receiving());
                        }
                    }
                }
                if(notifications_list.size() != sharedPreferences.getInt("list size",0)){
                    serviceIntent.putExtra("Title","WTE message from " + notifications_list.get(notifications_list.size()-1).getPerson_sending());
                    serviceIntent.putExtra("Replier",notifications_list.get(notifications_list.size()-1).getPerson_sending());
                    serviceIntent.putExtra("Reply", notifications_list.get(notifications_list.size()-1).getReply());
                    startService(serviceIntent);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         */

    }
    public void stop_service(View view){
        Intent serviceIntent = new Intent(getApplicationContext(),notservice.class);
        stopService(serviceIntent);
    }
}
