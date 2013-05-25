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
	 * Position in the source file, where the structure represented by this
	 * node has been defined.
	 */
	public final String position;

	public Node(NodeType type, String position) {
		this.type = type;
		this.position = position;
	}

	@Override
	public String toString() {
		return position + "-" + type;
	}
}
