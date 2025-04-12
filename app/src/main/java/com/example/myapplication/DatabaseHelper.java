package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "data.sqlite";
    private static String DB_PATH = "";
    private static SQLiteDatabase mDatabase;
    private static Context mContext;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
        DB_PATH = mContext.getApplicationInfo().dataDir + "/databases/";

        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            SQLiteOpenHelper dummyHelper = new DatabaseHelper(mContext);
            dummyHelper.getReadableDatabase();
            try {
                copyDBFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        openDatabase();
    }

    private static void copyDBFile() throws IOException {
        InputStream inputStream = mContext.getAssets().open(DB_NAME);
        File dbFolder = new File(DB_PATH);
        if (!dbFolder.exists()) dbFolder.mkdir();

        OutputStream outputStream = new FileOutputStream(DB_PATH + DB_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private static void openDatabase() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            String dbPath = DB_PATH + DB_NAME;
            mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public static Cursor getData(String query) {
        if (mDatabase == null || !mDatabase.isOpen()) {
            openDatabase();
        }
        return mDatabase.rawQuery(query, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}