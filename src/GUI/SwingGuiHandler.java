package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

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
		refreshTree((String) jComboBoxCurrentBook.getSelectedItem());
	}

	//interface view
	private void refreshTree(String currentBook) {
		if(currentBook == null)
		{
			jTree1.setVisible(false);
			return;
		} else
			jTree1.setVisible(true);
		
		Tree tree = con.getTree();
		Tree book = tree.getChildren(currentBook);
		DefaultMutableTreeNode bookNode = new DefaultMutableTreeNode(book);
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
		jTree1.setModel(new DefaultTreeModel(bookNode));
	}
	
	@Override
	public void refreshBookComboBox()
	{
		jComboBoxBook.removeAllItems();
		jComboBoxBook2.removeAllItems();
		jComboBoxCurrentBook.removeAllItems();
		for(Tree t : con.getTree().getChildren())
		{
			jComboBoxBook.addItem(t.toString());
			jComboBoxBook2.addItem(t.toString());
			jComboBoxCurrentBook.addItem(t.toString());
		}
	}
	
	@Override
	public void openBook(String bookName) {
		jComboBoxCurrentBook.setSelectedItem(bookName);
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
	protected void jComboBoxCurrentBookActionPerformed(ActionEvent evt) 
	{
		refreshTree((String) jComboBoxCurrentBook.getSelectedItem());
	}
	
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
		String currentBook = (String) jComboBoxCurrentBook.getSelectedItem();
    	TreePath[] treePaths =  jTree1.getSelectionPaths();
    	for(TreePath treePath : treePaths)
    	{
    		Object[] path = treePath.getPath();
    		String[] pathString = new String[path.length];
    		for (int i = 0; i < pathString.length; i++) {
    			pathString[i] = path[i].toString();
			}
    		if(currentBook.equals(pathString[0]))//deleteCurrentBook
    			currentBook = null;
    		con.delete(pathString);
    	}
    	
    	if(currentBook != null) //go back to currentBook
    		jComboBoxCurrentBook.setSelectedItem(currentBook);
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
}
