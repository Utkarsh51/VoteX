package com.android.tony.mastervotex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;


public class AddCandidateActivity extends AppCompatActivity {

    boolean imageFlag = true; // true:default false:custom
    ArrayList<CandidateDatabase> adhaarArrayList;
    RadioGroup candidateRadioGroup;
    RadioButton candidateRadioButton;
    ImageView candidateImage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    EditText candidateNameEditText,candidateConsitutencyEditText,candidatePartyEditText,candidateDateOfBirthEditText,candidateAdhaarEditText,candidateVoterIdEditText;
    Button candidateSumbitButton;
    ProgressBar progressBar;
    Uri imagePath;
    String candidateName,candidateConstitutency,candidateParty ,candidateDateOfBirth,candidateAdhaar,candidateVoterId,candidatePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

        adhaarArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Candidate");
        storageReference = FirebaseStorage.getInstance().getReference();

        progressBar = findViewById(R.id.candidateprogressBar);
        candidateRadioGroup = findViewById(R.id.candidateradioGroup);
        candidateImage = findViewById(R.id.candidateimageView);
        candidateNameEditText = findViewById(R.id.candidateNameEditText);
        candidateConsitutencyEditText = findViewById(R.id.candidateConsitutencyEditText);
        candidatePartyEditText = findViewById(R.id.candidatePartyEditText);
        candidateDateOfBirthEditText = findViewById(R.id.candidateDateOfBirthEditText);
        candidateSumbitButton = findViewById(R.id.candiatesumbitbutton);
        candidateAdhaarEditText = findViewById(R.id.candidateAdhaarnumberEditext);
        candidateVoterIdEditText = findViewById(R.id.candidateVoterId);

        setUpAdhaar();
        candidateSumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candidateSumbitButton.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                candidateName = candidateNameEditText.getText().toString().trim();
                candidateConstitutency = candidateConsitutencyEditText.getText().toString().trim();
                candidateParty = candidatePartyEditText.getText().toString().trim();
                candidateDateOfBirth = candidateDateOfBirthEditText.getText().toString().trim();
                candidateAdhaar = candidateAdhaarEditText.getText().toString().trim();
                candidateVoterId = candidateVoterIdEditText.getText().toString().trim();
                candidateRadioButton = findViewById(candidateRadioGroup.getCheckedRadioButtonId());
                candidatePosition = candidateRadioButton.getText().toString();

                if(candidateAdhaar.isEmpty() || candidateVoterId.isEmpty() || candidateName.isEmpty() || candidateConstitutency.isEmpty() || candidateDateOfBirth.isEmpty() || candidateParty.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
                    candidateSumbitButton.setClickable(true);
                    progressBar.setVisibility(View.GONE);
                }
                else if(candidateAdhaar.length()!=12)
                {
                    candidateAdhaarEditText.setError("Check your Adhaar Number.");
                }
                else {

                    if(validateCandidate(candidateAdhaar,candidateVoterId))
                        createCandidate();
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Adhaar Number voter id already Exists",Toast.LENGTH_SHORT).show();
                        candidateSumbitButton.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    void setUpAdhaar()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    adhaarArrayList.add(new CandidateDatabase(child.child("candidateAdhaarNumber").getValue(String.class),child.child("candidateVoterId").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    boolean validateCandidate(String candidateAdhaar,String candidateVoterId)
    {
        if(adhaarArrayList.size()==0)
            return true;
        else
        {
            for(CandidateDatabase child:adhaarArrayList)
            {
                Log.i("UserAct1",child.getCandidateAdhaarNumber() + " " + child.getCandidateVoterId());
                if(child.getCandidateAdhaarNumber().equals(candidateAdhaar) || child.getCandidateVoterId().equals(candidateVoterId))
                    return false;
            }
        }
        return true;
    }

    void createCandidate()
    {
        CandidateDatabase candidateDatabase = new CandidateDatabase(candidateAdhaar,candidateVoterId,candidateName,candidateParty,candidateConstitutency,candidateDateOfBirth,candidatePosition);
        databaseReference.child(candidateDatabase.getCandidateAdhaarNumber()).setValue(candidateDatabase);

        // storing default profile on cloud storage and sign out
        if(imageFlag) {
            imagePath = Uri.parse("android.resource://" + getPackageName() + "/drawable/profilepic");
        }

        storageReference.child("Candidate").child(candidateDatabase.getCandidateAdhaarNumber()).child("Images").child("ProfilePic").putFile(imagePath);
        // clearing edittext
        candidateNameEditText.setText("");
        candidateConsitutencyEditText.setText("");
        candidatePartyEditText.setText("");
        candidateDateOfBirthEditText.setText("");
        candidateAdhaarEditText.setText("");
        candidateVoterIdEditText.setText("");
        candidateImage.setImageURI(Uri.parse("android.resource://" + getPackageName() + "/drawable/profilepic"));
        imageFlag = true;
        Toast.makeText(getApplicationContext(),"DataUploaded Successfully",Toast.LENGTH_SHORT).show();

        //hiding progress bar
        progressBar.setVisibility(View.GONE);
    }

    void imageUpdate(View v)
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        else
        {
            //Toast.makeText(getActivity(),"Good To go",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 &&  resultCode == RESULT_OK && data != null)
        {
            progressBar.setVisibility(View.VISIBLE);
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagePath);
                candidateImage.setImageBitmap(bitmap);
                imageFlag = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
