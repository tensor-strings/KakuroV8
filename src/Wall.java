import java.awt.Color;

import java.util.Arrays;

public class Wall {
	private Tile[] dividedCells = new Tile[2];
	private boolean outsideWall = false;
	private Color wallColor;
	private Direction relativeCellPosition;
	private int x;
	private int y;
	private int x2;
	private int y2;

	/**
	 * @param cell1 A cell
	 * @param cell2 Cell one's neighbor
	 */
	public Wall(Tile cell1, Tile cell2) {
		dividedCells[0] = cell1;
		dividedCells[1] = cell2;
		x = cell1.getPosition().x;
		y = cell1.getPosition().y;

		if (cell2 == null) {
			outsideWall = true;
		} else {
			x2 = cell2.getPosition().x;
			y2 = cell2.getPosition().y;
		}
		relativeCellPosition = getRelativeCellPosition();
		setWallColor(Color.GRAY);
	}
	
	/**
	 * @return the first cell
	 */
	public Tile getCell1() {
		return dividedCells[0];
	}
	
	/**
	 * @return the neighb cell
	 */
	public Tile getCell2() {
		return dividedCells[1];
	}
	
	/**
	 * @return true if the wall is part of the border
	 */
	public boolean isOutsideWall() {
		return outsideWall;
	}
	
	/**
	 * @return The position of the first cell relative to the wall and second cell.
	 */
	public Direction getRelativeCellPosition() {
		if (x == x2 && y > y2) {
			relativeCellPosition = Direction.North;
		} else if (x == x2 && y < y2) {
			relativeCellPosition = Direction.South;
		} else if (x > x2 && y == y2) {
			relativeCellPosition = Direction.West;
		} else if (x < x2 && y == y2)
			relativeCellPosition = Direction.East;
		return relativeCellPosition;
	}

	/**
	 * @return the neighboring cell
	 */
	public Tile getNeighbor() {
		return dividedCells[1];
	}

	/**
	 * Prints both cells and their relative position
	 */
	@Override
	public String toString() {
		return "Wall [dividedCells=" + Arrays.toString(dividedCells) + ", outsideWall=" + outsideWall
				+ ", relativeCellPosition=" + relativeCellPosition + "]";
	}
	
	/**
	 * @return the color of the wall
	 */
	public Color getWallColor() {
		return wallColor;
	}
	
	/**
	 * @param wallColor 
	 * sets the wall color
	 */
	public void setWallColor(Color wallColor) {
		this.wallColor = wallColor;
	}
}
