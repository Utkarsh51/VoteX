package com.android.tony.mastervotex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        setProg(0);
    }

    void setProg(final int prog)
    {
        progressBar.setProgress(prog);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                setProg(prog+25);
                if(prog==100)
                {
                    MainActivity.this.finish();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    //SurveyListActivity
                }
            }
        });
        thread.start();
    }
}
