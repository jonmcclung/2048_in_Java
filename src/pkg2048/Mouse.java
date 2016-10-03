package pkg2048;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
	
public class Mouse implements MouseListener{
	
	Button parent;
	boolean pressed = false;
	
	Mouse(Button parent) {
		this.parent = parent;
		parent.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		parent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		parent.selected = true;
		parent.repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		parent.selected = false;
		parent.repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		pressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
}
