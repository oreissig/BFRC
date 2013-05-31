package bfrc.java.jit;

public abstract class JITContext {

	public final byte[] mem;
	public int ptr = 0;

	protected JITContext() {
		this(9001);
	}

	protected JITContext(int memSize) {
		mem = new byte[memSize];
	}

	public abstract JITBlock compile(int blockID);
}
