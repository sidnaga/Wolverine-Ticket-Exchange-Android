package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private Button SendData;
    //private Firebase mref;
    EditText email;
    EditText password;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //sign in as sidnaga@umich.edu on emulator and sid.nagamangala2431@gmail.com on phone
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        //show_uploading_images();
        user_ref = FirebaseDatabase.getInstance().getReference("Users");
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.wolverine);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        if(user != null){
            show_ongoing_users();
        }


    }
    public void sign_up(View view){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification();
                            //Toast.makeText(getApplicationContext(),"Email Verification Sent",Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("success", "email sent");
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                String Email = email.getText().toString();
                                String id = user_ref.push().getKey();
                                Users user = new Users(Email, id);
                                user_ref.child(id).setValue(user);
                                Toast.makeText(getApplicationContext(), "Sign Up Success.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            //Log.i("failure", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Sign up failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void log_in(View view){
        Toast.makeText(getApplicationContext(),"Logging in...",Toast.LENGTH_LONG).show();
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("message", "signInWithEmail:success");
                                //FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Authentication Success.", Toast.LENGTH_SHORT).show();
                                show_ongoing_users();

                                //updateUI(user);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.i("message", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }
    public void show_ongoing_users(){
        Intent intent = new Intent(getApplicationContext(),OngoingConvosUsers.class);
        startActivity(intent);
    }
    public void sign_out(){
        FirebaseAuth.getInstance().signOut();
    }
}
