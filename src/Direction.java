import java.util.HashMap;
import java.util.Map;


public enum Direction {
	North(0), East(1), South(2), West(3);
	public int num;
	private static Map map = new HashMap<>();
	
	/**
	 * @param num the number associated with the given direction
	 */
	Direction(int num) {
		this.num = num;
	}
	
	/**
	 * Storing the direction with it associated num as the key
	 */
	static {
		for (Direction d : Direction.values()) {
			map.put(d.num, d);
		}
	}
	
	/**
	 * @param num the numeration for the direction
	 * @return the direction associated with the key
	 */
	public static Direction valueOf(int num) {
		return (Direction) map.get(num);
	}

	/**
	 * Get the opposite direction
	 * @return the integer number of the opposite directions
	 */
	public int opposite() {
		switch (num) {
		case 0:
			return 2;
		case 1:
			return 3;
		case 2:
			return 0;
		case 3:
			return 1;
		default:
			return num;
		}
	}
}
