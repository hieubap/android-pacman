package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Food {
    public Bitmap[] bitmaps;
    public int x,y,setbitmap = 0;
    public boolean appear,eat = false;

    public Food(Bitmap[] bitmaps,int x,int y){
        appear = true;
        this.bitmaps = bitmaps;
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas){
        if (appear)
        canvas.drawBitmap(bitmaps[setbitmap],x,y,null);
        if (eat){
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setTextSize(30);
            canvas.drawText("1000",x-10,y+30,p);
        }
    }

    public void update(){
        appear = false;
        eat = true;
        setbitmap++;
        if (setbitmap == 4)
            setbitmap = 0;

    }

}
