package com.android.tony.mastervotex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    EditText userEmailEditText,userPasswordEditText;
    Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userEmailEditText = findViewById(R.id.signupemaileditText);
        userPasswordEditText = findViewById(R.id.signinpasswordeditText);
        signInButton = findViewById(R.id.signinbutton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEmailEditText.getText().toString().trim().isEmpty() && userPasswordEditText.getText().toString().trim().isEmpty())
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
                else
                {
                    if(userEmailEditText.getText().toString().trim().equals("admin") && userPasswordEditText.getText().toString().trim().equals("admin"))
                    {
                        SignInActivity.this.finish();
                        startActivity(new Intent(SignInActivity.this,AdminDashboardActivity.class));
                    }
                    else
                        Toast.makeText(getApplicationContext(),"You are not a valid user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
