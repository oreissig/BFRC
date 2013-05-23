package bfrc.java;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import bfrc.ast.Node;
import bfrc.backend.Backend;

public class JavaClassBackend implements Backend {

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
