package bfrc.ast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

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
	 * @throws E
	 */
	protected boolean visit(Node n, Deque<BlockNode> stack) throws E {
		// nothing per default
		return true;
	}

	/**
	 * Called when entering a given block node. When this block is skipped
	 * instead, the according {@link #leave(BlockNode, Deque)} won't be called.
	 * 
	 * @param node
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

	protected void before() throws E {
		// nothing per default
	}

	protected void after() throws E {
		// nothing per default
	}
}
