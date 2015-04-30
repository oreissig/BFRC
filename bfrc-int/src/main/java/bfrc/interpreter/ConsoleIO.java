package bfrc.interpreter;

import java.io.IOException;

public class ConsoleIO implements InputOutput {

	@Override
	public void write(byte value) {
		System.out.write(value);
	}

	@Override
	public byte read() throws IOException {
		return (byte) System.in.read();
	}
}
