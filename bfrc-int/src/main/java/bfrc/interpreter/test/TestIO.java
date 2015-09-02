package bfrc.interpreter.test;

import java.util.ArrayList;
import java.util.List;

import bfrc.interpreter.InputOutput;

import com.google.common.primitives.Bytes;

public class TestIO implements InputOutput {
	private List<Byte> inputs = new ArrayList<>();
	private final List<Byte> outputs = new ArrayList<>();
	
	public List<Byte> getInputs() {
		return inputs;
	}

	public void setInputs(List<Byte> inputs) {
		setInputs(Bytes.toArray(inputs));
	}

	public void setInputs(byte... inputs) {
		List<Byte> list = Bytes.asList(inputs);
		// copy inputs because we need it modifiable
		this.inputs = new ArrayList<>(list);
	}

	public void setInputs(String inputs) {
		setInputs(inputs.getBytes());
	}

	public List<Byte> getOutputs() {
		return outputs;
	}

	public String getOutputString() {
		return new String(Bytes.toArray(outputs));
	}

	@Override
	public byte read() {
		System.out.println("read " + inputs.get(0));
		return inputs.remove(0);
	}

	@Override
	public void write(byte value) {
		System.out.println("write " + value);
		outputs.add(value);
	}
}
