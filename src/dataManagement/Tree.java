package dataManagement;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.util.ArrayList;

/**
 * Diese Klasse dient dazu, die Information über den Verzeichnisbaum zu speichern.
 * Die verwendete Datenstruktur ist ein Baum. Ein Tree speichert in einer ArrayList
 * alle seiner Kindknoten. Jeder Kindknoten ist wiederum ein Objekt der Klasse Tree.
 * Die höchste im Programm sinnvoll verwendbare Tiefe eines solchen Baumes ist 3.
 * Im ersten Level liegen die Bücher, im zweiten liegen die Kapitel und im dritten
 * die Seiten. Jeder Baum speichert seinen Titel, um später die Namen der einzelnen
 * Elemente der Verzeichnisstruktur rekonstruieren zu können.
 * */
public class Tree {
	/**
	 * Der Titel des Baums
	 * */
	private String title;
	
	/**
	 * Eine Liste aller Kindbäume
	 * */
	private ArrayList<Tree> children;
	
	/**
	 * Der Baum bekommt den Defaulttitel "root" und die Liste wird initialisiert
	 * */
	public Tree()
	{
		this.title = "root";
		children = new ArrayList<>();
	}
	
	/**
	 * Der Baum wird mit einem vorgegebenen Titel erstellt.
	 * Die Liste wird initialisiert
	 * @param title der Titel des Baums
	 */
	public Tree(String title)
	{
		this.title = title;
		children = new ArrayList<>();
	}
	
	/**
	 * Erzeugt einen Kindbaum zu vorgegebenen Titel 
	 * und nimmt diesen in die Liste aller Kindbäume auf
	 * @param title der Titel des Kindbaumes
	 */
	public void addChildren(String title)
	{
		children.add(new Tree(title));
	}
	
	/**
	 * Nimmt den übergebenen Kindbaum in die Liste aller Kindbäume auf
	 * @param t Der einzufügende Kindbaum
	 **/
	public void addChildren(Tree t)
	{
		children.add(t);
	}
	
	@Override
	/**
	 * Diese Methode gibt einen String zurück, welcher aus dem Titel des Baumes besteht
	 * @return der Titel
	 * */
	public String toString()
	{
		return title;
	}
	
	/**
	 * Diese Methode gibt die Liste aller Kindbäume zurück, sodass darüber iteriert werden kann.
	 * @return die Kindbäume
	 * */
	public ArrayList<Tree> getChildren()
	{
		return children;
	}
	
	/**
	 * Diese Methode gibt zu einem String den Kindbaaum zurueck, der diesen
	 * String als Namen eingetragen hat.
	 * @return Den Kindbaum mit uebergebenem Namen
	 * @trows RuntimeException der Name ist nicht Vorhanden
	 */
	public Tree getChildren(String name)
	{
		for(Tree t : children)
			if(t.title.equals(name))
				return t;
		throw new RuntimeException(name + " existiert nicht");
	}
	
	/**
	 * Diese Methode gibt zurück, ob dieser Baum Kindbäume gespeichert hat.
	 * @return dieser Baum besitzt Kindbäume
	 */
	public boolean hasNoChildren()
	{
		return children.isEmpty();
	}
	
}
