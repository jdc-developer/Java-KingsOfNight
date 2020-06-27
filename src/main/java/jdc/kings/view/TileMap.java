package jdc.kings.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import jdc.kings.utils.Constants;

/**
 * 
 * @author ForeignGuyMike, Jorge Do Carmo
 * {@link https://www.youtube.com/watch?v=qJpdRFvSj1A}
 *
 */
public class TileMap {
	
	private float x;
	private float y;
	
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private float tween;
	
	private int[][] map;
	private int tileSize;
	private int increaseSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	private BufferedImage tileSet;
	private int numTilesAcross;
	private int numTilesRows;
	private Tile[][] tiles;
	
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize, int increaseSize) {
		this.increaseSize = increaseSize;
		this.tileSize = tileSize;
		numRowsToDraw = Constants.HEIGHT / tileSize + 2;
		numColsToDraw = Constants.WIDTH / tileSize + 2;
		tween = 0.07f;
	}
	
	public void loadTiles(String s, int[] normal) {
		try {
			tileSet = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileSet.getWidth() / tileSize;
			numTilesRows = tileSet.getHeight() / tileSize;
			tiles = new Tile[numTilesRows][numTilesAcross];
			
			BufferedImage subImage;
			
			int count = 1;
			for (int row = 0; row < numTilesRows; row++) {
				for (int col = 0; col < numTilesAcross; col++) {
					subImage = tileSet.getSubimage(
							col * tileSize,
							row * tileSize,
							tileSize,
							tileSize);
					int type = Tile.BLOCKED;
					for (int i = 0; i < normal.length; i++) {
						if (count == normal[i]) {
							type = Tile.NORMAL;
						}
					}
					tiles[row][col] = new Tile(subImage, type);
					count++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s) {
		try {
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = Constants.WIDTH / Constants.WIDTHSCALE - width;
			xmax = 0;
			ymin = Constants.HEIGHT / Constants.SCALE - height;
			ymax = 0;
			
			String delims = "\\s+";
			for (int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public float getTween() {
		return tween;
	}

	public void setTween(float tween) {
		this.tween = tween;
	}

	public int getIncreaseSize() {
		return increaseSize;
	}

	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	public void setPosition(float x, float y) {
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		colOffset = (int) - this.x / (tileSize + increaseSize);
		rowOffset = (int) - this.y / (tileSize + increaseSize);
	}

	private void fixBounds() {
		if (x < xmin) x = xmin;
		if (y < ymin) y = ymin;
		if (x > xmax) x = xmax;
		if (y > ymax) y = ymax;
	}
	
	public void render(Graphics2D g) {
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			if (row >= numRows) break;
			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
				if (col >= numCols) break;
				if (map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
 				g.drawImage(tiles[r][c].getImage(),
						(int)x + col * (tileSize + increaseSize),
						(int)y + row * (tileSize + increaseSize),
						tileSize + increaseSize,
						tileSize + increaseSize,
						null);
			}
		}
	}

}
