package com.example.student.raketka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void CloseApplication(View view) {
        Toast.makeText(getApplicationContext(), "Ukončuji aplikaci!", Toast.LENGTH_SHORT).show();
        finish();
        System.exit(0);
    }

    public void StartGame(View view) {
        Toast.makeText(getApplicationContext(), "Play Game!!!", Toast.LENGTH_SHORT).show();
        String name = ((EditText) findViewById(R.id.playerName)).getText().toString();

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("playerName", name);
        startActivity(intent);
    }

    public void ShowHighScore(View view) {
        Toast.makeText(getApplicationContext(), "High Score!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, HighScore.class));
    }
}
