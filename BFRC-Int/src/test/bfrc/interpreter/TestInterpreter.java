package bfrc.interpreter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import bfrc.ast.RootNode;

class TestInterpreter extends Interpreter {
	private Iterator<Byte> inputs;
	private Iterator<Byte> outputs;

	public TestInterpreter(List<Byte> inputs, List<Byte> expectedOutputs) {
		this.inputs = inputs.iterator();
		this.outputs = expectedOutputs.iterator();
	}

	public TestInterpreter(List<Byte> expectedOutputs) {
		this(Collections.emptyList(), expectedOutputs);
	}

	public TestInterpreter() {
		this(Collections.emptyList());
	}

	public TestInterpreter(Byte[] inputs, Byte... expectedOutputs) {
		this(Arrays.asList(inputs), Arrays.asList(expectedOutputs));
	}

	public TestInterpreter(Byte... expectedOutputs) {
		this(Arrays.asList(expectedOutputs));
	}

	@Override
	public void work(RootNode root) {
		try {
			super.work(root);
		} catch (IOException e) {
			// cannot occur
			throw new RuntimeException(e);
		}
		assertFalse("unread inputs", inputs.hasNext());
		assertFalse("missing expected outputs", outputs.hasNext());
	}

	@Override
	byte read() {
		assertTrue("additional read", inputs.hasNext());
		return inputs.next();
	}

	@Override
	void write(byte value) {
		assertTrue("additional write: " + value, outputs.hasNext());
		assertEquals(outputs.next().byteValue(), value);
	}
}
