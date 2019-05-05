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

public class CandidateActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<CandidateDatabase> candidateDatabaseList;
    CandidateRecyclerAdapter candidateRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        floatingActionButton = findViewById(R.id.candidatefloatingActionButton);
        recyclerView = findViewById(R.id.candidaterecyclerView);
        progressBar = findViewById(R.id.candidateprogressBar);

        candidateDatabaseList = new ArrayList<>();
        candidateRecyclerAdapter = new CandidateRecyclerAdapter(candidateDatabaseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(candidateRecyclerAdapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddCandidateActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Candidate");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                candidateDatabaseList.clear();
                //candidateDatabaseList.add(new CandidateDatabase("Test","Test","Test"));
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    candidateDatabaseList.add(new CandidateDatabase(child.child("candidateAdhaarNumber").getValue(String.class),child.child("candidateVoterId").getValue(String.class),child.child("candidateName").getValue(String.class)));
                    candidateRecyclerAdapter.notifyDataSetChanged();
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
