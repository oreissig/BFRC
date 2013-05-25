package bfrc.ast;

/**
 * Common interface for all classes processing the AST. 
 * 
 * @author oreissig
 *
 * @param <E> some exception than may be thrown, specify
 * 		{@link RuntimeException} for none
 */
public interface TreeWalker<E extends Exception> {

	/**
	 * Traverse the AST starting at the given root {@link Node}.
	 * 
	 * @param root of tree
	 */
	public void work(Node root) throws E;
}
