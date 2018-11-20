package com.example.student.raketka;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;


public class Rocket {

    private PointF position;
    private GameSurface gs;
    private Paint p;


    public Rocket(GameSurface gs) {
        this.gs = gs;
        position = new PointF(gs.getResolution().x / 2, gs.getResolution().y - 100);

    }

    public void draw(Canvas canvas) {

        /*p=new Paint();
        Bitmap b=BitmapFactory.decodeResource(gs.getResources(), R.drawable.raketa);
        p.setColor(Color.RED);
        int height = 100; // height in pixels
        int width = 50; // width in pixels
        Rect src = new Rect(0,0,b.getWidth()-1, b.getHeight()-1);
        Rect dest = new Rect(0,0,width-1, height-1);
        canvas.drawBitmap(b, src, dest, null);*/
        // canvas.drawBitmap(b, position.x, position.y, p);
        // canvas.drawBitmap(b, null, new RectF(0, 0, position.x, position.y), p);

        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(69, 69, 69));
        canvas.drawRect(position.x, position.y, position.x + 100, position.y + 100, myPaint);
    }


    public void Move(float rocketAngle) {
        if (rocketAngle > 30) {
            if (position.x < gs.getResolution().x - 100)
                position.x += 11;
        } else if (rocketAngle < -30) {
            if (position.x > 0)
                position.x -= 11;
        }
        /*else if(Math.abs(rocketAngle) < 10) {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }*/
    }
}
