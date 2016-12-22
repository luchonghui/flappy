package cz.uhk.pro2.flappy.game.tiles;

import java.awt.Graphics;
import java.awt.Image;

import cz.uhk.pro2.flappy.game.Tile;

public class BonusTile extends AbstractWallTile {
	private boolean eat;//zda je bonus sezrán
	private Tile emptyTile;
	
	
	public BonusTile(Image image, Tile emptyTile) {
		super(image);
		eat = false;
		this.emptyTile=emptyTile;
	}
	

	public void sezrano(boolean eat) {
		this.eat = eat;
	}
	public Tile getEmptyTile(){
		return emptyTile;
	}
	
	//dokud neni sezran je videt
	// libovolne prazdny ctvereèek
	// taky přes kolizi
	// když je to instance bonus tile tak sežráno
	//metoda draw z abstract tile ...prepsat
	
	@Override
	public void draw(Graphics g, int x, int y){
		
		// nesežráno
		if(eat == false){
			super.draw(g, x, y);
			
		// sežráno
		}else{
			emptyTile.draw(g, x, y);
		}
	}
	@Override
	public BonusTile clone(){
		return new BonusTile(img, emptyTile);
	}
}