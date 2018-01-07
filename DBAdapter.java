package com.ab2017.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final Context context;
    //<--Change-->
    public static final String COL_1 = "id";
    public static final String COL_2 = "name";
    //<--Change-->
    private static final String TAG = "DBAdapter";
    //<--Change-->
    private static final String DATABASE_NAME = "DB_NAME";
    private static final String DATABASE_TABLE = "TABLE_NAME";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREATE = "SQL QUERY";
    //<--Change-->
    // Constructor
    public DBAdapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    // To create and upgrade a database in an Android application SQLiteOpenHelper subclass is usually created
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // onCreate() is called by the framework, if the database does not exist
            Log.d("Create", "Creating the database");

            try {
                db.execSQL(TABLE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Sends a Warn log message
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            // Method to execute an SQL statement directly
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }
    }

    // Opens the database
    public DBAdapter open() throws SQLException {
        // Create and/or open a database that will be used for reading and writing
        db = DBHelper.getWritableDatabase();

        // Use if you only want to read data from the database
        //db = DBHelper.getReadableDatabase();
        return this;
    }

    // Closes the database
    public void close() {
        // Closes the database
        DBHelper.close();
    }

    // Insert a book into the database
    //<--Change-->
    public long insertData(String name) {
        // The class ContentValues allows to define key/values. The "key" represents the
        // table column identifier and the "value" represents the content for the table
        // record in this column. ContentValues can be used for inserts and updates of database entries.
        ContentValues initialValues = new ContentValues();
        //<--Change-->
        initialValues.put(COL_1, name);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    //<--Change-->
    public Cursor getData(String name) throws SQLException {
        // rawQuery() directly accepts an SQL select statement as input.
        // query() provides a structured interface for specifying the SQL query.

        // A query returns a Cursor object. A Cursor represents the result of a query
        // and basically points to one row of the query result. This way Android can buffer
        // the query results efficiently; as it does not have to load all data into memory
        //<--Change-->
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                KEY_ROWID,KEY_NAME, KEY_AUT, KEY_YEAR, KEY_IMG}, KEY_NAME + " like '" + name +"%'", null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
