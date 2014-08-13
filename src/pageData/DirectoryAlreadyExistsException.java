package pageData;

/**
 *@author Felix Kibellus
 *@version 1.0 
 */

/**
 * Diese Exception dient der Information über eine fehlerhaften Zugriff auf die Datenbank.
 * Soll ein Verzeichnis angelegt werden, welches bereits existiert wird diese
 * Exception geworfen. Es wird gespeichert, ob es sich um ein Buch, ein Kapitel, eine Seite
 * oder einen Seiteninhalt handelt. Des Weiteren wird gespeichert, bei welcher Aktion die
 * Ausnahme aufgetreten ist. Im Normalfall ist dies create. 
 * */
public class DirectoryAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private DirectoryType dirType;
	private ActionType actionType;
	private String directoryTitle;
	
	//constructors
	/**
	 * Es wird ein Objekt der Klasse DirectoryAlreadyExistsException erzeugt.
	 * @param dirType der Typ des problematischen Verzeichnisses
	 * @param actionType die Art der Aktion bei welcher der Fehler aufgetreten ist
	 * @param directoryTitle der Titel des Verzeichnisses
	 * */
	public DirectoryAlreadyExistsException (DirectoryType dirType, 
			ActionType actionType, String directoryTitle) {
		super("Es existiert bereits ein Verzeichnis mit diesem Namen");
		this.dirType = dirType;
		this.actionType = actionType;
		this.directoryTitle = directoryTitle;
	}
	
	/**
	 * Es wird ein Objekt der Klasse DirectoryAlreadyExistsException erzeugt.
	 * Hier kann zusätzlich eine Fehlernachricht übergeben werden.
	 * @param dirType der Typ des problematischen Verzeichnisses
	 * @param actionType die Art der Aktion bei welcher der Fehler aufgetreten ist
	 * @param directoryTitle der Titel des Verzeichnisses
	 * @param msg die Fehlermeldung
	 * */
	public DirectoryAlreadyExistsException (DirectoryType dirType
			, ActionType actionType, String directoryTitle, String msg) {
		super(msg);
		this.dirType = dirType;
		this.actionType = actionType;
		this.directoryTitle = directoryTitle;
	}

	//getter and setter
	public DirectoryType getDirType() {
		return dirType;
	}

	public void setDirType(DirectoryType dirType) {
		this.dirType = dirType;
	}

	public String getDirectoryTitle() {
		return directoryTitle;
	}

	public void setDirectoryTitle(String directoryTitle) {
		this.directoryTitle = directoryTitle;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
}
