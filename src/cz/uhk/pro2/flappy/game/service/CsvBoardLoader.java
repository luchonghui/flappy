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
import cz.uhk.pro2.flappy.game.tiles.EmptyTile;
import cz.uhk.pro2.flappy.game.tiles.WallTile;

public class CsvBoardLoader implements BoardLoader {
	// pomocny objekt pro zapisovani hlasek o prubehu programu
	static final Logger logger = Logger.getLogger(CsvBoardLoader.class.getName());

	InputStream is; // stream, ze ktereho nacitame level

	public CsvBoardLoader(InputStream is) {
		this.is = is;
	}

	@Override
	public GameBoard getGameboard() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
			// radek s poctem typu dlazdic
			String[] line = br.readLine().split(";");
			int numberOfTypes = Integer.parseInt(line[0]);
			// logger.log(Level.FINE, "Number of tile types: " + numberOfTypes);
			System.out.println("Poèet druhù dlaždic: " + numberOfTypes);
			// typy dlazdic
			Map<String, Tile> tileTypes = new HashMap<>();
			for (int i = 0; i < numberOfTypes; i++) {
				line = br.readLine().split(";");
				String type = line[0];
				String clazz = line[1];
				int spriteX = Integer.parseInt(line[2]);
				int spriteY = Integer.parseInt(line[3]);
				int spriteWidth = Integer.parseInt(line[4]);
				int spriteHeight = Integer.parseInt(line[5]);
				String url = line[6];
				Tile tile = createTile(clazz, spriteX, spriteY, spriteWidth, spriteHeight, url);
				tileTypes.put(type, tile);
			}
			// radek s pocty radku a sloupcu v matici herni plochy
			line = br.readLine().split(";");
			int rows = Integer.parseInt(line[0]);
			int columns = Integer.parseInt(line[1]);
			System.out.println("Poèet øádkù, sloupcù: " + rows + "," + columns);
			// vyrobime matici dlazdic
			Tile[][] tiles = new Tile[rows][columns];
			// projdeme radky s matici
			for (int i = 0; i < rows; i++) {
				line = br.readLine().split(";");
				for (int j = 0; j < columns; j++) {
					String t; // retezec v dane bunce
					// osetrime pripad, ze by v CSV chybely prazdne bunky na
					// konci radku
					if (j < line.length) {
						// v poradku, bunku mame v CSV
						t = line[j];
					} else {
						// bunka v CSV chybi, povazujeme ji za prazdnou
						t = "";
					}
					tiles[i][j] = tileTypes.get(t);
				}
			}
			GameBoard gb = new GameBoard(tiles);
			return gb;
		} catch (IOException e) {
			throw new RuntimeException("Chyba pri cteni souboru", e);
		}
	}

	private Tile createTile(String clazz, int x, int y, int w, int h, String url)
			throws IOException {
		
		// stahnout obrazek z URL a ulozit do promenne
		BufferedImage originalImage = ImageIO.read(new URL(url));
		
		// z urcitych souradnic vyrizneme dlazdici velikosti 16x16
		// vyriznout odpovidajici sprite z velkeho obrazku s mnoha sprity
		BufferedImage croppedImage = originalImage.getSubimage(x, y, w, h);
		
		// zvetsime/zmensime obrazek tak, aby sedel na velikost dlazdice
		BufferedImage resizedImage = new BufferedImage(Tile.SIZE, Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) resizedImage.getGraphics();
		g.drawImage(croppedImage, 0, 0, Tile.SIZE, Tile.SIZE, null);
		
		// vytvorime odpovidajici dlazdice
		switch (clazz) {
		case "Wall":
			return new WallTile(resizedImage);
		case "Bonus":
			return new EmptyTile(resizedImage); // TODO dodelat dlazdici typu
												// bonus
		case "Empty":
			return new EmptyTile(resizedImage);
		default:
			throw new RuntimeException("Neznámý typ dlazdice " + clazz);
		}

	}

}
