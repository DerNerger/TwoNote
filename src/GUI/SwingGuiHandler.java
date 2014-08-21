package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import pageData.Content;
import pageData.PageInformation;
import pageData.TextBox;
import Tools.DrawTool;
import Tools.EditMod;
import Tools.TextDrawTool;
import Tools.EditTool;
import Tools.TextEditTool;
import dataManagement.Tree;
import dataProcessing.Controller;

public class SwingGuiHandler extends SwingGui implements View, FocusListener {
	
	private static final long serialVersionUID = 1L;
	private Controller con;
	private HashMap<Content, Component> map;
	private DrawTool drawTool;
	
	public SwingGuiHandler(Controller con)
	{
		super();
		this.setVisible(true);
		this.con = con;
		map = new HashMap<Content, Component>();
		drawTool = null;
	}

	//interface view ----------------------------------------------------------
	@Override
	public void refreshTree() {
		Tree tree = con.getTree();
		DefaultMutableTreeNode rootNode = 
				new DefaultMutableTreeNode("Meine Notizen");
		for(Tree book : tree.getChildren())
		{
			DefaultMutableTreeNode bookNode = 
					new DefaultMutableTreeNode(book);
			for(Tree chapter : book.getChildren())
			{
				DefaultMutableTreeNode chapterNode =
						new DefaultMutableTreeNode(chapter);
				for(Tree page : chapter.getChildren())
				{
					chapterNode.add(new DefaultMutableTreeNode(page));
				}
				bookNode.add(chapterNode);
			}
			rootNode.add(bookNode);
		}
		jTree1.setModel(new DefaultTreeModel(rootNode));
	}
	
	@Override
	public void refreshBookComboBox()
	{
		jComboBoxBook.removeAllItems();
		jComboBoxBook2.removeAllItems();
		for(Tree t : con.getTree().getChildren())
		{
			jComboBoxBook.addItem(t.toString());
			jComboBoxBook2.addItem(t.toString());
		}
	}

	@Override
	public void showPage(Content[] content, PageInformation pageInfo) {
		String pageName = pageInfo.getPageName();
		int height = pageInfo.getHeight();
		int width = pageInfo.getWidth();
		
		//open the page
		jPanelPage.removeAll();
		jPanelPage.setBackground(Color.LIGHT_GRAY);
		jPanelPage.setLayout(null);
		jPanelPage.setPreferredSize(new Dimension(width, height));
		jTabbedPane1.remove(jScrollPanePage);
		jTabbedPane1.addTab(pageName, jScrollPanePage);
		
		//open the content
		for(Content c : content)
			drawContent(c);
		refreshPage();
	}

	@Override
	public void showMessage(String title, String msg) {
		jLabelError.setText(title);
		jTextAreaErrorMessage.setText(msg); //TODO: erst bei OK beenden
		jDialogError.setVisible(true);
	}
	
	@Override
	public void removeContent(Content cont) {
		Component comp = map.get(cont);
		jPanelPage.remove(comp);
		comp.setVisible(false);
		refreshPage();
	}
	
	//interface Focuslistener -------------------------------------------------
	@Override
	public void focusLost(FocusEvent arg0) {
		con.closeTool();
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		System.out.println("inc focus");
		Component comp = arg0.getComponent();
		Content c = con.getContent(Integer.parseInt(comp.getName()));
		TextEditTool tool = null;
		EditMod editMod = con.getEditMod();
		switch(c.getContentType())
		{
		case textBox:
			if(editMod != EditMod.Text && editMod != EditMod.Curser)
				return;
			if(editMod == editMod.Curser)
			{
				con.setEditMod(EditMod.Text);
				jToggleButtonText.setSelected(true);
			}
			TextBox box = (TextBox)c;
			JTextPane pane = (JTextPane) comp;
			tool = new TextEditTool(box, pane, jPanelPage);
			tool.setToolbar(jToolBarTool);
			refreshPage();
			break;
		default:
			break;
		}
		if(tool != null)
			con.openTool(tool);
			
	}
	
	//handler -----------------------------------------------------------------
	@Override
	protected void jButtonCreateActionPerformed(ActionEvent evt)
	{
		if(jTabbedPaneCreate.getSelectedComponent() == jPanelCreateBook)
		{
			String bookName = jTextFieldBook.getText();
			String[] path = {bookName};
			con.create(path);
			jTextFieldBook.setText("neues Buch");
		}
		else if(jTabbedPaneCreate.getSelectedComponent() == jPanelCreateChapter)
		{
			String bookName = (String) jComboBoxBook.getSelectedItem();
			String chapterName = jTextFieldChapter.getText(); 
			String[] path = {bookName, chapterName};
			con.create(path);
			jTextFieldChapter.setText("neues Kapitel");
		}
		else if(jTabbedPaneCreate.getSelectedComponent() == jPanelCreatePage)
		{
			String bookName = (String) jComboBoxBook2.getSelectedItem();
			String chapterName = (String) jComboBoxChapter.getSelectedItem();
			String pageName = (String) jTextFieldPage.getText();
			String[] path = {bookName, chapterName, pageName};
			con.create(path);
			jTextFieldPage.setText("neue Seite"); 
		}
		jDialogCreate.setVisible(false);
	}

	@Override
	protected void jComboBoxBook2ActionPerformed(ActionEvent evt) {
		String selectedBook = (String) jComboBoxBook2.getSelectedItem();
		if(selectedBook == null)
			return;
		Tree book = con.getTree().getChildren(selectedBook);
		jComboBoxChapter.removeAllItems();
		for(Tree t : book.getChildren())
			jComboBoxChapter.addItem(t.toString());
    }
	
	@Override
    protected void jButtonDeleteActionPerformed(ActionEvent evt) {
    	TreePath[] treePaths =  jTree1.getSelectionPaths();
    	for(TreePath treePath : treePaths)
    	{
    		Object[] path = treePath.getPath();
    		String[] pathString = new String[path.length];
    		for (int i = 0; i < pathString.length; i++) {
    			pathString[i] = path[i].toString();
			}
    		con.delete(pathString);
    	}
    }
	
	@Override
	protected void jTree1MousePressed(MouseEvent evt) {
		TreePath treePath = jTree1.getSelectionPath();
		if(treePath == null)
			return;
        Object[] path = treePath.getPath();
        String[] pathString = new String[path.length];
        for (int i = 0; i < path.length; i++) {
        	pathString[i] = path[i].toString();
		}
        con.openPage(pathString);
    }
	
	@Override
    protected void jButtonRenameActionPerformed(ActionEvent evt) {
		TreePath path = jTree1.getSelectionPath();
		if(path == null)
		{
			showMessage("Warnung", "Es wurde kein Verzeichnis ausgewaehlt.");
		}
		else
		{
			jTree1.setEnabled(false);
			String t = path.getPathComponent(path.getPathCount()-1).toString();
			jTextFieldNewName.setText(t);
			jDialogRename.setVisible(true);
		}
    }
	
	@Override
    protected void jButtonRenameNowActionPerformed(ActionEvent evt) {
    	Object[] treePath = jTree1.getSelectionPath().getPath();
    	String[] path = new String[treePath.length];
    	for (int i = 0; i < path.length; i++) {
			path[i] = treePath[i].toString();
		}
    	String newName = jTextFieldNewName.getText();
    	con.rename(path, newName);
    	jDialogRename.setVisible(false);
    	jTree1.setEnabled(true);
    	jTextFieldNewName.setText("");
    }
	
	@Override
	 protected void jMenuItemExitActionPerformed(ActionEvent evt) {
        con.exit();
    }
	
	@Override
	protected void jDialogRenameWindowClosing(WindowEvent evt) {
		jTree1.setEnabled(true);
    }
	
	@Override
    protected void jTree1KeyPressed(KeyEvent evt) {
		
		//cut
       if(evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_X)
       {
    	   TreePath[] paths = jTree1.getSelectionPaths();
    	   if(paths == null || paths.length == 0)
    		   return;
    	   String[][] pathStrings = new String[paths.length][];
    	   for (int i = 0; i < pathStrings.length; i++) {
    		   pathStrings[i] = new String[paths[i].getPathCount()];
    		   Object[] path = paths[i].getPath();
    		   for (int j = 0; j < path.length; j++) {
    			   pathStrings[i][j] = path[j].toString();
    		   }
    	   }
    	   con.cutDirectories(pathStrings);
       }//paste 
       else if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V)
       {
    	   TreePath path = jTree1.getSelectionPath();
    	   if(path == null)
    		   return;
    	   String [] stringPath = new String[path.getPathCount()];
    	   Object[] pathParts = path.getPath();
    	   for (int i = 0; i < pathParts.length; i++) {
    		   stringPath[i] = pathParts[i].toString();
    	   }
    	   con.pasteDirectories(stringPath);
       }
    }
	
	@Override
	protected void jToggleButtonCurserActionPerformed(ActionEvent evt) {
		con.setEditMod(EditMod.Curser);
		jToggleButtonCurser.transferFocus();
	}

	@Override
	protected void jToggleButtonTextActionPerformed(ActionEvent evt) {
		con.setEditMod(EditMod.Text);
	}
	
	@Override
    protected void jPanelPageMousePressed(MouseEvent evt) {
        if(drawTool != null)
        	throw new RuntimeException("Draw tool bereits gestartet");
        EditMod currentEditMod = con.getEditMod();
        switch(currentEditMod) 
        {
        case Curser:
        	break;
        case Text:
        	drawTool = new TextDrawTool(jPanelPage);
        	drawTool.drawNewContent(evt.getX(), evt.getY());
        	refreshPage();
        	break;
		default:
			break;
        }
    }
	
	@Override
    protected void jPanelPageMouseDragged(MouseEvent evt) {
        if(drawTool != null)
        {
        	drawTool.editNewContent(evt.getX(), evt.getY());
        	refreshPage();
        }
    }
	
	@Override
	protected void jPanelPageMouseReleased(MouseEvent evt) {
		if(drawTool != null)
		{
			Content c = drawTool.getNewContent(evt.getX(), evt.getY());
			con.createContent(c);
			Component comp = drawContent(c);
			comp.requestFocusInWindow();
			refreshPage();
			drawTool = null;
		}
    }

	//private methods --------------------------------------------------------
	private void refreshPage()
	{
		revalidate();
		repaint();
	}
	
	private Component drawContent(Content c)
	{
		Component comp = c.draw(jPanelPage);
		comp.setName(c.getNumber()+"");
		comp.addFocusListener(this);
		map.put(c, comp);
		return comp;
	}
}
