package com.android.tony.votex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class UserDashBoardActivity extends AppCompatActivity {

    static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);

        bottomNavigationView = findViewById(R.id.userdashboardbottonnavigation);
        switchFrag(new HomeFragment());
        bottomNavigationView.setSelectedItemId(R.id.usdhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.usdhome:
                        switchFrag(new HomeFragment());
                        return true;
                    case R.id.usduser:
                        switchFrag(new ProfileFragment());
                        return true;
                    case R.id.usdlogout:
                        switchFrag(new LogoutFragment());
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    void switchFrag(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framecontainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickCard(View v)
    {
        switch (v.getId())
        {
            case R.id.upcomingcardView:
                startActivity(new Intent(getApplicationContext(),UpcomigElectionActivity.class));
                break;
            case R.id.cyvcardView:
                startActivity(new Intent(getApplicationContext(),CastYourVoteActivity.class));
                break;
            case R.id.resultscardView:
                startActivity(new Intent(getApplicationContext(),ResultActivity.class));
                break;
        }
    }
}
