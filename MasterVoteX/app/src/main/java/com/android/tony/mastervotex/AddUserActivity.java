package com.android.tony.mastervotex;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AddUserActivity extends AppCompatActivity {

    ArrayList<UserDatabase> adhaarArrayList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    EditText userAdhaarNumberEditText,userNameEditText,userVoterIdEditText,userEmailEditText,userDateOfBirthEditText,userPhoneNumberEditText,userCityEditText,userStateEditText,userConstitunecyEditText;
    Button signUpButton;
    ProgressBar signupProgressbar;
    String userAdhaarNumberString,userNameString,userVoterIdString,userEmailString,userDateOfBirthString,userPhoneNumberString,userCityString,userStateString,userConstitunecyString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        firebaseAuth =  FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        adhaarArrayList = new ArrayList<>();

        userAdhaarNumberEditText = findViewById(R.id.adhaarnumbereditText);
        userNameEditText = findViewById(R.id.nameeditText);
        userVoterIdEditText = findViewById(R.id.voterideditText);
        userEmailEditText = findViewById(R.id.signupemaileditText);
        userDateOfBirthEditText = findViewById(R.id.dobeditText);
        userPhoneNumberEditText = findViewById(R.id.phonenumbereditText);
        userCityEditText = findViewById(R.id.cityeditText);
        userStateEditText = findViewById(R.id.stateeditText);
        signUpButton = findViewById(R.id.signupbutton);
        signupProgressbar = findViewById(R.id.signupprogressBar);
        userConstitunecyEditText = findViewById(R.id.userConsitutencyEditText);

        setUpAdhaar();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupProgressbar.setVisibility(View.VISIBLE);

                userAdhaarNumberString = userAdhaarNumberEditText.getText().toString().trim();
                userNameString = userNameEditText.getText().toString().trim();
                userVoterIdString = userVoterIdEditText.getText().toString().trim();
                userEmailString = userEmailEditText.getText().toString().trim();
                userDateOfBirthString = userDateOfBirthEditText.getText().toString().trim();
                userPhoneNumberString = userPhoneNumberEditText.getText().toString().trim();
                userCityString = userCityEditText.getText().toString().trim();
                userStateString= userStateEditText.getText().toString().trim();
                userConstitunecyString = userConstitunecyEditText.getText().toString().trim();

                if(userAdhaarNumberString.isEmpty() && userNameString.isEmpty() && userVoterIdString.isEmpty() && userEmailString.isEmpty() && userDateOfBirthString.isEmpty() && userPhoneNumberString.isEmpty() && userCityString.isEmpty() && userStateString.isEmpty() && userConstitunecyString.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
                    signupProgressbar.setVisibility(View.GONE);
                }
                else if(userAdhaarNumberString.length()!=12)
                {
                    userAdhaarNumberEditText.setError("Enter Correct Adhaar Number");
                    signupProgressbar.setVisibility(View.GONE);
                }
                else
                {
                   if(validateUser(userAdhaarNumberString,userVoterIdString))
                       createUser();
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Adhaar Number voter id already Exists",Toast.LENGTH_SHORT).show();
                       signupProgressbar.setVisibility(View.GONE);
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
                    adhaarArrayList.add(new UserDatabase(child.child("userAdhaarNumber").getValue(String.class),child.child("userVoterId").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    // to check user alredy exist or not
    boolean validateUser(String userAdhaarNumberString,String userVoterIdString)
    {
        if(adhaarArrayList.size()==0)
            return true;
        else
        {
            for(UserDatabase child:adhaarArrayList)
            {
                //Log.i("UserAct1",child.getUserAdhaarNumber() + " " + child.getUserVoterId());
                if(child.getUserAdhaarNumber().equals(userAdhaarNumberString) || child.getUserVoterId().equals(userVoterIdString))
                    return false;
            }
        }
        return true;
    }

    //fuction to create user
    void createUser()
    {
        firebaseAuth.createUserWithEmailAndPassword(userEmailString,userAdhaarNumberString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //  storing data on database
                    UserDatabase userDatabase = new UserDatabase(userAdhaarNumberString,userNameString,userVoterIdString,userEmailString,userDateOfBirthString,userPhoneNumberString,userCityString,userStateString,userConstitunecyString);
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userDatabase);

                    // storing default profile on cloud storage and sign out
                    Uri uri = Uri.parse("android.resource://"+ getPackageName() +"/drawable/profilepic");
                    storageReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Images").child("ProfilePic").putFile(uri);

                    firebaseAuth.signOut();

                    // clearing edittext
                    userAdhaarNumberEditText.setText("");
                    userVoterIdEditText.setText("");
                    userNameEditText.setText("");
                    userEmailEditText.setText("");
                    userDateOfBirthEditText.setText("");
                    userPhoneNumberEditText.setText("");
                    userCityEditText.setText("");
                    userDateOfBirthEditText.setText("");
                    userStateEditText.setText("");
                    userConstitunecyEditText.setText("");
                    Toast.makeText(getApplicationContext(),"DataUploaded Successfully",Toast.LENGTH_SHORT).show();

                    //hiding progress bar
                    signupProgressbar.setVisibility(View.GONE);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}

