import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;


public class KakuroBoard {
	private Dimension size;
	private Tile[][] gameData;
	private BufferedImage boardImage; // The game generator needs to know the image
	private int cellSize; // The game generator needs to know the cell size
	private int drawX;
	private int drawY;
	private Mode gameMode;

	/**
	 * @param size dimension for the game
	 * @param gameMode mode of the game
	 */
	public KakuroBoard(Dimension size, Mode gameMode) {
		this.setSize(size);
		this.gameMode = gameMode;
		adjustCellSize();
		gameData = new Tile[size.width][size.height];
		boardImage = new BufferedImage(size.width * cellSize-10, size.height * cellSize, BufferedImage.SCALE_DEFAULT);
		reset(); // when a new board is instantiated, we must reset the board
	}

	/**
	 * @return the image of the board
	 */
	public BufferedImage getImage() {
		return boardImage;
	}

	/**
	 * @return the size of the board
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * @return the data within all of the cells
	 */
	public Tile[][] getGameData() {
		return gameData;
	}

	/**
	 * @return the size of the cells
	 */
	public int getCellSize() {
		return cellSize;
	}

	/**
	 * resets the game data
	 */
	public void reset() {
		for (int x = 0; x < getSize().getWidth(); x++) {
			for (int y = 0; y < getSize().height; y++) {
				gameData[x][y] = new Tile(this, new Point(x, y));
			}
		}
		for (int x = 0; x < getSize().getWidth(); x++) {
			for (int y = 0; y < getSize().height; y++) {
				gameData[x][y].initWalls();
			}
		}

	}

	/**
	 * adjusts the cell size so the board fits in the window properly
	 */
	private void adjustCellSize() {
		int smallestDim = Toolkit.getDefaultToolkit().getScreenSize().height;
		boolean tooSmall = true;
		if (Toolkit.getDefaultToolkit().getScreenSize().width < smallestDim) {
			smallestDim = Toolkit.getDefaultToolkit().getScreenSize().width;
			tooSmall = false;
		}
		if (tooSmall = true)
			cellSize = (Toolkit.getDefaultToolkit().getScreenSize().height / getSize().height) - 10;
		else
			cellSize = Toolkit.getDefaultToolkit().getScreenSize().width / getSize().width;
	}
	/**
	 * Clears all of the entered data into white blocks. Used for starting a new game.
	 */
	public void clear() {
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {
				if (!gameData[x][y].isStartBlock()) {
					gameData[x][y].setValue(new int[] {0,0});
				}
			}
		}
	}

	/**
	 * 
	 * @param someCell a cell
	 * @param x cell's x position
	 * @param y cell's y position
	 * @return the top left corner of the cell
	 */
	public int getCellXCoordinate(Tile someCell, int x, int y) {
		return someCell.getBoardX() + (gameData[x][y].getPosition().x * cellSize);
	}

	/**
	 * 
	 * @param someCell a cell
	 * @param x cell's x position
	 * @param y cell's y position
	 * @return the top left corner of the cell
	 */
	public int getCellYCoordinate(Tile someCell, int x, int y) {
		return someCell.getBoardY() + (gameData[x][y].getPosition().y * cellSize);
	}

	/**
	 * 
	 * @param g graphics for the board
	 * @param panelSize dimension of the game panel
	 */
	public void draw(Graphics2D g, Dimension panelSize) {

		// Filling the area around the board in with black
		g.setColor(new Color(128,0,0));
		g.fillRect(0, 0, panelSize.width, panelSize.height);
		Graphics2D g2 = (Graphics2D) boardImage.getGraphics();
		
		for (int x = 0; x < getSize().getWidth(); x++) {
			for (int y = 0; y < getSize().getHeight(); y++) {
				int x1 = getCellXCoordinate(gameData[x][y], x, y); // Top right corner of the cell (x)
				int y1 = getCellYCoordinate(gameData[x][y], x, y); // Top right corner of the cell (y)
				g2.setColor(Color.black);
				if ((gameData[x][y].getValue()[0] != 0
						|| gameData[x][y].getValue()[1] != 0) && gameData[x][y].isStartBlock()) { // Starting block
																									// with
																									// values
					gameData[x][y].setStartColor(Color.black);
					gameData[x][y].draw(g2, cellSize);
					g2.setColor(Color.WHITE);
					// draw the diagonal
					g2.drawLine(x1, y1, x1 + cellSize, y1 + cellSize);
					// draw a white border around the cell
					g2.drawLine(x1, y1, x1 + cellSize, y1);
					g2.drawLine(x1 + cellSize, y1, x1 + cellSize, y1 + cellSize);
					g2.drawLine(x1 + cellSize, y1 + cellSize, x1, y1 + cellSize);
					g2.drawLine(x1, y1, x1, y1 + cellSize);
					if (gameData[x][y].getValue()[1] != 0 && gameData[x][y].isStartBlock()) { // Writing text in the
																								// start blocks
						g2.drawString(Integer.toString(gameData[x][y].getValue()[1]), x1 + cellSize / 2,
								y1 + cellSize / 3);
					}
					if (gameData[x][y].getValue()[0] != 0 && gameData[x][y].isStartBlock()) { // Writing text in the
																								// start blocks
						g2.drawString(Integer.toString(gameData[x][y].getValue()[0]), x1 + cellSize / 4,
								(int) (y1 + cellSize - (cellSize * .3)));
					}
				}

				else { // Empty block
					gameData[x][y].draw(g2, cellSize);

				}
				if (gameData[x][y].getValue()[0] < 0 && gameData[x][y].isStartBlock()) { // Starting block with no
																							// value
					g2.setColor(Color.black);
					gameData[x][y].draw(g2, cellSize);
					g2.setColor(Color.WHITE);
					// draw a white border around the cell
					g2.drawLine(x1, y1, x1 + cellSize, y1);
					g2.drawLine(x1 + cellSize, y1, x1 + cellSize, y1 + cellSize);
					g2.drawLine(x1 + cellSize, y1 + cellSize, x1, y1 + cellSize);
					g2.drawLine(x1, y1, x1, y1 + cellSize);
				}
			}
		}
		// These variables are the location that the game board image will be drawn at
		drawX = panelSize.width / 2 - boardImage.getWidth() / 2;
		drawY = panelSize.height / 2 - boardImage.getHeight() / 2;
		g.drawImage(boardImage, drawX, drawY, null);

	}

	/**
	 * @return the x for the board
	 */
	public int getX() {
		return drawX;
	}

	/**
	 * @return the y for the board
	 */
	public int getY() {
		return drawY;
	}

	/**
	 * @param position of the cell relative to other cells
	 * @return the cell at the given x column and y row
	 */
	public Tile getCell(Point position) {
		return gameData[position.x][position.y];
	}

	/**
	 * @return the image of the board
	 */
	public BufferedImage getGameImage() {
		return boardImage;
	}

	/**
	 * @param size sets the size of the board
	 */
	public void setSize(Dimension size) {
		this.size = size;
	}

}
