package GUI;

import pageData.Content;
import pageData.PageInformation;

public interface View {
	void refreshBookComboBox();
	void refreshTree();
	
	void showPage(Content[] content, PageInformation pageInfo);
	void showMessage(String title, String msg);
	
	void removeContent(Content cont);
	void setVisible(boolean flag);
}
