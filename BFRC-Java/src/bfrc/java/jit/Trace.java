package bfrc.java.jit;

import java.util.Arrays;

@SuppressWarnings("serial")
class Trace extends Throwable {

	public Trace(int blockID) {
		super("compile block " + blockID);

		// just print calls inside the JIT
		StackTraceElement[] stack = getStackTrace();

		int end = 1; // skip JITBackend.compile
		while (!stack[end++].getMethodName().equals("work"))
			;
		end--;

		StackTraceElement[] newStack = Arrays.copyOfRange(stack, 1, end);
		setStackTrace(newStack);
	}
}
