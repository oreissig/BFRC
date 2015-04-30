package bfrc.backend;

import bfrc.ast.RootNode;

/**
 * Common interface for all BFRC backends.
 * 
 * @author oreissig
 */
public interface Backend {

	/**
	 * The predefined size of a program's working memory array.
	 */
	public int MEM_SIZE = 9001;

	/**
	 * Performs code generation for the AST specified.
	 * 
	 * @param root of AST
	 */
	public void work(RootNode root) throws Exception;
}
