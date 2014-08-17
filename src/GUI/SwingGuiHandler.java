package GUI;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pageData.Content;
import pageData.PageInformation;
import Tools.Tool;
import dataManagement.Tree;
import dataProcessing.Controller;

public class SwingGuiHandler extends SwingGui implements View {
	
	private static final long serialVersionUID = 1L;
	private Controller con;
	private Tree tree;
	
	public SwingGuiHandler(Controller con)
	{
		super();
		this.setVisible(true);
		this.con = con;
	}

	//interface view
	@Override
	public void refreshTree(Tree t) {
		this.tree = t;
		refreshBookDropDown();
		
		if(jComboBoxCurrentBook.getSelectedIndex() == -1)
		{
			jTree1.setVisible(false);
		}
		else
		{
			jTree1.setVisible(true);
			refreshTreeView(jComboBoxCurrentBook.getSelectedItem().toString());
		}
	}
	
	private void refreshBookDropDown()
	{
		jComboBoxBook.removeAllItems();
		jComboBoxBook2.removeAllItems();
		jComboBoxCurrentBook.removeAllItems();
		for(Tree t : tree.getChildren())
		{
			jComboBoxBook.addItem(t);
			jComboBoxBook2.addItem(t);
			jComboBoxCurrentBook.addItem(t);
		}
	}
	
	private void refreshTreeView(String bookName)
	{
		Tree book = tree.getChildren(bookName);
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
	public void showPage(Content[] content, PageInformation pageInfo) {
		// TODO Auto-generated method stub
		
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
		if(jComboBoxCurrentBook.getSelectedIndex() == -1)
			return;
		refreshTreeView(jComboBoxCurrentBook.getSelectedItem().toString());
	}
	
	@Override
	protected void jButtonCreateActionPerformed(ActionEvent evt)
	{
		con.createBook(jTextFieldBook.getText());
		jDialogCreate.setVisible(false);
	}
}
