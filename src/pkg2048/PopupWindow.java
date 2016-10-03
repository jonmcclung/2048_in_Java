package pkg2048;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import net.miginfocom.swing.MigLayout;

public class PopupWindow extends JPanel{
	public static Font font;
	public static int xOffset, yOffset, width, textHeight;
	public Button[] button;
	public String [] commands;
	public int bw = 90, bh = 35;
	Main main;
	
	PopupWindow (String msg, int width, int height, int textHeight, String[] buttonText, String [] commands, Main main) {
		this.main = main;
		Game.ready = false;
		Game.shouldRender = false;
		button = new Button[buttonText.length];
		this.commands = commands;
		PopupWindow.width = width;
		PopupWindow.textHeight = textHeight;
		xOffset = (int) ((Main.pane.getPreferredSize().getWidth()-width)/2);
		yOffset = (int) ((Main.pane.getPreferredSize().getHeight()-height)/2);
		setBackground(Main.dark);
		font = Main.font;
		setLayout(new BorderLayout());
		JTextPane text = new JTextPane();
		StyledDocument doc = new DefaultStyledDocument();
		text.setDocument(doc);
		
		text.setFont(font.deriveFont(24f));
		text.setEditable(false);
		text.setBackground(Main.dark);
		text.setForeground(Main.light);
		text.setText(msg);
		add(text, BorderLayout.NORTH);
		JPanel buttonPanel;
		add(buttonPanel = new JPanel(new MigLayout("ins 0, ","[50%][50%]", "")), BorderLayout.CENTER);
		buttonPanel.setBackground(Main.dark);
		String gap = ", gapleft 8, gaptop 8";
		String dimensions = "";
		String wrap = "";
		for (int i = 0; i < buttonText.length; i++) {
			if (i < 6) {
				if (i%2 == 1 && i != 0) wrap = ", wrap";
				else wrap = "";
				if (buttonText.length != 1) {
					if (i%2 == 1) dimensions = "left"+gap+wrap;
					else dimensions = "right"+gap+wrap;
				}
				if (i == buttonText.length-1 && buttonText.length%2 == 1) dimensions = "span, center"+gap;

				button[i] = new Button(buttonText[i], commands[i], main);
				buttonPanel.add(button[i], dimensions);
			}
		}
		
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		setBounds(xOffset, yOffset, width, height);
		Main.pane.setLayer(this, new Integer(1));
		Main.pane.add(this);
	}
	
	
}
