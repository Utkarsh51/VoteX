package com.android.tony.mastervotex;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.MyViewHolder> {

    List<UserDatabase> userDatabaseList;

    UserRecyclerAdapter(List<UserDatabase> userDatabaseList){
        this.userDatabaseList = userDatabaseList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView adhaarTextView,nameTextView,voterTextView;

        MyViewHolder(View v)
        {
            super(v);
            adhaarTextView = v.findViewById(R.id.usercycleradhaarnumbertextView);
            nameTextView = v.findViewById(R.id.usercyclernametextView);
            voterTextView = v.findViewById(R.id.usercyclervoteridtextView);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userrecyclerview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        UserDatabase userDatabase = userDatabaseList.get(i);
        viewHolder.voterTextView.setText(userDatabase.getUserVoterId());
        viewHolder.nameTextView.setText(userDatabase.getUserName());
        viewHolder.adhaarTextView.setText(userDatabase.getUserAdhaarNumber());
    }

    @Override
    public int getItemCount() {
        return userDatabaseList.size();
    }
}
