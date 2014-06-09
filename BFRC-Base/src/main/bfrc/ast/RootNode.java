package bfrc.ast;

import java.util.ArrayDeque;
import java.util.Deque;

public final class RootNode extends BlockNode {

	public RootNode() {
		super(NodeType.ROOT, 1, 1);
	}

	/**
	 * This method starts a depth-first traversing of the AST defined by this
	 * root node.
	 * 
	 * @param visitor
	 *            to traverse the AST
	 * @throws E
	 *             exception thrown by the visitor
	 */
	public <E extends Exception> void visit(TreeVisitor<E> visitor) throws E {
		visit(visitor, new ArrayDeque<BlockNode>());
	}

	@Override
	public <E extends Exception> void visit(TreeVisitor<E> visitor,
			Deque<BlockNode> stack) throws E {
		visitor.before(this);
		super.visit(visitor, stack);
		visitor.after(this);
	}
}
