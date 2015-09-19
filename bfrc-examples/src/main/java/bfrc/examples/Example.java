package bfrc.examples;

import java.io.InputStream;

public class Example {

	public final String name;
	public final String text;
	public final String input;
	public final String output;

	public Example(String name) {
		this.name = name;
		String prefix = "bfrc.examples." + name;
		text = read(prefix + ".bf");
		input = read(prefix + ".in");
		output = read(prefix + ".out");
	}

	@SuppressWarnings("resource")
	private static String read(String path) {
		InputStream is = Example.class.getResourceAsStream(path);
		if (is == null)
			return "";
		// taken from https://stackoverflow.com/a/5445161/1651991
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
