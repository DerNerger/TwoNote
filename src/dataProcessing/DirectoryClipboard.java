package dataProcessing;

public class DirectoryClipboard {
	private String[][] paths;
	
	public DirectoryClipboard(String[][] paths)
	{
		this.paths = paths;
	}

	public String[][] getPaths() {
		return paths;
	}

	public void setPaths(String[][] paths) {
		this.paths = paths;
	}
	
	public int getPathDepth()
	{
		return paths[0].length;
	}
}
