package com.android.tony.votex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class VoteListAdapter extends BaseAdapter {

    List<ElectionDatabase> electionDatabaseList;
    Context context;
    TextView consitutencyTextView,positionTextView,dateofelectionTextView;
    VoteListAdapter(Context context,List<ElectionDatabase> electionDatabaseList)
    {
        this.context = context;
        this.electionDatabaseList = electionDatabaseList;
    }

    @Override
    public int getCount() {
        return electionDatabaseList.size();
    }

    @Override
    public Object getItem(int position) {
        return electionDatabaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(this.context).inflate(R.layout.userrecyclerview,parent,false);
        consitutencyTextView = convertView.findViewById(R.id.usercycleradhaarnumbertextView);
        positionTextView= convertView.findViewById(R.id.usercyclernametextView);
        dateofelectionTextView = convertView.findViewById(R.id.usercyclervoteridtextView);
        ElectionDatabase electionDatabase = electionDatabaseList.get(position);
        consitutencyTextView.setText(electionDatabase.getConsitutecy());
        dateofelectionTextView.setText(electionDatabase.dateOfElection);
        positionTextView.setText(electionDatabase.getPosition());
        return convertView;
    }
}
