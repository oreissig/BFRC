package bfrc.ast;

public abstract class ChangeNode extends Node {

	/**
	 * Flag stating whether the {@link #change} is relative or absolute
	 */
	public final boolean absolute;
	/**
	 * Change implied by this node.
	 */
	public int change;

	public ChangeNode(NodeType type, int line, int offset, int change) {
		this(type, line, offset, change, false);
	}

	public ChangeNode(NodeType type, int line, int offset, int change, boolean absolute) {
		super(type, line, offset);
		this.change = change;
		this.absolute = absolute;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString())
		  .append('(');
		if (absolute)
			sb.append('=');
		if (change > 0)
			sb.append('+');
		sb.append(change)
		  .append(')');
		return sb.toString();
	}
}
