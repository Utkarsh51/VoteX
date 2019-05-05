package com.android.tony.votex;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

public class AdhaarIdActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ImageView profilepic;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    EditText adhaarEditText,emailEditText;
    TextView nameTextView,adhaarTextView,voterIdTextView,emailTextView,phoneNumbeTextView,dobTextView,cityTextView,stateTextView,consTextView;
    ProgressBar progressBar,detailspg;
    Button sumbitButton,sendVerificationButoon,continueButton;
    ConstraintLayout detailsConstraintLayout,formConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhaar_id);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX").child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        detailspg = findViewById(R.id.detailsprogressBar);
        formConstraintLayout = findViewById(R.id.formconstraintLayout);
        detailsConstraintLayout = findViewById(R.id.detailslayout);
        profilepic = findViewById(R.id.adhaaruserimageView);
        adhaarEditText = findViewById(R.id.adhaareditText);
        emailEditText = findViewById(R.id.adharemaileditText);
        progressBar = findViewById(R.id.adhaarprogressBar);
        nameTextView = findViewById(R.id.adhaarusernametextview);
        adhaarTextView = findViewById(R.id.adhaaradhaarnumbertextView);
        voterIdTextView = findViewById(R.id.adhaarvoteridtextView);
        emailTextView = findViewById(R.id.adhaaremailtextView);
        phoneNumbeTextView = findViewById(R.id.adhaarphonenumbertextView);
        dobTextView = findViewById(R.id.adhaardobtextView);
        cityTextView = findViewById(R.id.adhaarcitytextView);
        stateTextView = findViewById(R.id.adhaarstatetextView);
        consTextView = findViewById(R.id.adhaarcontextView);
        sendVerificationButoon = findViewById(R.id.adhharsendbutton);
        continueButton = findViewById(R.id.adhaarcontibutton);
        sumbitButton = findViewById(R.id.adharsumbitbutton);


        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if(adhaarEditText.getText().toString().trim().isEmpty() || emailEditText.getText().toString().trim().isEmpty() )
                {
                    Toast.makeText(getApplicationContext(),"All Fields are Reuired",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    firebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString().trim(),adhaarEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                if(firebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    continueButton.setVisibility(View.VISIBLE);
                                    sendVerificationButoon.setVisibility(View.GONE);
                                }

                                formConstraintLayout.setVisibility(View.GONE);
                                detailsConstraintLayout.setVisibility(View.VISIBLE);
                                databaseReference = databaseReference.child(firebaseAuth.getCurrentUser().getUid());
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        UserDatabase userDatabase = dataSnapshot.getValue(UserDatabase.class);
                                        adhaarTextView.setText(userDatabase.getUserAdhaarNumber());
                                        nameTextView.setText(userDatabase.getUserName());
                                        voterIdTextView.setText(userDatabase.getUserVoterId());
                                        emailTextView.setText(userDatabase.getUserEmail());
                                        phoneNumbeTextView.setText(userDatabase.getUserPhoneNumber());
                                        dobTextView.setText(userDatabase.getUserDateOfBirth());
                                        stateTextView.setText(userDatabase.getUserState());
                                        cityTextView.setText(userDatabase.getUserCity());
                                        consTextView.setText(userDatabase.getUserConstitunecy());

                                        storageReference.child(firebaseAuth.getCurrentUser().getUid()).child("Images/ProfilePic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Picasso.get().load(uri).into(profilepic);
                                                progressBar.setVisibility(View.GONE);
                                                detailspg.setVisibility(View.GONE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        sendVerificationButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();
                        Toast.makeText(getApplicationContext(),"Check your Email",Toast.LENGTH_SHORT).show();
                        AdhaarIdActivity.this.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdhaarIdActivity.this.finish();
                startActivity(new Intent(AdhaarIdActivity.this,UserDashBoardActivity.class));
            }
        });

    }
}
