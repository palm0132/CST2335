package com.example.michael.lab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    public final static int MESSAGE_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Button startImABtn = (Button) findViewById(R.id.startImABtn);
        Button startChatBtn = (Button) findViewById(R.id.startChatBtn);

        startImABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, MESSAGE_REQUEST_CODE);
            }
        });

        startChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });

    }

    // #6.3
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data){
        if (requestCode == MESSAGE_REQUEST_CODE){
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }
        // #11
        if (responseCode == Activity.RESULT_OK){
            // Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            // String messagePassed = data.getStringExtra("Response");
            // Add toast to display info in intent // displays ListItemsActivity passed: My information to share
            // Set Toast’s display time to either short or long
            // static final int LENGTH_SHORT = 1;
            // static final int LENGTH_LONG = 1;

            //this is the ListActivity
            Toast toast = Toast.makeText(this , "ListItemsActivity passed: My information to share", Toast.LENGTH_LONG);
            //displays message box
            toast.show();
        }
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
