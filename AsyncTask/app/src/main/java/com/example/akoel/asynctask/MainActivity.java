package com.example.akoel.asynctask;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private String tagMsg = "MyApp";
    private int orderNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTask(View view) {

        Log.d(tagMsg, "   " + this.hashCode() + ". task has triggered.");

        MyAsyncTask myTask = new MyAsyncTask(this.hashCode());
        //myTask.execute("" + orderNumber++ );                                               //Running the tasks SERIAL
        myTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "" + orderNumber++ );           //Running the tasks SERIAL
        //myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "" + orderNumber++ );    //Running the tasks PARALLEL

    }

    /**
     * !!!! It is not possible to setProgress() from the AsyncTask !!!!!
     *
     * @param pb
     * @param status
     */
    public void changeStatusOfProgress(ProgressBar pb, int status){
        pb.setProgress(status);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, Integer> {
        private static final int steps = 10;
        private ProgressBar progressBar;
        private Integer activityId;

        public MyAsyncTask(Integer ActivityId) {
            this.activityId = ActivityId;
        }

        @Override
        protected void onPreExecute() {
            Log.d( tagMsg, "   " + activityId + " activity/" + this.hashCode() + " AsyncTask Started");

            //progressBar = (ProgressBar) findViewById(R.id.progressbar);
            progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            ll.addView(progressBar);

            progressBar.setMax(steps);
            progressBar.setProgress(0);

        }

        @Override
        protected Integer doInBackground(String... strings) {
            for( int i = 1; i <= steps; i++ ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                changeStatusOfProgress( progressBar, i);
                Log.d( tagMsg, "      " + activityId + " activity/" + this.hashCode() + "/" + strings[0] + ": " + i + ". counted");
            }
            return 315;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            Log.d( tagMsg, "   " + activityId + " activity/" + this.hashCode() + " AsyncTask Ended: " + integer);
        }
    }
}
