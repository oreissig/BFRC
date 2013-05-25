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

	public ChangeNode(NodeType type, String position, int change) {
		this(type, position, change, false);
	}

	public ChangeNode(NodeType type, String position, int change, boolean absolute) {
		super(type, position);
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
