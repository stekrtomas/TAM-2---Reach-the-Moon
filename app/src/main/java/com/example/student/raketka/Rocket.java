package com.example.student.raketka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Rocket {

    private PointF position;
    private GameSurface gs;
    private Paint p;

    public Rocket(GameSurface gs) {
        this.gs = gs;
        position = new PointF(gs.getResolution().x / 2, gs.getResolution().y / 2);
    }

    public void draw(Canvas canvas) {

        p=new Paint();
        Bitmap b=BitmapFactory.decodeResource(gs.getResources(), R.drawable.raketa);
        p.setColor(Color.RED);
        int height = 100; // height in pixels
        int width = 50; // width in pixels
        Rect src = new Rect(0,0,b.getWidth()-1, b.getHeight()-1);
        Rect dest = new Rect(0,0,width-1, height-1);
        canvas.drawBitmap(b, src, dest, null);
        // canvas.drawBitmap(b, position.x, position.y, p);
        // canvas.drawBitmap(b, null, new RectF(0, 0, position.x, position.y), p);

        /*Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(255, 255, 0));
        canvas.drawRect(position.x, position.y, position.x + 100, position.y + 100, myPaint);*/
    }

}
