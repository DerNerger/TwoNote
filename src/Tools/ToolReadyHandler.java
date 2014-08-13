package Tools;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import pageData.Content;

/**
 * Dieser Handler kann als anonyme innere Klasse implementiert werden,
 * falls es nötig ist darauf zu reagieren, wenn das Tool die Bearbeitung 
 * eines Seiteninhalts abgeschlossen hat. Für üblich wird dieser Handler
 * in der View implementiert um das Panel mit dem bearbeiteten Seitenelement
 * zu aktualisieren.
 * */
public interface ToolReadyHandler {
	
	/**
	 * Diese Methode wird vom Tool ausgeführt, falls dies mit der 
	 * Bearbeitung fertig ist.
	 * @param c der bearbeitete Seiteninhalt
	 * @param type gibt an, ob das Tool regulär beendet wurde oder den Fokus verloren hat
	 * */
	void toolEnds(Content c, ToolEndsType type);
}
