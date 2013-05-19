package bfrc.ast;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class AbstractTreeVisitor<E extends Exception> implements TreeWalker<E> {

	public void work(Node root) throws E {
		walk(root, new ArrayDeque<BlockNode>());
	}

	private void walk(Node node, Deque<BlockNode> stack) throws E {
		visit(node, stack);

		if (node instanceof BlockNode) {
			BlockNode block = (BlockNode) node;
			stack.push(block);
			for (Node child : block.sub)
				walk(child, stack);
			stack.pop();
			leave(block, stack);
		} else {
		}
	}

	protected abstract void visit(Node node, Deque<BlockNode> stack) throws E;

	protected void leave(BlockNode node, Deque<BlockNode> stack) throws E {
		// do nothing per default
	}
}
