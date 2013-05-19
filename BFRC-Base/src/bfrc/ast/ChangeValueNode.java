package bfrc.ast;

public class ChangeValueNode extends Node {

	public final int change;

	public ChangeValueNode(String position, int change) {
		super(NodeType.VALUE, position);
		this.change = change;
	}

	@Override
	public String toString() {
		if (change < 0)
			return super.toString() + "(" + change + ")";
		else
			return super.toString() + "(+" + change + ")";
	}
}
