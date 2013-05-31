package bfrc.java.jit;

import bfrc.backend.Backend;

public abstract class JITContext {

	public final byte[] mem;
	public int ptr = 0;

	protected JITContext() {
		this(Backend.MEM_SIZE);
	}

	protected JITContext(int memSize) {
		mem = new byte[memSize];
	}

	public abstract JITBlock compile(int blockID);
}
