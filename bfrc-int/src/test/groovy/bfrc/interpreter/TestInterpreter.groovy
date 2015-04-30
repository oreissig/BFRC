package bfrc.interpreter

import groovy.transform.CompileStatic
import bfrc.ast.RootNode

@CompileStatic
class TestInterpreter extends Interpreter {
	List<Byte> inputs = []
	final List<Byte> outputs = []

	void setInputs(Byte... inputs) {
		this.inputs = inputs as List
	}

	void setInputs(String inputs) {
		setInputs(inputs.bytes)
	}

	@Override
	void work(RootNode root) {
		super.work(root)
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
