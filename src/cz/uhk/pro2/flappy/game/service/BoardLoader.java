package cz.uhk.pro2.flappy.game.service;

import cz.uhk.pro2.flappy.game.GameBoard;

/**
 * Spoledne rozhrani pro tridy umoznujici nacitat level
 * 
 * @author kratkra1
 *
 */

public interface BoardLoader {
	/**
	 * načte level (herní plochu)
	 * @return
	 */
	GameBoard loadLevel();
	
}
