package cz.uhk.pro2.flappy.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

public class Bird implements TickAware {
	// fyzika
	static final double koefUp = -5.0;
	static final double koefDown = 2;
	static final int ticksFlyingUp = 4;

	// souradnice stredu ptaka
	int viewportX;
	double viewportY; // pro ladìní rychlosti

	// rychlost padani (pozitivni) nebo vzletu (negativni)
	double velocityY = koefDown;
	// kolik ticku jeste zbyva, nez ptak zacne po nakopnuti zase padat
	int ticksToFall = 0;

	public Bird(int initialX, int initialY) {
		this.viewportX = initialX;
		this.viewportY = initialY;
	}

	public void kick() {
		velocityY = koefUp; // ma zacit letet nahoru
		ticksToFall = ticksFlyingUp;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(viewportX - Tile.SIZE / 2, (int) viewportY - Tile.SIZE / 2, Tile.SIZE, Tile.SIZE);

		// debug, souradnice ptaka
		g.setColor(Color.BLACK);
		g.drawString(viewportX + ", " + viewportY, viewportX, (int) viewportY);
	}

	/**
	 * metoda která zjistí jestli pták narazil do dlaždice
	 * 
	 * @return
	 */
	public boolean collidesWithRectangle(int x, int y, int w, int h) {
			// vytvoøíme kružnici reprezentující obrys ptáka
			Ellipse2D.Float birdsBoundary = new Ellipse2D.Float(viewportX - Tile.SIZE / 2, (int) viewportY - Tile.SIZE / 2, Tile.SIZE, Tile.SIZE);
			// ovìøíme jestli kruznice ma neprázdný prunik s ctvercem zadanym x,y,w,h
			return birdsBoundary.intersects(x, y, w, h);
	}

	@Override
	public void tick(long ticksSinceStart) {
		viewportY += velocityY;
		if (ticksToFall > 0) {
			// ptak jeste leti nahoru, èekáme
			ticksToFall--;
		} else {
			// ptak ma padat
			velocityY = koefDown;
		}
	}

}
