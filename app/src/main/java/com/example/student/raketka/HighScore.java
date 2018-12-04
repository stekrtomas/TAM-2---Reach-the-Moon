package com.example.student.raketka;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class HighScore extends Activity {

    private String playerName;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);


        score = this.getIntent().getIntExtra("playerScore", -1);
        playerName = this.getIntent().getStringExtra("playerName");
        Toast.makeText(getApplicationContext(), "wiiiiin teda vlastně lamka konec!", Toast.LENGTH_SHORT).show();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
