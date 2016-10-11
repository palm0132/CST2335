package com.example.michael.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    Button loginLoginBtn;
    EditText loginEmailEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        final SharedPreferences sharedPref = getSharedPreferences("stored data", Context.MODE_PRIVATE);
        loginEmailEditTxt = (EditText) findViewById(R.id.loginEmailEditTxt);
        loginEmailEditTxt.setText(sharedPref.getString("DefaultEmail","email@domain.com"));

        loginLoginBtn = (Button) findViewById(R.id.loginLoginBtn);
        loginLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loginEmailEditTxt = (EditText)findViewById(R.id.loginEmailEditTxt);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("DefaultEmail", loginEmailEditTxt.getText().toString());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
