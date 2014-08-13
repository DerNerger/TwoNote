package dataProcessing;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.io.IOException;
import java.sql.SQLException;

import dataManagement.Model;
import dataManagement.Tree;
import pageData.Content;
import pageData.ContentType;
import pageData.DirectoryDoesNotExistsException;
import pageData.Page;
import pageData.PageInformation;
import pageData.Startpage;
import pageData.TextBox;

/**
 * Die Klasse Controller dient der Kapselung der Datenverwaltung und ist Teil des MVC-Design.
 * Der Controller speichert und verwaltet die aktuell geöffnete Sitzung des Programms.
 * Mittels des Controllers kann eine Seite geladen und modifiziert werden.
 * Alle komplexeren Aufgaben, welcher nicht ohne Verarbeitungslogik von View zu Model
 * weitergeleitet werden können werden vom Controller verarbeitet.
 * */
public class Controller {
	
	/**
	 * Die aktuell geöffnete Seite
	 * */
	private Page currentPage;
	
	/**
	 * Eine Referenz auf das Datenmodel
	 * */
	private Model model;
	
	/**
	 * Der Verzeichnisbaum
	 * */
	private Tree dataTree;
	
	//constructor
	/**
	 * Es wird eine Instanz eines Controllers erstellt und 
	 * der Verzeichnisbaum aktualisiert
	 * @throws SQLException Datenbankfehler
	 * */
	public Controller(Model model) throws SQLException
	{
		this.model = model;
		refreshTree();
	}
	
	//load Page
	/**
	* Es zu einem vorgegebenen Pfad eine Seite aus dem Model geladen
	* und als aktuelle Seite gespeichert. Sollte die angeforderte Seite
	* Startseite sein, wird eine Instanz der Startseite erstellt und in
	* currentPage gespeichert. Die geladene Seite ist direkt nicht von der
	* View aus ansprechbar, sondern es müssen die Inhalte oder die PageInformation
	* über die entsprechenden Methoden abgefragt werden.
	* @param pageName der Name der Seite
	* @param chapterName der Name des Kapitels
	* @param bookName der Name des Buches
	* @throws DirectoryDoesNotExistsException Pfad nicht korrekt
	* @throws SQLException Datenbankfehler
	 * @throws ClassNotFoundException Fehler beim deserialisieren
	 * */
	public void loadPage(String pageName, String chapterName, String bookName) 
			throws DirectoryDoesNotExistsException, SQLException, IOException, ClassNotFoundException
	{
		if(pageName == "Startseite")
			currentPage = new Startpage();
		else
			currentPage = model.loadPage(pageName, chapterName, bookName);
	}
	
	//getContent :content[]
	/**
	 * Es wird der Inhalt der aktuell geöffneten Seite als Content[] zurückgegeben
	 * @throws NullPointerException es ist aktuell keine Seite geladen
	 * @return der Inhalt der aktuell geöffneten seite
	 * */
	public Content[] getContent() throws NullPointerException
	{
		if(currentPage == null)
			throw new NullPointerException("Es wurde keine Seite geoeffnet");
		return currentPage.getContent();
	}
	
	//createContent
	/**
	 * Auf der Aktuell geöffneten Seite wird ein Seiteninhalt erstellt,
	 * und somit auf der aktuellen Seite als ungespeicherte Änderung vermerkt.
	 * @param type der Typ des Seiteninhalts
	 * @param x Die x-Koordinate an welcher der Inhalt erstellt werden soll
	 * @param y Die y-Koordinate an welcher der Inhalt erstellt werden soll
	 * @return der erstellte Seiteninhalt
	 * */
	public Content createContent(ContentType type, int x, int y) 
	{
		Content content = null;
		//create
		switch (type) {
		case textBox:
			content = new TextBox(x, y);
			break;

		default:
			new RuntimeException("Gewaehlter content ist nicht verfuegbar");
		}
		//create content on page
		currentPage.createContent(content);
		return content;
	}
	
	/**
	 * Ein übergebener Seiteninhalt wird auf der aktuellen Seite gespeichert.
	 * Sollte die aktuell geöffnete Seite nicht die Startseite sein wird diese
	 * gespeichert.
	 * @param c der Seiteninhalt
	 * @throws DirectoryDoesNotExistsException der Pfad der Seite ist falsch
	 * @throws SQLException Datenbankfehler
	 * @throws IOException Fehler beim Serialisieren
	 * */
	public void saveContent(Content c) 
			throws DirectoryDoesNotExistsException, SQLException, IOException
	{
		if(currentPage.getPageInfo().getPageName().equals("Startseite"))
			return;
		currentPage.saveContent(c);
		model.savePage(currentPage);
	}
	
	/**
	 * Ein übergebener Seiteninhalt wird auf der aktuellen Seite gelöscht.
	 * Sollte die aktuell geöffnete Seite nicht die Startseite sein wird diese
	 * gespeichert.
	 * @param c der Seiteninhalt
	 * @throws DirectoryDoesNotExistsException der Pfad der Seite ist falsch
	 * @throws SQLException Datenbankfehler
	 * @throws IOException Fehler beim Serialisieren
	 * */
	public void deleteContent(Content c) 
			throws DirectoryDoesNotExistsException, SQLException, IOException
	{
		if(currentPage.getPageInfo().getPageName().equals("Startseite"))
			return;
		currentPage.deleteContent(c.getNumber());
		model.savePage(currentPage);
	}
	
	/**
	 * Der Verzeichnisbaum wird aktualisiert. Diese Methode wird bei
	 * instanziieren des Controllers aufgerufen, und muss danach von der 
	 * View aufgerufen werden, falls die Anzeige des Verzeichnisbaums im
	 * GUI aktualisiert werden soll. Der vom Model geladene Verzeichnisbaum
	 * wird in dataTree gespeichert und kann über die Methode getDataTree()
	 * abgefragt werden.
	 * @throws SQLException Datenbankfehler
	 * */
	public void refreshTree() throws SQLException
	{
		dataTree = model.getTree();
	}
	
	/**
	 * Der aktuell geladene Verzeichnisbaum wird zurückgegeben. 
	 * Gegebenfalls muss dieser vorher mittels der Methode refreshTree()
	 * aktualisiert werden.
	 * @return der Verzeichnisbaum
	 * */
	public Tree getDataTree()
	{
		return dataTree;
	}
	
	/**
	 * Es werden die Seiteninformationen zur aktuellen Seite zurückgegeben.
	 * @return die Seiteninformationen
	 * */
	public PageInformation getCurrentPageInformation()
	{
		return currentPage.getPageInfo();
	}
	
}
