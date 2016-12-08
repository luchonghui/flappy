package cz.uhk.pro2.flappy.game.tiles;

import java.awt.Graphics;
import java.awt.Image;

import cz.uhk.pro2.flappy.game.Tile;

public class BonusTile extends AbstractWallTile {
	private boolean active = false;// sezrano nebo ne
	
	public BonusTile(Image image) {
		super(image);
	}
	
	

	public void draw(Graphics g, int x, int y, boolean active) {
		if(active){
			g.clearRect(x, y, Tile.SIZE, Tile.SIZE);
		g.drawRect(x, y, Tile.SIZE, Tile.SIZE);
		}
	}

	//dokud neni sezran je videt
	// libovolne prazdny ctvereèek
	// taky pøes kolizi
	// když je to instance bonus tile tak sežráno
	
	//metoda draw z abstract tile ...prepsat
}
