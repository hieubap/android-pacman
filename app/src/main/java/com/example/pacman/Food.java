package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Food extends GameObject {
    protected final Paint paint;
    public Bitmap[] bitmaps;
    public int setBitMap = 0;
    public boolean appear, eat = false;
    protected Pacman pacman;
    protected Control control;

    public Food(Control control, int x, int y) {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        this.control = control;
        this.bitmaps = control.picture.food;
        this.pacman = control.getPacman();
        this.x = x + Control.MAP_X;
        this.y = y + Control.MAP_Y;
        appear = true;
    }

    public void draw(Canvas canvas) {
        if (appear)
            canvas.drawBitmap(bitmaps[setBitMap], x, y, null);
        if (eat) {
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setTextSize(60);
            canvas.drawText("1000", x - 40, y + 20, p);
        }
    }

    public void update() {
        // sau 1p thì xuất hiện trở lại
        eat = false;
        if (control.timePlay % 60 == 0) {
            appear = true;
        }

        if (appear) {
            Rect rect = new Rect(x, y, x + Control.PIXEL, y + Control.PIXEL);
            if (rect.contains(pacman.x + Control.PIXEL / 2, pacman.y + Control.PIXEL / 2)) {
                control.getThread().delay();
                control.getSound().play(control.getSound().eatFruit);
                pacman.score += Control.SCORE_FOOD;

                appear = false;
                eat = true;
                setBitMap++;
                if (setBitMap == 4)
                    setBitMap = 0;
            }
        }
    }

}
