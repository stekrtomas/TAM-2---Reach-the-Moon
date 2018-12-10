package com.example.student.raketka;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HighScore extends Activity {

    private String playerName;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        TextView text = (TextView) findViewById(R.id.textView);
        score = this.getIntent().getIntExtra("playerScore", -1);
        playerName = this.getIntent().getStringExtra("playerName");
        // Toast.makeText(getApplicationContext(), "Game over!", Toast.LENGTH_SHORT).show();

        SQLiteDatabase db = new FeedReaderDbHelper(this).getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List itemIds = new ArrayList<>();
        text.setText("");
        int i = 1;
        while (cursor.moveToNext())

        {
            if (i <= 10) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME));
                int score = cursor.getInt(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE));
                itemIds.add(score);

                //Toast.makeText(this,name + " " + score,Toast.LENGTH_LONG).show();
                text.append(i + " : \t" + name + " :\t " + score + "\n");
                i++;
            }

        }
        cursor.close();


    }


}
