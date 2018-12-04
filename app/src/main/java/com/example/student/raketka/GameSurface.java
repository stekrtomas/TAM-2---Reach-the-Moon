package com.example.student.raketka;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private final String playerName;
    private final Controller controller;
    private GameThread gameThread;
    private Rocket rocket;
    //private Enemy enemy;
    //private Fuel fuel;
    private int counter;

    private final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.fuel);
    private final MediaPlayer mp2 = MediaPlayer.create(getContext(), R.raw.mcmc);

    boolean addEnemy1;
    boolean addEnemy2;
    boolean addEnemy3;
    boolean addEnemy4;

    boolean gameOver = true;


    public List<Enemy> enemies = new ArrayList<>();
    public List<Fuel> fuels = new ArrayList<>();

    private Point resolution;

    public FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getContext());

    public GameSurface(Context context, String name, Controller controller) {
        super(context);
        this.playerName = name;
        this.controller = controller;

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update()  {
        float rocketAngle = controller.getAngle();
        rocket.Move(rocketAngle);
        ArrayList<Enemy> toRemove = new ArrayList<>();
        ArrayList<Fuel> toRemoveFuel = new ArrayList<>();
        //enemy.Move();
        //fuel.Move();


        for (Enemy enemy : enemies) {
            enemy.Move();
            if (rocket.collideEnemy(enemy)) {
                if (gameOver) {
                    this.GameOver();
                    gameOver = false;
                }

                toRemove.add(enemy);
            }
            if (enemy.getPosition().y > this.resolution.y) {
                toRemove.add(enemy);
            }
        }


        for (Fuel fuel : fuels) {
            fuel.Move();
            if (rocket.collideFuel(fuel)) {
                rocket.addFuel();
                mp.start();
                toRemoveFuel.add(fuel);
            }
            if (fuel.getPosition().y > this.resolution.y) {
                toRemoveFuel.add(fuel);
            }
        }

        //add enemies
        for (int i = 0; i < toRemove.size(); i++) {
            enemies.add(new Enemy(this));
        }

        //add fuels
        for (int i = 0; i < toRemoveFuel.size(); i++) {
            fuels.add(new Fuel(this));
        }

        enemies.removeAll(toRemove);
        fuels.removeAll(toRemoveFuel);

        rocket.addScore();
        counter++;

        if (rocket.getScore() > 1000 && rocket.getScore() < 1999) {
            if (addEnemy1) {
                addEnemy1 = false;
                enemies.add(new Enemy(this));
            }
        }
        if (rocket.getScore() > 2000 && rocket.getScore() < 2999) {
            if (addEnemy2) {
                addEnemy2 = false;
                enemies.add(new Enemy(this));
            }
        }
        if (rocket.getScore() > 3000 && rocket.getScore() < 3999) {
            if (addEnemy3) {
                addEnemy3 = false;
                enemies.add(new Enemy(this));
            }
        }
        if (rocket.getScore() > 4000) {
            if (addEnemy4) {
                addEnemy4 = false;
                enemies.add(new Enemy(this));
            }
        }

        if (counter > 100) {
            counter = 0;

            rocket.useFuel();
        }

        if (rocket.getFuel() == 0) {
            if (gameOver) {
                this.GameOver();
                gameOver = false;
            }
        }

    }

    private void GameOver() {
        mp2.stop();
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, this.playerName);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, rocket.getScore());

        // Insert the new row, returning the primary key value of the new row
        if (db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values) < 0) {
            Toast.makeText(getContext(), "chyba", Toast.LENGTH_LONG).show();
        }


        Intent intent = new Intent(getContext(), HighScore.class);
        intent.putExtra("playerName", playerName);
        intent.putExtra("playerScore", rocket.getScore());
        getContext().startActivity(intent);
    }

    public Point getResolution() {
        return resolution;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        resolution = new Point(w, h);
        super.onSizeChanged(w,h,oldw,oldh);
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);


        // draw
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(25);
        canvas.drawText("Player: " + playerName + ",Score:  " + rocket.getScore() + " ,Fuel: " + rocket.getFuel(), 10, 25, paint);


        rocket.draw(canvas);
        for (Enemy enemy : enemies) {
            enemy.draw(canvas);
        }
        for (Fuel fuel : fuels) {
            fuel.draw(canvas);
        }

        //enemy.draw(canvas);
        //fuel.draw(canvas);
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();

        counter = 0;
        addEnemy1 = true;
        addEnemy2 = true;
        addEnemy3 = true;
        addEnemy4 = true;

        mp2.start();


        rocket = new Rocket(this);
        //enemy = new Enemy(this);
        enemies.add(new Enemy(this));
        fuels.add(new Fuel(this));
        //fuel = new Fuel(this);
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry = false;
        }
    }

}
