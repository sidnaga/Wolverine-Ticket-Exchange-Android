package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class OngoingConvosUsers extends AppCompatActivity {
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
    String sport = "";
    String amount;
    int optionClicked = -1;
    //Date date;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.tweet){
            /*
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LinearLayout layout = new LinearLayout(this);
            builder.setTitle("Send a Tweet");
            final EditText tweetEditText = new EditText(this);
            tweetEditText.setHint("tweet");
            layout.addView(tweetEditText);
            builder.setView(layout);
            final String[] sports = {"Basketball","Hockey","Football"};
            builder.setSingleChoiceItems(sports, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    optionClicked = which;
                   sport = sports[which];
                }
            });
            */
            /*
            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(tweetEditText.getText().toString().isEmpty() || sport.isEmpty()){
                                Toast.makeText(getApplicationContext(), "Please enter a valid tweet",
                                        Toast.LENGTH_SHORT).show();
                                        return;
                            }
                            else {
                                String tweet = tweetEditText.getText().toString();
                                String sender = auth.getCurrentUser().getEmail().toString();
                                String id = tweets_ref.push().getKey();
                                String mydate = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
                                Tweet Tweet = new Tweet(sender, id, tweet, mydate, sport);
                                Tweet.setDate_and_Time(mydate);
                                tweets_ref.child(id).setValue(Tweet);
                            }
                        }
                    });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            */
            final String[] sports = {"Basketball","Hockey","Football"};
            final EditText tweetEditText = new EditText(this);
            tweetEditText.setHint("tweet");
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(sports, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            optionClicked = which;
                            sport = sports[which];
                        }
                    })
                    .setView(tweetEditText)
                    .setTitle("Send a Tweet")
                    .setCancelable(false)
                    .setPositiveButton("Send", null) //Set to null. We override the onclick
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sport = "";
                        }
                    })
                    .create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialogInterface) {

                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // Check if your condition is met, Dismiss once everything is OK.
                            if(tweetEditText.getText().toString().isEmpty() || (!sport.equals("Basketball")
                            && !sport.equals("Hockey") && !sport.equals("Football"))){
                                Toast.makeText(getApplicationContext(), "Please enter a valid tweet",
                                        Toast.LENGTH_SHORT).show();

                            }
                            else {
                                String tweet = tweetEditText.getText().toString();
                                String sender = auth.getCurrentUser().getEmail().toString();
                                String id = tweets_ref.push().getKey();
                                String mydate = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
                                Tweet Tweet = new Tweet(sender, id, tweet, mydate, sport);
                                Tweet.setDate_and_Time(mydate);
                                tweets_ref.child(id).setValue(Tweet);
                                sport = "";
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });
            dialog.show();
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_convos_users);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.umich_wte_logo_resized);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        auth = FirebaseAuth.getInstance();
        view = findViewById(R.id.ListView);
        notifications_list = new ArrayList<>();
        list_of_users = new ArrayList();
        sharedPreferences = this.getSharedPreferences(SHARED_PREFS, 0);
        user_storage = this.getSharedPreferences("user_storage",Context.MODE_PRIVATE);
        user_storage.edit().putString("User",auth.getCurrentUser().getEmail()).apply();
        final SharedPreferences.Editor editor = sharedPreferences.edit();;
        //System.out.println(sharedPreferences.getInt("list size",0));
        //notifications_rep_list_size = 6;
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_users);
        set_of_ongoing_users = new HashSet<>();
        replies_ref = FirebaseDatabase.getInstance().getReference("Replies");
        tweets_ref = FirebaseDatabase.getInstance().getReference("Tweets");
        replies_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(),"No Ongoing Conversations",Toast.LENGTH_LONG).show();
                    editor.remove("list size").apply();
                    editor.putInt("list size",notifications_list.size());
                    editor.apply();
                    System.out.println(sharedPreferences.getInt("list size",0));
                    view.setAdapter(adapter);
                }
                if(dataSnapshot.exists()) {
                    set_of_ongoing_users.clear();
                    list_of_users.clear();
                    notifications_list.clear();
                    for (DataSnapshot users : dataSnapshot.getChildren()) {
                        Reply reply = users.getValue(Reply.class);
                        //Log.i("person receiving",reply.getPerson_sending());
                        Log.i("person receiving",reply.getPerson_receiving());
                        if ((reply.getPerson_receiving().equals(auth.getCurrentUser().getEmail())) || (reply.getPerson_sending().equals(auth.getCurrentUser().getEmail()))) {
                            if (reply.getPerson_receiving().equals(auth.getCurrentUser().getEmail())) {
                                set_of_ongoing_users.add(reply.getPerson_sending());
                                notifications_list.add(reply);
                            } else if (reply.getPerson_sending().equals(auth.getCurrentUser().getEmail())) {
                                Log.i("person receiving",reply.getPerson_sending());
                                set_of_ongoing_users.add(reply.getPerson_receiving());
                            }
                        }
                    }

                    if(notifications_list.size() != sharedPreferences.getInt("list size",0)){
                        System.out.println(notifications_list.size());
                        System.out.println(sharedPreferences.getInt("list size",0));
                        createNotificationChannel();
                        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        intent.putExtra("Title","WTE message from " + notifications_list.get(notifications_list.size()-1).getPerson_sending());
                        intent.putExtra("Replier",notifications_list.get(notifications_list.size()-1).getPerson_sending());
                        intent.putExtra("Reply", notifications_list.get(notifications_list.size()-1).getReply());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, '5', pendingIntent);
                        //System.out.println(notifications_list.size());
                        editor.remove("list size").apply();
                        editor.putInt("list size",notifications_list.size());
                        editor.apply();
                        System.out.println(notifications_list.get(notifications_list.size()-1).getPerson_sending());
                        System.out.println(notifications_list.get(notifications_list.size()-1).getReply());
                        System.out.println(notifications_list.size());
                        System.out.println(notifications_list.size());
                        System.out.println(sharedPreferences.getInt("list size",0));
                        //new comment for git
                    }

                    Log.i("current user",auth.getCurrentUser().getEmail());
                    list_of_users.addAll(set_of_ongoing_users);
                    Collections.reverse(list_of_users);
                    view.setAdapter(adapter);
                    view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(),Indiv_Thread.class);
                            intent.putExtra("other_user", list_of_users.get(position));
                            startActivity(intent);
                            //Toast.makeText(getApplicationContext(),list_of_users.get(position),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void createNotificationChannel() { //notification channel for alarm receiver
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "channel for messages";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
