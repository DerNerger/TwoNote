package pageData;

/**
 *@author Felix Kibellus
 *@version 1.0 
 */

import java.awt.Component;
import java.io.Serializable;
import javax.swing.JPanel;

/**
 * Von dieser abstrakten Klasse können Seiteninhalte abgeleitet werden.
 * Gespeichert wird ein Seitenelement indem es serialisiert und in der 
 * Datenbank gespeichert wird. Es ist darauf zu achten nur serialisierbare
 * Attribute zu verwenden.
 * */
public abstract class Content implements Serializable{

	/**
	 * Die serialVersionUID dient der Verifizierung, dass ein deserialisiertes Objekt 
	 * tatsächlich von dieser Klasse stammt.
	 * */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Die "number" stellt die Nummer des Inhalts innerhalb einer Seite da.
	 * Über diese Nummer kann von der Seite aus auf den Inhalt zugegriffen werden.
	 * Die Nummer dient auch dazu die einzelnen Inhalte einer 
	 * Seite voneinander zu unterscheiden.
	 * Achtung: Diese Nummer hat nichts mit der id in der Datenbank zutun und dient
	 * nur der internen Verwaltung.
	 * */
	private int number;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	/**
	 * Wird nach dem Deserialisieren verwendet, um einen sicheren
	 * Cast zu ermöglichen
	 * */
	private ContentType contentType;
	
	//constructor
	/**
	 * Konstruktor muss aus Unterklasse aufgerufen werden.
	 * Es werden alle Attribute mit den übergebenen Parametern initialisiert.
	 * @param x die x-Koordinate des Seiteninhalts
	 * @param y die y-Koordinate des Seiteninhalts
	 * @param width die Breite des Seiteninhalts
	 * @param height die Höhe des Seiteninhalts
	 * @param contentType der Typ der abgeleiteten Klasse zum sicheren Upcast
	 * */
	public Content(int x, int y, int width, int height, ContentType contentType)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.contentType = contentType;
	}
	
	
	//getter and setter
	public int getNumber() {
		return number;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	//Methods
	/**
	 * Der Seiteninhalt wird angewiesen sich auf das übergebene JPanel zu zeichnen.
	 * @param panel das JPanel auf welches gezeichnet werden soll
	 * @return die gezeichnete Komponente, kann verwendet werden um listener zu initialisieren
	 * */
	public abstract Component draw(JPanel panel);
}
