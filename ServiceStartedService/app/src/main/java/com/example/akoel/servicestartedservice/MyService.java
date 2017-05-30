package com.example.akoel.servicestartedservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {
    public static String tagMsg = "MyService";

    private Looper mServiceLooper;
    private MyServiceHandler mServiceHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(tagMsg, "   " + this.hashCode() + ". Service Created");

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("Parameters to Service");
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new MyServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.d(tagMsg, "      " + this.hashCode() + ". Service Started: " + startId );

        // For each start request, send a message to start a job and deliver the
        // startId so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tagMsg, "   " + this.hashCode() + ". Service Destroyed");
    }

    // Handler that receives messages from the thread
    private final class MyServiceHandler extends Handler {
        public MyServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            int steps = 15;
            Log.d(tagMsg, "         MyService's handleMessage(): " + msg.arg1);

            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            for( int i = 1; i <= steps; i++ ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Log.d(tagMsg, "         MyService's handleMessage(): " + msg.arg1 + " counting: " + i);
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }
}