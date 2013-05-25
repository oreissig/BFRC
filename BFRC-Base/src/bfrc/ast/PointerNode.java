package bfrc.ast;

public class PointerNode extends ChangeNode {

	public PointerNode(String position, int direction) {
		// Pointer directions are always relative
		super(NodeType.POINTER, position, direction, false);
	}
}
