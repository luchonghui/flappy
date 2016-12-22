package cz.uhk.pro2.flappy.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Ellipse2D;

public class Bird implements TickAware {
	//fyzika
	static final double koefUp = -4.0;
	static final double koefDown = 2.0;
	static final int ticksFlyingUp = 5;
	
	//souřadnice středu ptáka
	int viewportX;
	double viewportY;//aby se dala jemně ladit rychlost padání
	
	//rychlost padání (pozitivní), vzletu(negativní)
	double velocityY = koefDown;
	//kolik ticku zbývá, než začne padat
	int ticksToFall = 0;
	//obrazek ptaka
	final Image image;
	public Bird(int initialX, int initialY, Image image) {
		this.viewportX = initialX;
		this.viewportY = initialY;
		this.image = image;
	}
	
	public void kick(){
		velocityY = koefUp; //letí nahoru - mění stav
		ticksToFall = ticksFlyingUp; //jak dlouho letí nahoru
		
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		//g.fillOval(viewportX - Tile.SIZE / 2, (int) viewportY - Tile.SIZE / 2, Tile.SIZE, Tile.SIZE);
		g.drawImage(image, (int) viewportX - Tile.SIZE / 2, (int) viewportY - Tile.SIZE / 2, null);
		
		// debug, souradnice ptaka
		g.setColor(Color.BLACK);
		//g.drawString(viewportX + ", " + viewportY, viewportX, (int) viewportY);
	}

	/**
	 * metoda která zjistí jestli pták narazil do dlaždice
	 * 
	 * @return
	 */
	public boolean collidesWithRectangle(int x, int y, int w, int h) {
		// vytvoříme kružnici reprezentující obrys ptáka
		Ellipse2D.Float birdsBoundary = new Ellipse2D.Float((float)viewportX-Tile.SIZE/2, (float)viewportY-Tile.SIZE/2, w, h);
		// ověříme jestli kruznice ma neprázdný prunik s ctvercem zadanym x,y,w,h
		return birdsBoundary.intersects(x, y, w, h);
	}

	@Override
	public void tick(long ticksSinceStart) {
		viewportY +=velocityY;
		if(ticksToFall >0){//ptak letel nahoru
			ticksToFall --;
		}else{
			velocityY=koefDown;//ptak zacne padat
		}
	}

}