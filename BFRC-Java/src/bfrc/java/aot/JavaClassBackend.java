package bfrc.java.aot;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import bfrc.ast.Node;
import bfrc.backend.FileBackend;

/**
 * This backend generates a Java class from the given AST and saves the resulting
 * code as a .class file to disk. This class file contains a
 * <tt>public static void main(String[] args)</tt> method, that exits the program
 * with an exit code equal to the pointer's last value.
 * 
 * @author oreissig
 */
public class JavaClassBackend implements FileBackend {

	public static final String main = "public static void main(String[] args) { System.exit(main()); }";
	private final JavassistHelper helper = new JavassistHelper();
	private String className;

	@Override
	public void setOutput(String output) throws IOException {
		this.className = output;
	}

	@Override
	public String getDefaultExtension() {
		// hack, provide a valid class name as extension
		return "Main";
	}

	@Override
	public void work(Node root) throws IOException {
		CtClass c = null;
		try {
			c = helper.create(className, root);
			CtMethod m = CtNewMethod.make(main, c);
			c.addMethod(m);
			c.writeFile();
		} catch (CannotCompileException | NotFoundException e) {
			throw new IOException(e);
		} finally {
			if (c != null)
				c.detach();
		}
	}
}
