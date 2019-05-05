package com.android.tony.votex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VoteActivity extends AppCompatActivity {

    DatabaseReference upcomingdatabaseReference, userDatabaseReference;
    ListView voteListView;
    VoteListAdapter voteListAdapter;
    List<ElectionDatabase> electionDatabaseList;
    String userConsitutency,userAdhaar,upcomingElectionDate;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);


        voteListView = findViewById(R.id.votelistview);
        electionDatabaseList = new ArrayList<>();
        voteListAdapter = new VoteListAdapter(this, electionDatabaseList);
        voteListView.setAdapter(voteListAdapter);
        progressBar = findViewById(R.id.voteprogressBar);

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX/Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDatabase userDatabase = dataSnapshot.getValue(UserDatabase.class);
                userConsitutency = userDatabase.getUserConstitunecy();
                userAdhaar = userDatabase.getUserAdhaarNumber();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(getApplicationContext(), userConsitutency, Toast.LENGTH_SHORT).show();

        voteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), electionDatabaseList.get(position).getConsitutecy() + " " + electionDatabaseList.get(position).getDateOfElection() + " " + electionDatabaseList.get(position).getPosition(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(VoteActivity.this,VotingActivity.class);
                intent.putExtra("Consitutecy", electionDatabaseList.get(position).getConsitutecy() );
                intent.putExtra("DateOfElection", electionDatabaseList.get(position).getDateOfElection());
                intent.putExtra("Position", electionDatabaseList.get(position).getPosition());
                intent.putExtra("UserAdhaar",userAdhaar);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        upcomingdatabaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Upcomingelection");
        upcomingdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                electionDatabaseList.clear();
                progressBar.setVisibility(View.VISIBLE);
                String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()).trim();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ElectionDatabase electionDatabase = child.getValue(ElectionDatabase.class);

                    if (userConsitutency.equalsIgnoreCase(electionDatabase.getConsitutecy()) && electionDatabase.getDateOfElection().equals(currentDate))
                    {
                        electionDatabaseList.add(new ElectionDatabase(electionDatabase.getConsitutecy(), electionDatabase.getDateOfElection() , electionDatabase.getPosition()));
                        voteListAdapter.notifyDataSetChanged();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
