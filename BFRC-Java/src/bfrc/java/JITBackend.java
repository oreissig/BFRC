package bfrc.java;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import bfrc.ast.Node;
import bfrc.backend.Backend;

import javassist.CannotCompileException;
import javassist.CtClass;

public class JITBackend implements Backend {

	private static final String className = "Temp";
	private final JavassistHelper helper = new JavassistHelper();

	@Override
	public void setOutput(String output) {
		// nothing to do here
	}

	@Override
	public String getDefaultExtension() {
		return "Temp";
	}

	@Override
	public void work(Node root) throws IOException {
		Method m;
		try {
			CtClass clazz = helper.create(className, root);
			Class<?> compiled = clazz.toClass();
			m = compiled.getMethod("main");
		} catch (CannotCompileException | NoSuchMethodException e) {
			throw new IllegalArgumentException("invalid class", e);
		}
		// call static method
		try {
			m.invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException("exception calling compiled code", e);
		}
	}
}
