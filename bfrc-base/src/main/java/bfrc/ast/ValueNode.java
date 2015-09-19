package bfrc.ast;

public final class ValueNode extends ChangeNode {

	public ValueNode(int line, int offset, int change) {
		this(line, offset, change, false);
	}

	public ValueNode(int line, int offset, int change, boolean absolute) {
		super(NodeType.VALUE, line, offset, change, absolute);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (absolute) {
			sb.append(" =").append(change).append(' ');
		} else {
			if (change > 1 || change < -1)
				sb.append(' ');
			if (change < 0)
				sb.append('+');
			else
				sb.append('-');
			if (change > 1)
				sb.append(change).append(' ');
			else if (change < -1)
				sb.append(-change).append(' ');
		}
		return sb.toString();
	}
}
