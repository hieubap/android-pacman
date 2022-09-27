package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Ghost extends GameObject {
    public final int PIXEL = Control.PIXEL;
    public int SPEED = Control.SPEED;
    public int TIME_END_SCARE = Control.TIME_END_SCARE * GameThread.FPS;
    public int TIME_SCARE = Control.TIME_SCARE * GameThread.FPS;
    public static int deltaPosition = 0;

    enum ModeMove {NORMAL, CHASE, SCARE, SCATTER, BACK}

    public ModeMove modemove;

    public int scatterX, countDownScare = TIME_SCARE;
    public int scatterY;

    public Bitmap[] image, imageScare, imageEndScare;
    public Bitmap eye;
    public int animateIndex = 0, swapScareCount = 0;// animation
    public int direction = 0;// 0: up, 1: right, 2: down, 3: left, 4: dont move
    public Level level;
    private final Finder finder;
    private final Pacman pacman;
    public boolean endScare = false, eat = false;
    public Control control;

    public Ghost(Control control, int mapPosX, int mapPosY, int sx, int sy) {
        System.out.println(TIME_SCARE + " TIME_SCARE");
        this.control = control;
        this.level = control.getLevel();
        this.pacman = control.getPacman();
        finder = new Finder(level);

        this.imageEndScare = control.picture.endscare;
        this.image = control.picture.ghost;
        this.imageScare = control.picture.scare;
        this.eye = control.picture.eye;

        this.x = Control.MAP_X + mapPosX * Control.PIXEL;
        this.y = Control.MAP_Y + mapPosY * Control.PIXEL;
        this.scatterX = sx;
        this.scatterY = sy;
        this.direction = 0;
        this.modemove = ModeMove.SCATTER;
        deltaPosition = Control.PIXEL / 2 - Control.SIZE_GHOST / 2;
    }

    public void setScareBitmap(Bitmap[] a) {
        imageScare = new Bitmap[3];
        imageScare = a;
    }

    public void move() {
        animateIndex++;
        if (animateIndex == 3)
            animateIndex = 0;

        switch (direction) {
            case 0:
                y -= SPEED;
                break;
            case 1:
                x += SPEED;
                break;
            case 2:
                y += SPEED;
                break;
            case 3:
                x -= SPEED;
                break;
        }
    }

    public void getDirection(int xInMap, int yInMap) {

        if(xInMap/PIXEL==1&&yInMap/PIXEL<=1) {
            System.out.println("here");
        };

        switch (modemove) {
            case NORMAL: {
                normalMoving();
                break;
            }
            case CHASE: {
                if (xInMap % PIXEL == 0 && yInMap % PIXEL == 0) {
                    int column = xInMap / PIXEL;
                    int row = yInMap / PIXEL;

                    int m = finder.getMove(column, row,
                            (pacman.x - Control.MAP_X) / PIXEL,
                            (pacman.y - Control.MAP_Y) / PIXEL);
                    if (m != 4) direction = m;
                }
                break;
            }
            case SCARE: {
                normalMoving();

                countDownScare--;
                if (countDownScare <= TIME_END_SCARE) {
                    swapScareCount++;
                    if (swapScareCount == 10) {
                        endScare = !endScare;
                        swapScareCount = 0;
                    }
                }
                if (countDownScare == 0) {
                    countDownScare = TIME_SCARE;
                    setModeMove(ModeMove.NORMAL);
                    endScare = false;
                }
                break;
            }
            case SCATTER: {
                if ((xInMap % PIXEL == 0 && yInMap % PIXEL == 0)) {
                    int column = xInMap / PIXEL;
                    int row = yInMap / PIXEL;

                    if (column == scatterX && row == scatterY) {
                        setModeMove(ModeMove.NORMAL);
                        findRandomDirection();
                        return;
                    } else {
                        direction = finder.getMove(column, row, scatterX, scatterY);
                    }
                }
                break;
            }
            case BACK: {
                if (xInMap % PIXEL == 0 && yInMap % PIXEL == 0) {


                    int column = xInMap / PIXEL;
                    int row = yInMap / PIXEL;

                    direction = finder.getMove(column, row, 13, 14);

                    if (column == 13 && row == 14) {
                        setModeMove(ModeMove.NORMAL);
                        return;
                    }
                }

                break;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        int xInMap = x - Control.MAP_X;
        int yInMap = y - Control.MAP_Y;

        getDirection(xInMap, yInMap);
        move();
//        System.out.println(SPEED+"_direction");

        if (eat) {
            eat = false;
        }
        if (isCollision(pacman.x, pacman.y)) {
            if (modemove == ModeMove.SCARE) {
                pacman.count++;
                pacman.score += pacman.count * 200;
                setModeMove(ModeMove.BACK);
                control.getSound().play(control.getSound().eatGhost);
                control.getThread().delay();
                eat = true;
            } else if (modemove != ModeMove.BACK) {
				control.getThread().delay();
				pacman.live --;
				control.resetPosition();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int positionX = x + deltaPosition;
        int positionY = y + deltaPosition;

        if (modemove == ModeMove.SCARE) {
            if (endScare) {
                canvas.drawBitmap(imageEndScare[animateIndex], positionX, positionY, null);
            } else
                canvas.drawBitmap(imageScare[animateIndex], positionX, positionY, null);
        } else if (modemove == ModeMove.BACK) {
            if (eat) {
                drawScore(canvas);
            } else
                canvas.drawBitmap(eye, x - PIXEL / 4, y - PIXEL / 4, null);
        } else
            canvas.drawBitmap(image[animateIndex + (direction % 4) * 3], positionX, positionY, null);
    }

    public void drawScore(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(35);
        canvas.drawText("" + pacman.count * 200, x - 10, y + 30, paint);
    }

    // mode: NORMAL
    public void normalMoving() {
        int xInMap = x - Control.MAP_X;
        int yInMap = y - Control.MAP_Y;

        int column = xInMap / PIXEL + 1;
        int row = yInMap / PIXEL;

        if (xInMap % PIXEL == 0)
            column--;

        if ((xInMap % PIXEL != 0) || (yInMap % PIXEL != 0)) return;

        // check đi ra ngoài biên
        if (row == 14) {
            checkBorder();
        }

        if (level.checkMap(row, column, 3)) {
            // random khi ở ngã ba, ngã tư hoặc đang đứng yên
            findRandomDirection();
        } else {
            // random khi gặp tường và đứng yên
            switch (direction) {
                case 0:
                    if (level.checkMap(row - 1, column, 1)) findRandomDirection();
                    break;
                case 1:
                    if (level.checkMap(row, column + 1, 1)) findRandomDirection();
                    break;
                case 2:
                    if (level.checkMap(row + 1, column, 1)) findRandomDirection();
                    break;
                case 3:
                    if (level.checkMap(row, column - 1, 1)) findRandomDirection();
                    break;
                case 4:
                    findRandomDirection();
            }
        }
    }

    // mode: NORMAL
    public void findRandomDirection() {
        int count = 0;
        int ignoreDirection = 0;
        int xInMap = x - Control.MAP_X;
        int yInMap = y - Control.MAP_Y;

        int column = xInMap / PIXEL;
        int row = yInMap / PIXEL;
        //boolean point=(GhostX%20==0)&&(GhostY%20==0);

        if (direction == 1) ignoreDirection = 3;
        else if (direction == 3) ignoreDirection = 1;
//        else if (direction == 2) ignoreDirection = 0;
        else if (direction == 0) ignoreDirection = 2;

        int[] dir = {0, 0, 0, 0};

        for (int i = 0; i < 4; i++) {
            if(ignoreDirection == i) continue;
            boolean check = false;
            switch (i) {
                case 0:
                    if (level.canRun(column, row - 1, 1)) check = true;
                    break;
                case 1:
                    if (level.canRun(column + 1, row, 1)) check = true;
                    break;
                case 2:
                    if (level.canRun(column, row + 1, 1)) check = true;
                    break;
                case 3:
                    if (level.canRun(column - 1, row, 1)) check = true;
                    break;
            }
            if (check) {
                dir[count] = i;
                count++;
            }
        }

        Random r = new Random();
        int ch = Math.abs(r.nextInt()) % count;

        direction = dir[ch];
    }

    public boolean isCollision(int x, int y) {
        return x - PIXEL <= this.x && this.x <= x + PIXEL && y - PIXEL <= this.y && this.y <= y + PIXEL;
    }

    public void setModeMove(ModeMove modemove) {
        System.out.println("Ghost.setModeMove = " + modemove);
        this.modemove = modemove;
//        this.direction = 4;
        switch (modemove) {
            case NORMAL:
            case CHASE:
            case SCATTER:
                setDirectlyPosition(Control.SPEED);
                SPEED = Control.SPEED;
                break;
            case BACK:
                setDirectlyPosition(Control.SPEED_BACK);
                SPEED = Control.SPEED_BACK;
                break;
            case SCARE: {
                setDirectlyPosition(Control.SPEED_SCARE);
                SPEED = Control.SPEED_SCARE;
                countDownScare = TIME_SCARE;
            }
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDirectlyPosition(int a) {
        System.out.println("Ghost.setDirectlyPosition()");
        int xInMap = x - Control.MAP_X;
        int yInMap = y - Control.MAP_Y;

        if (xInMap % a != 0 || yInMap % a != 0) {
            this.x -= (xInMap % a);
            this.y -= (yInMap % a);
        }
    }
}
