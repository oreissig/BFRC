package bfrc.ast;

public class ValueNode extends ChangeNode {

	public ValueNode(String position, int change) {
		super(NodeType.VALUE, position, change);
	}
}
