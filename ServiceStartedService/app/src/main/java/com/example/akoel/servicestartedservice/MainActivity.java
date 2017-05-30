package com.example.akoel.servicestartedservice;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(MyService.tagMsg, "MainActivity's onCreate() event. MainActivity object created.");
    }

    public void startService(View view) {
        //START_SERVICE
        Log.d(MyService.tagMsg, "MainActivity's startService() button clicked.");

        //It creates the service, if it was NOT created yet, invoking the onCreate() Method
        //If the service is done then it calls the onStartCommand() method.
        //It sends an integer to the onStartCommand() method as startId which is a sequential number
        //in a specific service. If a new service is created then it starts again from zero.
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        Log.d(MyService.tagMsg, "MainActivity's stopService() button clicked.");

        //It stops the service regardless
        //If under the service a Thread was running then it still running.
        stopService(new Intent(getBaseContext(), MyService.class));
    }
}