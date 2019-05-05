package com.android.tony.mastervotex;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CandidateRecyclerAdapter extends RecyclerView.Adapter<CandidateRecyclerAdapter.MyViewHolder> {

    List<CandidateDatabase> candidateDatabaseList;

    CandidateRecyclerAdapter(List<CandidateDatabase> candidateDatabaseList)
    {
        this.candidateDatabaseList = candidateDatabaseList;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        CandidateDatabase candidateDatabase = candidateDatabaseList.get(i);
        myViewHolder.voterTextView.setText(candidateDatabase.getCandidateVoterId());
        myViewHolder.nameTextView.setText(candidateDatabase.getCandidateName());
        myViewHolder.adhaarTextView.setText(candidateDatabase.getCandidateAdhaarNumber());
    }

    @Override
    public int getItemCount() {
        return candidateDatabaseList.size();
    }
}
