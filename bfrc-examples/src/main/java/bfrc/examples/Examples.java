package bfrc.examples;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class Examples {

	public static final Map<String, Example> EXAMPLES;

	// forbid instantiation
	private Examples() {
	}

	static {
		Map<String, Example> map = new HashMap<>();
		Consumer<String> add = name -> map.put(name, new Example(name));

		// TODO make excluded tests executable
		add.accept("bottles");
		//add.accept("factor");
		//add.accept("fibs");
		add.accept("fizzbuzz");
		add.accept("hello_world");
		//add.accept("life");
		//add.accept("mandelbrot");
		add.accept("squares");

		EXAMPLES = Collections.unmodifiableMap(map);
	}
}
