package bfrc.interpreter.test;

import bfrc.ast.RootNode;
import bfrc.interpreter.Interpreter;

class BenchmarkImpl implements Benchmark {

	private final Interpreter interpreter;
	private final TestIO io;
	private final StepCounter count;

	public BenchmarkImpl() {
		io = new TestIO();
		interpreter = new Interpreter(io);
		count = new StepCounter();
		interpreter.addListener(count);
	}

	@Override
	public void work(RootNode root) throws InterruptedException {
		io.reset();
		interpreter.work(root);
	}

	@Override
	public void setInput(String input) {
		io.setInputs(input);
	}

	@Override
	public String getOutput() {
		return io.getOutputString();
	}

	@Override
	public long getSteps() {
		return count.getSteps();
	}
}
