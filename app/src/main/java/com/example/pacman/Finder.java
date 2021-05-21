package com.example.pacman;


public class Finder {
	private boolean[][] map;
	private Level level;

	public Finder(Level pb) {
		level = pb;
		map = new boolean[31][29];

		for(int x=0;x<30;x++)
			for(int y=0;y<28;y++)
			{
				if(pb.mapgame[x][y] !=1){
					map[x][y] = false;
				}else{
					map[x][y] = true;
				}
			}
	}

	public int getMove(int a, int b, int x, int y) {
		if(x==a && y==b){
			return 4;
		}
		if (level.checkborder(a,b)){
			return 0;
		}

		boolean[][] valid = new boolean[31][29];

		for(int i=0;i<31;i++)
			for(int j=0;j<29;j++)
				valid[i][j]= map[i][j];
		valid[b][a] = true;

		Find[] Maze = new Find[500];

		int size = 1;
		int done=0;
		Find start = new Find(a, b, -1);
		Maze[0] = start;
		for(int i=0;i< size ; i++)
		{
			if(Maze[i].x==x&&Maze[i].y==y)
			{
				done=i;
				break;
			}
			if(Maze[i].x>0&&!valid[Maze[i].y][Maze[i].x-1])
			{
				valid[Maze[i].y][Maze[i].x-1]=true;
				Find m = new Find(Maze[i].x-1, Maze[i].y, i);
				Maze[size] = m;
				size++;
			}
			if(Maze[i].y>0&&!valid[Maze[i].y-1][Maze[i].x])
			{
				valid[Maze[i].y-1][Maze[i].x]=true;
				Find m = new Find(Maze[i].x, Maze[i].y-1, i);
				Maze[size] = m;
				size++;
			}
			if(Maze[i].x<28&&!valid[Maze[i].y][Maze[i].x+1])
			{
				valid[Maze[i].y][Maze[i].x+1]=true;
				Find m = new Find(Maze[i].x+1, Maze[i].y, i);
				Maze[size] = m;
				size++;
			}
			if(Maze[i].y<30&&!valid[Maze[i].y+1][Maze[i].x])
			{
				valid[Maze[i].y+1][Maze[i].x]=true;
				Find m = new Find(Maze[i].x, Maze[i].y+1, i);
				Maze[size] = m;
				size++;
			}
		}

		int xp = -1;
		int yp = -1;

		while(Maze[done].parent!=0) {
			done = Maze[done].parent;
		}
		xp = Maze[done].x;
		yp = Maze[done].y;

		if(xp == a - 1 && yp == b)
			return 3;
		if(xp == a + 1 && yp == b)
			return 1;
		if(xp == a && yp == b - 1)
			return 4;
		if(xp == a && yp == b + 1)
			return 2;
		return 0;

	}

	private class Find{
		public int x,y,parent;

		public Find(int a,int b,int c) {
			x=a;
			y=b;
			parent=c;
		}
	}

	public void render(boolean[][] a) {
		for(int x=0;x<31;x++)
		{for(int y=0;y<29;y++)
		{
			if(a[x][y]) System.out.print("1 ");
			else System.out.print("0 ");

		}
			System.out.println();
		}

	}
}
