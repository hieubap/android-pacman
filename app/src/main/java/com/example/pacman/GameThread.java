package com.example.pacman;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public final int SLEEP = 30;

    private boolean running;
    private Control gameSurface;
    private SurfaceHolder surfaceHolder;
    public boolean eat = false;

    public GameThread(Control gameSurface, SurfaceHolder surfaceHolder)  {
        this.gameSurface= gameSurface;
        this.surfaceHolder= surfaceHolder;
    }

    @Override
    public void run()  {
        long start = System.nanoTime();
        long timelate = 0;
        int count=0;
        while(running)  {
            Canvas canvas= null;
            start = System.currentTimeMillis();
            try {
                // Get Canvas from Holder and lock it.
                canvas = this.surfaceHolder.lockCanvas();
                // Synchronized
                synchronized (canvas)  {
                    this.gameSurface.draw(canvas);
                    this.gameSurface.update();

                    count++;
                    if (count == 1000/SLEEP){
                        gameSurface.timeplay ++;
                        count = 0;
                    }
                }
            }catch(Exception e)  {
                // Do nothing.
            } finally {
                if(canvas!= null)  {
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.currentTimeMillis();
            timelate = SLEEP - (now - start);
            if (timelate < 0){
                timelate = 1;
            }

            try {
                if (eat) {
                    this.sleep(800);
                    eat = false;
                }
                else
                    this.sleep(timelate);
            } catch(InterruptedException e)  {
            }
//            System.out.println(timelate);


        }
    }

    public void setRunning(boolean running)  {
        this.running= running;
    }
}