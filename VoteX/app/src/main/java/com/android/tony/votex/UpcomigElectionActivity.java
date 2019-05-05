package com.android.tony.votex;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpcomigElectionActivity extends AppCompatActivity {

    DatabaseReference upcomingdatabaseReference;
    RecyclerView recyclerView;
    ElectionRecyclerAdapter electionRecyclerAdapter;
    List<ElectionDatabase> electionDatabaseList;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomig_election);

        recyclerView = findViewById(R.id.upcominelectionrecyclerView);
        electionDatabaseList = new ArrayList<>();
        electionRecyclerAdapter = new ElectionRecyclerAdapter(electionDatabaseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(electionRecyclerAdapter);
        progressBar = findViewById(R.id.upcominelectionprogressBar);

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
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    ElectionDatabase electionDatabase =  child.getValue(ElectionDatabase.class);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDateString = simpleDateFormat.format(Calendar.getInstance().getTime()).trim();
                    String electionDateString = electionDatabase.getDateOfElection();
                    try
                    {
                        Date d1 = simpleDateFormat.parse(currentDateString);
                        Date d2 = simpleDateFormat.parse(electionDateString);
                        if(d1.before(d2) || d1.equals(d2))
                        {
                            electionDatabaseList.add(new ElectionDatabase(electionDatabase.getConsitutecy(), electionDatabase.getPosition(), electionDatabase.getDateOfElection()));
                            electionRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }

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
