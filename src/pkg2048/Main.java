package pkg2048;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Main extends Canvas implements Runnable {
	
	private static JFrame frame;
	static JLabel label;
	static JLabel pointLabel;
	static JLayeredPane pane;
	private static Game game;
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int xCenter = (int)screenSize.getWidth()/2;
	static final int yCenter = (int)screenSize.getHeight()/2;
	private static int 	fps = 512;
	static int fpm = 64;
	static int animationCount = 0;
	static Random rand = new Random();
	private boolean running = false;
	static Color dark = Color.decode("0x9e9b8a"), light = Color.decode("0xfff9c1");
	static Font font;
	private PopupWindow popup;
	private JPanel gamePanel;
	boolean playFlag = false;
	static boolean wonFlag = false;
	private Reset reset;
	private int total, average, gamesPlayed;
	
	public void reStarter() {
		if (popup == null) {
			reset.ready = false;
			playFlag = false;
			String message = "Would you like to quit this game and start another?";
			popup = new PopupWindow(message, 480, 220, 140, new String[]{"Restart as Human", "Restart with AI", "Cancel"},
					new String[]{"restartHuman", "restartAI", "continue"}, this);
		}
	}
	
	public void won() {
		if (popup == null) {
			reset.ready = false;
			playFlag = false;
			String message = "Nice Job! You won!\n Would you like to keep playing?";
			popup = new PopupWindow(message, 480, 250, 140, new String[] {"Restart as Human", "Restart with AI",
                    "Keep Playing", "Exit"}, new String[] {"restartHuman", "restartAI", "continue", "exit"}, this);
		}
	}
	
	public void gameOver() {
		if (popup == null) {
			reset.ready = false;
			playFlag = false;
			String message = "Game Over!\nPoints: " + Game.points;
			popup = new PopupWindow(message, 480, 250, 140, new String[] {"Restart as Human", "Restart with AI", "Cancel"},
                    new String[] {"restartHuman", "restartAI", "exit"}, this);
		}
	}
	
	Main() throws Exception {
		frame = new JFrame("2048");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(xCenter-70, yCenter-50);
		frame.setVisible(true);
		pane = new JLayeredPane();

		InputStream fontFile = this.getClass().getResourceAsStream("res/ebrimaBold.ttf");
		assert fontFile != null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		} catch (IOException|FontFormatException e) {
			e.printStackTrace();
			throw new Exception("failure to load font");
		}
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("res/ebrimaBold.ttf")));


		String message = "Welcome to 2048!\n"
				+ "Your goal is to create one 2048 tile by joining other tiles using the arrow keys.";
		gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout());
		pane.setLayer(gamePanel, 0); // new Integer(0) ??
		pane.add(gamePanel);
		label = labelMaker(24f);
		pointLabel = labelMaker(24f);
		game = new Game(true);
		frame.setLocation(pkg2048.Main.xCenter-240, pkg2048.Main.yCenter-240);
		MigLayout mig = new MigLayout("ins 4,wrap 3","","");
		JPanel bar = new JPanel(mig);
		bar.setBackground(dark);
		gamePanel.add(bar, BorderLayout.SOUTH);
		bar.add(pointLabel, "gapright 4");
		bar.add(label, "push");
		reset = new Reset();
		bar.add(reset, "w 30!,h 30!");
		bar.setSize(new Dimension((int)game.getWidth(), 38));
		gamePanel.add(game, BorderLayout.CENTER);
		gamePanel.setBounds(0, 0, (int) game.getWidth(), (int) (game.getHeight() + bar.getHeight()));
		pane.setPreferredSize(new Dimension(gamePanel.getWidth(), gamePanel.getHeight()));
		popup = new PopupWindow(message, 480, 180, 100, new String[] {"Play", "Watch AI Play"}, new String[] {"play", "aiPlay"}, this);
		frame.add(pane);
		frame.pack();
	}

	private JLabel labelMaker(float size) {
		JLabel l = new JLabel();
		l.setBackground(dark);
		l.setForeground(light);
		l.setOpaque(true);
		l.setFont(font.deriveFont(size));
		return l;
	}
	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new Main().start();
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}


	public void doAction(String command) {
		switch(command) {
			case "play" : {if (!playFlag) {Game.aiPlaying = false; play(); playFlag = true; break;}}
			case "aiPlay" : {if (!playFlag) {Game.aiPlaying = true; play(); playFlag = true; break;}}
			case "continue": {if (!playFlag) {play(); playFlag = true; break;}}
			case "restart": {if (!playFlag) {play(); game.newGame(); play(); playFlag = true; break;}}
			case "restartHuman": {if (!playFlag) {Game.aiPlaying = false; game.newGame(); play(); playFlag = true; break;}}
			case "restartAI": {if (!playFlag) {Game.aiPlaying = true; game.newGame(); play(); playFlag = true; break;}}
			case "exit": {System.exit(0); break;}
		}
	}

	@Override
	public void run() {

		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/(double)fps;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (running)
		{
//			System.out.println(reset.ready);
			long now = System.nanoTime();
			delta += (now - lastTime)/nsPerTick;
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {
				delta -= 1;
				shouldRender = true;
			}
			if (shouldRender) {
//				reset.ready = false;
				if (reset.pressed) {
					reset.pressed = false;
					Game.shouldRender = false;
					System.out.println("reset was pressed");
					Game.ready = false;
					reStarter();
				}
				if (game.GameOver) {
					Game.shouldRender = false;
					total += Game.points;
					gamesPlayed++;
					average = total/gamesPlayed;
					System.out.println("gameover, points: "+Game.points+" moves: "+Game.moves+" running average: "+average+" games Played: "+gamesPlayed);
					Game.ready = false;
					game.GameOver = false;
					gameOver();
				}
				if (game.won && !wonFlag) {
					Game.shouldRender = false;
					System.out.println("you won");
					Game.ready = false;
					wonFlag = true;
					game.won = false;
					won();
				}
				if (animationCount>fpm) {
					if (!game.moveFlag) {
						Game.shouldRender = false;
					}
					animationCount=0;
					if (Game.aiPlaying && !Game.thinking && Game.ready) {
							game.takeAITurn();
					}
				}
				if (Game.shouldRender) {
					game.render();
					animationCount++;
				}
//				reset.ready = true;
			}
		}

		if (System.currentTimeMillis() - lastTimer >= 1000) {
			lastTimer += 1000;
		}
	}

	public void play() {
		popup.removeAll();
		frame.remove(popup);
		popup = null;
		reset.ready = true;
		pane.removeAll();
		pane.setLayer(gamePanel, new Integer(0));
		pane.add(gamePanel);
		reset.ready = true;
		Game.ready = true;
		Game.shouldRender = true;
		frame.revalidate();
		frame.repaint();
		game.requestFocusInWindow();
		if (Game.aiPlaying) {
			game.takeAITurn();
		}
	}
}
