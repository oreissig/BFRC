package bfrc.ast;

import java.util.Deque;

/**
 * This interface implements the visitor pattern for traversing the AST in a
 * depth-first manner. Use the default {@link AbstractTreeVisitor} and only
 * override the needed methods for convenience.
 * 
 * @author oreissig
 * 
 * @param <E>
 *            exception type of this TreeVisitor
 */
public interface TreeVisitor<E extends Exception> {

	/**
	 * Visits the given Node.
	 * 
	 * @param n
	 *            node to visit
	 * @param stack
	 *            nesting path of current node
	 */
	public void visit(Node n, Deque<BlockNode> stack) throws E;

	/**
	 * Called when entering a given block node. When this block is skipped
	 * instead, the according {@link #leave(BlockNode, Deque) leave method} 
	 * won't be called.
	 * 
	 * @param block
	 *            to enter
	 * @param stack
	 *            before entering
	 */
	public void enter(BlockNode block, Deque<BlockNode> stack) throws E;

	/**
	 * Called when leaving the given block.
	 * 
	 * @param block
	 *            left
	 * @param stack
	 *            after leaving
	 */
	public void leave(BlockNode block, Deque<BlockNode> stack) throws E;

	/**
	 * Called at the very beginning of traversing the AST.
	 * 
	 * @param root
	 *            AST's root
	 */
	public void before(RootNode root) throws E;

	/**
	 * Called after the AST has been completely traversed.
	 * 
	 * @param root
	 *            AST's root
	 */
	public void after(RootNode root) throws E;
}
