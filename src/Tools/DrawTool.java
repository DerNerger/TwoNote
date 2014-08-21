package Tools;

import pageData.Content;

public interface DrawTool {
	void drawNewContent(int x, int y);
	void editNewContent(int x, int y);
	Content getNewContent(int x, int y);
}
