package bfrc.ast;

public final class LoopNode extends BlockNode {

	public LoopNode(int line, int offset) {
		super(NodeType.LOOP, line, offset);
	}

	@Override
	public String toString() {
		return " [" + super.toString() + "] ";
	}
}
