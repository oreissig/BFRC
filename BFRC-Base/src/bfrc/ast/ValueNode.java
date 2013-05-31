package bfrc.ast;

public class ValueNode extends ChangeNode {

	public ValueNode(int line, int offset, int change) {
		super(NodeType.VALUE, line, offset, change);
	}

	public ValueNode(int line, int offset, int change, boolean absolute) {
		super(NodeType.VALUE, line, offset, change, absolute);
	}
}
