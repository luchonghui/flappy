package cz.uhk.pro2.flappy.game;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import cz.uhk.pro2.flappy.game.tiles.BonusTile;
import cz.uhk.pro2.flappy.game.tiles.WallTile;

public class GameBoard implements TickAware {
	Tile[][] tiles; // matice dlazdic na herni plose
	int shiftX=30; // o kolik pixelu uz svet ubehl doleva
	int viewportWidth=200; // sirka hraci plochy v pixelech
	Bird bird;
	boolean gameover = false;
	Image imageOfTheBird;
	

	public GameBoard(Tile[][] tiles, Image imageOfTheBird){
		this.tiles = tiles;;
		this.imageOfTheBird=imageOfTheBird;
		reset();
	}
	public boolean isGameOver(){
		return gameover;
	}

	public void kickTheBird(){
		bird.kick();
	}
	
	public void setviewportWidth(int viewportWidth) {
		this.viewportWidth = viewportWidth;
	}
	
	/**
	 * Vykresli celou herni plochu (sloupy, bonusy, ptak) na platno g.
	 * a kontroluje jestli pták narazil do nežádoucích dlaždic
	 * @param g
	 */
	public void drawAndTestCollisions(Graphics g) {
		// j-souradnice prvni dlazdice vlevo, kterou je nutne kreslit
		int minJ = shiftX/Tile.SIZE;
		// pocet dlazdic (na sirku), kolik je nutne kreslit (do viewportu)
		// + 2 protoze celociselne delime jak shiftX tak viewportSize
		// ale hcceme zaokrouhlit nahoru
		int maxJ = minJ + viewportWidth/Tile.SIZE + 2;
		for (int i = 0; i < tiles.length; i++) {			
			for (int j = minJ; j < maxJ; j++) {
				// chceme, aby se svet tocil dokola
				// na konci vraci zase 0; tiles[0].length je pocet sloupcu - 1
				int j2 = j % tiles[0].length;
				Tile t = tiles[i][j2];
				if (t != null) {
					// ja na souradnicich i,j dlazdice?
					int screenX = j*Tile.SIZE - shiftX;
					int screenY = i*Tile.SIZE;
					//nakreslime dlazdici
					t.draw(g, screenX, screenY);
					
					// otestujeme moznou kolizi dlazdice s ptakem
					if(t instanceof WallTile){//je dlazdice typu zed?
						if(bird.collidesWithRectangle(screenX, screenY, Tile.SIZE, Tile.SIZE)){
							//gameOver = true; //doslo ke kolizi, hra ma skoncit
						}
					}
					
					if(t instanceof BonusTile){
						if(bird.collidesWithRectangle(screenX, screenY, Tile.SIZE, Tile.SIZE)){
							((BonusTile) t).setEaten(true);//ptak snedl bonus
							//t.draw(g, viewportX,viewportY);
							
						}
					}
					
					if(t instanceof BonusTile && j == (maxJ - 2)  ){//
						((BonusTile)t).setEaten(false);
					}
				}
			}
		}
		// vykreslit ptaka
		bird.draw(g);
	}

	 public void reset(){
		 gameover= false;
		 bird = new Bird(100, 100,imageOfTheBird ); 
	 }
	
	@Override
	public void tick(long ticksSinceStart) {
		if(!gameover){
			//s kazdym tickem ve hre posuneme hru o jeden pixel
			//tj. pocet ticku a pixelu se rovnaji
			shiftX = (int)ticksSinceStart;
			
			// dame vedet ptakovi, ze hodiny tickly
			bird.tick(ticksSinceStart);
		}
	}	
}
