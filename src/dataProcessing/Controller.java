package dataProcessing;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.io.IOException;
import java.sql.SQLException;

import GUI.SwingGui;
import GUI.SwingGuiHandler;
import GUI.View;
import Tools.EditMod;
import Tools.Tool;

import dataManagement.Model;
import dataManagement.Tree;
import pageData.Content;
import pageData.DirectoryAlreadyExistsException;
import pageData.DirectoryDoesNotExistsException;
import pageData.Page;
import pageData.PageInformation;
import pageData.TextBox;

/**
 * Die Klasse Controller dient der Kapselung der Datenverwaltung und ist Teil 
 * des MVC-Design. Der Controller speichert und verwaltet die aktuell 
 * geoeffnete Sitzung des Programms. Mittels des Controllers kann eine 
 * Seite geladen und modifiziert werden.
 * */
public class Controller {
	
	private View view;
	private Model model;
	private Tree dataTree;
	private Page currentPage;
	private Tool currentTool;
	private EditMod editMod;
	
	//constructor
	/**
	 * Es wird eine Instanz eines Controllers erstellt und 
	 * der Verzeichnisbaum aktualisiert
	 * @throws SQLException Datenbankfehler
	 * */
	public Controller()
	{
		try {
			/* Create and display the form */
			this.view = new SwingGuiHandler(this);
			view.setVisible(true);
			this.model = new Model();
			refreshTree(); 
			//this.currentPage = gute default loesung
			//this.currentTool = gute default loesung
			this.editMod = EditMod.Curser;
		} catch (ClassNotFoundException | SQLException e) {
			handleFatalError(e);
		}
	}
	
	//create
	public void createBook(String bookName)
	{
		try {
			model.createBook(bookName);
			refreshTree();
		} catch (DirectoryAlreadyExistsException e) {
			handleError(e);
		} catch (SQLException e) {
			handleFatalError(e);
		}
	}
	
	public void createChapter(String chapterName, String bookName)
	{
		try {
			model.createChapter(chapterName, bookName);
			refreshTree();
		} catch (DirectoryAlreadyExistsException e) {
			handleError(e);
		} catch (SQLException e) {
			handleFatalError(e);
		}
	}
	
	public void createPage(String pageName, String chapterName, String bookName)
	{
		try {
			model.createPage(pageName, chapterName, bookName);
			refreshTree();
		} catch (DirectoryAlreadyExistsException e) {
			handleError(e);
		} catch (SQLException e) {
			handleFatalError(e);
		}
	}
	
	/**
    * Auf der Aktuell geoeffneten Seite wird ein Seiteninhalt erstellt,
	* und somit auf der aktuellen Seite als ungespeicherte aenderung vermerkt.
	* @param type der Typ des Seiteninhalts
	* @param x Die x-Koordinate an welcher der Inhalt erstellt werden soll
	* @param y Die y-Koordinate an welcher der Inhalt erstellt werden soll
	* @return der erstellte Seiteninhalt
	* */
	public void createContent(int x, int y) 
	{
		Content cont = null;
		switch(editMod)
		{
		case Text:
			cont = new TextBox(x, y);
			//currentTool = new TextEditTool(cont); TODO: THIS
			view.showTool(currentTool);
			break;
		default:
			break;
		}
		
		if(cont != null)
		{
			currentPage.addContent(cont);
		}
	}
	
	//delete
	public void delete(String path)
	{
		try {
			String[] pathParts = path.split(", ");
			//remove the [ in front of the path
			pathParts[0] = pathParts[0].substring(1, pathParts[0].length());
			if( pathParts.length == 1) //is it a book ?
				deleteBook(pathParts[0]);
			else
				if( pathParts.length == 2) // is it a chapter ?
					deleteChapter(pathParts[0], pathParts[1]);
				else // it is a page
					deletePage(pathParts[0], pathParts[1], pathParts[2]);
			refreshTree();
		} catch (SQLException e) {
			handleFatalError(e);
		} catch(DirectoryDoesNotExistsException e){
			handleError(e);
		}
	}
	
	private void deleteBook(String book) 
			throws DirectoryDoesNotExistsException, SQLException
	{
		//remove the ] at the end of the path
		book = book.substring(0, book.length()-1);
		model.deleteBook(book); //delete it in dataBase
	}
	
	private void deleteChapter(String book, String chapter) 
			throws DirectoryDoesNotExistsException, SQLException
	{
		//remove the ] at the end of the path
		chapter = chapter.substring(0, chapter.length()-1);
		model.deleteChapter(chapter, book); //delete it in dataBase
	}
	
	private void deletePage(String book, String chapter, String page) 
			throws DirectoryDoesNotExistsException, SQLException
	{
		//remove the ] at the end of the path
		page = page.substring(0, page.length()-1);
		model.deletePage(page, chapter, book); //delete it in dataBase
	}
	
	//load Page
	/**
	* Es zu einem vorgegebenen Pfad eine Seite aus dem Model geladen
	* und als aktuelle Seite gespeichert. Sollte die angeforderte Seite
	* Startseite sein, wird eine Instanz der Startseite erstellt und in
	* currentPage gespeichert. Die geladene Seite ist direkt nicht von der
	* View aus ansprechbar, sondern es muessen die Inhalte oder die 
	* PageInformation ueber die entsprechenden Methoden abgefragt werden.
	* @param pageName der Name der Seite
	* @param chapterName der Name des Kapitels
	* @param bookName der Name des Buches
	 * */
	public void openPage(String pageName, String chapterName, String bookName) 
	{
		try {
			currentPage = model.loadPage(pageName, chapterName, bookName);
			Content[] content = currentPage.getContent();
			PageInformation pageInfo = currentPage.getPageInfo();
			view.showPage(content, pageInfo);
		} catch (DirectoryDoesNotExistsException e) {
			handleError(e);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			handleFatalError(e);
		}
	}
	
	//rename
	public void rename(String path, String newName)
	{
		try {
			String[] pathParts = path.split(", ");
			//remove the [ in front of the path
			pathParts[0] = pathParts[0].substring(1, pathParts[0].length());
			if( pathParts.length == 1) //is it a book ?
				renameBook(pathParts[0], newName);
			else
				if( pathParts.length == 2) // is it a chapter ?
					renameChapter(pathParts[0], pathParts[1], newName);
				else // it is a page
					renamePage(pathParts[0],pathParts[1],pathParts[2],newName);
			refreshTree();
		} catch (SQLException | ClassNotFoundException | IOException e) {
			handleFatalError(e);
		} catch(DirectoryDoesNotExistsException e){
			handleError(e);
		} 
	}
	
	private void renameBook(String book, String newName) 
			throws DirectoryDoesNotExistsException, SQLException
	{
		//remove the ] at the end of the path
		book = book.substring(0, book.length()-1);
		model.renameBook(book, newName);
	}
	
	private void renameChapter(String chapter, String book, String newName) 
			throws DirectoryDoesNotExistsException, SQLException
	{
		//remove the ] at the end of the path
		chapter = chapter.substring(0, chapter.length()-1);
		model.renameChapter(chapter, book, newName);
	}
	
	private void renamePage(String page, String chapter, String book, 
			String newName) throws DirectoryDoesNotExistsException, 
			SQLException, ClassNotFoundException, IOException
	{
		//remove the ] at the end of the path
		page = page.substring(0, page.length()-1);
		
		//load the Page
		Page tempPage = model.loadPage(page, chapter, book);
		tempPage.getPageInfo().setPageName(newName);
		
		model.savePage(tempPage); //save the page
	}
	
	//move
	public void move(String oldPath, String newPath)
	{
		try {
			String[] pathPartsOld = oldPath.split(", ");
			String[] pathPartsNew = newPath.split(", ");
			
			//is it a book ?
			if(pathPartsOld.length == 1 || pathPartsNew.length == 1)
				return; //sensless to move a book
			else
			{
				String oldBook = pathPartsOld[0];
				String newBook = pathPartsNew[0];
				String oldChapter = pathPartsOld[1];
				
				//remove the [ in front of the path
				oldBook = oldBook.substring(1, oldBook.length());
				newBook = newBook.substring(1, newBook.length());
				
				// is it a chapter ?
				if( pathPartsOld.length == 2 || pathPartsNew.length == 2)
					moveChapter(oldChapter, oldBook, newBook);
				else // it is a page
				{
					String oldPage = pathPartsOld[2];
					String newChapter = pathPartsNew[1];
					movePage(oldPage, oldChapter, oldBook, newChapter, newBook);
				}
			}
			refreshTree();
		} catch (SQLException | ClassNotFoundException | IOException e) {
			handleFatalError(e);
		} catch(DirectoryDoesNotExistsException e){
			handleError(e);
		} 
	}
	
	private void moveChapter(String oldChapter, String oldBook, String newBook) 
			throws SQLException
	{
		oldChapter = oldChapter.substring(0, oldChapter.length() - 1);
		model.move(oldChapter, oldBook, newBook);
	}
	
	private void movePage(String oldPage, String oldChapter, String oldBook, 
			String newChapter, String newBook) 
			throws DirectoryDoesNotExistsException, ClassNotFoundException,
			SQLException, IOException
	{
		oldPage = oldPage.substring(0, oldPage.length() - 1);
		Page tempPage = model.loadPage(oldPage, oldChapter, oldBook);
		tempPage.getPageInfo().setBookName(newBook);
		tempPage.getPageInfo().setChapterName(newChapter);
		model.savePage(tempPage);
	}
	
	private void refreshTree()
	{
		try {
			dataTree = model.getTree();
			view.refreshTree(dataTree);
		} catch (SQLException e) {
			handleFatalError(e);
		}
	}
	
	private void handleFatalError(Exception e)
	{
		String msg = e.getMessage() + "\n Das Programm muss beendet werden";
		view.showMessage("Fataler Fehler!", msg);
		System.exit(1);
	}
	
	private void handleError(Exception e)
	{
		view.showMessage("Warnung", e.getMessage());
	}
}
