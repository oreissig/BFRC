package bfrc.interpreter.test;

import bfrc.ast.Node;
import bfrc.ast.RootNode;
import bfrc.interpreter.Interpreter;

public class BenchmarkImpl extends Interpreter implements Benchmark {

	private final TestIO io;
	private long steps = -1;

	public BenchmarkImpl() {
		super(new TestIO());
		// we know its TestIO
		this.io = (TestIO) super.io;
	}

	@Override
	public void work(RootNode root) {
		steps = 0;
		super.work(root);
	}

	@Override
	protected void visit(Node node) {
		steps++;
		super.visit(node);
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
		return steps;
	}
}
