package pkg2048;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
	
	Number[][] map = new Number[4][4];
	Movement direction;
	boolean moveMade, gameOver, setEval, made4;
	int eval, blankCount = 0, bonus;
	
	public State(Number[][] map, Movement direction, int eval) {
		this(map, eval);
		this.direction = new Movement(direction);
		move();
	}
	
	public State(Number[][] map, int eval) {
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				this.map[x][y] = new Number(map[x][y].newSpot, map[x][y].val);
			}
		}
		this.eval = eval;
		
	}
	
	public State(Number[][] map, Number num, int eval, boolean made4) {
		
		this(map, eval);
		this.map[num.newSpot.x][num.newSpot.y] = new Number(num.newSpot, num.val);
		this.made4 = made4;
		
	}
	
	public int getEval() {
		if (!setEval) {
			setEval();
		}
		//System.out.println("result: "+(eval + bonus));
		return eval + bonus;
	}
	
	public int highestIndex(int ... list) {
		int index = 0;
		int max = list[0];
		for (int i = 1; i < list.length; i++) {
			if (list[i] > max) {
				max = list[i];
				index = i;
			}
		}
		return index;
	}
	
	public ArrayList<Number> getAdjacent(int x, int y) {
		ArrayList<Number> adjacent = new ArrayList<Number>();
		if (x > 0) {
			adjacent.add(map[x-1][y]);
		}
		if (x < 3) {
			adjacent.add(map[x+1][y]);
		}
		if (y > 0) {
			adjacent.add(map[x][y-1]);
		}
		if (y < 3) {
			adjacent.add(map[x][y+1]);
		}
		return adjacent;
	}
	
	public Number max(Number ... list) {
		Number max = list[0];
		for (Number num : list) {
			if (num.val > max.val) {
				max = num;
			}
		}
		return max;
	}
	
	public int max(int ... list) {
		int max = list[0];
		for (int i : list) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}
	
	public void printMonotonicity() {

		int[] sides = new int[4], lowVertical = new int[4], lowHorizontal = new int[4], highVertical = new int[4], highHorizontal = new int[4];
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (map[x][y].val == 0) {
					continue;
				}
				if (lowHorizontal[y] == 0 || map[x][y].val < lowHorizontal[y]) {
					lowHorizontal[y] = map[x][y].val;					
				}
				if (highHorizontal[y] < map[x][y].val) {
					highHorizontal[y] = map[x][y].val;
				}
				if (lowVertical[x] == 0 || map[x][y].val < lowVertical[x]) {
					lowVertical[x] = map[x][y].val;					
				}
				if (highVertical[y] < map[x][y].val) {
					highVertical[y] = map[x][y].val;
				}
				else {
					if (x == 0 || x == 3) {
						sides[x] += map[x][y].val;
					}
					if (y == 0 || y == 3) {
						sides[(y+2)%4] += map[x][y].val;
					}
				}
			}
		}
		int side = highestIndex(sides);
		
		switch(side) {
		//left
		case 0: {
			System.out.println("left: ");
			for (int x = 1; x < 4; x++) {
				System.out.println(lowVertical[x-1] - highVertical[x]);
			}
			System.out.println(lowVertical[3]);
			break;
		}
		//down
		
		case 1: {
			System.out.println("down: ");
			for (int y = 2; y > -1; y--) {
				System.out.println(lowHorizontal[y+1] - highHorizontal[y]);
			}
			System.out.println(lowHorizontal[0]);
			break;
		}
		//up
		case 2: {
			System.out.println("up: ");
			for (int y = 1; y < 4; y++) {
				System.out.println(lowHorizontal[y-1] - highHorizontal[y]);
			}
			System.out.println(lowHorizontal[3]);
			break;
		}
		//right
		case 3: {
			System.out.println("right: ");
			for (int x = 2; x > -1; x--) {
				System.out.println(lowVertical[x+1] - highVertical[x]);
			}
			System.out.println(bonus += lowVertical[0]);
			break;
		}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setEval() {

		int[] sides = new int[4], lowVertical = new int[4], lowHorizontal = new int[4], highVertical = new int[4], highHorizontal = new int[4];
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (map[x][y].val == 0) {
					blankCount++;
					continue;
				}
				if (lowHorizontal[y] == -1 || map[x][y].val < lowHorizontal[y]) {
					lowHorizontal[y] = map[x][y].val;					
				}
				if (highHorizontal[y] < map[x][y].val) {
					highHorizontal[y] = map[x][y].val;
				}
				if (lowVertical[x] == -1 || map[x][y].val < lowVertical[x]) {
					lowVertical[x] = map[x][y].val;					
				}
				if (highVertical[x] < map[x][y].val) {
					highVertical[x] = map[x][y].val;
				}
				if (x == 0 || x == 3) {
					//bonus += map[x][y].val;
					sides[x] += map[x][y].val;
				}
				if (y == 0 || y == 3) {
					//bonus += map[x][y].val;
					sides[(y+2)%4] += map[x][y].val;
				}
			}
		}
		bonus += blankCount*5;
		int side = highestIndex(sides);
		bonus += sides[side];
		
		switch(side) {
		//left
		case 0: {
			for (int x = 1; x < 4; x++) {
				bonus += lowVertical[x-1] - highVertical[x];
			}
			bonus += lowVertical[3];
			break;
		}
		//right
		case 3: {
			for (int x = 2; x > -1; x--) {
				bonus += lowVertical[x+1] - highVertical[x];
			}
			bonus += lowVertical[0];
			break;
		}		
		//up
		case 2: {
			for (int y = 1; y < 4; y++) {
				bonus += lowHorizontal[y-1] - highHorizontal[y];
			}
			bonus += lowHorizontal[3];
			break;
		}		
		//down
		case 1: {
			for (int y = 2; y > -1; y--) {
				bonus += lowHorizontal[y+1] - highHorizontal[y];
			}
			bonus += lowHorizontal[0];
			break;
		}
		}
		
		if (side == 1 || side == 2) {
			int y = (side == 1 ? 3 : 0);
			int monotonicityRight = 0, monotonicityLeft = 0;
			rightLoop:
			for (int x = 1; x < 4; x++) {
				if (map[x-1][y].val >= map[x][y].val || map[x-1][y].val == 0) {
					monotonicityRight += map[x-1][y].val;
				}
				else {
					monotonicityRight -= map[x][y].val;
					break rightLoop;
				}
				if (x == 3) {
					monotonicityRight += map[3][y].val;
					break rightLoop;
				}
			}
			leftLoop:
			for (int x = 2; x > -1; x--) {
				if (map[x+1][y].val >= map[x][y].val || map[x+1][y].val == 0) {
					monotonicityLeft += map[x+1][y].val;
				}
				else {
					monotonicityLeft -= map[x][y].val;
					break leftLoop;
				}
				if (x == 0)  {
					monotonicityLeft += map[0][y].val;
					break leftLoop;
				}
			}
			if (monotonicityRight > monotonicityLeft) {
				bonus += monotonicityRight;
				bonus += map[3][(y+2)%4].val;
			}
			else {
				bonus += monotonicityLeft;
				bonus += map[0][(y+2)%4].val;
			}
		}
		else {		
			int x = (side == 0 ? 0 : 3);
			int monotonicityUp = 0, monotonicityDown = 0;
			downLoop:
				for (int y = 1; y < 4; y++) {
					if (map[x][y-1].val >= map[x][y].val || map[x][y-1].val == 0) {
						monotonicityDown += map[x][y-1].val;
					}
					else {
						monotonicityDown -= map[x][y].val;
						break downLoop;
					}
					if (y == 3) {
						monotonicityDown += map[x][3].val;
						break downLoop;
					}
				}
				upLoop:
				for (int y = 2; y > -1; y--) {
					if (map[x][y+1].val >= map[x][y].val || map[x][y+1].val == 0) {
						monotonicityUp += map[x][y+1].val;
					}
					else {
						monotonicityUp -= map[x][y].val;
						break upLoop;
					}
					if (y == 0)  {
						monotonicityUp += map[x][0].val;
						break upLoop;
					}
				}
				if (monotonicityDown > monotonicityUp) {
					bonus += monotonicityDown;
					bonus += map[Math.abs(x-1)][3].val;
				}
				else {
					bonus += monotonicityUp;
					bonus += map[Math.abs(x-1)][0].val;
				}
		} 
		
		/*for (int y = 0; y < 4; y++) {
			int monotonicityRight = 0, monotonicityLeft = 0;
			rightLoop:
			for (int x = 1; x < 5; x++) {
				if (x == 4) {
					monotonicityRight += map[3][y].val;
					break rightLoop;
				}
				if (map[x-1][y].val >= map[x][y].val) {
					monotonicityRight += map[x-1][y].val;
				}
				else {
					monotonicityRight -= map[x][y].val;
					break rightLoop;
				}
			}
			leftLoop:
			for (int x = 2; x > -2; x--) {
				if (x == -1)  {
					monotonicityLeft += map[0][y].val;
					break leftLoop;
				}
				if (map[x+1][y].val >= map[x][y].val) {
					monotonicityLeft += map[x+1][y].val;
				}
				else {
					monotonicityLeft -= map[x][y].val;
					break leftLoop;
				}
			}
			bonus += Math.max(monotonicityRight, monotonicityLeft);
		}
		

		for (int x = 0; x < 4; x++) {
			int monotonicityUp = 0, monotonicityDown = 0;
			downLoop:
			for (int y = 1; y < 5; y++) {
				if (y == 4) {
					monotonicityDown += map[x][3].val;
					break downLoop;
				}
				if (map[x][y-1].val >= map[x][y].val) {
					monotonicityDown += map[x][y-1].val;
				}
				else {
					monotonicityDown -= map[x][y].val;
					break downLoop;
				}
			}
			upLoop:
			for (int y = 2; y > -2; y--) {
				if (y == -1) {
					monotonicityUp += map[x][0].val;
					break upLoop;
				}
				if (map[x][y+1].val >= map[x][y].val) {
					monotonicityUp += map[x][y+1].val;
				}
				else {
					monotonicityUp -= map[x][y].val;
					break upLoop;
				}
			}
			bonus += Math.max(monotonicityUp, monotonicityDown);
		}*/
		
		if (blankCount == 0) {
			gameOver = true;
			for (State s : getStates(-1)) {
				if (s.blankCount != 0) {
					gameOver = false;
					break;
				}
			}
		}
		
		if (bonus < 0) {
			bonus = 0;
		}
		setEval = true;
	}
	
	public ArrayList<State> getStates(int i) {

		ArrayList<State> states = new ArrayList<State>();
		if (i == 1) {		
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					if (map[x][y].val == 0) {
						states.add(new State(this.map, new Number(new Movement(x, y), 2), eval, false));
						states.add(new State(this.map, new Number(new Movement(x, y), 4), eval, true));
					}
				}
			}
	}
		if (i == -1) {
			for (Movement m : Game.allMovements) {
				states.add(new State(map, m, eval));
			}
		}

		return states;
	}
	
	public void move() {
		moveMade = false;
		HashMap<Integer, Number> last = new HashMap<Integer, Number>(4);
		for(int i = 0; i < 4; i++) {
			last.put(i, null);
		}
		for (int x = direction.getStart(); direction.checkEnd(x); x = direction.next(x)) {
			for (int y = direction.getStart(); direction.checkEnd(y); y = direction.next(y)) {
				int row = direction.getRow(x, y);
				Number lastNumber = last.get(row);
				//if this is first row
				if (lastNumber == null) {
					last.put(row, map[x][y]);
					map[x][y].newSpot.set(x, y);
					continue;
				}
				//if this is blank
				if (map[x][y].val == 0) {
					continue;
				}
				//if no other Number have been encountered yet
				if (lastNumber.val == 0) {
					map[x][y].newSpot.set(lastNumber.newSpot);
					map[lastNumber.newSpot.x][lastNumber.newSpot.y] = map[x][y];
					last.put(row, map[x][y]);
					map[x][y] = new Number(new Movement(x, y), 0);
					moveMade = true;
					continue;
				}
				Movement temp = direction.getNextBlank(lastNumber.newSpot.x, lastNumber.newSpot.y);
				//if they match
				if (lastNumber.val == map[x][y].val) {
					map[x][y].newSpot.set(lastNumber.newSpot);
					map[x][y].val *= 2;
					eval += map[x][y].val;
					map[x][y] = new Number(new Movement(x, y), 0);
					if (temp != null) {
						last.put(row, map[temp.x][temp.y]);						
					}
					else {
						System.err.println("error: temp is null (166)");
					}
					moveMade = true;
					continue;
				}
				//if they don't match
				if (temp == null) {
					System.err.println("error: temp is null (173)");
					continue;
				}
				if (temp.x == x && temp.y == y) {
					map[x][y].newSpot.set(x, y);
					last.put(row, map[x][y]);
					continue;
				}
				map[x][y].newSpot.set(temp.x, temp.y);
				map[temp.x][temp.y] = map[x][y];
				last.put(row, map[x][y]);
				map[x][y] = new Number(new Movement(x, y), 0);
				moveMade = true;
			}	
		}
	}
}
