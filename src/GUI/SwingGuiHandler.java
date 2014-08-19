package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(width, height));
		
		//JScrollPane pane = new JScrollPane();
		//pane.setViewportView(panel);
		jScrollPane2.setViewportView(panel);
		
		//jTabbedPane1.remove(jScrollPane2);
		//jTabbedPane1.addTab(pageName, pane);
		//jScrollPane2 = pane;
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
			String chapterName = jTextFieldPage.getText(); //TODO:rename to jTextFieldChapter
			String[] path = {bookName, chapterName};
			con.create(path);
			jTextFieldPage.setText("neues Kapitel");//TODO:rename to jTextFieldChapter
		}
		else if(jTabbedPaneCreate.getSelectedComponent() == jPanelCreatePage)
		{
			String bookName = (String) jComboBoxBook2.getSelectedItem();
			String chapterName = (String) jComboBoxChapter.getSelectedItem();
			String pageName = (String) jTextField1.getText();//TODO:rename to jTextFieldPage
			String[] path = {bookName, chapterName, pageName};
			con.create(path);
			jTextField1.setText("neue Seite"); //TODO:rename to jTextFieldPage
		}
		jDialogCreate.setVisible(false);//TODO:rename to jTextFieldPage
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
