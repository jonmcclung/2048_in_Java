package pkg2048;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Num extends JComponent{
	
	BufferedImage img = null;
	int val = 0, xOffset = 0, yOffset = 0, spaces = 0, oldX = 0, oldY = 0, mergX, mergY, mergeSpaces;
	Movement offset, oldSpot, newSpot, mergeSpot;
	boolean merg = false;
	
	public static int log(int base, int number) {
		int result = (int) (Math.log(number)/Math.log(base));
		return result;
	}
	
	public void doubleVal() {
		setValue(val*2);
	}
	
	public Num (Num n) {
		this(n.newSpot.x, n.newSpot.y);
		this.val = n.val;
	}
	
	public void clear() {
		val = 0;
		xOffset = 0;
		yOffset = 0;
		spaces = 0;
	}
	
	Num(int x, int y) {
		this.newSpot = new Movement(x, y);
		this.oldSpot = new Movement(x, y);
		this.mergeSpot = new Movement(-1, -1);
		val = 0;
	}
	
	Num(int x, int y, int val) {
		this.newSpot = new Movement(x, y);
		this.val = val;
	}
	
	Num(boolean isBlank) {
		val = 0;
	}
	
	Num(int i) {
		val = (int)Math.pow(2,i);
		img = Game.numbers[log(2, val)-1];
	}
	
	public void setValue(int i) {
		val = i;
		img = Game.numbers[log(2, val)-1];
	}
	
	public String toString() {
		return "("+newSpot.x+", "+newSpot.y+")";
	}
}
