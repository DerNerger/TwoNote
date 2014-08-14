package dataManagement;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.sql.SQLException;
import pageData.PageInformation;

/**
 * Dieses Interface stellt die Schnittstelle zur Datenbank her. Alle Methoden verwenden keinerlei
 * Logik, sondern uebersetzen lediglich eine elementare Programmanweisung in die Sprache des DMBS
 * und leiten diese an das DMBS zur Ausfuehrung weiter. Die ueberpruefung der korrekten 
 * Arbeitsweise dieser Methoden ist somit nicht Aufgabe der Klassen die dieses Interface implementieren,
 * sondern muss von den verwendenden Klassen geleistet werden. Dieses Interface fuehrt somit die 
 * noetige Abstraktion ein um verschiedene Datenbanken zu verwenden oder den Umstieg auf eine andere
 * Datenbank zu erleichtern.
 * */
public interface IDataBase {
	
	//create
	/**
	 * Diese Methode legt ein neues Buch in der Tabelle "Books" an
	 * @param bookName der Name des anzulegenden Buches
	 * @throws SQLException Datenbankfehler
	 * */
	public void createBook(String bookName) throws SQLException;
	
	/**
	 * Diese Methode legt ein neues Kapitel in der Tabelle "Chapter" an
	 * @param chapterName der Name des anzulegenden Kapitels
	 * @param bookID die id des Buches zu dem das anzulegende Kapitel gehoeren soll
	 * @throws SQLException Datenbankfehler
	 * */
	public void createChapter(String chapterName, int bookID)throws SQLException;
	
	/**
	 * Diese Methode legt eine neue Seite in der Tabelle "Pages" an
	 * @param pageInfo informationen zur Seite
	 * @param chapterID die id des Kapitels zu dem die anzulegende Seite gehoeren soll
	 * @throws SQLException Datenbankfehler
	 * */
	public void createPage(PageInformation pageInfo, int chapterID)throws SQLException;
	
	/**
	 * Diese Methode legt einen neuen Seiteninhalt in der Tabelle "Content" an
	 * @param contentBytes das serialisierte Objekt
	 * @param contentNumber die nummer des Seiteninhalts innerhalb seiner Seite
	 * @param pageID die id der Seite zu welcher der Inhalt gehoeren soll
	 * @throws SQLException Datenbankfehler
	 * */
	public void createContent(byte[] contentBytes,int contentNumber, int pageID)throws SQLException;
	
	//delete
	/**
	 * Diese Methode loescht ein Buch aus der Tabelle "Books"
	 * @param bookID Die id des Buches, welches geloescht werden soll
	 * @throws SQLException Datenbankfehler
	 * */
	public void deleteBook(int bookID)throws SQLException;
	
	/**
	 * Diese Methode loescht ein Kapitel aus der Tabelle "Chapter"
	 * @param chapterID Die id des Kapitels, welches geloescht werden soll
	 * @throws SQLException Datenbankfehler
	 * */
	public void deleteChapter(int chapterID)throws SQLException;
	
	/**
	 * Diese Methode loescht eine Seite aus der Tabelle "Pages"
	 * @param pageID Die id der Seite, welche geloescht werden soll
	 * @throws SQLException Datenbankfehler
	 * */
	public void deletePage(int pageID)throws SQLException;
	
	/**
	 * Diese Methode loescht einen Seiteninhalt aus der Tabelle "Content"
	 * @param contentID Die id des Seiteninhaltes, welcher geloescht werden soll
	 * @throws SQLException Datenbankfehler
	 * */
	public void deleteContent(int contentID)throws SQLException;
	
	//save
	/**
	 * Diese Methode speichert einen Seiteninhalt in der Tabelle "Content"
	 * @param contentBytes das serialisierte Objekt
	 * @param contentID die innerhalb der Tabelle "Content"
	 * @throws SQLException Datenbankfehler
	 * */
	public void saveContent(byte[] contentBytes, int contentID)throws SQLException;
	
	/**
	 * Diese Methode speichert eine Seite in der Tabelle "Pages"
	 * @param pageInfo die Information zur Seite
	 * @param chapterID das Kapitel zu welchem die Seite gehoertb
	 * @param pageID die id der Seite innerhalb der Tabelle "Pages"
	 * @throws SQLException Datenbankfehler
	 * */
	public void savePage(PageInformation pageInfo, int chapterID, int pageID)throws SQLException;
	
	//load
	/**
	 * Diese Methode laed einen Seiteninhalt aus der Tabelle "Content"
	 * @param contentID die id des angefragten Seiteninhalts
	 * @throws SQLException Datenbankfehler
	 * @return das serialisierte Objekt zur angefragten id
	 * */
	public byte[] loadContent(int contentID)throws SQLException;
	
	/**
	 * Diese Methode laedt die Seiteninformation aus der Tabelle "Pages"
	 * @param pageID die id der angefragten Seite
	 * @throws SQLException Datenbankfehler
	 * @return die Seiteninformation zur angefragten id
	 * */
	public PageInformation loadPageInformation (int pageID)throws SQLException;
	
	//exists
	/**
	 * Diese Methode gibt zurueck, ob es in der Tabelle "Books" ein Buch mit dem Namen "bookName" gibt
	 * @param bookName der Name des Buchs
	 * @throws SQLException Datenbankfehler
	 * @return zum angefragten Buch existiert ein Eintrag in der Datenbank
	 * */
	public boolean existsBook(String bookName)throws SQLException;
	
	/**
	 * Diese Methode gibt zurueck, ob es in der Tabelle "Chapter" ein Kapitel mit dem Namen "chapterName" gibt
	 * @param chapterName der Name des Kapitels
	 * @param bookID die id des Buches zu welchem das Kapitel gehoert
	 * @throws SQLException Datenbankfehler
	 * @return zum angefragten Kapitel existiert ein Eintrag in der Datenbank
	 * */
	public boolean existsChapter(String chapterName, int bookID)throws SQLException;
	
	/**
	 * Diese Methode gibt zurueck, ob es in der Tabelle "Pages" eine Seite mit dem Namen "pageName" gibt
	 * @param pageName der Name der Seite
	 * @param chapterID die id des Kapitels zu welchem die Seite gehoert
	 * @throws SQLException Datenbankfehler
	 * @return zur angefragten Seite existiert ein Eintrag in der Datenbank
	 * */
	public boolean existsPage(String pageName, int chapterID)throws SQLException;
	
	/**
	 * Diese Methode gibt zurueck, ob es in der Tabelle "Content" ein Eintrag mit der Nummer "contentNumber" gibt
	 * @param contentNumber die Nummer des Seiteninhalts
	 * @param pageID die id der Seite zu welcher der Inhalt gehoert
	 * @throws SQLException Datenbankfehler
	 * @return zum angefragten Seiteninhalt existiert ein Eintrag in der Datenbank
	 * */
	public boolean existsContent(int contentNumber, int pageID)throws SQLException;
	
	//getID
	/**
	 * Diese Methode liefert zu dem Namen eines Buches die entsprechende id in der Datenbank
	 * @param bookName Der Name des angefragten Buches
	 * @throws SQLException Datenbankfehler
	 * @return die id des angefragten Buches
	 * */
	public int getBookID(String bookName)throws SQLException;
	
	/**
	 * Diese Methode liefert zu dem Namen eines Kapitels die entsprechende id in der Datenbank
	 * @param chapterName Der Name des angefragten Kapitels
	 * @param bookID die id des Buches zu welchem das Kapitel gehoert
	 * @throws SQLException Datenbankfehler
	 * @return die id des angefragten Kapitels
	 * */
	public int getChapterID(String chapterName, int bookID)throws SQLException;
	
	/**
	 * Diese Methode liefert zu dem Namen einer Seite die entsprechende id in der Datenbank
	 * @param pageName Der Name der angefragten Seite
	 * @param chapterID die id des Kapitels zu welchem die Seite gehoert
	 * @throws SQLException Datenbankfehler
	 * @return die id der angefragten Seite
	 * */
	public int getPageID(String pageName, int chapterID)throws SQLException;
	
	/**
	 * Diese Methode liefert zu der Nummer eines Seiteinhalts die entsprechende id in der Datenbank
	 * @param contentNumber Die Nummer des angefragten Seiteninhalts
	 * @param pageID die id der Seite zu welchem dir Inhalt gehoert
	 * @throws SQLException Datenbankfehler
	 * @return die id ders angefragten Seiteinhalts
	 * */
	public int getContentID(int contentNumber, int pageID)throws SQLException;
	
	//getChildren
	/**
	 * Diese Methode liefert zur id eines Buches alle Kapitel welche zu diesem Buch gehoeren
	 * @param bookID Die id des angefragten Buches
	 * @throws SQLException Datenbankfehler
	 * @return der Name aller Kapitel, welche zum angefragten Buch gehoeren
	 * */
	public String[] getBookChildren(int bookID)throws SQLException;
	
	/**
	 * Diese Methode liefert zur id eines Kapitels alle Seiten welche zu diesem Kapitel gehoeren
	 * @param chapterID Die id des angefragten Kapitels
	 * @throws SQLException Datenbankfehler
	 * @return der Name aller Seiten, welche zum angefragten Kapitel gehoeren
	 * */
	public String[] getChapterChildren(int chapterID)throws SQLException;
	
	/**
	 * Diese Methode liefert zur id einer Seite alle Inhalte welche zu dieser Seite gehoeren
	 * @param pageID Die id der angefragten Seite
	 * @throws SQLException Datenbankfehler
	 * @return der Name aller Inhalte, welche zur angefragten Seite gehoeren
	 * */
	public int[] getPageChildren(int pageID)throws SQLException;
	
	//getAllBooks
	/**
	 * Diese Methode liefert den Namen aller Buecher welche in der Datenbank angelegt sind
	 * @throws SQLException Datenbankfehler
	 * @return ein String[] mit den Namen aller angelegten Buechern
	 * */
	public String[] getAllBooks() throws SQLException;
	
	//rename
	/**
	 *Diese Methode aendert den Namen eines Buches in der Datenbank
	 *@param  bookID Die id des Buches
	 *@param newName Der neue Name
	 */
	public void renameBook(int bookID, String newName) throws SQLException;
	
	/**
	 *Diese Methode aendert den Namen eines Kapitels in der Datenbank
	 *@param  chapterID Die id des Kapitels
	 *@param newName Der neue Name
	 */
	public void renameChapter(int chapterID, String newName)throws SQLException;
	
	//move
	/**
	 * Diese Methode aendert bei einem Kapitel die id des 
	 * zugehoerigen Buches auf eine andere.
	 */
	public void move(int chapterID, int newBookId) throws SQLException;
}
