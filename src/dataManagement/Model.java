package dataManagement;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.io.IOException;
import java.sql.SQLException;

import pageData.ActionType;
import pageData.Content;
import pageData.ContentInstruction;
import pageData.DirectoryAlreadyExistsException;
import pageData.DirectoryDoesNotExistsException;
import pageData.DirectoryType;
import pageData.Page;
import pageData.PageInformation;


/**
 * Die Klasse Model dient der Kapselung der Datenhaltung und ist Teil des MVC-Design.
 * Das Model verwaltet die Schnittstelle zur Datenbank und implementiert die noetige
 * Verarbeitungslogik im Umgang mit der Datenbank. Dazu ueberprueft es die Existenz von
 * Verzeichnissen und wirft die passenden Exceptions, falls es zu Konflikten in der
 * Verarbeitung kommt. Das Model kann aus dem Pfad eines Elementes die id in der Datenbank
 * bestimmen und diese zur weiteren Verarbeitung nutzen. Zielsetzung dieser Klasse ist ein
 * komfortabler Zugriff auf die Daten, welcher lediglich ueber Pfadnamen operiert. 
 * Die Komplexitaet der Datenbank soll somit gekapselt werden und vom Controller aus nicht mehr
 * ersichtlich sein. Die Schnittstelle zum Controller soll minimal gehalten sein. 
 * Das Model serialisiert und deserialisiert den Seiteninhalt, welcher in der Datenbank
 * abgelegt werden soll.
 * */
public class Model {
	/**
	 * Eine Instanz der Klasse Serializer zum Serialisieren und Deserialisieren der Seitenelemente
	 * */
	private Serializer serializer;
	
	/**
	 * Die Schnittstelle zur Datenbank
	 * */
	private IDataBase db;
	
	//constructor
	/**
	 * Instanziiert die Attribute zum Serializer und db
	 * Pfad und Logininformationen zur Datenbank sind uebergangsweise "hard coded"
	 * */
	public Model() throws ClassNotFoundException, SQLException
	{
		serializer = new Serializer();
		db = new MySqlDatabase("jdbc:mysql://localhost/TwoNote", "TwoNote", "1dNfdTF");
	}
	
	//methods
	
	//create methods
	/**
	 * Ueberprueft ob das zu erstellende Buch bereits existiert, falls nicht
	 * leitet diese Methode den Auftrag ein Buch zu erstellen an die Datenbankschnittstelle weiter
	 * @param bookName der Name des Buches
	 * @throws DirectoryAlreadyExistsException das Buch existiert bereits
	 * @throws SQLException Datenbankfehler
	 * */
	public void createBook(String bookName) 
			throws DirectoryAlreadyExistsException, SQLException
	{
		//check existence
		if(db.existsBook(bookName))
			throw new DirectoryAlreadyExistsException(DirectoryType.Book, ActionType.create, bookName);
		
		db.createBook(bookName);
	}
	
	/**
	 * Die id des Buches in welchem das Kapitel gespeichert werden soll wird geladen
	 * ueberprueft ob das zu erstellende Kapitel bereits existiert, falls nicht
	 * leitet diese Methode den Auftrag ein Kapitel zu erstellen an die Datenbankschnittstelle weiter
	 * @param bookName der Name des Buches
	 * @param chapterName der Name des Kapitels
	 * @throws DirectoryDoesNotExistsException das Buch existiert nicht
	 * @throws DirectoryAlreadyExistsException das Kapitel existiert bereits
	 * @throws SQLException Datenbankfehler
	 * */
	public void createChapter(String chapterName, String bookName) 
			throws DirectoryAlreadyExistsException, SQLException, DirectoryDoesNotExistsException
	{
		int bookID = this.getBookID(ActionType.create, bookName);
		if(db.existsChapter(chapterName, bookID))
			throw new DirectoryAlreadyExistsException(DirectoryType.Chapter, ActionType.create, chapterName);
		
		db.createChapter(chapterName, bookID);
	}
	
	/**
	 * Die id des Kapitels in welchem die Seite gespeichert werden soll wird geladen
	 * ueberprueft ob die zu erstellende Seite bereits existiert, falls nicht
	 * leitet diese Methode den Auftrag die Seite zu erstellen an die Datenbankschnittstelle weiter
	 * Die Attribute der Seite werden mit Defaultwerten gef��llt
	 * @param bookName der Name des Buches
	 * @param chapterName der Name des Kapitels
	 * @param pageName der Name der Seite
	 * @throws DirectoryDoesNotExistsException das Buch oder Kapitel existiert nicht
	 * @throws DirectoryAlreadyExistsException die Seite existiert bereits
	 * @throws SQLException Datenbankfehler
	 * */
	public Page createPage(String pageName, String chapterName, String bookName)
			throws DirectoryAlreadyExistsException, SQLException, DirectoryDoesNotExistsException
	{
		int chapterID = this.getChapterID(ActionType.create, chapterName, bookName);
		if(db.existsPage(pageName, chapterID))
			throw new DirectoryAlreadyExistsException(DirectoryType.Page, ActionType.create, pageName);
		
		//create empty default Page
		int width = Page.DEFAULT_WIDTH;
		int height = Page.DEFAULT_HEIGHT;
		PageInformation pageInfo= new PageInformation(pageName, chapterName, bookName, width, height);
		
		//save page in DB
		db.createPage(pageInfo, chapterID);
		
		return new Page(pageInfo);
	}
	
	private void createContent(Content cont, int pageID) 
			throws SQLException, IOException, DirectoryAlreadyExistsException
	{
		byte[] contentBytes = serializer.serialize(cont);
		int contentNumber = cont.getNumber();
		if(db.existsContent(contentNumber, pageID))
			throw new DirectoryAlreadyExistsException(DirectoryType.Content, ActionType.create, contentNumber+"");
		db.createContent(contentBytes, cont.getNumber(), pageID);
	}
	
	//delete methods
	/**
	 * Diese Methode laedt die id des zu loeschenden Buches. Falls das Buch nicht existiert 
	 * wird eine Exception geworfen. Falls das Buch existiert wird es geloescht.
	 * Alle Unterverzeichnisse dieses Buches werden durch das dmbs mittels ON DELETE CASCATE geloescht.
	 * @param bookName Der Name des Buches
	 * @throws SQLException Datenbankfehler
	 * @throws DirectoryDoesNotExistsException das Buch existiert nicht
	 * */
	public void deleteBook(String bookName) 
			throws SQLException, DirectoryDoesNotExistsException
	{
		int bookID = getBookID(ActionType.delete, bookName);
		
		//delete the book
		db.deleteBook(bookID);
	}
	
	/**
	 * Diese Methode laedt die id des zu loeschenden Kapitels. Falls das Buch oder Kapitel
	 * nicht existiert wird eine Exception geworfen. 
	 * Falls das Kapitel existiert wird es geloescht.
	 * Alle Unterverzeichnisse dieses Kapitels werden durch das dmbs mittels ON DELETE CASCATE geloescht.
	 * @param bookName Der Name des Buches
	 * @param chapterName Der Name des Kapitels
	 * @throws SQLException Datenbankfehler
	 * @throws DirectoryDoesNotExistsException das Buch oder Kapitel existiert nicht
	 * */
	public void deleteChapter(String chapterName, String bookName) 
			throws SQLException, DirectoryDoesNotExistsException
	{
		int chapterID = this.getChapterID(ActionType.delete, chapterName, bookName);
		
		//delete the chapter
		db.deleteChapter(chapterID);
	}
	
	/**
	 * Diese Methode laedt die id der zu loeschenden Seite. Falls das Buch, Kapitel
	 * oder Seite nicht existiert wird eine Exception geworfen. 
	 * Falls die Seite existiert wird sie geloescht.
	 * Alle Unterverzeichnisse dieser Seite werden durch das dmbs mittels ON DELETE CASCATE geloescht.
	 * @param bookName Der Name des Buches
	 * @param chapterName Der Name des Kapitels
	 * @param pageName Der Name der Seite
	 * @throws SQLException Datenbankfehler
	 * @throws DirectoryDoesNotExistsException das Buch oder Kapitel existiert nicht
	 * */
	public void deletePage(String pageName, String chapterName, String bookName) 
			throws SQLException, DirectoryDoesNotExistsException
	{
		int pageID = this.getPageID(ActionType.delete, pageName, chapterName, bookName);
		
		//delete the page
		db.deletePage(pageID);
	}
	
	private void deleteContent(int contentNumber, int pageID) throws SQLException
	{
		if(!db.existsContent(contentNumber, pageID))
			throw new DirectoryDoesNotExistsException(DirectoryType.Content, ActionType.delete, contentNumber+"");
		int contentID = db.getContentID(contentNumber, pageID);
		db.deleteContent(contentID);
	}
	
	// save methods
	/**
	 * Diese Methode speichert eine Seite. Alle Pfadangaben liegen in der PageInformation.
	 * Die PageInformation beinhalten ebenfalls alle Informationen zum Speichern der Seite.
	 * Die Schnittstelle zum Controller besteht nur aus dieser Methode, das Speichern der
	 * Seiteninformation wird bewusst gekapselt und in dieser Methode ebenfalls ausgefuehrt.
	 * Jede Seite speichert alle ungespeicherten aenderungen, welche an ihr vorgenommen werden.
	 * In dieser Methode wird ueber die Liste der aenderungen iteriert und abhaengig von der 
	 * ContentInstruction der Seiteninhalt serialisiert und erstellt, gespeichert oder geloescht.
	 * @param page die zu speichernde Seite
	 * @throws DirectoryDoesNotExistsException die Seite oder ein Seiteninhalt existiert nicht
	 * @throws SQLException Datenbankfehler
	 * @throws IOException Fehler beim Serialisieren
	 * */
	public void savePage(Page page) 
			throws DirectoryDoesNotExistsException, SQLException, IOException
	{
		PageInformation pageInfo = page.getPageInfo();
		String bookName = pageInfo.getBookName();
		String chapterName = pageInfo.getChapterName();
		String pageName = pageInfo.getPageName();
		
		int pageID = this.getPageID(ActionType.save, pageName, chapterName, bookName);
		int chapterID = this.getChapterID(ActionType.save, chapterName, bookName);
		int instruSize = page.getContentInstructionSize();
		
		for (int i = 0; i < instruSize; i++) {
			ContentInstruction instru =page.getNextContentInstruction();
			int contentNumber = instru.getContentNumber();
			switch (instru.getActionType()) {
			case create:
				createContent(page.getContent(contentNumber), pageID);
				break;
			case save:
				saveContent(page.getContent(contentNumber), pageID);
				break;
			case delete:
				deleteContent(contentNumber, pageID);
				break;
			default:
				throw new RuntimeException("Ungueltiger ActionType");
			}
			page.contentInstructionCompleted();	
		}
		
		db.savePage(pageInfo, chapterID, pageID);
	}

	private void saveContent(Content cont, int pageID) 
			throws SQLException, IOException {
		
		byte[] contentBytes = serializer.serialize(cont);
		int contentNumber = cont.getNumber();
		int contentID = db.getContentID(contentNumber, pageID);
		if(db.existsContent(contentNumber, pageID))
			db.saveContent(contentBytes, contentID);
	}
	


	// load methods
	/**
	 * Es wird ueberprueft ob der angegebene Pfad gueltig ist.
	 * Das Laden einer Seite besteht aus zwei Schritten. Zuerst wird die PageInformation aus
	 * der Datenbank abgerufen und ein Objekt der Klasse Page erstellt. Danach wird der
	 * Seiteninhalt zu dieser Seite geladen, deserialisiert und der Seite zugefuegt.
	 * @param pageName der Name der Seite
	 * @param chapterName der Name des Kapitels
	 * @param bookName der Name des Buches#
	 * @throws SQLException Datenbankfehler
	 * @throws DirectoryDoesNotExistsException der Pfad zur Seite ist falsch
	 * @throws ClassNotFoundException Fehler beim deserialisieren
	 * @throws IOException Fehler beim deserialisieren
	 * @return die angefragte Seite
	 */
	public Page loadPage(String pageName, String chapterName, String bookName) 
			throws SQLException, DirectoryDoesNotExistsException, ClassNotFoundException, IOException
	{
		int pageID = getPageID(ActionType.load, pageName, chapterName, bookName);
		
		PageInformation pageInfo = db.loadPageInformation(pageID);
		pageInfo.setBookName(bookName);
		pageInfo.setChapterName(chapterName);
		
		Page page = new Page(pageInfo);
		
		int[] pageChildren = db.getPageChildren(pageID);
		for (int i = 0; i < pageChildren.length; i++) {
			page.addContent(loadContent(pageChildren[i]));
		}
		return page;
	}
	
	private Content loadContent(int contentID) 
			throws ClassNotFoundException, IOException, SQLException {
		byte[] contentBytes = db.loadContent(contentID);
		Content cont = serializer.deserialize(contentBytes);
		return cont;
	}


	//get ID methods
	private int getBookID(ActionType type, String bookName) 
			throws SQLException, DirectoryDoesNotExistsException
	{
		if(!db.existsBook(bookName))
			throw new DirectoryDoesNotExistsException(DirectoryType.Book, type, bookName);
		int bookID = db.getBookID(bookName);
		return bookID;
	}
	
	private int getChapterID(ActionType type,String chapterName ,String bookName) 
			throws SQLException, DirectoryDoesNotExistsException
	{
		int bookID = this.getBookID(type, bookName);
		if(!db.existsChapter(chapterName, bookID))
			throw new DirectoryDoesNotExistsException(DirectoryType.Chapter, type, chapterName);
		int chapterID = db.getChapterID(chapterName, bookID);
		return chapterID;
	}
	
	private int getPageID(ActionType type,String pageName, String chapterName ,String bookName) 
			throws SQLException, DirectoryDoesNotExistsException
	{
		int chapterID = this.getChapterID(type, chapterName, bookName);
		if(!db.existsPage(pageName, chapterID))
			throw new DirectoryDoesNotExistsException(DirectoryType.Page, type, pageName);
		int pageID = db.getPageID(pageName, chapterID);
		return pageID;
	}
	
	
	//get Tree
	/**
	 * Der Verzeichnisbaum wird aus der Datenbank abgerufen und als Objekt der Klasse
	 * Tree zurueckgegeben. Dazu wird iterativ mittels 3 Schleifen immer zuerst das obere
	 * Level des Baumes erstellt, und dann zu jedem Element dieses Levels die Kindbaeume geladen
	 * @throws SQLException Datenbankfehler
	 * @return der Verzeichnisbaum
	 * */
	public Tree getTree() throws SQLException
	{
		ActionType type = ActionType.load;
		String[] books = db.getAllBooks();
		Tree t = new Tree();
		for (int i = 0; i < books.length; i++) {
			Tree chapterTree = new Tree(books[i]);
			String[] chapters = db.getBookChildren(getBookID(type, books[i]));
			for (int j = 0; j < chapters.length; j++) {
				Tree pageTree = new Tree(chapters[j]);
				int chapterID = getChapterID(type, chapters[j], books[i]);
				String[] pages = db.getChapterChildren(chapterID);
				for (int k = 0; k < pages.length; k++) {
					pageTree.addChildren(pages[k]);
				}
				chapterTree.addChildren(pageTree);
			}
			t.addChildren(chapterTree);
		}
		return t;
	}
	
	//rename
	/**
	 * Diese Methode aendert den Namen eines vorhandenen Buches
	 * @param bookName alter Name
	 * @param newName neuer Name
	 * */
	public void renameBook(String bookName, String newName) 
			throws DirectoryDoesNotExistsException, SQLException
	{
		if(db.existsBook(newName))
			throw new DirectoryAlreadyExistsException
				(DirectoryType.Book, ActionType.rename, newName);
		
		int bookID = getBookID(ActionType.rename, bookName);
		db.renameBook(bookID, newName);
	}
	
	/**
	 * Diese Methode aendert den Namen eines vorhandenen Kapitels
	 * @param bookName name des Buches
	 * @param chapterName alter Name
	 * @param newName neuer Name
	 * */
	public void renameChapter(String chapterName, String bookName, 
			String newName) throws DirectoryDoesNotExistsException, 
			SQLException, DirectoryDoesNotExistsException
	{
		int bookID = getBookID(ActionType.rename, bookName);
		if(db.existsChapter(newName, bookID))
			throw new DirectoryAlreadyExistsException
				(DirectoryType.Chapter, ActionType.rename, newName);
		
		int chapterID = getChapterID(ActionType.rename, chapterName, bookName);
		db.renameChapter(chapterID, newName);
	}
	
	public void renamePage(String pageName, String chapterName, 
			String bookName, String newName) 
			throws DirectoryDoesNotExistsException, SQLException,
			DirectoryAlreadyExistsException
	{
		ActionType type = ActionType.rename;
		int chapterID = getChapterID(type, chapterName, bookName);
		if(db.existsPage(newName, chapterID))
			throw new DirectoryAlreadyExistsException
				(DirectoryType.Page, type, newName);
		
		int pageID = getPageID(type, pageName, chapterName, bookName);
		db.renamePage(pageID, newName);
	}
	
	/**
	 * Diese Methode verschiebt ein Kapitel in ein anderes Buch
	 * @param bookName Name des alten Buches
	 * @param chapterName Name des Kapitels
	 * @param newBook name des neuen Buches
	 */
	public void moveChapter(String chapterName, String bookName, String newBook)
		throws SQLException, DirectoryAlreadyExistsException, 
			DirectoryDoesNotExistsException
	{
		ActionType type = ActionType.move;
		int chapterID = getChapterID(type, chapterName, bookName);
		int newBookId = getBookID(type, newBook);
		if(db.existsChapter(chapterName, newBookId))
			throw new DirectoryAlreadyExistsException
				(DirectoryType.Chapter, type, chapterName);
		db.moveChapter(chapterID, newBookId);
	}
	
	public void movePage(String pageName, String chapterName, String bookName,
			String newChapter, String newBook) throws SQLException, 
			DirectoryAlreadyExistsException, DirectoryDoesNotExistsException
		{
			ActionType type = ActionType.move;
			int pageID = getPageID(type, pageName, chapterName, bookName);
			int newChapterID = getChapterID(type, newChapter, newBook);
			
			if(db.existsPage(pageName, newChapterID))
				throw new DirectoryAlreadyExistsException
					(DirectoryType.Page, type, pageName);
			db.movePage(pageID, newChapterID);
		}
}
