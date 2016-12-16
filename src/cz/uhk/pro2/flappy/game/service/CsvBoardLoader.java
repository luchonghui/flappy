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
private InputStream is;	 // stream, ze ktereho nacitame level
	
	public CsvBoardLoader(InputStream is) {
		this.is = is;
	}
	
	@Override
	public GameBoard loadLevel() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(is))){// radek s poctem typu dlazdic
			String[] line = br.readLine().split(";");			
			int typeCount = Integer.parseInt(line[0]);
			Map<String, Tile> tileMap = new HashMap<String,Tile>();
			BufferedImage birdBufferedImage = null;
			for(int i=0; i<typeCount; i++){
				line=br.readLine().split(";");
				String tileType = line[0];
				String clazz=line[1];
				int x = Integer.parseInt(line[2]);
				int y = Integer.parseInt(line[3]);
				int w = Integer.parseInt(line[4]);
				int h = Integer.parseInt(line[5]);
				String url = line[6];
				String referencedTileType = line.length > 7?line[7]:"";
				Tile referencedTile = tileMap.get(referencedTileType);
//				tileTypes[i] = new WallTile(new Image);
				if(clazz.equals("Bird")){
					birdBufferedImage = loadImage(x, y, w, h, url);
				}else{
					Tile tile = createTile(clazz, x, y, w, h, url, referencedTile);
					tileMap.put(tileType, tile);
				}
			}
			// radek s pocty radku a sloupcu v matici herni plochy
			line = br.readLine().split(";");
			int rows = Integer.parseInt(line[0]);
			int columns = Integer.parseInt(line[1]);
			// vyrobime matici dlazdic
			Tile[][] tiles = new Tile[rows][columns];
			// projdeme radky s matici
			System.out.println("Radky: " +rows+ " Sloupce: "+columns);
			for(int i = 0; i<rows; i++){
				line = br.readLine().split(";");
				for(int j=0;j<columns;j++){
					String cell; // retezec v dane bunce
					// osetrime pripad, ze by v CSV chybely prazdne bunky na
					// konci radku
					if(j<line.length){
						cell = line[j]; //bunka v CSV existuje
					}else{
						cell=""; //bunka v CSV chybi - povazujeme ji za prazdnou
					}
					tiles[i][j]=tileMap.get(cell);
				}
			}
			GameBoard gb = new GameBoard(tiles, birdBufferedImage);
			gb.setTile(tileMap.get(""));
			return gb;			
		} catch (IOException e) {
			throw new RuntimeException("Chyba pri cteni souboru",e);
		}
		
	}
	
	// pomocny objekt pro zapisovani hlasek o prubehu programu
	static final Logger logger = Logger.getLogger(CsvBoardLoader.class.getName());

	
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
