package cz.uhk.pro2.flappy.game.service;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.management.RuntimeErrorException;

import cz.uhk.pro2.flappy.game.GameBoard;
import cz.uhk.pro2.flappy.game.Tile;
import cz.uhk.pro2.flappy.game.tiles.BonusTile;
import cz.uhk.pro2.flappy.game.tiles.EmptyTile;
import cz.uhk.pro2.flappy.game.tiles.WallTile;

public class CsvBoardLoader implements BoardLoader {
	//zapis logovacich hlasek - pomocny objekt pro zapisovani hlasek o prubehu programu
		static final Logger logger= Logger.getLogger(CsvBoardLoader.class.getName());
		private InputStream is;	 // stream, ze ktereho nacitame level
	
	public CsvBoardLoader(InputStream is) {
		this.is = is;
	}
	
	@Override
	public GameBoard loadLevel() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"))) {
			String[] line = br.readLine().split(";");
			//kolik typů dlazdic
			int numberOfTypes = Integer.parseInt(line[0]);			
			//zpracovavame zdroje obrazku k matici a obrazek ptaka
			BufferedImage imageOfTheBird = null;
			Map<String, Tile> tileTypes = new HashMap<>();
			for(int i =0; i<numberOfTypes;i++){
				line = br.readLine().split(";");
				String type = line[0];
				String clazz = line[1];
				int spriteX =Integer.parseInt(line[2]);
				int spriteY = Integer.parseInt(line[3]);
				int spriteWidth = Integer.parseInt(line[4]);
				int spriteHeight = Integer.parseInt(line[5]);
				String url = line[6];
				//pouziva se u bonusu
				String extraInfo =(line.length>=8)? line[7]: "";
				Tile referenceTile=tileTypes.get(extraInfo);
				if(clazz.equals("Bird")){
					//specialni radek - definice ptaka
				imageOfTheBird = loadImage(spriteX, spriteY, spriteWidth, spriteHeight, url);
				}else{
					//normalni dlazdice
					Tile tile = createTile(clazz, spriteX, spriteY, spriteWidth, spriteHeight, url, referenceTile);
					tileTypes.put(type, tile);
				}
				
				
			}
			//radek s pocty radku a sloupcu v matici herni plochy
			line = br.readLine().split(";");
			int rows = Integer.parseInt(line[0]);
			int colums = Integer.parseInt(line[1]);
			
	//		System.out.println("radky a sloupce " + rows +" " +colums);
			//vyrobime matici dlazdic
			Tile[][] tiles = new Tile[rows][colums];
	
		
			//projdeme radky s matici
			for(int i = 0; i<rows;i++){
				line=br.readLine().split(";");
				for(int j =0; j<colums;j++){
					//retezec v dane bunce
					String t;
					//osetreni, kdyby v csv chybely prazdne bunky na konci radku
					if(j<line.length){
						//v poradku, bunka je v csv
						t = line[j];
						
					}else{
						//bunka chyby, povazujeme ji za prazdnou
						t="";
					}
					if("B".equals(t)){
						tiles[i][j] = ((BonusTile)tileTypes.get("B")).clone();
						continue;
					}
					tiles[i][j]=tileTypes.get(t);
					
				}
			}
			GameBoard  gb = new GameBoard(tiles, imageOfTheBird);
			return gb;			
		} catch (IOException e) {
			throw new RuntimeException("Chyba pri cteni souboru",e);
		}
		
	}

	
private Tile createTile(String type, int x, int y, int w, int h, String url, Tile referencedTile) {
		
		try {
			
			BufferedImage resizedImage = loadImage(x, y, w, h, url);
			
			//vytvoøíme odpovídající typ dlaždice
//			Tile tile;
			switch(type){
				case "Wall": return new WallTile(resizedImage);
				case "Empty": return new EmptyTile(resizedImage);
				case "Bonus": return new BonusTile(resizedImage, referencedTile);
				default: throw new RuntimeException("Unknown tile type: "+type);
			}
			
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Wrong url of image "+type+": "+url, e);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading file "+type+": "+url, e);
		}
		
	}


	private BufferedImage loadImage(int x, int y, int w, int h, String url)
			throws IOException, MalformedURLException {
		// stahnout obrazek z URL a ulozit do promenne
		BufferedImage originalImage = ImageIO.read(new URL(url));

		// z urcitych souradnic vyrizneme dlazdici velikosti 16x16
		// vyriznout odpovidajici sprite z velkeho obrazku s mnoha sprity
		BufferedImage croppedImage = originalImage.getSubimage(x, y, w, h);

		// zvetsime/zmensime obrazek tak, aby sedel na velikost dlazdice
		BufferedImage resizedImage = new BufferedImage(Tile.SIZE, Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(croppedImage, 0, 0, Tile.SIZE, Tile.SIZE, null);
		return resizedImage;
	}

}
