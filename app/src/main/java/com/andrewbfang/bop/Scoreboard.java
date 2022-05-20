package com.andrewbfang.bop;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Dan on 6/11/13.
 */
public class Scoreboard extends Activity {
    // This is the Adapter being used to provision the list's data.

    private Cursor cur;
    private CursorAdapter mAdapter;
    static final private String[] QUERY_PROJECTION = new String[] {
            "_id",
            HighScoreProvider.HighScoreDB.NAME,
            HighScoreProvider.HighScoreDB.DATE,
            HighScoreProvider.HighScoreDB.SCORE
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get a read-only database
        SQLiteDatabase db = new
                HighScoreProvider.HighScoreDB.HighScoreDBHelper(
                this).getReadableDatabase();


        cur = db.query(
                HighScoreProvider.HighScoreDB.TABLE_NAME,
                QUERY_PROJECTION,	                            // Projection (Columns to Return)
                null, 			                            	// WHERE clause
                null,                             				// Variables for WHERE clause
                null,                             				// Row grouping (none)
                null, 				                            // Row group filtering (none)
                HighScoreProvider.HighScoreDB.SCORE + " DESC"	// Sort order (default)
        );


        mAdapter = new CursorAdapter(this, null, 0) {
            @Override
            public void bindView(View v, Context ctx, Cursor cur) {
                TextView tvIndex = (TextView)v.findViewById(R.id.index_field);
                TextView tvName = (TextView)v.findViewById(R.id.name_field);
                TextView tvScore = (TextView)v.findViewById(R.id.score_field);
                TextView tvDate = (TextView)v.findViewById(R.id.date_field);
                Integer indexInt = cur.getPosition()+1;
                String nameString = cur.getString(1);
                Long dateLong = cur.getLong(2);
                int scoreInt = cur.getInt(3);
                tvIndex.setText(String.valueOf(indexInt));
                tvName.setText(nameString);
                tvScore.setText(String.valueOf(scoreInt));
                tvDate.setText(DateUtils.getRelativeTimeSpanString(dateLong));
            }
            @Override
            public View newView(Context ctx, Cursor cur, ViewGroup vg) {
                LayoutInflater li = getLayoutInflater();
                View v = li.inflate(R.layout.single_high_score, vg, false);
                return v;
            }
        };
        mAdapter.swapCursor(cur);

        setContentView(R.layout.activity_scoreboard);
        ((ListView) findViewById(R.id.listView)).setAdapter(mAdapter);
    }

    public void clearScores(View view){
        SQLiteDatabase db = new
                HighScoreProvider.HighScoreDB.HighScoreDBHelper(
                this).getWritableDatabase();
        db.delete(HighScoreProvider.HighScoreDB.TABLE_NAME, null, null);
        cur.requery();
        mAdapter.notifyDataSetChanged();
    }



}