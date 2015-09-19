package bfrc.examples;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Examples {

	public static final Set<Example> EXAMPLES;

	// forbid instantiation
	private Examples() {
	}

	static {
		Set<Example> set = new HashSet<>();

		// TODO make excluded tests executable
		set.add(new Example("bottles"));
		//set.add(new Example("factor"));
		//set.add(new Example("fibs"));
		set.add(new Example("fizzbuzz"));
		set.add(new Example("hello_world"));
		//set.add(new Example("life"));
		//set.add(new Example("mandelbrot"));
		set.add(new Example("squares"));

		EXAMPLES = Collections.unmodifiableSet(set);
	}
}
