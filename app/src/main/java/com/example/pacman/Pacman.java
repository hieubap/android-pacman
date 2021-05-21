package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pacman {
	public static final int PIXEL = Control.PIXEL;
	public static final int SPEED = Control.SPEED;

	Bitmap image[];

	public int Pacmanx=13*PIXEL;
	public int Pacmany=23*PIXEL;
	public int color = Color.YELLOW;
	public int score =0;
	public int countdot;
	public int count=0;
	public int c=0,d=0;
	private Level lv;
	public int level;
	public int live;
	public boolean levelup=false;

	protected enum movemode {UP,DOWN,LEFT,RIGHT,NON};
	public Bitmap pacmanright,
			pacmandown,
			pacmanleft,
			pacmanup,
			oval;

	public movemode mode;
	public movemode moving;

	Pacman(Bitmap[] bitmaps){
		this.image = bitmaps;

		mode = movemode.RIGHT;
		moving= movemode.RIGHT;

		lv= new Level();
		this.countdot= getcountdot();
		level=1;
		live=3;
	}

	public void move() {
		c++;
		if(c == 5)
			c =0;
		if (!lv.checkborder(Pacmanx/PIXEL,Pacmany/PIXEL))
		{
			checkline();
		}
		else {
			checkBorder();
		}
		switch(moving) {
		case LEFT: this.Pacmanx-=SPEED;d = 2;break;
		case RIGHT:this.Pacmanx+=SPEED;d = 0;break;
		case UP:   this.Pacmany-=SPEED;d = 3;break;
		case DOWN: this.Pacmany+=SPEED;d = 1;break;
		case NON: if (c != 0) c--;break;
		}
	}

	public void checkline() {

		boolean check=(Pacmanx%PIXEL==0)&&(Pacmany%PIXEL==0);

		int mapx=Pacmanx/PIXEL+1;
		int mapy=Pacmany/PIXEL;
		if (Pacmanx%PIXEL == 0)
			mapx --;


		if(mode!= moving) switch(mode) {
		case LEFT: if(lv.canRun(mapy,mapx-1,1)&&check) moving=mode;break;
		case RIGHT:if(lv.canRun(mapy,mapx+1,1)&&check) moving=mode;break;
		case UP: if(lv.canRun(mapy-1,mapx,1)&&check) moving = mode;break;
		case DOWN: if(lv.canRun(mapy+1,mapx,1)&&check) moving =mode;break;
		case NON: break;
		}

		switch(moving) {
		case LEFT:if(lv.checkMap(mapy,mapx-1,1)&&check) moving=movemode.NON;break;
		case RIGHT:if(lv.checkMap(mapy,mapx+1,1)&&check) moving=movemode.NON;break;
		case UP: if(lv.checkMap(mapy-1,mapx,1)&&check) moving=movemode.NON;break;
		case DOWN: if(lv.checkMap(mapy+1,mapx,1)&&check) moving=movemode.NON;break;
		case NON: break;
		}

		if(moving==movemode.LEFT&&mode==movemode.RIGHT) moving=movemode.RIGHT;
		else if(moving==movemode.RIGHT&&mode==movemode.LEFT) moving=movemode.LEFT;
		else if(moving==movemode.DOWN&&mode==movemode.UP) moving=movemode.UP;
		else if(moving==movemode.UP&&mode==movemode.DOWN) moving=movemode.DOWN;
	}

	public void checkBorder() {
		if(this.Pacmany==14*PIXEL)
		if(this.Pacmanx<=-PIXEL) this.Pacmanx=28*PIXEL;
		else if(this.Pacmanx>=28*PIXEL+SPEED) this.Pacmanx=-PIXEL+SPEED;

	}

	public void draw(Canvas canvas){
		canvas.drawBitmap(image[c+d*5],Pacmanx,Pacmany,null);
		Paint paint = new Paint();
		paint.setTextSize(30);
		paint.setColor(Color.WHITE);
		canvas.drawText("Score : "+score,20,1350,paint);
	}
	public boolean checkdead(int x,int y) {

		if(x-PIXEL<=this.Pacmanx&&this.Pacmanx<=x+PIXEL&&y-PIXEL<=this.Pacmany&&this.Pacmany<=y+PIXEL) this.live--;
		if(live==0) return true;
		return false;
	}

	public int getcountdot() {
		int count=0;
		for(int i=0;i<31;i++)
			for(int j=0;j<28;j++) if(lv.checkMap(i, j, 0)||lv.checkMap(i,j,3)) count++;
		//System.out.println(countdot);
		return count;
	}

//	public void endgame() {
//		dead=true;
//	}
    private int idstream = 0;
    public void setscore(Level lv,Sound sound) {
		boolean check=(Pacmanx%PIXEL==0)&&(Pacmany%PIXEL==0);
		int mapx=Pacmanx/PIXEL;
		int mapy=Pacmany/PIXEL;
		if(check)
			if (lv.checkMap(mapy, mapx, 0)) {
			    if (idstream != 0)
			    sound.stop(idstream);
				idstream = sound.play(sound.eat);

				score += 10;
				this.countdot--;
				lv.setMap(mapy, mapx, 2);
			} else if (lv.checkMap(mapy, mapx, 3)) {
				sound.play(sound.eat);
				score += 10;
				this.countdot--;

				lv.setMap(mapy, mapx, 4);
			}
	}
	public void setposition(int x,int y) {
		this.Pacmanx=x;
		this.Pacmany=y;
	}
	public boolean getlevelup() {
		if(countdot==0) {this.countdot=this.count;return true;}
		return false;
	}
	public void setModemove(movemode m) {
		mode = m;
	}
//	public Bitmap getPacman(boolean i,movemode j) {
//		if(i)
//			return getImage(j);
//		return oval;
//	}
	public boolean isStop() {
		if(moving == movemode.NON)
			return true;
		return false;
	}
}
