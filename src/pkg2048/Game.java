package pkg2048;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Game extends JPanel {
	
	BufferedImage bg = null;
	BufferedImage spriteSheet = null;
	static BufferedImage[] numbers = new BufferedImage[16];
	static int points = 0, moves = 0, spriteWidth = 120;
	//public static Num blank;

	private static Num[][] map;
	boolean setSpaces = false, moveMade = false, won = false, GameOver = false, moveFlag = false;
	static boolean ready = false, shouldRender = false, aiPlaying, thinking;
	private Movement direction = new Movement(0, 0);
	private static Movement up = new Movement(0, -1), down = new Movement(0, 1), left = new Movement(-1, 0), right = new Movement(1, 0);
	static Movement[] allMovements = {up, right, down, left};
	private int aiChoice = 0, time = 0, timeLimit = 400;
	private long lastTime = System.currentTimeMillis();
	
	Game (boolean AI) {
		setSize(new Dimension(4*120, 4*120));
		//getting images
		try {
			bg = ImageIO.read(this.getClass().getResourceAsStream("res/bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			spriteSheet = ImageIO.read(this.getClass().getResourceAsStream("res/spriteSheet2048.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//setting up image array
		for (int i = 0; i<numbers.length;i++) {
			int x = i%4;
			int y = i/4;
			numbers[i] = spriteSheet.getSubimage(x*106, y*106, 106, 106);
		}

		newGame(AI);
		class KeyAction extends AbstractAction {
			String key;

			KeyAction(String key) {this.key = key;}
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ready && !moveFlag && !moveMade) {
					switch(key) {
					case "UP": {
						direction = up; break;
					}
					case "DOWN": {
							direction = down; break;
						}
					case "LEFT": {
						direction = left; break;
					}
					case "RIGHT": {
						direction = right; break;
					}
				}
				moveFlag = true;
				move();
			}
			}	
		}
		String [] keys= {"UP","DOWN","LEFT","RIGHT"};
		for (int i = 0; i < keys.length; i++) {
			String s = keys[i];
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(s), s);
			getActionMap().put(s, new KeyAction(s));
		}
	}
	
	public void newGame(boolean AI) {
		Main.wonFlag = false;
		GameOver = false;
		won = false;
		points = moves = 0;
		map = new Num[4][4];
		int r = Main.rand.nextInt(4*4);
		int r1 = Main.rand.nextInt(4*4);
		if (r==r1) r++;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				map[x][y] = new Num(x, y);
				if (x+(y*4) == r||x+(y*4) == r1) {
					map[x][y].setValue((Main.rand.nextInt(2)+1)*2);
				}
			}
		}

		Main.label.setText(" Moves: "+Integer.toString(moves));
		Main.pointLabel.setText(" Points: "+Integer.toString(points));
		repaint();

		aiPlaying = AI;
	}

	public void newGame() {newGame(aiPlaying);}
	
	public static int log(int base, int number) {
		if (number == 0) {
			return 0;
		}
		int result = (int) (Math.log(number)/Math.log(base));
		return result;
	}
	
	public void move() {
		moveMade = false;
		HashMap<Integer, Num> last = new HashMap<Integer, Num>(4);
		for(int i = 0; i < 4; i++) {
			last.put(i, null);
		}
		for (int x = direction.getStart(); direction.checkEnd(x); x = direction.next(x)) {
			for (int y = direction.getStart(); direction.checkEnd(y); y = direction.next(y)) {
				int row = direction.getRow(x, y);
				Num lastNum = last.get(row);
				//if this is first row
				if (lastNum == null) {
					last.put(row, map[x][y]);
					map[x][y].newSpot.set(x, y);
					continue;
				}
				//if this is blank
				if (map[x][y].val == 0) {
					continue;
				}
				//if no other Num have been encountered yet
				if (lastNum.val == 0) {
					map[x][y].newSpot.set(lastNum.newSpot);
					map[lastNum.newSpot.x][lastNum.newSpot.y] = map[x][y];
					last.put(row, map[x][y]);
					map[x][y] = new Num(x, y);
					moveMade = true;
					continue;
				}
				Movement temp = direction.getNextBlank(lastNum.newSpot.x, lastNum.newSpot.y);
				//if they match
				if (lastNum.val == map[x][y].val) {
					map[x][y].merg = true;
					map[x][y].mergeSpot.set(lastNum.oldSpot);
					map[x][y].newSpot.set(lastNum.newSpot);
					map[lastNum.newSpot.x][lastNum.newSpot.y] = map[x][y];
					points += map[x][y].val;
					map[x][y] = new Num(x, y);
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
				map[x][y] = new Num(x, y);
				moveMade = true;
			}	
		}
		shouldRender = true;
	}

	
	public int expectimax(State state, int depth) {
		int result = state.getEval();
		
		if (state.gameOver) {
			return depth*-1;
		}
		if (depth == 0) {
			return result;
		}		
		
		int count = 0, total = 0;
		for (State checking : state.getStates(1)) {
			result = minimax(checking, depth - 1);
			if (checking.made4) {
				count += 1;
				total += result;
			}
			else {
				count += 9;
				total += 9*result;
			}
		}
		return (total/count);
	}
	
	public int minimax(State state, int depth) {
		
		int result = state.getEval();
		if (state.gameOver) {
			return depth*-1;
		}
		if (depth == 0) {
			return result;
		}		
		int bestValue = -1;
		for (State checking : state.getStates(-1)) {
			result = expectimax(checking, depth - 1);
			if (result > bestValue ) {
				bestValue = result;
			}
		}
		return bestValue;
	}
	
	public void takeAITurn() {
		thinking = true;
		int depth = 0;
		//System.out.println("\n\n\n\n");
		Movement bestChoice = up;
		Movement tempBestChoice = null;
		Number[][] aiMap = new Number[4][4]; 
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				aiMap[x][y] = new Number(map[x][y].newSpot, map[x][y].val);
			}	
		}
		/*State original = new State(aiMap, 0);
		original.printMonotonicity();*/
		loop:
		while (keepThinking() && depth < 11) {
			tempBestChoice = null;
			int bestValue = -11;
			for (Movement m : allMovements) {
				if (!keepThinking() && tempBestChoice != null) {
					tempBestChoice = null;
					break loop;
				}
				State state = new State(aiMap, m, 0);
				if (!state.moveMade) {
					continue;
				}
				int result = expectimax(state, depth);
				//System.out.println(m.toString()+" result: "+result);

				if (result > bestValue) {
					bestValue = result;
					tempBestChoice = m;
				}
			}
			if (tempBestChoice != null) {
				bestChoice = tempBestChoice;
			}
			System.out.println("depth: "+depth + " result: "+bestValue);
			depth++;
		}
		direction = bestChoice;
		move();
		thinking = false;
	}
	
	public boolean keepThinking() {
			time += (System.currentTimeMillis() - lastTime);
			lastTime = System.currentTimeMillis();
			if (time >= timeLimit) {
				time = 0;
				return false;
			}
		return true;
	}
	
	public void render() {
		boolean gameOver = false;
		if (shouldRender) {
				if (!setSpaces) {
					for (int y = 0;y<4;y++) {
						for (int x=0;x<4;x++) {
							map[x][y].spaces = (map[x][y].newSpot.difference(map[x][y].oldSpot));
							if (map[x][y].merg) {
								map[x][y].mergeSpaces = map[x][y].newSpot.difference(map[x][y].mergeSpot);
							}
						}
					}
					setSpaces = true;
					}
			if (Main.animationCount >= Main.fpm) {
				setSpaces = false;
				int r, blankCount = 0;
				
				for (int y = 0; y < 4; y++) {
					for (int x = 0; x < 4; x++) {
						
						map[x][y].oldSpot.set(x, y);
						
						map[x][y].spaces = 0;
						map[x][y].mergeSpaces = 0;
						
						if (map[x][y].merg) {
							map[x][y].doubleVal();
							map[x][y].merg = false;
						}

						if (map[x][y].val == 2048) {
							won = true;
						}
						if (map[x][y].val == 0) {
							blankCount++;
						}
					}
				}
				if (blankCount != 0 && moveMade) {
				r = Main.rand.nextInt(blankCount);
				makeLoop:
				for (int y=0;y<4;y++) {
					for (int x=0;x<4;x++) {
						if (map[x][y].val == 0) {
							r--;
						}
						if (r == -1) {
							map[x][y].setValue(Main.rand.nextInt(10) == 0 ? 4 : 2);
							moves++;
							Main.label.setText(" Moves: "+Integer.toString(moves));
							Main.pointLabel.setText(" Points: "+Integer.toString(points));
							break makeLoop;
						}
						
					}
				}
				}
				if (blankCount == 1 && moveMade) {
					gameOver = true;
					Num last = null;
					checkingLoop:
					for (int checkingType = 0; checkingType < 2; checkingType++) {
						int x = 0; int y = 0; last = null;
						for (int i = 0; i < 4*4; i++) {
							switch(checkingType) {
							case 0: {
								x++;
								if (x == 4) {
									last = null;
									x = 0;
									y++;
								}
								break;
							}
							case 1: {
								y++;
								if (y == 4) {
									last = null;
									y = 0;
									x++;
								}
							}
							}
							if (i == 0) {
								x = 0;
								y = 0;
							}
							if (last == null) {
								last = map[x][y];
								continue;
							}
							if (last.val == map[x][y].val) {
								gameOver = false;
								break checkingLoop;
							}
							last = map[x][y];
						}
					}
				}
				moveMade = false;
				moveFlag = false;
			}
			if (gameOver) {
				GameOver = true;
			}
			repaint();
	}	
		else {
			moveMade = false;
			moveFlag = false;
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(bg,0,0,null);
		int animationXOffset = (direction.x*(Main.animationCount*spriteWidth/Main.fpm));
		int animationYOffset = (direction.y*(Main.animationCount*spriteWidth/Main.fpm));
		for (int y =0;y<4;y++) {
			for(int x=0;x<4;x++) {
				if (map[x][y].val == 0) {
					continue;
				}
				g.drawImage(
						  map[x][y].img
						, map[x][y].oldSpot.x*120 + 7 + animationXOffset*map[x][y].spaces
						, map[x][y].oldSpot.y*120 + 7 + animationYOffset*map[x][y].spaces
						, null);
				if (map[x][y].merg) {
					g.drawImage(
							  map[x][y].img
							, map[x][y].mergeSpot.x*120 + 7 + animationXOffset*map[x][y].mergeSpaces
							, map[x][y].mergeSpot.y*120 + 7 + animationYOffset*map[x][y].mergeSpaces
							, null);
				}
			}
		}
	}
}
