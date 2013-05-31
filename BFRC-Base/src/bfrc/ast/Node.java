package bfrc.ast;

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
	 * Offset in the source line, where the structure represented by this
	 * node has been defined.
	 */
	public final int offset;

	public Node(NodeType type, int line, int offset) {
		this.type = type;
		this.line = line;
		this.offset = offset;
	}

	/**
	 * @return Position in the source file, where the structure represented by
	 * this node has been defined.
	 */
	public String position() {
		return line + ":" + offset;
	}

	@Override
	public String toString() {
		return position() + "-" + type;
	}
}
