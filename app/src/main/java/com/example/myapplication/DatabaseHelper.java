package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME_1 = "data.sqlite";
    public static final String DB_NAME_2 = "lessons.db";
    public static final String DB_NAME_3 = "user_data.sqlite";
    private static String DB_PATH = "";
    private static SQLiteDatabase mDatabase1;
    private static SQLiteDatabase mDatabase2;
    private static SQLiteDatabase mDatabase3;
    private static Context mContext;

    private DatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
    }
    public static void execSQL(int database, String query)
    {
        if(database==1)
            mDatabase1.execSQL(query);
        else if(database==2)
            mDatabase2.execSQL(query);
        else if(database==3)
            mDatabase3.execSQL(query);
    }

    public static void update(int database, int lessonIndex, int completedValue)
    {
        //inne bazy danych narazie nie potrzebne tu
        if(database == 3) {
            ContentValues values = new ContentValues();
            values.put("is_completed", completedValue);
            mDatabase3.update("user_data", values, "lesson_id=?", new String[]{Integer.toString(lessonIndex)});
        }
    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
        DB_PATH = mContext.getApplicationInfo().dataDir + "/databases/";

        copyAndOpenDatabase(DB_NAME_1);
        copyAndOpenDatabase(DB_NAME_2);
        copyAndOpenDatabase(DB_NAME_3);
    }

    private static void copyAndOpenDatabase(String dbName) {
        File dbFile = new File(DB_PATH + dbName);
        if (!dbFile.exists()) {
            SQLiteOpenHelper dummyHelper = new DatabaseHelper(mContext, dbName);
            dummyHelper.getReadableDatabase();
            try {
                copyDBFile(dbName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        openDatabase(dbName);
    }

    private static void copyDBFile(String dbName) throws IOException {
        InputStream inputStream = mContext.getAssets().open(dbName);
        File dbFolder = new File(DB_PATH);
        if (!dbFolder.exists()) dbFolder.mkdir();

        OutputStream outputStream = new FileOutputStream(DB_PATH + dbName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private static void openDatabase(String dbName) {
        String dbPath = DB_PATH + dbName;
        if (dbName.equals(DB_NAME_1)) {
            if (mDatabase1 == null || !mDatabase1.isOpen()) {
                mDatabase1 = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
        } else if (dbName.equals(DB_NAME_2)) {
            if (mDatabase2 == null || !mDatabase2.isOpen()) {
                mDatabase2 = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
        } else if (dbName.equals(DB_NAME_3)) {
            if (mDatabase3 == null || !mDatabase3.isOpen()) {
                mDatabase3 = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
        }
    }

    public static Cursor getData(String query, String dbName) {
        if (dbName.equals(DB_NAME_1)) {
            if (mDatabase1 == null || !mDatabase1.isOpen()) {
                openDatabase(DB_NAME_1);
            }
            return mDatabase1.rawQuery(query, null);
        } else if (dbName.equals(DB_NAME_2)) {
            if (mDatabase2 == null || !mDatabase2.isOpen()) {
                openDatabase(DB_NAME_2);
            }
            return mDatabase2.rawQuery(query, null);
        } else if (dbName.equals(DB_NAME_3)) {
            if (mDatabase3 == null || !mDatabase3.isOpen()) {
                openDatabase(DB_NAME_3);
            }
            return mDatabase3.rawQuery(query, null);
        } else {
            throw new IllegalArgumentException("Unknown database name: " + dbName);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}