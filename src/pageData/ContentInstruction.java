package pageData;

/**
 *@author Felix Kibellus
 *@version 1.0 
 */

/**
 * Diese Klasse speichert eine in der Datenbank noch nicht
 * eingetragene Änderung innerhalb einer Seite.
 * Pro Änderung muss eine ContentInstruction angelegt werden.
 * Jede ContentInstruction speichert die Nummer des betroffenen
 * Contents und die Aktion welche vorgenommen werden muss.
 * Aktion kann löschen, speichern, und erstellen sein
 * */
public class ContentInstruction {
	
	private int contentNumber;
	private ActionType actionType;
	
	//Constructor
	/**
	 * Es wird ein Objekt der Klasse ContentInstruction erstellt.
	 * Die Attribute werden anhand der Parameter belegt.
	 * @param contentNumber Nummer des betroffenen Seiteninhalts
	 * @param actionType löschen, speichern oder erstellen
	 * */
	public ContentInstruction(int contentNumber, ActionType actionType)
	{
		this.contentNumber = contentNumber;
		this.actionType = actionType;
	}

	//getter and setter
	public int getContentNumber() {
		return contentNumber;
	}

	public void setContentNumber(int contentNumber) {
		this.contentNumber = contentNumber;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
}
