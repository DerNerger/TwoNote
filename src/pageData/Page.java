package pageData;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

/**
 * Diese Klasse dient der internen Verwaltung und Speicherung einer Seite.
 * Die Seite enthält PageInformation und eine HashMap mit den Inhalten.
 * Wird ein Seiteninhalt verändert, so wird die ungespeicherte Änderung
 * in einer ArrayList mittels einer ContentInstruction gespeichert.
 * Jeder Seiteninhalt wird von dieser Klasse mit einer Nummer versehen.
 * Diese startet bei 0 und wird dann bei jedem neuen Inhalt inkrementiert.
 * Soll die Seite nun in der Datenbank gespeichert werden, so werden über die
 * Schnittstelle "getNextContentInstruction" alle Änderungen abgerufen und in der Datenbank gespeichert.
 * Danach werden die ContentInstruction aus der Arraylist gelöscht. Diese Vorgänge sind
 * innerhalbt dieser Klasse gekapselt und müssen bei der Verwendung nicht berücksichtig werden.
 * Achtung: Die Schnittstele "addContent" wird nur vom Model verwendet um eine aus der Datenbank
 * geladene Seite aufzubauen. Um ein neuen Inhalt anzulegen sollte "createContent" verwendet werden.
 * */
import java.util.ArrayList;
import java.util.HashMap;

public class Page {
	
	private PageInformation pageInfo;
	private ArrayList<ContentInstruction> contentInstructions;
	private HashMap<Integer, Content> pageContent;
	private int nextContentNumber;
	
	public static final int DEFAULT_WIDTH= 800;
	public static final int DEFAULT_HEIGHT= 600;
	
	//constructor	
	/**
	 * Erstellt eine neue Seite mit parametrierbaren Seiteninformationen.
	 * Die Nummer des ersten Seiteninhalts wird auf 0 gestetzt.
	 * */
	public Page(PageInformation pageInfo)
	{
		this.pageInfo = pageInfo;
		this.contentInstructions = new ArrayList<ContentInstruction>();
		this.pageContent = new HashMap<Integer, Content>();
		nextContentNumber = 0;
	}
	
	//getter and setter
	public PageInformation getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInformation pageInfo) {
		this.pageInfo = pageInfo;
	}
	
	public Content[] getContent()
	{
		Content [] back = new Content[pageContent.size()];
		pageContent.values().toArray(back);
		return back;
	}
	
	public Content getContent(int number) throws DirectoryDoesNotExistsException
	{
		if(!pageContent.containsKey(number))
			throw new DirectoryDoesNotExistsException(DirectoryType.Content, ActionType.load, number+"");
		return pageContent.get(number);
	}
	
	/**
	 * Liefert die nächste ContentInstruction.
	 * Es sollte vorher überprüft werden, ob noch
	 * ungespeicherte Änderungen vorhanden sind.
	 * Achtung: wird die ContentInstruction korrekt bearbeitet muss
	 * zuerst "contentInstructionCompleted" aufgerufen werden, damit 
	 * die nächste ContentInstruction bearbeitet werden kann.
	 * Somit wird sichergestellt, dass die Daten jederzeit konsistent sind,
	 * auch wenn es beim Speichern einer Änderung zu einem Fehler kommt. 
	 * @throws RuntimeException es wurden bereits alle contentInstructions bearbeitet
	 * @return die nächste contentInstruction
	 * */
	public ContentInstruction getNextContentInstruction()
	{
		if(contentInstructions.size()==0)
			throw new RuntimeException("Es wurden bereits alle ContentInstructions bearbeitet");
		return contentInstructions.get(0);
	}
	
	/**
	 * Diese Methode gibt die Anzahl der noch nicht in der
	 * Datenbank gespeicherten Änderungen zurück
	 * @return Anzahl der ungespeicherten Änderungen
	 * */
	public int getContentInstructionSize()
	{
		return contentInstructions.size();
	}
	
	
	//methods
	/**
	 * Es wird ein neuer Seiteninhalt innerhalb der Seite gespeichert
	 * Der neue Inhalt wird mit einer Nummer versehen und zurückgegeben.
	 * Der Zähler für die Nummer des nächsten Seiteninhalts wird inkrementiert.
	 * Es wird eine contentInstruction angelegt.
	 * @return der Seiteninhalt mit Nummer
	 * */
	public Content createContent(Content cont)
	{
		//interface for the controller
		cont.setNumber(nextContentNumber);
		pageContent.put(nextContentNumber, cont);
		contentInstructions.add(new ContentInstruction(nextContentNumber, ActionType.create));
		nextContentNumber++;
		return cont;
	}
	
	/**
	 * Der übergebene Seiteninhalt wird von der Seite gelöscht.
	 * Falls er nicht Teil der Seite ist, wird eine Exception geworfen.
	 * Falls der Seiteninhalt noch gar nicht in der Datenbank gespeichert wurde
	 * wird die Anweisung den Inhalt zu erstellen aus den contentInstructions
	 * gelöscht.
	 * Falls nicht wird eine neue contentInstruction angelegt, welche dokumentiert, dass der
	 * Inhalt aus der Datenbank gelöscht werden soll.
	 * @param number die Nummer des zu löschenden Seiteninhalts
	 * @throws DirectoryDoesNotExistsException es existiert kein Seiteninhalt mit der angegebenen Nummer
	 * */
	public void deleteContent(int number)
	{
		if(!pageContent.containsKey(number))
			throw new DirectoryDoesNotExistsException(DirectoryType.Content, ActionType.delete, number+"");
		for(ContentInstruction instru : contentInstructions)
		{
			//no need to create and delete
			if(instru.getContentNumber() == number && instru.getActionType() == ActionType.create)
			{
				contentInstructions.remove(instru);
				pageContent.remove(number);
				return;
			}
		}
		pageContent.remove(number);
		contentInstructions.add(new ContentInstruction(number, ActionType.delete));
	}
	
	/**
	 * Der übergebene Seiteninhalt wird innerhalb der Seite gespeichert.
	 * Es wird den contentInstructions ein Eintrag hinzugefügt, sodass die
	 * Änderung in der Datenbank übernommen wird, falls diese Seite das nächste
	 * mal gespeichert wird.
	 * @param cont der zu speichernde Inhalt
	 * */
	public void saveContent(Content cont)
	{
		int number = cont.getNumber();
		contentInstructions.add(new ContentInstruction(number, ActionType.save));
	}
	
	/**
	 * Der Seite wird mitgeteilt, dass eine contentInstruction korrekt bearbeitet wurde.
	 * Die contentInstruction wird gelöscht und es kann die nächste abgefragt werden.
	 * @throws RuntimeException Es wurden bereits alle ContentInstructions abgeschlossen
	 * */
	public void contentInstructionCompleted()
	{
		if(contentInstructions.size()==0)
			throw new RuntimeException("Es wurden bereits alle ContentInstructions abgeschlossen");
		contentInstructions.remove(0);
	}
	
	/**
	 * Dies ist nur die Schnittstelle der Seite zum Model.
	 * Wird die Seite aus der Datenbank geladen, ruft das Model jeden
	 * dazugehörigen Seiteninhalt ab und fügt ihn mit dieser Methode zur
	 * Seite hinzu. Dem Inhalt wird einer Nummer hinzugefügt und der Nummerzähler
	 * wird inkrementiert
	 * @param cont Der hinzuzufügende Inhalt
	 * */
	public void addContent(Content cont)
	{
		//interface for the model
		int number = cont.getNumber();
		pageContent.put(number, cont);
		if(nextContentNumber<=number)
			nextContentNumber=number+1;
	}
}
