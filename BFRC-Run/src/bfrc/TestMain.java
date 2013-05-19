package bfrc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class TestMain {
	public static void main(String[] args) throws IOException {
		String src = ">+++++++++[<++++++++>-]<.>+++++++[<++++>-]<+.+++++++..+++.[-]>++++++++[<++++>-]<.>+++++++++++[<+++++>-]<.>++++++++[<+++>-]<.+++.------.--------.[-]>++++++++[<++++>-]<+.[-]++++++++++.";

		File srcFile = createTempFile(src);

		String[] backends = { "dot", "java", "c", "jit" };
		String[] params = { null, srcFile.getAbsolutePath(), "test" };

		for (String config : backends) {
			// set config
			params[0] = "-" + config;
			// run
			Brainfuck.main(params);
		}
	}

	private static File createTempFile(String content) throws IOException {
		File f = File.createTempFile("bfrc", null);
		f.deleteOnExit();
		Writer w = new FileWriter(f);
		w.write(content);
		w.close();
		return f;
	}
}
