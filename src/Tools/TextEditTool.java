package Tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import pageData.Content;
import pageData.TextBox;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */



public class TextEditTool implements EditTool{
	private TextBox box;
	private JTextPane pane;
	private JPanel panel;
	
	public TextEditTool(TextBox box, JTextPane pane, JPanel panel)
	{
		this.pane = pane;
		this.box = box;
		this.panel = panel;
	}

	@Override
	public void startEdit() {
		pane.setEditable(true);
		pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		/*
		pane.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {}
			@Override
			public void mouseDragged(MouseEvent arg0) {
				//TODO:BUGGY
				Rectangle rec = pane.getBounds();
				rec.x = arg0.getX();
				rec.y = arg0.getY();
				pane.setBounds(rec);
			}
		});*/
		
	}

	@Override
	public Content stopEdit() {
		pane.setEditable(false);
		pane.setBorder(BorderFactory.createEmptyBorder());
		pane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		String text = pane.getText(); 
		if(text.equals(""))
			return null;
		
		box.setText(text);
		box.setX(box.getX());
		box.setY(box.getY());
		box.setWidth(box.getWidth());
		box.setHeight(box.getHeight());
		return box;
	}

	@Override
	public int getContentNumber() {
		return box.getNumber();
	}

	@Override
	public void setToolbar(JToolBar toolbar) {
		JLabel label = new JLabel("Schriftgröße:");
		toolbar.add(label);
		label.setVisible(true);
		
		JComboBox<Integer> comboBox = new JComboBox<>();
		for (int i = 0; i < 100; i++) {
			comboBox.addItem(i);
		}
		comboBox.setSelectedItem(box.getFontSize());
		toolbar.add(comboBox);
		comboBox.setVisible(true);
	}
}
