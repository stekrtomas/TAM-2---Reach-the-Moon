package com.example.student.raketka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;


public class Rocket {
    private static final int MAX_ANGLE = 45;
    private static final int SPEED = 11;
    private PointF position;
    private GameSurface gs;
    private Bitmap rocket;
    private Point imgResolution;
    private RectF collisionBox;
    private int score;
    private int fuel;


    public Rocket(GameSurface gs) {

        this.gs = gs;
        fuel = 100;
        rocket = BitmapFactory.decodeResource(gs.getResources(), R.drawable.raketa);
        imgResolution = new Point(rocket.getWidth(), rocket.getHeight());
        position = new PointF((gs.getResolution().x / 2) - (imgResolution.x / 2), gs.getResolution().y - (imgResolution.y + 50));
        collisionBox = new RectF(position.x, position.y, position.x + imgResolution.x, position.y + imgResolution.y);
    }

    public void draw(Canvas canvas) {
        /*//COLLISION BOX TEST
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(69, 69, 69));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(collisionBox, myPaint);*/


        canvas.drawBitmap(rocket, position.x, position.y, null);

    }


    public void Move(float rocketAngle) {
//        if (rocketAngle > 10) {
//            if (position.x < gs.getResolution().x - imgResolution.x)
//                position.x += speed;
//        } else if (rocketAngle < -10) {
//            if (position.x > 0)
//                position.x -= speed;
//        }

        position.x += Math.min(SPEED, rocketAngle / MAX_ANGLE * SPEED);

        if (position.x > gs.getResolution().x - imgResolution.x) {
            position.x = gs.getResolution().x - imgResolution.x;
        }

        if (position.x < 0) {
            position.x = 0;
        }

        collisionBox.top = position.y;
        collisionBox.bottom = position.y + imgResolution.y;
        collisionBox.left = position.x;
        collisionBox.right = position.x + imgResolution.x;
    }

    public boolean collideEnemy(Enemy enemy) {
        return this.collisionBox.intersect(enemy.getCollisionBox());
    }

    public boolean collideFuel(Fuel fuel) {
        return this.collisionBox.intersect(fuel.getCollisionBox());
    }

    public void addScore() {
        score += 1;

    }

    public void addFuel() {
        score += 100;
        fuel = 100;
    }

    public void useFuel() {
        this.fuel -= 10;
    }

    public int getScore() {
        return this.score;
    }

    public int getFuel() {
        return this.fuel;
    }

    public void GameOver() {
        score = 0;
        fuel = 0;
    }
}
