package bfrc.interpreter.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bfrc.interpreter.InputOutput;

import com.google.common.primitives.Bytes;

class TestIO implements InputOutput {
	private final Logger log = LoggerFactory.getLogger(getClass());
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
		return Collections.unmodifiableList(outputs);
	}

	public String getOutputString() {
		return new String(Bytes.toArray(outputs));
	}

	@Override
	public byte read() {
		if (inputs.isEmpty()) {
			log.debug("nothing left to read, return 0");
			return 0;
		} else {
			log.debug("read {}", inputs.get(0));
			return inputs.remove(0);
		}
	}

	@Override
	public void write(byte value) {
		log.debug("write {}", value);
		outputs.add(value);
	}
	
	protected void reset() {
		outputs.clear();
	}
}
