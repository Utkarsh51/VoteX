package com.android.tony.mastervotex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

public class ElectionActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FloatingActionButton upcomingElectionFloatingActionButton;
    RecyclerView recyclerView;
    ElectionRecyclerAdapter electionRecyclerAdapter;
    List<ElectionDatabase> electionDatabaseList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_election);

        recyclerView = findViewById(R.id.upcominelectionrecyclerView);
        electionDatabaseList = new ArrayList<>();
        electionRecyclerAdapter = new ElectionRecyclerAdapter(electionDatabaseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(electionRecyclerAdapter);
        progressBar = findViewById(R.id.upcominelectionprogressBar);


        upcomingElectionFloatingActionButton = findViewById(R.id.upcomingelectionfloatingActionButton);
        upcomingElectionFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElectionActivity.this,AddUpcomingElectionActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Upcomingelection");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                electionDatabaseList.clear();
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    electionDatabaseList.add(new ElectionDatabase(child.child("consitutecy").getValue(String.class),child.child("dateOfElection").getValue(String.class),child.child("position").getValue(String.class)));
                    electionRecyclerAdapter.notifyDataSetChanged();
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
