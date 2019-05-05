package com.android.tony.votex;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VotingActivity extends AppCompatActivity {

    Intent intent;
    TextView consTextView,positionTextVew,DOETextView;
    ListView candidateListView;
    List<CandidateDatabase> candidateDatabaseList;
    CandidateVotingListAdapter candidateVotingListAdapter;
    DatabaseReference candidateDatabaseReference,votingDatabaseReference;
    ProgressBar progressBar;
    String userAdhaar;
    String[] date;
    ArrayList<String> userVotedStringArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        intent = getIntent();
        consTextView = findViewById(R.id.VotingConsitutencytextview);
        positionTextVew = findViewById(R.id.VotingPositiontextView);
        DOETextView = findViewById(R.id.VotingDateOfElectionTextview);
        progressBar = findViewById(R.id.VotingprogressBar);
        consTextView.setText(intent.getStringExtra("Consitutecy"));
        positionTextVew.setText(intent.getStringExtra("Position"));
        DOETextView.setText(intent.getStringExtra("DateOfElection"));
        userAdhaar = intent.getStringExtra("UserAdhaar");
        userVotedStringArrayList = new ArrayList<>();

        date = DOETextView.getText().toString().split("/");
        votingDatabaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child(consTextView.getText().toString()+positionTextVew.getText().toString()+date[0]+date[1]+date[2]);
        votingDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userVotedStringArrayList.clear();
                for (DataSnapshot child:dataSnapshot.getChildren())
                {
                    userVotedStringArrayList.add(child.getKey());
                    Log.i("Users",child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        candidateDatabaseList = new ArrayList<>();
        candidateListView = findViewById(R.id.VotingListView);
        candidateVotingListAdapter = new CandidateVotingListAdapter(this,candidateDatabaseList);
        candidateListView.setAdapter(candidateVotingListAdapter);
        candidateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(VotingActivity.this).setIcon(R.mipmap.ic_launcher_round).setTitle("VoteX").setMessage("Are you sure! You want to Vote " + candidateDatabaseList.get(position).getCandidateName() + " from " + candidateDatabaseList.get(position).getCandidateParty())
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                votingDatabaseReference.child(userAdhaar).setValue(new VotingClass(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()).trim(),candidateDatabaseList.get(position).getCandidateParty(),consTextView.getText().toString(),positionTextVew.getText().toString()));
                                VotingActivity.this.finish();
                            }
                        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        candidateDatabaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX/Candidate");
        candidateDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                candidateDatabaseList.clear();
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    CandidateDatabase candidateDatabase = child.getValue(CandidateDatabase.class);
                    if(candidateDatabase.getCandidateConsitunecy().equalsIgnoreCase(consTextView.getText().toString()))
                    {
                        if(!userVotedStringArrayList.contains(userAdhaar))
                        {
                            candidateDatabaseList.add(new CandidateDatabase(candidateDatabase.getCandidateAdhaarNumber(),candidateDatabase.getCandidateVoterId(),candidateDatabase.getCandidateName(),candidateDatabase.getCandidateParty(),candidateDatabase.getCandidateConsitunecy(),candidateDatabase.getCadidateDateOfBirth(),candidateDatabase.getCandidatePosition()));
                        }
                    }

                    candidateVotingListAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
