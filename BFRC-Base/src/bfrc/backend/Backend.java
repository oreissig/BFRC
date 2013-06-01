package bfrc.backend;

import java.io.IOException;

import bfrc.ast.TreeWalker;

/**
 * Common interface for all BFRC backends.
 * 
 * @author oreissig
 */
// TODO do not use IOException for non-file backends
public interface Backend extends TreeWalker<IOException> {

	/**
	 * The predefined size of a program's working memory array.
	 */
	public int MEM_SIZE = 9001;
}
