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
import android.view.View;

public class MyService extends Service {
    public static String tagMsg = "MyService";
    public static String ACTION = "com.example.akoel.USE_SERVICE";
    public static int steps = 15;

    private Looper mServiceLooper;
    private MyServiceHandler mServiceHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Creates a new Service.
     * Only one service exists at time.
     * After the Start button was clicked and the Service is still existing then this method
     * will be ignored. In that case the onStartCommand() method will be invoked directly.
     */
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

    /**
     * After the Service was started this method is invoked with a new startId
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(tagMsg, "      " + this.hashCode() + ". Service Started: " + startId );

        // For each started request, send a message to start a job and deliver the
        // startId so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = hashCode();
        msg.arg2 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        sendBroadcast(0, 0);
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
            Log.d(tagMsg, "         MyService's handleMessage(): " + msg.arg1 + "-" + msg.arg2);

            //Create new progressbar if it necessary with 0 value
            sendBroadcast(msg.arg2, 0);

            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            for( int i = 1; i <= steps; i++ ) {

                //Set the actual progressbar value
                sendBroadcast(msg.arg2, i);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Log.d(tagMsg, "         MyService's handleMessage(): " + msg.arg1 + "-" + msg.arg2 + " counting: " + i);

            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg2);
        }
    }

    public void sendBroadcast( int taskId, int value ){
        Intent intent = new Intent();
        intent.putExtra("bar_id", taskId );
        intent.putExtra("bar_value", value );
        intent.setAction( ACTION );
        sendBroadcast(intent);
    }
}