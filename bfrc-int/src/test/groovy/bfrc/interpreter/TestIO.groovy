package bfrc.interpreter

import groovy.transform.CompileStatic

@CompileStatic
class TestIO implements InputOutput {
	List<Byte> inputs = []
	final List<Byte> outputs = []

	void setInputs(Byte... inputs) {
		this.inputs = inputs as List
	}

	void setInputs(String inputs) {
		setInputs(inputs.bytes)
	}

	@Override
	byte read() {
		inputs.remove(0)
	}

	@Override
	void write(byte value) {
		outputs << value
	}
}
