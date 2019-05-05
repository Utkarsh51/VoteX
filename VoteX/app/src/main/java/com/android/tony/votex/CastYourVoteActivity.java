package com.android.tony.votex;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class CastYourVoteActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    String userVoterId, userNumber,userEmail,userAdhaar, mVerificationId, code;
    ProgressBar progressBar,codeProgressBar;
    EditText voterIdEdittext, otpEditText;
    Button sumbitButton;
    Snackbar snackbar;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_your_vote);

        progressBar = findViewById(R.id.cyvprogressBar);
        voterIdEdittext = findViewById(R.id.cyvvoterideditText);
        sumbitButton = findViewById(R.id.cyvsumbitbutton);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX/Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDatabase userDatabase = dataSnapshot.getValue(UserDatabase.class);
                userVoterId = userDatabase.getUserVoterId();
                userNumber = userDatabase.getUserPhoneNumber();
                userEmail = userDatabase.getUserEmail();
                userAdhaar = userDatabase.getUserAdhaarNumber();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                progressBar.setVisibility(View.VISIBLE);
                String voterId = voterIdEdittext.getText().toString().trim();
                if (voterId.isEmpty() | !voterId.equals(userVoterId)) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Voter id ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {

                    sendVerificationCode(userNumber);
                    LayoutInflater layoutInflater = LayoutInflater.from(CastYourVoteActivity.this);
                    View promptView = layoutInflater.inflate(R.layout.verificationcodelayout, null);
                    alertDialogBuilder = new AlertDialog.Builder(CastYourVoteActivity.this);
                    alertDialogBuilder.setView(promptView);
                    otpEditText = (EditText) promptView.findViewById(R.id.otpedittext);
                    codeProgressBar = promptView.findViewById(R.id.codeprogressBar);
                    otpEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    otpEditText.setHint("Enter otp send @ +91XXXXXX" + userNumber.charAt(userNumber.length() - 4) + userNumber.charAt(userNumber.length() - 3) + userNumber.charAt(userNumber.length() - 2) + userNumber.charAt(userNumber.length() - 1));
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //otp verification
                            String code = otpEditText.getText().toString().trim();
                            if (code.isEmpty() || code.length() < 6) {
                                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //verifying the code entered manually

                            /*CastYourVoteActivity.this.finish();
                            Intent intent = new Intent(CastYourVoteActivity.this, VoteActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/

                            verifyVerificationCode(code);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert = alertDialogBuilder.create();
                    alert.show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        codeProgressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        FirebaseAuth.getInstance().signOut();
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobile, 1, TimeUnit.MINUTES,CastYourVoteActivity.this , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Toast.makeText(getApplicationContext(),"Please don't press anybutton we will verify u automatically",Toast.LENGTH_SHORT).show();
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    otpEditText.setText(code);
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                Log.i("Number","time out " + s);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Log.i("Number","Code Sent " + s);
                mVerificationId = s;
            }
        });
        Log.i("Number","Mobile " + mobile);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        codeProgressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(CastYourVoteActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseAuth.getInstance().signOut();
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,userAdhaar).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        CastYourVoteActivity.this.finish();
                                        Intent intent = new Intent(CastYourVoteActivity.this, VoteActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        snackbar = Snackbar.make(findViewById(R.id.csvparentcons), "Something went wrong", Snackbar.LENGTH_LONG);
                                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        });
                                        snackbar.show();
                                        alert.dismiss();
                                        codeProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {

                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            snackbar = Snackbar.make(findViewById(R.id.csvparentcons), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) { }
                            });
                            snackbar.show();
                            alert.dismiss();
                            codeProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
        codeProgressBar.setVisibility(View.GONE);
    }


}
