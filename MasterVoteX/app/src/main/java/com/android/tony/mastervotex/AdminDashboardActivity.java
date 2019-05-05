package com.android.tony.mastervotex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class AdminDashboardActivity extends AppCompatActivity {

    CardView addUserCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
    }

    void onClickCard(View v)
    {
        switch (v.getId())
        {
            case R.id.addusercardView:
                startActivity(new Intent(AdminDashboardActivity.this, UserActivity.class));
                break;
            case R.id.addcandidatecardView:
                startActivity(new Intent(AdminDashboardActivity.this,CandidateActivity.class));
                break;
            case R.id.electioncardView:
                startActivity(new Intent(AdminDashboardActivity.this, ElectionActivity.class));
                break;
        }
    }
}
