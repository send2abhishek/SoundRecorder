package com.example.abhishekaryan.soundrecorder.ViewHolder;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.abhishekaryan.soundrecorder.DataBase.DbHelper;
import com.example.abhishekaryan.soundrecorder.Entity.RecordingItems;
import com.example.abhishekaryan.soundrecorder.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecorderCursorAdapter extends CursorAdapter {

    private List<RecordingItems> recordingItems;
    public RecorderCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);

        recordingItems=new ArrayList<>();
    }

    public List<RecordingItems> getRecordingItems() {
        return recordingItems;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.sound_recorder_file_list_container,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int id=cursor.getInt(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_ID));
        String fileName=cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_NAME));
        String filePath=cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_FILE_PATH));
        long fileLength=cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_RECORDING_LENGTH));
        long fileAdded=cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_COLUMN_TIME_ADDED));
        recordingItems.add(new RecordingItems(id,fileName,filePath,fileLength,fileAdded));


        long itemDuration = fileLength;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);


        View cardView=view.findViewById(R.id.sound_recorder_files_cardView);
        TextView fileNameTextView=(TextView)view.findViewById(R.id.sound_recorder_file_file_name_text);
        TextView fileNameCreate=(TextView)view.findViewById(R.id.sound_recorder_file_file_creation_date);
        TextView fileNameLength=(TextView)view.findViewById(R.id.sound_recorder_file_duration);
        fileNameTextView.setText(fileName);
        fileNameCreate.setText(
                DateUtils.formatDateTime(
                        context,
                        fileAdded,
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE |
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR
                )
        );
        fileNameLength.setText(String.format("%02d:%02d", minutes, seconds));



    }
}
