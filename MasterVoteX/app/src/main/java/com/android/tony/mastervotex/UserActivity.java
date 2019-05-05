package com.android.tony.mastervotex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView userRecyclerView;
    UserRecyclerAdapter  userRecyclerAdapter;
    List<UserDatabase> userDatabaseList;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //progress bar
        progressBar = findViewById(R.id.userprogressBar);

        //recycler view
        userDatabaseList = new ArrayList<>();
        userRecyclerAdapter = new UserRecyclerAdapter(userDatabaseList);
        userRecyclerView = findViewById(R.id.userrecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        userRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        userRecyclerView.setAdapter(userRecyclerAdapter);

        // fab
        floatingActionButton = findViewById(R.id.userfloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this,AddUserActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDatabaseList.clear();
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    userDatabaseList.add(new UserDatabase(child.child("userAdhaarNumber").getValue(String.class),child.child("userName").getValue(String.class),child.child("userVoterId").getValue(String.class)));
                    userRecyclerAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
