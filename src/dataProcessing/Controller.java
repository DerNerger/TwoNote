package dataProcessing;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.io.IOException;
import java.sql.SQLException;

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
			this.dataTree = model.getTree();
			this.view.refreshBookComboBox();
			this.view.refreshTree();
			//this.currentPage = gute default loesung
			//this.currentTool = gute default loesung
			this.editMod = EditMod.Curser;
		} catch (ClassNotFoundException | SQLException e) {
			handleFatalError(e);
		}
	}
	
	//create
	public void create(String[] path)
	{
		try {
			if(path.length == 1)
				model.createBook(path[0]);
			else if(path.length == 2)
				model.createChapter(path[1], path[0]);
			else if(path.length == 3)
				model.createPage(path[2], path[1], path[0]);
			
			dataTree = model.getTree();
			view.refreshBookComboBox();
			view.refreshTree();
		} catch (DirectoryAlreadyExistsException 
				| DirectoryDoesNotExistsException e) {
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
	public void delete(String[] path)
	{
		try {
			if(path.length == 2)
				model.deleteBook(path[1]);
			else if(path.length == 3)
				model.deleteChapter(path[2], path[1]);
			else if(path.length == 4)
				model.deletePage(path[3], path[2], path[1]);
			dataTree = model.getTree();
			view.refreshBookComboBox();
			view.refreshTree();
		} catch (SQLException e) {
			handleFatalError(e);
		} catch(DirectoryDoesNotExistsException e){
			handleError(e);
		}
	}
	
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
	public void openPage(String[] path) 
	{
		if(path.length != 4)
			return;
		try {
			currentPage = model.loadPage(path[3], path[2], path[1]);
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
	public void rename(String[] path, String newName)
	{
		try {
			if(path.length == 2)
				model.renameBook(path[1], newName);
			else if (path.length == 3)
				model.renameChapter(path[2], path[1], newName);
			else if (path.length == 4)
			{
				model.renamePage(path[3], path[2], path[1], newName);
				PageInformation info = currentPage.getPageInfo();
				
				boolean sameName = info.getPageName().equals(path[3]);
				boolean sameChapter = info.getChapterName().equals(path[2]);
				boolean sameBook = info.getBookName().equals(path[1]);
				if(sameName && sameChapter && sameBook)//its the current page
				{
					path[3] = newName;
					openPage(path);
				}
			}
			this.dataTree = model.getTree();
			view.refreshBookComboBox();
			view.refreshTree();
		} catch (SQLException  e) {
			handleFatalError(e);
		} catch(DirectoryDoesNotExistsException |
				DirectoryAlreadyExistsException e){
			handleError(e);
		} 
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
			//refreshTree();
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
	
	
	private void handleFatalError(Exception e)
	{
		/* TODO: Exit verbessern
		String msg = e.getMessage() + "\n Das Programm muss beendet werden";
		view.showMessage("Fataler Fehler!", msg);
		System.exit(1);
		*/
		System.out.println(e.getMessage());
	}
	
	private void handleError(Exception e)
	{
		view.showMessage("Warnung", e.getMessage());
	}
	
	public Tree getTree()
	{
		return dataTree;
	}
	
	public void exit()
	{
		try {
			model.savePage(currentPage);
			System.exit(0);
		} catch (DirectoryDoesNotExistsException e) {
			handleError(e);
		} catch (SQLException | IOException e) {
			handleFatalError(e);
		}
	}
}
