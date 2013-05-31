package bfrc.backend;

import java.io.IOException;

import bfrc.ast.TreeWalker;

/**
 * Common interface for all BFRC backends.
 * 
 * @author oreissig
 */
public interface Backend extends TreeWalker<IOException> {

	/**
	 * The predefined size of a program's working memory array.
	 */
	public int MEM_SIZE = 9001;

	/**
	 * Specifies the output for this backend.
	 * 
	 * @param output backend-specific destination for code generation
	 * @throws IOException 
	 */
	public void setOutput(String output) throws IOException;

	/**
	 * Gets the default file extension for files created by this backend.
	 * 
	 * @return file extension
	 */
	public String getDefaultExtension();
}
