package bfrc.ast;

public class InputNode extends Node {

	public InputNode(int line, int offset) {
		super(NodeType.INPUT, line, offset);
	}
}
