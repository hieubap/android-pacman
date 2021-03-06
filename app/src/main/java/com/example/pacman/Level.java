package com.example.pacman;

public class Level {
	public int mapgame[][]={//  y = 28  x =31
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,3,0,0,0,0,0,0,1,1,0,0,0,0,0,0,3,0,0,0,0,0,1},//1 2
        {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
        {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
        {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
        {1,3,0,0,0,0,3,0,0,3,0,0,3,0,0,3,0,0,3,0,0,3,0,0,0,0,3,1},// 3 4 5 6 / 7 8 9 10
        {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
        {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
        {1,0,0,0,0,0,3,1,1,0,0,0,0,1,1,0,0,0,0,1,1,3,0,0,0,0,0,1},// 11 12
        {1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1},
        {1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1},
        {1,1,1,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,1,1,1,1},// 13 14
	    {1,1,1,1,1,1,0,1,1,0,1,2,2,2,2,2,2,1,0,1,1,0,1,1,1,1,1,1},
	    {1,1,1,1,1,1,0,1,1,0,1,2,2,2,2,2,2,1,0,1,1,0,1,1,1,1,1,1},
        {0,0,0,0,0,0,3,0,0,3,1,2,2,2,2,2,2,1,3,0,0,3,0,0,0,0,0,0,2},// 15 16 17 18
        {1,1,1,1,1,1,0,1,1,0,1,2,2,2,2,2,2,1,0,1,1,0,1,1,1,1,1,1},
        {1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1},
        {1,1,1,1,1,1,0,1,1,3,0,0,0,0,0,0,0,0,3,1,1,0,1,1,1,1,1,1},// 19 20
        {1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1},
        {1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1},
        {1,0,0,0,0,0,3,0,0,3,0,0,0,1,1,0,0,0,3,0,0,3,0,0,0,0,0,1},// 21 22 23 24
        {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
        {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
        {1,0,0,0,1,1,3,0,0,3,0,0,3,0,0,3,0,0,3,0,0,3,1,1,0,0,0,1},// 25 26 27 28 29 30
        {1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1},
        {1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1},
        {1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1},// 31 32
        {1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
        {1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0,0,0,0,1},// 33 34
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
	public boolean checkborder(int x,int y){
		if (y == 14 &&( x <= 0|| x >= 27 )) return true;
		return false;
	}
	
	public boolean checkmap(int x,int y) {
		if(mapgame[y][x]==1) return true;
		return false;
	}
	public void setMap(int a,int b,int c) {
		mapgame[a][b]=c;
	}
	public boolean checkMap(int a,int b,int c) {
		if(mapgame[a][b]==c) return true;
		return false;
	}
	public boolean canRun(int a,int b,int c) {

		if(mapgame[a][b]!=c ) return true;
		return false;
	}
	public int getMap(int a,int b) {
		return mapgame[b][a];
	}
}
