package bfrc.ast;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public abstract class BlockNode extends Node {

	/**
	 * Child nodes contained in this Block.
	 */
	public final List<Node> sub = new ArrayList<>();

	public BlockNode(NodeType type, int line, int offset) {
		super(type, line, offset);
	}

	@Override
	public <E extends Exception> void visit(TreeVisitor<E> visitor, Deque<BlockNode> stack) throws E {
		super.visit(visitor, stack);

		visitor.enter(this, stack);
		stack.push(this);

		for (Node n : sub)
			n.visit(visitor, stack);

		stack.pop();
		visitor.leave(this, stack);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof BlockNode))
			return false;
		BlockNode other = (BlockNode) obj;
		if (!sub.equals(other.sub))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sub.forEach(n -> sb.append(n.toString()));
		return sb.toString();
	}
}
