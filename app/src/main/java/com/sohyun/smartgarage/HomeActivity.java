package com.sohyun.smartgarage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button logoutButton;
    private TextView nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        logoutButton = findViewById(R.id.btnuser);
        nama = findViewById(R.id.nama);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String username = currentUser.getEmail().split("@")[0];
        mDatabase.child("user").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama.setText("Hai, " + dataSnapshot.child("username").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() == null) {
                            loginActivity();
                        }
                    }
                });
            }
        });
    }

    public void toLampu(View view) {
        Intent intent = new Intent(this, LampuActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void toPintu(View view) {
        Intent intent = new Intent(this, PintuActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void toParkir(View view) {
        Intent intent = new Intent(this, ParkirActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void toUser(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void loginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}