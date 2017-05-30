package com.example.akoel.servicestartedservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver receiver;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(MyService.tagMsg, "MainActivity's onCreate() event. MainActivity object created.");

        // This receiver allows the service to send information to the main activity
        // In this case we get infromation about the progressbar
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ProgressBar progressBar = null;
                LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

                Integer bar_id = intent.getIntExtra("bar_id", 1);
                Integer bar_value = intent.getIntExtra("bar_value", 0);

                //A new Service started -> remove all progressbar
                if( bar_id <= 1 && bar_value == 0 ){
                    int barID = 1;
                    while( ( progressBar = (ProgressBar)ll.findViewById( barID++ ) ) != null ){
                        ll.removeView(progressBar);
                    }
                }

                if( bar_id > 0 ) {
                    progressBar = (ProgressBar) ll.findViewById(bar_id);

                    //Totally new progressbar
                    if (null == progressBar) {

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleHorizontal);
                        progressBar.setLayoutParams(lp);
                        progressBar.setMax(MyService.steps);
                        progressBar.setId(bar_id);
                        ll.addView(progressBar);
                    }
                    progressBar.setProgress(bar_value);
                }
            }
        };

        //You have to Register the receiver manually
        IntentFilter filter = new IntentFilter();
        filter.addAction( MyService.ACTION );
        registerReceiver(receiver,filter);
    }

    //It is important to unregister the receiver on Destroy
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d(MyService.tagMsg, "MainActivity Destroyed.");
    }

    // Method to start the service if it is not there yet.
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