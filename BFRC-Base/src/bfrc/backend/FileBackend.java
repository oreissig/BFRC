package bfrc.backend;

import java.io.IOException;

/**
 * Common interface for all BFRC backends, that produce an output file.
 * 
 * @author oreissig
 */
public interface FileBackend extends Backend {

	/**
	 * Specifies the output for this backend.
	 * 
	 * @param output backend-specific destination for code generation
	 * @throws IOException 
	 */
	public void setOutput(String output) throws IOException;

	/**
	 * Gets the default file extension for files created by this backend.
	 * 
	 * @return file extension
	 */
	public String getDefaultExtension();
}
