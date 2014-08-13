package Tools;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import pageData.Content;

/**
 * Ein Tool dient zur Bearbeitung eines Seitenelements.
 * Dazu muss das Tool mit dem Seitenelement und einem JPanel initialisiert werden.
 * Ein Tool kann sich dann auf dem JPanel zeichnen und den Content
 * modifizieren oder löschen. Wird das Tool beendet, so kann dies durch das Hinzufügen
 * eines ToolReadyHandlers abgefangen und verarbeitet werden.
 * Tool ist bewusst als Interface implementiert um das mächtigere Werkzeug der Vererbung
 * für größere Tools verwendbar zu halten.
 * */
public interface Tool {
	
	/**
	 * Das Tool zeichnet sich auf dem übergebenen JPanel.
	 * Wird das Tool beendet entfernt es sich wieder.
	 * Hier werden auch alle Handler initialisiert, welche eine Interaktion mit 
	 * dem Benutzer erlauben. Ein Tool sollte es ermöglichen ein Element zu verschieben
	 * es zu bearbeiten und zu löschen. Eine Verschiebung sollte mittels der Pfeiltasten und der Maus
	 * und das Löschen mittels "entfernen" möglich sein.
	 * */
	void drawTool();
	
	/**
	 * Diese Methode wird normalerweise automatisch aufgerufen sobald das Tool beendet wird.
	 * Sollte man wünschen das Tool vorher manuell zu beenden, kann diese Methode aufgerufen werden.
	 * */
	void endEdit(ToolEndsType type);
	
	/**
	 * Hiermit kann der Seiteninhalt abgerufen werden, welcher durch das Tool bearbeitet wurde.
	 * @return der bearbeitete Seiteninhalt
	 * */
	Content getContent();
	
	/**
	 * Möchte man wissen, wann das Tool beendet wurde, kann ein ToolReadyHandler hinzugefügt werden.
	 * @param handler ein ToolReadyHandler
	 * */
	void addToolReadyHandler(ToolReadyHandler handler);
}
