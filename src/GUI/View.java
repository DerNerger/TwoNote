package GUI;

import Tools.Tool;
import pageData.Content;
import pageData.PageInformation;
import dataManagement.Tree;

public interface View {
	void refreshBookComboBox();
	void refreshTree();
	void showPage(Content[] content, PageInformation pageInfo);
	void showMessage(String title, String msg);
	void showTool(Tool t);
	void setVisible(boolean flag);
}
