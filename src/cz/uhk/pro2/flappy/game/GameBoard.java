package cz.uhk.pro2.flappy.game;

import java.awt.Graphics;
import java.util.ArrayList;

import cz.uhk.pro2.flappy.game.tiles.BonusTile;
import cz.uhk.pro2.flappy.game.tiles.WallTile;

public class GameBoard implements TickAware {
	Tile[][] tiles; // matice dlazdic na herni plose
	int shiftX; // o kolik pixelu uz svet ubehl doleva
	int viewportWidth; // sirka hraci plochy v pixelech
	Bird bird;
	boolean gameover = false;
	ArrayList<Tile> list = new ArrayList<Tile>();
	
	public GameBoard() {
		tiles = new Tile[20][10];
		//tiles[2][1] = new WallTile();
		bird = new Bird(100, 100); /// TODO umistit do stredu okna?
	}
	
	public GameBoard(Tile[][] tiles) {
		this.tiles = tiles;
		bird = new Bird(100, 100); /// TODO umistit do stredu okna?
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
					if(t instanceof WallTile){// je dlaždice typu zeï?
						if(bird.collidesWithRectangle(screenX, screenY, Tile.SIZE, Tile.SIZE)){
							// došlo ke kolizi ptáka s dlaždicí
							//System.out.println("Kolize");
							gameover = true;
						}
					}

					// otestujeme jestli ptak sebral bonus
					if(t instanceof BonusTile){// je dlaždice typu bonus?
						if(bird.collidesWithRectangle(screenX, screenY, Tile.SIZE, Tile.SIZE)){
							// došlo ke kolizi ptáka s dlaždicí
							((BonusTile) t).setTitle(false);
						}
					}

				}
			}
		}
		// vykreslit ptaka
		bird.draw(g);
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
	
	public void kickTheBird() {
		bird.kick();
	}
	
	
}
