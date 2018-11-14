package com.example.student.raketka;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private final String playerName;
    private GameThread gameThread;
    private Rocket rocket;


    private Point resolution;

    public GameSurface(Context context, String name) {
        super(context);
        this.playerName = name;

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update()  {

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
        paint.setTextSize(20);
        canvas.drawText(playerName, 10, 25, paint);


        rocket.draw(canvas);
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();

        rocket = new Rocket(this);
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
