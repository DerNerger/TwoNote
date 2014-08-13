package Tools;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pageData.Content;
import pageData.TextBox;

/**
 * Das TextEditTool dient zum Bearbeiten einer TextBox.
 * Dazu zeichnet sich das Tool auf ein vorgegebenes JPanel.
 * Alle UI-Elemente, die benötigt werden um eine TextBox zu 
 * modifizieren, werden vom TextEditTool erstellt und später
 * wieder entfernt. Es kann ein Listener hinzugefügt werden um 
 * informiert zu werden, wenn das Tool beendet wird. Das Tool 
 * wird beendet, falls es den Focus verliert, mit Enter beendet wird
 * oder das zu bearbeitende Element entfernt wird.
 * */
public class TextEditTool implements Tool{
	
	//swing
	private JPanel panel;
	private JTextField textField;
	private JComboBox<Integer> fontSize;
	private JLabel fontSizeLabel;
	private JButton buttonChooseFontColor;
	private JColorChooser colorChooser;
	
	//editor
	private TextBox content;
	private ToolReadyHandler handler;
	private boolean isClosed;
	
	//create 
	/**
	 * Es wird ein neues TextEditTool gestartet.
	 * @param panel das JPanel auf dem das Tool gezeichnet wird
	 * @param content der Seiteninhalt, welcher vom Tool bearbeitet werden soll.
	 * */
	public TextEditTool(JPanel panel, TextBox content)
	{
		this.panel = panel;
		this.content = content;
		this.textField = new JTextField();
		this.isClosed = false;
	}
	

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void drawTool() {
		//colorChooser
		colorChooser = new JColorChooser();
		panel.add(colorChooser);
		colorChooser.setBounds(20, 30, 700, 400);
		colorChooser.setVisible(false);
		
		//font size
		fontSizeLabel = new JLabel("Schriftgröße: ");
		panel.add(fontSizeLabel);
		fontSizeLabel.setBounds(20, 0, 100, 30);
		
		fontSize = new JComboBox<>();
		for (int i = 0; i < 100; i++) {
			fontSize.addItem(i);
		}
		fontSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textField.setFont(new Font("Ubuntu", 0, (int)fontSize.getSelectedItem()));
				content.setFontSize((int)fontSize.getSelectedItem());
			}
		});
		
		fontSize.setSelectedItem(content.getFontSize());
		panel.add(fontSize);
		fontSize.setBounds(120, 0, 100, 30);
		
		//textField
		
		//init listeners
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				endEdit(ToolEndsType.ExitTool);
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			  @Override
			public void keyPressed(KeyEvent evt) 
			  {
				  if(evt.getKeyCode() == KeyEvent.VK_RIGHT )
				  {
					  content.setX(content.getX() + 2);
					  Rectangle rect = textField.getBounds();
					  rect.setBounds(rect.x + 2, rect.y, rect.width, rect.height);
					  textField.setBounds(rect);
				  }
				  if(evt.getKeyCode() == KeyEvent.VK_LEFT)
				  {
					  content.setX(content.getX() - 2);
					  Rectangle rect = textField.getBounds();
					  rect.setBounds(rect.x - 2, rect.y, rect.width, rect.height);
					  textField.setBounds(rect);
				  }
				  if(evt.getKeyCode() == KeyEvent.VK_UP)
				  {
					  content.setY(content.getY() - 2);
					  Rectangle rect = textField.getBounds();
					  rect.setBounds(rect.x, rect.y - 2, rect.width, rect.height);
					  textField.setBounds(rect);
				  }
				  
				  if(evt.getKeyCode() == KeyEvent.VK_DOWN)
				  {
					  content.setY(content.getY() + 2);
					  Rectangle rect = textField.getBounds();
					  rect.setBounds(rect.x, rect.y + 2, rect.width, rect.height);
					  textField.setBounds(rect);
				  }
				  
				  if(evt.getKeyCode() == KeyEvent.VK_DELETE)
				  {
					  textField.setText("");
					  endEdit(ToolEndsType.ExitTool);
				  }
			  }
		});
		
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent evt)
			{
				Component c = evt.getOppositeComponent();
				if(c!=fontSize && c!=buttonChooseFontColor && c!=colorChooser && c!=textField)
					endEdit(ToolEndsType.FocusLost);
			}
		});
		
		textField.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				content.setX(arg0.getXOnScreen() - 60);
				content.setY(arg0.getYOnScreen() - 180);
				Rectangle rect = textField.getBounds();
				rect.setBounds(arg0.getXOnScreen() - 60, arg0.getYOnScreen() - 180, rect.width, rect.height);
				textField.setBounds(rect);
			}
		});
		//end init listeners
		
		textField.setText(content.getText());
		textField.setBounds(content.getX(), content.getY(),content.getWidth(), content.getHeight());
		textField.setFont(new Font("Ubuntu", 0, (int)fontSize.getSelectedItem()));
		textField.setForeground(content.getFontColor());
		panel.add(textField);
		textField.requestFocusInWindow();
		
		//color chooser
		buttonChooseFontColor = new JButton("Schriftfarbe festlegen");
		buttonChooseFontColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(colorChooser.isVisible())
				{
					colorChooser.setVisible(false);
					content.setFontColor(colorChooser.getColor());
					textField.setForeground(colorChooser.getColor());
				}
				else
				{
					colorChooser.setVisible(true);
				}
			}
		});
		panel.add(buttonChooseFontColor);
		buttonChooseFontColor.setBounds(220, 0, 200, 30);
	}
	
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void endEdit(ToolEndsType type) {
		if(isClosed)
			return;
		else
			isClosed = true;
		
		//remove the tool
		textField.setVisible(false);
		fontSizeLabel.setVisible(false);
		fontSize.setVisible(false);
		colorChooser.setVisible(false);
		buttonChooseFontColor.setVisible(false);
		panel.remove(textField);
		panel.remove(fontSizeLabel);
		panel.remove(fontSize);
		panel.remove(buttonChooseFontColor);
		panel.remove(colorChooser);
		
		//change the content
		content.setText(textField.getText());
		
		//call the handler
		if(handler != null)
			handler.toolEnds(content, type);
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public Content getContent() {
		return content;
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void addToolReadyHandler(ToolReadyHandler handler) {
		this.handler = handler;
	}
}
