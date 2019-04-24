package com.example.abhishekaryan.soundrecorder.FileDirectory;

import android.os.Environment;

import java.io.File;
import java.util.Random;

public class FileDir {


    private String recordingFilePath=null;
    private String recordingFileName=null;
    private File folder;
    private   String filPath=null;


    public  String fileCreated() {


        recordingFilePath = createFile();

        if (recordingFilePath != null) {


            File file = new File(recordingFilePath);

            return recordingFilePath;


        }

        return null;
    }



    private  String createFile() {



        if(isExternalStorageWritable()){


            Random random=new Random();
            long sl=random.nextInt();

            recordingFileName="MyRec" + sl +".mp3";

            File fileDir=getPublicAlbumStorageDir();
            recordingFilePath=fileDir.getAbsolutePath();
            filPath = recordingFilePath;
            recordingFilePath =recordingFilePath + "/" + recordingFileName;

            //Log.i("Abhishek",recordingFilePath);

            return recordingFilePath;

        }

        else {

            //Toast.makeText(context,"File can't created",Toast.LENGTH_SHORT).show();
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


}
