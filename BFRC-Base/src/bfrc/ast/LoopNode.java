package bfrc.ast;

public final class LoopNode extends BlockNode {

	public LoopNode(int line, int offset) {
		super(NodeType.LOOP, line, offset);
	}
}
