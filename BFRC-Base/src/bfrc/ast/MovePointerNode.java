package bfrc.ast;

public class MovePointerNode extends Node {

	public final int direction;

	public MovePointerNode(String position, int direction) {
		super(NodeType.POINTER, position);
		this.direction = direction;
	}

	@Override
	public String toString() {
		if (direction < 0)
			return super.toString() + "(" + direction + ")";
		else
			return super.toString() + "(+" + direction + ")";
	}
}
