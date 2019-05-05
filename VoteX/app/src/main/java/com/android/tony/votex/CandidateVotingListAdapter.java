package com.android.tony.votex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CandidateVotingListAdapter extends BaseAdapter {

    Context context;
    List<CandidateDatabase> candidateDatabaseList;
    TextView candidateName,candidateParty,candidateAdhaar;

    CandidateVotingListAdapter(Context context,List<CandidateDatabase> candidateDatabaseList)
    {
        this.context = context;
        this.candidateDatabaseList = candidateDatabaseList;
    }

    @Override
    public int getCount() {
        return candidateDatabaseList.size();
    }

    @Override
    public Object getItem(int position) {
        return candidateDatabaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(this.context).inflate(R.layout.userrecyclerview,parent,false);
        candidateName = convertView.findViewById(R.id.usercycleradhaarnumbertextView);
        candidateParty = convertView.findViewById(R.id.usercyclernametextView);
        candidateAdhaar = convertView.findViewById(R.id.usercyclervoteridtextView);
        CandidateDatabase candidateDatabase = candidateDatabaseList.get(position);
        candidateName.setText(candidateDatabase.getCandidateName());
        candidateParty.setText(candidateDatabase.getCandidateParty());
        candidateAdhaar.setText(candidateDatabase.getCandidateAdhaarNumber());
        return convertView;
    }
}
