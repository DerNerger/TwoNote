package pageData;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Diese Klasse dient der internen Speicherung einer TextBox und ist
 * von Content abgeleitet. Der Content wird durch diese Klasse um
 * einen Text, eine Schriftgröße, eine Schriftfarbe und eine 
 * Hintergrundfarbe ergänzt. Diese Klasse kann auf einem vorgegebenem JPanel
 * gezeichnet werden. 
 * */
public class TextBox extends Content implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//default values
	private static final int DEFAULT_WIDTH = 200;
	private static final int DEFAULT_HEIGHT = 30;
	private static final int DEFAULT_FONTSIZE= 15;
	private static final Color DEFAULT_FONTCOLOR = Color.black;
	private static final Color DEFAULT_BACKGROUNDCOLOR = Color.black;
	private static final String DEFAULT_TEXT = "";
	
	private String text;
	private int fontSize;
	private Color fontColor;
	private Color backgroundColor;

	//constructor	
	/**
	 * Es wird eine TextBox mit Defaultwerten an einer vorgegebenen Position erstellt.
	 * @param x die x-Koordinate
	 * @param y die y-Koordinate
	 * */
	public TextBox(int x, int y) {
		super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, ContentType.textBox);
		initDefault();
	}

	//copy-constructor
	/**
	 * Der Copy-Constructor dient dazu eine tiefe Kopie einer TextBox anzulegen.
	 * @param t Die TextBox welche kopiert werden soll.
	 * */
	public TextBox(TextBox t)
	{
		super(t.x, t.y, t.width, t.height, ContentType.textBox);
		this.backgroundColor = t.backgroundColor;
		this.text = t.text;
		this.fontColor = t.fontColor;
		this.backgroundColor = t.backgroundColor;
	}
	
	//getter and setter
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	//methods
	private void initDefault()
	{
		this.text = DEFAULT_TEXT;
		this.backgroundColor = DEFAULT_BACKGROUNDCOLOR;
		this.fontColor = DEFAULT_FONTCOLOR;
		this.fontSize = DEFAULT_FONTSIZE;
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
	}
	
	/**
	 * Der Text der TextBox wird zurückgegeben.
	 * @return TextBox.Text
	 * */
	@Override
	public String toString()
	{
		return text;
	}
	
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public Component draw(JPanel panel) {
		if(text.length() == 0)
			return null;
		
		//create Label
		JLabel label = new JLabel(text);
		label.setForeground(fontColor);
		label.setBackground(backgroundColor);		
		label.setFont(new Font("Ubuntu", 0, fontSize));
		
		//add label to panel
		panel.add(label);
		label.setBounds(x,y,width, height);
		return label;
	}
}
