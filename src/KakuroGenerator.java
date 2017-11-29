import java.util.ArrayList;


public abstract class KakuroGenerator {
	KakuroBoard game_board;

	public KakuroGenerator(KakuroBoard gameBoard)
	{
		game_board = gameBoard;
	}

	public abstract void generateGame();

	public abstract ArrayList<Tile> getCells();
		
	}

