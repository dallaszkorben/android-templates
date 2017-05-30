package com.example.akoel.handlerthread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private static String msgID = "MyThread";
    static int taskId = 0;
    static int threadId = 0;

    private Looper myLooper;
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyHandlerThread thread = new MyHandlerThread( "" + ++threadId );
        thread.start();
        myLooper = thread.getLooper();
        myHandler = new MyHandler(myLooper);

        Log.d(msgID, "Created a new Activity and Thread: " + threadId);
    }

    @Override
    public void onStart(){
        Log.d(msgID, "Starts Activity");
        super.onStart();
    }

    @Override
    public void onResume(){
        Log.d(msgID, "Resumes Activity");
        super.onResume();
    }

    @Override
    public void onPause(){
        Log.d(msgID, "Pauses Activity");
        super.onPause();
    }

    @Override
    public void onStop(){
        Log.d(msgID, "Stops Activity");
        super.onStop();
    }

    @Override
    public void onDestroy(){
        Log.d(msgID, "Destroys Activity");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    public void startTask(View view) {
        Message msg = myHandler.obtainMessage();
        msg.arg1 = threadId;
        msg.arg2 = ++taskId;

        Log.d(msgID, "   " + threadId + ". thread / " + taskId + ". task has triggered.");

        ProgressBar progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(lp);
        ll.addView(progressBar);
        msg.obj = progressBar;

        myHandler.sendMessage(msg);
    }

    private final class MyHandler extends Handler {
        private static final int steps = 10;

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(msgID, "      " + msg.arg1 + ". thread / " + msg.arg2 + ". task starts counting");

            ProgressBar progressBar = (ProgressBar)msg.obj;

            progressBar.setMax(steps);
            progressBar.setProgress(0);

            for( int i = 1; i <= steps; i++ ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                //!!!! You are able to manimulate the UI elements from the Handler !!!!
                progressBar.setProgress( i );
                Log.d(msgID, "         " + msg.arg1 + ". thread / " + msg.arg2 + ": " + i + " counted" );
            }
            Log.d(msgID, "      " + msg.arg1 + ". thread / " + msg.arg2 + ". task finished");
        }
    }

    private class MyHandlerThread extends HandlerThread {

        private String threadNumber;

        public MyHandlerThread(String threadNumber) {
            super(threadNumber);
            this.threadNumber = threadNumber;
        }

        public String getThread(){
            return threadNumber;
        }

    }
}
