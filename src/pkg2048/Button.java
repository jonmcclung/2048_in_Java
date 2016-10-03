package pkg2048;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Button extends JComponent implements MouseListener{
	
	public int width = 200, height = 100, arc = 30, arcOffset = arc/6-3;
	JPanel textPanel;
	JLabel message;
	public boolean selected = false;
	BufferedImage Img, selectedImg;
	String msg;
	boolean pressed = false;
	Main main;
	String command;
	
	Button (String msg, String command, Main main) {
//		setPreferredSize(new Dimension(width-arc*2+6, height-arc*2+60));
		setPreferredSize(new Dimension(width-arc*2+6, height-arc*2+6));
		try {
			Img = ImageIO.read(this.getClass().getResourceAsStream("res/button.png"));
		}
		catch (IOException e){
			e.printStackTrace();
		}
		try {
			selectedImg = ImageIO.read(this.getClass().getResourceAsStream("res/selected.png"));
		}
		catch (IOException e){
			e.printStackTrace();
		}
		this.msg = msg;
		this.command = command;
		this.main = main;
		setBackground(Main.dark);
		addMouseListener(this);
		//mouse = new Mouse(this);
	}
	
	public void paintComponent(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
               RenderingHints.VALUE_RENDER_QUALITY);		

        g2.setRenderingHints(rh);
        
		g2.setColor(getBackground());
		g2.fill(new Rectangle2D.Double(0, 0, width, height));	
		if (selected) {
			g2.setColor(Main.light);
			g2.fill(new RoundRectangle2D.Double(arcOffset, arcOffset, width-arc*2, height-arc*2, arc, arc));	
			g2.setColor(Main.dark);
		}
		else {
			g2.setColor(Main.dark);
			g2.fill(new RoundRectangle2D.Double(arcOffset, arcOffset, width-arc*2, height-arc*2, arc, arc));	
			g2.setColor(Main.light);
			g2.setStroke(new BasicStroke(4f));
			g2.draw(new RoundRectangle2D.Double(arcOffset, arcOffset, width-arc*2, height-arc*2, arc, arc));
		}
		if (msg.length() > 14) {
			g2.setFont(Main.font.deriveFont(16f));
			g2.drawString(msg, 85-msg.length()*5, (int)(((height-arc)/2))-4);
		}
        else if (msg.length() > 9) {
        	g2.setFont(Main.font.deriveFont(20f));
    	    g2.drawString(msg, 70-msg.length()*5, (int)(((height-arc)/2))-4);
            }
       	else {
    	  g2.setFont(Main.font.deriveFont(24f));
    	    g2.drawString(msg, 63-msg.length()*4, (int)(((height-arc)/2))-4);
        }

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		selected = true;
		repaint();
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		selected = false;
		repaint();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		main.doAction(command);
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
