package com.andrewbfang.bop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Andrew
 */
public class FinalScore extends Activity{

    private String name;
    int score;
    Long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_score);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose name");
        final View nameChange = LayoutInflater.from(this).inflate(R.layout.name_change, null);
        builder.setView(nameChange);
        ((EditText) nameChange.findViewById(R.id.nameSpace)).setText(com.andrewbfang.bop.MainActivity.name);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mInput = ((EditText) nameChange.findViewById(R.id.nameSpace)).getText().toString();
                setName(mInput);
                setDB();
            }
        });
        builder.show();

        TextView finalScore = (TextView) findViewById(R.id.final_score);
        Intent intent = getIntent();
        score = intent.getIntExtra("FANTASTICSCORE", 12345);
        time = System.currentTimeMillis();

        finalScore.setText("Score: " + score);


    }

    public void setDB(){
        SQLiteDatabase db  = new com.andrewbfang.bop.HighScoreProvider.HighScoreDB.HighScoreDBHelper(this).getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(com.andrewbfang.bop.HighScoreProvider.HighScoreDB.NAME, name);
        cv.put(com.andrewbfang.bop.HighScoreProvider.HighScoreDB.DATE, time);
        cv.put(com.andrewbfang.bop.HighScoreProvider.HighScoreDB.SCORE, score);
        db.insert(com.andrewbfang.bop.HighScoreProvider.HighScoreDB.TABLE_NAME, null, cv);
    }

    public void setName(String name){
        this.name = name;
    }

    public void replay(View view) {
        Intent intent = new Intent(this, com.andrewbfang.bop.MainActivity.class);
        startActivity(intent);
    }

    public void scoreboard(View view) {
        Intent intent = new Intent(this, com.andrewbfang.bop.Scoreboard.class);
        startActivity(intent);


    }
}
