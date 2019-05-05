package com.android.tony.mastervotex;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUpcomingElectionActivity extends AppCompatActivity {

    EditText constitencyEditText,dateofElectionEditText;
    Button sumbitButton;
    RadioGroup positionRadioGroup;
    ProgressBar progressBar;
    RadioButton positionRadioButton;
    String constituencySting,dateOfElectionString,positionString;
    DatabaseReference databaseReference;
    ArrayList<ElectionDatabase> electionDatabasesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upcoming_election);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Upcomingelection");
        electionDatabasesList = new ArrayList<>();
        constitencyEditText = findViewById(R.id.consituencyupcomingelectioneditText);
        dateofElectionEditText = findViewById(R.id.dateofelectionupcomingelectioneditText);
        positionRadioGroup = findViewById(R.id.positionradioGroup);
        progressBar = findViewById(R.id.addupcomingelectionprogressBar);
        sumbitButton = findViewById(R.id.upcomingelectionsumbitbutton);
        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constituencySting = constitencyEditText.getText().toString();
                dateOfElectionString = dateofElectionEditText.getText().toString();
                if(constituencySting.isEmpty() && dateOfElectionString.isEmpty())
                    Toast.makeText(getApplicationContext(),"All Fields are required",Toast.LENGTH_SHORT).show();
                else
                {
                    positionRadioButton = findViewById(positionRadioGroup.getCheckedRadioButtonId());
                    positionString = positionRadioButton.getText().toString();
                    if(validate(constituencySting,dateOfElectionString,positionString))
                    {
                        databaseReference.push().setValue(new ElectionDatabase(constituencySting,dateOfElectionString,positionString));
                        Toast.makeText(getApplicationContext(),"Election Uploaded",Toast.LENGTH_SHORT).show();
                        constitencyEditText.setText(""); dateofElectionEditText.setText("");
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Election Already Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpUpComingElection();
    }

    void setUpUpComingElection()
    {
        progressBar.setVisibility(View.VISIBLE);
        electionDatabasesList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    electionDatabasesList.add(new ElectionDatabase(child.child("consitutecy").getValue(String.class),child.child("dateOfElection").getValue(String.class),child.child("position").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.GONE);
    }


    boolean validate(String mconstituencySting,String mdateOfElectionString,String mpositionString)
    {
        if(electionDatabasesList.size()==0)
            return true;
        else
        {
            for(ElectionDatabase electionDatabase : electionDatabasesList)
            {
                if(electionDatabase.getConsitutecy().equals(mconstituencySting) && electionDatabase.getDateOfElection().equals(mdateOfElectionString) && electionDatabase.getPosition().equals(mpositionString))
                    return false;
            }
        }
        return true;
    }
}
