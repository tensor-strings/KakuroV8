import java.util.ArrayList;


public class TestKakuro extends KakuroGenerator {
	private ArrayList<Tile> tiles = new ArrayList<>();
	
	/**
	 * @param game_board the game board to be drawn on
	 */
	public TestKakuro(KakuroBoard game_board) {
		super(game_board);
	}
	
	/**
	 * start board
	 */
	@Override
	public void generateGame() {
		for (int i = 0; i < game_board.getGameData().length; i++) {
			for (int j = 0; j < game_board.getGameData()[0].length; j++) {
				tiles.add(game_board.getGameData()[i][j]);
			}
		}
	}
	
	/**
	 * @return all tiles
	 */
	public ArrayList<Tile> getCells() {
		return tiles;
	}
	
	
}
