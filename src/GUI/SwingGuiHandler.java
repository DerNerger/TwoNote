package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JEditorPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import pageData.Content;
import pageData.PageInformation;
import Tools.Tool;
import dataManagement.Tree;
import dataProcessing.Controller;

public class SwingGuiHandler extends SwingGui implements View {
	
	private static final long serialVersionUID = 1L;
	private Controller con;
	
	public SwingGuiHandler(Controller con)
	{
		super();
		this.setVisible(true);
		this.con = con;
	}

	//interface view
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
		jPanelPage.setBackground(Color.WHITE);
		jPanelPage.setLayout(null);
		jPanelPage.setPreferredSize(new Dimension(width, height));
		jTabbedPane1.remove(jScrollPanePage);
		jTabbedPane1.addTab(pageName, jScrollPanePage);
		
		//open the content
		//TODO: open
	}

	@Override
	public void showMessage(String title, String msg) {
		jLabelError.setText(title);
		jTextAreaErrorMessage.setText(msg); //TODO: erst bei OK beenden
		jDialogError.setVisible(true);
	}

	@Override
	public void showTool(Tool t) {
		// TODO Auto-generated method stub
		
	}
	
	//handler
	
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
    protected void jDialogRenameWindowClosed(WindowEvent evt) {
        jTree1.setEnabled(true);
        System.out.println("BLAAAAAAAAAA");
    }
}
