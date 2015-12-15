package bfrc.interpreter.test;

import bfrc.backend.Backend;

public interface Benchmark extends Backend {

	void setInput(String input);

	String getOutput();

	long getSteps();
	
	public static Benchmark create() {
		return new BenchmarkImpl();
	}
}
