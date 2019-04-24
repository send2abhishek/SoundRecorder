package com.example.abhishekaryan.soundrecorder.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhishekaryan.soundrecorder.R;
import com.example.abhishekaryan.soundrecorder.Services.RecordService;

import java.util.Timer;
import java.util.TimerTask;

public class SoundRecorderFragment extends Fragment implements View.OnClickListener {

    private View RecorderUI;
    Boolean recording=true;
    private Chronometer chronometer;
    private ProgressBar progressBar;
    private ImageButton RecordBtn;
    private Handler handler;
    private Runnable runnable;
    private Timer timer;
    int i=0;



    public SoundRecorderFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler=new Handler();
        timer=new Timer();





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        RecorderUI=inflater.inflate(R.layout.sound_recorder_layout,container,false);
        chronometer=(Chronometer)RecorderUI.findViewById(R.id.sound_recorder_chronometer);
        progressBar=(ProgressBar)RecorderUI.findViewById(R.id.recorder_progressbar);
        progressBar.setVisibility(View.GONE);
        RecordBtn=(ImageButton) RecorderUI.findViewById(R.id.sound_recorder_recordBtn);
        RecordBtn.setOnClickListener(this);
        return RecorderUI;
    }

    private void handleChronometer() {





    }



    @Override
    public void onClick(View view) {

        int id=view.getId();


        switch (id){


            case R.id.sound_recorder_recordBtn:

                if(recording){

                    chronometer.setBase(SystemClock.elapsedRealtime());;
                    chronometer.start();
                    progressBar.setVisibility(View.VISIBLE);
                    runnable= new Runnable() {
                        @Override
                        public void run() {


                            if(++i<=100){

                                progressBar.setProgress(i);
                            }
                            else {

                                //timer.cancel();
                            }

                        }
                    };

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            handler.post(runnable);

                        }
                    },2000,2000);




                    recording=false;
                    RecordBtn.setImageResource(R.mipmap.ic_stop);
                    Intent intent=new Intent(getActivity(),RecordService.class);
                    getActivity().startService(intent);

                }
                else {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());

                    RecordBtn.setImageResource(R.mipmap.ic_launcher);
                    progressBar.setVisibility(View.GONE);
                    progressBar.setProgress(0);
                    i=0;
                    timer.purge();
                    recording=true;

                    Intent intent=new Intent(getActivity(),RecordService.class);
                    getActivity().stopService(intent);


                }

                break;
        }

    }



}
