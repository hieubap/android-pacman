package com.example.pacman;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Control extends SurfaceView implements SurfaceHolder.Callback {
    public int WIDTH = MainActivity.WIDTH;
    public int HEIGHT = MainActivity.HEIGHT;
    public static final int PIXEL = 24;
    public static final int SPEED = 4;

    long timeplay = 0;
    private Rect up,down,left,right;
    private Pacman pm;
	private ArrayList<Ghost> ghost;
	private ArrayList<Point> powerfood;
	private Food food;
	public Picture picture;
	private Sound sound;
	private int streamid = 0;

	private Level lv;
	private GameThread main;
    private boolean endgame = false,danger = false,playsoundend = true;
    private MainActivity mainActivity;

    public Control(MainActivity context) {
        super(context);
        mainActivity = context;
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        picture = new Picture(this.getResources());
        sound = new Sound(context);
    }

    public void resetPosition(){
        int i=0;
        for (Ghost gh : ghost) {
            gh.setghost((12 + i) * PIXEL, 13 * PIXEL);
            i++;
        }
            pm.setposition(13*PIXEL,23*PIXEL);
    }
    public void setmodemove(Ghost.Modemove modemove){
        for (Ghost gh : ghost) {
            gh.setModemove(modemove);
        }
    }

    public void update() {
        if (!checkPower()) {

            if (timeplay % 100 == 20) {
                setmodemove(Ghost.Modemove.chase);
            } else if (timeplay % 100 == 40) {
                setmodemove(Ghost.Modemove.scatter);
            }
        }
        if (timeplay % 60 == 0){
            food.appear = true;
        }

        if(!endgame)
		{
		pm.move();
		pm.setscore(lv,sound);
//		sound.play(sound.eat);
            if (checkChase()){
                danger = true;
            }
            else{
                danger = false;
            }
            if (!checkPower()){

                pm.count = 0;
                if (sound.soundbg == sound.pac6){
                    sound.stop(streamid);
                    streamid = sound.playSoundBackground(sound.siren);
                }
            }

		for (Ghost gh : ghost){
		    gh.move();
		    if (gh.checkdead(pm.Pacmanx,pm.Pacmany)){
		        if (gh.modemove == Ghost.Modemove.scare){

		            pm.count++;
		            pm.score += pm.count*200;
		            gh.modemove = Ghost.Modemove.back;
		            gh.setPosition(PIXEL);
                    gh.eat = true;
                    sound.play(sound.eatghost);
                }
		        else if (gh.modemove != Ghost.Modemove.back && gh.modemove != Ghost.Modemove.scare){
		            main.eat = true;
		            pm.live --;
		            resetPosition();
                }
            }
		}

		for(Point point : powerfood){
		    Rect rect = new Rect(point.x,point.y,point.x+PIXEL,point.y + PIXEL );
		    if (rect.contains(pm.Pacmanx+PIXEL/2,pm.Pacmany+PIXEL/2)){

                for (Ghost gh : ghost){
                    gh.setModemove(Ghost.Modemove.scare);
                }

                sound.stop(streamid);
                streamid = sound.playSoundBackground(sound.pac6);
                powerfood.remove(point);
            };
        }

		if (food.appear){
		    Rect rect = new Rect(food.x,food.y,food.x+PIXEL,food.y+PIXEL);
		    if (rect.contains(pm.Pacmanx+PIXEL/2,pm.Pacmany+PIXEL/2)){
		        sound.play(sound.eatfruit);
                pm.score += 1000;
		        food.update();
            }
        }

		if(pm.live==0||pm.countdot == 0) endgame=true;

		}
	}
	@Override
	public void draw(Canvas g) {
	    super.draw(g);

        Paint p = new Paint();

        p.setColor(Color.WHITE);

        g.drawBitmap(picture.background,-5,-2,null);
		for(int y=0;y<30;y++)
			for(int x=0;x<28;x++) {
				if(lv.checkMap(y, x, 0)||lv.checkMap(y, x, 3)) {
					g.drawRect(x*PIXEL+15, y*PIXEL+15, x*PIXEL+25, y*PIXEL+25,p);
					}
			}

		p.setColor(Color.rgb(100,100,100));
		g.drawRect(up,p);
        g.drawRect(down,p);
        g.drawRect(left,p);
        g.drawRect(right,p);

        p.setTextSize(30);
        g.drawText("Time : ",WIDTH-,HEIGHT-30,p);
        pm.draw(g);
        g.drawText("Time : "+timeplay,WIDTH-,HEIGHT-30,p);

        for (Ghost gs : ghost){
            gs.draw(g);
            if (gs.eat){
                gs.drawscore(g,pm.count*200);
                gs.eat = false;
                main.eat = true;
            }
        }

        p.setColor(Color.WHITE);
        for(Point point: powerfood){
            g.drawRect(point.x+10,point.y+10,point.x+30,point.y + 30 , p);
        }
        food.draw(g);
        if (food.eat){
            food.eat = false;
            main.eat = true;
        }
        for(int i=0;i<pm.live;i++){
            g.drawBitmap(pm.image[2],i*(PIXEL+2),HEIGHT-PIXEL,null);
        }
        if (danger) {
            p.setColor(Color.RED);
            p.setTextSize(35);
            g.drawText("!!! DANGER !!!", 0, HEIGHT-PIXEL, p);
        }
        if (endgame){
            sound.stop(streamid);
            if (pm.live == 0) {
                g.drawBitmap(picture.gameover, 0, 200, null);
                if (playsoundend)
                {
                    playsoundend = false;
                    sound.play(sound.lose);
                }
            }
            else {
                g.drawBitmap(picture.win,0,200,null);
                if (playsoundend)
                {
                    playsoundend = false;
                    sound.play(sound.intermission);
                }
            }
            p.setColor(Color.YELLOW);
            g.drawText("Touch to restart game",20,1400,p);
        }
    }

	@Override
	public boolean onTouchEvent(MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            if (endgame){
                init(this.getHolder());
            }
	        int x = (int)e.getX();
	        int y = (int)e.getY();
	        if(up.contains(x,y)){
	            pm.setModemove(Pacman.movemode.UP);
	            return true;
            }
            if(down.contains(x,y)){
                pm.setModemove(Pacman.movemode.DOWN);
                return true;
            }
            if(left.contains(x,y)){
                pm.setModemove(Pacman.movemode.LEFT);
                return true;
            }
            if(right.contains(x,y)){
                pm.setModemove(Pacman.movemode.RIGHT);
                return true;
            }
        }
	    return false;
    }
    public boolean checkPower(){
        for(Ghost gh : ghost){
            if(gh.modemove == Ghost.Modemove.scare)
                return true;
        }
        return false;
    }
    public boolean checkChase(){
        for(Ghost gh : ghost){
            if(gh.modemove == Ghost.Modemove.chase)
                return true;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init(holder);

        main = new GameThread(this,holder);
        main.setRunning(true);
        main.start();
    }
    public void init(SurfaceHolder holder){
        endgame = false;danger = false;playsoundend = true;
        timeplay = 0;

        pm=new Pacman(picture.pacman);
        ghost = new ArrayList<Ghost>();

        if (streamid != 0)
        sound.stop(streamid);

        streamid = sound.playSoundBackground(sound.siren);

        int[] a = {1,1,1,29,26,1,26,29};

        for(int i=0;i<4;i++) {
            Ghost gh0 = new Ghost(pm, Ghost.Modemove.normal, (12+i) * PIXEL, 14 * PIXEL,a[i*2],a[i*2+1],
                    picture.ghost,picture.scare,picture.endscare,picture.eye);
            ghost.add(gh0);
        }
        setmodemove(Ghost.Modemove.scatter);

        powerfood = new ArrayList<Point>();
        powerfood.add(new Point(1*PIXEL,3*PIXEL));
        powerfood.add(new Point(1*PIXEL,28*PIXEL));
        powerfood.add(new Point(26*PIXEL,3*PIXEL));
        powerfood.add(new Point(26*PIXEL,28*PIXEL));
        food = new Food(picture.food,13*PIXEL,17*PIXEL);


        up = new Rect(100,750,200,850);
        down = new Rect(100,950,200,1050);
        left = new Rect(0,850,100,950);
        right = new Rect(200,850,300,950);


        lv=new Level();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("Change .......................");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.main.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.main.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }
}
