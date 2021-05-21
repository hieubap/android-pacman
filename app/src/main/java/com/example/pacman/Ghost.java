package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

//import javax.swing.JPanel;

public class Ghost {
	public final int PIXEL = Control.PIXEL;
	public int SPEED = Control.SPEED;
	public int TIMESCARE = 700;
	enum Modemove {normal,chase,scare,scatter,back};
	public Modemove modemove = Modemove.scare;
	public int GhostX,scatterx,countdown = 600;
	public int GhostY,scattery;

	public Bitmap image[],imagescare[],endscare[];
	public Bitmap eye;
	public int c = 0,cscare = 0,direction = 0;// animation
	public int mode=0;
	public Level lv = new Level();
	private Finder finder;
	private Pacman pacman;
	public boolean eat = false,escare = false;

	public Ghost(Pacman pacman,Modemove modemove,int x,int y,int sx,int sy,Bitmap[] bitmaps,Bitmap[] scare,Bitmap[] endscare,Bitmap eye){
		this.eye = eye;
		this.endscare = endscare;
		image = bitmaps;
		this.GhostX=x;
		this.GhostY=y;
		this.mode = 1;
		this.modemove = Modemove.scare;
		this.imagescare = scare;
		this.scatterx = sx;
		this.scattery = sy;
		this.pacman = pacman;
		this.modemove = modemove;
		finder = new Finder(lv);


	}

	public void setScareBitmap(Bitmap[] a){
		imagescare = new Bitmap[3];
		imagescare = a;
	}

	public void move() {
		c++;
		if(c == 3)
			c = 0;
		SPEED = Control.SPEED;

		switch (modemove){
			case normal:{
				if (!lv.checkborder(GhostX / PIXEL, GhostY / PIXEL))
					check();
				else
					checkBorder();
				break;
			}
			case chase:{
				if(GhostX%PIXEL == 0 && GhostY%PIXEL == 0) {

					int Ghostx = GhostX / PIXEL;
					int Ghosty = GhostY / PIXEL;

					int m = finder.getMove(Ghostx, Ghosty, pacman.Pacmanx / PIXEL, pacman.Pacmany / PIXEL);
					if (m!= 0) mode = m;
				}
				break;
			}
			case scare:{
				SPEED = 1;
				if (!lv.checkborder(GhostX / PIXEL, GhostY / PIXEL))
					check();
				else
					checkBorder();
				countdown -- ;

				if (countdown <= 100){
					cscare++;
					if (cscare == 10) {
						escare = !escare;
						cscare = 0;
					}
				}
				if (countdown == 0){
					countdown = TIMESCARE;
					modemove = Modemove.normal;
					escare = false;
				}

				break;
			}
			case scatter:{
				if(GhostX%PIXEL == 0 && GhostY%PIXEL == 0) {

					int Ghostx = GhostX / PIXEL;
					int Ghosty = GhostY / PIXEL;

					mode = finder.getMove(Ghostx, Ghosty, scatterx, scattery);
					if (Ghostx == scatterx && Ghosty == scattery){
						modemove = Modemove.normal;
						return;
					}
				}
				break;
			}
			case back:{
				SPEED = 8;

				if(GhostX%PIXEL == 0 && GhostY%PIXEL == 0) {


					int Ghostx = GhostX / PIXEL;
					int Ghosty = GhostY / PIXEL;

					mode = finder.getMove(Ghostx, Ghosty, 13,14);

					if (Ghostx == 13 && Ghosty == 14){
						SPEED = Control.SPEED;
						modemove = Modemove.normal;
						return;
					}
				}

				break;
			}
		}

		switch(mode) {
			case 1: GhostX+=SPEED;direction=0;break;
			case 2: GhostY+=SPEED;direction=1;break;
			case 3: GhostX-=SPEED;direction=2;break;
			case 4: GhostY-=SPEED;direction=3;break;
		}
	}
	public void draw(Canvas canvas){
		if (modemove == Modemove.scare )
		{
			if (escare){
				canvas.drawBitmap(endscare[c],GhostX,GhostY,null);
			}
			else
				canvas.drawBitmap(imagescare[c],GhostX,GhostY,null);
		}
		else if (modemove == Modemove.back){
			canvas.drawBitmap(eye,GhostX-PIXEL/4,GhostY-PIXEL/4,null);
		}
		else
			canvas.drawBitmap(image[c+direction*3],GhostX,GhostY,null);
	}
	public void drawscore(Canvas canvas,int number){
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setTextSize(35);
		canvas.drawText(""+number,GhostX,GhostY+30,p);
	}
	public void check() {

		int Ghostx=GhostX/PIXEL+1;
		int Ghosty=GhostY/PIXEL;

		if (GhostX%PIXEL == 0)
			Ghostx--;

		boolean check=(GhostX%PIXEL==0)&&(GhostY%PIXEL==0);
		if(check)
			if(lv.checkMap(Ghosty,Ghostx,3)||lv.checkMap(Ghosty,Ghostx,4)) findline();


		switch(mode) {
			case 1: if(lv.checkMap(Ghosty,Ghostx+1,1)&&check) findline();break;
			case 2: if(lv.checkMap(Ghosty+1,Ghostx,1)&&check) findline();break;
			case 3: if(lv.checkMap(Ghosty,Ghostx-1,1)&&check) findline();break;
			case 4: if(lv.checkMap(Ghosty-1,Ghostx,1)&&check) findline();break;
		}
	}
	public void findline() {
		int count=0;
		int oldmode=0;
		int Ghostx=GhostX/PIXEL;
		int Ghosty=GhostY/PIXEL;
		//boolean point=(GhostX%20==0)&&(GhostY%20==0);

		if(mode==1) oldmode=3;
		else if(mode==3) oldmode=1;
		else if(mode==2) oldmode=4;
		else if(mode==4) oldmode=2;

		int dir[]= {0,0,0,0};

		for(int i=0;i<4;i++) {
			boolean check=false;

			switch(i) {
				case 0: if(lv.canRun(Ghosty,Ghostx+1,1)) check=true;break;
				case 1: if(lv.canRun(Ghosty+1,Ghostx,1)) check=true;break;
				case 2: if(lv.canRun(Ghosty,Ghostx-1,1)) check=true;break;
				case 3: if(lv.canRun(Ghosty-1,Ghostx,1)) check=true;break;

			}

			if((oldmode!=i+1)&&check) {
				dir[count]=i+1;
				count++;}
		}

		Random r=new Random();
		int ch = Math.abs(r.nextInt())%count;

		mode=dir[ch];

	}
	public void checkBorder() {
		if(this.GhostY==14*PIXEL)
			if(this.GhostX<=-PIXEL) this.GhostX=28*PIXEL;
			else if(this.GhostX>=28*PIXEL+SPEED) this.GhostX=-PIXEL+SPEED;

	}
	public void setghost(int x,int y) {
		this.GhostX=x;
		this.GhostY=y;
	}

	public boolean checkdead(int x,int y) {

		if(x-PIXEL<=this.GhostX&&this.GhostX<=x+PIXEL&&y-PIXEL<=this.GhostY&&this.GhostY<=y+PIXEL)
			return true;
		return false;
	}
	public void setModemove(Modemove modemove){
		this.modemove = modemove;
		switch (modemove){
			case normal:
			case chase:
			case scatter:setPosition(Control.SPEED);break;
			case back:setPosition(10);break;
			case scare:{
				setPosition(2);
				countdown = 600;
			}
		}
	}
	public void setPosition(int a){
		if (GhostX % a != 0 || GhostY % a != 0){
			GhostX -= (GhostX%a);
			GhostY -= (GhostY%a);
		}
	}
}
