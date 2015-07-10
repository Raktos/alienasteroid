package com.titangaming.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.titangaming.alienasteroid.R;

/**
 * Created by Jason on 7/10/2015.
 * Main GameBoard controller
 */

public class GameBoard extends View {

    private class Star{
        int alpha;
        int fade = 2;
        Point point;

        public Star(int x, int y, int alpha){
            this.point = new Point(x, y);
            this.alpha = alpha;
        }
    }

    //region variables
    //starfield variables
    private List<Star> starField = null;
    private static final int MAX_NUM_OF_STARS = 51;
    private static final int MIN_NUM_OF_STARS = 10;
    private int numStars;
    private Paint p;

    //sprite variables
    private Rect sprite1Bounds = new Rect(0,0,0,0);
    private Rect sprite2Bounds = new Rect(0,0,0,0);
    private Point sprite1;
    private Point sprite2;

    //Bitmaps that hold the actual sprite images
    private Bitmap bm1 = null;
    private Bitmap bm2 = null;
    //endregion

    //region sprite getters and setters
    //sprite 1 setter
    synchronized public void setSprite1(Point p) {
        sprite1 = p;
    }

    //sprite 1 getter
    synchronized public Point getSprite1() {
        return sprite1;
    }

    //sprite 2 setter
    synchronized public void setSprite2(Point p) {
        sprite2 = p;
    }

    //sprite 2 getter
    synchronized public Point getSprite2() {
        return sprite2;
    }

    //expose sprite bounds to controller
    synchronized public int getSprite1Width() {
        return sprite1Bounds.width();
    }

    synchronized public int getSprite1Height() {
        return sprite1Bounds.height();
    }

    synchronized public int getSprite2Width() {
        return sprite2Bounds.width();
    }


    synchronized public int getSprite2Height() {
        return sprite2Bounds.height();
    }
    //endregion

    synchronized public void resetStarField() {
        starField = null;
    }

    public GameBoard(Context context, AttributeSet aSet/*, int defStyle*/) {
        super(context, aSet/*, defStyle*/);

        //it's best not to create any new objects in the on draw
        //initialize them as class variables here

        p = new Paint();

        //load our bitmaps and set the bounds for the controller
        sprite1 = new Point(-1,-1);
        sprite2 = new Point(-1,-1);
        bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
        bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.alien);

        //set sprite sizes
        sprite1Bounds = new Rect(0,0, bm1.getWidth(), bm1.getHeight());
        sprite2Bounds = new Rect(0,0, bm2.getWidth(), bm2.getHeight());
    }

    private void initializeStars (int maxX, int maxY){
        Random r = new Random();

        starField = new ArrayList<>(); //going to be an array of Star objects
        numStars = r.nextInt(MAX_NUM_OF_STARS - MIN_NUM_OF_STARS) + MIN_NUM_OF_STARS;

        for(int i = 0; i < numStars; i++){
            int x = r.nextInt(maxX - 5 + 1) + 5;
            int y = r.nextInt(maxY - 5 + 1) + 5;
            int alpha = r.nextInt(252 - 80) + 80;
            starField.add(new Star(x, y, alpha));
        }
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        //create black canvas
        p.setColor(Color.BLACK);
        p.setAlpha(255);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        //initialize the starfield if necessary
        if(starField == null){
            initializeStars(canvas.getWidth(), canvas.getHeight());
        }

        p.setStrokeWidth(5);
        p.setColor(Color.CYAN);

        //draw the stars
        for(int i = 0; i < numStars; i++){
            p.setAlpha(starField.get(i).alpha += starField.get(i).fade);

            //fade them in and out
            if(starField.get(i).alpha >= 252 || starField.get(i).alpha <= 80){
                starField.get(i).fade *= -1;
            }
            canvas.drawPoint(starField.get(i).point.x, starField.get(i).point.y, p);
        }

        //draw the sprites Items drawn in this function are stacked.
        //The items drawn at the top of the loop are on the bottom of the z-order.
        //Therefore we draw our set, then our actors, and finally any fx.
        if(sprite1.x >= 0){
            canvas.drawBitmap(bm1, sprite1.x, sprite1.y, null);
        }
        if(sprite2.x >= 0){
            canvas.drawBitmap(bm1, sprite2.x, sprite2.y, null);
        }
    }
}
