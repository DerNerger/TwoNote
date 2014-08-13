package pageData;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */


/**
 * Die PageInformation speichern Name des Buches, des Kapitels und der Seite.
 * Zusätzlich wird zu jeder seite die Höhe und die Breite gespeichert.
 * Diese Informationen werden in der Datenbank gespeichert und beim Laden einer
 * Seite dieser zugefügt. Die PageInformation wird beim Speichern einer Seite verwendet
 * um den Pfad zur Seite zu erhalten.
 * */
public class PageInformation {
	
	private String pageName;
	private String chapterName;
	private String bookName;
	private int width;
	private int height;
	
	//constructor
	/**
	 * Es wird eine Instanz der Klasse PageInformation mit den übergebenen Parametern angelegt.
	 * @param pageName Name der Seite
	 * @param chapterName Name des Kapitels
	 * @param bookName Name des Buches
	 * @param width Breite der Seite
	 * @param height Höhe der Seite
	 * */
	public PageInformation(String pageName, String chapterName,String bookName, int width, int height)
	{
		this.pageName = pageName;
		this.chapterName = chapterName;
		this.bookName = bookName;
		this.height = height;
		this.width = width;
	}
	

	//getter and setter
	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	
	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
