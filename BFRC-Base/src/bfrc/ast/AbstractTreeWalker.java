package bfrc.ast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Common base class for {@link TreeWalker} implementations, dealing with all
 * the traversal logic. It provides methods, that may be overridden to react to
 * specific events.
 * 
 * @author oreissig
 *
 * @param <E> exception type of this {@link TreeWalker}
 */
public abstract class AbstractTreeWalker<E extends Exception> implements TreeWalker<E> {

	@Override
	public final void work(Node root) throws E {
		before();

		try {
			Deque<BlockNode> stack = new ArrayDeque<BlockNode>();
			if (visit(root, stack))
				checkRecurse(root, stack);
		} finally {
			after();
		}
	}

	private void work(Iterator<Node> nodes, Deque<BlockNode> stack) throws E {
		while (nodes.hasNext()) {
			Node n = nodes.next();
			if (visit(n, stack))
				checkRecurse(n, stack);
			else
				nodes.remove();
		}
	}

	private void checkRecurse(Node n, Deque<BlockNode> stack) throws E {
		if (n instanceof BlockNode) {
			BlockNode block = (BlockNode) n;
			if (enter(block, stack)) {
				stack.push(block);
				work(block.sub.iterator(), stack);
				stack.pop();
				leave(block, stack);
			}
		}
	}

	/**
	 * Visits the given Node.
	 * 
	 * @param n node to visit
	 * @param stack nesting path of current node
	 * @return true if node stays in the tree, false removes it
	 */
	protected boolean visit(Node n, Deque<BlockNode> stack) throws E {
		// nothing per default
		return true;
	}

	/**
	 * Called when entering a given block node. When this block is skipped
	 * instead, the according {@link #leave(BlockNode, Deque) leave method}
	 * won't be called.
	 * 
	 * @param block
	 * @param stack
	 * @return true when this subtree is to be entered, false will skip it
	 */
	protected boolean enter(BlockNode block, Deque<BlockNode> stack) throws E {
		// nothing per default
		return true;
	}

	/**
	 * Called when leaving the given block.
	 * 
	 * @param block
	 * @param stack
	 */
	protected void leave(BlockNode block, Deque<BlockNode> stack) throws E {
		// nothing per default
	}

	/**
	 * Called before traversing the given AST.
	 */
	protected void before() throws E {
		// nothing per default
	}

	/**
	 * Called after traversing the given AST, even in case of an exception being
	 * thrown.
	 */
	protected void after() throws E {
		// nothing per default
	}
}
