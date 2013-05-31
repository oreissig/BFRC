package bfrc.ast;

public class OutputNode extends Node {

	public OutputNode(int line, int offset) {
		super(NodeType.OUTPUT, line, offset);
	}
}
