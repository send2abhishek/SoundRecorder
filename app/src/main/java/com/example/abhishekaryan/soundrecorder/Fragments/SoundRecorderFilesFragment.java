package com.example.abhishekaryan.soundrecorder.Fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekaryan.soundrecorder.ContentProvider.RecordingsProvider;
import com.example.abhishekaryan.soundrecorder.DataBase.DbHelper;
import com.example.abhishekaryan.soundrecorder.Entity.RecordingItems;
import com.example.abhishekaryan.soundrecorder.R;
import com.example.abhishekaryan.soundrecorder.ViewHolder.RecorderCursorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SoundRecorderFilesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        AdapterView.OnItemLongClickListener {

    private ListView listView;
    private RecorderCursorAdapter recorderCursorAdapter;
    private List<RecordingItems> recordingItems;
    private TextView fileNameText;
    private TextView durationText;
    private ImageButton playBtn;
    private Boolean stateIdentifier=true;
    private SeekBar seekBar;
    long minutes = 0;
    long seconds = 0;
    private TextView currentProgress;
    private MediaPlayer mediaPlayer=null;
    private Handler handler=null;
    String filefullPath=null;
    EditText desc;


    public SoundRecorderFilesFragment() {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new Handler();
        getLoaderManager().initLoader(0,null,this);


    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            stopPlaying();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopPlaying();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sound_recorder_files_list,container,false);
        listView=(ListView)view.findViewById(R.id.soundRecorder_files_listView);
        fileNameText=(TextView)view.findViewById(R.id.sound_record_player_file_name);
        durationText=(TextView)view.findViewById(R.id.sound_record_player_file_duration_end);
        playBtn=(ImageButton) view.findViewById(R.id.sound_record_player_playBtn);
        currentProgress=(TextView)view.findViewById(R.id.sound_record_player_file_duration_start);
        seekBar=(SeekBar)view.findViewById(R.id.sound_record_player_seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        playBtn.setOnClickListener(this);
        recorderCursorAdapter=new RecorderCursorAdapter(getActivity(),null,0);
        listView.setAdapter(recorderCursorAdapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);




        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {


        return new CursorLoader(getActivity(),RecordingsProvider.CONTENT_URI,null,
                null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        recorderCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        recorderCursorAdapter.swapCursor(null);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Cursor cursor=(Cursor)recorderCursorAdapter.getItem(position);

        String fileName=cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_NAME));
        String filePath=cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_FILE_PATH));
        long fileLength=cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_LENGTH));


        long minutes = TimeUnit.MILLISECONDS.toMinutes(fileLength);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(fileLength)
                - TimeUnit.MINUTES.toSeconds(minutes);

        String duration=String.format("%02d:%02d", minutes, seconds);


        if(mediaPlayer !=null){
            stopPlaying();

            playMediaFiles(fileName, filePath, duration);

        }

        else {

            playMediaFiles(fileName, filePath, duration);
        }


    }

    private void playMediaFiles(String fileName, String filePath, String duration) {


        stateIdentifier=false;


        playBtn.setImageResource(R.mipmap.ic_pause);
        fileNameText.setText(fileName);
        durationText.setText(duration);


        filefullPath=filePath + "/" + fileName;
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(filefullPath);
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
           //mediaPlayer.seekTo(progress);


            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
            updateSeekBar();

        } catch (IOException e) {
            Log.e("MediaPlayer", "prepare() failed");
        }

        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);







    }

    @Override
    public void onClick(View v) {

        onPlay(stateIdentifier);
        //stateIdentifier = false;







    }

    private void onPlay(Boolean stateIdentifier) {


        if (stateIdentifier) {
            //currently MediaPlayer is not playing audio
            if(mediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            //pause the MediaPlayer
            pausePlaying();
        }
    }

    private void startPlaying() {





        if(filefullPath !=null){


            playBtn.setImageResource(R.mipmap.ic_pause);

            mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(filefullPath);
                mediaPlayer.prepare();
                seekBar.setMax(mediaPlayer.getDuration());
                //mediaPlayer.seekTo(progress);


                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlaying();
                    }
                });
                updateSeekBar();

            } catch (IOException e) {
                Log.e("MediaPlayer", "prepare() failed");
            }

            //keep screen on while playing audio
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        }

        else {
            Toast.makeText(getActivity(),"Please select the file from the list to play",Toast.LENGTH_SHORT).show();
        }


    }


    private void stopPlaying() {
        playBtn.setImageResource(R.mipmap.ic_play);
        handler.removeCallbacks(runnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        stateIdentifier=true;
        seekBar.setProgress(seekBar.getMax());

        currentProgress.setText(durationText.getText());
        seekBar.setProgress(seekBar.getMax());

        //allow the screen to turn off again once audio is finished playing
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void resumePlaying() {

        playBtn.setImageResource(R.mipmap.ic_pause);
        handler.removeCallbacks(runnable);
        mediaPlayer.start();
        updateSeekBar();
    }
    private void pausePlaying() {


        playBtn.setImageResource(R.mipmap.ic_play);
        handler.removeCallbacks(runnable);
        mediaPlayer.pause();
        stateIdentifier=true;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


        if(mediaPlayer !=null && fromUser){

            mediaPlayer.seekTo(progress);
            handler.removeCallbacks(runnable);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())
                    - TimeUnit.MINUTES.toSeconds(minutes);
            currentProgress.setText(String.format("%02d:%02d", minutes,seconds));
            updateSeekBar();
        }
        else {


        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if(mediaPlayer != null) {
            // remove message Handler from updating progress bar
            handler.removeCallbacks(runnable);
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (mediaPlayer != null) {
            handler.removeCallbacks(runnable);
            mediaPlayer.seekTo(seekBar.getProgress());

            long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())
                    - TimeUnit.MINUTES.toSeconds(minutes);
            currentProgress.setText(String.format("%02d:%02d", minutes,seconds));
            updateSeekBar();
        }

    }


    //updating mSeekBar
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer != null){

                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                currentProgress.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


        Cursor cursor=(Cursor)recorderCursorAdapter.getItem(position);
        final int tableId=cursor.getInt(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_ID));
        final String fileName=cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_NAME));
        final String filePath=cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_FILE_PATH));
        long fileLength=cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_LENGTH));


        final String[] items={"Delete","Rename","Share"};


        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);

        dialog.setTitle("Select Action");


        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //Toast.makeText(getActivity(),"You clicked - " + which,Toast.LENGTH_SHORT).show();

                if(which==0){


                    deleteSound(tableId,fileName,filePath);


                }
                else if(which==1){


                    renameSound(tableId,fileName,filePath);

                }
                else {

                    shareFile(fileName,filePath);
                }

            }
        });


        dialog.show();

        return true;
    }

    private void shareFile(String fileName, String filePath) {

        String FullFilePath=filePath + "/" + fileName;
        Uri uri = Uri.parse(FullFilePath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Sound File"));
    }


    public void deleteSound(int tableId, final String fileName, String filePath){



        String FullFilePath=filePath + "/" + fileName;

        if(FullFilePath.equals(filefullPath)){
            fileNameText.setText("");
            filefullPath=null;
            stopPlaying();


        }
        File file = new File(FullFilePath);
        final boolean deleted = file.delete();
        final String SoundFilter=DbHelper.TABLE_COLUMN_ID + "=" + tableId ;


        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),R.style.MyDeleteDialogTheme);

        dialog.setMessage("Are you sure to delete - "+fileName);
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(deleted){



                    getActivity().getContentResolver().delete(RecordingsProvider.CONTENT_URI,SoundFilter,null);

                    Toast.makeText(getActivity(),""+ fileName + " has deleted ",Toast.LENGTH_SHORT).show();
                    recorderCursorAdapter.notifyDataSetChanged();
                }
                else {

                    Toast.makeText(getActivity(),"File failed to Delete",Toast.LENGTH_SHORT).show();

                }

            }
        });
        dialog.setNegativeButton("Cancel",null);
        dialog.show();

    }

    private void renameSound(int tableId, final String fileName, String filePath) {

        String FullFilePath=filePath + "/" + fileName;

        if(FullFilePath.equals(filefullPath)){
            fileNameText.setText("");
            filefullPath=null;
            stopPlaying();

        }


        final String SoundFilter=DbHelper.TABLE_COLUMN_ID + "=" + tableId ;



        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lParamsMW = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        desc = new EditText(getActivity());
        desc.setLayoutParams(lParamsMW);
        desc.setHint("Enter Desc");
        desc.setText(fileName);
        linearLayout.addView(desc);

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),R.style.MyDeleteDialogTheme);

        dialog.setView(linearLayout);
        dialog.setTitle("Rename the file");

        final String FilePath=filePath + "/" ;



        dialog.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File existingFile= new File(FilePath,fileName);
                String renameFileFormat=desc.getText().toString() ;
                File renameFile= new File(FilePath,renameFileFormat);
                boolean isSuccess=existingFile.renameTo(renameFile);

                if(isSuccess){


                    ContentValues contentValues=new ContentValues();
                    contentValues.put(DbHelper.TABLE_COLUMN_RECORDING_NAME,renameFileFormat);
                    getActivity().getContentResolver().update(RecordingsProvider.CONTENT_URI,
                            contentValues,SoundFilter,null);
                    Toast.makeText(getActivity(),"file renamed to "+renameFileFormat ,Toast.LENGTH_SHORT).show();

                }

                else {

                    Toast.makeText(getActivity(),"failed to rename the file",Toast.LENGTH_SHORT).show();
                }




            }
        });
        dialog.setNegativeButton("Cancel",null);
        dialog.show();


    }
}
