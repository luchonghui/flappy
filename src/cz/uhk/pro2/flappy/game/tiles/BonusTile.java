package cz.uhk.pro2.flappy.game.tiles;

import java.awt.Graphics;
import java.awt.Image;

import cz.uhk.pro2.flappy.game.Tile;

public class BonusTile extends AbstractWallTile {
	private boolean active = true;// sezrano nebo ne
	
	public BonusTile(Image image) {
		super(image);
	}
	
	public void setTitle(boolean active){
		this.active=active;
	}

	public void draw(Graphics g, int x, int y) {
		if(active){
			super.draw(g,x,y);
		}
	}

	//dokud neni sezran je videt
	// libovolne prazdny ctvere�ek
	// taky p�es kolizi
	// kdy� je to instance bonus tile tak se�r�no
	
	//metoda draw z abstract tile ...prepsat
}
