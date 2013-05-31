package bfrc.ast;

public class LoopNode extends BlockNode {

	public LoopNode(int line, int offset) {
		super(NodeType.LOOP, line, offset);
	}
}
