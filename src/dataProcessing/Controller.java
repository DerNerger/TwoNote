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
import Tools.TextEditTool;
import Tools.EditTool;

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
	//testcomment
	private View view;
	private Model model;
	private Tree dataTree;
	private Page currentPage;
	private EditTool currentTool;
	private EditMod editMod;
	private DirectoryClipboard clipboard;
	
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
			this.clipboard = null;
		} catch (ClassNotFoundException | SQLException e) {
			handleFatalError(e);
		}
	}
	
	//directory manipulation -------------------------------------------------
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
	

	public void createContent(Content c) 
	{
		currentPage.createContent(c);
		savePage();
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
				
				if(isCurrentPage(path))
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
	public void move(String[] oldPath, String[] newPath)
	{
		try {
			if(oldPath.length == 3) //is it a chapter?
				model.moveChapter(oldPath[2], oldPath[1], newPath[1]);
			else if(oldPath.length ==4) //is it a page?
			{
				model.movePage(oldPath[3], oldPath[2], oldPath[1], 
						newPath[2], newPath[1]);
				if(isCurrentPage(oldPath))
				{
					oldPath[1] = newPath[1];
					oldPath[2] = newPath[2];
					openPage(newPath);
				}
			}
			dataTree = model.getTree();
			view.refreshTree();
		} catch (SQLException  e) {
			handleFatalError(e);
		} catch(DirectoryDoesNotExistsException |
				DirectoryAlreadyExistsException e){
			handleError(e);
		} 
	}
	
	//cut
	public void cutDirectories(String[][] paths)
	{
 	   //checking the depth
 	   int depth = paths[0].length;
 	   for (int i = 1; i < paths.length; i++) {
 		   if(paths[i].length != depth)
 		   {
 			   String msg = "Es können nur Verzeichnisse der " +
 			   		"gleichen Tiefe \ngemeinsam verschoben werden.";
 			   view.showMessage("Ausschneiden", msg);
 			   return;
 		   }
 	   }
 	   clipboard = new DirectoryClipboard(paths);
	}
	
	//paste
	public void pasteDirectories(String[] newPath)
	{
		if(newPath.length != clipboard.getPathDepth() - 1){
			String msg = "Die Tiefe der Verzeichnisse darf sich " +
					"nicht unterscheiden.";
			view.showMessage("Einfügen", msg);
			return;
		}
		String[][] src = clipboard.getPaths();
		for (int i = 0; i < src.length; i++) {
			move(src[i], newPath);
		}
	}
	
	public void savePage()
	{
		try {
			model.savePage(currentPage);
		} catch (DirectoryDoesNotExistsException e) {
			handleError(e);
		} catch (SQLException | IOException e) {
			handleFatalError(e);
		}
	}

	private boolean isCurrentPage(String[] oldPath) {
		PageInformation info = currentPage.getPageInfo();
		boolean sameName = info.getPageName().equals(oldPath[3]);
		boolean sameChapter = info.getChapterName().equals(oldPath[2]);
		boolean sameBook = info.getBookName().equals(oldPath[1]);
		return (sameName && sameChapter && sameBook);
	}

	private void handleFatalError(Exception e)
	{
		/* TODO: Exit verbessern
		String msg = e.getMessage() + "\n Das Programm muss beendet werden";
		view.showMessage("Fataler Fehler!", msg);
		System.exit(1);
		*/
		savePage();
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
		savePage();
		System.exit(0);
	}
	
	public void setEditMod(EditMod mod)
	{
		this.editMod = mod;
	}
	
	public EditMod getEditMod()
	{
		return editMod;
	}
	
	//Content manipulation ---------------------------------------------------
	
	public void openTool(EditTool tool)
	{
		if(currentTool != null)
			closeTool();
		currentTool = tool;
		currentTool.startEdit();
	}
	
	public void closeTool()
	{
		if(currentTool != null)
		{
			Content editCont = currentTool.stopEdit();
			if(editCont == null)
			{
				int number = currentTool.getContentNumber();
				view.removeContent(currentPage.getContent(number));
				currentPage.deleteContent(number);
			}
			else
				currentPage.saveContent(editCont);
			savePage();
			currentTool = null;
		}
	}
	
	public Content getContent(int number)
	{
		return currentPage.getContent(number);
	}
}
