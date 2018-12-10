package com.example.student.raketka;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;


public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private final String playerName;
    private final Controller controller;
    private final GameViewModel model;
    private GameThread gameThread;
    private Bitmap mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);

    private final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.fuel);


    private Point resolution;

    public FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getContext());

    public GameSurface(Context context, String name, Controller controller, GameViewModel model) {
        super(context);
        this.playerName = name;
        this.controller = controller;
        this.model = model;
        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update() {
        float rocketAngle = controller.getAngle();
        model.rocket.Move(rocketAngle);
        ArrayList<Enemy> toRemove = new ArrayList<>();
        ArrayList<Fuel> toRemoveFuel = new ArrayList<>();
        //enemy.Move();
        //fuel.Move();


        for (Enemy enemy : model.enemies) {
            enemy.Move();
            if (model.rocket.collideEnemy(enemy)) {
                if (model.gameOver) {
                    this.GameOver();
                    model.gameOver = false;
                }

                toRemove.add(enemy);
            }
            if (enemy.getPosition().y > this.resolution.y) {
                toRemove.add(enemy);
            }
        }


        for (Fuel fuel : model.fuels) {
            fuel.Move();
            if (model.rocket.collideFuel(fuel)) {
                model.rocket.addFuel();
                mp.start();
                toRemoveFuel.add(fuel);
            }
            if (fuel.getPosition().y > this.resolution.y) {
                toRemoveFuel.add(fuel);
            }
        }

        //add enemies
        for (int i = 0; i < toRemove.size(); i++) {
            model.enemies.add(new Enemy(this));
        }

        //add fuels
        for (int i = 0; i < toRemoveFuel.size(); i++) {
            model.fuels.add(new Fuel(this));
        }

        model.enemies.removeAll(toRemove);
        model.fuels.removeAll(toRemoveFuel);

        model.rocket.addScore();
        model.counter++;

        if (model.rocket.getScore() > 1000 && model.rocket.getScore() < 1999) {
            if (model.addEnemy1) {
                model.addEnemy1 = false;
                model.enemies.add(new Enemy(this));
            }
        }
        if (model.rocket.getScore() > 2000 && model.rocket.getScore() < 2999) {
            if (model.addEnemy2) {
                model.addEnemy2 = false;
                model.enemies.add(new Enemy(this));
            }
        }
        if (model.rocket.getScore() > 3000 && model.rocket.getScore() < 3999) {
            if (model.addEnemy3) {
                model.addEnemy3 = false;
                model.enemies.add(new Enemy(this));
            }
        }
        if (model.rocket.getScore() > 4000) {
            if (model.addEnemy4) {
                model.addEnemy4 = false;
                model.enemies.add(new Enemy(this));
            }
        }

        if (model.counter > 100) {
            model.counter = 0;

            model.rocket.useFuel();
        }

        if (model.rocket.getFuel() == 0) {
            if (model.gameOver) {
                this.GameOver();
                model.gameOver = false;
            }
        }

    }

    private void GameOver() {
        model.mp2.stop();
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, this.playerName);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, model.rocket.getScore());

        // Insert the new row, returning the primary key value of the new row
        if (db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values) < 0) {
            Toast.makeText(getContext(), "chyba", Toast.LENGTH_LONG).show();
        }


        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("playerName", playerName);
        intent.putExtra("playerScore", model.rocket.getScore());
        getContext().startActivity(intent);
    }

    public Point getResolution() {
        return resolution;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        resolution = new Point(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);



        // draw
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        canvas.drawBitmap(mBackground, 0, 0, null); // pozadi
        canvas.drawText("Player: " + playerName + ",Score:  " + model.rocket.getScore() + " ,Fuel: " + model.rocket.getFuel(), 10, 25, paint);


        model.rocket.draw(canvas);
        for (Enemy enemy : model.enemies) {
            enemy.draw(canvas);
        }
        for (Fuel fuel : model.fuels) {
            fuel.draw(canvas);
        }

        invalidate();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
        if (model.mp2 == null) {
            model.mp2 = MediaPlayer.create(getContext(), R.raw.mcmc);
        }
        if (!model.mp2.isPlaying()) {
            model.mp2.start();
            model.mp2.setLooping(true);
        }
        if (model.rocket == null) {
            model.rocket = new Rocket(this);
        }
        //enemy = new Enemy(this);
        if (model.enemies.size() == 0) {
            model.enemies.add(new Enemy(this));
        }
        if (model.fuels.size() == 0) {
            model.fuels.add(new Fuel(this));
        }
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
        while (retry) {
            try {
                model.mp2.pause();
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

}
