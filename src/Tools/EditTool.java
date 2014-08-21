package Tools;

import javax.swing.JToolBar;

import pageData.Content;

/**
 * @author Felix Kibellus
 * @version 1.0
 * */

public interface EditTool {
	void startEdit();
	Content stopEdit();
	int getContentNumber();
	void setToolbar(JToolBar toolbar);
}
