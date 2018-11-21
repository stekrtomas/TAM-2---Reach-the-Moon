package com.example.student.raketka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

public class Enemy {

    private PointF position;
    private GameSurface gs;
    private Bitmap enemy;
    private Point imgResolution;
    private boolean up;
    private int speed;
    private Rect collisionBox;


    public Enemy(GameSurface gs) {
        this.gs = gs;
        enemy = BitmapFactory.decodeResource(gs.getResources(), R.drawable.meteor);
        speed = 10;
        imgResolution = new Point(enemy.getWidth(), enemy.getHeight());
        position = new PointF(gs.getResolution().x / 2, gs.getResolution().y / 2);
        collisionBox = new Rect(gs.getResolution().x / 2, gs.getResolution().y / 2, gs.getResolution().x / 2 + imgResolution.x, gs.getResolution().y / 2 + imgResolution.y);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(enemy, position.x, position.y, null);

    }

    public void Move() {
        if (position.y > gs.getResolution().y) {
            up = false;

        } else if (position.y < 0) {
            up = true;
        }
        if (up) {
            position.y += speed;
        } else {
            position.y -= speed;
        }

    }

}
