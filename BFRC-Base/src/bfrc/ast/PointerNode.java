package bfrc.ast;

public class PointerNode extends ChangeNode {

	public PointerNode(String position, int direction) {
		super(NodeType.POINTER, position, direction);
	}
}
