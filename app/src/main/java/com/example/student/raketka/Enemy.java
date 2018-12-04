package com.example.student.raketka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

public class Enemy {

    private PointF position;
    private GameSurface gs;
    private Bitmap enemy;
    private Point imgResolution;
    private int speed;
    private RectF collisionBox;
    private Random random;


    public Enemy(GameSurface gs) {
        this.gs = gs;
        enemy = BitmapFactory.decodeResource(gs.getResources(), R.drawable.meteor);
        random = new Random();
        speed = 5;
        imgResolution = new Point(enemy.getWidth(), enemy.getHeight());
        position = new PointF(random.nextInt(gs.getResolution().x - imgResolution.x), 0 - imgResolution.y);
        collisionBox = new RectF(position.x, position.y, position.x + imgResolution.x, position.y + imgResolution.y);
    }

    public void draw(Canvas canvas) {
        /*//COLLISION BOX TEST
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(69, 69, 69));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(collisionBox, myPaint);*/
        canvas.drawBitmap(enemy, position.x, position.y, null);

    }

    public void Move() {
        position.y += speed;

        collisionBox.top = position.y;
        collisionBox.bottom = position.y + imgResolution.y;
        collisionBox.left = position.x;
        collisionBox.right = position.x + imgResolution.x;
    }

    public PointF getPosition() {
        return this.position;
    }

    public RectF getCollisionBox() {
        return this.collisionBox;
    }
}
