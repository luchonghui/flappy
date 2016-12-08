package cz.uhk.pro2.flappy.game.service;

import cz.uhk.pro2.flappy.game.GameBoard;

/**
 * Rozhrani pro vsechny tridy, ktere umi nejak nacist herni plochu
 * 
 * @author krizpa1
 *
 */
public interface BoardLoader {
	/**
	 * Nacte a vrati herni plochu
	 * 
	 * @return
	 */
	public GameBoard getGameboard();
}
