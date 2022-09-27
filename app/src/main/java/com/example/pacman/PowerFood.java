package com.example.pacman;

import android.graphics.Canvas;
import android.graphics.Rect;

public class PowerFood extends Food {
    public int left, top, right, bottom;

    // x,y là vị trí trên bản đồ
    public PowerFood(Control control, int column, int row) {
        super(control, column, row);
        deltaPosition = Control.PIXEL / 2 - Control.POWER_SIZE / 2;
        this.x = Control.MAP_X + column * Control.PIXEL;
        this.y = Control.MAP_Y + row * Control.PIXEL;
        left = this.x + deltaPosition;
        top = this.y + deltaPosition;
        right = this.x + deltaPosition + Control.POWER_SIZE;
        bottom = this.y + deltaPosition + Control.POWER_SIZE;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public void update() {
        Rect rect = new Rect(pacman.x, pacman.y, pacman.x + Control.SIZE_PACMAN, pacman.y + Control.SIZE_PACMAN);
        if (rect.contains(x,y)) {
            control.setModeMove(Ghost.ModeMove.SCARE);

            control.getSound().stop(control.soundId);
            control.soundId = control.getSound().playSoundBackground(control.getSound().pac6);
            control.getPowerFoods().remove(this);
        }
    }
}
