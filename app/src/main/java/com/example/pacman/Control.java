package com.example.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Control extends SurfaceView implements SurfaceHolder.Callback {
    // constant game
    public static final int SCORE_FOOD = 1000;
    public static final int SPEED_PER_PIXEL_SCARE = 16;

    // thời gian để thay đổi các trạng thái: (NORMAL, SCATTER, CHASE)
    public static final int TIME_CHANGE_MODE_MOVE = 50;
    // sợ hãi trong bao lâu (giây)
    public static final int TIME_SCARE = 20;
    // thời điểm chuyển sang sắp hết sợ hãi(giây)
    public static final int TIME_END_SCARE = 5;
    // đuổi trong thời gian bao lâu(giây)
    public static final int TIME_CHASE = 25;

    public static final int DEFAULT_ROW = 23;
    public static final int DEFAULT_COLUMN = 13;
    //-------------------------------------------------------


    // environment variable
    // setup with screen device in constructor
    public static int WIDTH;
    public static int HEIGHT;
    public static int PIXEL = 28;
    public static int SPEED = 4;
    public static int SPEED_SCARE = 1;
    public static int SPEED_BACK = 4;
    public static int MAP_X = 0;
    public static int MAP_Y = 0;
    public static int MAP_W = 0;
    public static int MAP_H = 0;
    public static int DOT_SIZE = 0;
    public static int POWER_SIZE = 0;
    public static int SIZE_GHOST = 0;
    public static int SIZE_PACMAN = 0;

    public static int BTN_X;
    public static int BTN_Y;
    public static int BTN_SIZE;

    //---------------------------------------
    private Paint paint;

    long timePlay = 0, countDownModeMove = 0; // giây
    private Rect up, down, left, right;
    private Pacman pacman;
    private ArrayList<Ghost> ghosts;
    private ArrayList<PowerFood> powerFoods;
    private Food food;
    public Picture picture;
    private Sound sound;
    public int soundId = 0;

    private Level level;
    private GameThread thread;
    private boolean endgame = false, danger = false, playsoundend = true;
    private MainActivity mainActivity;

    public Control(MainActivity context) {
        super(context);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WIDTH = (displayMetrics.widthPixels / 28) * 28;
        HEIGHT = (displayMetrics.heightPixels / 28) * 31;
        SPEED_SCARE = (WIDTH / 28) / SPEED_PER_PIXEL_SCARE;
        SPEED_BACK = 4 * SPEED_SCARE;
        SPEED = 2 * SPEED_SCARE;
        PIXEL = SPEED_PER_PIXEL_SCARE * SPEED_SCARE;
        MAP_W = PIXEL * 28;
        MAP_H = PIXEL * 31;
        MAP_X = (WIDTH - MAP_W) / 2;
        MAP_Y = (HEIGHT - MAP_H) / 3;
        DOT_SIZE = PIXEL / 10 * 3;
        POWER_SIZE = PIXEL / 10 * 8;
        SIZE_GHOST = PIXEL / 10 * 15;
        SIZE_PACMAN = PIXEL / 10 * 15;
        BTN_SIZE = (HEIGHT - MAP_H - 40) / 6;
        BTN_X = (WIDTH / 2 - BTN_SIZE * 3 / 2);
        BTN_Y = (MAP_Y + PIXEL * 31 + 10);

        System.out.println("SCREEN WIDTH = " + WIDTH + " PIXEL=" + PIXEL + " SPEED=" + SPEED + " SCARE=" + SPEED_SCARE + " BACK=" + SPEED_BACK);

        mainActivity = context;
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        picture = new Picture(this.getResources());
        sound = new Sound(context);
        paint = new Paint();

        up = new Rect(BTN_X + BTN_SIZE, BTN_Y, BTN_X + 2 * BTN_SIZE, BTN_Y + BTN_SIZE);
        down = new Rect(BTN_X + BTN_SIZE, BTN_Y + 2 * BTN_SIZE, BTN_X + 2 * BTN_SIZE, BTN_Y + 3 * BTN_SIZE);
        left = new Rect(BTN_X, BTN_Y + BTN_SIZE, BTN_X + BTN_SIZE, BTN_Y + 2 * BTN_SIZE);
        right = new Rect(BTN_X + 2 * BTN_SIZE, BTN_Y + BTN_SIZE, BTN_X + 3 * BTN_SIZE, BTN_Y + 2 * BTN_SIZE);
    }

    public void resetPosition() {
        int i = 0;
        for (Ghost gh : ghosts) {
            gh.setPosition(Control.MAP_X + (12 + i) * PIXEL, Control.MAP_Y + 13 * PIXEL);
            i++;
        }
        pacman.setposition(Control.MAP_X + 13 * PIXEL, Control.MAP_Y + 23 * PIXEL);
    }

    public void setModeMove(Ghost.ModeMove modeMove) {
        System.out.println("Control.setModeMove ..." + modeMove);
        for (Ghost gh : ghosts) {
            gh.setModeMove(modeMove);
        }
    }

    public void updateModeMove() {
    }

    public void updatePerSecond() {
        timePlay++;
        Ghost.ModeMove mode = getGhostModeMove();
        if (mode == Ghost.ModeMove.CHASE) {
            countDownModeMove--;
            if (countDownModeMove == 0) {
                setModeMove(Ghost.ModeMove.SCATTER);
            }
        } else if (mode != Ghost.ModeMove.BACK &&
                mode != Ghost.ModeMove.SCARE &&
                timePlay % TIME_CHANGE_MODE_MOVE == 0) {
            // không phải rượt đuổi
            setModeMove(Ghost.ModeMove.CHASE);
            countDownModeMove = TIME_CHASE;
        }
    }

    public void update() {
        if (!endgame) {
            updateModeMove();
            pacman.move();
            pacman.setScore(level, sound);
            if (checkChase()) {
                danger = true;
            } else {
                danger = false;
            }
            if (!checkPower()) {
                pacman.count = 0;
                if (sound.soundBg == sound.pac6) {
                    sound.stopId(soundId);
                    soundId = sound.playSoundBackground(sound.siren);
                }
            }

            for (Ghost gh : ghosts) {
                gh.update();
            }

            for (PowerFood powerFood : powerFoods) {
                powerFood.update();
            }
            food.update();

            if (pacman.live == 0 || pacman.countDot == 0) endgame = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint.setColor(Color.WHITE);
        picture.drawBackground(canvas);

        for (int y = 0; y < 30; y++)
            for (int x = 0; x < 28; x++) {
                if (level.checkMap(y, x, 0) || level.checkMap(y, x, 3)) {
                    canvas.drawRect(MAP_X + x * PIXEL + PIXEL / 2 - DOT_SIZE / 2,
                            MAP_Y + y * PIXEL + PIXEL / 2 - DOT_SIZE / 2,
                            MAP_X + x * PIXEL + PIXEL / 2 + DOT_SIZE / 2,
                            MAP_Y + y * PIXEL + PIXEL / 2 + DOT_SIZE / 2, paint);
                }
            }

        // nút điều khiển
        paint.setColor(Color.rgb(100, 100, 100));
        canvas.drawRect(up, paint);
        canvas.drawRect(down, paint);
        canvas.drawRect(left, paint);
        canvas.drawRect(right, paint);

        // số mạng còn lại
        for (int i = 0; i < pacman.live; i++) {
            canvas.drawBitmap(pacman.image[2],
                    WIDTH - MAP_X - (i + 1) * (SIZE_PACMAN + 10),
                    (int) (WIDTH * 0.3) + SIZE_PACMAN, null);
        }

        paint.setTextSize(40);
        canvas.drawText("Time : " + timePlay, MAP_X, (int) (WIDTH * 0.3), paint);
        Ghost.ModeMove mode = getGhostModeMove();
        /**
         * scatter: gray
         * normal: green
         * chase: red
         * scare: blue
         */
        if (mode != Ghost.ModeMove.SCATTER)
            paint.setColor(mode == Ghost.ModeMove.CHASE
                    ? Color.RED : mode == Ghost.ModeMove.SCARE
                    ? Color.BLUE : Color.GREEN);
        canvas.drawText(getGhostModeMove().toString(), WIDTH - MAP_X - 160, (int) (WIDTH * 0.3), paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score : " + pacman.score, MAP_X, (int) (WIDTH * 0.3) + 60, paint);

        for (PowerFood powerFood : powerFoods) {
            powerFood.draw(canvas);
        }

        for (int i = 0; i < pacman.live; i++) {
            canvas.drawBitmap(pacman.image[2], i * (PIXEL + 2), HEIGHT - PIXEL, null);
        }

        pacman.draw(canvas);
        food.draw(canvas);
        for (Ghost gs : ghosts) {
            gs.draw(canvas);
        }

        paint.setColor(Color.WHITE);
        if (endgame) {
            sound.stopId(soundId);
            if (pacman.live == 0) {
                canvas.drawBitmap(picture.gameover, MAP_X, (int) ((HEIGHT - MAP_H) / 2), null);
                if (playsoundend) {
                    playsoundend = false;
                    sound.play(sound.lose);
                }
            } else {
                canvas.drawBitmap(picture.win, MAP_X, (int) ((HEIGHT - MAP_H) / 2), null);
                if (playsoundend) {
                    playsoundend = false;
                    sound.play(sound.intermission);
                }
            }
            paint.setColor(Color.YELLOW);
            paint.setTextSize(60);
            canvas.drawText("Touch to restart game",
                    (int) ((WIDTH - MAP_W) + 50),
                    (int) ((HEIGHT + MAP_H) / 3), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (endgame) {
                init(this.getHolder());
            }
            int x = (int) e.getX();
            int y = (int) e.getY();
            if (up.contains(x, y)) {
                pacman.setModeMove(Pacman.ModeMove.UP);
                return true;
            }
            if (down.contains(x, y)) {
                pacman.setModeMove(Pacman.ModeMove.DOWN);
                return true;
            }
            if (left.contains(x, y)) {
                pacman.setModeMove(Pacman.ModeMove.LEFT);
                return true;
            }
            if (right.contains(x, y)) {
                pacman.setModeMove(Pacman.ModeMove.RIGHT);
                return true;
            }
        }
        return false;
    }

    public boolean checkPower() {
        for (Ghost gh : ghosts) {
            if (gh.modemove == Ghost.ModeMove.SCARE)
                return true;
        }
        return false;
    }

    public Ghost.ModeMove getGhostModeMove() {
        boolean scare = false;
        boolean scatter = false;
        for (Ghost gh : ghosts) {
            if (gh.modemove == Ghost.ModeMove.CHASE)
                return Ghost.ModeMove.CHASE;
            if (gh.modemove == Ghost.ModeMove.SCARE)
                scare = true;
            if (gh.modemove == Ghost.ModeMove.SCATTER)
                scatter = true;
        }
        if (scare) return Ghost.ModeMove.SCARE;
        if (scatter) return Ghost.ModeMove.SCATTER;
        return Ghost.ModeMove.NORMAL;
    }

    public boolean checkChase() {
        for (Ghost gh : ghosts) {
            if (gh.modemove == Ghost.ModeMove.CHASE)
                return true;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init(holder);

        thread = new GameThread(this, holder);
        thread.setRunning(true);
        thread.start();
    }

    public void init(SurfaceHolder holder) {
        picture = new Picture(this.getResources());
        level = new Level();

        endgame = false;
        danger = false;
        playsoundend = true;
        timePlay = 0;

        pacman = new Pacman(picture.pacman);
        ghosts = new ArrayList<>();

        sound.play(sound.intermission);

        soundId = sound.playSoundBackground(sound.siren);

        int[] a = {1, 1, 1, 29, 26, 1, 26, 29};

        for (int i = 0; i < 4; i++) {
            Ghost gh0 = new Ghost(this, (12 + i), 14, a[i * 2], a[i * 2 + 1]);
            ghosts.add(gh0);
        }

        powerFoods = new ArrayList<>();
        powerFoods.add(new PowerFood(this, 1, 3));
        powerFoods.add(new PowerFood(this, 1, 28));
        powerFoods.add(new PowerFood(this, 26, 3));
        powerFoods.add(new PowerFood(this, 26, 28));
        food = new Food(this, 13 * PIXEL, 17 * PIXEL);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("Change .......................");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                this.thread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = true;
        }
    }

    public Pacman getPacman() {
        return pacman;
    }

    public Sound getSound() {
        return sound;
    }

    public GameThread getThread() {
        return thread;
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public ArrayList<PowerFood> getPowerFoods() {
        return powerFoods;
    }

    public Level getLevel() {
        return level;
    }

}
