package dataManagement;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.DatatypeConverter;

import pageData.PageInformation;


/**
 * Dies ist die konkrete Implementierung der Schnittstelle zur Datenbank.
 * Dazu implementiert diese Klasse das Interface IDataBase, welches eine 
 * abstrakte Definition der Schnittstelle von Java zu einer beliebigen
 * Datenbank vorgibt. Bei der konkret in dieser Klasse verwendeten 
 * Datenbank handelt es sich um MySql
 * */
public class MySqlDatabase implements IDataBase {

	/**
	 *Das Attribut con ist eine Instanz der Klasse java.sql.Connection 
	 *und liefert die Schnittstelle zu jdbc.
	 * */
	private Connection con;
	
	//constructor
	/**
	 * Der Konstruktor laedt den jdbc Treiber und richtet danach eine 
	 * Datenbankverbindung mittels der uebergebenen Daten ein, indem er 
	 * das Attribut con instanziiert.
	 * @param url der Pfad zur Datenbank
	 * @param name der Loginname zur Datenbank
	 * @param pw das Passwort zur Datenbank
	 * @throws ClassNotFoundException Fehler beim Laden des jdbc Treibers
	 * @throws SQLException Datenbankfehler
	 * */
	public MySqlDatabase (String url, String name, String pw) 
			throws ClassNotFoundException, SQLException
	{		
		//load CDBC driver
		Class.forName("com.mysql.jdbc.Driver");

		//initialize connection
		con = DriverManager.getConnection(url, name, pw);
	}
	
	//create
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void createBook(String bookName) throws SQLException {
		Statement stmt =null;
		try  {		
			stmt = con.createStatement();
			String query="INSERT INTO `Books` (`Name`) VALUES ('"+bookName+"');";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void createChapter(String chapterName, int bookID) 
			throws SQLException {
		Statement stmt =null;
		try  {		
			stmt = con.createStatement();
			String query = "INSERT INTO `Chapter` (`Name`,`Book`) VALUES ('"+chapterName+"', '"+bookID+"');";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void createPage(PageInformation pageInfo, int chapterID) 
			throws SQLException {
		Statement stmt =null;
		String pageName = pageInfo.getPageName();
		int width = pageInfo.getWidth();
		int height = pageInfo.getHeight();
		try  {		
			stmt = con.createStatement();
			String query = "INSERT INTO `Pages` (`Name`,`Chapter`,`Width`,`Height`) " +
					"VALUES ('"+pageName+"', '"+chapterID+"', "+width+", "+height+");";
			stmt.executeUpdate(query);	
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void createContent(byte[] contentBytes, int contentNumber, int pageID)
			throws SQLException {
		Statement stmt =null;
		String byteString = DatatypeConverter.printBase64Binary(contentBytes);
		try  {
			stmt = con.createStatement();
			String query="INSERT INTO `Content` (`Objekt`, `Page`, `Number`)" +
					" VALUES ('"+byteString+"',"+pageID+","+contentNumber+");";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	//delete
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void deleteBook(int bookID) throws SQLException {
		Statement stmt =null;
		try {
			stmt = con.createStatement();
			String query="DELETE FROM `Books` WHERE ID="+bookID+";";
			stmt.executeUpdate(query);
		} 
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void deleteChapter(int chapterID) throws SQLException {
		Statement stmt =null;
		try {
			stmt = con.createStatement();
			String query="DELETE FROM `Chapter` WHERE ID="+chapterID+";";
			stmt.executeUpdate(query);
		} 
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void deletePage(int pageID) throws SQLException {
		Statement stmt =null;
		try {
			stmt = con.createStatement();
			String query="DELETE FROM `Pages` WHERE ID="+pageID+";";
			stmt.executeUpdate(query);
		} 
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void deleteContent(int contentID) throws SQLException {
		Statement stmt =null;
		try {
			stmt = con.createStatement();
			String query="DELETE FROM `Content` WHERE ID="+contentID+";";
			stmt.executeUpdate(query);
		} 
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	//save
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void saveContent(byte[] contentBytes, int contentID) 
			throws SQLException {
		Statement stmt =null;
		String byteString = DatatypeConverter.printBase64Binary(contentBytes);
		try  {	
			stmt = con.createStatement();
			String query="UPDATE `Content` SET Objekt = '"+byteString+"'  WHERE ID = "+contentID+";";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void savePage(PageInformation pageInfo, int chapterID, int pageID) 
			throws SQLException {
		Statement stmt =null;
		String pageName = pageInfo.getPageName();
		int width = pageInfo.getWidth();
		int height = pageInfo.getHeight();
		try  {	
			stmt = con.createStatement();
			String query="UPDATE `Pages` SET Name = '"+pageName+"',Chapter = "+chapterID+
					" ,Height = "+height+",Width = "+width+"  WHERE ID = "+pageID+";";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}

	//load
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public byte[] loadContent(int contentID) throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Content` WHERE ID= "+contentID+";";
			rst = stmt.executeQuery(query);
			rst.next();
			String byteString = rst.getString("Objekt");
			return DatatypeConverter.parseBase64Binary(byteString);
		}
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public PageInformation loadPageInformation(int pageID) throws SQLException{
		Statement stmt =null;
		ResultSet rst =  null;		
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Pages` WHERE ID= "+pageID+";";
			rst = stmt.executeQuery(query);
			//get attributes
			
			rst.next();
			int width = rst.getInt("Width");
			int height = rst.getInt("Height");
			String pageTitle = rst.getString("Name");
			
			//returns the PageInformation to pageTitle
			return new PageInformation(pageTitle, null, null, width, height);
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}
	
	//exists
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public boolean existsBook(String bookName) throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;		
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Books` WHERE Name= '"+bookName+"';";
			rst = stmt.executeQuery(query);
			if(rst.next())
				return true;
			else
				return false;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public boolean existsChapter(String chapterName, int bookID) 
			throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Chapter` WHERE Name= '"+chapterName+"' AND Book= "+bookID+";";
			rst = stmt.executeQuery(query);
			if(rst.next())
				return true;
			else
				return false;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public boolean existsPage(String pageName, int chapterID) 
			throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Pages` WHERE Name= '"+pageName+"' AND Chapter= "+chapterID+";";
			rst = stmt.executeQuery(query);
			if(rst.next())
				return true;
			else
				return false;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public boolean existsContent(int contentNumber, int pageID) 
			throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Content` WHERE Number= "+contentNumber+" AND Page= "+pageID+";";
			rst = stmt.executeQuery(query);
			if(rst.next())
				return true;
			else
				return false;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	//get ID
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public int getBookID(String bookName) throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Books` WHERE Name= '"+bookName+"';";
			rst = stmt.executeQuery(query);
			rst.next();
			int bookID=rst.getInt(1);
			return bookID;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public int getChapterID(String chapterName, int bookID) 
			throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Chapter` WHERE Book= '"+bookID+"' AND Name= '"+chapterName+"';";
			rst = stmt.executeQuery(query);
			rst.next();
			int chapterID=rst.getInt(1);
			return chapterID;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public int getPageID(String pageName, int chapterID) throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Pages` WHERE Chapter= '"+chapterID+"' AND Name= '"+pageName+"';";
			rst = stmt.executeQuery(query);
			rst.next();
			int pageID =rst.getInt(1);
			return pageID;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public int getContentID(int ContentNumber, int pageID) throws SQLException{
		Statement stmt =null;
		ResultSet rst =  null;
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Content` WHERE Page= '"+pageID+"' AND Number= "+ContentNumber+";";
			rst = stmt.executeQuery(query);
			rst.next();
			int contentID =rst.getInt("ID");
			return contentID;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	//get Children
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public String[] getBookChildren(int bookID) throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;		
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Chapter` WHERE Book= "+bookID+";";
			rst = stmt.executeQuery(query);
			rst.last();
			int childrenNumber = rst.getRow();
			String [] children = new String [childrenNumber];
			rst.beforeFirst();
			for (int i = 0; i < children.length; i++) {
				rst.next();
				children[i] = rst.getString("Name");
			}
			return children;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public String[] getChapterChildren(int chapterID) throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;		
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Pages` WHERE Chapter= "+chapterID+";";
			rst = stmt.executeQuery(query);
			rst.last();
			int childrenNumber = rst.getRow();
			String [] children = new String[childrenNumber];
			rst.beforeFirst();
			for (int i = 0; i < children.length; i++) {
				rst.next();
				children[i] = rst.getString("Name");
			}
			return children;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public int[] getPageChildren(int pageID) throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;		
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Content` WHERE Page= "+pageID+";";
			rst = stmt.executeQuery(query);
			rst.last();
			int childrenNumber = rst.getRow();
			int [] children = new int[childrenNumber];
			rst.beforeFirst();
			for (int i = 0; i < children.length; i++) {
				rst.next();
				children[i] = rst.getInt("ID");
			}
			return children;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public String[] getAllBooks() throws SQLException {
		Statement stmt =null;
		ResultSet rst =  null;		
		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM `Books`;";
			rst = stmt.executeQuery(query);
			rst.last();
			int bookNumber = rst.getRow();
			String [] books = new String[bookNumber];
			rst.beforeFirst();
			for (int i = 0; i < books.length; i++) {
				rst.next();
				books[i] = rst.getString("Name");
			}
			return books;
		} 
		finally
		{
			if(rst!=null) rst.close();
			if(stmt!=null) stmt.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void renameBook(int bookID, String newName) 	throws SQLException
	{
		Statement stmt =null;
		try  {		
			stmt = con.createStatement();
			String query="UPDATE `Books` SET Name = '"+newName+"', WHERE ID= '"+bookID+"';";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void renameChapter(int chapterID, String newName)throws SQLException
	{
		Statement stmt =null;
		try  {		
			stmt = con.createStatement();
			String query="UPDATE `Chapter` SET Name = '"+newName+"', WHERE ID= '"+chapterID+"';";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}
	
	//move
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void move(int chapterID, int newBookId) throws SQLException
	{
		Statement stmt =null;
		try  {		
			stmt = con.createStatement();
			String query="UPDATE `Chapter` SET Book = '"+newBookId+"', WHERE ID= '"+chapterID+"';";
			stmt.executeUpdate(query);
		}
		finally
		{
			if(stmt!=null) stmt.close();
		}
	}
}
