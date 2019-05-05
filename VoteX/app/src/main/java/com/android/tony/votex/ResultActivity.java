package com.android.tony.votex;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    DatabaseReference upcomingdatabaseReference;
    RecyclerView recyclerView;
    ElectionRecyclerAdapter electionRecyclerAdapter;
    List<ElectionDatabase> electionDatabaseList;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerView = findViewById(R.id.resultrecyclerView);
        electionDatabaseList = new ArrayList<>();
        electionRecyclerAdapter = new ElectionRecyclerAdapter(electionDatabaseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(electionRecyclerAdapter);
        progressBar = findViewById(R.id.resultprogressBar);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ElectionDatabase electionDatabase = electionDatabaseList.get(position);
                Toast.makeText(getApplicationContext(),electionDatabase.getConsitutecy(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResultActivity.this,ShowResultActivity.class);
                intent.putExtra("Cons",electionDatabase.getConsitutecy());
                intent.putExtra("DOE",electionDatabase.getDateOfElection());
                intent.putExtra("Pos",electionDatabase.getPosition());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
                       if(d2.before(d1) || d2.equals(d1))
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
