package bfrc.ast;

public class ValueNode extends ChangeNode {

	public ValueNode(String position, int change) {
		super(NodeType.VALUE, position, change);
	}

	public ValueNode(String position, int change, boolean absolute) {
		super(NodeType.VALUE, position, change, absolute);
	}
}
