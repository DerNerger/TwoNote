package dataManagement;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import pageData.Content;

/**
 * Diese Klasse serialisiert ein Objekt der Klasse Content zu einem byte[]
 * oder deserialisiert ein byte[] zu einem Objekt der Klasse Content.
 * Diese Funktionalität wurde in diese Klasse ausgelagert um die Komplexität
 * der anderen Klassen zu verringernm, indem man die Funktionen um das serialisieren
 * als Obkjekt kapselt.
 * */
public class Serializer {
	
	//methods
	/**
	 * Diese Methode serialisiert ein Objekt vom Typ Content
	 * @param cont der zu serialisierende Content.
	 * @throws IOException Fehler beim serialisieren
	 * @return das Object als bytecode
	 * */
	public byte[] serialize(Content cont) throws IOException
	{
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
				ObjectOutput out = new ObjectOutputStream(bos)) {
			
			out.writeObject(cont);
			byte[] bytes = bos.toByteArray();
			return bytes;
		}
	}
	
	/**
	 * Diese Methode deserialisiert ein byte[]
	 * @param bytes der zu deserialisierende bytecode
	 * @throws IOException Fehler beim deserialisieren
	 * @throws ClassNotFoundException fehler beim Lesen des Objekts
	 * @return ein Objekt der Klasse Content
	 * */
	public Content deserialize(byte[] bytes) throws ClassNotFoundException, IOException
	{
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInput in = new ObjectInputStream(bis)){
			
			  Object o = in.readObject();
			  return (Content)o;
		}
	}
	
}
