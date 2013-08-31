package bfrc.ast;

public final class ValueNode extends ChangeNode {

	public ValueNode(int line, int offset, int change) {
		this(line, offset, change, false);
	}

	public ValueNode(int line, int offset, int change, boolean absolute) {
		super(NodeType.VALUE, line, offset, change, absolute);
	}
}
