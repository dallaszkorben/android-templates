package com.example.akoel.serviceboundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static String tagMsg = "MyService";

    MyService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(tagMsg, "Activity has been CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tagMsg, "Activity has been DESTROYED");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(tagMsg, "   Activity STARTED");

        // Bind to LocalService
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(tagMsg, "   Activity STOPPED");

        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) */
    public void onButtonClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();

            Log.d(tagMsg, "         Service bound");
        }else{
            Log.d(tagMsg, "         Service unbound");
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            Log.d(tagMsg, "      Service has just bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;

            Log.d(tagMsg, "      Service hs just unbound");
        }
    };
}
