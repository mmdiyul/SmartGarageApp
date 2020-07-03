package com.sohyun.smartgarage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PintuActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ConstraintLayout on, off;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pintu);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);
        status = findViewById(R.id.status);

        mDatabase.child("garageDoor").child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean statuses = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                if (statuses == true) {
                    status.setText("Pintu Terbuka");
                } else {
                    status.setText("Pintu Tertutup");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                mDatabase.child("garageDoor").child("status").setValue(true);
                status.setText("Pintu Terbuka");
                mDatabase.child("garageDoor").child("lastAccessed").setValue("" + dateFormat.format(date));

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String username = currentUser.getEmail().split("@")[0];

                mDatabase.child("user").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    String user;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.child("username").getValue().toString();
                        mDatabase.child("garageDoor").child("lastUser").setValue(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                mDatabase.child("garageDoor").child("status").setValue(false);
                status.setText("Pintu Tertutup");
                mDatabase.child("garageDoor").child("lastAccessed").setValue("" + dateFormat.format(date));

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String username = currentUser.getEmail().split("@")[0];

                mDatabase.child("user").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    String user;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.child("username").getValue().toString();
                        mDatabase.child("garageDoor").child("lastUser").setValue(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }
}