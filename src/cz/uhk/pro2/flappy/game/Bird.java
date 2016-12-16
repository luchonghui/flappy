package cz.uhk.pro2.flappy.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Ellipse2D;

public class Bird implements TickAware {
	// fyzika
	static double speed = 0.0;
	static final double speedInc = 0.2*Tile.SIZE/20;
	static final double kickSpeed = -3.2*Tile.SIZE/20;

	// souradnice stredu ptaka
	int viewportX;
	double viewportY; // pro lad�n� rychlosti

	// kolik ticku jeste zbyva, nez ptak zacne po nakopnuti zase padat
	int ticksToFall = 0;

	Image image; // obrazek ptaka
	
	public Bird(int initialX, int initialY, Image image) {
		this.viewportX = initialX;
		this.viewportY = initialY;
		this.image = image;
	}

	public void kick(){
		if(speed > 0)speed = 0;
		speed += kickSpeed;
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
	 * metoda kter� zjist� jestli pt�k narazil do dla�dice
	 * 
	 * @return
	 */
	public boolean collidesWithRectangle(int x, int y, int w, int h) {
		// vytvo��me kru�nici reprezentuj�c� obrys pt�ka
		Ellipse2D.Float birdsBoundary = new Ellipse2D.Float((float)viewportX-Tile.SIZE/2, (float)viewportY-Tile.SIZE/2, w, h);
		// ov���me jestli kruznice ma nepr�zdn� prunik s ctvercem zadanym x,y,w,h
		return birdsBoundary.intersects(x, y, w, h);
	}
	
	public double getX(){
		return viewportX;
	}
		
	public double getY(){
		return viewportY;
	}

	@Override
	public void tick(long ticksSinceStart) {
		speed += speedInc;
		viewportY = viewportY + speed;
	}

}
