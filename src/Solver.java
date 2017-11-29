import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Solver {

	/**
	 * @param all_tiles
	 * @param current_tile - active tile
	 * @return number suggestion
	 */
	public static String getHelp(Tile[][] all_tiles, Tile currentCell) {
		String help = "";
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			numbers.add(i);
		}

		ArrayList<Integer> verticalNums = new ArrayList<>();

		int vertical = currentCell.getPosition().y;
		int horizontal = currentCell.getPosition().x;
		
		int verticalTarget = 0;
		int vertLen = 0;
		for (int j = vertical - 1; j >= 0; j--) { 
			if (all_tiles[horizontal][j].isStartBlock()) {
				verticalTarget = all_tiles[horizontal][j].getValue()[0]; 
				break;
			}
			verticalNums.add(all_tiles[horizontal][j].getValue()[0]); 
			if(all_tiles[horizontal][j].getValue()[0]==0){
				vertLen+=1;
			}
		}
		
		for (int i = vertical; i < all_tiles[0].length; i++) {
			if (all_tiles[horizontal][i].isStartBlock()) {
				break;
			}
			verticalNums.add(all_tiles[horizontal][i].getValue()[0]); 
			if(all_tiles[horizontal][i].getValue()[0]==0){
				vertLen+=1;
			}
		}
		int vertSum = 0;
		for (int i : verticalNums) {
			vertSum += i;
		}

		int vNeeded = verticalTarget - vertSum;

		ArrayList<ArrayList<Integer>> setA = (Solver.findNums(numbers, vNeeded, vertLen,
				new ArrayList<Integer>()));
		Set<Integer> verticalSet = new HashSet<Integer>();
		for (int i = 0; i < setA.size(); i++) {
			for (int j = 0; j < setA.get(0).size(); j++) {
				verticalSet.add(setA.get(i).get(j));
			}
		}

		ArrayList<Integer> horizontalNums = new ArrayList<>();
		int horizontalTarget = 0;
		int horLen = 0;
		for (int j = horizontal - 1; j >= 0; j--) {
			if (all_tiles[j][vertical].isStartBlock()) {
				horizontalTarget = all_tiles[j][vertical].getValue()[1]; 
				break;
			}
			horizontalNums.add(all_tiles[j][vertical].getValue()[0]); 
			if (all_tiles[j][vertical].getValue()[0] == 0) {
				horLen += 1;
			}
		}
		
		for (int i = horizontal; i < all_tiles[0].length; i++) {
			if (all_tiles[i][vertical].isStartBlock()) {
				break;
			}
			horizontalNums.add(all_tiles[i][vertical].getValue()[0]); 
			if (all_tiles[i][vertical].getValue()[0] == 0) {
				horLen += 1;
			}
		}

		int horSum = 0;
		for (int i : horizontalNums) { 
			horSum += i;
		}

		int hNeeded = horizontalTarget - horSum;
		
		ArrayList<ArrayList<Integer>> setB = Solver.findNums(numbers, hNeeded, horLen,
				new ArrayList<Integer>());
		Set<Integer> horizontalSet = new HashSet<Integer>();
		for (int i = 0; i < setB.size(); i++) {
			for (int j = 0; j < setB.get(0).size(); j++) {
				horizontalSet.add(setB.get(i).get(j));
			}
		}

		Set<Integer> finalSet = new HashSet<Integer>();
		for (int s : horizontalSet) {
			if (verticalSet.contains(s) && !verticalNums.contains(s) && !horizontalNums.contains(s)) {
				if (verticalTarget >= vertSum + s && horizontalTarget >= horSum + s)
					finalSet.add(s);
			}
		}
		int i = 0;
		for (int s : finalSet) {
			if (i == 0) {
				help += String.valueOf(s);
			} else
				help += ", " + String.valueOf(s);
			i++;
		}

		return help;
	}

	/**
	 * @param numbers - nums to choose from
	 * @param target - target num val
	 * @param summands - count of summands
	 * @param partial - empty list
	 * @param resultList
	 */
	public static void subsetSums(ArrayList<Integer> numbers, int target, int summands, ArrayList<Integer> partial,
			ArrayList<ArrayList<Integer>> resultList) {

		int s = 0;
		for (int p : partial) {
			s += p;
		}

		if (s == target && partial.size() == summands) {
			resultList.add(partial);
		}

		for (int i = 0; i < numbers.size(); i++) {
			int n = numbers.get(i);
			ArrayList<Integer> remaining = new ArrayList<>();
			List<Integer> swag = new ArrayList<>();
			swag = numbers.subList(i + 1, numbers.size());
			for (int test : swag) {
				remaining.add(test);
			}
			ArrayList<Integer> tryMe = new ArrayList<>();
			partial.add(n);
			tryMe.addAll(partial);
			partial.remove(partial.size() - 1);

			Solver.subsetSums((ArrayList<Integer>) remaining, target, summands, tryMe, resultList);
		}
	}

	/**
	 * @param numbers - nums to choose from
	 * @param target - target num val
	 * @param summands - count of summands
	 * @param partial - empty list
	 * @param resultList
	 */
	public static ArrayList<ArrayList<Integer>> findNums(ArrayList<Integer> numbers, int target, int groupLength,
			ArrayList<Integer> partial) {
		ArrayList<ArrayList<Integer>> resultList = new ArrayList<ArrayList<Integer>>();
		Solver.subsetSums(numbers, target, groupLength, partial, resultList);
		return resultList;

	}
}
