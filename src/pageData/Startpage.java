package pageData;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

import java.awt.Color;

/**
 * Die Startpage gibt ist eine Seite, welche Informationen für den Benutzer über
 * die Verwendung dieses Programms beinhaltet. Die Startpage wird angezeigt, wenn
 * das Programm gestartet wird und wenn die Seite gelöscht wird, welche aktuell
 * angezeigt wird.
 * */
public class Startpage extends Page{

	public Startpage() {
		super(new PageInformation("Startseite", "", "", 800, 600));
		
		TextBox welcomeText = new TextBox(20, 20);
		welcomeText.setFontSize(50);
		welcomeText.height = 60;
		welcomeText.width = 600;
		welcomeText.setText("Willkommen bei TwoNote");
		super.createContent(welcomeText);
		
		TextBox toolbox = new TextBox(20, 100);
		toolbox.setText("Um eine Textnotiz anzulegen, wählen Sie in der Toolbox den Textmodus aus und klicken Sie auf die Seite.");
		toolbox.width = 1000;
		toolbox.setFontSize(20);
		super.createContent(toolbox);
		
		TextBox editText = new TextBox(20, 150);
		editText.setText("Um eine Textnotiz zu bearbeiten, klicken Sie auf die betreffende Notiz.");
		editText.width = 1000;
		editText.setFontSize(20);
		super.createContent(editText);
		
		TextBox hierarchie = new TextBox(20, 200);
		hierarchie.setText("Legen Sie ein Buch, innerhalb eines Buches ein Kapitel und innerhalb eines Kapitels eine neue Seite an.");
		hierarchie.width = 1000;
		hierarchie.setFontSize(20);
		super.createContent(hierarchie);
		
		TextBox delete = new TextBox(20, 250);
		delete.setText("Um eine Seite zu löschen, markieren Sie diese im Baum und wählen löschen.");
		delete.width = 1000;
		delete.setFontSize(20);
		super.createContent(delete);
		
		TextBox color = new TextBox(20, 300);
		color.setText("Um die Farbe eines Textes zu ändern, wählen Sie im Textwerkzeug \"Schriftfarbe festlegen\".");
		color.setFontColor(Color.RED);
		color.width = 1000;
		color.setFontSize(20);
		super.createContent(color);
		
		TextBox tree = new TextBox(550, 400);
		tree.setText("Klicken Sie hier auf eine Seite um diese zu öffnen. -->");
		tree.width = 1000;
		tree.setFontSize(20);
		super.createContent(tree);
		
		TextBox haveFun = new TextBox(20, 550);
		haveFun.setText("Viel Spaß mit TwoNote!");
		haveFun.width = 1000;
		haveFun.setFontSize(20);
		super.createContent(haveFun);
	}
	
}
