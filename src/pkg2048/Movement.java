package pkg2048;

public class Movement {
	int x, y;
	
	Movement(int x, int y) {
		this.x = x; this.y = y;
	}
	
	Movement(Movement m) {
		x = m.x;
		y = m.y;
	}
	
	
	public void add(Movement m1) {
		x += m1.x;
		y += m1.y;
	}
	
	public void set(int x, int y) {
		this.x = x; this.y = y;
	}
	
	public void set(Movement m) {
		x = m.x; y = m.y;
	}
	
	public boolean isEqual(int tx, int ty) {
		if (x != tx) return false;
		if (y != ty) return false;
		return true;
	}
	
	public boolean isPositive() {
		if (x < 0 || y < 0)
			return false;
		return true;
	}
	
	public boolean isEqual(Movement m) {
		if (x != m.x) return false;
		if (y != m.y) return false;
		return true;
	}	
	
	public Movement opposite() {
		Movement m = new Movement(this);
		m.x *= -1;
		m.y *= -1;
		return m;
	}
	
	public boolean isOpposite(Movement m) {
		if (x*m.x == -1) return true; 
		if (y*m.y == -1) return true; 
		return false;
	}	
	
	public Movement negative() {
		Movement m = new Movement(0, 0);
		if (x < 0) {
			m.x = x;
		}
		if (y < 0) {
			m.y = y;
		}
		return m;
	}
	

	
	public int getStart() {
		if (x > 0 || y > 0) {
			return 3;
		}
		else {
			return 0;
		}
	}
	
	public boolean checkEnd(int i) {
		if (x > 0 || y > 0) {
			return i > -1;
		}
		else {
			return i < 4;
		}
	}
	
	public int next(int i) {
		return i - y - x;
	}
	
	public int getRow(int xa, int ya) {
		return (xa*Math.abs(y)+ya*Math.abs(x));
	}
	
	public Movement getNextBlank(int xa, int ya) {
		Movement m = new Movement(xa-x, ya-y);
		if (m.x < 0 || m.y < 0 || m.x > 3 || m.y > 3) {
			return null;
		}
		return m;
	}
	
	public Movement positive() {
		Movement m = new Movement(0, 0);
		if (x > 0) {
			m.x = x;
		}
		if (y > 0) {
			m.y = y;
		}
		return m;
	}
	
	public int difference(Movement m) {
		return Math.abs(x-m.x)+Math.abs(y-m.y);
	}
	
	public String toString() {
		return ("("+x+", "+y+")");
		/*switch(x*10 + y) {
		case 1: {
			return "down";
		}
		case 10: {
			return "left";
		}
		case -1: {
			return "up";
		}		
		case -10: {
			return "right";
		}
		}*/
	}
}
