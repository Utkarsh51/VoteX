package com.android.tony.votex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowResultActivity extends AppCompatActivity {

    TextView consTextView,doeTextView,posTextView;
    Intent intent;
    DatabaseReference databaseReference;
    ArrayList<String> partyStringArrayList,resultArrayList;
    String dates;
    ListView listView;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        intent = getIntent();
        consTextView = findViewById(R.id.showresultconstextView);
        doeTextView = findViewById(R.id.showresultdoetextView);
        posTextView = findViewById(R.id.showresultpostextView);

        consTextView.setText(intent.getStringExtra("Cons"));
        doeTextView.setText(intent.getStringExtra("DOE"));
        posTextView.setText(intent.getStringExtra("Pos"));

        partyStringArrayList = new ArrayList<>();
        listView = findViewById(R.id.showresultlistview);
        resultArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.result_layout,R.id.resulttextView,resultArrayList);
        listView.setAdapter(arrayAdapter);
        dates = posTextView.getText().toString().replace("/","");
        Log.i("Dates",consTextView.getText().toString()+doeTextView.getText().toString()+dates);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child(consTextView.getText().toString()+doeTextView.getText().toString()+dates);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpParty();

    }

    void setUpParty()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("Dates","setUpParty");
                partyStringArrayList.clear();
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    VotingClass votingClass = child.getValue(VotingClass.class);
                    partyStringArrayList.add(votingClass.getGivenTo());
                }
                calculateResult();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    void calculateResult()
    {
        Map<String,Integer> stringIntegerMap = new HashMap<>();
        Log.i("Dates","cal");
        resultArrayList.clear();
        for(String party:partyStringArrayList)
        {
            Integer integer = stringIntegerMap.get(party);
            stringIntegerMap.put(party,(integer==null)?1:integer+1);
        }

        for(Map.Entry<String,Integer> val:stringIntegerMap.entrySet())
        {
            resultArrayList.add(val.getKey() + " got " + val.getValue() + " votes");
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
