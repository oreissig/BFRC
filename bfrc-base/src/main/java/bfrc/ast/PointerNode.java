package bfrc.ast;

public final class PointerNode extends ChangeNode {

	public PointerNode(int line, int offset, int direction) {
		// Pointer directions are always relative
		super(NodeType.POINTER, line, offset, direction, false);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (change > 1 || change < -1)
			sb.append(' ');
		if (change < 0)
			sb.append('<');
		else
			sb.append('>');
		if (change > 1)
			sb.append(change).append(' ');
		else if (change < -1)
			sb.append(-change).append(' ');
		return sb.toString();
	}
}
