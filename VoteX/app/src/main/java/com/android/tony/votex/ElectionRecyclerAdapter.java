package com.android.tony.votex;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ElectionRecyclerAdapter extends RecyclerView.Adapter<ElectionRecyclerAdapter.myViewHolder> {
    List<ElectionDatabase> electionDatabaseList;

    ElectionRecyclerAdapter(List<ElectionDatabase> electionDatabaseList)
    {
        this.electionDatabaseList = electionDatabaseList;
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView consitutencyTextView,positionTextView,dateofelectionTextView;

        myViewHolder(View v)
        {
            super(v);
            consitutencyTextView = v.findViewById(R.id.usercycleradhaarnumbertextView);
            positionTextView= v.findViewById(R.id.usercyclernametextView);
            dateofelectionTextView = v.findViewById(R.id.usercyclervoteridtextView);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new myViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userrecyclerview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        ElectionDatabase electionDatabase = electionDatabaseList.get(i);
        myViewHolder.consitutencyTextView.setText(electionDatabase.getConsitutecy());
        myViewHolder.dateofelectionTextView.setText(electionDatabase.getDateOfElection());
        myViewHolder.positionTextView.setText(electionDatabase.getPosition());
    }

    @Override
    public int getItemCount() {
        return electionDatabaseList.size();
    }
}
