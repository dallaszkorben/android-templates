package com.example.akoel.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    private Button button_backward,button_forward,button_pause,button_play;
    private ImageView iv;
    private MediaPlayer mediaPlayer = null;
    private boolean isGenerated = false;

    private int actualPosition = 0;
    private int endPosition = 0;

    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView textMusicTitle, textLengthTime, textActualTime;

    private double actualTime, lengthTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isGenerated) {
            button_backward = (Button) findViewById(R.id.button_backward);
            button_forward = (Button) findViewById(R.id.button_forward);
            button_pause = (Button) findViewById(R.id.button_pause);
            button_play = (Button) findViewById(R.id.button_play);

            textMusicTitle = (TextView) findViewById(R.id.musicTitle);
            textMusicTitle.setText("song.mp3");

            textLengthTime = (TextView) findViewById(R.id.lengthTime);
            textActualTime = (TextView) findViewById(R.id.actualTime);

            mediaPlayer = MediaPlayer.create(this, R.raw.song);
            //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Sound/Music/Garbo/Garbo-20000Feet.mp3");
            //Uri uri = Uri.parse("/storage/external_SD/Sound/Music/Garbo/Garbo-20000Feet.mp3");
            //mediaPlayer = MediaPlayer.create(this, uri);

            seekbar = (SeekBar) findViewById(R.id.seekBar);
            seekbar.setClickable(false);

            button_backward.setEnabled(true);
            button_forward.setEnabled(true);
            button_play.setEnabled(true);
            button_pause.setEnabled(false);

            button_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                    continuePlaying(mediaPlayer);
                }
            });

            button_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                    pausePlaying(mediaPlayer);
                }
            });

            button_forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int) actualTime;

                    if ((temp + forwardTime) <= endPosition) {
                        actualTime = actualTime + forwardTime;
                        mediaPlayer.seekTo((int) actualTime);
                        Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            button_backward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int) actualTime;

                    if ((temp - backwardTime) > 0) {
                        actualTime = actualTime - backwardTime;
                        mediaPlayer.seekTo((int) actualTime);
                        Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("Position", mediaPlayer.getCurrentPosition());
        savedInstanceState.putBoolean("isplaying", mediaPlayer.isPlaying());

        if (mediaPlayer.isPlaying())
            pausePlaying( mediaPlayer );

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        int position = savedInstanceState.getInt("Position");
        mediaPlayer.seekTo(position);
        if (savedInstanceState.getBoolean("isplaying"))
            continuePlaying( mediaPlayer);
    }

    void pausePlaying( MediaPlayer mediaPlayer){
        mediaPlayer.pause();

        button_pause.setEnabled(false);
        button_play.setEnabled(true);
        
        showTime();
    }

    void continuePlaying( MediaPlayer mediaPlayer ){
        mediaPlayer.start();

        endPosition = mediaPlayer.getDuration();
        actualPosition = mediaPlayer.getCurrentPosition();

        seekbar.setProgress(actualPosition);
        seekbar.setMax( endPosition );
        myHandler.postDelayed(UpdateSongTime,100);

        button_pause.setEnabled(true);
        button_play.setEnabled(false);
        
        showTime();
    }
    
    void showTime(){

        lengthTime = mediaPlayer.getDuration();
        actualTime = mediaPlayer.getCurrentPosition();

        textLengthTime.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) lengthTime),
                TimeUnit.MILLISECONDS.toSeconds((long) lengthTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                lengthTime))));
        
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            actualTime = mediaPlayer.getCurrentPosition();
            textActualTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) actualTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) actualTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) actualTime)))
            );

            seekbar.setProgress((int) actualTime);
            myHandler.postDelayed(this, 100);
        }

    };

}