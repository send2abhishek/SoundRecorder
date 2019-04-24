package com.example.abhishekaryan.soundrecorder.Services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.abhishekaryan.soundrecorder.ContentProvider.RecordingsProvider;
import com.example.abhishekaryan.soundrecorder.DataBase.DbHelper;
import com.example.abhishekaryan.soundrecorder.FileDirectory.FileDir;
import com.example.abhishekaryan.soundrecorder.R;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class RecordService extends Service {


    private static final String LOG_TAG ="LOG_TAG" ;
    private MediaRecorder mediaRecorder=null;
    private String recordingFilePath=null;
    private String recordingFileName=null;
    private File folder;
    private String filPath=null;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private int mElapsedSeconds = 0;


    public RecordService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //FileDir fileDir=new FileDir(this);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        startRecording();
        
        return START_STICKY;
    }

    private void startRecording() {

        int HE_AAC = 4;   /* High quality Audio Encoder */
        int AMR_NB = 1; /* Medium quality Audio Encoder */
        int AMR_WB = 2; /* low quality Audio Encoder */

        int AUDIO_ENCODER;
        int AUDIO_ENCODING_BIT_RATE;
        int AUDIO_SAMPLING_RATE;

        int AUDIO_CHANNEL;



        SharedPreferences prefences= PreferenceManager.getDefaultSharedPreferences(this);
        String quality=prefences.getString(getResources().getString(R.string.sound_quality_key),"High");
        String encoding=prefences.getString(getResources().getString(R.string.sound_encoding_key),"Mono");
        String file=prefences.getString(getResources().getString(R.string.file_encoding_key),".wav");
        recordingFilePath=setUpFilePath(file);
        Log.i("Abhishek","encoding - " +encoding);
        Log.i("Abhishek","quality - " +quality);
        Log.i("Abhishek","quality - " +file);

        if(quality.equals("High")){

            AUDIO_ENCODER= HE_AAC;
            AUDIO_ENCODING_BIT_RATE=192000;
            AUDIO_SAMPLING_RATE=48000;
        }
        else if(quality.equals("Medium")) {
            AUDIO_ENCODER= AMR_NB;
            AUDIO_ENCODING_BIT_RATE=96000;
            AUDIO_SAMPLING_RATE=44100;
        }
        else {
            AUDIO_ENCODER= AMR_WB;
            AUDIO_ENCODING_BIT_RATE=64000;
            AUDIO_SAMPLING_RATE=32000;
        }

        if(encoding.equals("Stereo")){

            AUDIO_CHANNEL=2;
        }
        else {
            AUDIO_CHANNEL=1;
        }




            if(recordingFilePath !=null) {

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setOutputFile(recordingFilePath);
                mediaRecorder.setAudioEncoder(AUDIO_ENCODER);
                mediaRecorder.setAudioEncodingBitRate(AUDIO_ENCODING_BIT_RATE);
                mediaRecorder.setAudioSamplingRate(AUDIO_SAMPLING_RATE);
                mediaRecorder.setAudioChannels(AUDIO_CHANNEL);

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    mStartingTimeMillis = System.currentTimeMillis();

                    //startTimer();
                    //startForeground(1, createNotification());

                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
            }
            else {

                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        }

    private String setUpFilePath(String fileNameExtn) {
        if (isExternalStorageWritable()) {


            DbHelper helper=new DbHelper(this);
            int slno=helper.QueryForID(helper.getWritableDatabase());
            int count=slno +1;

            recordingFileName = "MyRec-" + count + fileNameExtn;

            File fileDir = getPublicAlbumStorageDir();
            recordingFilePath = fileDir.getAbsolutePath();
            filPath = recordingFilePath;
            recordingFilePath = recordingFilePath + "/" + recordingFileName;

            if (recordingFilePath != null) {


                File file = new File(recordingFilePath);

                return recordingFilePath;


            }




        } else {

            Toast.makeText(this,"something went wrong ",Toast.LENGTH_SHORT).show();
        }

        return null;


    }





    public boolean isExternalStorageWritable() {

        /* Checks if external storage is available for read and write */
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public  File getPublicAlbumStorageDir() {
        // Get the directory for the user's public pictures directory.
        File folder = new File(Environment.getExternalStorageDirectory()+ "/VoiceRecordings");
        if (!folder.exists()) {
            //folder /SoundRecorder doesn't exist, create the folder
            folder.mkdir();

        }
        return folder;
    }


    @Override
    public void onDestroy() {


        super.onDestroy();
        if(mediaRecorder !=null){
            stopRecording();
        }




        Toast.makeText(this,"service stoped",Toast.LENGTH_SHORT).show();
    }

    private void stopRecording() {

        try {
            mediaRecorder.stop();
        } catch (IllegalStateException e) {

            Log.i("Abhishek-failed","Exception due to " +e);
        }
        mediaRecorder.release();
        mediaRecorder=null;

        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);

        Log.i("Abhishek","recording length - "+mElapsedMillis);
        Log.i("Abhishek","File name " +recordingFileName);
        Log.i("Abhishek","path name " +filPath);

        saveDataToDataBase(recordingFileName,filPath,mElapsedMillis);

        if(recordingFilePath !=null){

            Toast.makeText(this, "Recording saved to - " + recordingFilePath, Toast.LENGTH_LONG).show();
            Log.i("Abhishek",recordingFilePath);

        }

        else {
            Toast.makeText(this, "Recording failed  " , Toast.LENGTH_LONG).show();

        }

    }

    private void saveDataToDataBase(String recordingFileName, String filPath, long mElapsedMillis) {


//        DbHelper helper=new DbHelper(this);
//        SQLiteDatabase database=helper.getWritableDatabase();
//        int totalRec=helper.QueryForID(database) + 1;
//
//        String filename= recordingFileName + "file_" + (totalRec+1) + ".mp4" ;
//        Log.i("Abhishek","Created file new as - "+filename);

        ContentValues contentValues=new ContentValues();
        contentValues.put(DbHelper.TABLE_COLUMN_RECORDING_NAME,recordingFileName);
        contentValues.put(DbHelper.TABLE_COLUMN_RECORDING_FILE_PATH,filPath);
        contentValues.put(DbHelper.TABLE_COLUMN_RECORDING_LENGTH,mElapsedMillis);
        contentValues.put(DbHelper.TABLE_COLUMN_TIME_ADDED,System.currentTimeMillis());
        Uri soundUri=getContentResolver().insert(RecordingsProvider.CONTENT_URI,contentValues);
        Log.i("Abhishek","Inserted one value - "+ soundUri.getLastPathSegment());



    }
}
