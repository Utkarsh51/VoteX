package com.android.tony.votex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;


public class LogoutFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AlertDialog.Builder(getContext()).setIcon(getResources().getDrawable(R.mipmap.ic_launcher)).setTitle(getResources().getString(R.string.app_name)).setMessage("Are you sure you want to Logout").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getContext(),MainActivity.class));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                UserDashBoardActivity.bottomNavigationView.setSelectedItemId(R.id.usdhome);
            }
        }).setCancelable(false).create().show();
    }
}
