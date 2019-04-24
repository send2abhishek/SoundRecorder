package com.example.abhishekaryan.soundrecorder.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.abhishekaryan.soundrecorder.DataBase.DbHelper;

public class RecordingsProvider extends ContentProvider {

    private static final String AUTHORITY="com.example.abhishekaryan.soundrecorder.ContentProvider.Recordings";
    private static final String BASE_PATH="SoundRecords";
    public static final Uri CONTENT_URI=Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);


    //constant to identify requested operation
    private static final int SoundRecords=1;
    private static final int SoundRecords_ID=2;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    public static final String CONTENT_ITEM_TYPE="SoundRecords";

    static {

        uriMatcher.addURI(AUTHORITY,BASE_PATH,SoundRecords);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",SoundRecords_ID);

    }

    private SQLiteDatabase database;


    @Override
    public boolean onCreate() {

        DbHelper dbHelper=new DbHelper(getContext());
        database=dbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {


        if(uriMatcher.match(uri)==SoundRecords_ID){
            selection=DbHelper.TABLE_COLUMN_ID + "=" + uri.getLastPathSegment();
        }


        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DbHelper.TABLE_NAME);


        Cursor cursor=queryBuilder.query(database,DbHelper.ALL_COLIMNS,selection,
                null,null,null,DbHelper.TABLE_COLUMN_TIME_ADDED);

         cursor.setNotificationUri(getContext().getContentResolver(),uri);




        return cursor;
    }
    

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long id=database.insert(DbHelper.TABLE_NAME,null,values);
        getContext().getContentResolver().notifyChange(uri, null, false);
        return Uri.parse(BASE_PATH + "/" +id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        getContext().getContentResolver().notifyChange(uri, null, false);
        return database.delete(DbHelper.TABLE_NAME,selection,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        getContext().getContentResolver().notifyChange(uri, null, false);
        return database.update(DbHelper.TABLE_NAME,values,selection,selectionArgs);
    }
}
