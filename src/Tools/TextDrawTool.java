package Tools;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pageData.Content;
import pageData.TextBox;

public class TextDrawTool implements DrawTool {
	
	private JPanel panel;
	private JPanel rect;
	
	private int x;
	private int y;
	
	public TextDrawTool(JPanel panel)
	{
		this.panel = panel;
		this.rect = new JPanel();
		rect.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void drawNewContent(int x2, int y2) {
		this.x = x2;
		this.y = y2;
		panel.add(rect);
		rect.setBounds(x,y,0,0);
		rect.setVisible(true);
	}

	@Override
	public void editNewContent(int x2, int y2) {
		Rectangle newBounds = getRectangle(x2, y2);
		rect.setBounds(newBounds);
	}

	@Override
	public Content getNewContent(int x2, int y2) {
		//remove from panel
		panel.remove(rect);
		rect.setVisible(false);
		
		//build the Content
		Rectangle bounds = getRectangle(x2, y2);
		TextBox box = new TextBox(bounds.x, bounds.y);
		if(bounds.height > 3 && bounds.width > 3)
		{
			box.setWidth(bounds.width);
			box.setHeight(bounds.height);
		}
		return box;
	}
	
	private Rectangle getRectangle(int x2, int y2)
	{
		int width = x2 - x;
		int height = y2 - y;
		int startX = x;
		int startY = y;
		
		if(width < 0)
		{
			startX += width;
			width *= -1;
		}
		if(height < 0)
		{
			startY += height;
			height*= -1; 
		}
		return new Rectangle(startX, startY, width, height);
	}
}
