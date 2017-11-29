import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import java.util.ArrayList;


public class Tile {
	private static final long serialVersionUID = 1L;
	private Wall[] walls = new Wall[4];
	
	private KakuroBoard game_board;
	
	private Color start_color;
	private int[] value = new int[] { 0, 0 };
	private boolean startBlock = false;
	private int label;
	
	private ArrayList<Tile> neighbors;
	
	private Point position;
	private int boardX;
	private int boardY;

	/**
	 * @param gameBoard the board
	 * @param position cells position
	 */
	public Tile(KakuroBoard gameBoard, Point position) {
		this.game_board = gameBoard;
		this.position = position;
		start_color = Color.white;
		boardX = gameBoard.getX();
		boardY = gameBoard.getY();
		neighbors = new ArrayList<Tile>();
		label = 1;
	}

	/**
	 * @return color of the tile
	 */
	public Color getStartColor() {
		return start_color;
	}

	/**
	 * @param startColor sets the tile color
	 */
	public void setStartColor(Color startColor) {
		this.start_color = startColor;
	}

	/**
	 * @return the x and y position on the game board
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * initializes the walls
	 */
	public void initWalls() {
		for (int i = 0; i < walls.length; i++) {
			if (walls[i] == null) {
				addWall(Direction.valueOf(i));
			}
		}

	}

	/**
	 * @param g graphic for the board
	 * @param size tile size
	 */
	public void draw(Graphics2D g, int size) {
		g.setColor(start_color);
		g.fillRect(position.x * size, position.y * size, size, size);
		for (int i = 0; i < 4; i++) {

			Wall w = walls[i];
			if (w == null) { 
				continue;
			} 
			else {
				g.setColor(w.getWallColor());
				if (i == Direction.North.num) {
					g.drawLine(position.x * size, position.y * size, (position.x + 1) * size, position.y * size);
				} 
				else if (i == Direction.East.num) {
					g.drawLine((position.x + 1) * size, position.y * size, (position.x + 1) * size,
							(position.y + 1) * size);
				} 
				else if (i == Direction.South.num) {
					g.drawLine(position.x * size, (position.y + 1) * size, (position.x + 1) * size,
							(position.y + 1) * size);
				} 
				else if (i == Direction.West.num) {
					g.drawLine(position.x * size, position.y * size, position.x * size, (position.y + 1) * size);

				}
			}
		}

	}

	/**
	 * @param x position on the game panel
	 * @param y position on the game panel
	 * @return true if the point is inside the tile
	 */
	public boolean Contains(int x, int y) {
		int startX = game_board.getX() + (position.x * game_board.getCellSize());
		int startY = game_board.getY() + (position.y * game_board.getCellSize());
		if (x > startX && y > startY && x < startX + game_board.getCellSize() && y < startY + game_board.getCellSize()) {
			return true;
		} 
		else
			return false;
	}

	/**
	 * @param direction - adding the walls to build the cell
	 */
	public void addWall(Direction direction) {
		Tile cell2 = null;
		if (direction == Direction.North) {
			if (position.y > 0) {
				cell2 = game_board.getCell(new Point(position.x, position.y - 1));
			}
		} else if (direction == Direction.East) {
			if (position.x < game_board.getSize().width - 1) {
				cell2 = game_board.getCell(new Point(position.x + 1, position.y));
			}
		} else if (direction == Direction.South) {
			if (position.y < game_board.getSize().height - 1) {
				cell2 = game_board.getCell(new Point(position.x, position.y + 1));
			}
		} else if (direction == Direction.West) {
			if (position.x > 0) {
				cell2 = game_board.getCell(new Point(position.x - 1, position.y));
			}
		}
		walls[direction.num] = new Wall(this, cell2);
	}

	/**
	 * @return north wall
	 */
	public Wall getNorthWall() {
		return walls[Direction.North.num];
	}

	/**
	 * @return west wall
	 */
	public Wall getWestWall() {
		return walls[Direction.West.num];
	}
	
	/**
	 * @return south wall
	 */
	public Wall getSouthWall() {
		return walls[Direction.West.num];
	}

	/**
	 * @return east wall
	 */
	public Wall getEastWall() {
		return walls[Direction.East.num];
	}

	/**
	 * prints value and isStart
	 */
	@Override
	public String toString() {
		return value[0] + "," + value[1] + "," + startBlock;
	}

	/**
	 * @param direction direction relative to the tile
	 * @return true if the cell currently has a wall in that direction
	 */
	public boolean hasWall(Direction direction) {
		if (walls[direction.num] != null) {
			return true;
		} else
			return false;
	}

	/**
	 * @return the position of the tile on the frame
	 */
	public int getBoardX() {
		return boardX;
	}

	/**
	 * @return the position of the tile on the frame
	 */
	public int getBoardY() {
		return boardY;
	}

	/**
	 * @return Top and right values. 0th index is overall for white tile.
	 */
	public int[] getValue() {
		return value;
	}

	/**
	 * @param value to set the tile
	 */
	public void setValue(int[] value) {
		this.value = value;
	}

	/**
	 * @return true if white tile
	 */
	public boolean checkWhite() {
		return start_color == Color.white;
	}

	/**
	 * @return true for black tile
	 */
	public boolean isStartBlock() {
		return startBlock;
	}

	/**
	 * @param startBlock makes a tile a new start tile
	 */
	public void setStartBlock(boolean startBlock) {
		this.startBlock = startBlock;
	}

	/**
	 * @return returns the tiles set for the maze puzzle
	 */
	public int getLabel() {
		return label;
	}

	/**
	 * @param label gives the tile a set
	 */
	public void setLabel(int label) {
		this.label = label;
	}

	/**
	 * Removes the wall for the maze generation
	 * @param direction direction we want to remove the wall in relative to the given tile
	 */
	public void removeWall(Direction direction) {
		if (walls[direction.num] != null) {
			Tile neighbs = walls[direction.num].getNeighbor();
			neighbors.add(neighbs);
			neighbs.neighbors.add(this);
			walls[direction.num] = null; 
			neighbs.walls[direction.opposite()] = null;

		}
	}
}
