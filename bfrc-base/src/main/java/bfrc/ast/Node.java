package bfrc.ast;

import java.util.Deque;

/**
 * Abstract super class for all Nodes of the AST.
 * 
 * @author oreissig
 */
public abstract class Node {
	/**
	 * Exact type of this node.
	 */
	public final NodeType type;
	/**
	 * Line number of the source file, where the structure represented by this
	 * node has been defined.
	 */
	public final int line;
	/**
	 * Offset in the source line, where the structure represented by this node
	 * has been defined.
	 */
	public final int offset;

	public Node(NodeType type, int line, int offset) {
		this.type = type;
		this.line = line;
		this.offset = offset;
	}

	/**
	 * @return Position in the source file, where the structure represented by
	 *         this node has been defined.
	 */
	public String position() {
		return line + ":" + offset;
	}

	@Override
	public String toString() {
		return position() + "-" + type;
	}

	/**
	 * This method implements the node-specific behaviour used by the default
	 * depth-first traversion mechanism.
	 * 
	 * @param visitor
	 *            to traverse the AST
	 * @param stack
	 *            nesting path during traversion
	 * @throws E
	 *             exception thrown by the visitor
	 */
	public <E extends Exception> void visit(TreeVisitor<E> visitor,
			Deque<BlockNode> stack) throws E {
		visitor.visit(this, stack);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + line;
		result = prime * result + offset;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* 
	 * Ignore line and offset for equals check. 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		if (type != other.type)
			return false;
		return true;
	}
}
