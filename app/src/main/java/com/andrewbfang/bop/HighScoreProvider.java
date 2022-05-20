package com.andrewbfang.bop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Dan on 6/13/13.
 */
public class HighScoreProvider {
    public static final String LOG_TAG = HighScoreProvider.class.getName();


    public static class HighScoreDB implements BaseColumns {
        // SQL table name
        public static final String TABLE_NAME = "HighScores";

        // SQL table column names
        public static final String NAME = "name";
        public static final String DATE = "date";
        public static final String SCORE = "score";

        // SQL create table query
        private static final String CREATE_TABLE_QUERY = "CREATE TABLE "
                + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT," + SCORE + " INTEGER,"+ DATE + " INTEGER" + ");";

        // SQL drop table query
        private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "
                + TABLE_NAME;

        // Name of the database, version of its schema
        private static final String DATABASE_NAME = "HighScoreDB";
        private static final int DATABASE_VERSION = 1;

        /**
         * SQLiteOpenHelper takes care of some of the basics of maintaining the database:
         * Creating, opening, closing, and updating the tables at the right times.
         */
        public static class HighScoreDBHelper extends SQLiteOpenHelper {
            public HighScoreDBHelper(Context paramContext) {
                super(paramContext, DATABASE_NAME, null, DATABASE_VERSION);
            }
            /**
             * Creates the underlying database w/table name & column names defined above.
             */
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_TABLE_QUERY);
            }
            /**
             * Deletes the old table and recreates database. A more sophisticated
             * application would convert and transfer records from the old version of the
             * database to the new one.
             */
            @Override
            public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
                                  int paramInt2) {
                Log.w(LOG_TAG, "Migrating database from version " + paramInt1 + " to "
                        + paramInt2 + ", which destroys all old data.");
                // Kills the table and existing data
                paramSQLiteDatabase.execSQL(DROP_TABLE_QUERY);
                onCreate(paramSQLiteDatabase);
            }
            @Override
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgrade(db, oldVersion, newVersion);
            }

//            @Override
//            public void insert(String name, int score, long date) {
//
//            }
        } // HighScoreDBHelper
    }
}
