package bfrc.backend;

import java.io.IOException;

import bfrc.ast.TreeWalker;

public interface Backend extends TreeWalker<IOException> {

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
