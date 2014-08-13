package pageData;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

/**
 * Diese Ausnahme beinhaltet Informationen über einen fehlerhaften Datenbankzugriff
 * Wenn auf ein Verzeichnis zugegriffen wird, welches nicht existiert, wird diese Exception
 * geworfen. Diese Klasse beinhaltet Informationen über den Namen des fehlerhaften
 * Verzeichnisses, den Typ des Verzeichnisses und über die Aktion welche die Ausnahme 
 * ausgelöst hat.
 * */
public class DirectoryDoesNotExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private DirectoryType dirType;
	private ActionType actionType;
	private String directoryTitle;
	
	//constructors
	/**
	 * Es wird ein Objekt der Klasse DirectoryDoesNotExistsException erzeugt.
	 * Anhand der übergebenen Parameter werden die Attribute belegt.
	 * @param dirType der Typ des fehlerhaften Verzeichnisses
	 * @param actionType die Aktion welche den Fehler ausgelöst hat(create, delete, load, save,... )
	 * @param directoryTitle der Titel des fehlerhaften Verzeichnisses
	 * */
	public DirectoryDoesNotExistsException (DirectoryType dirType, ActionType actionType,
			String directoryTitle) {
		super("Das angeforderte Verzeichnis existiert nicht");
		this.dirType = dirType;
		this.actionType = actionType;
		this.directoryTitle = directoryTitle;
	}
	
	/**
	 * Es wird ein Objekt der Klasse DirectoryDoesNotExistsException erzeugt.
	 * Anhand der übergebenen Parameter werden die Attribute belegt.
	 * Zusätzlich kann eine Fehlermeldung mit übergeben werden.
	 * @param dirType der Typ des fehlerhaften Verzeichnisses
	 * @param actionType die Aktion welche den Fehler ausgelöst hat(create, delete, load, save,... )
	 * @param directoryTitle der Titel des fehlerhaften Verzeichnisses
	 * @param msg die Fehlermeldung
	 * */
	public DirectoryDoesNotExistsException (DirectoryType dirType, ActionType actionType,
			String directoryTitle, String msg) {
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
