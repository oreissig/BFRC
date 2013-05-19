package bfrc.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockNode extends Node {

	public final List<Node> sub = new ArrayList<>();

	public BlockNode(NodeType type, String position) {
		super(type, position);
	}
}
