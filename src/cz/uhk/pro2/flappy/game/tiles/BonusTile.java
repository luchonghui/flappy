package cz.uhk.pro2.flappy.game.tiles;

import java.awt.Graphics;
import java.awt.Image;

import cz.uhk.pro2.flappy.game.Tile;

public class BonusTile extends AbstractWallTile {
	Tile emptyTile;
	

	public BonusTile(Image img, Tile emptyTile){
		super.img = img;
		this.emptyTile = emptyTile;
	}
	
	@Override
	public void draw(Graphics g, int x, int y){
		g.drawImage(img, x, y, null);
	}

	//dokud neni sezran je videt
	// libovolne prazdny ctvere�ek
	// taky p�es kolizi
	// kdy� je to instance bonus tile tak se�r�no
	
	//metoda draw z abstract tile ...prepsat
}
