package bfrc.ast;

public final class PointerNode extends ChangeNode {

	public PointerNode(int line, int offset, int direction) {
		// Pointer directions are always relative
		super(NodeType.POINTER, line, offset, direction, false);
	}
}
