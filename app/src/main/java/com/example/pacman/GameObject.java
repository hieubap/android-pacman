package com.example.pacman;

import android.graphics.Canvas;

public abstract class GameObject {
    public int deltaPosition;
    public int x, y;

    public void update() {

    }

    public void draw(Canvas canvas) {

    }

    public void checkBorder() {
        if (this.x < Control.MAP_X - Control.PIXEL) this.x = Control.MAP_X + 28 * Control.PIXEL;
        else if (this.x > Control.MAP_X + 28 * Control.PIXEL)
            this.x = Control.MAP_X - Control.PIXEL;

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
