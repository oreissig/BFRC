package bfrc.java.jit;

import java.util.Arrays;

@SuppressWarnings("serial")
class Trace extends Throwable {

	public Trace(String verb, int blockID) {
		super(verb + " block " + blockID);

		// just print calls inside the JIT
		StackTraceElement[] stack = getStackTrace();

		int end = 1; // skip JITBackend.compile
		while (!stack[end++].getClassName().equals(JITBackend.class.getName()))
			;
		end--;

		StackTraceElement[] newStack = Arrays.copyOfRange(stack, 1, end);
		setStackTrace(newStack);
	}
}
