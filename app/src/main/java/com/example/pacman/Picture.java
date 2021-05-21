package com.example.pacman;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


public class Picture
{
    public final int SIZEPACMAN = 40;
    public final int SIZEGHOST = 50;

    public Bitmap[] ghost,pacman;
    public Bitmap[] scare,food,endscare;
    public Bitmap background,eye,win,gameover;

    public Picture(Resources resources) {
        ghost = new Bitmap[3*4];

        ghost[0] = BitmapFactory.decodeResource(resources,R.drawable.ghost1);
        ghost[0] = Bitmap.createScaledBitmap(ghost[0],SIZEGHOST,SIZEGHOST,true);
        ghost[1] = BitmapFactory.decodeResource(resources,R.drawable.ghost2);
        ghost[1] = Bitmap.createScaledBitmap(ghost[1],SIZEGHOST,SIZEGHOST,true);
        ghost[2] = BitmapFactory.decodeResource(resources,R.drawable.ghost3);
        ghost[2] = Bitmap.createScaledBitmap(ghost[2],SIZEGHOST,SIZEGHOST,true);
        ghost[3] = BitmapFactory.decodeResource(resources,R.drawable.ghost6);
        ghost[3] = Bitmap.createScaledBitmap(ghost[3],SIZEGHOST,SIZEGHOST,true);
        ghost[4] = BitmapFactory.decodeResource(resources,R.drawable.ghost7);
        ghost[4] = Bitmap.createScaledBitmap(ghost[4],SIZEGHOST,SIZEGHOST,true);
        ghost[5] = BitmapFactory.decodeResource(resources,R.drawable.ghost7);
        ghost[5] = Bitmap.createScaledBitmap(ghost[5],SIZEGHOST,SIZEGHOST,true);

        ghost[6] = flipHor(ghost[0]);
        ghost[7] = flipHor(ghost[1]);
        ghost[8] = flipHor(ghost[2]);

        ghost[9] = BitmapFactory.decodeResource(resources,R.drawable.ghost4);
        ghost[9] = Bitmap.createScaledBitmap(ghost[9],SIZEGHOST,SIZEGHOST,true);
        ghost[10] = BitmapFactory.decodeResource(resources,R.drawable.ghost5);
        ghost[10] = Bitmap.createScaledBitmap(ghost[10],SIZEGHOST,SIZEGHOST,true);
        ghost[11] = BitmapFactory.decodeResource(resources,R.drawable.ghost5);
        ghost[11] = Bitmap.createScaledBitmap(ghost[11],SIZEGHOST,SIZEGHOST,true);

        scare = new Bitmap[3];
        scare[0] = BitmapFactory.decodeResource(resources,R.drawable.scare1);
        scare[0] = Bitmap.createScaledBitmap(scare[0],SIZEGHOST,SIZEGHOST,true);
        scare[1] = BitmapFactory.decodeResource(resources,R.drawable.scare2);
        scare[1] = Bitmap.createScaledBitmap(scare[1],SIZEGHOST,SIZEGHOST,true);
        scare[2] = BitmapFactory.decodeResource(resources,R.drawable.scare1);
        scare[2] = Bitmap.createScaledBitmap(scare[2],SIZEGHOST,SIZEGHOST,true);

        endscare = new Bitmap[3];
        endscare[0] = BitmapFactory.decodeResource(resources,R.drawable.white1);
        endscare[0] = Bitmap.createScaledBitmap(endscare[0],SIZEGHOST,SIZEGHOST,true);
        endscare[1] = BitmapFactory.decodeResource(resources,R.drawable.white2);
        endscare[1] = Bitmap.createScaledBitmap(endscare[1],SIZEGHOST,SIZEGHOST,true);
        endscare[2] = BitmapFactory.decodeResource(resources,R.drawable.white1);
        endscare[2] = Bitmap.createScaledBitmap(endscare[2],SIZEGHOST,SIZEGHOST,true);



        pacman = new Bitmap[5*4];
        pacman[0] = BitmapFactory.decodeResource(resources,R.drawable.pac0);
        pacman[0] = Bitmap.createScaledBitmap(pacman[0],SIZEPACMAN,SIZEPACMAN,true);
        pacman[1] = BitmapFactory.decodeResource(resources,R.drawable.pac1);
        pacman[1] = Bitmap.createScaledBitmap(pacman[1],SIZEPACMAN,SIZEPACMAN,true);
        pacman[2] = BitmapFactory.decodeResource(resources,R.drawable.pac2);
        pacman[2] = Bitmap.createScaledBitmap(pacman[2],SIZEPACMAN,SIZEPACMAN,true);
        pacman[3] = BitmapFactory.decodeResource(resources,R.drawable.pac3);
        pacman[3] = Bitmap.createScaledBitmap(pacman[3],SIZEPACMAN,SIZEPACMAN,true);
        pacman[4] = BitmapFactory.decodeResource(resources,R.drawable.pac4);
        pacman[4] = Bitmap.createScaledBitmap(pacman[4],SIZEPACMAN,SIZEPACMAN,true);

        pacman[5] = rotation(pacman[0],90);
        pacman[6] = rotation(pacman[1],90);
        pacman[7] = rotation(pacman[2],90);
        pacman[8] = rotation(pacman[3],90);
        pacman[9] = rotation(pacman[4],90);

        pacman[10] = flipHor(pacman[0]);
        pacman[11] = flipHor(pacman[1]);
        pacman[12] = flipHor(pacman[2]);
        pacman[13] = flipHor(pacman[3]);
        pacman[14] = flipHor(pacman[4]);

        pacman[15] = flipVer(pacman[5]);
        pacman[16] = flipVer(pacman[6]);
        pacman[17] = flipVer(pacman[7]);
        pacman[18] = flipVer(pacman[8]);
        pacman[19] = flipVer(pacman[9]);

        background = BitmapFactory.decodeResource(resources,R.drawable.map);
        background = Bitmap.createScaledBitmap(background,Control.PIXEL*28,Control.PIXEL*31,true);

        food = new Bitmap[4];
        food[0] = BitmapFactory.decodeResource(resources,R.drawable.food1);
        food[0] = Bitmap.createScaledBitmap(food[0],Control.PIXEL,Control.PIXEL,true);
        food[1] = BitmapFactory.decodeResource(resources,R.drawable.food2);
        food[1] = Bitmap.createScaledBitmap(food[1],Control.PIXEL,Control.PIXEL,true);
        food[2] = BitmapFactory.decodeResource(resources,R.drawable.food3);
        food[2] = Bitmap.createScaledBitmap(food[2],Control.PIXEL,Control.PIXEL,true);
        food[3] = BitmapFactory.decodeResource(resources,R.drawable.food4);
        food[3] = Bitmap.createScaledBitmap(food[3],Control.PIXEL,Control.PIXEL,true);

        eye = BitmapFactory.decodeResource(resources,R.drawable.eye);
        eye = Bitmap.createScaledBitmap(eye,Control.PIXEL*3/2,Control.PIXEL*3/2,true);

        win = BitmapFactory.decodeResource(resources,R.drawable.victory);
        win = Bitmap.createScaledBitmap(win,MainActivity.WIDTH,400,true);
        gameover = BitmapFactory.decodeResource(resources,R.drawable.gameover);
        gameover = Bitmap.createScaledBitmap(gameover,MainActivity.WIDTH,400,true);



    }

    public Bitmap flipHor(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(-1,1,bitmap.getWidth()/2,bitmap.getHeight()/2);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }
    public Bitmap flipVer(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(1,-1,bitmap.getWidth()/2,bitmap.getHeight()/2);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }
    public Bitmap rotation(Bitmap bitmap,float rotation){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }



}
