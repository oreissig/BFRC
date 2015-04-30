package bfrc.interpreter;

import java.io.IOException;

public interface InputOutput {

	/**
	 * Writes a single value.
	 * 
	 * @param value to be written
	 * @throws IOException in case of an error
	 */
	void write(byte value) throws IOException;

	/**
	 * Reads a single value.
	 * 
	 * @return value read
	 * @throws IOException in case of an error
	 */
	byte read() throws IOException;
}
