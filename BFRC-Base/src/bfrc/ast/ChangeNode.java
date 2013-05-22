package bfrc.ast;

public abstract class ChangeNode extends Node {

	public int change;

	public ChangeNode(NodeType type, String position, int change) {
		super(type, position);
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
