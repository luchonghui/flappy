package cz.uhk.pro2.flappy.game;

import java.awt.Graphics;

/**
 * Spolecny interface pro vsechny druhy dlazdic na herni plose
 * 
 * @author krizpa1
 *
 */
public interface Tile {
	/**
	 * Sirka a vyska dlazdice v pixelech
	 */
	public static final int SIZE = 20;
	
	/**
	 * Vykresli dlazdici na platno g.
	 * @param g
|	 * @param x x-ova souradnice dlazdice ve viewportu v pixelech
	 * @param y y-ova souradnice dlazdice ve viewportu v pixelech
	 */
	void draw(Graphics g, int x, int y);
}
