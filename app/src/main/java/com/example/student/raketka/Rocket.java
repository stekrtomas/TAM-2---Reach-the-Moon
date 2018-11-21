package com.example.student.raketka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;


public class Rocket {

    private PointF position;
    private GameSurface gs;
    private Bitmap rocket;
    private Point imgResolution;


    public Rocket(GameSurface gs) {
        this.gs = gs;
        rocket = BitmapFactory.decodeResource(gs.getResources(), R.drawable.raketa);
        imgResolution = new Point(rocket.getWidth(), rocket.getHeight());
        position = new PointF((gs.getResolution().x / 2) - (imgResolution.x / 2), gs.getResolution().y - (imgResolution.y + 50));
    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(rocket, position.x, position.y, null);

    }


    public void Move(float rocketAngle) {
        if (rocketAngle > 10) {
            if (position.x < gs.getResolution().x - imgResolution.x)
                position.x += 11;
        } else if (rocketAngle < -10) {
            if (position.x > 0)
                position.x -= 11;
        }

    }
}
