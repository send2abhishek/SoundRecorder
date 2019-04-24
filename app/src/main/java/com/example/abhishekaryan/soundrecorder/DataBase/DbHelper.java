package com.example.abhishekaryan.soundrecorder.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;
    //constant for database name and version

    private static final String DATABASE_NAME="records.db";
    private static final int DATABASE_VERSION=1;


    //constant for Table name  SoundRecords and Table columns

    public static final String TABLE_NAME="SoundRecords";
    public static final String TABLE_COLUMN_ID="_id";
    public static final String TABLE_COLUMN_RECORDING_NAME = "recording_name";
    public static final String TABLE_COLUMN_RECORDING_FILE_PATH = "file_path";
    public static final String TABLE_COLUMN_RECORDING_LENGTH = "length";
    public static final String TABLE_COLUMN_TIME_ADDED = "time_added";


    public static final String[] ALL_COLIMNS={
            TABLE_COLUMN_ID,
            TABLE_COLUMN_RECORDING_NAME,
            TABLE_COLUMN_RECORDING_FILE_PATH,
            TABLE_COLUMN_RECORDING_LENGTH,
            TABLE_COLUMN_TIME_ADDED
    };


    //Table creation constant for table notes
    private static final String TABLE_CREATION_QUERY="CREATE TABLE " + TABLE_NAME + " ("
            + TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TABLE_COLUMN_RECORDING_NAME + " TEXT,"
            + TABLE_COLUMN_RECORDING_FILE_PATH + " TEXT,"
            + TABLE_COLUMN_RECORDING_LENGTH + " INTEGER,"
            + TABLE_COLUMN_TIME_ADDED + " INTEGER" +
            " )";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        try {

            db.execSQL(TABLE_CREATION_QUERY);
            Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {

            Toast.makeText(context, "Have Expection in creating table" +e, Toast.LENGTH_LONG).show();
            Log.e("DatabaseTagg",""+e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            Toast.makeText(context, "onUpgrade called",Toast.LENGTH_SHORT).show();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
        catch (SQLException e) {

            Toast.makeText(context, "Have Expection in upgrading table" +e, Toast.LENGTH_LONG).show();
        }

    }

    public int QueryForID(SQLiteDatabase database){


        int count=0;


        String[] projection={DbHelper.TABLE_COLUMN_ID};


        Cursor cursor=database.query(TABLE_NAME,projection,
                null,null,null,null,null);


        count=cursor.getCount();
        cursor.close();

        return count;

    }
}
