package bfrc.ast;

/**
 * Abstract super class for all Nodes of the AST.
 * 
 * @author oreissig
 */
public abstract class Node {
	public final NodeType type;
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
