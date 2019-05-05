package com.android.tony.votex;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    TextView adhaarTextView,nameTextView,voterIdTextView,cityTextView,consTextView,stateTextView,dobTextView,emailTextView,phoneNumbeTextView;
    ImageView profileImageView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        adhaarTextView = v.findViewById(R.id.profileadhaartextView);
        nameTextView = v.findViewById(R.id.profilenametextview);
        voterIdTextView = v.findViewById(R.id.profilevotertextView);
        cityTextView = v.findViewById(R.id.profilecitytextView);
        consTextView = v.findViewById(R.id.profileconstextView);
        stateTextView = v.findViewById(R.id.profilestatetextView);
        dobTextView = v.findViewById(R.id.profiledobtextView);
        emailTextView = v.findViewById(R.id.profileemailtextView);
        phoneNumbeTextView = v.findViewById(R.id.profilephonenumbertextView);
        profileImageView = v.findViewById(R.id.profileimageView);
        progressBar = v.findViewById(R.id.profileprogressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteX/Users").child(firebaseAuth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Images/ProfilePic");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);

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

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
